package com.cthulhu.repositories;

import com.cthulhu.models.BladeRunner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BladeRunnerRepository extends JpaRepository<BladeRunner, Long> {
    BladeRunner findByName(String name);
}
