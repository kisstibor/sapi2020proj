package ro.sapientia2015.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import ro.sapientia2015.project.model.Epic;


public interface EpicRepository extends JpaRepository<Epic, Long> {
}