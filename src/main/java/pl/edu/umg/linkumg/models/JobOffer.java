package pl.edu.umg.linkumg.models;


import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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
//    @ManyToOne
//    private Employer employer;
}
