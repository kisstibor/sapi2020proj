package ro.sapientia2015.story.controller;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import ro.sapientia2015.story.dto.StoryDTO;
import ro.sapientia2015.story.dto.StoryTimeLimitDTO;
import ro.sapientia2015.story.exception.NotFoundException;
import ro.sapientia2015.story.model.Story;
import ro.sapientia2015.story.model.StoryTimeLimit;
import ro.sapientia2015.story.service.StoryService;
import ro.sapientia2015.story.service.StoryTimeLimitService;

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
    protected static final String FEEDBACK_MESSAGE_KEY_ADDED_TIMELIMIT = "feedback.message.storytimelimit.added";
    protected static final String FEEDBACK_MESSAGE_KEY_DELETED_TIMELIMIT = "feedback.message.storytimelimit.deleted";
    protected static final String FEEDBACK_MESSAGE_KEY_UPDATED_TIMELIMIT = "feedback.message.storytimelimit.updated";

    protected static final String FLASH_MESSAGE_KEY_ERROR = "errorMessage";
    protected static final String FLASH_MESSAGE_KEY_FEEDBACK = "feedbackMessage";

    protected static final String MODEL_ATTRIBUTE = "story";
    protected static final String MODEL_ATTRIBUTE_LIST = "stories";
    protected static final String MODEL_ATTRIBUTE_STORYTIMELIMIT = "storytimelimit";
   

    protected static final String PARAMETER_ID = "id";

    protected static final String REQUEST_MAPPING_LIST = "/";
    protected static final String REQUEST_MAPPING_VIEW = "/story/{id}";

    protected static final String VIEW_ADD = "story/add";
    protected static final String VIEW_LIST = "story/list";
    protected static final String VIEW_UPDATE = "story/update";
    protected static final String VIEW_VIEW = "story/view";
    protected static final String VIEW_TIMELIMIT_ADD = "story/timelimit/add";
    protected static final String VIEW_UPDATE_TIMELIMIT = "story/timelimit/update";

    @Resource
    private StoryService service;
    
    @Resource
    private StoryTimeLimitService storytimelimitService;

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
        StoryTimeLimit timelimit = storytimelimitService.findByStoryId(id);
        model.addAttribute(MODEL_ATTRIBUTE, found);
        model.addAttribute(MODEL_ATTRIBUTE_STORYTIMELIMIT, timelimit);
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
    
    @RequestMapping(value = "/story/timelimit/add/{storyId}", method = RequestMethod.GET)
    public String showAddTimelimitForm(@PathVariable("storyId") Long storyId, Model model)  throws NotFoundException {
        StoryTimeLimitDTO formObject = new StoryTimeLimitDTO();
        formObject.setStoryId(storyId);
        model.addAttribute(MODEL_ATTRIBUTE_STORYTIMELIMIT, formObject);
        
        Story found = service.findById(storyId);
        model.addAttribute(MODEL_ATTRIBUTE, found);

        return VIEW_TIMELIMIT_ADD;
    }
    
    @RequestMapping(value = "/story/timelimit/add/{storyId}", method = RequestMethod.POST)
    public String addTimelimit(@Valid @ModelAttribute(MODEL_ATTRIBUTE_STORYTIMELIMIT) StoryTimeLimitDTO dto, @PathVariable("storyId") Long storyId,BindingResult result, RedirectAttributes attributes) {
        if (result.hasErrors()) {
            return VIEW_TIMELIMIT_ADD;
        }
        dto.setStoryId(storyId);
        StoryTimeLimit added = storytimelimitService.add(dto);
        addFeedbackMessage(attributes, FEEDBACK_MESSAGE_KEY_ADDED_TIMELIMIT, added.getTimelimit());
    	attributes.addAttribute(PARAMETER_ID, added.getStoryId());

        return createRedirectViewPath(REQUEST_MAPPING_VIEW);
    }
    
    @RequestMapping(value = "/story/timelimit/delete/{storyId}/{id}", method = RequestMethod.GET)
    public String deleteTimelimitById(@PathVariable("id") Long id, @PathVariable("storyId") Long storyId, RedirectAttributes attributes) throws NotFoundException {
        StoryTimeLimit deleted = storytimelimitService.deleteById(id);
        addFeedbackMessage(attributes, FEEDBACK_MESSAGE_KEY_DELETED_TIMELIMIT, deleted.getTimelimit());
        attributes.addAttribute(PARAMETER_ID, storyId);
        return createRedirectViewPath(REQUEST_MAPPING_VIEW);
    }
    
    @RequestMapping(value = "/story/timelimit/update/{storyId}/{id}", method = RequestMethod.GET)
    public String showUpdateTimelimitForm(@PathVariable("id") Long id, @PathVariable("storyId") Long storyId, Model model) throws NotFoundException {
        StoryTimeLimit updated = storytimelimitService.findById(id);
        StoryTimeLimitDTO formObject = constructFormObjectForUpdateTimelimit(updated);
        model.addAttribute(MODEL_ATTRIBUTE_STORYTIMELIMIT, formObject);
        
        Story found = service.findById(storyId);
        model.addAttribute(MODEL_ATTRIBUTE, found);

        return VIEW_UPDATE_TIMELIMIT;
    }

    @RequestMapping(value = "/story/timelimit/update/{storyId}/{id}", method = RequestMethod.POST)
    public String updateTimelimit(@Valid @ModelAttribute(MODEL_ATTRIBUTE_STORYTIMELIMIT) StoryTimeLimitDTO dto,@PathVariable("id") Long id, @PathVariable("storyId") Long storyId, BindingResult result, RedirectAttributes attributes) throws NotFoundException {
        if (result.hasErrors()) {
            return VIEW_UPDATE_TIMELIMIT;
        }

        StoryTimeLimit updated = storytimelimitService.update(dto);
        addFeedbackMessage(attributes, FEEDBACK_MESSAGE_KEY_UPDATED_TIMELIMIT, updated.getTimelimit());
        attributes.addAttribute(PARAMETER_ID, storyId);

        return createRedirectViewPath(REQUEST_MAPPING_VIEW);
    }

    private StoryDTO constructFormObjectForUpdateForm(Story updated) {
        StoryDTO dto = new StoryDTO();

        dto.setId(updated.getId());
        dto.setDescription(updated.getDescription());
        dto.setTitle(updated.getTitle());

        return dto;
    }
    
    private StoryTimeLimitDTO constructFormObjectForUpdateTimelimit(StoryTimeLimit updated) {
        StoryTimeLimitDTO dto = new StoryTimeLimitDTO();

        dto.setId(updated.getId());
        dto.setStoryId(updated.getStoryId());
        dto.setTimelimit(updated.getTimelimit());

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
