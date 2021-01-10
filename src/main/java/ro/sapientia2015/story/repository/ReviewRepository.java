package ro.sapientia2015.story.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import ro.sapientia2015.story.model.Review;

public interface ReviewRepository extends JpaRepository<Review, Long>{

}
