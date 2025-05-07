package com.jpacourse.service.impl;

import com.jpacourse.dto.PatientTO;
import com.jpacourse.mapper.PatientMapper;
import com.jpacourse.persistance.dao.PatientDao;
import com.jpacourse.persistance.entity.PatientEntity;
import com.jpacourse.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class PatientServiceImpl implements PatientService {

    private final PatientDao patientDao;

    @Autowired
    public PatientServiceImpl(PatientDao pPatientDao) {
        patientDao = pPatientDao;
    }

    @Override
    public PatientTO findById(Long id) {
        final PatientEntity entity = patientDao.findOne(id);
        return PatientMapper.mapToTo(entity);
    }

    public List<PatientTO> findByLastName(String lastName) {
        return patientDao.findByLastName(lastName).stream()
                .map(PatientMapper::mapToTo)
                .collect(Collectors.toList());
    }

    public List<PatientTO> findPatientsWithMoreThanXVisits(long visitCount) {
        return patientDao.findPatientsWithMoreThanXVisits(visitCount).stream()
                .map(PatientMapper::mapToTo)
                .collect(Collectors.toList());
    }

    public List<PatientTO> findPatientsBornBefore(LocalDate date) {
        return patientDao.findPatientsBornBefore(date).stream()
                .map(PatientMapper::mapToTo)
                .collect(Collectors.toList());
    }
}
