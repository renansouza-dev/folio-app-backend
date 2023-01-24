package com.renansouza.folio.user;

import com.renansouza.folio.utils.WordUtils;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.URL;

import java.io.Serializable;

@Entity
@NoArgsConstructor
@Table(name = "users")
public class User implements Serializable {

    @Id
    @Getter
    @Setter
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Getter
    @NotBlank
    @Size(min = 5, max = 255)
    @Column(nullable = false)
    private String name;

    @Getter
    @Setter
    @Column
    @URL(regexp = "^(https\\:\\/\\/)[a-z0-9\\.\\/\\S]+$")
    private String avatar;

    public User(String name, String avatar) {
        this.name = WordUtils.capitalizeFully(name);
        this.avatar = avatar;
    }

    public void setName(String name) {
        this.name = WordUtils.capitalizeFully(name);
    }

}