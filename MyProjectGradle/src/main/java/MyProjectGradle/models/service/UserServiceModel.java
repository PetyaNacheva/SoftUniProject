package MyProjectGradle.models.service;


import org.springframework.web.multipart.MultipartFile;

public class UserServiceModel {
    private Long id;
    private String username;

    private String firstName;

    private String lastName;

    private String password;

    private String email;
    private String phone;

    private MultipartFile profileImg;


    public UserServiceModel() {
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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
