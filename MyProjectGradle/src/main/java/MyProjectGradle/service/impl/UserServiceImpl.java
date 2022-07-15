package MyProjectGradle.service.impl;

import MyProjectGradle.models.binding.UserProfileBindingModel;
import MyProjectGradle.models.entities.Apartment;
import MyProjectGradle.models.entities.Picture;
import MyProjectGradle.models.entities.Role;
import MyProjectGradle.models.entities.UserEntity;
import MyProjectGradle.models.enums.RolesEnum;
import MyProjectGradle.models.service.UserServiceModel;
import MyProjectGradle.models.views.UserViewModel;
import MyProjectGradle.config.repository.UserRepository;
import MyProjectGradle.service.PictureService;
import MyProjectGradle.service.RoleService;
import MyProjectGradle.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@Service
public class UserServiceImpl implements UserService {
    private final UserDetailsService userDetailsService;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;
    private final PictureService pictureService;

    public UserServiceImpl(UserDetailsService userDetailsService, UserRepository userRepository, ModelMapper modelMapper, RoleService roleService, PasswordEncoder passwordEncoder, PictureService pictureService) {
        this.userDetailsService = userDetailsService;
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
        this.roleService = roleService;
        this.passwordEncoder = passwordEncoder;
        this.pictureService = pictureService;
    }

    @Override
    public boolean saveUser(UserServiceModel userServiceModel) {
        try {
                UserEntity user = modelMapper.map(userServiceModel, UserEntity.class);
                if (userRepository.count() == 0) {
                    Role roleAdmin = roleService.findByName(RolesEnum.ADMIN);
                    Role roleUser = roleService.findByName(RolesEnum.USER);
                    Role roleHost = roleService.findByName(RolesEnum.HOST);
                    Role roleGuest = roleService.findByName(RolesEnum.GUEST);
                    user.setRole(List.of(roleAdmin, roleUser, roleHost, roleGuest));
                } else {
                    Role roleUser = roleService.findByName(RolesEnum.USER);
                    user.setRole(List.of(roleUser));
                }
                if(Objects.equals(userServiceModel.getProfileImg().getOriginalFilename(), "")){
                    Picture defaultProfilePicture = pictureService.findDefaultProfilePicture("defaultPicture");
                    user.setProfileImg(defaultProfilePicture);
                }else {
                    MultipartFile picture = userServiceModel.getProfileImg();
                    Picture pictureFile = pictureService.createProfilePicture(picture, picture.getOriginalFilename(), userServiceModel.getUsername());
                    user.setProfileImg(pictureFile);
                }
                LocalDate date = LocalDate.now();
                user.setRegisterOn(date);
                user.setPassword(passwordEncoder.encode(userServiceModel.getPassword()));

                    userRepository.save(user);
                    UserDetails principal = userDetailsService.loadUserByUsername(userServiceModel.getUsername());
                    Authentication authentication = new UsernamePasswordAuthenticationToken(
                            principal,
                            user.getPassword(),
                            principal.getAuthorities()
                    );

                    SecurityContextHolder.
                            getContext().
                            setAuthentication(authentication);
        }catch (Exception e){
            return false;
        }
        return true;

    }


    @Override
    public UserEntity findByUsername(String username) {
        return userRepository.findByUsername(username).orElseThrow(()->new EntityNotFoundException("User"));
    }

    @Transactional
    @Override
    public void ChangeTheRoleOfUser(String username) {
        UserEntity user = userRepository.findByUsername(username).orElseThrow(() -> new EntityNotFoundException("User"));
        if(user!=null) {
            List<Apartment> apartments = user.getHostedApartments();
            List<Role> role = user.getRole();
            if (apartments.size() == 0) {
                if(!role.contains(roleService.findByName(RolesEnum.HOST))) {
                    role.add(roleService.findByName(RolesEnum.HOST));
                    user.setRole(role);
                }
            }
           userRepository.save(user);
        }
    }

    @Transactional
    @Override
    public void RemoveHostRole(String username) {
        UserEntity user = userRepository.findByUsername(username).orElseThrow(() -> new EntityNotFoundException("User"));
        List<Apartment> apartments = user.getHostedApartments();
        if (apartments.size() == 1) {
            List<Role> role = user.getRole();
            Role host = roleService.findByName(RolesEnum.HOST);
            if (role.contains(roleService.findByName(RolesEnum.HOST))) {
                role.remove(host);
                user.setRole(role);
            }
        }
            userRepository.save(user);
        }

    @Override
    public boolean isUsernameFree(String username) {
        return userRepository.findByUsername(username).isEmpty();
    }

    @Override
    public boolean isAdmin(String userIdentifier) {
        UserEntity user =userRepository.findByUsername(userIdentifier).orElseThrow(() -> new EntityNotFoundException("User"));
        return user.getRole().stream().anyMatch(r-> r.getName().equals(RolesEnum.ADMIN));
    }
    @Transactional
    @Override
    public void updateUser(UserProfileBindingModel userProfileBindingResult, String userIdentifier) throws IOException {
        UserEntity user = userRepository.findByUsername(userIdentifier).orElseThrow(() -> new EntityNotFoundException("User"));
        user.setFirstName(userProfileBindingResult.getFirstName());
        user.setLastName(userProfileBindingResult.getLastName());
        user.setPhone(userProfileBindingResult.getPhone());
        if(!userProfileBindingResult.getProfileImg().isEmpty()) {
          if(!user.getProfileImg().getTitle().equals("defaultPicture")){
              Picture oldPicture = pictureService.findDefaultProfilePicture(user.getProfileImg().getTitle());
              oldPicture.setUserName(null);
              pictureService.delete(oldPicture.getPublicId());
            }
            MultipartFile picture = userProfileBindingResult.getProfileImg();
            Picture pictureFile = pictureService.createProfilePicture(picture, picture.getOriginalFilename(), user.getUsername());
            user.setProfileImg(pictureFile);
        }
        userRepository.save(user);
    }

    @Override
    public UserEntity findById(Long id) {
       return userRepository.findById(id).orElseThrow(()->new EntityNotFoundException("User"));
    }

    @Override
    public UserViewModel findUserViewModelByUsername(String userIdentifier) {
       UserEntity user =userRepository.findByUsername(userIdentifier).orElseThrow(() -> new EntityNotFoundException("User"));
        UserViewModel userViewModel =modelMapper.map(user, UserViewModel.class);
            userViewModel.setProfileImg(user.getProfileImg().getUrl());
        return userViewModel;
    }
}
