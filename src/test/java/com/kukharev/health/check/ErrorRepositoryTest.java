package com.kukharev.health.check;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@DataJpaTest
public class ErrorRepositoryTest {
    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ErrorRepository errorRepository;

    @Test
    public void testFindAll() {
        ErrorEntity error1 = new ErrorEntity();
        ErrorEntity error2 = new ErrorEntity();
        entityManager.persist(error1);
        entityManager.persist(error2);
        entityManager.flush();

        List<ErrorEntity> errors = errorRepository.findAll();

        assertThat(errors).hasSize(2);
        assertThat(errors).contains(error1, error2);
    }
}
