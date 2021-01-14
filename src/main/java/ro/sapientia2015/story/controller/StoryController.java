package ro.sapientia2015.story.controller;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import ro.sapientia2015.story.dto.StoryDTO;
import ro.sapientia2015.story.exception.NotFoundException;
import ro.sapientia2015.story.model.Story;
import ro.sapientia2015.story.service.StoryService;

import javax.annotation.Resource;
import javax.validation.Valid;

import java.util.List;
import java.util.Locale;


@Controller
@SessionAttributes("story")
public class StoryController {

	private final boolean INSERT_DUMMY_DATA = true;
	
    protected static final String FEEDBACK_MESSAGE_KEY_ADDED = "feedback.message.story.added";
    protected static final String FEEDBACK_MESSAGE_KEY_UPDATED = "feedback.message.story.updated";
    protected static final String FEEDBACK_MESSAGE_KEY_DELETED = "feedback.message.story.deleted";


    protected static final String FLASH_MESSAGE_KEY_ERROR = "errorMessage";
    protected static final String FLASH_MESSAGE_KEY_FEEDBACK = "feedbackMessage";

    protected static final String MODEL_ATTRIBUTE = "story";
    protected static final String MODEL_ATTRIBUTE_LIST = "stories";

    protected static final String PARAMETER_ID = "id";

    protected static final String REQUEST_MAPPING_LIST = "/";
    protected static final String REQUEST_MAPPING_VIEW = "/story/{id}";

    protected static final String VIEW_ADD = "story/add";			// webapp/WEB-INF/jsp/story/add.jsp
    protected static final String VIEW_LIST = "story/list";
    protected static final String VIEW_UPDATE = "story/update";
    protected static final String VIEW_VIEW = "story/view";

    
    

    @Resource
    private StoryService service;
      

    @Resource
    private MessageSource messageSource;

    
    @RequestMapping(value = "/story/add", method = RequestMethod.GET)
    public String showAddForm(Model model) {
    	System.out.println(">>> REQUEST: /story/add  GET");
        StoryDTO formObject = new StoryDTO();
        model.addAttribute(MODEL_ATTRIBUTE, formObject);

        return VIEW_ADD;
    }

