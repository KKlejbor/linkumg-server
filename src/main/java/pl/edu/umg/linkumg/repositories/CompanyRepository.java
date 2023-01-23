package pl.edu.umg.linkumg.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import pl.edu.umg.linkumg.models.users.Company;

import java.util.UUID;

@Repository
public interface CompanyRepository extends
        PagingAndSortingRepository<Company, UUID> {

    Page<Company> findAllByEmailContaining(Pageable pageable, String email);
}
