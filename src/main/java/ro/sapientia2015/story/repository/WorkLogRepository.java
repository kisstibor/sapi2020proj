package ro.sapientia2015.story.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import ro.sapientia2015.story.model.Story;
import ro.sapientia2015.story.model.WorkLog;

public interface WorkLogRepository extends JpaRepository<WorkLog, Long> {

}
