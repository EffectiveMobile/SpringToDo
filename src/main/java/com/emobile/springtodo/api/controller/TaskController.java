package com.emobile.springtodo.api.controller;

import com.emobile.springtodo.api.request.TaskCreationRequest;
import com.emobile.springtodo.api.request.TaskUpdateRequest;
import com.emobile.springtodo.api.request.validation.Patterns;
import com.emobile.springtodo.api.swagger.ITaskController;
import com.emobile.springtodo.core.entity.Task;
import com.emobile.springtodo.core.service.TaskService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/task")
@Validated
public class TaskController implements ITaskController {

    private final TaskService taskService;

    @GetMapping("/all")
    public ResponseEntity<List<Task>> getAll(@RequestParam(defaultValue = "10") int limit,
                                             @RequestParam(defaultValue = "0") int offset) {
        return ResponseEntity.ok(taskService.getAllTasks(limit, offset));
    }

    @GetMapping("/{taskid}")
    public ResponseEntity<?> getTask(@Pattern(regexp = Patterns.ID, message = "Invalid task ID format") @PathVariable("taskid") String taskID) {
        return ResponseEntity.ok(taskService.getTaskById(taskID));
    }

    @GetMapping("/category/{categoryid}")
    public ResponseEntity<?> getAllTasksByCategory(@Pattern(regexp = Patterns.ID, message = "Invalid category ID format") @PathVariable("categoryid") String categoryID) {
        return ResponseEntity.ok(taskService.getByCategory(categoryID));
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<?> getAllTasksByStatus(@Pattern(regexp = Patterns.STATUS,
            message = "Status must be one of: TODO, INPROGRESS, COMPLETE, UNCOMPLETED, DELAYED")
                                                 @PathVariable("status") String status) {
        return ResponseEntity.ok(taskService.getByStatus(status.toUpperCase()));
    }

    @PostMapping("/add")
    public ResponseEntity<?> addTask(@Valid @RequestBody TaskCreationRequest request) {
        taskService.createTask(request);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping("/{taskid}")
    public ResponseEntity<?> updateTask(@Pattern(regexp = Patterns.ID, message = "Invalid task ID format") @PathVariable("taskid") String taskID,
                                        @Valid @RequestBody TaskUpdateRequest request) {
        if (request.taskTitle() == null && request.taskDescription() == null && request.taskStatus() == null
        && request.taskDate() == null && request.taskHours() == null && request.taskCategory() == null) {
            return ResponseEntity.badRequest().body("Request body cannot be empty");
        }

        taskService.updateTask(taskID, request);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/{taskid}")
    public ResponseEntity<?> deleteTask(@Pattern(regexp = Patterns.ID, message = "Invalid task ID format") @PathVariable("taskid") String taskID) {
        taskService.deleteTask(taskID);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
