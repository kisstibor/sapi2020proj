package ro.sapientia2015.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ro.sapientia2015.project.model.Project;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {
}
