package pl.edu.umg.linkumg.models;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.Data;

import java.util.List;

@Entity
@Data
public class Employer extends User {

    private String companyName;
    private String leiNumber;
    @OneToMany
    private List<JobOffer> jobOfferList;

}
