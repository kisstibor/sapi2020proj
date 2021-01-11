package ro.sapientia2015.story.service;


import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ro.sapientia2015.story.dto.VacationDTO;
import ro.sapientia2015.story.exception.NotFoundException;
import ro.sapientia2015.story.model.Story;
import ro.sapientia2015.story.model.Vacation;
import ro.sapientia2015.story.repository.VacationRepository;

@Service
public class RepositoryVacationService implements VacationService {

	@Resource
	private VacationRepository vacationRepository;
	
	 @Transactional
	 @Override
	 public Vacation add(VacationDTO added) {

		 Vacation model = Vacation.getBuilder(added.getVacationStartDate(),added.getVacationEndDate())
				 .build();

	        return vacationRepository.save(model);
	    }
	 
	 @Transactional(rollbackFor = {NotFoundException.class})
	    @Override
	    public Vacation deleteById(Long id) throws NotFoundException {
	        Vacation deleted = findById(id);
	        vacationRepository.delete(deleted);
	        return deleted;
	    }
	
	 @Transactional(readOnly = true, rollbackFor = {NotFoundException.class})
		@Override
		public Vacation findById(Long id) throws NotFoundException {
		 Vacation found = vacationRepository.findOne(id);
	        if (found == null) {
	            throw new NotFoundException("No entry found with id: " + id);
	        }

	        return found;
		}
	 
	  @Transactional(readOnly = true)
	    @Override
	    public List<Vacation> findAll() {
	       return vacationRepository.findAll();
	    }


	@Transactional(rollbackFor = {NotFoundException.class})
    @Override
    public Vacation update(VacationDTO updated) throws NotFoundException {
        Vacation model = findById(updated.getId());
        model.update(updated.getVacationEndDate(), updated.getVacationStartDate());

        return model;
    }
}
