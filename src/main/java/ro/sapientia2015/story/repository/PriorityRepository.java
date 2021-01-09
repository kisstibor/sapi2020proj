package ro.sapientia2015.story.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import ro.sapientia2015.story.model.Priority;
import ro.sapientia2015.story.model.Story;

/**
 * @author Kapas Krisztina
 */
public interface PriorityRepository extends JpaRepository<Priority, Long> 
{
}
