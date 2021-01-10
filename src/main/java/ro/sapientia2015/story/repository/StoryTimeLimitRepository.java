package ro.sapientia2015.story.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import ro.sapientia2015.story.model.StoryTimeLimit;

public interface StoryTimeLimitRepository extends JpaRepository<StoryTimeLimit, Long> {

}
