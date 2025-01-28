package emobile.by.smertex.springtodo.service;

import emobile.by.smertex.springtodo.annotation.IT;
import emobile.by.smertex.springtodo.service.exception.UserNotFoundInDatabaseException;
import emobile.by.smertex.springtodo.database.entity.sql.realisation.Task;
import emobile.by.smertex.springtodo.database.entity.sql.realisation.enums.Priority;
import emobile.by.smertex.springtodo.database.entity.sql.realisation.enums.Role;
import emobile.by.smertex.springtodo.database.entity.sql.realisation.enums.Status;
import emobile.by.smertex.springtodo.dto.filter.TaskFilter;
import emobile.by.smertex.springtodo.dto.filter.UserFilter;
import emobile.by.smertex.springtodo.dto.read.Pageable;
import emobile.by.smertex.springtodo.dto.read.ReadTaskDto;
import emobile.by.smertex.springtodo.dto.security.SecurityUserDto;
import emobile.by.smertex.springtodo.dto.update.CreateOrUpdateTaskDto;
import emobile.by.smertex.springtodo.service.exception.UpdateException;
import emobile.by.smertex.springtodo.service.realisation.AuthServiceImpl;
import emobile.by.smertex.springtodo.service.realisation.TaskServiceImpl;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@IT
@RequiredArgsConstructor
@ExtendWith(MockitoExtension.class)
public class TaskServiceImplIT {

    private static final String USER_EMAIL_TEST = "evgenii@gmail.com";

    private static final String ADMIN_EMAIL_TEST = "smertexx@gmail.com";

    private static final UUID TASK_ID_WHERE_PERFORMER_USER_TEST = UUID.fromString("a9099b32-e5b2-41aa-9ab6-d4d461549c70");

    private static final Integer PAGE_NUMBER = 0;

    private static final Integer PAGE_SIZE = 2;

    @InjectMocks
    private final TaskServiceImpl taskServiceImpl;

    @MockBean
    private final AuthServiceImpl authServiceImpl;

    @Test
    void findByFilterWhereUserPerformer(){
        Mockito.doReturn(Optional.of(new SecurityUserDto(USER_EMAIL_TEST, false)))
                .when(authServiceImpl)
                .takeUserFromContext();
        TaskFilter filter = TaskFilter.builder()
                .createdBy(new UserFilter(null, null))
                .performer(new UserFilter(null, null))
                .build();
        Pageable pageable = new Pageable(PAGE_SIZE, PAGE_NUMBER);
        List<ReadTaskDto> tasks = taskServiceImpl.findAllByFilter(filter, pageable);

        assertFalse(tasks.isEmpty());
        tasks.forEach(task -> assertEquals(task.performer().email(), USER_EMAIL_TEST));
    }

    @Test
    void findByFilterWhereUserNotPerformer(){
        Mockito.doReturn(Optional.of(new SecurityUserDto(USER_EMAIL_TEST, false)))
                .when(authServiceImpl)
                .takeUserFromContext();
        TaskFilter filter = TaskFilter.builder()
                .createdBy(new UserFilter(null, null))
                .performer(new UserFilter(ADMIN_EMAIL_TEST, null))
                .build();
        Pageable pageable = new Pageable(PAGE_SIZE, PAGE_NUMBER);
        List<ReadTaskDto> tasks = taskServiceImpl.findAllByFilter(filter, pageable);
        assertTrue(tasks.isEmpty());

        Mockito.doReturn(Optional.of(new SecurityUserDto(ADMIN_EMAIL_TEST, false)))
                .when(authServiceImpl)
                .takeUserFromContext();
        List<ReadTaskDto> tasksWhereAdminPerformer = taskServiceImpl.findAllByFilter(filter, pageable);
        assertFalse(tasksWhereAdminPerformer.isEmpty());
        tasksWhereAdminPerformer
                .forEach(task -> assertNotEquals(task.performer().email(), USER_EMAIL_TEST));
    }

    @Test
    void findByFilterWhereAdminNotPerformer(){
        Mockito.doReturn(Optional.of(new SecurityUserDto(ADMIN_EMAIL_TEST, true)))
                .when(authServiceImpl)
                .takeUserFromContext();
        TaskFilter filter = TaskFilter.builder()
                .createdBy(new UserFilter(null, null))
                .performer(new UserFilter(null, Role.USER))
                .build();
        Pageable pageable = new Pageable(PAGE_SIZE, PAGE_NUMBER);

        List<ReadTaskDto> tasks = taskServiceImpl.findAllByFilter(filter, pageable);

        assertFalse(tasks.isEmpty());
        tasks.forEach(task -> assertNotEquals(task.performer().email(), ADMIN_EMAIL_TEST));
    }

    @Test
    void saveTestForNotExistencePerformer(){
        Mockito.doReturn(Optional.of(new SecurityUserDto(USER_EMAIL_TEST, false)))
                .when(authServiceImpl)
                .takeUserFromContext();
        CreateOrUpdateTaskDto createOrUpdateTaskDto = CreateOrUpdateTaskDto.builder()
                .status(Status.IN_PROGRESS)
                .priority(Priority.HIGHEST)
                .description("Test save")
                .name("Test save task")
                .performerEmail(USER_EMAIL_TEST + ".")
                .build();
        assertThrows(UserNotFoundInDatabaseException.class,
                () -> taskServiceImpl.save(createOrUpdateTaskDto));
    }

