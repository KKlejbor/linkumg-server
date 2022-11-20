package pl.edu.umg.linkumg.dtos.users.update;

import pl.edu.umg.linkumg.dtos.users.SocialDto;

import java.time.LocalDate;

public record StudentUpdateDto(LocalDate birthDate, String phone, String about, SocialDto socials, String avatar) {
}
