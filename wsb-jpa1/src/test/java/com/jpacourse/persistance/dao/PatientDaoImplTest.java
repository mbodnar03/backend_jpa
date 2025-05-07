package com.jpacourse.persistance.dao;

import com.jpacourse.persistance.entity.DoctorEntity;
import com.jpacourse.persistance.entity.PatientEntity;
import com.jpacourse.persistance.entity.VisitEntity;
import com.jpacourse.persistance.enums.Specialization;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class PatientDaoImplTest {

    @Autowired
    private PatientDao patientDao;

    @PersistenceContext
    private EntityManager entityManager;

    @Test
    @Transactional
    public void testAddVisitToPatient() {
        DoctorEntity doctor = new DoctorEntity();
        doctor.setFirstName("Test");
        doctor.setLastName("Doctor");
        doctor.setDoctorNumber("D123");
        doctor.setTelephoneNumber("123456789");
        doctor.setSpecialization(Specialization.SURGEON);
        entityManager.persist(doctor);
        Long doctorId = doctor.getId();

        PatientEntity patient = new PatientEntity();
        patient.setFirstName("Test");
        patient.setLastName("Patient");
        patient.setTelephoneNumber("987654321");
        patient.setEmail("test@patient.com");
        patient.setPatientNumber("P456");
        patient.setDateOfBirth(LocalDate.of(1990, 1, 1));
        patient.setInsured(true);
        entityManager.persist(patient);
        Long patientId = patient.getId();

        LocalDateTime time = LocalDateTime.of(2025, 1, 1, 10, 0);
        String description = "Testowa wizyta";

        patientDao.addVisitToPatient(patientId, doctorId, time, description);

        PatientEntity refreshed = entityManager.find(PatientEntity.class, patientId);
        List<VisitEntity> visits = refreshed.getVisitEntities().stream().toList();

        assertThat(visits).hasSize(1);
        VisitEntity visit = visits.get(0);
        assertThat(visit.getDescription()).isEqualTo(description);
        assertThat(visit.getTime()).isEqualTo(time);
        assertThat(visit.getDoctor().getId()).isEqualTo(doctorId);
    }

    @Test
    @Transactional
    public void shouldFindPatientsByLastName() {
        PatientEntity patient = new PatientEntity();
        patient.setFirstName("Anna");
        patient.setLastName("Nowak");
        patient.setTelephoneNumber("123456789");
        patient.setEmail("anna@nowak.com");
        patient.setPatientNumber("AN001");
        patient.setDateOfBirth(LocalDate.of(1995, 5, 5));
        patient.setInsured(true);
        entityManager.persist(patient);

        List<PatientEntity> result = patientDao.findByLastName("Nowak");
        assertFalse(result.isEmpty());
        assertEquals("Nowak", result.get(0).getLastName());
    }

    @Test
    @Transactional
    public void shouldFindPatientsWithMoreThanXVisits() {
        DoctorEntity doctor = new DoctorEntity();
        doctor.setFirstName("Jan");
        doctor.setLastName("Kowalski");
        doctor.setDoctorNumber("DOC001");
        doctor.setTelephoneNumber("111222333");
        doctor.setSpecialization(Specialization.SURGEON);
        entityManager.persist(doctor);

        PatientEntity patient = new PatientEntity();
        patient.setFirstName("Kasia");
        patient.setLastName("Wielka");
        patient.setTelephoneNumber("987654321");
        patient.setEmail("kasia@wielka.com");
        patient.setPatientNumber("KW001");
        patient.setDateOfBirth(LocalDate.of(1985, 3, 10));
        patient.setInsured(true);
        entityManager.persist(patient);
        Long patientId = patient.getId();

        LocalDateTime now = LocalDateTime.now();
        patientDao.addVisitToPatient(patientId, doctor.getId(), now, "wizyta 1");
        patientDao.addVisitToPatient(patientId, doctor.getId(), now.plusHours(1), "wizyta 2");

        List<PatientEntity> result = patientDao.findPatientsWithMoreThanXVisits(1);
        assertFalse(result.isEmpty());
        assertTrue(result.stream().anyMatch(p -> p.getId().equals(patientId)));
    }

    @Test
    @Transactional
    public void shouldFindPatientsBornBeforeDate() {
        PatientEntity patient = new PatientEntity();
        patient.setFirstName("Zosia");
        patient.setLastName("Mala");
        patient.setTelephoneNumber("000111222");
        patient.setEmail("zosia@mala.com");
        patient.setPatientNumber("ZM001");
        patient.setDateOfBirth(LocalDate.of(1980, 1, 1));
        patient.setInsured(false);
        entityManager.persist(patient);

        LocalDate date = LocalDate.of(1990, 1, 1);
        List<PatientEntity> result = patientDao.findPatientsBornBefore(date);
        assertFalse(result.isEmpty());
        assertTrue(result.stream().anyMatch(p -> p.getLastName().equals("Mala")));
    }

    @Test
    @Transactional
    public void shouldLoadPatientWithVisits() {
        DoctorEntity doctor = new DoctorEntity();
        doctor.setFirstName("Obserwator");
        doctor.setLastName("Lekarz");
        doctor.setDoctorNumber("OBS001");
        doctor.setTelephoneNumber("123000000");
        doctor.setSpecialization(Specialization.SURGEON);
        entityManager.persist(doctor);

        PatientEntity patient = new PatientEntity();
        patient.setFirstName("Obserwator");
        patient.setLastName("Pacjent");
        patient.setTelephoneNumber("123456789");
        patient.setEmail("observer@patient.com");
        patient.setPatientNumber("OBS123");
        patient.setDateOfBirth(LocalDate.of(1990, 1, 1));
        patient.setInsured(true);
        entityManager.persist(patient);

        patientDao.addVisitToPatient(patient.getId(), doctor.getId(), LocalDateTime.now(), "Pierwsza");
        patientDao.addVisitToPatient(patient.getId(), doctor.getId(), LocalDateTime.now().plusHours(1), "Druga");

        entityManager.flush();
        entityManager.clear();

        PatientEntity loaded = entityManager.find(PatientEntity.class, patient.getId());

        System.out.println("Liczba wizyt: " + loaded.getVisitEntities().size());
    }

}