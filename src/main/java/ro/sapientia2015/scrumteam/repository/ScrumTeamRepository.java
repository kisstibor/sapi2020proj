package ro.sapientia2015.scrumteam.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import ro.sapientia2015.scrumteam.model.ScrumTeam;

public interface ScrumTeamRepository extends JpaRepository<ScrumTeam, Long> {
}
