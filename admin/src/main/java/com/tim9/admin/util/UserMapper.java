package com.tim9.admin.util;

import org.springframework.stereotype.Component;

import com.tim9.admin.dto.UserDTO;
import com.tim9.admin.dto.response.UserResDTO;
import com.tim9.admin.model.User;

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
