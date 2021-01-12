package ro.sapientia2015.story.service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ro.sapientia2015.story.dto.WorkLogDTO;
import ro.sapientia2015.story.exception.NotFoundException;
import ro.sapientia2015.story.model.Story;
import ro.sapientia2015.story.model.WorkLog;
import ro.sapientia2015.story.repository.StoryRepository;
import ro.sapientia2015.story.repository.WorkLogRepository;

@Service
public class RepositoryWorkLogService implements WorkLogService {
	
	protected static final String LOCAL_DATE_FORMAT = "yyyy-MM-dd";
	
	@Resource
    private WorkLogRepository repository;

	@Transactional
    @Override
	public WorkLog add(WorkLogDTO added) {
		WorkLog model = WorkLog.getBuilder(added.getStory_id(), added.getStory_title())
				.logged_at(added.getLogged_at_date())
				.started_at(added.getStarted_at_date())
				.ended_at(added.getEnded_at_date())
                .description(added.getDescription())
                .build();

        return repository.save(model);
	}

	@Transactional(rollbackFor = {NotFoundException.class})
	@Override
	public WorkLog deleteById(Long id) throws NotFoundException {
		WorkLog deleted = findById(id);
        repository.delete(deleted);
        return deleted;
	}

	@Transactional(readOnly = true)
	@Override
	public List<WorkLog> findAll() {
		return repository.findAll();
	}

	@Transactional(readOnly = true, rollbackFor = {NotFoundException.class})
	@Override
	public WorkLog findById(Long id) throws NotFoundException {
		WorkLog found = repository.findOne(id);
        if (found == null) {
            throw new NotFoundException("No entry found with id: " + id);
        }

        return found;
	}

	@Transactional(rollbackFor = {NotFoundException.class})
	@Override
	public WorkLog update(WorkLogDTO updated) throws NotFoundException {
		WorkLog model = findById(updated.getId());
        model.update(updated.getStory_id(),
        				updated.getStory_title(),
        				updated.getLogged_at_date(),
        				updated.getStarted_at_date(),
        				updated.getEnded_at_date(),
        				updated.getDescription());

        return model;
	}

	@Transactional(readOnly = true, rollbackFor = {NotFoundException.class})
	@Override
	public List<WorkLog> findByLoggedDate(String date) {
		List<WorkLog> allWorkLog = repository.findAll();
		List<WorkLog> searchedWorkLogs = new ArrayList<WorkLog>();
		for (WorkLog wl: allWorkLog) {
			if (wl.getLogged_at().equals(convertStringToLocalDate(date))) {
				searchedWorkLogs.add(wl);
			}
		}
		return searchedWorkLogs;
	}
	
	protected LocalDate convertStringToLocalDate(String strDate) {
    	DateTimeFormatter data_formatter = DateTimeFormatter.ofPattern(LOCAL_DATE_FORMAT);
		return LocalDate.parse(strDate, data_formatter);
    }
	
	public boolean checkIfLoggedEarlier(String date, LocalTime started_time, LocalTime ended_time) {
		List<WorkLog> searchedWorkLogs = findByLoggedDate(date);
		
		for (WorkLog wl: searchedWorkLogs) {
			if (started_time.compareTo(wl.getStarted_at()) > 0 && started_time.compareTo(wl.getEnded_at()) < 0) {
				return false;
			}
			
			if (ended_time.compareTo(wl.getStarted_at()) > 0 && ended_time.compareTo(wl.getEnded_at()) < 0) {
				return false;
			}
		}
		
		return true;
	}

}
