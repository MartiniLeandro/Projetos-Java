package My_Tasks.demo.services;

import My_Tasks.demo.entities.Task;
import My_Tasks.demo.entities.enums.Status;
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
        return taskRepository.findById(id).orElseThrow();
    }

    public Task createTask(Task task){
        return taskRepository.save(task);
    }

    public Task updateTask(Task task, Long id){
        Task updatedTask = taskRepository.findById(id).orElseThrow();
        updatedTask.setStatus(task.getStatus());
        updatedTask.setTaskName(task.getTaskName());
        return taskRepository.save(updatedTask);
    }

    public void deleteTask(Long id){
        taskRepository.deleteById(id);
    }
}
