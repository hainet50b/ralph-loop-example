package com.programacho.ralphloopexample;

import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User create(User user) {
        userRepository.findByName(user.getName()).ifPresent(existing -> {
            throw new UserAlreadyExistsException("name", user.getName());
        });
        userRepository.findByEmail(user.getEmail()).ifPresent(existing -> {
            throw new UserAlreadyExistsException("email", user.getEmail());
        });
        return userRepository.save(user);
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
    }

    public User update(Long id, User updated) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
        userRepository.findByName(updated.getName())
                .filter(existing -> !existing.getId().equals(id))
                .ifPresent(existing -> {
                    throw new UserAlreadyExistsException("name", updated.getName());
                });
        userRepository.findByEmail(updated.getEmail())
                .filter(existing -> !existing.getId().equals(id))
                .ifPresent(existing -> {
                    throw new UserAlreadyExistsException("email", updated.getEmail());
                });
        user.setName(updated.getName());
        user.setEmail(updated.getEmail());
        return userRepository.save(user);
    }

    public void delete(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
        userRepository.delete(user);
    }
}
