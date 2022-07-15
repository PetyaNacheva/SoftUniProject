package MyProjectGradle.service;



import MyProjectGradle.models.service.LogServiceModel;

import java.util.List;


public interface LogService {

    void logClear();
    List<LogServiceModel> findAll();

    void createLog(String action, Long recipeId);

}
