package ro.sapientia2015.story.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import ro.sapientia2015.story.model.Epic;

/**
 * @author Hunor Szatmari
 */
public interface EpicRepository extends JpaRepository<Epic, Long> {
}
