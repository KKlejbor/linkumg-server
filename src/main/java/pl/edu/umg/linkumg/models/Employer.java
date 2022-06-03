package pl.edu.umg.linkumg.models;

import jakarta.persistence.Entity;
import lombok.Data;

@Entity
@Data
public class Employer extends User {
    private String companyName;
    private String leiNumber;
}
