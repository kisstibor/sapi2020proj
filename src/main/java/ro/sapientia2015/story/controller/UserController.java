package ro.sapientia2015.story.controller;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import ro.sapientia2015.story.dto.UserDTO;
import ro.sapientia2015.story.exception.NotFoundException;
import ro.sapientia2015.story.model.User;
import ro.sapientia2015.story.service.UserService;

import javax.annotation.Resource;
import javax.validation.Valid;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

/**
 * @author Kiss Tibor
 */
@Controller
@SessionAttributes("user")
public class UserController {

    protected static final String FEEDBACK_MESSAGE_KEY_ADDED = "feedback.message.user.added";
    protected static final String FEEDBACK_MESSAGE_KEY_UPDATED = "feedback.message.user.updated";
    protected static final String FEEDBACK_MESSAGE_KEY_DELETED = "feedback.message.user.deleted";

    protected static final String FLASH_MESSAGE_KEY_ERROR = "errorMessage";
    protected static final String FLASH_MESSAGE_KEY_FEEDBACK = "feedbackMessage";

    protected static final String MODEL_ATTRIBUTE = "user";
    protected static final String MODEL_ATTRIBUTE_LIST = "users";

    protected static final String PARAMETER_ID = "id";

    protected static final String REQUEST_MAPPING_LIST = "/user";
    protected static final String REQUEST_MAPPING_VIEW = "/user/{id}";

    protected static final String VIEW_ADD = "user/add";
    protected static final String VIEW_LIST = "user/list";
    protected static final String VIEW_UPDATE = "user/update";
    protected static final String VIEW_VIEW = "user/view";

    @Resource
    private UserService service;

    @Resource
    private MessageSource messageSource;

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
        addFeedbackMessage(attributes, FEEDBACK_MESSAGE_KEY_ADDED, added.getName());
        attributes.addAttribute(PARAMETER_ID, added.getId());

        return createRedirectViewPath(REQUEST_MAPPING_VIEW);
    }

    @RequestMapping(value = "/user/delete/{id}", method = RequestMethod.GET)
    public String deleteById(@PathVariable("id") Long id, RedirectAttributes attributes) throws NotFoundException {
        User deleted = service.deleteById(id);
        addFeedbackMessage(attributes, FEEDBACK_MESSAGE_KEY_DELETED, deleted.getName());
        return createRedirectViewPath(REQUEST_MAPPING_LIST);
    }

    @RequestMapping(value = REQUEST_MAPPING_LIST, method = RequestMethod.GET)
    public String findAll(Model model) {
        List<User> models = service.findAll();
        model.addAttribute(MODEL_ATTRIBUTE_LIST, models);
        
        return VIEW_LIST;
    }
    
    @RequestMapping(value = "user/sort", method = RequestMethod.POST)
    public String findAllSort(Model model, @RequestParam int sort) {
        List<User> models = service.findAll();
        model.addAttribute(MODEL_ATTRIBUTE_LIST, models);
        
        switch(sort) {
        	case 1: 
        		Collections.sort(models, new Comparator<User>() {
        			  @Override
        			  public int compare(User u1, User u2) {
        			    return u1.getName().compareTo(u2.getName());
        			  }
        			});
        		break;
        	case 2: 
        		Collections.sort(models, new Comparator<User>() {
      			  @Override
      			  public int compare(User u1, User u2) {
      			    return u2.getName().compareTo(u1.getName());
      			  }
      			});
        		break;
        	case 3:
        		Collections.sort(models, new Comparator<User>() {
      			  @Override
      			  public int compare(User u1, User u2) {
      			    return u1.getType().compareTo(u2.getType());
      			  }
      			});
        		break;
        	case 4:
        		Collections.sort(models, new Comparator<User>() {
      			  @Override
      			  public int compare(User u1, User u2) {
      			    return u2.getType().compareTo(u1.getType());
      			  }
      			});
        		break;
        		
        }
        
        return VIEW_LIST;
    }

    @RequestMapping(value = REQUEST_MAPPING_VIEW, method = RequestMethod.GET)
    public String findById(@PathVariable("id") Long id, Model model) throws NotFoundException {
        User found = service.findById(id);
        model.addAttribute(MODEL_ATTRIBUTE, found);
        return VIEW_VIEW;
    }

    @RequestMapping(value = "/user/update/{id}", method = RequestMethod.GET)
    public String showUpdateForm(@PathVariable("id") Long id, Model model) throws NotFoundException {
        User updated = service.findById(id);
        UserDTO formObject = constructFormObjectForUpdateForm(updated);
        model.addAttribute(MODEL_ATTRIBUTE, formObject);

        return VIEW_UPDATE;
    }

    @RequestMapping(value = "/user/update", method = RequestMethod.POST)
    public String update(@Valid @ModelAttribute(MODEL_ATTRIBUTE) UserDTO dto, BindingResult result, RedirectAttributes attributes) throws NotFoundException {
        if (result.hasErrors()) {
            return VIEW_UPDATE;
        }

        User updated = service.update(dto);
        addFeedbackMessage(attributes, FEEDBACK_MESSAGE_KEY_UPDATED, updated.getName());
        attributes.addAttribute(PARAMETER_ID, updated.getId());

        return createRedirectViewPath(REQUEST_MAPPING_VIEW);
    }

    private UserDTO constructFormObjectForUpdateForm(User updated) {
        UserDTO dto = new UserDTO();

        dto.setId(updated.getId());
        dto.setType(updated.getType());
        dto.setName(updated.getName());

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
