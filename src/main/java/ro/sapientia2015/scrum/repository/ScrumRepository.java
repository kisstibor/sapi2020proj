package ro.sapientia2015.scrum.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import ro.sapientia2015.scrum.model.Scrum;

public interface ScrumRepository extends JpaRepository<Scrum, Long>{

}
