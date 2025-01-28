package emobile.by.smertex.springtodo.mapper.realisation;

import emobile.by.smertex.springtodo.database.entity.sql.realisation.Task;
import emobile.by.smertex.springtodo.dto.update.CreateOrUpdateTaskDto;
import emobile.by.smertex.springtodo.mapper.interfaces.Mapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CreateOrUpdateTaskDtoToTaskMapper implements Mapper<CreateOrUpdateTaskDto, Task> {

    @Override
    public Task map(CreateOrUpdateTaskDto from) {
        return copy(from, new Task());
    }

    @Override
    public Task map(CreateOrUpdateTaskDto from, Task to) {
        return copy(from, to);
    }

    private Task copy(CreateOrUpdateTaskDto from, Task to) {
        to.setName(from.name());
        to.setDescription(from.description());
        to.setStatus(from.status());
        to.setPriority(from.priority());
        return to;
    }
}
