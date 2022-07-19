package MyProjectGradle.service.impl;

import MyProjectGradle.models.views.UserViewModel;
import MyProjectGradle.repository.UserRepository;
import MyProjectGradle.models.entities.Role;
import MyProjectGradle.models.entities.UserEntity;
import MyProjectGradle.models.enums.RolesEnum;
import MyProjectGradle.models.service.UserServiceModel;
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

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest{
    private UserDetailsService userDetailsService;
    private UserServiceImpl serviceToTest;
    private PasswordEncoder passwordEncoder;
    private UserViewModel userViewModel;
    private UserEntity testUser;
    private UserEntity secondUser;
    private List<GrantedAuthority> authorities;
    private Role userRole, adminRole, hostRole;
    

    @Mock
    UserRepository mockUserRepository;
    @Mock
    RoleService mockRoleService;
    @Mock
    PictureService mockPictureService;
    @Mock
    ModelMapper modelMapper = new ModelMapper();


    @BeforeEach
    void setUp(){
        passwordEncoder = new Pbkdf2PasswordEncoder();
        serviceToTest= new UserServiceImpl(userDetailsService, mockUserRepository, modelMapper, mockRoleService, passwordEncoder, mockPictureService);
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
        mockUserRepository.save(testUser);

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

   /* @Test
    public void testFindUserViewModelByUsername(){

        when(mockUserRepository.findByUsername(testUser.getUsername())).thenReturn(Optional.of(testUser));
        UserViewModel userViewModel = serviceToTest.findUserViewModelByUsername(testUser.getUsername());
        assertEquals(userViewModel.getFirstName(), testUser.getFirstName());
    }*/
}
