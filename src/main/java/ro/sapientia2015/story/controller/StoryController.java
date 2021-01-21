package ro.sapientia2015.story.controller;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import ro.sapientia2015.story.dto.StoryDTO;
import ro.sapientia2015.story.dto.VacationDTO;
import ro.sapientia2015.story.exception.NotFoundException;
import ro.sapientia2015.story.model.Story;
import ro.sapientia2015.story.model.Vacation;
import ro.sapientia2015.story.service.StoryService;
import ro.sapientia2015.story.service.VacationService;

import javax.annotation.Resource;
import javax.validation.Valid;

import java.util.List;
import java.util.Locale;

/**
 * @author Kiss Tibor
 */
@Controller
@SessionAttributes("story")
public class StoryController {

    protected static final String FEEDBACK_MESSAGE_KEY_ADDED = "feedback.message.story.added";
    protected static final String FEEDBACK_MESSAGE_KEY_UPDATED = "feedback.message.story.updated";
    protected static final String FEEDBACK_MESSAGE_KEY_DELETED = "feedback.message.story.deleted";

    protected static final String FEEDBACK_MESSAGE_KEY_ADDED_VACATION = "feedback.message.vacation.added";
    protected static final String FEEDBACK_MESSAGE_KEY_DELETED_VACATION  = "feedback.message.vacation.deleted";
    protected static final String FEEDBACK_MESSAGE_KEY_UPDATED_VACATION = "feedback.message.vacation.updated";
    
    protected static final String FLASH_MESSAGE_KEY_ERROR = "errorMessage";
    protected static final String FLASH_MESSAGE_KEY_FEEDBACK = "feedbackMessage";
    
    protected static final String MODEL_ATTRIBUTE = "story";
    protected static final String MODEL_ATTRIBUTE_LIST = "stories";
    protected static final String MODEL_ATTRIBUTE_VACATION= "vacation";
    

    protected static final String PARAMETER_ID = "id";

    protected static final String REQUEST_MAPPING_LIST = "/";
    protected static final String REQUEST_MAPPING_VIEW = "/story/{id}";
    
    protected static final String REQUEST_MAPPING_VACATION_LIST = "story/vacation/";
    protected static final String REQUEST_MAPPING_VACATION_VIEW = "/vacation/{id}";

    protected static final String VIEW_ADD = "story/add";
    protected static final String VIEW_LIST = "story/list";
    protected static final String VIEW_UPDATE = "story/update";
    protected static final String VIEW_VIEW = "story/view";

    protected static final String VIEW_VACATION_ADD = "story/vacation/add";
    protected static final String VIEW_UPDATE_VACATION = "story/vacation/update";
    protected static final String VIEW_VACATION_VIEW = "story/vacation/view";
    protected static final String VIEW_VACATION_LIST= "story/vacation/list";

    @Resource
    private StoryService service;
    
    @Resource
    private VacationService vacationService;

    @Resource
    private MessageSource messageSource;

    @RequestMapping(value = "/story/add", method = RequestMethod.GET)
    public String showAddForm(Model model) {
        StoryDTO formObject = new StoryDTO();
        model.addAttribute(MODEL_ATTRIBUTE, formObject);

        return VIEW_ADD;
    }

    @RequestMapping(value = "/story/add", method = RequestMethod.POST)
    public String add(@Valid @ModelAttribute(MODEL_ATTRIBUTE) StoryDTO dto, BindingResult result, RedirectAttributes attributes) {
        if (result.hasErrors()) {
            return VIEW_ADD;
        }

        Story added = service.add(dto);
        addFeedbackMessage(attributes, FEEDBACK_MESSAGE_KEY_ADDED, added.getTitle());
        attributes.addAttribute(PARAMETER_ID, added.getId());

        return createRedirectViewPath(REQUEST_MAPPING_VIEW);
    }

    @RequestMapping(value = "/story/delete/{id}", method = RequestMethod.GET)
    public String deleteById(@PathVariable("id") Long id, RedirectAttributes attributes) throws NotFoundException {
        Story deleted = service.deleteById(id);
        addFeedbackMessage(attributes, FEEDBACK_MESSAGE_KEY_DELETED, deleted.getTitle());
        return createRedirectViewPath(REQUEST_MAPPING_LIST);
    }

    @RequestMapping(value = REQUEST_MAPPING_LIST, method = RequestMethod.GET)
    public String findAll(Model model) {
        List<Story> models = service.findAll();
        model.addAttribute(MODEL_ATTRIBUTE_LIST, models);
        return VIEW_LIST;
    }

    @RequestMapping(value = REQUEST_MAPPING_VIEW, method = RequestMethod.GET)
    public String findById(@PathVariable("id") Long id, Model model) throws NotFoundException {
        Story found = service.findById(id);
        model.addAttribute(MODEL_ATTRIBUTE, found);
        
        return VIEW_VIEW;
    }

    @RequestMapping(value = "/story/update/{id}", method = RequestMethod.GET)
    public String showUpdateForm(@PathVariable("id") Long id, Model model) throws NotFoundException {
        Story updated = service.findById(id);
        StoryDTO formObject = constructFormObjectForUpdateForm(updated);
        model.addAttribute(MODEL_ATTRIBUTE, formObject);

        return VIEW_UPDATE;
    }

