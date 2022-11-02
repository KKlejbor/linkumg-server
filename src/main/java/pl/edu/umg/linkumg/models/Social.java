package pl.edu.umg.linkumg.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.UUID;

@Entity(name="socials")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Social {
    @Id
    @GeneratedValue(generator = "UUID")
    private UUID uuid;
    private String github;
    private String linkedin;
    private String facebook;
    private String instagram;
}
