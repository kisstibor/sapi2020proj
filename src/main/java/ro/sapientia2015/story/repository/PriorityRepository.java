package ro.sapientia2015.story.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import ro.sapientia2015.story.model.Priority;

public interface PriorityRepository extends JpaRepository<Priority, Long> {

}
