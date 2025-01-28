package emobile.by.smertex.springtodo.repository;

import emobile.by.smertex.springtodo.annotation.IT;
import emobile.by.smertex.springtodo.database.entity.sql.realisation.Task;
import emobile.by.smertex.springtodo.database.entity.sql.realisation.enums.Priority;
import emobile.by.smertex.springtodo.database.entity.sql.realisation.enums.Role;
import emobile.by.smertex.springtodo.database.entity.sql.realisation.enums.Status;
import emobile.by.smertex.springtodo.database.repository.sql.TaskRepository;
import emobile.by.smertex.springtodo.dto.filter.TaskFilter;
import emobile.by.smertex.springtodo.dto.filter.UserFilter;
import emobile.by.smertex.springtodo.dto.read.Pageable;
import emobile.by.smertex.springtodo.dto.security.SecurityUserDto;
import emobile.by.smertex.springtodo.service.realisation.AuthServiceImpl;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@IT
@RequiredArgsConstructor
@ExtendWith(MockitoExtension.class)
public class TaskRepositoryIT {

    private static final String USER_EMAIL_TEST = "evgenii@gmail.com";

    private static final String ADMIN_EMAIL_TEST = "smertexx@gmail.com";

    private static final Integer PAGE_NUMBER = 0;

    private static final Integer PAGE_SIZE = 5;

    private final TaskRepository taskRepository;

    @Mock
    private final AuthServiceImpl authServiceImpl;

    @Test
    void findAllByFilterUser(){
        Mockito.doReturn(Optional.of(new SecurityUserDto(USER_EMAIL_TEST, false)))
                .when(authServiceImpl)
                .takeUserFromContext();
        TaskFilter filter = TaskFilter.builder()
                .createdBy(new UserFilter(null, null))
                .status(Status.WAITING)
                .performer(new UserFilter(null, null))
                .priority(Priority.LOWEST)
                .build();
        Pageable pageable = new Pageable(PAGE_SIZE, PAGE_NUMBER);

        List<Task> tasks = taskRepository.findAllByFilter(filter, authServiceImpl.takeUserFromContext().orElseThrow(), pageable);

        tasks.stream()
                .peek(task -> assertEquals(task.getPerformer().getEmail(), USER_EMAIL_TEST))
                .peek(task -> assertEquals(task.getStatus(), filter.status()))
                .forEach(task -> assertEquals(task.getPriority(), filter.priority()));

    }

    @Test
    void findAllByFilterAdmin(){
        Mockito.doReturn(Optional.of(new SecurityUserDto(ADMIN_EMAIL_TEST, true)))
                .when(authServiceImpl)
                .takeUserFromContext();

        TaskFilter filter = TaskFilter.builder()
                .createdBy(new UserFilter(null, null))
                .status(Status.WAITING)
                .performer(new UserFilter(null, Role.USER))
                .build();

        Pageable pageable = new Pageable(PAGE_SIZE, PAGE_NUMBER);

        List<Task> tasks = taskRepository.findAllByFilter(filter, authServiceImpl.takeUserFromContext().orElseThrow(), pageable);

        assertFalse(tasks.isEmpty());

        tasks.stream()
                .peek(task -> assertNotEquals(task.getPerformer().getEmail(), ADMIN_EMAIL_TEST))
                .peek(task -> assertEquals(task.getPerformer().getRole(), Role.USER))
                .forEach(task -> assertEquals(task.getStatus(), Status.WAITING));
    }
}
