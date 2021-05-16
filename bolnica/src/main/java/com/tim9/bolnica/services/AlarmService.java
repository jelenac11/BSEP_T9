package com.tim9.bolnica.services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.drools.core.ClassObjectFilter;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.tim9.bolnica.dto.response.AlarmDoctorResponseDTO;
import com.tim9.bolnica.dto.response.AlarmResponseDTO;
import com.tim9.bolnica.model.AdminAlarm;
import com.tim9.bolnica.model.DoctorAlarm;
import com.tim9.bolnica.model.Log;
import com.tim9.bolnica.model.Message;
import com.tim9.bolnica.model.Patient;
import com.tim9.bolnica.repositories.AlarmAdminRepository;
import com.tim9.bolnica.repositories.AlarmDoctorRepository;
import com.tim9.bolnica.repositories.PatientRepository;


@Service
public class AlarmService {
	
	@Autowired
	private AlarmAdminRepository alarmRepository;
	
	@Autowired
	private AlarmDoctorRepository alarmDoctorRepository;
	
	@Autowired
	private PatientRepository patientRepo;
	
	//@Autowired
    KieContainer kieContainer;
	
	//@Autowired
    KieContainer kieContainerD;
	
	public Page<AlarmResponseDTO> findAll(Pageable pageable) {
		ArrayList<AlarmResponseDTO> resp = new ArrayList<AlarmResponseDTO>();
		Page<AdminAlarm> page = alarmRepository.findAllByOrderByTimestampDesc(pageable);
		for (AdminAlarm a : page.getContent())
			resp.add(new AlarmResponseDTO(a));
		return new PageImpl<AlarmResponseDTO>(resp, pageable, page.getTotalElements());
	}

	public AdminAlarm save(AdminAlarm alarm) {
        return alarmRepository.save(alarm);
    }
	
	public DoctorAlarm saveDoctorAlarm(DoctorAlarm alarm) {
        return alarmDoctorRepository.save(alarm);
    }
	
	public void checkAlarms(List<Log> logs) {
		KieSession kieSession = getKieSession();
		for (Log log: logs) {
            kieSession.insert(log);
        }
        kieSession.fireAllRules();

        @SuppressWarnings("unchecked")
		Collection<AdminAlarm> raisedAlarms = (Collection<AdminAlarm>) kieSession.getObjects(new ClassObjectFilter(AdminAlarm.class));
        for (AdminAlarm alarm: raisedAlarms) {
            save(alarm);
        }
	}
	
	private KieSession getKieSession() {
        return kieContainer.newKieSession("admin-session");
    }
	
	public void checkDoctorAlarms(Message message) {
		KieSession kieSession = getDoctorKieSession();
		kieSession.insert(message);
        kieSession.fireAllRules();

        @SuppressWarnings("unchecked")
		Collection<DoctorAlarm> raisedAlarms = (Collection<DoctorAlarm>) kieSession.getObjects(new ClassObjectFilter(DoctorAlarm.class));
        for (DoctorAlarm alarm: raisedAlarms) {
            saveDoctorAlarm(alarm);
        }
	}
	
	private KieSession getDoctorKieSession() {
        return kieContainerD.newKieSession("doctor-session");
    }

	public Page<AlarmDoctorResponseDTO> findAllDoctorAlarms(Pageable pageable) {
		ArrayList<AlarmDoctorResponseDTO> resp = new ArrayList<AlarmDoctorResponseDTO>();
		Page<DoctorAlarm> page = alarmDoctorRepository.findAllByOrderByTimestampDesc(pageable);
		for (DoctorAlarm a : page.getContent()) {
			AlarmDoctorResponseDTO ad = new AlarmDoctorResponseDTO();
			Optional<Patient> p = patientRepo.findById(a.getPatientId());
			if (p.isPresent()) {
				ad.setPatient(p.get().getFirstName() + ' ' + p.get().getLastName());
			}
			ad.setId(a.getId());
			ad.setTimestamp(a.getTimestamp());
			ad.setMessage(a.getMessage());
			resp.add(ad);
		}
		return new PageImpl<AlarmDoctorResponseDTO>(resp, pageable, page.getTotalElements());
	}
}
