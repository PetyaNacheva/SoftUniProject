package MyProjectGradle.config.repository;

import MyProjectGradle.models.entities.Town;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TownRepository extends JpaRepository<Town, Long> {

    Optional<Town> findByName(String name);

    @Query(value = "select t from Town as t order by size(t.apartments) DESC , t.name ASC")
    List<Town> findByAndApartments();
}
