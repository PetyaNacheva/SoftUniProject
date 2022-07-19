package MyProjectGradle.service.impl;

import MyProjectGradle.models.entities.Apartment;
import MyProjectGradle.models.entities.Role;
import MyProjectGradle.models.entities.UserEntity;
import MyProjectGradle.models.enums.RolesEnum;
import MyProjectGradle.repository.ApartmentRepository;
import MyProjectGradle.repository.UserRepository;
import MyProjectGradle.service.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;

import java.util.List;

@ExtendWith(MockitoExtension.class)
class ApartmentServiceImplTest {
    private Apartment apartment;
    private UserEntity testUser;
    private ApartmentService apartmentService;
    private PasswordEncoder passwordEncoder;
    private List<GrantedAuthority> authorities;
    private Role userRole, adminRole, hostRole;


    @Mock
    UserRepository userRepository;

    @Mock
    ApartmentRepository apartmentRepository;
    @Mock
    TownService townService;
    @Mock
    TypeService typeService;
    @Mock
    PictureService pictureService;
    @Mock
    ReservationService reservationService;
    @Mock
    UserService userService;
    @Mock
    ModelMapper modelMapper = new ModelMapper();

    @BeforeEach
    public void setUp(){
        passwordEncoder = new Pbkdf2PasswordEncoder();
        apartmentService = new ApartmentServiceImpl(apartmentRepository, modelMapper,userService, townService, typeService, pictureService, reservationService);
        testUser = new UserEntity();
        userRole = new Role();
        userRole.setName(RolesEnum.USER);
        testUser.setRole(List.of(userRole));
        testUser.setUsername("testUser");
        testUser.setFirstName("test");
        testUser.setLastName("test");
        testUser.setPassword("test");
        testUser.setEmail("test@test.com");
        testUser.setPhone("+3598935467");
        userRepository.save(testUser);
    }

    @Test
    public void testSaveApartment(){


    }

}