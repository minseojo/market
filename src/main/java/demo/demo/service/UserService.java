package demo.demo.service;

import demo.demo.domain.User;
import demo.demo.repository.JdbcUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final JdbcUserRepository userRepository;

    public Long create(User user) {
        userRepository.sava(user);
        return user.getId();
    }

    // 기본키
    public Optional<User> findById(Long id) {return userRepository.findById(id);}

    public Optional<User> findByUserId(String userId) {return userRepository.findByUserId(userId);}

}
