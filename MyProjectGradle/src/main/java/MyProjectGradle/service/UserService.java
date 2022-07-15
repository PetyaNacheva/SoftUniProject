package MyProjectGradle.service;

import MyProjectGradle.models.binding.UserProfileBindingModel;
import MyProjectGradle.models.entities.Apartment;
import MyProjectGradle.models.entities.UserEntity;
import MyProjectGradle.models.service.UserServiceModel;
import MyProjectGradle.models.views.UserViewModel;

import javax.transaction.Transactional;
import java.io.IOException;

public interface UserService {
    boolean saveUser(UserServiceModel userServiceModel);

     UserEntity findByUsername(String username);
    @Transactional
    void ChangeTheRoleOfUser(String username);


    @Transactional
    void RemoveHostRole(String username);

    boolean isUsernameFree(String username);

    boolean isAdmin(String userIdentifier);

    void updateUser(UserProfileBindingModel userProfileBindingResult, String userIdentifier) throws IOException;



    UserViewModel findUserViewModelByUsername(String userIdentifier);

    UserEntity findById(Long id);
}
