package com.cusco.limpio.mapper;

import com.cusco.limpio.domain.model.User;
import com.cusco.limpio.dto.user.CreateUserDTO;
import com.cusco.limpio.dto.user.UserDTO;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserDTO toDTO(User user) {
        if (user == null) return null;
        return new UserDTO(
                user.getId(),
                user.getEmail(),
                user.getFirstName(),
                user.getLastName(),
                user.getPhone(),
                user.getRole() == null ? null : user.getRole().name(),
                user.getActive(),
                user.getCreatedAt()
        );
    }

    public User toEntity(CreateUserDTO dto) {
        if (dto == null) return null;
        return User.builder()
                .email(dto.email())
                .firstName(dto.firstName())
                .lastName(dto.lastName())
                .phone(dto.phone())
                .build();
    }
}
