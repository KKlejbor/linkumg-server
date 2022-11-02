package pl.edu.umg.linkumg.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record JobOfferDto(UUID offerId, String title, Integer salaryDown, Integer salaryUp, String city,
                          String pathToJobOfferImage, LocalDateTime createdAt, LocalDateTime lastUpdatedAt,
                          UUID companyId) {
}
