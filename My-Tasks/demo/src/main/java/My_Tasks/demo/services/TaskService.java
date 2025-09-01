package My_Tasks.demo.services;

import My_Tasks.demo.entities.Task;
import My_Tasks.demo.exceptions.AlreadyExistException;
import My_Tasks.demo.exceptions.NotFoundException;
import My_Tasks.demo.repositories.TaskRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskService {

    private final TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public List<Task> findAllTasks(){
        return taskRepository.findAll();
    }

    public Task findTaskById(Long id){
        return taskRepository.findById(id).orElseThrow(() -> new NotFoundException("Não existe uma task com este ID"));
    }

    public Task createTask(Task task){
        if(taskRepository.existsByTaskName(task.getTaskName())) throw new AlreadyExistException("Já existe uma task com este nome");
        return taskRepository.save(task);
    }

    public Task updateTask(Task task, Long id){
        Task updatedTask = taskRepository.findById(id).orElseThrow(() -> new NotFoundException("Não existe uma task com este ID"));
        if(taskRepository.existsByTaskName(task.getTaskName())) throw new AlreadyExistException("Já existe uma task com este nome");
        updatedTask.setStatus(task.getStatus());
        updatedTask.setTaskName(task.getTaskName());
        return taskRepository.save(updatedTask);
    }

    public void deleteTask(Long id){
        if(!taskRepository.existsById(id)) throw new NotFoundException("Não existe uma task com este ID");
        taskRepository.deleteById(id);
    }
}
