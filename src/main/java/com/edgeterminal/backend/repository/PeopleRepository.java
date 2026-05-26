package com.edgeterminal.backend.repository;

import com.edgeterminal.backend.entity.People;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface PeopleRepository extends JpaRepository<People, Long> {
    List<People> findByNameContaining(String name);
}
