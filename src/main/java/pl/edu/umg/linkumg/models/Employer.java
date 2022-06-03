package pl.edu.umg.linkumg.models;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Entity
@DiscriminatorValue("3")
@Getter
@Setter
public class Employer extends User {
    private String companyName;
    private String leiNumber;
}
