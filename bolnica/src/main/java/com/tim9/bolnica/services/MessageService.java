package com.tim9.bolnica.services;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.bouncycastle.asn1.ASN1InputStream;
import org.bouncycastle.asn1.cms.ContentInfo;
import org.bouncycastle.cms.CMSException;
import org.bouncycastle.cms.CMSProcessable;
import org.bouncycastle.cms.CMSSignedData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.tim9.bolnica.dto.FilterDTO;
import com.tim9.bolnica.dto.response.MessageResponseDTO;
import com.tim9.bolnica.model.Message;
import com.tim9.bolnica.model.Patient;
import com.tim9.bolnica.repositories.MessageRepository;
import com.tim9.bolnica.repositories.PatientRepository;
import com.tim9.bolnica.util.DateUtil;

@Service
public class MessageService {

	@Autowired
	private MessageRepository messageRepo;
	
	@Autowired
	private PatientRepository patientRepo;
	
	@Autowired
    private AlarmService alarmService;
	
	public void save(@Valid byte[] signedMessage) throws ParseException, CMSException, IOException {
		ByteArrayInputStream inputStream = new ByteArrayInputStream(signedMessage);
        ASN1InputStream asnInputStream = new ASN1InputStream(inputStream);
        CMSSignedData cmsSignedData = new CMSSignedData(ContentInfo.getInstance(asnInputStream.readObject()));
        CMSProcessable msg = cmsSignedData.getSignedContent();
        System.out.println(new String((byte[]) msg.getContent()));
        String dto = new String((byte[]) msg.getContent());
        
		Message message = new Message();
		String[] array = dto.split(" ");
		message.setTimestamp(DateUtil.parse(array[0].trim().split("=")[1].trim()));
		message.setPatientId(Long.parseLong(array[1].trim().split("=")[1].trim()));
		message.setTemperature(Double.parseDouble(array[2].trim().split("=")[1].trim()));
		message.setSystolic(Integer.parseInt(array[3].trim().split("=")[1].trim()));
		message.setDiastolic(Integer.parseInt(array[4].trim().split("=")[1].trim()));
		message.setHeartRate(Integer.parseInt(array[5].trim().split("=")[1].trim()));
		message.setOxygenLevel(Integer.parseInt(array[6].trim().split("=")[1].trim()));
		this.messageRepo.save(message);
		this.alarmService.checkDoctorAlarms(message);
	}

	public Page<MessageResponseDTO> findAll(Pageable pageable, FilterDTO filter) {
		List<Message> all = messageRepo.findAllByOrderByTimestampDesc();
		ArrayList<Message> filtered = this.filter(all, filter);
		Page<Message> logPage = messageRepo.findByIdIn(pageable, filtered.stream().map(Message::getId).collect(Collectors.toList())); 
		ArrayList<MessageResponseDTO> forReturn = new ArrayList<MessageResponseDTO>();
		for (Message l: logPage.getContent()) {
			MessageResponseDTO mr = new MessageResponseDTO(l);
			Optional<Patient> p = patientRepo.findById(l.getPatientId());
			if (p.isPresent()) {
				mr.setPatient(p.get().getFirstName() + ' ' + p.get().getLastName());
			}
			forReturn.add(mr);
		}
		return new PageImpl<MessageResponseDTO>(forReturn, pageable, logPage.getTotalElements());
	}
	
	private ArrayList<Message> filter(List<Message> all, FilterDTO f) {
		if (f.getId() != null) {
			return (ArrayList<Message>) all.stream().filter(c -> c.getPatientId() == f.getId()).collect(Collectors.toList());
		}
		return (ArrayList<Message>) all;
	}
}
