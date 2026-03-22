package com.cusco.limpio.mapper;

import org.springframework.stereotype.Component;

import com.cusco.limpio.domain.model.User;
import com.cusco.limpio.dto.user.UserDTO;

@Component
public class UserMapper {

    public UserDTO toDTO(User user) {
        if (user == null) {
            return null;
        }

        return new UserDTO(
                user.getId(),
                user.getEmail(),
                user.getFirstName(),
                user.getLastName(),
                user.getPhone(),
                user.getRole() == null ? null : user.getRole().name(),
                user.getActive(),
                user.getCreatedAt());
    }
}
