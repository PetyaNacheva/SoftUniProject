package MyProjectGradle.models.binding;

import MyProjectGradle.models.entities.Picture;
import MyProjectGradle.models.entities.Role;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.*;
import java.util.List;

public class UserRegisterBindingModel {
    @NotBlank(message = "username is required")
    @Size(min = 3, max=20, message = "username must be at least 3 chars")
    private String username;
    @NotBlank(message = "first name is required")
    @Size(min = 3, max=20, message = "first name must be at least 3 chars")
    private String firstName;
    @NotBlank(message = "last name is required")
    @Size(min = 3, max=20, message = "last name must be at least 3 chars")
    private String lastName;
    @Email(message = "email must be valid")
    private String email;
    @NotBlank(message = "password can not be empty")
    @Size(min = 5, max=12, message = "password must be minimum 5 chars")
    private String password;
    @NotBlank(message = "confirmedPassword can not be empty")
    @Size(min = 5, max=12, message = "password must be minimum 5 chars")
    private String confirmPassword;
    @NotEmpty(message = "Field must be filled")
    @Pattern(regexp = "\\+*[0-9]{10,12}")
    private String phone;

    private MultipartFile profileImg;

    private List<Role> role;


    public UserRegisterBindingModel() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    public List<Role> getRole() {
        return role;
    }

    public void setRole(List<Role> role) {
        this.role = role;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public MultipartFile getProfileImg() {
        return profileImg;
    }

    public void setProfileImg(MultipartFile profileImg) {
        this.profileImg = profileImg;
    }
}
