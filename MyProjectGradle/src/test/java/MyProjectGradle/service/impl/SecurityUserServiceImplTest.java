package MyProjectGradle.service.impl;

import MyProjectGradle.repository.UserRepository;
import MyProjectGradle.models.entities.Role;
import MyProjectGradle.models.entities.UserEntity;
import MyProjectGradle.models.enums.RolesEnum;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@ExtendWith(MockitoExtension.class)
class SecurityUserServiceImplTest {
    private UserEntity testUser;
    private Role adminRole, userRole;

    private SecurityUserServiceImpl serviceToTest;

    @Mock
    private UserRepository mockUserRepository;

    @BeforeEach
    void init(){
        serviceToTest = new SecurityUserServiceImpl(mockUserRepository);
        adminRole = new Role();
        adminRole.setName(RolesEnum.ADMIN);
        userRole = new Role();
        userRole.setName(RolesEnum.USER);
        testUser = new UserEntity();
        testUser.setUsername("admin");
        testUser.setPassword("admin");
        testUser.setFirstName("admin");
        testUser.setLastName("admin");
        testUser.setEmail("admin@admin.bg");
        testUser.setRole(List.of(adminRole, userRole));

    }

    @Test
    void testUserNotFound(){
        Assertions.assertThrows(
                UsernameNotFoundException.class, () ->
                        serviceToTest.loadUserByUsername("someone"));
    }
    @Test
    void testUserFound() {

        // Arrange
        Mockito.when(mockUserRepository.findByUsername(testUser.getUsername())).
                thenReturn(Optional.of(testUser));
        // Act
        UserDetails actual = serviceToTest.loadUserByUsername(testUser.getUsername());
        // Assert
        String expectedRoles = "ROLE_ADMIN, ROLE_USER";
        String actualRoles = actual.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(
                Collectors.joining(", "));
        Assertions.assertEquals(actual.getUsername(), testUser.getUsername());
        Assertions.assertEquals(expectedRoles, actualRoles);
    }

}