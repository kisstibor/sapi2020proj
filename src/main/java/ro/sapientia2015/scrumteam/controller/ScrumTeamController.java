package ro.sapientia2015.scrumteam.controller;

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
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import ro.sapientia2015.scrumteam.dto.ScrumTeamDTO;
import ro.sapientia2015.scrumteam.model.ScrumTeam;
import ro.sapientia2015.scrumteam.service.ScrumTeamService;
import ro.sapientia2015.story.exception.NotFoundException;
import ro.sapientia2015.story.model.Story;
import ro.sapientia2015.story.service.StoryService;

@Controller
@SessionAttributes("scrumteam")
public class ScrumTeamController {

    protected static final String FEEDBACK_MESSAGE_KEY_ADDED = "feedback.message.scrumteam.added";
    protected static final String FEEDBACK_MESSAGE_KEY_UPDATED = "feedback.message.scrumteam.updated";
    protected static final String FEEDBACK_MESSAGE_KEY_DELETED = "feedback.message.scrumteam.deleted";

    protected static final String FLASH_MESSAGE_KEY_ERROR = "errorMessage";
    protected static final String FLASH_MESSAGE_KEY_FEEDBACK = "feedbackMessage";

    protected static final String MODEL_ATTRIBUTE = "team";			// add.jsp  -> input param name ("team-name" => .getName() method)
    protected static final String MODEL_ATTRIBUTE_LIST = "teams";	// list.jsp -> output param name

    protected static final String PARAMETER_ID = "id";

    protected static final String REQUEST_MAPPING_LIST = "/scrumteam";
    protected static final String REQUEST_MAPPING_VIEW = "/scrumteam/{id}";

    protected static final String VIEW_LIST = "scrumteam/list";			// webapp/WEB-INF/jsp/scrumteam/list.jsp
    protected static final String VIEW_ADD = "scrumteam/add";			
    protected static final String VIEW_UPDATE = "scrumteam/update";
    protected static final String VIEW_VIEW = "scrumteam/view";
    

    @Resource
    private StoryService storyService;
    
    @Resource
    private ScrumTeamService service;
    
    @Resource
    private MessageSource messageSource;
    
    @RequestMapping(value = "/scrumteam/add", method = RequestMethod.GET)
    public String showAddTeamForm(Model model) {
    	System.out.println(">>> REQUEST: /scrumteam/add  GET");
        ScrumTeamDTO formObject = new ScrumTeamDTO();
        formObject.setStories(storyService.findAll());
        model.addAttribute(MODEL_ATTRIBUTE, formObject);

        return VIEW_ADD;
    }
    
    @RequestMapping(value = "/scrumteam/add", method = RequestMethod.POST)
    public String add(@Valid @ModelAttribute(MODEL_ATTRIBUTE) ScrumTeamDTO dto, BindingResult result, RedirectAttributes attributes) {
    	System.out.println(">>> REQUEST: /scrumteam/add  POST: " + dto.getName() + " " + dto.getMembers());
        if (result.hasErrors()) {
            return VIEW_ADD;
        }
        
        // Log inputs
        System.out.println(">>> SELECTED STORIES: ");
        for(String title : dto.getSelectedStories()) {
        	System.out.println("object>>> " + title);
        }
        
        // put selected stories into model
        List<Story> storyObjs = ScrumTeam.filterStoriesByTitle(
        		storyService.findAll(), 
        		dto.getSelectedStories()
        );
        
        // Log selected objects
        System.out.println(">>> SELECTED STORIES: ");
        for(Story s : storyObjs) {
        	System.out.println("input>>> id: " + s.getId() + " -> title: " + s.getTitle());
        }
        
        dto.setStories(storyObjs);

        ScrumTeam added = service.add(dto);
        addFeedbackMessage(attributes, FEEDBACK_MESSAGE_KEY_ADDED, added.getName());
        attributes.addAttribute(PARAMETER_ID, added.getId());
        return createRedirectViewPath(REQUEST_MAPPING_VIEW);
    }
    
    
    @RequestMapping(value = REQUEST_MAPPING_VIEW, method = RequestMethod.GET)
    public String findTeamById(@PathVariable("id") Long id, Model model) throws NotFoundException {
    	System.out.println(">>> REQUEST: "+REQUEST_MAPPING_VIEW+" id: "+id+"  GET");
        ScrumTeam found = service.findById(id);
        model.addAttribute(MODEL_ATTRIBUTE, found);
        return VIEW_VIEW;
    }
    
    
    @RequestMapping(value = REQUEST_MAPPING_LIST, method = RequestMethod.GET)
    public String findAll(Model model) {
    	System.out.println(">>> REQUEST: /  GET");
        List<ScrumTeam> models = service.findAll();
        
        // ADD DUMMY DATA
        if (models.size() == 0) {
        	List<Story> stories = storyService.findAll();
        	if (stories.size() > 2) {
        		ScrumTeamDTO s1 = new ScrumTeamDTO();
        		ScrumTeamDTO s2 = new ScrumTeamDTO();
        		List<Story> sl1 = new ArrayList<Story>();
        		List<Story> sl2 = new ArrayList<Story>();
        		sl1.add(stories.get(0));
        		sl1.add(stories.get(1));
        		s1.setName("Alpha");
        		s1.setMembers("Albert, Adam, alkesz Alamer");
        		s1.setStories(sl1);
        		sl2.add(stories.get(2));
        		s2.setName("Beta");
        		s2.setMembers("Bela, Bob, Bill Bow");
        		s2.setStories(sl2);
        		service.add(s1);
            	service.add(s2);
            	models = service.findAll();
        	}
        }
        //
        
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