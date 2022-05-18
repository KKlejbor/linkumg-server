package pl.edu.umg.linkumg.models;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Entity
@DiscriminatorValue("2")
@Getter
@Setter
public class College extends User {
    private String faculty;
}
