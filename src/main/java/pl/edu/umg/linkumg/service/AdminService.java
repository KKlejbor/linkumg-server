package pl.edu.umg.linkumg.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.edu.umg.linkumg.models.Admin;
import pl.edu.umg.linkumg.repositories.AdminRepository;

import java.util.List;
import java.util.Optional;

@Service
public class AdminService implements IAdminService {

    @Autowired
    private AdminRepository adminRepository;

    @Override
    public List<Admin> findAll() {
        return adminRepository.findAll();
    }

    @Override
    public Optional<Admin> findById(int id) {
        return adminRepository.findById(id);
    }

    @Override
    public Admin saveOrUpdate(Admin admin) {
        return adminRepository.save(admin);
    }

    @Override
    public void deleteById(int id) {
        adminRepository.deleteById(id);
    }
}
