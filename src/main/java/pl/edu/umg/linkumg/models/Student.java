package pl.edu.umg.linkumg.models;

import jakarta.persistence.Entity;
import lombok.Data;

@Entity
@Data
public class Student extends User {
    private int indexNumber;
    private String faculty;
}
