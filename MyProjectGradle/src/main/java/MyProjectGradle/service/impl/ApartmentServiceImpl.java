package MyProjectGradle.service.impl;

import MyProjectGradle.models.entities.*;
import MyProjectGradle.models.service.ApartmentServiceModel;
import MyProjectGradle.models.views.ApartmentDetailsViewModel;
import MyProjectGradle.models.views.ApartmentStatisticViewModel;
import MyProjectGradle.models.views.ApartmentViewModel;
import MyProjectGradle.models.views.ReservationStatViewModel;
import MyProjectGradle.repository.ApartmentRepository;
import MyProjectGradle.service.*;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;

import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ApartmentServiceImpl implements ApartmentService {
    private final ApartmentRepository apartmentRepository;
    private final ModelMapper modelMapper;
    private final UserService userService;
    private final TownService townService;
    private final TypeService typeService;

    private final PictureService pictureService;
    private final ReservationService reservationService;

    public ApartmentServiceImpl(ApartmentRepository apartmentRepository, ModelMapper modelMapper, UserService userService, TownService townService, TypeService typeService,  PictureService pictureService, ReservationService reservationService) {
        this.apartmentRepository = apartmentRepository;
        this.modelMapper = modelMapper;
        this.userService = userService;
        this.townService = townService;
        this.typeService = typeService;

        this.pictureService = pictureService;
        this.reservationService = reservationService;
    }
    @Transactional
    @Override
    public boolean saveApartment(ApartmentServiceModel apartmentServiceModel, String name) throws IOException {

            Apartment apartment = modelMapper.map(apartmentServiceModel, Apartment.class);
           UserEntity user =userService.findByUsername(name);
            userService.ChangeTheRoleOfUser(user.getUsername());

            Town town = townService.findByName(apartmentServiceModel.getTown());
            Type type = typeService.findByName(apartmentServiceModel.getType());
            apartment.setType(type);
            MultipartFile picture = apartmentServiceModel.getPicture();
            Picture pictureFile = pictureService.createPicture(picture, picture.getOriginalFilename(), user.getUsername(), apartment.getName());
            apartment.setPictures(List.of(pictureFile));
            apartment.setTown(town);
            apartment.setOwner(user);

            apartmentRepository.save(apartment);

        if(apartment.getId()==null){
            return false;
        }
        return true;
    }

    @Override
    public List<ApartmentViewModel> findAllApartmentsByUsername(UserEntity user) {
      return apartmentRepository.findAllByOwner(user).stream().map(a->{
                 ApartmentViewModel ap=modelMapper.map(a, ApartmentViewModel.class);
            List<Picture> pictures = pictureService.findPictureByApartmentName(a.getName());
            ap.setPicture(pictures.get(0).getUrl());
                 return ap;}).collect(Collectors.toList());
    }

    @Override
    public ApartmentDetailsViewModel findById(Long id) {
        Apartment apartment = apartmentRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Apartment"));
        List<Picture> picturesList = pictureService.findPictureByApartmentName(apartment.getName());
        apartment.setPictures(picturesList);
        ApartmentDetailsViewModel detailsAp = modelMapper.map(apartment, ApartmentDetailsViewModel.class);
        detailsAp.setAddress(apartment.getTown().getName()+" "+apartment.getAddress());
        detailsAp.setCapacity(apartment.getType().getCapacity());
        detailsAp.setOwner(apartment.getOwner().getUsername());

        return detailsAp;
    }
    @Transactional
    @Override
    public boolean deleteApartment(Long id) {

            Apartment apartment = apartmentRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Apartment"));
               apartment.setPictures(new ArrayList<>());
                updateApartmentPictures(apartment);
                List<Picture> pictures = pictureService.findPictureByApartmentName(apartment.getName());
                pictures.forEach(picture -> {
                    pictureService.findPictureByPublicId(picture.getPublicId());
                    pictureService.delete(picture.getPublicId());
                });
                userService.RemoveHostRole(apartment.getOwner().getUsername());
                apartmentRepository.deleteById(id);
        Apartment apartmentById = apartmentRepository.findById(id).orElse(null);
        if(apartmentById==null){
            return true;
        }else {return false;}
    }


    @Override
    public List<ApartmentViewModel> findAllApartmentsByTownAndUser(Long town_id, Long user_id) {
        List<ApartmentViewModel> result = new ArrayList<>();
        List<Apartment> allByTown_id = apartmentRepository.findAllByTown_Id(town_id);
        for (Apartment apartment : allByTown_id) {
            if(apartment.getOwner().getId().equals(user_id)){
                ApartmentViewModel currAp = new ApartmentViewModel();
                currAp.setName(apartment.getName());
                currAp.setAddress(apartment.getAddress());
                currAp.setPicture(apartment.getPictures().get(0).getUrl());
                currAp.setId(apartment.getId());
                result.add(currAp);
            };
        }
        return result;

        // TODO: 7/23/2022 changed sintaksis because of the tests 
    }

    @Override
    public Apartment findApartmentById(Long apartmentId) {
        return apartmentRepository.findById(apartmentId).orElseThrow(() -> new EntityNotFoundException("Apartment"));
    }

    @Override
    public Apartment findByApartmentByApartmentName(String apartmentName) {
        return apartmentRepository.findApartmentByName(apartmentName).orElseThrow(()-> new EntityNotFoundException("Apartment"));
    }

    @Override
    public void updateApartmentPictures(Apartment apartment) {
        apartmentRepository.save(apartment);
    }

    @Transactional
    @Override
    public boolean canDelete(Long id, String username) {
        ApartmentServiceModel apartment = apartmentRepository.findById(id).map(a->{
            ApartmentServiceModel apServiceModel = modelMapper.map(a, ApartmentServiceModel.class);
            apServiceModel.setReservationList(a.getReservations());
            return apServiceModel;
        }).orElseThrow(() -> new EntityNotFoundException("Apartment"));
        if(userService.isAdmin(username)||userService.findByUsername(username).getUsername().equals(apartment.getOwner().getUsername())) {
            return ((apartment != null && apartment.getReservationList() == null) || apartment.getReservationList().size()==0);
        }
        return false;
    }

    @Override
    public boolean canUpdate(Long id, String username) {
        ApartmentServiceModel apartment = apartmentRepository.findById(id).map(a->modelMapper.map(a, ApartmentServiceModel.class)).orElseThrow(() -> new EntityNotFoundException("Apartment"));
       return (apartment != null && apartment.getOwner().getUsername().equals(username)||userService.isAdmin(username));
    }


    @Override
    public Long updateApartment(ApartmentServiceModel apartmentServiceModel) {
        Apartment apartment = apartmentRepository.findById(apartmentServiceModel.getId()).orElseThrow(() -> new EntityNotFoundException("Apartment"));

        apartment.setName(apartmentServiceModel.getName());
        apartment.setAddress(apartmentServiceModel.getAddress());
        apartment.setPrice(apartmentServiceModel.getPrice());
        List<Picture> pictures = apartment.getPictures();
        pictures.stream().forEach(p-> pictureService.UpdateApartmentName(p, apartmentServiceModel.getName()));
        apartment.setPictures(pictures);
        apartmentRepository.save(apartment);
        Apartment apartment1 = apartmentRepository.findById(apartment.getId()).orElseThrow(() -> new EntityNotFoundException("Apartment"));
        return apartment1.getId();
    }
    @Transactional
    @Override
    public ApartmentDetailsViewModel findApartmentDetailsViewModelById(Long id) {
        Apartment apartment = apartmentRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Apartment"));
        ApartmentDetailsViewModel apartmentViewModel = modelMapper.map(apartment, ApartmentDetailsViewModel.class);
        apartmentViewModel.setPictures(List.of(apartment.getPictures().get(0)));
        apartmentViewModel.setCapacity(apartment.getType().getCapacity());
        apartmentViewModel.setTown(apartment.getTown().getName());

        return apartmentViewModel;
    }

    @Override
    public List<Apartment> getAllApartments() {
        return apartmentRepository.findAll();
    }

    @Override
    public List<ApartmentViewModel> findAllAvailableApartmentsInPeriod(String town, LocalDate arrivalDate, LocalDate departureDate) {
        List<Apartment> allApartmentsByTown_name = apartmentRepository.findAllByTown_Name(town);
        List<ApartmentViewModel> apartmentViewModels = new ArrayList<>();
        for (Apartment apartment : allApartmentsByTown_name) {
           if(isAvailable(apartment.getName(), arrivalDate, departureDate).equals("available")){
               apartmentViewModels.add(modelMapper.map(apartment, ApartmentViewModel.class));
           }
        }

        return apartmentViewModels;
    }
    @Override
    public String isAvailable(String apartment, LocalDate arrivalDate, LocalDate departureDate){
        List<Reservation> reservationsByApartment = reservationService.findAllByApartmentsByName(apartment);
        List<Reservation> sorted = reservationsByApartment.stream().sorted(Comparator.comparing(Reservation::getArrivalDate)).toList();
        for (Reservation reservation : sorted) {
            LocalDate currResArrDate=reservation.getArrivalDate();
            LocalDate currResDepDate = reservation.getDepartureDate();
            if(((arrivalDate.isEqual(currResArrDate)||arrivalDate.isAfter(currResArrDate))&&
                    arrivalDate.isBefore(currResDepDate))
            ||((departureDate.equals(currResDepDate)||departureDate.isBefore(currResDepDate))&&
                    departureDate.isAfter(currResArrDate))){
                return findNextAvailableDates(sorted, reservation);
            }
        }
        return "available";
    }

    private String findNextAvailableDates(List<Reservation> reservationsByApartment, Reservation reservation) {
        int indexOfCurrReservation = reservationsByApartment.indexOf(reservation);
        String result="";
        if(indexOfCurrReservation+1<reservationsByApartment.size()) {
            for (int i = indexOfCurrReservation+1; i < reservationsByApartment.size(); i++) {
                long days = ChronoUnit.DAYS.between (reservationsByApartment.get(i-1).getDepartureDate(), reservationsByApartment.get(i).getArrivalDate());
                if(days>2){
                    LocalDate lastDepartureDate = reservationsByApartment.get(i-1).getDepartureDate();
                    LocalDate nextResArrivalDate = reservationsByApartment.get(i).getArrivalDate();
                    result= "Next available days are "+days+", between "+lastDepartureDate+" "+nextResArrivalDate;
                    break;
                }else {
                   result= "Next available days are after "+reservationsByApartment.get(i).getDepartureDate();
                }
            }
        }else {
            result = "Next available days are after "+reservation.getDepartureDate();
        }
        return result;
    }

    @Override
    public String findAllApartments() {
        int count = apartmentRepository.findAll().size();
        return String.format("Total count of all apartments: %d", count);
    }

    @Override
    public ApartmentStatisticViewModel getStatistic(Long id) {
        Apartment apartment = apartmentRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Apartment"));
        BigDecimal futureProfit = BigDecimal.valueOf(0);
        BigDecimal pastProfit = BigDecimal.valueOf(0);
        List<ReservationStatViewModel> pastReservations = new ArrayList<>();
        List<ReservationStatViewModel> futureReservations = new ArrayList<>();
        for (Reservation reservation : apartment.getReservations()) {
            if (reservation.getArrivalDate().isAfter(LocalDate.now().minusDays(1)) && reservation.getArrivalDate().isBefore(LocalDate.now().plusDays(31))) {
                futureReservations.add(modelMapper.map(reservation, ReservationStatViewModel.class));
                futureProfit = futureProfit.add(reservation.getPrice());
            } else if (reservation.getArrivalDate().isBefore(LocalDate.now()) && reservation.getArrivalDate().isAfter(LocalDate.now().minusDays(31))) {
                pastProfit.add(reservation.getPrice());
                pastReservations.add(modelMapper.map(reservation, ReservationStatViewModel.class));
            }
        }
        ApartmentStatisticViewModel apartmentStatisticViewModel = modelMapper.map(apartment, ApartmentStatisticViewModel.class);
        apartmentStatisticViewModel.setName(apartment.getName());

        apartmentStatisticViewModel.setComingReservations(futureReservations);
        apartmentStatisticViewModel.setPast30DaysReservations(pastReservations);
        apartmentStatisticViewModel.setProfitForFutureMonth(futureProfit);
        apartmentStatisticViewModel.setProfitFromPastMonth(pastProfit);
        return apartmentStatisticViewModel;
    }
}
