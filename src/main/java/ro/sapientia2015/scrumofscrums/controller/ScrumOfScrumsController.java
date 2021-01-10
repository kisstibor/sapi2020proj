package ro.sapientia2015.scrumofscrums.controller;

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

import ro.sapientia2015.scrumofscrums.dto.ScrumOfScrumsDTO;
import ro.sapientia2015.scrumofscrums.model.ScrumOfScrums;
import ro.sapientia2015.scrumofscrums.service.ScrumOfScrumsService;
import ro.sapientia2015.story.exception.NotFoundException;

@Controller
@SessionAttributes("scrumofscrums")
public class ScrumOfScrumsController {

    protected static final String FEEDBACK_MESSAGE_KEY_ADDED = "feedback.message.scrumofscrums.added";
    protected static final String FEEDBACK_MESSAGE_KEY_UPDATED = "feedback.message.scrumofscrums.updated";
    protected static final String FEEDBACK_MESSAGE_KEY_DELETED = "feedback.message.scrumofscrums.deleted";

    protected static final String FLASH_MESSAGE_KEY_ERROR = "errorMessage";
    protected static final String FLASH_MESSAGE_KEY_FEEDBACK = "feedbackMessage";

    protected static final String MODEL_ATTRIBUTE = "sos";
    protected static final String MODEL_ATTRIBUTE_LIST = "sos_list";

    protected static final String PARAMETER_ID = "id";

    protected static final String REQUEST_MAPPING_LIST = "/scrumofscrums";
    protected static final String REQUEST_MAPPING_VIEW = "/scrumofscrums/{id}";

    protected static final String VIEW_LIST = "scrumofscrums/list";			// webapp/WEB-INF/jsp/scrumofscrums/list.jsp
    protected static final String VIEW_ADD = "scrumofscrums/add";			
    protected static final String VIEW_UPDATE = "scrumofscrums/update";
    protected static final String VIEW_VIEW = "scrumofscrums/view";
    

    @Resource
    private ScrumOfScrumsService service;
    
    @Resource
    private MessageSource messageSource;
    
    @RequestMapping(value = "/scrumofscrums/add", method = RequestMethod.GET)
    public String showAddTeamForm(Model model) {
    	System.out.println(">>> REQUEST: /scrumofscrums/add  GET");
    	ScrumOfScrumsDTO formObject = new ScrumOfScrumsDTO();
        model.addAttribute(MODEL_ATTRIBUTE, formObject);

        return VIEW_ADD;
    }
    
    @RequestMapping(value = "/scrumofscrums/add", method = RequestMethod.POST)
    public String add(@Valid @ModelAttribute(MODEL_ATTRIBUTE) ScrumOfScrumsDTO dto, BindingResult result, RedirectAttributes attributes) {
    	System.out.println(">>> REQUEST: /scrumofscrums/add  POST: " + dto.getName() + " " + dto.getDescription());
        if (result.hasErrors()) {
            return VIEW_ADD;
        }

        ScrumOfScrums added = service.add(dto);
        addFeedbackMessage(attributes, FEEDBACK_MESSAGE_KEY_ADDED, added.getName());
        attributes.addAttribute(PARAMETER_ID, added.getId());

        return createRedirectViewPath(REQUEST_MAPPING_VIEW);
    }
    
    
    @RequestMapping(value = REQUEST_MAPPING_VIEW, method = RequestMethod.GET)
    public String findTeamById(@PathVariable("id") Long id, Model model) throws NotFoundException {
    	System.out.println(">>> REQUEST: "+REQUEST_MAPPING_VIEW+" id: "+id+"  GET");
        ScrumOfScrums found = service.findById(id);
        model.addAttribute(MODEL_ATTRIBUTE, found);
        return VIEW_VIEW;
    }
    
    
    @RequestMapping(value = REQUEST_MAPPING_LIST, method = RequestMethod.GET)
    public String findAll(Model model) {
    	System.out.println(">>> REQUEST: /  GET");
        List<ScrumOfScrums> models = service.findAll();
        model.addAttribute(MODEL_ATTRIBUTE_LIST, models);
        return VIEW_LIST;
    }
    

    /*    
    @RequestMapping(value = "/story/delete/{id}", method = RequestMethod.GET)
    public String deleteById(@PathVariable("id") Long id, RedirectAttributes attributes) throws NotFoundException {
        Story deleted = storyService.deleteById(id);
        addFeedbackMessage(attributes, FEEDBACK_MESSAGE_KEY_DELETED, deleted.getTitle());
        return createRedirectViewPath(REQUEST_MAPPING_LIST);
    }
   



    @RequestMapping(value = "/story/update/{id}", method = RequestMethod.GET)
    public String showUpdateForm(@PathVariable("id") Long id, Model model) throws NotFoundException {
        Story updated = storyService.findById(id);
        StoryDTO formObject = constructFormObjectForUpdateForm(updated);
        model.addAttribute(MODEL_ATTRIBUTE, formObject);

        return VIEW_UPDATE;
    }

    @RequestMapping(value = "/story/update", method = RequestMethod.POST)
    public String update(@Valid @ModelAttribute(MODEL_ATTRIBUTE) StoryDTO dto, BindingResult result, RedirectAttributes attributes) throws NotFoundException {
        if (result.hasErrors()) {
            return VIEW_UPDATE;
        }

        Story updated = storyService.update(dto);
        addFeedbackMessage(attributes, FEEDBACK_MESSAGE_KEY_UPDATED, updated.getTitle());
        attributes.addAttribute(PARAMETER_ID, updated.getId());

        return createRedirectViewPath(REQUEST_MAPPING_VIEW);
    }

    private StoryDTO constructFormObjectForUpdateForm(Story updated) {
        StoryDTO dto = new StoryDTO();

        dto.setId(updated.getId());
        dto.setDescription(updated.getDescription());
        dto.setTitle(updated.getTitle());

        return dto;
    }
    */

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