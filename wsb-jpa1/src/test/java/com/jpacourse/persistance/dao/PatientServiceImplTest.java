package com.jpacourse.service;

import com.jpacourse.dto.PatientTO;
import com.jpacourse.persistance.dao.PatientDao;
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
public class PatientServiceImplTest {

    @Autowired
    private PatientService patientService;

    @Autowired
    private PatientDao patientDao;

    @PersistenceContext
    private EntityManager entityManager;

    private Long createdPatientId;
    private Long createdDoctorId;

    @BeforeEach
    @Transactional
    public void setUp() {
        DoctorEntity doctor = new DoctorEntity();
        doctor.setFirstName("Tomasz");
        doctor.setLastName("Doctor");
        doctor.setDoctorNumber("5");
        doctor.setTelephoneNumber("111111111");
        doctor.setSpecialization(Specialization.GP);
        entityManager.persist(doctor);
        createdDoctorId = doctor.getId();

        PatientEntity patient = new PatientEntity();
        patient.setFirstName("Anna");
        patient.setLastName("Patient");
        patient.setTelephoneNumber("222222222");
        patient.setEmail("anna@example.com");
        patient.setPatientNumber("6");
        patient.setDateOfBirth(LocalDate.of(1990, 1, 1));
        patient.setInsured(true);
        entityManager.persist(patient);
        createdPatientId = patient.getId();

        VisitEntity visit = new VisitEntity();
        visit.setTime(LocalDateTime.now());
        visit.setDescription("Konsultacja");
        visit.setDoctor(doctor);
        visit.setPatient(patient);
        entityManager.persist(visit);

        entityManager.flush();
    }

    @Test
    @Transactional
    public void testShouldDeletePatientAndCascadeVisitsButNotDoctors() {
        // when
        patientDao.delete(createdPatientId);
        entityManager.flush();
        entityManager.clear();

        // then
        PatientEntity deletedPatient = entityManager.find(PatientEntity.class, createdPatientId);
        assertThat(deletedPatient).isNull();

        // then
        List<VisitEntity> remainingVisits = entityManager
                .createQuery("SELECT v FROM VisitEntity v", VisitEntity.class)
                .getResultList();
        assertThat(remainingVisits).isEmpty();

        // then
        DoctorEntity remainingDoctor = entityManager.find(DoctorEntity.class, createdDoctorId);
        assertThat(remainingDoctor).isNotNull();
    }

    @Test
    @Transactional
    public void testShouldReturnPatientWithProperFields() {
        // when
        PatientTO patientTO = patientService.findById(createdPatientId);

        // then
        assertThat(patientTO).isNotNull();
        assertThat(patientTO.getFirstName()).isEqualTo("Anna");
        assertThat(patientTO.getLastName()).isEqualTo("Nowak");
        assertThat(patientTO.getTelephoneNumber()).isEqualTo("222222222");
        assertThat(patientTO.getEmail()).isEqualTo("anna@example.com");
        assertThat(patientTO.getPatientNumber()).isEqualTo("6");
        assertThat(patientTO.getDateOfBirth()).isEqualTo(LocalDate.of(1990, 1, 1));
        assertThat(patientTO.getInsured()).isTrue();
    }
}