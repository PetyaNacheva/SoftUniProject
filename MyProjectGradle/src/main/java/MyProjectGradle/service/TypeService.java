package MyProjectGradle.service;

import MyProjectGradle.models.entities.Type;
import MyProjectGradle.models.enums.TypeEnum;

public interface TypeService {
    void initTypes();

    Type findByName(TypeEnum name);
}
