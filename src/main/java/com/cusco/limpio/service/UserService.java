package com.cusco.limpio.service;

import java.util.List;

import com.cusco.limpio.dto.user.AuthResponseDTO;
import com.cusco.limpio.dto.user.CreateUserDTO;
import com.cusco.limpio.dto.user.LoginDTO;
import com.cusco.limpio.dto.user.UpdateUserDTO;
import com.cusco.limpio.dto.user.UserDTO;

public interface UserService {
    UserDTO createUser(CreateUserDTO createUserDTO);

    UserDTO getUserById(Long id);

    List<UserDTO> getAllUsers();

    /**
     * Actualiza un usuario. Solo el propio usuario o un ADMIN pueden hacerlo.
     *
     * @param id                 ID del usuario a actualizar
     * @param updateUserDTO      datos a actualizar
     * @param authenticatedEmail email del usuario autenticado (del JWT)
     */
    UserDTO updateUser(Long id, UpdateUserDTO updateUserDTO, String authenticatedEmail);

    /**
     * Baja lógica de un usuario. Solo el propio usuario o un ADMIN pueden hacerlo.
     *
     * @param id                 ID del usuario a eliminar
     * @param authenticatedEmail email del usuario autenticado (del JWT)
     */
    void deleteUser(Long id, String authenticatedEmail);

    AuthResponseDTO authenticate(LoginDTO loginDTO);
}
