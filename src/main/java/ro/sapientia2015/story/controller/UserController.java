package ro.sapientia2015.story.controller;

import javax.annotation.Resource;
import javax.validation.Valid;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import ro.sapientia2015.story.dto.UserDTO;
import ro.sapientia2015.story.model.User;
import ro.sapientia2015.story.service.UserService;

@Controller
@SessionAttributes("user")
public class UserController extends ControllerBase {
	
	protected static final String FEEDBACK_MESSAGE_KEY_ADDED = "feedback.message.user.added";
	
	protected static final String FLASH_MESSAGE_KEY_FEEDBACK = "feedbackMessage";
	
	protected static final String MODEL_ATTRIBUTE = "user";
	
	protected static final String VIEW_ADD = "user/add";
	
	protected static final String REQUEST_MAPPING_VIEW = "/story/list";
	
	@Resource
    private UserService service;

	@RequestMapping(value = "/user/add", method = RequestMethod.GET)
	public String showAddForm(Model model) {
		UserDTO formObject = new UserDTO();
		model.addAttribute(MODEL_ATTRIBUTE, formObject);
		
		return VIEW_ADD;
	}
	
	@RequestMapping(value = "/user/add", method = RequestMethod.POST)
    public String add(@Valid @ModelAttribute(MODEL_ATTRIBUTE) UserDTO dto, BindingResult result, RedirectAttributes attributes) {
		if (result.hasErrors()) {
			return VIEW_ADD;
		}
		
		User added = service.add(dto);
		addFeedbackMessage(attributes, FEEDBACK_MESSAGE_KEY_ADDED, added.getUsername());
		
		return createRedirectViewPath(REQUEST_MAPPING_VIEW);
	}
	
}
