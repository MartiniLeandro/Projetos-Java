package My_Tasks.demo.controllers;

import My_Tasks.demo.entities.Task;
import My_Tasks.demo.entities.enums.Status;
import My_Tasks.demo.repositories.TaskRepository;
import My_Tasks.demo.services.TaskService;
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
    public ResponseEntity<List<Task>> findAll(){
        return ResponseEntity.ok().body(taskService.findAllTasks());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Task> findById(@PathVariable Long id){
        return ResponseEntity.ok().body(taskService.findTaskById(id));
    }

    @PostMapping
    public ResponseEntity<Task> createTask(@RequestBody Task task){
        return ResponseEntity.ok().body(taskService.createTask(task));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Task> updateTask(@PathVariable Long id, @RequestBody Task task){
        return ResponseEntity.ok().body(taskService.updateTask(task,id));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Task> patchTask(@PathVariable Long id, @RequestBody Status status){
        Task task = taskService.findTaskById(id);
        task.setStatus(status);
        taskRepository.save(task);
        return ResponseEntity.ok().body(task);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id){
        taskService.deleteTask(id);
        return ResponseEntity.noContent().build();
    }
}
