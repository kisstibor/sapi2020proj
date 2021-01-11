package ro.sapientia2015.scrum.controller;

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

import ro.sapientia2015.scrum.dto.ScrumDTO;
import ro.sapientia2015.scrum.model.Scrum;
import ro.sapientia2015.scrum.service.ScrumService;
import ro.sapientia2015.story.exception.NotFoundException;

@Controller
@SessionAttributes("scrum")
public class ScrumController {

    protected static final String FEEDBACK_MESSAGE_KEY_ADDED = "feedback.message.scrum.added";
    protected static final String FEEDBACK_MESSAGE_KEY_UPDATED = "feedback.message.scrum.updated";
    protected static final String FEEDBACK_MESSAGE_KEY_DELETED = "feedback.message.scrum.deleted";

    protected static final String FLASH_MESSAGE_KEY_ERROR = "errorMessage";
    protected static final String FLASH_MESSAGE_KEY_FEEDBACK = "feedbackMessage";

    //add.jsp output model
    protected static final String MODEL_ATTRIBUTE = "scrum";
    //list.jsp input model
    protected static final String MODEL_ATTRIBUTE_LIST = "scrums";

    protected static final String PARAMETER_ID = "id";

    protected static final String REQUEST_MAPPING_LIST = "/scrum";
    protected static final String REQUEST_MAPPING_VIEW = "/scrum/{id}";

    // webapp/WEB-INF/jsp/scrum/list.jsp
    protected static final String VIEW_LIST = "scrum/list";
    protected static final String VIEW_ADD = "scrum/add";			
    protected static final String VIEW_UPDATE = "scrum/update";
    protected static final String VIEW_VIEW = "scrum/view";
    

    @Resource
    private ScrumService service;
    
    @Resource
    private MessageSource messageSource;
    
    @RequestMapping(value = "/scrum/add", method = RequestMethod.GET)
    public String showAddTeamForm(Model model) {
    	System.out.println("gotcha get /scrum/add");
        ScrumDTO formObject = new ScrumDTO();
        model.addAttribute(MODEL_ATTRIBUTE, formObject);

        return VIEW_ADD;
    }
    
    @RequestMapping(value = "/scrum/add", method = RequestMethod.POST)
    public String add(@Valid @ModelAttribute(MODEL_ATTRIBUTE) ScrumDTO dto, BindingResult result, RedirectAttributes attributes) {
    	System.out.println("gotcha post /scrum/add " + dto.getTitle() + ": " + dto.getMembers());
        if (result.hasErrors()) {
            return VIEW_ADD;
        }

        Scrum added = service.add(dto);
        addFeedbackMessage(attributes, FEEDBACK_MESSAGE_KEY_ADDED, added.getTitle());
        attributes.addAttribute(PARAMETER_ID, added.getId());

        return createRedirectViewPath(REQUEST_MAPPING_VIEW);
    }
    
    
    @RequestMapping(value = REQUEST_MAPPING_VIEW, method = RequestMethod.GET)
    public String findTeamById(@PathVariable("id") Long id, Model model) throws NotFoundException {
    	System.out.println("gotcha get " + REQUEST_MAPPING_VIEW+" id: "+id);
        Scrum found = service.findById(id);
        System.out.println("GOTCHA findTeamById " + found.getTitle());
        model.addAttribute(MODEL_ATTRIBUTE, found);
        return VIEW_VIEW;
    }
    
    
    @RequestMapping(value = REQUEST_MAPPING_LIST, method = RequestMethod.GET)
    public String findAll(Model model) {
    	System.out.println("gotcha get");
        List<Scrum> models = service.findAll();
        
     // ADD DUMMY DATA
        if (models.size() == 0) {
    		ScrumDTO s1 = new ScrumDTO();
    		ScrumDTO s2 = new ScrumDTO();
    		s1.setTitle("Elso");
    		s1.setMembers("Alex, Beci, Kamilla");
    		s2.setTitle("Masodik");
    		s2.setMembers("Pisti, Tundi, Kati");
    		service.add(s1);
        	service.add(s2);
        	models = service.findAll();
        }
        
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