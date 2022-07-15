package MyProjectGradle.models.binding;

import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.*;


public class UserProfileBindingModel {
    private Long id;
    private String username;
    @NotBlank(message = "firstName can not be empty")
    @Size(min = 3, message = "first name must be at least 3 chars")
    private String firstName;
    @NotBlank(message = "lastName can not be empty")
    @Size(min = 3, message = "first name must be at least 3 chars")
    private String lastName;
    private String email;
    @NotEmpty(message = "Field must be filled")
    @Pattern(regexp = "\\+*[0-9]{10,12}")
    private String phone;
    private MultipartFile profileImg;

    public UserProfileBindingModel() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public MultipartFile getProfileImg() {
        return profileImg;
    }

    public void setProfileImg(MultipartFile profileImg) {
        this.profileImg = profileImg;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
