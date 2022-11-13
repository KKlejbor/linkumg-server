package pl.edu.umg.linkumg.models.resources;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.edu.umg.linkumg.models.users.Company;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity(name = "jobs_offers")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class JobOffer {
    @Id
    @GeneratedValue(generator = "UUID")
    private UUID jobOfferId;
    private String title;
    private Integer salaryDown;
    private Integer salaryUp;
    private String location;
    private String description;
    private LocalDateTime createdAt;
    private LocalDateTime lastUpdatedAt;
    @ManyToOne
    private Company company;

    public JobOffer(String title, Integer salaryDown, Integer salaryUp, String location, String description) {
        this.title = title;
        this.salaryDown = salaryDown;
        this.salaryUp = salaryUp;
        this.location = location;
        this.description = description;
    }
}
