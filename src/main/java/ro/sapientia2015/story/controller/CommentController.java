package ro.sapientia2015.story.controller;

import javax.annotation.Resource;
import javax.validation.Valid;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import ro.sapientia2015.story.dto.CommentDTO;
import ro.sapientia2015.story.exception.NotFoundException;
import ro.sapientia2015.story.model.Comment;
import ro.sapientia2015.story.model.Story;
import ro.sapientia2015.story.service.CommentService;
import ro.sapientia2015.story.service.StoryService;

@Controller
@SessionAttributes("comment")
public class CommentController {

	protected static final String MODEL_ATTRIBUTE = "comment";
	protected static final String MODEL_ATTRIBUTE_LIST = "comments";

	protected static final String STORY_ID = "storyId";

	protected static final String REQUEST_MAPPING_STORY_VIEW = "/story/{storyId}";

	protected static final String VIEW_ADD = "comment/add";

	@Resource
	private CommentService service;

	@Resource
	private StoryService storyService;

	@Resource
	private MessageSource messageSource;

	@RequestMapping(value = "/story/{storyId}/comment/add", method = RequestMethod.GET)
	public String showAddForm(@PathVariable("storyId") Long storyId, Model model) {
		CommentDTO formObject = new CommentDTO();
		formObject.setStoryId(storyId);

		model.addAttribute(MODEL_ATTRIBUTE, formObject);
		model.addAttribute(STORY_ID, storyId);

		return VIEW_ADD;
	}

	@RequestMapping(value = "/story/{storyId}/comment/add", method = RequestMethod.POST)
	public String add(@PathVariable("storyId") Long storyId, @Valid @ModelAttribute(MODEL_ATTRIBUTE) CommentDTO dto,
			BindingResult result, RedirectAttributes attributes) throws NotFoundException {
		if (result.hasErrors()) {
			return VIEW_ADD;
		}

		Comment added = service.add(dto);

		attributes.addAttribute(STORY_ID, storyId);

		return createRedirectViewPath(REQUEST_MAPPING_STORY_VIEW);

	}

	@RequestMapping(value = "/story/{storyId}/comment/delete/{id}", method = RequestMethod.GET)
	public String deleteById(@PathVariable("storyId") Long storyId, @PathVariable("id") Long id,
			RedirectAttributes attributes) throws NotFoundException {
		Comment deleted = service.deleteById(id);

		attributes.addAttribute(STORY_ID, storyId);

		return createRedirectViewPath(REQUEST_MAPPING_STORY_VIEW);
	}

	private String createRedirectViewPath(String requestMapping) {
		StringBuilder redirectViewPath = new StringBuilder();
		redirectViewPath.append("redirect:");
		redirectViewPath.append(requestMapping);
		return redirectViewPath.toString();
	}

}
