package com.jpacourse.persistance.dao;

import com.jpacourse.persistance.entity.DoctorEntity;
import com.jpacourse.persistance.entity.PatientEntity;
import com.jpacourse.persistance.entity.VisitEntity;
import com.jpacourse.persistance.enums.Specialization;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class PatientDaoImplTest {

    @Autowired
    private PatientDao patientDao;

    @PersistenceContext
    private EntityManager entityManager;

    private Long patientId;
    private Long doctorId;

    @BeforeEach
    @Transactional
    public void setUp() {
        DoctorEntity doctor = new DoctorEntity();
        doctor.setFirstName("Test");
        doctor.setLastName("Doctor");
        doctor.setDoctorNumber("D123");
        doctor.setTelephoneNumber("123456789");
        doctor.setSpecialization(Specialization.SURGEON);
        entityManager.persist(doctor);
        doctorId = doctor.getId();

        PatientEntity patient = new PatientEntity();
        patient.setFirstName("Test");
        patient.setLastName("Patient");
        patient.setTelephoneNumber("987654321");
        patient.setEmail("test@patient.com");
        patient.setPatientNumber("P456");
        patient.setDateOfBirth(LocalDate.of(1990, 1, 1));
        patient.setInsured(true);
        entityManager.persist(patient);
        patientId = patient.getId();
    }

    @Test
    @Transactional
    public void testAddVisitToPatient() {
        LocalDateTime time = LocalDateTime.of(2025, 1, 1, 10, 0);
        String description = "Testowa wizyta";

        patientDao.addVisitToPatient(patientId, doctorId, time, description);

        PatientEntity patient = entityManager.find(PatientEntity.class, patientId);
        List<VisitEntity> visits = patient.getVisitEntities().stream().toList();

        assertThat(visits).hasSize(1);
        VisitEntity visit = visits.get(0);
        assertThat(visit.getDescription()).isEqualTo(description);
        assertThat(visit.getTime()).isEqualTo(time);
        assertThat(visit.getDoctor().getId()).isEqualTo(doctorId);
    }
}