    @RequestMapping(value = "/story/update", method = RequestMethod.POST)
    public String update(@Valid @ModelAttribute(MODEL_ATTRIBUTE) StoryDTO dto, BindingResult result, RedirectAttributes attributes) throws NotFoundException {
        if (result.hasErrors()) {
            return VIEW_UPDATE;
        }

        Story updated = service.update(dto);
        addFeedbackMessage(attributes, FEEDBACK_MESSAGE_KEY_UPDATED, updated.getTitle());
        attributes.addAttribute(PARAMETER_ID, updated.getId());

        return createRedirectViewPath(REQUEST_MAPPING_VIEW);
    }

    @RequestMapping(value = "/story/vacation/add", method = RequestMethod.GET)
    public String showAddVacationForm(Model model) {
        VacationDTO formObject = new VacationDTO();
        model.addAttribute(MODEL_ATTRIBUTE_VACATION, formObject);

        return VIEW_VACATION_ADD;
    }

    @RequestMapping(value = "/story/vacation/add", method = RequestMethod.POST)
    public String add(@Valid @ModelAttribute(MODEL_ATTRIBUTE_VACATION) VacationDTO dto, BindingResult result, RedirectAttributes attributes) {
        if (result.hasErrors()) {
            return VIEW_VACATION_ADD;
        }

        Vacation added = vacationService.add(dto);
        addFeedbackMessage(attributes, FEEDBACK_MESSAGE_KEY_ADDED, added.getVacationStartDate());
        attributes.addAttribute(PARAMETER_ID, added.getId());

        return createRedirectViewPath(REQUEST_MAPPING_VACATION_VIEW);
    }
    
    
    @RequestMapping(value = "/story/vacation/delete/{id}", method = RequestMethod.GET)
    public String deleteVacationById(@PathVariable("id") Long id, RedirectAttributes attributes) throws NotFoundException {
        Vacation deleted = vacationService.deleteById(id);
        addFeedbackMessage(attributes, FEEDBACK_MESSAGE_KEY_DELETED_VACATION, deleted.getId());
        attributes.addAttribute(PARAMETER_ID, id);
        return createRedirectViewPath(REQUEST_MAPPING_VACATION_VIEW);
    }
    

    @RequestMapping(value = REQUEST_MAPPING_VACATION_LIST, method = RequestMethod.GET)
    public String findAllVacation(Model model) {
        List<Vacation> models = vacationService.findAll();
        System.out.println(models);
        model.addAttribute(MODEL_ATTRIBUTE_VACATION, models);
        return VIEW_VACATION_LIST;
    }

    @RequestMapping(value = REQUEST_MAPPING_VACATION_VIEW, method = RequestMethod.GET)
    public String findByIdVacations(@PathVariable("id") Long id, Model model) throws NotFoundException {
        Vacation vacation= vacationService.findById(id);
        model.addAttribute(MODEL_ATTRIBUTE_VACATION, vacation);
        
        return VIEW_VACATION_VIEW;
    }

    @RequestMapping(value = "/story/vacation/update/{id}", method = RequestMethod.GET)
    public String showUpdateVacationForm(@PathVariable("id") Long id, Model model) throws NotFoundException {
        Vacation updated = vacationService.findById(id);
        VacationDTO formObject = constructFormObjectForUpdateFormVacation(updated);
        model.addAttribute(MODEL_ATTRIBUTE_VACATION, formObject);

        Vacation found = vacationService.findById(id);
        model.addAttribute(MODEL_ATTRIBUTE_VACATION, found);

        return VIEW_UPDATE_VACATION;
    }

    @RequestMapping(value = "/story/vacation/update/{id}", method = RequestMethod.POST)
    public String updateTimelimit(@Valid @ModelAttribute(MODEL_ATTRIBUTE_VACATION) VacationDTO dto,@PathVariable("id") Long id, BindingResult result, RedirectAttributes attributes) throws NotFoundException {
        if (result.hasErrors()) {
            return VIEW_UPDATE_VACATION;
        }

        Vacation updated = vacationService.update(dto);
        addFeedbackMessage(attributes, FEEDBACK_MESSAGE_KEY_UPDATED_VACATION, updated.getId());
        attributes.addAttribute(PARAMETER_ID, id);

        return createRedirectViewPath(REQUEST_MAPPING_VACATION_VIEW);
    }

    private VacationDTO constructFormObjectForUpdateFormVacation(Vacation updated) {
    	VacationDTO dto = new VacationDTO();

        dto.setId(updated.getId());
        dto.setVacationStartDate(updated.getVacationStartDate());
        dto.setVacationEndDate(updated.getVacationEndDate());

        return dto;
    }


    private StoryDTO constructFormObjectForUpdateForm(Story updated) {
        StoryDTO dto = new StoryDTO();

        dto.setId(updated.getId());
        dto.setDescription(updated.getDescription());
        dto.setTitle(updated.getTitle());

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
