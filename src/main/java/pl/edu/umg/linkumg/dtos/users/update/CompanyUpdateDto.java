package pl.edu.umg.linkumg.dtos.users.update;

import pl.edu.umg.linkumg.dtos.users.SocialDto;

public record CompanyUpdateDto(String phone, String about, SocialDto socials, String avatar) {
}
