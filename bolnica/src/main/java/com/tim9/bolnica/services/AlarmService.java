package com.tim9.bolnica.services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.drools.core.ClassObjectFilter;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.tim9.bolnica.dto.response.AlarmResponseDTO;
import com.tim9.bolnica.model.AdminAlarm;
import com.tim9.bolnica.model.Log;
import com.tim9.bolnica.repositories.AlarmAdminRepository;


@Service
public class AlarmService {
	
	@Autowired
	private AlarmAdminRepository alarmRepository;
	//public static KieSession kieSession;
	@Autowired
    KieContainer kieContainer;
	
	/*@EventListener(ApplicationReadyEvent.class)
    public void initializeSessions() {
        kieSession = getKieSession();
    }*/
	
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
}
