package emobile.by.smertex.springtodo.mapper.realisation;

import emobile.by.smertex.springtodo.database.entity.sql.realisation.User;
import emobile.by.smertex.springtodo.dto.read.ReadUserDto;
import emobile.by.smertex.springtodo.mapper.interfaces.Mapper;
import org.springframework.stereotype.Component;

@Component
public class UserToReadUserDtoMapper implements Mapper<User, ReadUserDto> {
    @Override
    public ReadUserDto map(User from) {
        return ReadUserDto.builder()
                .id(from.getId())
                .email(from.getEmail())
                .role(from.getRole())
                .build();
    }
}
