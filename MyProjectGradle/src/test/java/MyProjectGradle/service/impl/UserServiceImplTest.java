package MyProjectGradle.service.impl;

import MyProjectGradle.models.binding.UserProfileBindingModel;
import MyProjectGradle.models.entities.*;
import MyProjectGradle.models.enums.TypeEnum;
import MyProjectGradle.models.views.UserViewModel;
import MyProjectGradle.repository.UserRepository;
import MyProjectGradle.models.enums.RolesEnum;
import MyProjectGradle.service.PictureService;
import MyProjectGradle.service.RoleService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest{
    private UserDetailsService userDetailsService;
    private Picture pictureTest;
    private UserServiceImpl serviceToTest;
    private PasswordEncoder passwordEncoder;
    private UserViewModel userViewModel;
    private UserProfileBindingModel userProfileBindingModel;
    private UserEntity testUser;
    private UserEntity secondUser;
    private List<GrantedAuthority> authorities;
    private Role userRole, adminRole, hostRole;
    private final ModelMapper modelMapper=new ModelMapper();
    private Apartment apartment;
    private Type studio;
    private Town testTown;

    @Mock
    UserRepository mockUserRepository;
    @Mock
    RoleService mockRoleService;
    @Mock
    PictureService mockPictureService;
    @Mock
    MultipartFile mockMultipartFile;



    @BeforeEach
    void setUp(){
        passwordEncoder = new Pbkdf2PasswordEncoder();
        serviceToTest= new UserServiceImpl(userDetailsService, mockUserRepository, new ModelMapper(), mockRoleService, passwordEncoder, mockPictureService);
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
        pictureTest = new Picture();
        pictureTest.setTitle("defaultPicture");
        pictureTest.setUserName("TestTown");
        pictureTest.setUrl("testUrl");
        pictureTest.setPublicId("testId");
        testUser.setProfileImg(pictureTest);
        mockUserRepository.save(testUser);



        studio = new Type();
        studio.setType(TypeEnum.STUDIO);
        studio.setCapacity(3);
        studio.setDescription("studio");

        testTown = new Town();
        testTown.setName("testTown");
        testTown.setDescription("testTown");
        testTown.setPictureUrl(pictureTest);
        testTown.setId(1L);

        apartment=new Apartment();
        apartment.setOwner(testUser);
        apartment.setType(studio);
        apartment.setAddress("any address");
        apartment.setPrice(BigDecimal.valueOf(50));
        apartment.setTown(testTown);
        apartment.setName("firstApartment");
        apartment.setPictures(List.of(pictureTest));

        secondUser = new UserEntity();
        userRole = new Role();
        userRole.setName(RolesEnum.USER);
        adminRole = new Role();
        adminRole.setName(RolesEnum.ADMIN);
        hostRole = new Role();
        hostRole.setName(RolesEnum.HOST);
        secondUser.setRole(List.of(userRole, adminRole, hostRole));
        secondUser.setUsername("admin");
        secondUser.setFirstName("admin");
        secondUser.setLastName("admin");
        secondUser.setPassword("admin");
        secondUser.setEmail("admin@admin.com");
        secondUser.setPhone("+3598935467");
        secondUser.setHostedApartments(List.of(apartment));
        mockUserRepository.save(secondUser);

    }

    @Test
    public void testFindByUsername(){
        when(mockUserRepository.findByUsername(testUser.getUsername())).thenReturn(Optional.of(testUser));
        assertTrue(serviceToTest.findByUsername(testUser.getUsername()).getUsername().equals("testUser"));
    }

    @Test
    public void testFindByUsernameThrow(){
        String fakeUsername = "FakeUsername";
        when(mockUserRepository.findByUsername(fakeUsername)).
                thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> serviceToTest.findByUsername(fakeUsername));
    }

    @Test
    public void testIsUsernameFree(){
        when(mockUserRepository.findByUsername(testUser.getUsername())).
                thenReturn(Optional.empty());

       assertTrue(serviceToTest.isUsernameFree(testUser.getUsername()));
    }

    @Test
    public void testIsUsernameNotFree(){
        when(mockUserRepository.findByUsername(testUser.getUsername())).
                thenReturn(Optional.of(testUser));

       assertFalse(serviceToTest.isUsernameFree(testUser.getUsername()));
    }

    @Test
    public void testIsAdmin(){
        when(mockUserRepository.findByUsername(secondUser.getUsername()))
                .thenReturn(Optional.of(secondUser));

        assertTrue(serviceToTest.isAdmin(secondUser.getUsername()));
    }

    @Test
    public void testIsNotAdmin(){
        when(mockUserRepository.findByUsername(testUser.getUsername()))
                .thenReturn(Optional.of(testUser));
        assertFalse(serviceToTest.isAdmin(testUser.getUsername()));
    }

    @Test
    public void testFindById(){
        when(mockUserRepository.findById(testUser.getId())).thenReturn(Optional.of(testUser));

        assertEquals("testUser", serviceToTest.findById(testUser.getId()).getUsername());
    }

    @Test
    public void testFindByIdThrow(){
        assertThrows(EntityNotFoundException.class, () -> serviceToTest.findById(5L));
    }


    @Test
    public void testRemoveHostRoleThrow(){
        String fakeUserName="fakeUsername";
        when(mockUserRepository.findByUsername(fakeUserName))
                .thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, ()-> serviceToTest.RemoveHostRole(fakeUserName));
    }

    @Test
    public void testUpdateUser() throws IOException {

        when(mockUserRepository.findByUsername(testUser.getUsername())).thenReturn(Optional.of(testUser));
        testUser.setProfileImg(pictureTest);
        userProfileBindingModel = new UserProfileBindingModel();
        userProfileBindingModel.setUsername(testUser.getUsername());
        userProfileBindingModel.setEmail(testUser.getEmail());
        userProfileBindingModel.setFirstName("firstName");
        userProfileBindingModel.setLastName(testUser.getLastName());
        userProfileBindingModel.setProfileImg(mockMultipartFile);
            serviceToTest.updateUser(userProfileBindingModel,testUser.getUsername());
        assertEquals(userProfileBindingModel.getFirstName(), testUser.getFirstName());
    }

    @Test
    public void testFindUserViewModelByUsername(){
        when(mockUserRepository.findByUsername(testUser.getUsername())).thenReturn(Optional.of(testUser));
        userViewModel = new UserViewModel();
        userViewModel = serviceToTest.findUserViewModelByUsername(testUser.getUsername());
        assertEquals("test", userViewModel.getFirstName());
    }

    @Test
    public void testChangeTheRoleOfUser(){
        when(mockUserRepository.findByUsername(secondUser.getUsername())).thenReturn(Optional.of(secondUser));
        serviceToTest.ChangeTheRoleOfUser(secondUser.getUsername());
        assertEquals(3, secondUser.getRole().size());
    }


   /* @Test
    public void testRemoveHostRole(){
       when(mockUserRepository.findByUsername(secondUser.getUsername())).thenReturn(Optional.of(secondUser));

        serviceToTest.RemoveHostRole(secondUser.getUsername());
        assertEquals(2, secondUser.getRole().size());
    }*/
}
