package pl.edu.umg.linkumg.models;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
public class Company extends User {

    private String phoneNumber;
    @ManyToOne
    @JoinColumn(name = "social_uuid")
    private Social socials;
    private String nip;
    @OneToMany
    private List<JobOffer> jobOfferList;

    public Company(@NonNull String email, @NonNull String password) {
        super(email, password);
    }

    public Company(UUID userId, @NonNull String email, @NonNull String password, boolean active, String name, String description, String pathToAvatar, LocalDateTime createdAt, LocalDateTime lastUpdateAt, String phoneNumber, Social socials, String nip, List<JobOffer> jobOfferList) {
        super(userId, email, password, active, name, description, pathToAvatar, createdAt, lastUpdateAt);
        this.phoneNumber = phoneNumber;
        this.socials = socials;
        this.nip = nip;
        this.jobOfferList = jobOfferList;
    }
}
