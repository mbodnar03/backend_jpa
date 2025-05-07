package com.jpacourse.persistance.dao.impl;

import com.jpacourse.persistance.dao.PatientDao;
import com.jpacourse.persistance.entity.DoctorEntity;
import com.jpacourse.persistance.entity.PatientEntity;
import com.jpacourse.persistance.entity.VisitEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
@Transactional
public class PatientDaoImpl extends AbstractDao<PatientEntity, Long> implements PatientDao {

    @PersistenceContext
    private EntityManager entityManager;

    public PatientDaoImpl() {
        super(PatientEntity.class);
    }

    @Override
    public void addVisitToPatient(Long patientId, Long doctorId, LocalDateTime time, String description) {
        PatientEntity patient = entityManager.find(PatientEntity.class, patientId);
        DoctorEntity doctor = entityManager.find(DoctorEntity.class, doctorId);

        VisitEntity visit = new VisitEntity();
        visit.setTime(time);
        visit.setDescription(description);
        visit.setDoctor(doctor);
        visit.setPatient(patient);

        patient.getVisitEntities().add(visit);
        entityManager.merge(patient);
    }

    @Override
    public List<PatientEntity> findByLastName(String lastName) {
        return entityManager.createQuery(
                        "SELECT p FROM PatientEntity p WHERE p.lastName = :lastName", PatientEntity.class)
                .setParameter("lastName", lastName)
                .getResultList();
    }

    @Override
    public List<PatientEntity> findPatientsWithMoreThanXVisits(long visitCount) {
        return entityManager.createQuery(
                        "SELECT p FROM PatientEntity p WHERE SIZE(p.visitEntities) > :visitCount", PatientEntity.class)
                .setParameter("visitCount", visitCount)
                .getResultList();
    }

    @Override
    public List<PatientEntity> findPatientsBornBefore(LocalDate date) {
        return entityManager.createQuery(
                        "SELECT p FROM PatientEntity p WHERE p.dateOfBirth < :date", PatientEntity.class)
                .setParameter("date", date)
                .getResultList();
    }
}
