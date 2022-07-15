package MyProjectGradle.config;

import com.cloudinary.*;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;

import java.util.Map;

@Configuration
public class MyProjectConfiguration {
    private final  CloudinaryConfig config;
    public MyProjectConfiguration(CloudinaryConfig config) {

        this.config = config;
    }

    @Bean
    public ModelMapper modelMapper(){
        return new ModelMapper();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new Pbkdf2PasswordEncoder();
    }

    @Bean
    public Cloudinary cloudinary(){
        return new Cloudinary( Map.of(
                "cloud_name", config.getCloudName(),
                "api_key", config.getApiKey(),
                "api_secret", config.getApiSecret()
        ));
    }
}
