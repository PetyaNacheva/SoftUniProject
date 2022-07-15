package MyProjectGradle.config.repository;


import MyProjectGradle.models.entities.Type;
import MyProjectGradle.models.enums.TypeEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TypeRepository extends JpaRepository<Type, Long> {

    Type findByType(TypeEnum name);
}
