package com.WriteNow.demo.services;

import com.WriteNow.demo.entities.DTOS.TaskResponseDTO;
import com.WriteNow.demo.entities.Task;
import com.WriteNow.demo.repositories.TaskRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskService {

    private final TaskRepository taskRepository;


    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public List<TaskResponseDTO> findAllTasks(){
        return taskRepository.findAll().stream().map(TaskResponseDTO::new).toList();
    }

    public TaskResponseDTO findTaskById(Long id){
        Task task = taskRepository.findById(id).orElseThrow();
        return new TaskResponseDTO(task);
    }

    public TaskResponseDTO createTask(Task task){
        taskRepository.save(task);
        return new TaskResponseDTO(task);
    }

    public TaskResponseDTO updateTask(Task task, Long id){
        Task updatedTask = taskRepository.findById(id).orElseThrow();
        updatedTask.setTitle(task.getTitle());
        updatedTask.setContent(task.getContent());
        taskRepository.save(updatedTask);
        return new TaskResponseDTO(updatedTask);
    }

    public void deleteTask(Long id){
        taskRepository.deleteById(id);
    }
}
