package MyProjectGradle.models.entities;


import MyProjectGradle.models.enums.RolesEnum;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

@Entity
@Table(name = "roles")
public class Role extends BaseEntity{
    @Enumerated(EnumType.STRING)
    private RolesEnum name;

    public Role() {
    }

    public Role(RolesEnum name) {
        this.name = name;
    }

    public RolesEnum getName() {
        return name;
    }

    public void setName(RolesEnum name) {
        this.name = name;
    }
}
