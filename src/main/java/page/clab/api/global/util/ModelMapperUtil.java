package page.clab.api.global.util;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class ModelMapperUtil {

    private static final ModelMapper modelMapper = new ModelMapper();

    public static ModelMapper getModelMapper() {
        return modelMapper;
    }

}