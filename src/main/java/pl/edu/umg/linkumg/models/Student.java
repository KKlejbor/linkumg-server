package pl.edu.umg.linkumg.models;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Entity
public class Student extends User {

    @Getter @Setter
    private int indexNumber;

    @Getter @Setter
    private String faculty;
}
