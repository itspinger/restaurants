package raf.rs.restaurants.userservice.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jms.support.converter.MappingJackson2MessageConverter;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.jms.support.converter.MessageType;
import raf.rs.restaurants.userservice.domain.User;
import raf.rs.restaurants.userservice.dto.UserDto;

@Configuration
public class MapperConfiguration {

    @Bean
    @Primary
    public ModelMapper modelMapper() {
        final ModelMapper modelMapper = new ModelMapper();
        final TypeMap<User, UserDto> typeMap = modelMapper.createTypeMap(User.class, UserDto.class);
        typeMap.addMappings(mapper -> mapper.map(User::getRoles, UserDto::setRoles));
        return modelMapper;
    }

    @Bean
    public ObjectMapper objectMapper() {
        final ObjectMapper objectMapper = new ObjectMapper();
        final JavaTimeModule module = new JavaTimeModule();
        module.addSerializer(LocalDateSerializer.INSTANCE);
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS); // Optional: Use ISO-8601 format
        return objectMapper;
    }

    @Bean // Serialize message content to json using TextMessage
    public MessageConverter jacksonJmsMessageConverter() {
        final MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();
        converter.setObjectMapper(this.objectMapper());
        converter.setTargetType(MessageType.TEXT);
        converter.setTypeIdPropertyName("_type");
        return converter;
    }
}
