package pl.edu.umg.linkumg.models;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Entity
@DiscriminatorValue("1")
@Getter
@Setter
public class Admin extends User {
    // nothing????!!
}
