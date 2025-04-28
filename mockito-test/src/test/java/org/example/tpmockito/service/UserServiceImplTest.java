package org.example.tpmockito.service;

import org.example.tpmockito.dto.UserDTO;
import org.example.tpmockito.exception.DataIntegrityViolationException;
import org.example.tpmockito.exception.ObjectNotFoundException;
import org.example.tpmockito.model.User;
import org.example.tpmockito.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@DisplayName("Test Pour UserServiceImpl")
public class UserServiceImplTest {
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userServiceImpl;

    private AutoCloseable openMocks;

    @BeforeEach
    void setUp() {
        openMocks = MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void tearDown() throws Exception {
        openMocks.close();
    }

    @Test
    @DisplayName("✔️ Récupérer un utilisateur par ID avec succès")
    void testGetUserById_Success() {
        User user = new User(1L, "John Doe", "john@example.com", "password");
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        UserDTO userDTO = userServiceImpl.getUserById(1L);

        assertNotNull(userDTO);
        assertEquals("John Doe", userDTO.getName());
        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("❌ Échec de récupération d'un utilisateur - NotFound")
    void testGetUserById_NotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ObjectNotFoundException.class, () -> userServiceImpl.getUserById(1L));
        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("✔️ Récupérer tous les utilisateurs")
    void testGetAllUsers() {
        List<User> users = Arrays.asList(
                new User(1L, "John Doe", "john@example.com", "password"),
                new User(2L, "Jane Smith", "jane@example.com", "password123")
        );
        when(userRepository.findAll()).thenReturn(users);

        List<UserDTO> userDTOs = userServiceImpl.getAllUsers();

        assertEquals(2, userDTOs.size());
        verify(userRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("✔️ Créer un utilisateur avec succès")
    void testSaveUser_Success() {
        User user = new User(null, "John Doe", "john@example.com", "password");
        when(userRepository.findByEmail("john@example.com")).thenReturn(Optional.empty());
        when(userRepository.save(user)).thenReturn(new User(1L, "John Doe", "john@example.com", "password"));

        UserDTO savedUser = userServiceImpl.saveUser(user);

        assertNotNull(savedUser);
        assertEquals("john@example.com", savedUser.getEmail());
        verify(userRepository, times(1)).findByEmail("john@example.com");
        verify(userRepository, times(1)).save(user);
    }

    @Test
    @DisplayName("❌ Échec de création d'un utilisateur - Email existant")
    void testSaveUser_EmailAlreadyExists() {
        User user = new User(null, "John Doe", "john@example.com", "password");
        when(userRepository.findByEmail("john@example.com")).thenReturn(Optional.of(user));

        assertThrows(DataIntegrityViolationException.class, () -> userServiceImpl.saveUser(user));
        verify(userRepository, times(1)).findByEmail("john@example.com");
        verify(userRepository, never()).save(any());
    }

    @Test
    @DisplayName("✔️ Mise à jour d'un utilisateur existant")
    void testUpdateUser_Success() {
        User existingUser = new User(1L, "Old Name", "old@example.com", "password");
        User updatedInfo = new User(null, "New Name", "new@example.com", "newpassword");

        when(userRepository.findById(1L)).thenReturn(Optional.of(existingUser));
        when(userRepository.findByEmail("new@example.com")).thenReturn(Optional.empty());
        when(userRepository.save(existingUser)).thenReturn(existingUser);

        UserDTO updatedUser = userServiceImpl.updateUser(1L, updatedInfo);

        assertEquals("New Name", updatedUser.getName());
        verify(userRepository, times(1)).findById(1L);
        verify(userRepository, times(1)).save(existingUser);
    }

    @Test
    @DisplayName("❌ Échec de mise à jour - Email utilisé par un autre utilisateur")
    void testUpdateUser_EmailAlreadyExists() {
        User existingUser = new User(1L, "Old Name", "old@example.com", "password");
        User anotherUser = new User(2L, "Another", "new@example.com", "password123");
        User updatedInfo = new User(null, "New Name", "new@example.com", "newpassword");

        when(userRepository.findById(1L)).thenReturn(Optional.of(existingUser));
        when(userRepository.findByEmail("new@example.com")).thenReturn(Optional.of(anotherUser));

        assertThrows(DataIntegrityViolationException.class, () -> userServiceImpl.updateUser(1L, updatedInfo));
        verify(userRepository, times(1)).findById(1L);
        verify(userRepository, never()).save(any());
    }

    @Test
    @DisplayName("✔️ Suppression d'un utilisateur existant")
    void testDeleteUserById_Success() {
        User user = new User(1L, "John", "john@example.com", "pass");
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        userServiceImpl.deleteUserById(1L);

        verify(userRepository, times(1)).findById(1L);
        verify(userRepository, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("❌ Échec de suppression d'un utilisateur inexistant")
    void testDeleteUserById_NotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ObjectNotFoundException.class, () -> userServiceImpl.deleteUserById(1L));
        verify(userRepository, times(1)).findById(1L);
        verify(userRepository, never()).deleteById(any());
    }
}
