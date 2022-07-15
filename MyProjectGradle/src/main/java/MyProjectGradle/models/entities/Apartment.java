package MyProjectGradle.models.entities;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "apartments")
public class Apartment extends BaseEntity{
    @Column(unique = true, nullable = false)
    private String name;
    @ManyToOne
    private Type type;
    @ManyToOne
    private Town town;
    @Column(nullable = false)
    private BigDecimal price;
   // @Column(nullable = false)
   // private Integer capacity;
    @Column(nullable = false)
    private String address;
    @ManyToOne
    private UserEntity owner;

   @OneToMany
   private List<Picture> pictures;
   @OneToMany(mappedBy = "apartment")
   private List<Reservation> reservations;


    public Apartment() {
        reservations=new ArrayList<>();
        pictures=new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public Town getTown() {
        return town;
    }

    public void setTown(Town town) {
        this.town = town;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public UserEntity getOwner() {
        return owner;
    }

    public void setOwner(UserEntity owner) {
        this.owner = owner;
    }

    public List<Picture> getPictures() {
        return pictures;
    }

    public void setPictures(List<Picture> pictures) {
        this.pictures = pictures;
    }

    public List<Reservation> getReservations() {
        return reservations;
    }

    public void setReservations(List<Reservation> reservations) {
        this.reservations = reservations;
    }
}
