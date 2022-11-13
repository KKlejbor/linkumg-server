package pl.edu.umg.linkumg.models.users;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import pl.edu.umg.linkumg.models.Social;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

@Entity
@Data
@NoArgsConstructor
public class Company extends User {

    private String phoneNumber;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "social_uuid")
    private Social socials;
    private String nip;

    public Company(@NonNull String email, @NonNull String password, @NonNull String name, String nip) {
        super(email, password, name);
        this.nip = nip;
    }
}
