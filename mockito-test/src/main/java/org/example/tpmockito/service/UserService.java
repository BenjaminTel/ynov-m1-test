package org.example.tpmockito.service;

import org.example.tpmockito.dto.UserDTO;
import org.example.tpmockito.model.User;

import java.util.List;

public interface UserService {
    UserDTO getUserById(Long id);
    List<UserDTO> getAllUsers();
    UserDTO saveUser(User user);
    UserDTO updateUser(Long id, User user);
    void deleteUserById(Long id);
}
