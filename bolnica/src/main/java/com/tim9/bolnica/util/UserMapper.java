package com.tim9.bolnica.util;

import org.springframework.stereotype.Component;

import com.tim9.bolnica.dto.UserDTO;
import com.tim9.bolnica.dto.response.UserResDTO;
import com.tim9.bolnica.model.User;

@Component
public class UserMapper {

	public User toEntity(UserDTO dto) {
		return new User(dto.getEmail(), dto.getPassword(), dto.getFirstName(), dto.getLastName());
	}

	public UserDTO toDto(User entity) {
		return new UserDTO(entity.getEmail(), entity.getPassword(), entity.getFirstName(), entity.getLastName());
	}
	
	public UserResDTO toResDTO(User entity) {
		return new UserResDTO(entity.getId(), entity.getEmail(), entity.getPassword(), entity.getFirstName(), entity.getLastName());
	}
}
