package pl.edu.umg.linkumg.models;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Entity
@DiscriminatorValue("4")
@Getter
@Setter
public class Student extends User {
    private int indexNumber;
    private String faculty;
}
