package emobile.by.smertex.springtodo.controller.realisation;

import emobile.by.smertex.springtodo.controller.interfaces.TaskController;
import emobile.by.smertex.springtodo.dto.exception.ApplicationResponse;
import emobile.by.smertex.springtodo.dto.filter.CommentFilter;
import emobile.by.smertex.springtodo.dto.filter.TaskFilter;
import emobile.by.smertex.springtodo.dto.read.PageResponse;
import emobile.by.smertex.springtodo.dto.read.Pageable;
import emobile.by.smertex.springtodo.dto.read.ReadCommentDto;
import emobile.by.smertex.springtodo.dto.read.ReadTaskDto;
import emobile.by.smertex.springtodo.dto.update.CreateOrUpdateCommentDto;
import emobile.by.smertex.springtodo.dto.update.CreateOrUpdateTaskDto;
import emobile.by.smertex.springtodo.service.interfaces.CommentService;
import emobile.by.smertex.springtodo.service.interfaces.TaskService;
import emobile.by.smertex.springtodo.util.ApiPath;
import emobile.by.smertex.springtodo.util.ResponseMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping(ApiPath.TASK_PATH)
public class TaskControllerImpl implements TaskController {

    private final TaskService taskService;

    private final CommentService commentService;

    @GetMapping
    public PageResponse<ReadTaskDto> findAll(@RequestBody @Validated TaskFilter filter,
                                             Pageable pageable){
        return PageResponse.of(taskService.findAllByFilter(filter, pageable));
    }

    @PostMapping
    public ReadTaskDto create(@Validated @RequestBody CreateOrUpdateTaskDto dto){
        return taskService.save(dto)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, ResponseMessage.CREATE_TASK_EXCEPTION));
    }

    @PutMapping(ApiPath.ID_TASK_PATH)
    public ReadTaskDto updateTask(@PathVariable UUID id,
                                  @Validated @RequestBody CreateOrUpdateTaskDto dto){
        return taskService.update(id, dto)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, ResponseMessage.UPDATE_TASK_NOT_FOUND));
    }

    @DeleteMapping(ApiPath.ID_TASK_PATH)
    public ResponseEntity<ApplicationResponse> deleteTask(@PathVariable UUID id){
        return taskService.delete(id) ? ResponseEntity.ok(new ApplicationResponse(ResponseMessage.DELETE_TASK_SUCCESSFULLY, HttpStatus.OK, LocalDateTime.now())) :
                ResponseEntity.badRequest().body(new ApplicationResponse(ResponseMessage.DELETE_TASK_FAILED, HttpStatus.BAD_REQUEST, LocalDateTime.now()));
    }

    @GetMapping(ApiPath.COMMENT_IN_TASK_PATH)
    public PageResponse<ReadCommentDto> findAllComment(@PathVariable UUID id,
                                                       @Validated @RequestBody CommentFilter filter,
                                                       Pageable pageable){
        return PageResponse.of(commentService.findAllByFilter(id, filter, pageable));
    }

    @PostMapping(ApiPath.COMMENT_IN_TASK_PATH)
    public ReadCommentDto addComment(@PathVariable UUID id,
                                     @Validated @RequestBody CreateOrUpdateCommentDto dto){
        return commentService.add(id, dto)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, ResponseMessage.ADD_COMMENT_EXCEPTION));
    }

    @PutMapping(ApiPath.COMMENT_UPDATE_PATH)
    public ReadCommentDto updateComment(@PathVariable UUID id,
                                        @Validated @RequestBody CreateOrUpdateCommentDto dto){
        return commentService.update(id, dto)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, ResponseMessage.UPDATE_COMMENT_EXCEPTION));
    }

}
