package com.jpacourse.persistance.dao;

import com.jpacourse.persistance.entity.PatientEntity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface PatientDao {

    void addVisitToPatient(Long patientId, Long doctorId, LocalDateTime time, String description);

    List<PatientEntity> findByLastName(String lastName);

    List<PatientEntity> findPatientsWithMoreThanXVisits(long visitCount);

    List<PatientEntity> findPatientsBornBefore(LocalDate date);

    PatientEntity findOne(Long id);
}
