package com.jpacourse.persistance.dao;

import com.jpacourse.persistance.entity.PatientEntity;
import jakarta.persistence.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class PatientOptimisticLockingTest {

    @Autowired
    private EntityManagerFactory entityManagerFactory;

    @Test
    public void shouldThrowOptimisticLockExceptionOnConcurrentUpdate() {
        Long patientId;

        try (EntityManager em = entityManagerFactory.createEntityManager()) {
            em.getTransaction().begin();
            PatientEntity patient = new PatientEntity();
            patient.setFirstName("Jan");
            patient.setLastName("Konfliktowy");
            patient.setPatientNumber("P999");
            patient.setTelephoneNumber("123123123");
            patient.setEmail("konflikt@demo.com");
            patient.setDateOfBirth(LocalDate.of(1990, 1, 1));
            patient.setInsured(true);
            em.persist(patient);
            em.getTransaction().commit();
            patientId = patient.getId();
        }

        EntityManager em1 = entityManagerFactory.createEntityManager();
        EntityManager em2 = entityManagerFactory.createEntityManager();

        PatientEntity p1 = em1.find(PatientEntity.class, patientId);
        PatientEntity p2 = em2.find(PatientEntity.class, patientId);

        em1.getTransaction().begin();
        p1.setEmail("pierwszy@update.com");
        em1.merge(p1);
        em1.getTransaction().commit();
        em1.close();

        em2.getTransaction().begin();
        p2.setEmail("drugi@update.com");

        RollbackException rollbackEx = assertThrows(RollbackException.class, () -> {
            em2.merge(p2);
            em2.getTransaction().commit();
        });

        Throwable cause = rollbackEx.getCause();
        assertInstanceOf(OptimisticLockException.class, cause, "OptimisticLockException");

        em2.close();
    }
}
