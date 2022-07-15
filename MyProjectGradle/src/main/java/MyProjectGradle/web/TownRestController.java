package MyProjectGradle.web;

import MyProjectGradle.models.views.TownViewModel;
import MyProjectGradle.service.TownService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/towns/api")
public class TownRestController {
    private final TownService townService;

    public TownRestController(TownService townService) {
        this.townService = townService;
    }


    @GetMapping("/top3")
    public ResponseEntity<List<TownViewModel>> getTop3Apartments(){
        List<TownViewModel> townViewModels = townService.getTop3TownsWithMostApartments();
        return ResponseEntity.ok().body(townViewModels);
    }

    // TODO: 7/7/2022 towns all to be implemented with rest controller
}
