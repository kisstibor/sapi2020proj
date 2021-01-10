package ro.sapientia2015.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import ro.sapientia2015.project.model.Project;

public interface ProjectRepository extends JpaRepository<Project, Long> {
}
