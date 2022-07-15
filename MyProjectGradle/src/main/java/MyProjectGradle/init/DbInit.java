package MyProjectGradle.init;

import MyProjectGradle.service.PictureService;
import MyProjectGradle.service.RoleService;
import MyProjectGradle.service.TypeService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DbInit implements CommandLineRunner {
    private final RoleService roleService;
    private final PictureService pictureService;

    private final TypeService typeService;

    public DbInit(RoleService roleService, PictureService pictureService, TypeService typeService) {

        this.roleService = roleService;
        this.pictureService = pictureService;
        this.typeService = typeService;
    }

    @Override
    public void run(String... args) throws Exception {
        roleService.initRoles();
        pictureService.createDefaultProfilePicture();
       typeService.initTypes();
    }
}
