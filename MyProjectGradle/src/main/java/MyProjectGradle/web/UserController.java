package MyProjectGradle.web;


import MyProjectGradle.models.binding.UserProfileBindingModel;
import MyProjectGradle.models.binding.UserRegisterBindingModel;
import MyProjectGradle.models.entities.Picture;
import MyProjectGradle.models.entities.UserEntity;
import MyProjectGradle.models.service.UserServiceModel;
import MyProjectGradle.models.views.UserViewModel;
import MyProjectGradle.service.PictureService;
import MyProjectGradle.service.UserService;
import MyProjectGradle.service.impl.MySecurityUser;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.io.IOException;

@Controller
@RequestMapping("/users")
public class UserController {
    private final UserService userService;
    private final ModelMapper modelMapper;
    private final PictureService pictureService;

    public UserController(UserService userService, ModelMapper modelMapper, PictureService pictureService) {
        this.userService = userService;
        this.modelMapper = modelMapper;
        this.pictureService = pictureService;
    }
    @ModelAttribute
    public UserRegisterBindingModel userRegisterBindingModel() {
        return new UserRegisterBindingModel();
    }

    @GetMapping("/register")
    public String register(){
        return "register";
    }

    @Transactional
    @PostMapping("/register")
    public String registerConfirmed(@Valid UserRegisterBindingModel userRegisterBindingModel, BindingResult bindingResult, RedirectAttributes redirectAttributes){

        if(bindingResult.hasErrors() || !userRegisterBindingModel.getPassword().equals(userRegisterBindingModel.getConfirmPassword())){
            redirectAttributes.addFlashAttribute("userRegisterBindingModel", userRegisterBindingModel);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.userRegisterBindingModel", bindingResult);
        return "redirect:register";
        }

        if (userService.isUsernameFree(userRegisterBindingModel.getUsername())) {
            userService.saveUser(modelMapper.map(userRegisterBindingModel, UserServiceModel.class));
            return "redirect:/";
        }else{ redirectAttributes.addFlashAttribute("userRegisterBindingModel", userRegisterBindingModel);
            redirectAttributes.addFlashAttribute("isExist", true);
            return "redirect:register";
        }

    }

    @GetMapping("/login")
    public String login(){
        return "login";
    }

     @PostMapping("/login-error")
      public String loginFailed(@ModelAttribute(UsernamePasswordAuthenticationFilter.SPRING_SECURITY_FORM_USERNAME_KEY) String userName,
                                RedirectAttributes redirectAttributes){

          redirectAttributes.addFlashAttribute("username", userName);
          redirectAttributes.addFlashAttribute("bad_credentials", true);

          return "redirect:login";
      }

    @GetMapping("/profile")
    public String profile(Model model, @AuthenticationPrincipal MySecurityUser principal){
        if(!model.containsAttribute("userProfile")){
           UserViewModel user= userService.findUserViewModelByUsername(principal.getUserIdentifier());
            model.addAttribute("userProfile", modelMapper.map(user, UserViewModel.class));
        }
        return "my-profile";
    }

    @GetMapping("/profile/update")
    public String profileUpdate(Model model, @AuthenticationPrincipal MySecurityUser principal){
        if(!model.containsAttribute("userEditViewModel")){
            UserViewModel userViewModel = userService.findUserViewModelByUsername(principal.getUserIdentifier());
            model.addAttribute("userProfileBindingModel", userViewModel );
        }
        return "my-profile-update";
    }

    @PatchMapping("/profile/update")
    public String profileConfirmed(@Valid UserProfileBindingModel userProfileBindingModel,
                                   @AuthenticationPrincipal MySecurityUser principal,
                                   BindingResult bindingResult,
                                   RedirectAttributes redirectAttributes) throws IOException {

        if(bindingResult.hasErrors()){
            redirectAttributes.addFlashAttribute("userProfileBindingModel", userProfileBindingModel);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.userProfileBindingResult", bindingResult);
            return "redirect:profile/update";
        }
        userService.updateUser(userProfileBindingModel, principal.getUserIdentifier());
       return "redirect:/";
    }

}
