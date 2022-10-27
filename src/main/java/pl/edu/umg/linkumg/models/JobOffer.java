package pl.edu.umg.linkumg.models;


import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Entity(name="jobs_offers")
@Data
public class JobOffer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer offerId;
    private String title;
    private Integer salaryDown;
    private Integer salaryUp;
    private String city;
    private String address;
    private Date addedDate;
    private Integer userId;
    private String pathToJobOfferImage;
    @ManyToOne
    private Employer employer;

}
