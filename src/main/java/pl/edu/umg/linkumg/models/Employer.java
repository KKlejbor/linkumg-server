package pl.edu.umg.linkumg.models;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.List;

@Entity
@Data
public class Employer extends User {

    private String companyName;
    private String leiNumber;
    @OneToMany
    private List<JobOffer> jobOfferList;

}
