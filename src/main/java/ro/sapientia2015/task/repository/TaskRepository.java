package ro.sapientia2015.task.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ro.sapientia2015.task.model.Task;

public interface TaskRepository extends JpaRepository<Task, Long>{
}
