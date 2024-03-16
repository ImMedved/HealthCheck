package com.kukharev.health.check;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ErrorRepository extends JpaRepository<ErrorEntity, Long> {
    // Дополнительные методы для работы с базой данных могут быть добавлены здесь
}