    @RequestMapping(value = "/story/add", method = RequestMethod.POST)
    public String add(@Valid @ModelAttribute(MODEL_ATTRIBUTE) StoryDTO dto, BindingResult result, RedirectAttributes attributes) {
    	System.out.println(">>> REQUEST: /story/add  POST: " + dto.getTitle() + " " + dto.getProgress() + " " + dto.getDescription());
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
    	System.out.println(">>> REQUEST: /  GET");
        List<Story> models = service.findAll();
        
        // ADD DUMMY DATA
        if (INSERT_DUMMY_DATA) {
	        if (models.size() == 0) {
	        	/*StoryDTO s1 = new StoryDTO();
	        	StoryDTO s2 = new StoryDTO();
	        	StoryDTO s3 = new StoryDTO();
	        	StoryDTO s4 = new StoryDTO();
	        	s1.setTitle("Story 1 - Add new item to stories");
	        	s1.setDescription("description 1");
	        	s1.setProgress(100);
	        	s2.setTitle("Story 2 - Update story");
	        	s2.setDescription("description 2");
	        	s2.setProgress(49);
	        	s3.setTitle("Story 3 - Delete story");
	        	s3.setDescription("description 3");
	        	s3.setProgress(0);
	        	s4.setTitle("Story 4 - Another Dummy story");
	        	s4.setDescription("description 4");
	        	s4.setProgress(1);
	        	service.add(s1);
	        	service.add(s2);
	        	service.add(s3);
	        	service.add(s4);
	        	models = service.findAll();*/
	        	
	        	StoryDTO s = new StoryDTO();
	        	s.setTitle("Story 1 - Nisl faucibus velit, in");
	        	s.setDescription("Lorem ipsum dolor sit amet, consectetur adipiscing elit. Phasellus interdum lacus sed odio cursus rhoncus eu at enim. Aliquam semper, leo eu ornare tempus, elit nisl faucibus velit, in aliquam lorem odio ut nulla. Duis porta pretium sapien vel porttitor. Pellentesque finibus mauris id faucibus convallis. Nam vitae mauris ultrices, gravida erat a, laoreet nisi. Curabitur a quam viverra, tempus libero nec, porttitor ipsum. Nam ornare sit amet dui luctus pretium. ");
	        	s.setProgress(100);
	        	service.add(s);
	        	
	        	s = new StoryDTO();
	        	s.setTitle("Story 2 - Duis porta pretium sapien ");
	        	s.setDescription("Lorem ipsum dolor sit amet, consectetur adipiscing elit. Phasellus interdum lacus sed odio cursus rhoncus eu at enim. Aliquam semper, leo eu ornare tempus, elit nisl faucibus velit, in aliquam lorem odio ut nulla. Duis porta pretium sapien vel porttitor. Pellentesque finibus mauris id faucibus convallis. Nam vitae mauris ultrices, gravida erat a, laoreet nisi. Curabitur a quam viverra, tempus libero nec, porttitor ipsum. Nam ornare sit amet dui luctus pretium. ");
	        	s.setProgress(100);
	        	service.add(s);
	        	
	        	s = new StoryDTO();
	        	s.setTitle("Story 3 - Enean sapien metus, molestie tempus massa in");
	        	s.setDescription("Lorem ipsum dolor sit amet, consectetur adipiscing elit. Phasellus interdum lacus sed odio cursus rhoncus eu at enim. Aliquam semper, leo eu ornare tempus, elit nisl faucibus velit, in aliquam lorem odio ut nulla. Duis porta pretium sapien vel porttitor. Pellentesque finibus mauris id faucibus convallis. Nam vitae mauris ultrices, gravida erat a, laoreet nisi. Curabitur a quam viverra, tempus libero nec, porttitor ipsum. Nam ornare sit amet dui luctus pretium. ");
	        	s.setProgress(99);
	        	service.add(s);
	        	
	        	s = new StoryDTO();
	        	s.setTitle("Story 4 - Mauris in ullamcorper felis");
	        	s.setDescription("Lorem ipsum dolor sit amet, consectetur adipiscing elit. Phasellus interdum lacus sed odio cursus rhoncus eu at enim. Aliquam semper, leo eu ornare tempus, elit nisl faucibus velit, in aliquam lorem odio ut nulla. Duis porta pretium sapien vel porttitor. Pellentesque finibus mauris id faucibus convallis. Nam vitae mauris ultrices, gravida erat a, laoreet nisi. Curabitur a quam viverra, tempus libero nec, porttitor ipsum. Nam ornare sit amet dui luctus pretium. ");
	        	s.setProgress(84);
	        	service.add(s);
	        	
	        	s = new StoryDTO();
	        	s.setTitle("Story 5 - Nullam massa est, volutpat vel pretium quis");
	        	s.setDescription("Lorem ipsum dolor sit amet, consectetur adipiscing elit. Phasellus interdum lacus sed odio cursus rhoncus eu at enim. Aliquam semper, leo eu ornare tempus, elit nisl faucibus velit, in aliquam lorem odio ut nulla. Duis porta pretium sapien vel porttitor. Pellentesque finibus mauris id faucibus convallis. Nam vitae mauris ultrices, gravida erat a, laoreet nisi. Curabitur a quam viverra, tempus libero nec, porttitor ipsum. Nam ornare sit amet dui luctus pretium. ");
	        	s.setProgress(55);
	        	service.add(s);
	        	
	        	s = new StoryDTO();
	        	s.setTitle("Story 6 - Mauris posuere fringilla interdum");
	        	s.setDescription("Lorem ipsum dolor sit amet, consectetur adipiscing elit. Phasellus interdum lacus sed odio cursus rhoncus eu at enim. Aliquam semper, leo eu ornare tempus, elit nisl faucibus velit, in aliquam lorem odio ut nulla. Duis porta pretium sapien vel porttitor. Pellentesque finibus mauris id faucibus convallis. Nam vitae mauris ultrices, gravida erat a, laoreet nisi. Curabitur a quam viverra, tempus libero nec, porttitor ipsum. Nam ornare sit amet dui luctus pretium. ");
	        	s.setProgress(33);
	        	service.add(s);
	        	
	        	s = new StoryDTO();
	        	s.setTitle("Story 7 - Mauris et sem gravida");
	        	s.setDescription("Lorem ipsum dolor sit amet, consectetur adipiscing elit. Phasellus interdum lacus sed odio cursus rhoncus eu at enim. Aliquam semper, leo eu ornare tempus, elit nisl faucibus velit, in aliquam lorem odio ut nulla. Duis porta pretium sapien vel porttitor. Pellentesque finibus mauris id faucibus convallis. Nam vitae mauris ultrices, gravida erat a, laoreet nisi. Curabitur a quam viverra, tempus libero nec, porttitor ipsum. Nam ornare sit amet dui luctus pretium. ");
	        	s.setProgress(16);
	        	service.add(s);
	        	
	        	s = new StoryDTO();
	        	s.setTitle("Story 8 - Integer vehicula");
	        	s.setDescription("Lorem ipsum dolor sit amet, consectetur adipiscing elit. Phasellus interdum lacus sed odio cursus rhoncus eu at enim. Aliquam semper, leo eu ornare tempus, elit nisl faucibus velit, in aliquam lorem odio ut nulla. Duis porta pretium sapien vel porttitor. Pellentesque finibus mauris id faucibus convallis. Nam vitae mauris ultrices, gravida erat a, laoreet nisi. Curabitur a quam viverra, tempus libero nec, porttitor ipsum. Nam ornare sit amet dui luctus pretium. ");
	        	s.setProgress(98);
	        	service.add(s);
	        	
	        	s = new StoryDTO();
	        	s.setTitle("Story 9 - Lorem ipsum dolor sit amet");
	        	s.setDescription("Lorem ipsum dolor sit amet, consectetur adipiscing elit. Phasellus interdum lacus sed odio cursus rhoncus eu at enim. Aliquam semper, leo eu ornare tempus, elit nisl faucibus velit, in aliquam lorem odio ut nulla. Duis porta pretium sapien vel porttitor. Pellentesque finibus mauris id faucibus convallis. Nam vitae mauris ultrices, gravida erat a, laoreet nisi. Curabitur a quam viverra, tempus libero nec, porttitor ipsum. Nam ornare sit amet dui luctus pretium. ");
	        	s.setProgress(35);
	        	service.add(s);
	        	
	        	s = new StoryDTO();
	        	s.setTitle("Story 10 - Nullam condimentum dolor ");
	        	s.setDescription("Lorem ipsum dolor sit amet, consectetur adipiscing elit. Phasellus interdum lacus sed odio cursus rhoncus eu at enim. Aliquam semper, leo eu ornare tempus, elit nisl faucibus velit, in aliquam lorem odio ut nulla. Duis porta pretium sapien vel porttitor. Pellentesque finibus mauris id faucibus convallis. Nam vitae mauris ultrices, gravida erat a, laoreet nisi. Curabitur a quam viverra, tempus libero nec, porttitor ipsum. Nam ornare sit amet dui luctus pretium. ");
	        	s.setProgress(48);
	        	service.add(s);

	        	models = service.findAll();
	        }
        }
        //
        
        model.addAttribute(MODEL_ATTRIBUTE_LIST, models);
        return VIEW_LIST;
    }
   
    
    @RequestMapping(value = REQUEST_MAPPING_VIEW, method = RequestMethod.GET)
    public String findById(@PathVariable("id") Long id, Model model) throws NotFoundException {
    	System.out.println("R>>> EQUEST: /story/"+id+"  GET");
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

    private StoryDTO constructFormObjectForUpdateForm(Story updated) {
        StoryDTO dto = new StoryDTO();

        dto.setId(updated.getId());
        dto.setTitle(updated.getTitle());
        dto.setDescription(updated.getDescription());
        dto.setProgress(updated.getProgress());

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
