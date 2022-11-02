package pl.edu.umg.linkumg.models;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

@Entity(name = "jobs_offers")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class JobOffer {
    @Id
    @GeneratedValue(generator = "UUID")
    private UUID offerId;
    private String title;
    private Integer salaryDown;
    private Integer salaryUp;
    private String city;
    private String address;
    private Date addedDate;
    private Integer userId;
    private String pathToJobOfferImage;
    private LocalDateTime createdAt;
    private LocalDateTime lastUpdatedAt;
    private UUID companyId;
}
