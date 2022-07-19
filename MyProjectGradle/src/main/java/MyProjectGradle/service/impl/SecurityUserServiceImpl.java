package MyProjectGradle.service.impl;

import MyProjectGradle.models.entities.Role;
import MyProjectGradle.models.entities.UserEntity;
import MyProjectGradle.repository.UserRepository;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SecurityUserServiceImpl implements UserDetailsService{
    private final UserRepository userRepository;

    public SecurityUserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        return userRepository.
                findByUsername(username).
                map(this::map).
                orElseThrow(() -> new UsernameNotFoundException("User with username " + username + " not found."));
    }

    private UserDetails map(UserEntity userEntity) {
        return new MySecurityUser(
                userEntity.getUsername(),
                userEntity.getPassword(),
                authorities(userEntity.getRole())
        );
    }
    private List<GrantedAuthority> authorities(List<Role> roleEntities) {
        return roleEntities.
                stream().
                map(r -> new SimpleGrantedAuthority("ROLE_" + r.getName().name())).
                collect(Collectors.toList());
    }
}
