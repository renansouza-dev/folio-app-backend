package com.renansouza.folioappbackend.user;

import com.renansouza.folioappbackend.invoice.Invoice;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;

import java.util.Objects;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "users", uniqueConstraints = {@UniqueConstraint(columnNames = "email")})
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "uuid", nullable = false)
    @JdbcTypeCode(SqlTypes.UUID)
    private UUID uuid;

    @Column(nullable = false, length = 30)
    private String name;

    @Email
    @Column(nullable = false)
    private String email;

    private String picture;

    @OneToMany(mappedBy = "user")
    private Set<Invoice> invoices;

    public User(OidcUser oidcUser) {
        this.name = oidcUser.getFullName().toUpperCase().trim();
        this.picture = oidcUser.getPicture();
        this.email = oidcUser.getEmail();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(name, user.name)
                && Objects.equals(email, user.email)
                && Objects.equals(picture, user.picture);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, email, picture);
    }
}