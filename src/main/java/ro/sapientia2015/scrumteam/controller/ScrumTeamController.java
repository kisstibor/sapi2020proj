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

	private final boolean INSERT_DUMMY_DATA = false;
	
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
    private ScrumTeamService service;
    
    @Resource
    private StoryService storyService;
    
    @Resource
    private MessageSource messageSource;
    
    @RequestMapping(value = "/scrumteam/add", method = RequestMethod.GET)
    public String showAddTeamForm(Model model) {
    	System.out.println(">>> REQUEST: /scrumteam/add  GET");
        ScrumTeamDTO formObject = new ScrumTeamDTO();
        
        List<Story> allStories = storyService.findAll();
        List<Story> unasignedStories = new ArrayList<Story>();
        
        if (allStories != null) {
	        for (Story story : allStories) {
	        	if (story.getScrumTeam() == null) {
	        		unasignedStories.add(story);
	        	}
	        }
        }
        
        formObject.setStories(unasignedStories);
        model.addAttribute(MODEL_ATTRIBUTE, formObject);

        return VIEW_ADD;
    }
    
    @RequestMapping(value = "/scrumteam/add", method = RequestMethod.POST)
    public String add(@Valid @ModelAttribute(MODEL_ATTRIBUTE) ScrumTeamDTO dto, BindingResult result, RedirectAttributes attributes) throws NotFoundException {
    	System.out.println(">>> REQUEST: /scrumteam/add  POST: " + dto.getName() + " " + dto.getMembers());
        if (result.hasErrors()) {
            return VIEW_ADD;
        }
        
        // Log inputs
        System.out.println(">>> SELECTED STORIES: ");
        if (dto.getSelectedStories() != null) {
	        for(String title : dto.getSelectedStories()) {
	        	System.out.println("object>>> " + title);
	        }
        }
        
        // put selected stories into model
        List<Story> selectedStoryObjects = ScrumTeam.filterStoriesByTitle(
        		storyService.findAll(), 
        		dto.getSelectedStories()
        );
        
        //dto.setStories(selectedStoryObjects);
        ScrumTeam scrumTeam = ScrumTeam.getBuilder(dto.getName())
    			.members(dto.getMembers())
    			.stories(dto.getStories())
    			.build();
        
        // Save ScrumTeam
        service.add(scrumTeam);// TO HERE
        
        
        // Connect selected Stories to ScrumTeams
        System.out.println(">>> SELECTED STORIES: ");
        for(Story s : selectedStoryObjects) {
        	System.out.println("input>>> Connect Story (id: " + s.getId() + " -> title: " + s.getTitle() +") --to--> Scrum-Team");
        	
        	s.setScrumTeam(scrumTeam);	//*// uncomment
        	//added.updateStory(s);
        	
        	try {
				storyService.update(s);
				
			} catch (NotFoundException e) {
				System.out.println(">>> ERROR can't update Stories after persisting ScrumTeams");
				e.printStackTrace();
			}
        }
        
        ScrumTeam added2 = service.update(scrumTeam);
        
        // Save ScrumTeam
        //ScrumTeam added = service.add(scrumTeam);// TO HERE
        ////////////////////ScrumTeam added2 = service.add(added); // FROM HERE
        
        // TESZT (NOT)
        for (Story s : storyService.findAll()) {
        	System.out.println("input>>> id: " + s.getId() + " -> title: " + s.getTitle() 
    		+ ",   TESZT: TEAM: " + (s.getScrumTeam()==null?"null":s.getScrumTeam().getId() + "). " + s.getScrumTeam().getName()
    		));
        	//save s
        }

        addFeedbackMessage(attributes, FEEDBACK_MESSAGE_KEY_ADDED, added2.getName());
        attributes.addAttribute(PARAMETER_ID, added2.getId());
        return createRedirectViewPath(REQUEST_MAPPING_VIEW);
    }
    
    
    @RequestMapping(value = REQUEST_MAPPING_VIEW, method = RequestMethod.GET)
    public String findTeamById(@PathVariable("id") Long id, Model model) throws NotFoundException {
    	System.out.println(">>> REQUEST: "+REQUEST_MAPPING_VIEW+" id: "+id+"  GET");
    	System.out.println(">>> REQUEST: findById("+id+")");
        ScrumTeam found = service.findById(id);
        
        // Find stories relating to this ScrumTeam (NO NEED)
        /*System.out.println(">>> Search stories for team.name: " + found.getName());
        for (Story story : storyService.findAll()) {
        	System.out.println(">>>   | story.team: " + (story.getScrumTeam()==null?"null":story.getScrumTeam()) );
        	if (story.getScrumTeam() != null) {
	        	if (story.getScrumTeam().getId() == found.getId()) {
	        		found.addStory(story);
	        	}
        	} else {
        		System.out.println(">>> WARNING >>> No related Stories found for ScrumTeam: " + found.getName());
        	}
        }*/

        System.out.println(">>> found --> team.name: " + (found==null?"null":found.getName()));
        for(Story s : found.getStories()) {
        	System.out.println(">>> | story:  id: " + s.getId() + ", title: " + s.getTitle());
        }
        
        ScrumTeamDTO foundDTO = new ScrumTeamDTO();
        foundDTO.setFrom(found);
        model.addAttribute(MODEL_ATTRIBUTE, foundDTO);
        return VIEW_VIEW;
    }
    
    
    @RequestMapping(value = REQUEST_MAPPING_LIST, method = RequestMethod.GET)
    public String findAll(Model model) {
    	System.out.println(">>> REQUEST: /  GET");
        List<ScrumTeam> models = service.findAll();
        
        if (INSERT_DUMMY_DATA) {
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
        }
        //
        
        //for (ScrumTeam)
        
        model.addAttribute(MODEL_ATTRIBUTE_LIST, models);
        return VIEW_LIST;
    }
    

    @RequestMapping(value = "/scrumteam/delete/{id}", method = RequestMethod.GET)
    public String deleteById(@PathVariable("id") Long id, RedirectAttributes attributes) throws NotFoundException {
    	System.out.println(">>> REQUEST: /scrumteam/delete/{" + id + "}  - GET");
    	ScrumTeam deleted = service.deleteById(id);
        addFeedbackMessage(attributes, FEEDBACK_MESSAGE_KEY_DELETED, deleted.getName());
        return createRedirectViewPath(REQUEST_MAPPING_LIST);
    }
   



    @RequestMapping(value = "/scrumteam/update/{id}", method = RequestMethod.GET)
    public String showUpdateForm(@PathVariable("id") Long id, Model model) throws NotFoundException {
    	System.out.println(">>> REQUEST: /scrumteam/update/{" + id + "}  - GET");
        ScrumTeam updated = service.findById(id);
        ScrumTeamDTO formObject = constructFormObjectForUpdateForm(updated);
        model.addAttribute(MODEL_ATTRIBUTE, formObject);

        return VIEW_UPDATE;
    }

    @RequestMapping(value = "/scrumteam/update", method = RequestMethod.POST)
    public String update(@Valid @ModelAttribute(MODEL_ATTRIBUTE) ScrumTeamDTO dto, BindingResult result, RedirectAttributes attributes) throws NotFoundException {
    	System.out.println(">>> REQUEST: /scrumteam/update/   - POST");
    	if (result.hasErrors()) {
            return VIEW_UPDATE;
        }

        ScrumTeam updated = service.update(dto);
        addFeedbackMessage(attributes, FEEDBACK_MESSAGE_KEY_UPDATED, updated.getName());
        attributes.addAttribute(PARAMETER_ID, updated.getId());

        return createRedirectViewPath(REQUEST_MAPPING_VIEW);
    }

    private ScrumTeamDTO constructFormObjectForUpdateForm(ScrumTeam updated) {
    	ScrumTeamDTO dto = new ScrumTeamDTO();

        dto.setId(updated.getId());
        dto.setName(updated.getName());
        dto.setMembers(updated.getMembers());
        dto.setStories(updated.getStories());

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