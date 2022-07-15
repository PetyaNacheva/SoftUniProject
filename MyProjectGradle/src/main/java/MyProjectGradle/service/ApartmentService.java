package MyProjectGradle.service;

import MyProjectGradle.models.entities.Apartment;
import MyProjectGradle.models.entities.Picture;
import MyProjectGradle.models.entities.UserEntity;
import MyProjectGradle.models.service.ApartmentServiceModel;
import MyProjectGradle.models.views.ApartmentDetailsViewModel;
import MyProjectGradle.models.views.ApartmentViewModel;

import javax.servlet.http.HttpSession;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

public interface ApartmentService {
    boolean saveApartment(ApartmentServiceModel apartmentServiceModel, String name);

    List<ApartmentViewModel> findAllApartmentsByUsername(UserEntity user);

    ApartmentDetailsViewModel findById(Long id);

    boolean deleteApartment(Long id);


    List<ApartmentViewModel> findAllApartmentsByTownAndUser(Long town_id, Long user_id);

    Apartment findApartmentById(Long apartmentId);


    Apartment findByApartmentByApartmentName(String apartmentName);

    void updateApartmentPictures(Apartment apartment);

    boolean canDelete(Long id, String username);

    boolean canUpdate(Long id, String username);

    Long updateApartment(ApartmentServiceModel apartment);

    ApartmentDetailsViewModel findApartmentDetailsViewModelById(Long id);

    List<Apartment> getAllApartments();

    List<ApartmentViewModel> findAllAvailableApartmentsInPeriod(String town, LocalDate arrivalDate, LocalDate departureDate);

    String isAvailable(String name, LocalDate arrivalDate, LocalDate departureDate);

    String findAllApartments();
}
