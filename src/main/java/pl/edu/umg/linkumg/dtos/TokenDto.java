package pl.edu.umg.linkumg.dtos;

public record TokenDto(String type, String accessToken, String refreshToken) {
    public TokenDto(String accessToken, String refreshToken) {
        this("Bearer", accessToken, refreshToken);
    }
}
