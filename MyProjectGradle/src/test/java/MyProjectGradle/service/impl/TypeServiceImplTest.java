package MyProjectGradle.service.impl;

import MyProjectGradle.models.entities.Type;
import MyProjectGradle.models.enums.TypeEnum;
import MyProjectGradle.repository.TypeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
@ExtendWith(MockitoExtension.class)
class TypeServiceImplTest {

    private TypeServiceImpl typeService;
    private Type testTypeStudio, testTypeOneBed;

    @Mock
    TypeRepository typeRepository;

    @BeforeEach
    void setUp(){
        typeService = new TypeServiceImpl(typeRepository);
        testTypeStudio = new Type(TypeEnum.STUDIO, "studio test", 3);
        testTypeOneBed = new Type(TypeEnum.ONE_BEDROOM, "onebedroom test", 4);
        typeRepository.save(testTypeStudio);
        typeRepository.save(testTypeOneBed);
    }

    @Test
    public void testFindByName(){
        when(typeRepository.findByType(testTypeStudio.getType())).thenReturn(testTypeStudio);
        assertEquals("studio test", typeService.findByName(testTypeStudio.getType()).getDescription());
    }

}