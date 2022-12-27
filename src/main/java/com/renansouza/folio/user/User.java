package com.renansouza.folio.user;

import com.renansouza.folio.shared.Auditable;
import com.renansouza.folio.shared.EntityAuditorAware;
import com.renansouza.folio.utils.WordUtils;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
        this.setLastModifiedDate(now);
        this.setCreatedDate(now);

        var auditor = new EntityAuditorAware().getCurrentAuditor();
        this.setLastModifiedBy(String.valueOf(auditor));
        this.setCreatedBy(String.valueOf(auditor));


        this.avatar = userForm.getAvatar();
        this.name = WordUtils.capitalizeFully(userForm.getName());
    }
}