package MyProjectGradle.service;

import MyProjectGradle.models.binding.TownUpdateBindingModel;
import MyProjectGradle.models.entities.Town;
import MyProjectGradle.models.service.TownServiceModel;
import MyProjectGradle.models.views.TownViewModel;
import MyProjectGradle.models.views.TownDetailsViewModel;

import java.io.IOException;
import java.util.List;

public interface TownService {
    //void initTowns();


    Town findByName(String town);

    List<TownViewModel> getAllTowns();

    List<String> getTowns();

    Town findById(Long id);

    boolean saveTown(TownServiceModel townServiceModel, String userIdentifier) throws IOException;

    List<TownViewModel> getTop3TownsWithMostApartments();

    TownDetailsViewModel findTownDetailsViewModelBy(Long id);

    TownServiceModel findByTownName(String name);

    void updateTown(Long id, TownUpdateBindingModel townUpdateBindingModel, String userIdentifier) throws IOException;

    boolean canDelete(Long id, String userIdentifier);
    boolean canUpdate(Long id, String userIdentifier);

    void deleteTown(Long id);
}
