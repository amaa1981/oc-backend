package com.edgeterminal.backend.service;

import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import java.lang.management.ManagementFactory;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Service
public class SystemConfigService {

    private final Map<Long, Map<String, Object>> sysConfigs = new ConcurrentHashMap<>();
    private final Map<Long, Map<String, Object>> networkConfigs = new ConcurrentHashMap<>();
    private final Map<String, List<Map<String, Object>>> pushLists = new ConcurrentHashMap<>();
    private final Map<String, Map<Long, Map<String, Object>>> pushById = new ConcurrentHashMap<>();
    private final List<Map<String, Object>> servicePackages = Collections.synchronizedList(new ArrayList<>());
    private final AtomicLong pushIdSeq = new AtomicLong(1);
    private String locale = "en";

    @PostConstruct
    void init() {
        seedSysConfig(1L);
        seedNetwork(1L, "eth0");
        seedNetwork(2L, "eth1");
        seedPublicAccount();
        pushLists.put("http", new ArrayList<>());
        pushLists.put("phone", new ArrayList<>());
        pushLists.put("email", new ArrayList<>());
        Map<Long, Map<String, Object>> mqtt = new ConcurrentHashMap<>();
        mqtt.put(1L, defaultMqtt());
        pushById.put("mqtt", mqtt);
        pushLists.put("audio", new ArrayList<>());
    }

    private void seedSysConfig(Long id) {
        Map<String, Object> cfg = new HashMap<>();
        cfg.put("id", id);
        cfg.put("delAlarmTime", "30");
        cfg.put("delAlarmStatus", "1");
        cfg.put("delSysTime", "90");
        cfg.put("delSysStatus", "1");
        cfg.put("heartbeatPushTime", "60");
        cfg.put("isHeartbeat", "0");
        cfg.put("alarmSocketStatus", "1");
        cfg.put("boxCode", "EDGE-001");
        cfg.put("remarks3", "");
        sysConfigs.put(id, cfg);
    }

    private void seedNetwork(Long id, String card) {
        Map<String, Object> cfg = new HashMap<>();
        cfg.put("id", id);
        cfg.put("internetCard", card);
        cfg.put("ip", "192.168.1.100");
        cfg.put("netMask", "255.255.255.0");
        cfg.put("defaultGateway", "192.168.1.1");
        cfg.put("dns1", "8.8.8.8");
        cfg.put("dns2", "8.8.4.4");
        cfg.put("mac", "00:11:22:33:44:55");
        cfg.put("dhcp", "0");
        networkConfigs.put(id, cfg);
    }

    private Map<String, Object> defaultMqtt() {
        Map<String, Object> cfg = new HashMap<>();
        cfg.put("id", 1L);
        cfg.put("connectInfo", "tcp://127.0.0.1:1883");
        cfg.put("userName", "admin");
        cfg.put("password", "");
        cfg.put("qos", "1");
        cfg.put("clientId", "edge-terminal");
        cfg.put("topic", "alarm/events");
        cfg.put("status", "1");
        cfg.put("connectStatus", "1");
        cfg.put("alarmType", "0");
        return cfg;
    }

    private void seedPublicAccount() {
        Map<String, Object> cfg = new HashMap<>();
        cfg.put("id", 1L);
        cfg.put("pushAccount", List.of(""));
        cfg.put("httpUrl", "");
        cfg.put("alarmType", "0");
        cfg.put("status", "1");
        pushById.computeIfAbsent("public", k -> new ConcurrentHashMap<>()).put(1L, cfg);
    }

    public Map<String, Object> getSysConfig(Long id) {
        return copy(sysConfigs.getOrDefault(id, sysConfigs.get(1L)));
    }

    public void updateSysConfig(Map<String, Object> body) {
        Object idObj = body.get("id");
        long id = idObj == null ? 1L : Long.parseLong(String.valueOf(idObj));
        Map<String, Object> existing = sysConfigs.getOrDefault(id, new HashMap<>());
        existing.putAll(body);
        existing.put("id", id);
        sysConfigs.put(id, existing);
    }

    public Map<String, Object> getNetwork(Long id) {
        return copy(networkConfigs.getOrDefault(id, networkConfigs.get(1L)));
    }

