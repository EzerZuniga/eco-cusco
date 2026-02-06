package com.cusco.limpio.service;

import com.cusco.limpio.dto.user.AuthResponseDTO;
import com.cusco.limpio.dto.user.CreateUserDTO;
import com.cusco.limpio.dto.user.LoginDTO;
import com.cusco.limpio.dto.user.UserDTO;

import java.util.List;

public interface UserService {
	UserDTO createUser(CreateUserDTO createUserDTO);
	UserDTO getUserById(Long id);
	List<UserDTO> getAllUsers();
	UserDTO updateUser(Long id, CreateUserDTO updateUserDTO);
	void deleteUser(Long id);
	AuthResponseDTO authenticate(LoginDTO loginDTO);
}
