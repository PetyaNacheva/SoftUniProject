package MyProjectGradle.service.impl;


import MyProjectGradle.models.entities.Apartment;
import MyProjectGradle.models.entities.Log;
import MyProjectGradle.models.entities.UserEntity;
import MyProjectGradle.models.service.LogServiceModel;
import MyProjectGradle.repository.LogRepository;
import MyProjectGradle.service.ApartmentService;
import MyProjectGradle.service.LogService;;
import MyProjectGradle.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class LogServiceImpl implements LogService {

    private final LogRepository logRepository;
    private final ApartmentService apartmentService;
    private final UserService userService;
    private final ModelMapper modelMapper;

    public LogServiceImpl(LogRepository logRepository, ApartmentService apartmentService, UserService userService, ModelMapper modelMapper) {
        this.logRepository = logRepository;
        this.apartmentService = apartmentService;

        this.userService = userService;
        this.modelMapper = modelMapper;
    }

    @Override
    public void logClear() {
        logRepository.deleteAll();
    }

    @Override
    public List<LogServiceModel> findAll() {
        return logRepository.findAll().stream()
                .map(logEntity -> this.modelMapper.map(logEntity, LogServiceModel.class))
                .collect(Collectors.toList());

    }

    @Override
    public void createLog(String action, Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Log log = new Log();
        log.setAction(action);
        log.setDateTime(LocalDateTime.now());
        log.setUser(this.modelMapper.map(this.userService.findByUsername(authentication.getName()), UserEntity.class));
        log.setApartment(modelMapper.map(apartmentService.findApartmentById(id), Apartment.class));
        logRepository.save(log);
    }
}
