package My_Tasks.demo.services;

import My_Tasks.demo.entities.Task;
import My_Tasks.demo.entities.User;
import My_Tasks.demo.exceptions.AlreadyExistException;
import My_Tasks.demo.exceptions.IsNotYoursException;
import My_Tasks.demo.exceptions.NotFoundException;
import My_Tasks.demo.exceptions.NotNullException;
import My_Tasks.demo.repositories.TaskRepository;
import My_Tasks.demo.repositories.UserRepository;
import My_Tasks.demo.security.TokenService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Objects;

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

    public Page<Task> findAllTasks(String authHeader, Pageable pageable){
        User user = getUserByToken(authHeader);
        return taskRepository.findByUser(user, pageable);
    }

    public Task findTaskById(Long id, String authHeader){
        User user = getUserByToken(authHeader);
        Task task = taskRepository.findById(id).orElseThrow(() -> new NotFoundException("Não existe uma task com este ID"));
        if(!user.getTasks().contains(task)) throw new IsNotYoursException("Esta task não pertence a você");
        return task;
    }

    public Task createTask(Task task, String authHeader){
        User user = getUserByToken(authHeader);
        if(task.getTaskName().isBlank()) throw new NotNullException("TaskName não pode ser null");
        if(taskRepository.existsByTaskNameAndUser(task.getTaskName(),user)) throw new AlreadyExistException("Você já possui uma task com este nome");
        task.setUser(user);
        return taskRepository.save(task);
    }

    public Task updateTask(Task task, Long id, String authHeader){
        User user = getUserByToken(authHeader);
        Task updatedTask = taskRepository.findById(id).orElseThrow(() -> new NotFoundException("Não existe uma task com este ID"));
        if(task.getTaskName().isBlank()) throw new NotNullException("TaskName não pode ser null");
        if(!user.getTasks().contains(updatedTask)) throw new IsNotYoursException("Esta task não pertence a você");
        if(!Objects.equals(updatedTask.getTaskName(), task.getTaskName()) && taskRepository.existsByTaskName(task.getTaskName())) throw new AlreadyExistException("Já existe uma task com este nome");
        updatedTask.setStatus(task.getStatus());
        updatedTask.setTaskName(task.getTaskName());
        return taskRepository.save(updatedTask);
    }


    public void deleteTask(Long id, String authHeader){
        User user = getUserByToken(authHeader);
        Task task = taskRepository.findById(id).orElseThrow(() -> new NotFoundException("Não existe uma task com este ID"));
        if(!user.getTasks().contains(task)) throw new IsNotYoursException("Esta task não pertence a você");
        user.getTasks().remove(task);
        taskRepository.deleteById(id);
    }

    public User getUserByToken(String authHeader){
        String token = authHeader.replace("Bearer ","");
        String email = tokenService.validateToken(token);
        return userRepository.findUserByEmail(email);

    }
}
