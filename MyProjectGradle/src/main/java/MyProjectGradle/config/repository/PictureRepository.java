package MyProjectGradle.config.repository;

import MyProjectGradle.models.entities.Picture;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PictureRepository extends JpaRepository<Picture, Long> {

    void deleteByPublicId(String publicId);
    List<Picture>findAllByApartmentName(String name);


    Optional<Picture> findByPublicId(String publicId);

    Optional<Picture> findByTitle(String s);
    Optional<Picture>findByUserName(String username);
}
