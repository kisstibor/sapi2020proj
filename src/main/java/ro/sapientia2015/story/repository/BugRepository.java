package ro.sapientia2015.story.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import ro.sapientia2015.story.model.Bug;

/**
* @author Zsok Andrei
*/
public interface BugRepository extends JpaRepository<Bug, Long> {

}
