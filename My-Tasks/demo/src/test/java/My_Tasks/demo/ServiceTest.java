package My_Tasks.demo;

import My_Tasks.demo.entities.Task;
import My_Tasks.demo.exceptions.AlreadyExistException;
import My_Tasks.demo.exceptions.NotFoundException;
import My_Tasks.demo.repositories.TaskRepository;
import My_Tasks.demo.services.TaskService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class ServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private TaskService taskService;

    private Task task1,task2;

    @BeforeEach
    void setup(){
        task1 = new Task("treinar musculação");
        task1.setId(1L);
        task2 = new Task("pagar faculdade");
        task2.setId(2L);
    }

    @Test
    void testFindAllTasks(){
        Mockito.when(taskRepository.findAll()).thenReturn(List.of(task1,task2));
        List<Task> allTasks = taskService.findAllTasks();

        Assertions.assertNotNull(allTasks);
        Assertions.assertEquals(2,allTasks.size());
    }

    @Test
    void testFindById(){
        Mockito.when(taskRepository.findById(Mockito.anyLong())).thenReturn(Optional.ofNullable(task1));
        Task task = taskService.findTaskById(1L);

        Assertions.assertEquals(task1,task);
    }

    @Test
    void testFindByIdFailed(){
        Mockito.when(taskRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty());
        NotFoundException exception = Assertions.assertThrows(NotFoundException.class, () -> taskService.findTaskById(1L));

        Assertions.assertEquals("Não existe uma task com este ID", exception.getMessage());
    }

    @Test
    void testCreateTask(){
        Task task3 = new Task("task teste");
        Mockito.when(taskRepository.existsByTaskName(Mockito.anyString())).thenReturn(false);
        Mockito.when(taskRepository.save(Mockito.any(Task.class))).thenReturn(task3);
        Task task = taskService.createTask(task3);

        Assertions.assertEquals(task,task3);
    }

    @Test
    void testCreateTaskFailed(){
        Task task3 = new Task("task teste");
        Mockito.when(taskRepository.existsByTaskName(Mockito.anyString())).thenReturn(true);

        AlreadyExistException exception = Assertions.assertThrows(AlreadyExistException.class, () -> taskService.createTask(task3));

        Assertions.assertEquals("Já existe uma task com este nome",exception.getMessage());
    }

    @Test
    void testUpdateTask(){
        Task task3 = new Task("task teste");
        Mockito.when(taskRepository.findById(Mockito.anyLong())).thenReturn(Optional.ofNullable(task2));
        Mockito.when(taskRepository.existsByTaskName(Mockito.anyString())).thenReturn(false);
        Mockito.when(taskRepository.save(task2)).thenReturn(task2);
        Task task = taskService.updateTask(task3, task2.getId());

        Assertions.assertNotNull(task);
        Assertions.assertEquals(task.getTaskName(),task3.getTaskName());
    }

    @Test
    void testUpdateTaskFailed(){
        Task task3 = new Task("task teste");
        Mockito.when(taskRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty());
        NotFoundException exception = Assertions.assertThrows(NotFoundException.class, () -> taskService.updateTask(task3,2L));

        Assertions.assertEquals("Não existe uma task com este ID",exception.getMessage());
    }
    @Test
    void testUpdateTaskFailed2(){
        Task task3 = new Task("task teste");
        Mockito.when(taskRepository.findById(Mockito.anyLong())).thenReturn(Optional.ofNullable(task2));
        Mockito.when(taskRepository.existsByTaskName(Mockito.anyString())).thenReturn(true);
        AlreadyExistException exception = Assertions.assertThrows(AlreadyExistException.class, () -> taskService.updateTask(task3,2L));

        Assertions.assertEquals("Já existe uma task com este nome",exception.getMessage());
    }

    @Test
    void testDeleteTask(){
        Mockito.when(taskRepository.existsById(Mockito.anyLong())).thenReturn(true);
        taskService.deleteTask(2L);

        Mockito.verify(taskRepository).deleteById(2L);

    }
}
