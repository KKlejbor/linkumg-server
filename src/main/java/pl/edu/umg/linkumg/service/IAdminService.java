package pl.edu.umg.linkumg.service;

import pl.edu.umg.linkumg.models.Admin;

import java.util.List;
import java.util.Optional;

public interface IAdminService {
    List<Admin> findAll();
    Optional<Admin> findById(int id);
    Admin saveOrUpdate(Admin admin);
    void deleteById(int id);
}