    @Test
    void saveForExistencePerformer(){
        Mockito.doReturn(Optional.of(new SecurityUserDto(USER_EMAIL_TEST, false)))
                .when(authServiceImpl)
                .takeUserFromContext();
        CreateOrUpdateTaskDto createOrUpdateTaskDto = CreateOrUpdateTaskDto.builder()
                .status(Status.IN_PROGRESS)
                .priority(Priority.HIGHEST)
                .description("Test save")
                .name("Test save task")
                .performerEmail(USER_EMAIL_TEST)
                .build();

        ReadTaskDto task = taskServiceImpl.save(createOrUpdateTaskDto);
        assertNotNull(task);
        assertEquals(task.performer().email(), USER_EMAIL_TEST);
        assertEquals(task.status(), Status.IN_PROGRESS);
        assertEquals(task.priority(), Priority.HIGHEST);
    }

    @Test
    void updateWhereUserPerformer(){
        String testDescription = "updateWhereUserPerformer";
        Mockito.doReturn(Optional.of(new SecurityUserDto(USER_EMAIL_TEST, false)))
                .when(authServiceImpl)
                .takeUserFromContext();
        CreateOrUpdateTaskDto createOrUpdateTaskDto = CreateOrUpdateTaskDto.builder()
                .status(Status.IN_PROGRESS)
                .priority(Priority.HIGHEST)
                .description(testDescription)
                .name("Test save task")
                .performerEmail(USER_EMAIL_TEST)
                .build();
        LocalDateTime startUpdateTime = taskServiceImpl.findById(TASK_ID_WHERE_PERFORMER_USER_TEST).orElseThrow().getMetainfo().getUpdatedAt();
        ReadTaskDto task = taskServiceImpl.update(TASK_ID_WHERE_PERFORMER_USER_TEST, createOrUpdateTaskDto);
        LocalDateTime updatedTime = taskServiceImpl.findById(TASK_ID_WHERE_PERFORMER_USER_TEST).orElseThrow().getMetainfo().getUpdatedAt();

        assertNotEquals(startUpdateTime, updatedTime);
        assertNotNull(task);
        assertEquals(task.status(), Status.IN_PROGRESS);
        assertEquals(task.priority(), Priority.HIGHEST);
        assertEquals(task.description(), testDescription);
    }

    @Test
    void updateWhereAdminNotPerformer(){
        Mockito.doReturn(Optional.of(new SecurityUserDto(ADMIN_EMAIL_TEST, true)))
                .when(authServiceImpl)
                .takeUserFromContext();

        Optional<Task> task = taskServiceImpl.findById(TASK_ID_WHERE_PERFORMER_USER_TEST);
        assertTrue(task.isPresent());
        assertEquals(task.orElseThrow().getPerformer().getEmail(), USER_EMAIL_TEST);

        String nameTask = "57fd59ce-39d7-4ec9-82d4-20b54ed5dd1e";
        String testDescription = "updateWhereUserPerformer";

        CreateOrUpdateTaskDto createOrUpdateTaskDto = CreateOrUpdateTaskDto.builder()
                .status(Status.IN_PROGRESS)
                .priority(Priority.HIGHEST)
                .description(testDescription)
                .name(nameTask)
                .performerEmail(ADMIN_EMAIL_TEST)
                .build();

        ReadTaskDto readTaskDto = taskServiceImpl.update(TASK_ID_WHERE_PERFORMER_USER_TEST, createOrUpdateTaskDto);
        assertEquals(readTaskDto.performer().email(), ADMIN_EMAIL_TEST);

        Mockito.doReturn(Optional.of(new SecurityUserDto(USER_EMAIL_TEST, false)))
                .when(authServiceImpl)
                .takeUserFromContext();
        TaskFilter taskFilter = TaskFilter.builder()
                .performer(new UserFilter(null, null))
                .createdBy(new UserFilter(null, null))
                .name(nameTask)
                .build();
        Pageable pageable = new Pageable(1, PAGE_NUMBER);
        List<ReadTaskDto> readTaskDtoList = taskServiceImpl.findAllByFilter(taskFilter, pageable);
        assertTrue(readTaskDtoList.isEmpty());
    }

    @Test
    void deleteTryByUser(){
        Mockito.doReturn(Optional.of(new SecurityUserDto(ADMIN_EMAIL_TEST, false)))
                .when(authServiceImpl)
                .takeUserFromContext();
        assertFalse(taskServiceImpl.delete(TASK_ID_WHERE_PERFORMER_USER_TEST));
    }

    @Test
    void deleteTryByPerformer(){
        Mockito.doReturn(Optional.of(new SecurityUserDto(USER_EMAIL_TEST, false)))
                .when(authServiceImpl)
                .takeUserFromContext();
        assertFalse(taskServiceImpl.delete(TASK_ID_WHERE_PERFORMER_USER_TEST));
    }

    @Test
    void deleteTryByAdmin(){
        Mockito.doReturn(Optional.of(new SecurityUserDto(ADMIN_EMAIL_TEST, true)))
                .when(authServiceImpl)
                .takeUserFromContext();
        assertTrue(taskServiceImpl.delete(TASK_ID_WHERE_PERFORMER_USER_TEST));
    }
}
