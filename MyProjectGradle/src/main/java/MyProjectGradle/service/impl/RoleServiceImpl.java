package MyProjectGradle.service.impl;

import MyProjectGradle.models.entities.Role;
import MyProjectGradle.models.enums.RolesEnum;
import MyProjectGradle.repository.RoleRepository;
import MyProjectGradle.service.RoleService;
import org.springframework.stereotype.Service;

@Service
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepository;

    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public void initRoles() {
        if(roleRepository.count()==0){

            Role adminRole = new Role(RolesEnum.ADMIN);
            Role userRole = new Role(RolesEnum.USER);
            Role guestRole = new Role(RolesEnum.GUEST);
            Role hostRole = new Role(RolesEnum.HOST);

            roleRepository.save(adminRole);
            roleRepository.save(userRole);
            roleRepository.save(guestRole);
            roleRepository.save(hostRole);
        }
    }

    @Override
    public Role findByName(RolesEnum role){
        return roleRepository.findByName(role);
    }

}
