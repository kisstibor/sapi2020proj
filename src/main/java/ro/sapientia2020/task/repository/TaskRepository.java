package ro.sapientia2020.task.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import ro.sapientia2020.task.model.Task;


public interface TaskRepository extends JpaRepository<Task, Long> {

}
