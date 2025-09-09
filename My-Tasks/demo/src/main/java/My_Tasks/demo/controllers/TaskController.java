package My_Tasks.demo.controllers;

import My_Tasks.demo.entities.Task;
import My_Tasks.demo.entities.enums.Status;
import My_Tasks.demo.repositories.TaskRepository;
import My_Tasks.demo.services.TaskService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tasks")
@CrossOrigin(origins = "http://localhost:4200")
public class TaskController {

    private final TaskService taskService;
    private final TaskRepository taskRepository;

    public TaskController(TaskService taskService, TaskRepository taskRepository) {
        this.taskService = taskService;
        this.taskRepository = taskRepository;
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @GetMapping
    public ResponseEntity<Page<Task>> findAll(@RequestHeader("Authorization") String authHeader, @PageableDefault(size = 6) Pageable pageable){
        return ResponseEntity.ok().body(taskService.findAllTasks(authHeader,pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Task> findById(@PathVariable Long id, @RequestHeader("Authorization") String authHeader){
        return ResponseEntity.ok().body(taskService.findTaskById(id,authHeader));
    }

    @PostMapping
    public ResponseEntity<Task> createTask(@RequestBody Task task, @RequestHeader("Authorization") String authHeader){
        return ResponseEntity.ok().body(taskService.createTask(task,authHeader));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Task> updateTask(@PathVariable Long id, @RequestBody Task task, @RequestHeader("Authorization") String authHeader){
        return ResponseEntity.ok().body(taskService.updateTask(task,id,authHeader));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id, @RequestHeader("Authorization") String authHeader){
        taskService.deleteTask(id, authHeader);
        return ResponseEntity.noContent().build();
    }
}
