package MyProjectGradle.web;

import MyProjectGradle.models.views.TownViewModel;
import MyProjectGradle.service.TownService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class HomeController {
    private final TownService townService;

    public HomeController(TownService townService) {
        this.townService = townService;
    }
    @GetMapping("/")
    public String index(Model model) {
        List<TownViewModel> allTowns= townService.getAllTowns();

      model.addAttribute("allTowns", allTowns);
        return "/index";
    }

    @GetMapping("/info")
    public String info() {
        return "info";
    }

}
