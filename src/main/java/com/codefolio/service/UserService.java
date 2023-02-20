package com.codefolio.service;

import com.codefolio.dto.user.NewUserDto;
import com.codefolio.dto.user.UserDto;
import com.codefolio.dto.user.UserPreviewDto;
import com.codefolio.entity.User;
import com.codefolio.mapper.UserMapper;
import com.codefolio.repostiory.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper mapper;

    @Transactional
    public UserDto createUser(NewUserDto newUserDto) {
        User user = User.builder()
                .username(newUserDto.username())
                .email(newUserDto.email())
                .password(newUserDto.password())
                .build();
        return mapper.map(userRepository.save(user));
    }

    public List<UserDto> getAllUsers() {
        return mapper.map(userRepository.findAll());
    }

    public UserPreviewDto previewUser(UUID userId) {
        return mapper.mapToPreview(findById(userId));
    }

    protected User findById(UUID id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
}
