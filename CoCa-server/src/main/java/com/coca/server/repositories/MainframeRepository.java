package com.coca.server.repositories;

import com.coca.server.models.Mainframe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MainframeRepository extends JpaRepository<Mainframe, Long> {
}