    public void updateNetwork(Map<String, Object> body) {
        Object idObj = body.get("id");
        long id = idObj == null ? 1L : Long.parseLong(String.valueOf(idObj));
        Map<String, Object> existing = networkConfigs.getOrDefault(id, new HashMap<>());
        existing.putAll(body);
        existing.put("id", id);
        networkConfigs.put(id, existing);
    }

    public Map<String, Object> listPush(String type, int pageNum, int pageSize) {
        List<Map<String, Object>> all = new ArrayList<>(pushLists.getOrDefault(type, List.of()));
        return paginate(all, pageNum, pageSize);
    }

    public Map<String, Object> getPush(String type, Long id) {
        if ("mqtt".equals(type) || "public".equals(type)) {
            Map<Long, Map<String, Object>> map = pushById.get(type);
            return map == null ? new HashMap<>() : copy(map.get(id));
        }
        return pushLists.getOrDefault(type, List.of()).stream()
                .filter(r -> Objects.equals(id, asLong(r.get("id"))))
                .findFirst()
                .map(this::copy)
                .orElse(new HashMap<>());
    }

    public Map<String, Object> addPush(String type, Map<String, Object> body) {
        long id = pushIdSeq.getAndIncrement();
        body.put("id", id);
        if ("mqtt".equals(type) || "public".equals(type)) {
            pushById.computeIfAbsent(type, k -> new ConcurrentHashMap<>()).put(id, new HashMap<>(body));
        } else {
            pushLists.computeIfAbsent(type, k -> Collections.synchronizedList(new ArrayList<>())).add(new HashMap<>(body));
        }
        return copy(body);
    }

    public void updatePush(String type, Map<String, Object> body) {
        long id = Long.parseLong(String.valueOf(body.get("id")));
        if ("mqtt".equals(type) || "public".equals(type)) {
            Map<Long, Map<String, Object>> map = pushById.computeIfAbsent(type, k -> new ConcurrentHashMap<>());
            Map<String, Object> existing = map.getOrDefault(id, new HashMap<>());
            existing.putAll(body);
            existing.put("id", id);
            map.put(id, existing);
        } else {
            List<Map<String, Object>> list = pushLists.computeIfAbsent(type, k -> Collections.synchronizedList(new ArrayList<>()));
            for (int i = 0; i < list.size(); i++) {
                if (Objects.equals(asLong(list.get(i).get("id")), id)) {
                    Map<String, Object> merged = new HashMap<>(list.get(i));
                    merged.putAll(body);
                    merged.put("id", id);
                    list.set(i, merged);
                    return;
                }
            }
            body.put("id", id);
            list.add(body);
        }
    }

    public void deletePush(String type, Long id) {
        if ("mqtt".equals(type) || "public".equals(type)) {
            Map<Long, Map<String, Object>> map = pushById.get(type);
            if (map != null) {
                map.remove(id);
            }
        } else {
            pushLists.getOrDefault(type, List.of()).removeIf(r -> Objects.equals(asLong(r.get("id")), id));
        }
    }

    public Map<String, Object> getMqtt(Long id) {
        return getPush("mqtt", id);
    }

    public void updateMqtt(Map<String, Object> body) {
        updatePush("mqtt", body);
    }

    public Map<String, Object> getPublicAccount(Long id) {
        return getPush("public", id);
    }

    public void updatePublicAccount(Map<String, Object> body) {
        updatePush("public", body);
    }

    public Map<String, Object> listServicePackages(int pageNum, int pageSize) {
        return paginate(new ArrayList<>(servicePackages), pageNum, pageSize);
    }

    public Map<String, Object> getServicePackage(Long id) {
        return servicePackages.stream()
                .filter(r -> Objects.equals(asLong(r.get("id")), id))
                .findFirst()
                .map(this::copy)
                .orElse(new HashMap<>());
    }

    public Map<String, Object> processListResponse() {
        List<Map<String, Object>> processes = List.of(
                processRow("Analysis Server", "analysis-server", 0),
                processRow("Stream Server", "stream-server", 0)
        );
        Map<String, Object> result = new HashMap<>();
        result.put("processList", processes);
        Map<String, Object> wrapper = new HashMap<>();
        wrapper.put("code", 0);
        wrapper.put("result", result);
        return wrapper;
    }

