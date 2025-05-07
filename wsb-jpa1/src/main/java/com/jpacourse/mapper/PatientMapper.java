package com.jpacourse.mapper;

import com.jpacourse.dto.PatientTO;
import com.jpacourse.dto.VisitTO;
import com.jpacourse.persistance.entity.PatientEntity;
import com.jpacourse.persistance.entity.VisitEntity;

import java.util.Collections;
import java.util.stream.Collectors;

public final class PatientMapper {

    public static PatientTO mapToTo(final PatientEntity patientEntity) {
        if (patientEntity == null) {
            return null;
        }

        PatientTO patientTO = new PatientTO();
        patientTO.setId(patientEntity.getId());
        patientTO.setFirstName(patientEntity.getFirstName());
        patientTO.setLastName(patientEntity.getLastName());
        patientTO.setTelephoneNumber(patientEntity.getTelephoneNumber());
        patientTO.setEmail(patientEntity.getEmail());
        patientTO.setPatientNumber(patientEntity.getPatientNumber());
        patientTO.setDateOfBirth(patientEntity.getDateOfBirth());
        patientTO.setInsured(patientEntity.isInsured());

        if (patientEntity.getVisitEntities() != null) {
            patientTO.setVisits(
                    patientEntity.getVisitEntities().stream()
                            .map(PatientMapper::mapVisitToTO)
                            .collect(Collectors.toList())
            );
        } else {
            patientTO.setVisits(Collections.emptyList());
        }

        return patientTO;
    }

    private static VisitTO mapVisitToTO(final VisitEntity visitEntity) {
        if (visitEntity == null) {
            return null;
        }

        VisitTO visitTO = new VisitTO();
        visitTO.setId(visitEntity.getId());
        visitTO.setDescription(visitEntity.getDescription());
        visitTO.setTime(visitEntity.getTime());

        if (visitEntity.getDoctor() != null) {
            visitTO.setDoctorFirstName(visitEntity.getDoctor().getFirstName());
            visitTO.setDoctorLastName(visitEntity.getDoctor().getLastName());
        }

        if (visitEntity.getMedicalTreatmentEntityCollection() != null) {
            visitTO.setTreatmentTypes(
                    visitEntity.getMedicalTreatmentEntityCollection().stream()
                            .map(t -> t.getType().name())
                            .collect(Collectors.toList())
            );
        } else {
            visitTO.setTreatmentTypes(Collections.emptyList());
        }

        return visitTO;
    }

    public static PatientEntity mapToEntity(final PatientTO patientTO) {
        if (patientTO == null) {
            return null;
        }

        PatientEntity patientEntity = new PatientEntity();
        patientEntity.setId(patientTO.getId());
        patientEntity.setFirstName(patientTO.getFirstName());
        patientEntity.setLastName(patientTO.getLastName());
        patientEntity.setTelephoneNumber(patientTO.getTelephoneNumber());
        patientEntity.setEmail(patientTO.getEmail());
        patientEntity.setPatientNumber(patientTO.getPatientNumber());
        patientEntity.setDateOfBirth(patientTO.getDateOfBirth());
        patientEntity.setInsured(patientTO.getInsured());

        return patientEntity;
    }
}
