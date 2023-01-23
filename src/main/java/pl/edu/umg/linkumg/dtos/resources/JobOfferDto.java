package pl.edu.umg.linkumg.dtos.resources;

import pl.edu.umg.linkumg.dtos.users.CompanyDto;

public record JobOfferDto(String offerId, String title, Integer salaryDown, Integer salaryUp, String location,
                          String description, String createdAt, String lastUpdatedAt, CompanyDto companyDto) {
}
