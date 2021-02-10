package ro.sapientia2015.story.controller;

import java.util.ArrayList;
import java.util.Arrays;
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

import ro.sapientia2015.story.dto.BugDTO;
import ro.sapientia2015.story.exception.NotFoundException;
import ro.sapientia2015.story.model.Bug;
import ro.sapientia2015.story.service.BugService;

@Controller
@SessionAttributes("bug")
public class BugController {

    protected static final String FEEDBACK_MESSAGE_KEY_ADDED = "feedback.message.bug.added";
    protected static final String FEEDBACK_MESSAGE_KEY_UPDATED = "feedback.message.bug.updated";
    protected static final String FEEDBACK_MESSAGE_KEY_DELETED = "feedback.message.bug.deleted";

    protected static final String FLASH_MESSAGE_KEY_ERROR = "errorMessage";
    protected static final String FLASH_MESSAGE_KEY_FEEDBACK = "feedbackMessage";

    protected static final String MODEL_ATTRIBUTE = "bug";
    protected static final String MODEL_ATTRIBUTE_LIST = "bugs";

    protected static final String PARAMETER_ID = "id";

    protected static final String REQUEST_MAPPING_LIST = "/bug";
    protected static final String REQUEST_MAPPING_VIEW = "/bug/{id}";

    protected static final String VIEW_ADD = "/bug/add";
    protected static final String VIEW_LIST = "/bug/list";
    protected static final String VIEW_UPDATE = "/bug/update";
    protected static final String VIEW_VIEW = "/bug/view";

    @Resource
    private BugService service;

    @Resource
    private MessageSource messageSource;

    @RequestMapping(value = "/bug/add", method = RequestMethod.GET)
    public String showAddForm(Model model) {
        BugDTO formObject = new BugDTO();
        model.addAttribute(MODEL_ATTRIBUTE, formObject);

        return VIEW_ADD;
    }

    @RequestMapping(value = "/bug/add", method = RequestMethod.POST)
    public String add(@Valid @ModelAttribute(MODEL_ATTRIBUTE) BugDTO dto, BindingResult result, RedirectAttributes attributes) {
        if (result.hasErrors()) {
            return VIEW_ADD;
        }

        Bug added = service.add(dto);
        addFeedbackMessage(attributes, FEEDBACK_MESSAGE_KEY_ADDED, added.getTitle());
        attributes.addAttribute(PARAMETER_ID, added.getId());

        return createRedirectViewPath(REQUEST_MAPPING_LIST);
    }

    @RequestMapping(value = "/bug/delete/{id}", method = RequestMethod.GET)
    public String deleteById(@PathVariable("id") Long id, RedirectAttributes attributes) throws NotFoundException {
        Bug deleted = service.deleteById(id);
        addFeedbackMessage(attributes, FEEDBACK_MESSAGE_KEY_DELETED, deleted.getTitle());
        return createRedirectViewPath(REQUEST_MAPPING_LIST);
    }

    @RequestMapping(value = REQUEST_MAPPING_LIST, method = RequestMethod.GET)
    public String findAll(Model model) {
        List<Bug> models = service.findAll();
        model.addAttribute(MODEL_ATTRIBUTE_LIST, models);
        return VIEW_LIST;
    }

    @RequestMapping(value = REQUEST_MAPPING_VIEW, method = RequestMethod.GET)
    public String findById(@PathVariable("id") Long id, Model model) throws NotFoundException {
    	Bug found = service.findById(id);
        model.addAttribute(MODEL_ATTRIBUTE, found);
        return VIEW_VIEW;
    }

    @RequestMapping(value = "/bug/update/{id}", method = RequestMethod.GET)
    public String showUpdateForm(@PathVariable("id") Long id, Model model) throws NotFoundException {
    	Bug updated = service.findById(id);
    	BugDTO formObject = constructFormObjectForUpdateForm(updated);
        model.addAttribute(MODEL_ATTRIBUTE, formObject);

        return VIEW_UPDATE;
    }

    @RequestMapping(value = "/bug/update", method = RequestMethod.POST)
    public String update(@Valid @ModelAttribute(MODEL_ATTRIBUTE) BugDTO dto, BindingResult result, RedirectAttributes attributes) throws NotFoundException {
        if (result.hasErrors()) {
            return VIEW_UPDATE;
        }

        Bug updated = service.update(dto);
        addFeedbackMessage(attributes, FEEDBACK_MESSAGE_KEY_UPDATED, updated.getTitle());
        attributes.addAttribute(PARAMETER_ID, updated.getId());

        return createRedirectViewPath(REQUEST_MAPPING_VIEW);
    }

    private BugDTO constructFormObjectForUpdateForm(Bug updated) {
    	BugDTO dto = new BugDTO();

        dto.setId(updated.getId());
        dto.setTitle(updated.getTitle());
        dto.setDescription(updated.getDescription());
        dto.setStatus(updated.getStatus());

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