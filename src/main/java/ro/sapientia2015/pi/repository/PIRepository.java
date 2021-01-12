package ro.sapientia2015.pi.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import ro.sapientia2015.pi.model.PI;

public interface PIRepository extends JpaRepository<PI, Long> {

}
