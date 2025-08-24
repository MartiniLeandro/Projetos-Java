package com.WriteNow.demo.services;

import com.WriteNow.demo.entities.DTOS.TaskResponseDTO;
import com.WriteNow.demo.entities.Task;
import com.WriteNow.demo.entities.User;
import com.WriteNow.demo.repositories.TaskRepository;
import com.WriteNow.demo.repositories.UserRepository;
import com.WriteNow.demo.security.TokenService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final TokenService tokenService;
    private final UserRepository userRepository;


    public TaskService(TaskRepository taskRepository, TokenService tokenService, UserRepository userRepository) {
        this.taskRepository = taskRepository;
        this.tokenService = tokenService;
        this.userRepository = userRepository;
    }

    public List<TaskResponseDTO> findAllTasks(String authHeader){
        User user = findUserByToken(authHeader);
        List<Task> allTasks = user.getTasks();
        return allTasks.stream().map(TaskResponseDTO::new).toList();
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

    public User findUserByToken(String authHeader){
        String token = authHeader.replace("Bearer ","");
        String email = tokenService.validateToken(token);
        return userRepository.findUserByEmail(email);
    }
}
