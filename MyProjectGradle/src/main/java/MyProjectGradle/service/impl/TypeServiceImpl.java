package MyProjectGradle.service.impl;

import MyProjectGradle.models.entities.Type;
import MyProjectGradle.models.enums.TypeEnum;
import MyProjectGradle.config.repository.TypeRepository;
import MyProjectGradle.service.TypeService;
import org.springframework.stereotype.Service;

@Service
public class TypeServiceImpl implements TypeService {
    private final TypeRepository typeRepository;

    public TypeServiceImpl(TypeRepository typeRepository) {
        this.typeRepository = typeRepository;
    }

    @Override
    public void initTypes() {
        if(typeRepository.count()==0){
            Type studio = new Type(TypeEnum.STUDIO, "Small apartment with one double bed and one sofa", 3);
            Type oneBed= new Type(TypeEnum.ONE_BEDROOM, "Apartment with one bedroom and one living room", 4);
            Type twoBed= new Type(TypeEnum.TWO_BEDROOMS, "Apartment with two separate bedrooms and one living room", 6);
            Type threeBed = new Type(TypeEnum.THREE_BEDROOMS, "Big apartment with 3 big separate bedrooms and one big living room", 8);

            typeRepository.save(studio);
            typeRepository.save(oneBed);
            typeRepository.save(twoBed);
            typeRepository.save(threeBed);
        }
    }

    @Override
    public Type findByName(TypeEnum name) {
        return typeRepository.findByType(name);
    }
}
