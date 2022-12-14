package com.renansouza.folio.user;

import com.renansouza.folio.utils.WordUtils;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
public class User implements Serializable {

    @Serial
    private static final long serialVersionUID = -1362258531757232654L;

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
    @LastModifiedDate
    private LocalDateTime registration;
    @CreatedDate
    private LocalDateTime modification;

    public User(UserForm userForm) {
        var now = LocalDateTime.now();
        this.registration = now;
        this.modification = now;

        this.avatar = userForm.getAvatar();
        this.name = WordUtils.capitalizeFully(userForm.getName());
    }
}