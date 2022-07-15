package MyProjectGradle.service;

import MyProjectGradle.models.entities.Role;
import MyProjectGradle.models.enums.RolesEnum;

public interface RoleService {
    void initRoles();

    Role findByName(RolesEnum role);


}
