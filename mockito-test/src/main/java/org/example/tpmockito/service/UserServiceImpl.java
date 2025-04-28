package org.example.tpmockito.service;

import org.example.tpmockito.repository.UserRepository;
import org.example.tpmockito.dto.UserDTO;
import org.example.tpmockito.exception.DataIntegrityViolationException;
import org.example.tpmockito.exception.ObjectNotFoundException;
import org.example.tpmockito.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    private UserDTO convertToDTO(User user) {
        return new UserDTO(user.getId(), user.getName(), user.getEmail());
    }

    @Override
    public UserDTO getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("User not found with id: " + id));
        return convertToDTO(user);
    }

    @Override
    public List<UserDTO> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public UserDTO saveUser(User user) {
        userRepository.findByEmail(user.getEmail()).ifPresent(u -> {
            throw new DataIntegrityViolationException("Email already used: " + user.getEmail());
        });
        return convertToDTO(userRepository.save(user));
    }

    @Override
    public UserDTO updateUser(Long id, User user) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("User not found with id: " + id));

        userRepository.findByEmail(user.getEmail()).ifPresent(u -> {
            if (!u.getId().equals(id)) {
                throw new DataIntegrityViolationException("Email already used by another user.");
            }
        });

        existingUser.setName(user.getName());
        existingUser.setEmail(user.getEmail());
        existingUser.setPassword(user.getPassword());

        return convertToDTO(userRepository.save(existingUser));
    }

    @Override
    public void deleteUserById(Long id) {
        if (!userRepository.findById(id).isPresent()) {
            throw new ObjectNotFoundException("User not found with id: " + id);
        }
        userRepository.deleteById(id);
    }
}
