package MyProjectGradle.web;


import MyProjectGradle.service.ApartmentService;
import MyProjectGradle.service.ReservationService;
import MyProjectGradle.service.StatService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;


@Controller
public class StatsController {

    private final StatService statService;
    private final ReservationService reservationService;
    private final ApartmentService apartmentService;

    public StatsController(StatService statService, ReservationService reservationService, ApartmentService apartmentService) {

        this.statService = statService;
        this.reservationService = reservationService;
        this.apartmentService = apartmentService;
    }

    @GetMapping("/statistics")
    public ModelAndView stats(){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("stats", statService.getStats());
        modelAndView.addObject("reservations", reservationService.findAllReservationsWithTotalProfit());
        modelAndView.addObject("apartments", apartmentService.findAllApartments());
        modelAndView.setViewName("stats");
        return modelAndView;
    }

}
