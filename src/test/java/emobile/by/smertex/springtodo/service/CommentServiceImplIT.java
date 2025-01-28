package emobile.by.smertex.springtodo.service;

import emobile.by.smertex.springtodo.annotation.IT;
import emobile.by.smertex.springtodo.dto.filter.CommentFilter;
import emobile.by.smertex.springtodo.dto.filter.UserFilter;
import emobile.by.smertex.springtodo.dto.read.ReadCommentDto;
import emobile.by.smertex.springtodo.dto.security.SecurityUserDto;
import emobile.by.smertex.springtodo.dto.update.CreateOrUpdateCommentDto;
import emobile.by.smertex.springtodo.service.exception.SaveException;
import emobile.by.smertex.springtodo.service.exception.UpdateException;
import emobile.by.smertex.springtodo.service.realisation.AuthServiceImpl;
import emobile.by.smertex.springtodo.service.realisation.CommentServiceImpl;
import emobile.by.smertex.springtodo.service.realisation.TaskServiceImpl;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@IT
@RequiredArgsConstructor
@ExtendWith(MockitoExtension.class)
public class CommentServiceImplIT {
    private static final String USER_EMAIL_TEST = "evgenii@gmail.com";

    private static final String ADMIN_EMAIL_TEST = "smertexx@gmail.com";

    private static final UUID TASK_ID_WHERE_PERFORMER_USER_TEST = UUID.fromString("a9099b32-e5b2-41aa-9ab6-d4d461549c70");

    private static final UUID TASK_ID_WHERE_PERFORMER_ADMIN_TEST = UUID.fromString("5f0288e7-301b-416b-af58-dd433667a607");

    private static final UUID COMMENT_ID_WHERE_CREATOR_USER_TEST = UUID.fromString("5e0dc76a-f6d1-44b9-9f6a-259915ceef7c");

    private static final Integer SECOND_FROM_CREATION = 10;

    private static final Integer PAGE_NUMBER = 0;

    private static final Integer PAGE_SIZE = 2;

    @InjectMocks
    private final CommentServiceImpl commentServiceImpl;

    private final TaskServiceImpl taskServiceImpl;

    @MockBean
    private final AuthServiceImpl authServiceImpl;

    @Test
    void findAllByFilterForUserWherePerformerTask(){
        Mockito.doReturn(Optional.of(new SecurityUserDto(USER_EMAIL_TEST, false)))
                .when(authServiceImpl)
                .takeUserFromContext();
        CommentFilter filter = CommentFilter.builder()
                .createdBy(UserFilter.builder()
                        .build())
                .build();
        Pageable pageable = PageRequest.of(PAGE_NUMBER, PAGE_SIZE);

        List<ReadCommentDto> readCommentDtoList = commentServiceImpl.findAllByFilter(TASK_ID_WHERE_PERFORMER_USER_TEST, filter, pageable);

        assertFalse(readCommentDtoList.isEmpty());
        assertEquals(taskServiceImpl.findById(TASK_ID_WHERE_PERFORMER_USER_TEST).orElseThrow()
                .getPerformer().getEmail(), USER_EMAIL_TEST);
    }

    @Test
    void findAllByFilterForUserWhereNotPerformerTask(){
        Mockito.doReturn(Optional.of(new SecurityUserDto(USER_EMAIL_TEST, false)))
                .when(authServiceImpl)
                .takeUserFromContext();
        CommentFilter filter = CommentFilter.builder()
                .createdBy(UserFilter.builder()
                        .build())
                .build();
        Pageable pageable = PageRequest.of(PAGE_NUMBER, PAGE_SIZE);

        List<ReadCommentDto> readCommentDtoListNotPerformer = commentServiceImpl.findAllByFilter(TASK_ID_WHERE_PERFORMER_ADMIN_TEST, filter, pageable);
        assertTrue(readCommentDtoListNotPerformer.isEmpty());

        Mockito.doReturn(Optional.of(new SecurityUserDto(ADMIN_EMAIL_TEST, false)))
                .when(authServiceImpl)
                .takeUserFromContext();
        List<ReadCommentDto> readCommentDtoListPerformer = commentServiceImpl.findAllByFilter(TASK_ID_WHERE_PERFORMER_ADMIN_TEST, filter, pageable);
        assertFalse(readCommentDtoListPerformer.isEmpty());
    }

