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

import ro.sapientia2015.story.dto.DailyDTO;
import ro.sapientia2015.story.exception.NotFoundException;
import ro.sapientia2015.story.model.Daily;
import ro.sapientia2015.story.service.DailyService;

@Controller
@SessionAttributes("daily")

public class DailyController {

	protected static final String FEEDBACK_MESSAGE_KEY_ADDED = "feedback.message.daily.added";
    protected static final String FEEDBACK_MESSAGE_KEY_UPDATED = "feedback.message.daily.updated";
    protected static final String FEEDBACK_MESSAGE_KEY_DELETED = "feedback.message.daily.deleted";

    protected static final String FLASH_MESSAGE_KEY_ERROR = "errorMessage";
    protected static final String FLASH_MESSAGE_KEY_FEEDBACK = "feedbackMessage";

    protected static final String MODEL_ATTRIBUTE = "daily";
    protected static final String MODEL_ATTRIBUTE_LIST = "dailies";

    protected static final String PARAMETER_ID = "id";
    protected static final String PARAMETER_DATE = "datee";
    protected static final String PARAMETER_DURATION = "duration";
    protected static final String PARAMETER_DESCRIPTION = "description";



    protected static final String REQUEST_MAPPING_LIST = "/daily/list";
    protected static final String REQUEST_MAPPING_VIEW = "/daily/{id}";
	
	 protected static final String VIEW_ADD = "daily/add";
	 protected static final String VIEW_LIST = "daily/list";
	 protected static final String VIEW_UPDATE = "daily/update";
	 protected static final String VIEW_VIEW = "daily/view";
	 
	 @Resource
	 private DailyService service;

	 @Resource
	 private MessageSource messageSource;
	    
	
	/*@RequestMapping(value = "/daily/list", method = RequestMethod.GET)
    public String showAddForm(Model model) {
        DailyDTO formObject = new DailyDTO();
        model.addAttribute(MODEL_ATTRIBUTE, formObject);

        return VIEW_LIST; 
    }*/
	
	@RequestMapping(value = "/daily/add", method = RequestMethod.GET)
	public String showForm(Model model) {

		DailyDTO daily = new DailyDTO();
		model.addAttribute("daily", daily);
		return VIEW_ADD;
	}
	
	@RequestMapping(value = "/daily/add", method = RequestMethod.POST)
    public String add(@Valid @ModelAttribute(MODEL_ATTRIBUTE) DailyDTO dto, BindingResult result, RedirectAttributes attributes) {
        if (result.hasErrors()) {
            return VIEW_ADD;
        }

        Daily added = service.add(dto);
        addFeedbackMessage(attributes, FEEDBACK_MESSAGE_KEY_ADDED, added.getTitle());
        attributes.addAttribute(PARAMETER_ID, added.getId());
        //attributes.addAttribute(PARAMETER_DURATION, added.getDuration());
        //attributes.addAttribute(PARAMETER_DATE, added.getDatee());
        //attributes.addAttribute(PARAMETER_DESCRIPTION, added.getDescription());

        return createRedirectViewPath(REQUEST_MAPPING_VIEW);
    }
	
	@RequestMapping(value = REQUEST_MAPPING_VIEW, method = RequestMethod.GET)
    public String findById(@PathVariable("id") Long id, Model model) throws NotFoundException {
        Daily found = service.findById(id);
        model.addAttribute(MODEL_ATTRIBUTE, found);
        return VIEW_VIEW;
    }
	
	@RequestMapping(value = REQUEST_MAPPING_LIST, method = RequestMethod.GET)
    public String findAll(Model model) {
        List<Daily> models = service.findAll();
        model.addAttribute(MODEL_ATTRIBUTE_LIST, models);
        return VIEW_LIST;
    }
	
	@RequestMapping(value = "/daily/delete/{id}", method = RequestMethod.GET)
    public String deleteById(@PathVariable("id") Long id, RedirectAttributes attributes) throws NotFoundException {
        Daily deleted = service.deleteById(id);
        addFeedbackMessage(attributes, FEEDBACK_MESSAGE_KEY_DELETED, deleted.getTitle());
        return createRedirectViewPath(REQUEST_MAPPING_LIST);
    }
	
	@RequestMapping(value = "/daily/update/{id}", method = RequestMethod.GET)
    public String showUpdateForm(@PathVariable("id") Long id, Model model) throws NotFoundException {
        Daily updated = service.findById(id);
        DailyDTO formObject = constructFormObjectForUpdateForm(updated);
        model.addAttribute(MODEL_ATTRIBUTE, formObject);

        return VIEW_UPDATE;
    }

    @RequestMapping(value = "/daily/update", method = RequestMethod.POST)
    public String update(@Valid @ModelAttribute(MODEL_ATTRIBUTE) DailyDTO dto, BindingResult result, RedirectAttributes attributes) throws NotFoundException {
        if (result.hasErrors()) {
            return VIEW_UPDATE;
        }

        Daily updated = service.update(dto);
        addFeedbackMessage(attributes, FEEDBACK_MESSAGE_KEY_UPDATED, updated.getTitle());
        attributes.addAttribute(PARAMETER_ID, updated.getId());
        attributes.addAttribute(PARAMETER_DURATION, updated.getDuration());
        attributes.addAttribute(PARAMETER_DATE, updated.getDatee());
        attributes.addAttribute(PARAMETER_DESCRIPTION, updated.getDescription());

        return createRedirectViewPath(REQUEST_MAPPING_VIEW);
    }
	
	private DailyDTO constructFormObjectForUpdateForm(Daily updated) {
        DailyDTO dto = new DailyDTO();

        dto.setId(updated.getId());
        dto.setDescription(updated.getDescription());
        dto.setTitle(updated.getTitle());
        dto.setDatee(updated.getDatee());
        dto.setDuration(updated.getDuration());

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
