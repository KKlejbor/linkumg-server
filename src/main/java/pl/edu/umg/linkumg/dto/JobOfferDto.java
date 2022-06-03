package pl.edu.umg.linkumg.dto;

import lombok.Data;

import java.util.Date;

@Data
public class JobOfferDto {

    private Integer offerId;
    private String title;
    private Integer salaryDown;
    private Integer salaryUp;
    private String city;
    private String address;
    private Date addedDate;
    private Integer userId;
    private String pathToJobOfferImage;
}
