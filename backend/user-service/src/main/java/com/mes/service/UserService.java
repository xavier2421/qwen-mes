package com.mes.service;

import com.mes.dto.UserDTO;
import java.util.List;

public interface UserService {

    UserDTO createUser(UserDTO userDTO);

    UserDTO updateUser(Long id, UserDTO userDTO);

    void deleteUser(Long id);

    UserDTO getUserById(Long id);

    List<UserDTO> getAllUsers(Long tenantId);

    UserDTO getUserByUsername(String username);
}
