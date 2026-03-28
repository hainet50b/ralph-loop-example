package com.programacho.ralphloopexample.user;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User create(User user) {
        if (userRepository.existsByName(user.getName())) {
            throw new UserAlreadyExistsException("name", user.getName());
        }
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new UserAlreadyExistsException("email", user.getEmail());
        }
        return userRepository.save(user);
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    public User update(Long id, User user) {
        User existing = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
        if (!existing.getName().equals(user.getName()) && userRepository.existsByName(user.getName())) {
            throw new UserAlreadyExistsException("name", user.getName());
        }
        if (!existing.getEmail().equals(user.getEmail()) && userRepository.existsByEmail(user.getEmail())) {
            throw new UserAlreadyExistsException("email", user.getEmail());
        }
        existing.setName(user.getName());
        existing.setEmail(user.getEmail());
        return userRepository.save(existing);
    }

    public void delete(Long id) {
        User existing = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
        userRepository.delete(existing);
    }
}
