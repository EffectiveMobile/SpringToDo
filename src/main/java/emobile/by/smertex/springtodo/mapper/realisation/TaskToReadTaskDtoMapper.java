package emobile.by.smertex.springtodo.mapper.realisation;

import emobile.by.smertex.springtodo.database.entity.sql.realisation.Task;
import emobile.by.smertex.springtodo.dto.read.ReadTaskDto;
import emobile.by.smertex.springtodo.mapper.interfaces.Mapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TaskToReadTaskDtoMapper implements Mapper<Task, ReadTaskDto> {

    private final UserToReadUserDtoMapper userToReadUserDtoMapper;

    @Override
    public ReadTaskDto map(Task from) {
        return ReadTaskDto.builder()
                .id(from.getId())
                .name(from.getName())
                .description(from.getDescription())
                .status(from.getStatus())
                .priority(from.getPriority())
                .performer(userToReadUserDtoMapper.map(from.getPerformer()))
                .build();
    }
}
