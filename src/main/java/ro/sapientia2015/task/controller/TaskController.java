package ro.sapientia2015.task.controller;

import java.util.List;
import java.util.Locale;

import javax.annotation.Resource;
import javax.validation.Valid;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import ro.sapientia2015.task.dto.TaskDTO;
import ro.sapientia2015.story.exception.NotFoundException;
import ro.sapientia2015.story.model.Story;
import ro.sapientia2015.story.service.StoryService;
import ro.sapientia2015.task.model.Task;
import ro.sapientia2015.task.service.TaskService;

public class TaskController {
	protected static final String FEEDBACK_MESSAGE_KEY_ADDED = "feedback.message.task.added";
    protected static final String FEEDBACK_MESSAGE_KEY_UPDATED = "feedback.message.task.updated";
    protected static final String FEEDBACK_MESSAGE_KEY_DELETED = "feedback.message.task.deleted";

    protected static final String FLASH_MESSAGE_KEY_ERROR = "errorMessage";
    protected static final String FLASH_MESSAGE_KEY_FEEDBACK = "feedbackMessage";

    protected static final String MODEL_ATTRIBUTE = "story";
    protected static final String MODEL_ATTRIBUTE_TASK = "new_task";
    protected static final String MODEL_ATTRIBUTE_LIST = "tasks";

    protected static final String PARAMETER_ID = "id";

    protected static final String REQUEST_MAPPING_LIST = "/";
    protected static final String REQUEST_MAPPING_VIEW = "/story/{id}";

    protected static final String VIEW_ADD = "story/add";
    protected static final String VIEW_LIST = "story/list";
    protected static final String VIEW_UPDATE = "story/update";
    protected static final String VIEW_VIEW = "story/view";

    @Resource
    private TaskService taskService;
    private StoryService service;

    @Resource
    private MessageSource messageSource;

    @RequestMapping(value = "/story/{storyId}/add_task", method = RequestMethod.POST)
	public String add(@Valid @ModelAttribute(MODEL_ATTRIBUTE_TASK) TaskDTO dto, BindingResult result, RedirectAttributes attributes, @PathVariable("storyId") Long storyId) throws NotFoundException {
		System.out.println(">>> REQUEST (TaskController): "+ "story/"+ storyId +"/add_task" + " POST" + "with @PathVariable: "+ storyId);
		if (result.hasErrors()) {
			return REQUEST_MAPPING_VIEW;
		}
		
		Story updatableStory = service.findById(storyId);
		
		dto.setStory(updatableStory);
		Task added = taskService.add(dto);
		List<Task> tasks = taskService.findAll();
		System.out.println(">>> Rigth after adding length of tasks : "+ tasks.size());
		updatableStory.addTask(added);
		//Story updatedStory = service.update(updatableStory);		
		//Story updatedStory = service.update(updatedStoryDTO);
	
		Story updatedStory = service.findById(storyId);
		List<Task> storyTasks = updatedStory.getTasks();
		System.out.println(">>> Rigth after adding, storyTitle :" + updatedStory.getTitle() +  " length of storyTasks : "+ storyTasks.size());
		addFeedbackMessage(attributes, "feedback.message.task.added", added.getTitle());
		attributes.addAttribute(PARAMETER_ID, updatableStory.getId());
		return createRedirectViewPath(REQUEST_MAPPING_VIEW);
	}
    
	@RequestMapping(value = "/task/delete/{id}", method = RequestMethod.GET)
    public String deleteTaskById(@PathVariable("id") Long id, RedirectAttributes attributes) throws NotFoundException {
		System.out.println(">>> REQUEST (TaskController): "+ "task/delete" + id + " POST");
		Story story = taskService.findById(id).getStory();
		Task deleted = taskService.deleteById(id);
        addFeedbackMessage(attributes, "feedback.message.task.deleted", deleted.getTitle());
        attributes.addAttribute(PARAMETER_ID, story.getId());
        return createRedirectViewPath(REQUEST_MAPPING_VIEW);
    }
	
	@RequestMapping(value = REQUEST_MAPPING_VIEW, method = RequestMethod.GET)
    public String findById(@PathVariable("id") Long id, Model model) throws NotFoundException {
		System.out.println(">>> REQUEST (TaskController): "+ REQUEST_MAPPING_VIEW);
    	System.out.println(">>>Show Story Details with Tasks");
        Story found = service.findById(id);
        List<Task> storyTasks = found.getTasks();
        System.out.println(">>>(story/{storiId} - GET) Length of storyTasks: "+ storyTasks.size());
        model.addAttribute(MODEL_ATTRIBUTE, found);
        model.addAttribute("tasks", storyTasks);
        model.addAttribute(MODEL_ATTRIBUTE_TASK, new TaskDTO());
        return VIEW_VIEW;
    }


    private TaskDTO constructFormObjectForUpdateForm(Task updated) {
        TaskDTO dto = new TaskDTO();

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
