package MyProjectGradle.models.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "reservations")
public class Reservation extends BaseEntity{
    @ManyToOne
    private UserEntity username;
    @Column(name = "guest_name", nullable = false)
    private String guestName;
    @Column(name = "arrival_date", nullable = false)
    private LocalDate arrivalDate;
    @Column(name = "departure_date", nullable = false)
    private LocalDate departureDate;
    @Column(name = "reserved_on", nullable = false)
    private LocalDate reservedOn;
   /* @ManyToOne
    private Town town;*/
    @ManyToOne
    private Apartment apartment;
    @Column(name = "number_of_guests", nullable = false)
    private Integer numberOfGuests;
    @Column
    private BigDecimal price;
   /* @Enumerated(EnumType.STRING)
    private GuestsEnum typeOfGuests;*/

    public Reservation() {
    }

    public UserEntity getUsername() {
        return username;
    }

    public void setUsername(UserEntity username) {
        this.username = username;
    }

    public String getGuestName() {
        return guestName;
    }

    public void setGuestName(String guestName) {
        this.guestName = guestName;
    }

    public LocalDate getArrivalDate() {
        return arrivalDate;
    }

    public void setArrivalDate(LocalDate arrivalDate) {
        this.arrivalDate = arrivalDate;
    }

    public LocalDate getDepartureDate() {
        return departureDate;
    }

    public void setDepartureDate(LocalDate departureDate) {
        this.departureDate = departureDate;
    }

    public LocalDate getReservedOn() {
        return reservedOn;
    }

    public void setReservedOn(LocalDate reservedOn) {
        this.reservedOn = reservedOn;
    }

    /*public Town getTown() {
        return town;
    }

    public void setTown(Town town) {
        this.town = town;
    }*/

    public Apartment getApartment() {
        return apartment;
    }

    public void setApartment(Apartment apartment) {
        this.apartment = apartment;
    }


    public Integer getNumberOfGuests() {
        return numberOfGuests;
    }

    public void setNumberOfGuests(Integer numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

   /* public GuestsEnum getGuests() {
        return guests;
    }

    public void setGuests(GuestsEnum guests) {
        this.guests = guests;
    }*/

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}
