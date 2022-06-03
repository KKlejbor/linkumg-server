package pl.edu.umg.linkumg.service;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.stereotype.Service;
import pl.edu.umg.linkumg.models.User;
import pl.edu.umg.linkumg.repositories.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
@Data
@AllArgsConstructor
public class UserService implements IUserService{
    private UserRepository userRepository;

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public Optional<User> findById(int id) {
        return userRepository.findById(id);
    }

    @Override
    public Optional<User> findByEmailAndPassword(String email, String password) {
        return userRepository.findByEmailAndPassword(email, password);
    }
}
