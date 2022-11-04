package pl.edu.umg.linkumg.models;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.time.LocalDateTime;
import java.util.ArrayList;
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

    public Company(@NonNull String email, @NonNull String password, @NonNull String name, String nip) {
        super(email, password, name);
        this.nip = nip;
    }

    public Company(UUID userId, @NonNull String email, @NonNull String password, boolean active, boolean banned, String name, String description, String pathToAvatar, LocalDateTime createdAt, LocalDateTime lastUpdateAt, String phoneNumber, Social socials, String nip) {
        super(userId, email, password, active, banned, name, description, pathToAvatar, createdAt, lastUpdateAt);
        this.phoneNumber = phoneNumber;
        this.socials = socials;
        this.nip = nip;
        this.jobOfferList = new ArrayList<>();
    }

    public void addJobOffer(JobOffer jobOffer){
        jobOfferList.add(jobOffer);
    }
}
