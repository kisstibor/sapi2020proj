package ro.sapientia2015.story.controller;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.annotation.Resource;
import javax.validation.Valid;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import ro.sapientia2015.story.dto.StoryDTO;
import ro.sapientia2015.story.dto.WorkLogDTO;
import ro.sapientia2015.story.exception.NotFoundException;
import ro.sapientia2015.story.model.Story;
import ro.sapientia2015.story.model.WorkLog;
import ro.sapientia2015.story.service.StoryService;
import ro.sapientia2015.story.service.WorkLogService;

@Controller
@SessionAttributes("worklog")
public class WorkLogController {
	
	protected static final String FEEDBACK_MESSAGE_KEY_ADDED = "feedback.message.worklog.added";
    protected static final String FEEDBACK_MESSAGE_KEY_UPDATED = "feedback.message.worklog.updated";
    protected static final String FEEDBACK_MESSAGE_KEY_DELETED = "feedback.message.worklog.deleted";
    protected static final String LOCAL_DATE_FORMAT = "yyyy-MM-dd";

    protected static final String FLASH_MESSAGE_KEY_ERROR = "errorMessage_worklog";
    protected static final String FLASH_MESSAGE_KEY_FEEDBACK = "feedbackMessage_worklog";

    protected static final String MODEL_ATTRIBUTE = "worklog";
    protected static final String MODEL_ATTRIBUTE_LIST = "worklogs";
    protected static final String STORIE_ATTRIBUTE_LIST = "stories";

    protected static final String PARAMETER_ID = "id";

    protected static final String REQUEST_MAPPING_LIST = "/worklog/list";
    protected static final String REQUEST_MAPPING_VIEW = "/worklog/view/{id}";

    protected static final String VIEW_ADD = "worklog/add";
    protected static final String VIEW_LIST = "worklog/list";
    protected static final String VIEW_UPDATE = "worklog/update";
    protected static final String VIEW_VIEW = "worklog/view";

    @Resource
    private WorkLogService service;
    
    @Resource
    private StoryService storyService;
    
    public WorkLogController(StoryService storyService) {
		super();
		this.storyService = storyService;
	}
    
    public WorkLogController() {

	}


	@Resource
    private MessageSource messageSource;
    
    @RequestMapping(value = "/worklog/add", method = RequestMethod.GET)
    public String showAddForm(Model model) {
        WorkLogDTO formObject = new WorkLogDTO();
        model.addAttribute(MODEL_ATTRIBUTE, formObject);
        
        List<Story> stories = storyService.findAll();
        model.addAttribute(STORIE_ATTRIBUTE_LIST, stories);

        return VIEW_ADD;
    }
    
    @RequestMapping(value = "/worklog/add", method = RequestMethod.POST)
    public String add(@Valid @ModelAttribute(MODEL_ATTRIBUTE) WorkLogDTO dto, BindingResult result, RedirectAttributes attributes) {
        if (result.hasErrors()) {
            return VIEW_ADD;
        }

        dto.setStory_id(Long.valueOf(123));

        try {
    		dto.setLogged_at_date(convertStringToLocalDate(dto.getLogged_at()));
    		dto.setStarted_at_date(convertStringToLocalTime(dto.getStarted_at()));
    		dto.setEnded_at_date(convertStringToLocalTime(dto.getEnded_at()));
        } catch(Exception e) {
        	result.addError(new FieldError("WorkLogDTO", "LocalDateTime", "Conversion error", true, null, null, "Error during convert"));
        	dto.setStory_title("Error during convert!");
        	return VIEW_ADD;
        }
        
        if (dto.getStarted_at_date().compareTo(dto.getEnded_at_date()) > 0) {
        	dto.setStory_title("Start date must be less than end date!");
        	return VIEW_ADD;
        }
        
        /*if (!service.checkIfLoggedEarlier(dto.getLogged_at(), dto.getStarted_at_date(), dto.getEnded_at_date())) {
        	dto.setStory_title("Logging already exists!");
        	addFeedbackMessage(attributes, FEEDBACK_MESSAGE_KEY_ADDED, "feedback");
        	return VIEW_ADD;
        }*/
		
        WorkLog added = service.add(dto);
        
        addFeedbackMessage(attributes, FEEDBACK_MESSAGE_KEY_ADDED, added.getLogged_at());
        attributes.addAttribute(PARAMETER_ID, added.getId());

        return createRedirectViewPath(REQUEST_MAPPING_VIEW);  
    }
    
