package pl.edu.umg.linkumg.models;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Entity
public class Employer extends User {

    @Getter @Setter
    private String companyName;

    @Getter @Setter
    private String leiNumber;
}
