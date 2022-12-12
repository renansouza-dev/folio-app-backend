package com.renansouza.folio.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.text.WordUtils;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
public class User {

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