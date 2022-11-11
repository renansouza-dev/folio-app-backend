package com.renansouza.folio.user;

import com.renansouza.folio.shared.Auditable;
import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.io.Serializable;
import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
public class User extends Auditable<String> implements Serializable {

    @Id
    @Getter
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @Getter
    @Setter
    @Column(nullable = false)
    private String name;

    @Getter
    @Setter
    @Column
    private String avatar;

    public User(UserForm userForm) {
        var now = LocalDateTime.now();
        this.registration = now;
        this.modification = now;

        this.avatar = userForm.getAvatar();
        this.name = WordUtils.capitalizeFully(userForm.getName());
    }
}