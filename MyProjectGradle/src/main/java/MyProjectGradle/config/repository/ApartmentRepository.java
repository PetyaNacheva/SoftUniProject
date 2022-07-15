package MyProjectGradle.config.repository;



import MyProjectGradle.models.entities.Apartment;
import MyProjectGradle.models.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface ApartmentRepository extends JpaRepository<Apartment, Long> {
    List<Apartment> findAllByOwner(UserEntity user);
   
    List<Apartment> findAllByTown_Id(Long id);
    //Apartment findByIdAndOwner_Username(Long id, String username);
    Optional<Apartment> findApartmentByName(String name);
   List<Apartment> findAllByTown_Name(String name);
   /*@Query(value = "select sum(a.price*a.reservations) from Apartment as a " +
           "join Reservation as r where r.arrivalDate is ")
    BigDecimal getTotalPriceByReservation();*/
}
