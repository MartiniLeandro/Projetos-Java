package com.WriteNow.demo.controllers;

import com.WriteNow.demo.entities.DTOS.TaskResponseDTO;
import com.WriteNow.demo.entities.Task;
import com.WriteNow.demo.services.TaskService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping
    public ResponseEntity<List<TaskResponseDTO>> findAll(){
        return ResponseEntity.ok().body(taskService.findAllTasks());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskResponseDTO> findById(@PathVariable Long id){
        return ResponseEntity.ok().body(taskService.findTaskById(id));
    }

    @PostMapping
    public ResponseEntity<TaskResponseDTO> create(@RequestBody Task task){
        return ResponseEntity.ok().body(taskService.createTask(task));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TaskResponseDTO> update(@RequestBody Task task, @PathVariable Long id){
        return ResponseEntity.ok().body(taskService.updateTask(task,id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id){
        taskService.deleteTask(id);
        return ResponseEntity.noContent().build();
    }
}
