package emobile.by.smertex.springtodo.service.realisation;

import emobile.by.smertex.springtodo.controller.exception.UserNotFoundInDatabaseException;
import emobile.by.smertex.springtodo.database.entity.realisation.Comment;
import emobile.by.smertex.springtodo.database.repository.interfaces.CommentRepository;
import emobile.by.smertex.springtodo.dto.filter.CommentFilter;
import emobile.by.smertex.springtodo.dto.read.Pageable;
import emobile.by.smertex.springtodo.dto.read.ReadCommentDto;
import emobile.by.smertex.springtodo.dto.security.SecurityUserDto;
import emobile.by.smertex.springtodo.dto.update.CreateOrUpdateCommentDto;
import emobile.by.smertex.springtodo.mapper.realisation.CommentToReadCommentDtoMapper;
import emobile.by.smertex.springtodo.mapper.realisation.CreateOrUpdateCommentDtoToCommentMapper;
import emobile.by.smertex.springtodo.service.interfaces.AuthService;
import emobile.by.smertex.springtodo.service.interfaces.CommentService;
import emobile.by.smertex.springtodo.service.interfaces.TaskService;
import emobile.by.smertex.springtodo.service.interfaces.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;

    private final CommentToReadCommentDtoMapper commentToReadCommentDtoMapper;

    private final CreateOrUpdateCommentDtoToCommentMapper createOrUpdateCommentDtoToCommentMapper;

    private final AuthService authService;

    private final UserService userService;

    private final TaskService taskService;

    @Override
    public List<ReadCommentDto> findAllByFilter(UUID taskId, CommentFilter commentFilter, Pageable pageable){
        return commentRepository.findAllByFilter(taskId, commentFilter, authService.takeUserFromContext().orElseThrow(), pageable)
                .stream()
                .map(commentToReadCommentDtoMapper::map)
                .toList();
    }

    @Override
    @Transactional
    public Optional<ReadCommentDto> add(UUID taskId, CreateOrUpdateCommentDto dto){
        SecurityUserDto user = authService.takeUserFromContext().orElseThrow();
        return taskService.findById(taskId)
                .filter(task -> user.isAdmin() || task.getPerformer().getEmail().equals(user.email()))
                .map(task -> {
                    Comment comment = createOrUpdateCommentDtoToCommentMapper.map(dto);
                    comment.setCreatedBy(userService.findByEmail(user.email())
                            .orElseThrow(() -> new UserNotFoundInDatabaseException(user.email())));
                    comment.setTask(taskService.findById(taskId)
                            .orElseThrow());
                    comment.setCreatedAt(LocalDateTime.now());
                    return comment;
                })
                .map(commentRepository::save)
                .map(commentToReadCommentDtoMapper::map);
    }

    @Override
    @Transactional
    public Optional<ReadCommentDto> update(UUID commentId, CreateOrUpdateCommentDto dto){
        return commentRepository.findById(commentId)
                .filter(comment -> authService.takeUserFromContext().orElseThrow().email().equals(comment.getCreatedBy().getEmail()))
                .map(comment -> createOrUpdateCommentDtoToCommentMapper.map(dto, comment))
                .map(commentRepository::update)
                .map(commentToReadCommentDtoMapper::map);
    }

}
