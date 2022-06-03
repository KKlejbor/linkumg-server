package pl.edu.umg.linkumg.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.Data;

import java.util.List;

@Entity
@Data
public class Employer extends User {
    private String companyName;
    @Column(unique = true)
    private String NIPNumber;
    @OneToMany
    private List<JobOffer> jobOfferList;
}
