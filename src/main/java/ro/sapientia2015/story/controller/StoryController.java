package ro.sapientia2015.story.controller;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import ro.sapientia2015.story.dto.ReviewDTO;
import ro.sapientia2015.story.dto.StoryDTO;
import ro.sapientia2015.story.exception.NotFoundException;
import ro.sapientia2015.story.model.Review;
import ro.sapientia2015.story.model.Story;
import ro.sapientia2015.story.service.ReviewService;
import ro.sapientia2015.story.service.StoryService;

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
    protected static final String FEEDBACK_MESSAGE_KEY_REVIEWED = "feedback.message.story.reviewed";
    protected static final String FEEDBACK_MESSAGE_KEY_REVIEW_MODIFIED = "feedback.message.story.review.modified";
    protected static final String FEEDBACK_MESSAGE_KEY_REVIEW_REMOVED = "feedback.message.review.removed";
    
    protected static final String ERROR_MESSAGE_KEY_EMPTY_REVIEW = "error.message.story.empty.review";
    protected static final String ERROR_MESSAGE_KEY_INVALID_REVIEW = "error.message.story.invalid.review";
    
    protected static final String FLASH_MESSAGE_KEY_ERROR = "errorMessage";
    protected static final String FLASH_MESSAGE_KEY_FEEDBACK = "feedbackMessage";

    protected static final String MODEL_ATTRIBUTE_REVIEW = "review";
    protected static final String MODEL_ATTRIBUTE = "story";
    protected static final String MODEL_ATTRIBUTE_LIST = "stories";

    protected static final String PARAMETER_ID = "id";
    protected static final String PARAMETER_STORY_ID = "storyId";

    protected static final String REQUEST_MAPPING_LIST = "/";
    protected static final String REQUEST_MAPPING_VIEW = "/story/{id}";
    protected static final String REQUEST_MAPPING_REVIEW = "/story/review/{id}";
    

    protected static final String VIEW_ADD = "story/add";
    protected static final String VIEW_LIST = "story/list";
    protected static final String VIEW_UPDATE = "story/update";
    protected static final String VIEW_REVIEW = "story/review";
    protected static final String VIEW_VIEW = "story/view";

    @Resource
    private StoryService service;

    @Resource 
    private ReviewService reviewService;
    
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
    
    
    @RequestMapping(value = "/story/review/{id}", method = RequestMethod.POST)
    public String addReview(@Valid @ModelAttribute(MODEL_ATTRIBUTE_REVIEW) ReviewDTO dto, @PathVariable("id") Long storyId, BindingResult result, RedirectAttributes attributes) {
    	if (result.hasErrors()) {
            return VIEW_REVIEW;
        }
        
    	dto.setStoryId(storyId);
        if(dto.getReview() == "" ) {
        	addFeedbackErrorMessage(attributes, ERROR_MESSAGE_KEY_EMPTY_REVIEW);
        	attributes.addAttribute(PARAMETER_ID, dto.getId());
        	return createRedirectViewPath(REQUEST_MAPPING_REVIEW);
        }

        try {
        	
        	Review existingReview = reviewService.findReviewByStoryId(storyId);
        	if(existingReview == null) {
        		reviewService.add(dto);
            	addFeedbackMessage(attributes, FEEDBACK_MESSAGE_KEY_REVIEWED);
        	}else {
        		reviewService.update(dto);
        		addFeedbackMessage(attributes, FEEDBACK_MESSAGE_KEY_REVIEW_MODIFIED);
        	}
        }
        catch(Exception ex)
        {
        	addFeedbackErrorMessage(attributes, ERROR_MESSAGE_KEY_INVALID_REVIEW);
        }
        
    	attributes.addAttribute(PARAMETER_ID, storyId);
    	
    	return createRedirectViewPath(REQUEST_MAPPING_VIEW);
    }
    
        
    @RequestMapping(value = "/story/review/remove/{id}", method = RequestMethod.GET)
    public String removeReviewById(@PathVariable("id") Long id, RedirectAttributes attributes) throws NotFoundException {
        Review deleted = reviewService.deleteReviewById(id);
        addFeedbackMessage(attributes, FEEDBACK_MESSAGE_KEY_REVIEW_REMOVED);
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
        Review review = reviewService.findReviewByStoryId(id);
        model.addAttribute(MODEL_ATTRIBUTE, found);
        model.addAttribute(MODEL_ATTRIBUTE_REVIEW, review);
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
    
    @RequestMapping(value = "/story/review/{id}", method = RequestMethod.GET)
    public String showReviewForm(@PathVariable("id") Long id, Model model) throws NotFoundException {
    	Review review = reviewService.findReviewByStoryId(id);
    	
    	ReviewDTO reviewDTO = new ReviewDTO();
    	if (review == null) {
    		reviewDTO.setStoryId(id);
    	}else {
    		reviewDTO = constructFromObjectForReviewUpdateForm(review);
    	}
    	
        model.addAttribute(PARAMETER_STORY_ID, reviewDTO.getStoryId());
        model.addAttribute(MODEL_ATTRIBUTE_REVIEW, reviewDTO);
 
        return VIEW_REVIEW;
    }

    private StoryDTO constructFormObjectForUpdateForm(Story updated) {
        StoryDTO dto = new StoryDTO();

        dto.setId(updated.getId());
        dto.setDescription(updated.getDescription());
        dto.setTitle(updated.getTitle());
        
        return dto;
    }
    
    private ReviewDTO constructFromObjectForReviewUpdateForm(Review review) {
    	ReviewDTO dto = new ReviewDTO();
    	
    	dto.setId(review.getId());
    	dto.setStoryId(review.getStoryId());
    	dto.setReview(review.getReview());
    	
    	return dto;
    }

    private void addFeedbackMessage(RedirectAttributes attributes, String messageCode, Object... messageParameters) {
        String localizedFeedbackMessage = getMessage(messageCode, messageParameters);
        attributes.addFlashAttribute(FLASH_MESSAGE_KEY_FEEDBACK, localizedFeedbackMessage);
    }
    
    private void addFeedbackErrorMessage(RedirectAttributes attributes, String messageCode, Object... messageParameters) {
        String localizedFeedbackMessage = getMessage(messageCode, messageParameters);
        attributes.addFlashAttribute(FLASH_MESSAGE_KEY_ERROR, localizedFeedbackMessage);
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