    private Map<String, Object> processRow(String name, String processName, int state) {
        Map<String, Object> row = new HashMap<>();
        row.put("name", name);
        row.put("processName", processName);
        row.put("startTime", Instant.now().toString());
        row.put("state", state);
        return row;
    }

    public Map<String, Object> processActionResponse() {
        Map<String, Object> wrapper = new HashMap<>();
        wrapper.put("code", 0);
        wrapper.put("result", "ok");
        return wrapper;
    }

    public Map<String, Object> serverInfo() {
        int cores = Runtime.getRuntime().availableProcessors();
        Map<String, Object> cpu = new HashMap<>();
        cpu.put("cpuNum", cores);
        cpu.put("used", 18.5);
        cpu.put("sys", 8.2);
        cpu.put("free", 73.3);

        long totalMem = Runtime.getRuntime().maxMemory();
        long freeMem = Runtime.getRuntime().freeMemory();
        long usedMem = totalMem - freeMem;
        Map<String, Object> mem = new HashMap<>();
        mem.put("total", totalMem / (1024 * 1024));
        mem.put("used", usedMem / (1024 * 1024));
        mem.put("free", freeMem / (1024 * 1024));
        mem.put("usage", totalMem == 0 ? 0 : (usedMem * 100.0 / totalMem));

        Map<String, Object> sys = new HashMap<>();
        sys.put("computerName", "edge-terminal");
        sys.put("osName", System.getProperty("os.name"));
        sys.put("osArch", System.getProperty("os.arch"));
        sys.put("userDir", System.getProperty("user.dir"));

        Map<String, Object> jvm = new HashMap<>();
        jvm.put("name", ManagementFactory.getRuntimeMXBean().getVmName());
        jvm.put("version", System.getProperty("java.version"));
        jvm.put("startTime", ManagementFactory.getRuntimeMXBean().getStartTime());
        jvm.put("runTime", ManagementFactory.getRuntimeMXBean().getUptime());
        jvm.put("total", totalMem / (1024 * 1024));
        jvm.put("max", totalMem / (1024 * 1024));
        jvm.put("free", freeMem / (1024 * 1024));
        jvm.put("usage", totalMem == 0 ? 0 : (usedMem * 100.0 / totalMem));

        Map<String, Object> server = new HashMap<>();
        server.put("cpu", cpu);
        server.put("mem", mem);
        server.put("sys", sys);
        server.put("jvm", jvm);
        return server;
    }

    public Map<String, Object> javaConfig() {
        Map<String, Object> cfg = new HashMap<>();
        cfg.put("javaVersion", System.getProperty("java.version"));
        cfg.put("javaHome", System.getProperty("java.home"));
        return cfg;
    }

    public String getLocale() {
        return locale;
    }

    public void setLocale(String value) {
        if (value != null && !value.isBlank()) {
            locale = value;
        }
    }

    public Map<String, Object> paginate(List<Map<String, Object>> all, int pageNum, int pageSize) {
        int from = Math.max(0, (pageNum - 1) * pageSize);
        int to = Math.min(all.size(), from + pageSize);
        List<Map<String, Object>> page = from >= all.size() ? List.of() : all.subList(from, to);
        Map<String, Object> data = new HashMap<>();
        data.put("records", page.stream().map(this::copy).collect(Collectors.toList()));
        data.put("rows", data.get("records"));
        data.put("totalCount", all.size());
        data.put("total", all.size());
        return data;
    }

    public Map<String, Object> listLogs(int pageNum, int pageSize) {
        return paginate(List.of(), pageNum, pageSize);
    }

    public Map<String, Object> listLogsAsRows(int pageNum, int pageSize) {
        Map<String, Object> page = listLogs(pageNum, pageSize);
        Map<String, Object> out = new HashMap<>();
        out.put("rows", page.get("records"));
        out.put("total", page.get("totalCount"));
        return out;
    }

    private Map<String, Object> copy(Map<String, Object> source) {
        return source == null ? new HashMap<>() : new HashMap<>(source);
    }

    private Long asLong(Object value) {
        if (value == null) {
            return null;
        }
        if (value instanceof Number n) {
            return n.longValue();
        }
        return Long.parseLong(String.valueOf(value));
    }
}
