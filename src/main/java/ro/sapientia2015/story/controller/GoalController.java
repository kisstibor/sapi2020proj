package ro.sapientia2015.story.controller;

import java.util.List;
import java.util.Locale;

import javax.annotation.Resource;
import javax.validation.Valid;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import ro.sapientia2015.story.dto.GoalDTO;
import ro.sapientia2015.story.exception.NotFoundException;
import ro.sapientia2015.story.model.Goal;
import ro.sapientia2015.story.service.GoalService;

@Controller
@SessionAttributes("goal")
public class GoalController {
	
	@Resource
	private GoalService service;
	
    @Resource
    private MessageSource messageSource;
    
    protected static final String FEEDBACK_MESSAGE_KEY_ADDED = "feedback.message.goal.added";
    protected static final String FEEDBACK_MESSAGE_KEY_UPDATED = "feedback.message.goal.updated";
    protected static final String FEEDBACK_MESSAGE_KEY_DELETED = "feedback.message.goal.deleted";

    protected static final String FLASH_MESSAGE_KEY_ERROR = "errorMessage";
    protected static final String FLASH_MESSAGE_KEY_FEEDBACK = "feedbackMessage";
    
    protected static final String MODEL_ATTRIBUTE = "goal";
    protected static final String MODEL_ATTRIBUTE_LIST = "goals";
    
    protected static final String PARAMETER_ID = "id";
    
    protected static final String REQUEST_MAPPING_LIST = "/goal/list";
    protected static final String REQUEST_MAPPING_VIEW = "/goal/{id}";
	
	public static final String VIEW_LIST = "goal/list";
	public static final String VIEW_ADD = "goal/add";
    protected static final String VIEW_UPDATE = "goal/update";
    protected static final String VIEW_VIEW = "goal/view";
	
	@RequestMapping(value = "/goal/list", method = RequestMethod.GET)
	public String listGoals(Model model) {

		List<Goal> goals = service.findAll();
		model.addAttribute("goals", goals);
		return VIEW_LIST;
	}
	
	@RequestMapping(value = "/goal/add", method = RequestMethod.GET)
	public String showForm(Model model) {

		GoalDTO goal = new GoalDTO();
		model.addAttribute("goal", goal);
		return VIEW_ADD;
	}
	
    @RequestMapping(value = REQUEST_MAPPING_VIEW, method = RequestMethod.GET)
    public String findById(@PathVariable("id") Integer id, Model model) throws NotFoundException {
        Goal found = service.findById(id);
        model.addAttribute(MODEL_ATTRIBUTE, found);
        return VIEW_VIEW;
    }

	
	@RequestMapping(value = "/goal/add", method = RequestMethod.POST)
	public String add(@Valid @ModelAttribute(MODEL_ATTRIBUTE) GoalDTO dto, BindingResult result, RedirectAttributes attributes) {
		if(result.hasErrors()){
			return VIEW_ADD;
		}
		
		Goal added = service.add(dto);
        addFeedbackMessage(attributes, FEEDBACK_MESSAGE_KEY_ADDED, added.getGoal());
        attributes.addAttribute(PARAMETER_ID, added.getId());
		
		return createRedirectViewPath(REQUEST_MAPPING_VIEW);
	}
	
    @RequestMapping(value = "/goal/update/{id}", method = RequestMethod.GET)
    public String showUpdateForm(@PathVariable("id") Integer id, Model model) throws NotFoundException {
        Goal updated = service.findById(id);
        GoalDTO formObject = constructFormObjectForUpdateForm(updated);
        model.addAttribute(MODEL_ATTRIBUTE, formObject);

        return VIEW_UPDATE;
    }

    @RequestMapping(value = "/goal/update", method = RequestMethod.POST)
    public String update(@Valid @ModelAttribute(MODEL_ATTRIBUTE) GoalDTO dto, BindingResult result, RedirectAttributes attributes) throws NotFoundException {
        if (result.hasErrors()) {
            return VIEW_UPDATE;
        }

        Goal updated = service.update(dto);
        addFeedbackMessage(attributes, FEEDBACK_MESSAGE_KEY_UPDATED, updated.getGoal());
        attributes.addAttribute(PARAMETER_ID, updated.getId());

        return createRedirectViewPath(REQUEST_MAPPING_VIEW);
    }
	
    @RequestMapping(value = "/goal/delete/{id}", method = RequestMethod.GET)
    public String deleteById(@PathVariable("id") Integer id, RedirectAttributes attributes) throws NotFoundException {
        Goal deleted = service.deleteById(id);
        addFeedbackMessage(attributes, FEEDBACK_MESSAGE_KEY_DELETED, deleted.getGoal());
        return createRedirectViewPath(REQUEST_MAPPING_LIST);
    }
    
    private GoalDTO constructFormObjectForUpdateForm(Goal updated) {
        GoalDTO dto = new GoalDTO();

        dto.setId(updated.getId());
        dto.setGoal(updated.getGoal());
        dto.setMethod(updated.getMethod());
        dto.setMetrics(updated.getMetrics());

        return dto;
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