    @Test
    void findAllByFilterForAdminWhereAdminNotPerformer(){
        Mockito.doReturn(Optional.of(new SecurityUserDto(ADMIN_EMAIL_TEST, true)))
                .when(authServiceImpl)
                .takeUserFromContext();
        CommentFilter filter = CommentFilter.builder()
                .createdBy(UserFilter.builder()
                        .build())
                .build();
        Pageable pageable = PageRequest.of(PAGE_NUMBER, PAGE_SIZE);
        List<ReadCommentDto> readCommentDtoListNotPerformer = commentServiceImpl.findAllByFilter(TASK_ID_WHERE_PERFORMER_USER_TEST, filter, pageable);
        assertFalse(readCommentDtoListNotPerformer.isEmpty());
    }

    @Test
    void addForUserWherePerformerTask(){
        Mockito.doReturn(Optional.of(new SecurityUserDto(USER_EMAIL_TEST, false)))
                .when(authServiceImpl)
                .takeUserFromContext();
        CreateOrUpdateCommentDto createOrUpdateCommentDto = new CreateOrUpdateCommentDto("Test add comment");
        ReadCommentDto readCommentDto = commentServiceImpl.add(TASK_ID_WHERE_PERFORMER_USER_TEST, createOrUpdateCommentDto);

        assertEquals(readCommentDto.createdBy().email(), USER_EMAIL_TEST);
        Assertions.assertTrue(ChronoUnit.SECONDS.between(readCommentDto.createdAt(), LocalDateTime.now()) < SECOND_FROM_CREATION);
    }

    @Test
    void addForUserWhereNotPerformerTask(){
        Mockito.doReturn(Optional.of(new SecurityUserDto(USER_EMAIL_TEST, false)))
                .when(authServiceImpl)
                .takeUserFromContext();
        CreateOrUpdateCommentDto createOrUpdateCommentDto = new CreateOrUpdateCommentDto("Test add comment");
        assertThrows(SaveException.class, () -> commentServiceImpl.add(TASK_ID_WHERE_PERFORMER_ADMIN_TEST, createOrUpdateCommentDto));
    }

    @Test
    void addForAdminWhereNotPerformerTask(){
        Mockito.doReturn(Optional.of(new SecurityUserDto(ADMIN_EMAIL_TEST, true)))
                .when(authServiceImpl)
                .takeUserFromContext();
        CreateOrUpdateCommentDto createOrUpdateCommentDto = new CreateOrUpdateCommentDto("Test add comment");
        ReadCommentDto readCommentDto = commentServiceImpl.add(TASK_ID_WHERE_PERFORMER_ADMIN_TEST, createOrUpdateCommentDto);
        assertNotNull(readCommentDto);
        assertNotEquals(taskServiceImpl.findById(TASK_ID_WHERE_PERFORMER_USER_TEST).orElseThrow()
                .getPerformer().getEmail(), ADMIN_EMAIL_TEST);
    }

    @Test
    void updateCommentForCreator(){
        Mockito.doReturn(Optional.of(new SecurityUserDto(USER_EMAIL_TEST, false)))
                .when(authServiceImpl)
                .takeUserFromContext();
        CreateOrUpdateCommentDto createOrUpdateCommentDto = new CreateOrUpdateCommentDto("Test update comment");
        ReadCommentDto readCommentDto = commentServiceImpl.update(COMMENT_ID_WHERE_CREATOR_USER_TEST, createOrUpdateCommentDto);
        assertNotNull(readCommentDto);
    }

    @Test
    void updateCommentNotForCreator(){
        Mockito.doReturn(Optional.of(new SecurityUserDto(ADMIN_EMAIL_TEST, true)))
                .when(authServiceImpl)
                .takeUserFromContext();
        CreateOrUpdateCommentDto createOrUpdateCommentDto = new CreateOrUpdateCommentDto("Test update comment");
        assertThrows(UpdateException.class, () -> commentServiceImpl.update(COMMENT_ID_WHERE_CREATOR_USER_TEST, createOrUpdateCommentDto));
    }
}
