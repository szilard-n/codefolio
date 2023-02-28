package com.codefolio.service;

import com.codefolio.dto.auth.SignUpRequest;
import com.codefolio.dto.user.UserDto;
import com.codefolio.dto.user.UserPreviewResponse;
import com.codefolio.entity.User;
import com.codefolio.exception.ExceptionFactory;
import com.codefolio.exception.exceptions.CredentialsAlreadyInUseException;
import com.codefolio.exception.exceptions.UserNotFoundException;
import com.codefolio.mapper.UserMapper;
import com.codefolio.repostiory.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    /**
     * EXCEPTIONS
     */
    private static final String EMAIL_IN_USE = "exceptions.user.emailAlreadyInUse";
    private static final String USERNAME_IN_USE = "exceptions.user.usernameAlreadyInUse";
    private static final String USER_NOT_FOUND = "exceptions.user.userNotFount";

    private static final String EMAIL_REGEX = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper mapper;

    public void checkCredentialValidity(String credential) {
        if (!ObjectUtils.isEmpty(credential) && credential.matches(EMAIL_REGEX)) {
            userRepository.findByEmail(credential)
                    .ifPresent(user -> {
                        throw ExceptionFactory.create(CredentialsAlreadyInUseException.class, EMAIL_IN_USE);
                    });
        } else {
            userRepository.findByUsername(credential)
                    .ifPresent(user -> {
                        throw ExceptionFactory.create(CredentialsAlreadyInUseException.class, USERNAME_IN_USE);
                    });
        }
    }

    @Transactional
    protected User createUser(SignUpRequest signUpRequest) {
        User user = User.builder()
                .username(signUpRequest.username())
                .email(signUpRequest.email())
                .password(passwordEncoder.encode(signUpRequest.password()))
                .build();
        return userRepository.save(user);
    }

    public List<UserDto> getAllUsers() {
        return mapper.map(userRepository.findAll());
    }

    public UserPreviewResponse previewUser(UUID userId) {
        return mapper.mapToPreview(findById(userId));
    }

    protected User findUserByCredential(String credential) {
        Optional<User> userOptional;

        if (!ObjectUtils.isEmpty(credential) && credential.matches(EMAIL_REGEX)) {
            userOptional = userRepository.findByEmail(credential);
        } else {
            userOptional = userRepository.findByUsername(credential);
        }

        return userOptional.orElseThrow(() -> ExceptionFactory.create(UserNotFoundException.class, USER_NOT_FOUND));
    }

    private User findById(UUID id) {
        return userRepository.findById(id)
                .orElseThrow(() -> ExceptionFactory.create(UserNotFoundException.class, USER_NOT_FOUND));
    }
}
