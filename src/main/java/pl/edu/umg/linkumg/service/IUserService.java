package pl.edu.umg.linkumg.service;

import pl.edu.umg.linkumg.models.User;

import java.util.List;
import java.util.Optional;

public interface IUserService {
    List<User> findAll();
    Optional<User> findById(int id);
    Optional<User> findByEmailAndPassword(String email, String password);
}