    @RequestMapping(value = REQUEST_MAPPING_VIEW, method = RequestMethod.GET)
    public String findById(@PathVariable("id") Long id, Model model) throws NotFoundException {
    	WorkLog found = service.findById(id);
        model.addAttribute(MODEL_ATTRIBUTE, found);
        return VIEW_VIEW;
    }
    
    @RequestMapping(value = "/worklog/update/{id}", method = RequestMethod.GET)
    public String showUpdateForm(@PathVariable("id") Long id, Model model) throws NotFoundException {
        WorkLog updated = service.findById(id);
        WorkLogDTO formObject = constructFormObjectForUpdateForm(updated);
        model.addAttribute(MODEL_ATTRIBUTE, formObject);

        return VIEW_UPDATE;
    }
    
    @RequestMapping(value = "/worklog/list/{type}", method = RequestMethod.GET)
    public String findAll(@PathVariable("type") String type, Model model) {
    	List<WorkLog> models = new ArrayList<WorkLog>();
    	if (type.equals("all")) {
    		models = service.findAll();
    	}
    	else {
    		models = service.findByLoggedDate(type);
    	}
        model.addAttribute(MODEL_ATTRIBUTE_LIST, models);
        return VIEW_LIST;
    }
    
    @RequestMapping(value = "/worklog/update", method = RequestMethod.POST)
    public String update(@Valid @ModelAttribute(MODEL_ATTRIBUTE) WorkLogDTO dto, BindingResult result, RedirectAttributes attributes) throws NotFoundException {
        if (result.hasErrors()) {
            return VIEW_UPDATE;
        }
        
        dto.setLogged_at_date(convertStringToLocalDate(dto.getLogged_at()));
        dto.setStarted_at_date(convertStringToLocalTime(dto.getStarted_at()));
		dto.setEnded_at_date(convertStringToLocalTime(dto.getEnded_at()));

        WorkLog updated = service.update(dto);
        addFeedbackMessage(attributes, FEEDBACK_MESSAGE_KEY_UPDATED, updated.getLogged_at());
        attributes.addAttribute(PARAMETER_ID, updated.getId());

        return createRedirectViewPath(REQUEST_MAPPING_VIEW);
    }
    
    @RequestMapping(value = "/worklog/delete/{id}", method = RequestMethod.GET)
    public String deleteById(@PathVariable("id") Long id, RedirectAttributes attributes) throws NotFoundException {
        WorkLog deleted = service.deleteById(id);
        addFeedbackMessage(attributes, FEEDBACK_MESSAGE_KEY_DELETED, deleted.getLogged_at());
        return createRedirectViewPath("/worklog/list/all");
    }
    
    private WorkLogDTO constructFormObjectForUpdateForm(WorkLog updated) {
    	WorkLogDTO dto = new WorkLogDTO();

        dto.setId(updated.getId());
        dto.setStory_id(updated.getStory_id());
        dto.setStory_title(updated.getStory_title());
        dto.setLogged_at(updated.getLogged_at().toString());
        dto.setLogged_at_date(updated.getLogged_at());
        dto.setStarted_at(updated.getStarted_at().toString());
        dto.setStarted_at_date(updated.getStarted_at());
        dto.setEnded_at(updated.getEnded_at().toString());
        dto.setEnded_at_date(updated.getEnded_at());
        dto.setDescription(updated.getDescription());

        return dto;
    }
    
    protected LocalDate convertStringToLocalDate(String strDate) {
    	DateTimeFormatter data_formatter = DateTimeFormatter.ofPattern(LOCAL_DATE_FORMAT);
		return LocalDate.parse(strDate, data_formatter);
    }
    
    protected LocalTime convertStringToLocalTime(String strTime) {
		DateTimeFormatter time_formatter = DateTimeFormatter.ISO_LOCAL_TIME;
		return LocalTime.parse(strTime, time_formatter);
    }
    
    private void addFeedbackMessage(RedirectAttributes attributes, String messageCode, Object... messageParameters) {
        String localizedFeedbackMessage = getMessage(messageCode, messageParameters);
        attributes.addFlashAttribute(FLASH_MESSAGE_KEY_FEEDBACK, localizedFeedbackMessage);
    }

    private String getMessage(String messageCode, Object... messageParameters) {
        Locale current = LocaleContextHolder.getLocale();
        return messageSource.getMessage(messageCode, messageParameters, current);
    }


    private String createRedirectViewPath(String requestMapping) {
        StringBuilder redirectViewPath = new StringBuilder();
        redirectViewPath.append("redirect:");
        redirectViewPath.append(requestMapping);
        return redirectViewPath.toString();
    }
}
