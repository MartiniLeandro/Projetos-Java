package My_Tasks.demo.repositories;

import My_Tasks.demo.entities.Task;
import My_Tasks.demo.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskRepository extends JpaRepository<Task,Long> {
    Boolean existsByTaskName(String taskName);
    Boolean existsByTaskNameAndUser(String taskName, User user);
}
