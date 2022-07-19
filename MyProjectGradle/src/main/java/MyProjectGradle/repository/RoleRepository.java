package MyProjectGradle.repository;


import MyProjectGradle.models.entities.Role;
import MyProjectGradle.models.enums.RolesEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByName(RolesEnum roleName);
}
