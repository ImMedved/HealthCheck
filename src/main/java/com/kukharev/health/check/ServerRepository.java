package com.kukharev.health.check;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ServerRepository extends JpaRepository<ServerEntity, Long> {
    // Дополнительные методы для работы с базой данных могут быть добавлены здесь
}
