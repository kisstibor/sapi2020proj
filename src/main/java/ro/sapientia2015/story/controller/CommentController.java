package ro.sapientia2015.story.controller;

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

import ro.sapientia2015.story.dto.CommentDTO;
import ro.sapientia2015.story.dto.StoryDTO;
import ro.sapientia2015.story.exception.NotFoundException;
import ro.sapientia2015.story.model.Comment;
import ro.sapientia2015.story.model.Story;
import ro.sapientia2015.story.service.CommentService;
import ro.sapientia2015.story.service.StoryService;

@Controller
@SessionAttributes("comment")
public class CommentController {

	protected static final String FEEDBACK_MESSAGE_KEY_ADDED = "feedback.message.comment.added";
    protected static final String FEEDBACK_MESSAGE_KEY_UPDATED = "feedback.message.comment.updated";
    protected static final String FEEDBACK_MESSAGE_KEY_DELETED = "feedback.message.comment.deleted";
    
    protected static final String FLASH_MESSAGE_KEY_ERROR = "errorMessage";
    protected static final String FLASH_MESSAGE_KEY_FEEDBACK = "feedbackMessage";
    
    protected static final String PARAMETER_ID = "id";
	
	protected static final String MODEL_ATTRIBUTE = "comment";
	
	protected static final String VIEW_ADD = "comment/add";
    protected static final String VIEW_LIST = "comment/list";
    protected static final String VIEW_UPDATE = "comment/update";
    protected static final String VIEW_VIEW = "comment/view";
	
    protected static final String REQUEST_MAPPING_VIEW = "/story/{id}";
	
	@Resource
    private CommentService service;

    @Resource
    private MessageSource messageSource;

    @RequestMapping(value = "/comment/add/{id}", method = RequestMethod.GET)
    public String showAddForm(@PathVariable("id") Long id, Model model) {
        CommentDTO formObject = new CommentDTO();
        formObject.setStoryId(id);
        model.addAttribute(MODEL_ATTRIBUTE, formObject);

        return VIEW_ADD;
    }
    
    @RequestMapping(value = "/comment/add", method = RequestMethod.POST)
    public String add(@Valid @ModelAttribute(MODEL_ATTRIBUTE) CommentDTO dto, BindingResult result, RedirectAttributes attributes) {
        if (result.hasErrors()) {
            return VIEW_ADD+"/"+dto.getStoryId();
        }

        Comment added = service.add(dto);
        addFeedbackMessage(attributes, FEEDBACK_MESSAGE_KEY_ADDED, added.getMessage());
        attributes.addAttribute(PARAMETER_ID, dto.getStoryId());

        return createRedirectViewPath(REQUEST_MAPPING_VIEW);
    }
    
    @RequestMapping(value = "/comment/delete/{id}", method = RequestMethod.GET)
    public String deleteById(@PathVariable("id") Long id, RedirectAttributes attributes) throws NotFoundException {
        Comment deleted = service.deleteById(id);
        addFeedbackMessage(attributes, FEEDBACK_MESSAGE_KEY_DELETED, deleted.getMessage());
        attributes.addAttribute(PARAMETER_ID, deleted.getStory().getId());
        return createRedirectViewPath(REQUEST_MAPPING_VIEW);
    }
    
    @RequestMapping(value = "/comment/update/{id}", method = RequestMethod.GET)
    public String showUpdateForm(@PathVariable("id") Long id, Model model) throws NotFoundException {
        Comment updated = service.findById(id);
        CommentDTO formObject = constructFormObjectForUpdateForm(updated);
        model.addAttribute(MODEL_ATTRIBUTE, formObject);

        return VIEW_UPDATE;
    }
    
    @RequestMapping(value = "/comment/update", method = RequestMethod.POST)
    public String update(@Valid @ModelAttribute(MODEL_ATTRIBUTE) CommentDTO dto, BindingResult result, RedirectAttributes attributes) throws NotFoundException {
        if (result.hasErrors()) {
            return VIEW_UPDATE;
        }

        Comment updated = service.update(dto);
        addFeedbackMessage(attributes, FEEDBACK_MESSAGE_KEY_UPDATED, updated.getMessage());
        attributes.addAttribute(PARAMETER_ID, updated.getStory().getId());

        return createRedirectViewPath(REQUEST_MAPPING_VIEW);
    }

    
    ////////////////////////////////////
    
    private CommentDTO constructFormObjectForUpdateForm(Comment updated) {
    	CommentDTO dto = new CommentDTO();

        dto.setId(updated.getId());
        dto.setMessage(updated.getMessage());
        dto.setStoryId(updated.getStory().getId());

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
