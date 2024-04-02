package com.kukharev.health.check;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ServerRepository extends JpaRepository<ServerEntity, Long> {
    ServerEntity findByUrl(String url);
}
