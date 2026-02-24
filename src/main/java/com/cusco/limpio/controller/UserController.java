package com.cusco.limpio.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cusco.limpio.dto.user.AuthResponseDTO;
import com.cusco.limpio.dto.user.CreateUserDTO;
import com.cusco.limpio.dto.user.LoginDTO;
import com.cusco.limpio.dto.user.UpdateUserDTO;
import com.cusco.limpio.dto.user.UserDTO;
import com.cusco.limpio.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    /**
     * Registro de nuevo usuario.
     * Endpoint público — no requiere autenticación.
     * Retorna 201 Created con el recurso creado.
     */
    @PostMapping
    public ResponseEntity<UserDTO> createUser(@Validated @RequestBody CreateUserDTO dto) {
        UserDTO created = userService.createUser(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    /**
     * Autenticación de usuario.
     * Endpoint público — no requiere autenticación.
     */
    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(@Validated @RequestBody LoginDTO loginDTO) {
        return ResponseEntity.ok(userService.authenticate(loginDTO));
    }

    /**
     * Obtener un usuario por su ID.
     * Requiere autenticación.
     */
    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUser(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    /**
     * Listar todos los usuarios.
     * Requiere autenticación. Solo accesible por ADMIN en un escenario real
     * (se puede refinar con @PreAuthorize).
     */
    @GetMapping
    public ResponseEntity<List<UserDTO>> getAll() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    /**
     * Actualizar datos de un usuario existente.
     * Requiere autenticación.
     */
    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> updateUser(@PathVariable Long id,
            @Validated @RequestBody UpdateUserDTO dto,
            Authentication authentication) {
        return ResponseEntity.ok(userService.updateUser(id, dto, authentication.getName()));
    }

    /**
     * Baja lógica de un usuario (no se elimina físicamente).
     * Requiere autenticación.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id, Authentication authentication) {
        userService.deleteUser(id, authentication.getName());
        return ResponseEntity.noContent().build();
    }
}
