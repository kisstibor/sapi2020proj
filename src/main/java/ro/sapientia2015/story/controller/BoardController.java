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

import ro.sapientia2015.story.dto.StoryDTO;
import ro.sapientia2015.story.dto.TaskDTO;
import ro.sapientia2015.story.exception.NotFoundException;
import ro.sapientia2015.story.model.Story;
import ro.sapientia2015.story.model.Task;
import ro.sapientia2015.story.service.StoryService;
import ro.sapientia2015.story.service.TaskService;

@Controller
//@SessionAttributes("board")
public class BoardController {
	
	protected static final String FEEDBACK_MESSAGE_KEY_ADDED = "feedback.message.story.added";
    protected static final String FEEDBACK_MESSAGE_KEY_UPDATED = "feedback.message.story.updated";
    protected static final String FEEDBACK_MESSAGE_KEY_DELETED = "feedback.message.story.deleted";

    protected static final String FLASH_MESSAGE_KEY_ERROR = "errorMessage";
    protected static final String FLASH_MESSAGE_KEY_FEEDBACK = "feedbackMessage";

    protected static final String MODEL_ATTRIBUTE = "task";
    protected static final String MODEL_ATTRIBUTE_LIST = "tasks";

    protected static final String PARAMETER_ID = "id";

    protected static final String REQUEST_MAPPING_LIST = "/";
    protected static final String REQUEST_MAPPING_VIEW = "/board/{id}";

    protected static final String VIEW_BOARD = "story/view_board";
    protected static final String VIEW_ADD = "story/add_task";
    protected static final String VIEW_UPDATE = "story/view_task";
    protected static final String VIEW_VIEW = "story/view";
    
    @Resource
    private TaskService service;
    
    @Resource
    private MessageSource messageSource;
    
    @RequestMapping(value = "/board", method = RequestMethod.GET)
    public String showBoard(Model model) {
    	List<Task> models = service.findAll();
        model.addAttribute(MODEL_ATTRIBUTE_LIST, models);

        return VIEW_BOARD;
    }
    
    @RequestMapping(value = "/board/add", method = RequestMethod.GET)
    public String showAddForm(Model model) {
    	TaskDTO formObject = new TaskDTO();
        model.addAttribute(MODEL_ATTRIBUTE, formObject);

        return VIEW_ADD;
    }
    
    @RequestMapping(value = "/board/migrate/r/{id}", method = RequestMethod.GET)
    public String migrateR(@PathVariable("id") Long id, RedirectAttributes attributes) throws NotFoundException {
        Task task = service.findById(id);
        TaskDTO formObject = constructFormObjectForUpdateForm(task);
        if(formObject.getState() == "todo")
        	formObject.setState("inprogress");
        else formObject.setState("done");
        service.update(formObject);
        return createRedirectViewPath("/board");
    }
    
    @RequestMapping(value = "/board/migrate/l/{id}", method = RequestMethod.GET)
    public String migrateL(@PathVariable("id") Long id, RedirectAttributes attributes) throws NotFoundException {
        Task task = service.findById(id);
        TaskDTO formObject = constructFormObjectForUpdateForm(task);
        if(formObject.getState() == "inprogress")
        	formObject.setState("todo");
        else formObject.setState("inprogress");
        service.update(formObject);
        return createRedirectViewPath("/board");
    }

    @RequestMapping(value = "/board/show/{id}", method = RequestMethod.GET)
    public String showUpdateForm(@PathVariable("id") Long id, Model model) throws NotFoundException {
        Task updated = service.findById(id);
        TaskDTO formObject = constructFormObjectForUpdateForm(updated);
        model.addAttribute("story", formObject);

        return VIEW_UPDATE;
    }
    
    @RequestMapping(value = "/board/delete/{id}", method = RequestMethod.GET)
    public String deleteTask(@PathVariable("id") Long id,  RedirectAttributes attributes) throws NotFoundException {
        Task deleted = service.findById(id);
        addFeedbackMessage(attributes, FEEDBACK_MESSAGE_KEY_DELETED, deleted.getTitle());
        service.deleteById(id);
        return createRedirectViewPath("/board");
    }
    
    @RequestMapping(value = "/board/add", method = RequestMethod.POST)
    public String add(@Valid @ModelAttribute(MODEL_ATTRIBUTE) TaskDTO dto, BindingResult result, RedirectAttributes attributes) {
        if (result.hasErrors()) {
        	System.out.println("We have problems here.");
        	System.out.println(dto.toString());
            return VIEW_ADD;
        }
        
        dto.setState("todo");
        System.out.println("We have problems here.");
        System.out.println("We have problems here.");
        System.out.println(dto.toString());
		Task added = service.add(dto);
		System.out.println(added.getState());
        addFeedbackMessage(attributes, FEEDBACK_MESSAGE_KEY_ADDED, added.getTitle());
        attributes.addAttribute(PARAMETER_ID, added.getId());

        return createRedirectViewPath("/board");
    }
    
    private String createRedirectViewPath(String requestMapping) {
        StringBuilder redirectViewPath = new StringBuilder();
        redirectViewPath.append("redirect:");
        redirectViewPath.append(requestMapping);
        return redirectViewPath.toString();
    }
    
    private void addFeedbackMessage(RedirectAttributes attributes, String messageCode, Object... messageParameters) {
        String localizedFeedbackMessage = getMessage(messageCode, messageParameters);
        attributes.addFlashAttribute(FLASH_MESSAGE_KEY_FEEDBACK, localizedFeedbackMessage);
    }


    private String getMessage(String messageCode, Object... messageParameters) {
        Locale current = LocaleContextHolder.getLocale();
        return messageSource.getMessage(messageCode, messageParameters, current);
    }
    
    TaskDTO constructFormObjectForUpdateForm(Task updated) {
        TaskDTO dto = new TaskDTO();

        dto.setId(updated.getId());
        dto.setDescription(updated.getDescription());
        dto.setTitle(updated.getTitle());
        dto.setState(updated.getState());

        return dto;
    }
}
