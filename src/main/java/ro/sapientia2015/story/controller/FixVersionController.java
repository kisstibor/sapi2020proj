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

import ro.sapientia2015.story.dto.FixVersionDTO;
import ro.sapientia2015.story.dto.StoryDTO;
import ro.sapientia2015.story.exception.NotFoundException;
import ro.sapientia2015.story.model.FixVersion;
import ro.sapientia2015.story.model.Story;
import ro.sapientia2015.story.service.FixVersionService;
import ro.sapientia2015.story.service.StoryService;

@Controller
@SessionAttributes("fixVersion")
public class FixVersionController {

	protected static final String FEEDBACK_MESSAGE_KEY_ADDED = "feedback.message.fixVersion.added";
    protected static final String FEEDBACK_MESSAGE_KEY_UPDATED = "feedback.message.fixVersion.updated";
    protected static final String FEEDBACK_MESSAGE_KEY_DELETED = "feedback.message.fixVersion.deleted";

    protected static final String FLASH_MESSAGE_KEY_ERROR = "errorMessage";
    protected static final String FLASH_MESSAGE_KEY_FEEDBACK = "feedbackMessage";

    protected static final String MODEL_ATTRIBUTE = "fixVersion";
    protected static final String MODEL_ATTRIBUTE_LIST = "fixVersions";

    protected static final String PARAMETER_ID = "id";

    protected static final String REQUEST_MAPPING_LIST = "/fixVersions";
    protected static final String REQUEST_MAPPING_VIEW = "/fixVersion/{id}";

    protected static final String VIEW_ADD = "fixVersion/add";
    protected static final String VIEW_LIST = "fixVersion/list";
    protected static final String VIEW_UPDATE = "fixVersion/update";
    protected static final String VIEW_VIEW = "fixVersion/view";
    
    @Resource
    private FixVersionService service;

    @Resource
    private MessageSource messageSource;
    
    @RequestMapping(value = "/fixVersion/add", method = RequestMethod.GET)
    public String showAddForm(Model model) {
        FixVersionDTO formObject = new FixVersionDTO();
        model.addAttribute(MODEL_ATTRIBUTE, formObject);

        return VIEW_ADD;
    }
    
    @RequestMapping(value = "/fixVersion/add", method = RequestMethod.POST)
    public String add(@Valid @ModelAttribute(MODEL_ATTRIBUTE) FixVersionDTO dto, BindingResult result, RedirectAttributes attributes) {
        if (result.hasErrors()) {
            return VIEW_ADD;
        }

        FixVersion added = service.add(dto);
        addFeedbackMessage(attributes, FEEDBACK_MESSAGE_KEY_ADDED, added.getName());
        attributes.addAttribute(PARAMETER_ID, added.getId());

        return createRedirectViewPath(REQUEST_MAPPING_VIEW);
    }
    
    @RequestMapping(value = "/fixVersion/delete/{id}", method = RequestMethod.GET)
    public String deleteById(@PathVariable("id") Long id, RedirectAttributes attributes) throws NotFoundException {
        FixVersion deleted = service.deleteById(id);
        addFeedbackMessage(attributes, FEEDBACK_MESSAGE_KEY_DELETED, deleted.getName());
        return createRedirectViewPath(REQUEST_MAPPING_LIST);
    }
    
    @RequestMapping(value = REQUEST_MAPPING_LIST, method = RequestMethod.GET)
    public String findAll(Model model) {
        List<FixVersion> models = service.findAll();
        model.addAttribute(MODEL_ATTRIBUTE_LIST, models);
        return VIEW_LIST;
    }
    
    @RequestMapping(value = REQUEST_MAPPING_VIEW, method = RequestMethod.GET)
    public String findById(@PathVariable("id") Long id, Model model) throws NotFoundException {
        FixVersion found = service.findById(id);
        model.addAttribute(MODEL_ATTRIBUTE, found);
        return VIEW_VIEW;
    }
    
    @RequestMapping(value = "/fixVersion/update/{id}", method = RequestMethod.GET)
    public String showUpdateForm(@PathVariable("id") Long id, Model model) throws NotFoundException {
        FixVersion updated = service.findById(id);
        FixVersionDTO formObject = constructFormObjectForUpdateForm(updated);
        model.addAttribute(MODEL_ATTRIBUTE, formObject);

        return VIEW_UPDATE;
    }
    
    @RequestMapping(value = "/fixVersion/update", method = RequestMethod.POST)
    public String update(@Valid @ModelAttribute(MODEL_ATTRIBUTE) FixVersionDTO dto, BindingResult result, RedirectAttributes attributes) throws NotFoundException {
        if (result.hasErrors()) {
            return VIEW_UPDATE;
        }

        FixVersion updated = service.update(dto);
        addFeedbackMessage(attributes, FEEDBACK_MESSAGE_KEY_UPDATED, updated.getName());
        attributes.addAttribute(PARAMETER_ID, updated.getId());

        return createRedirectViewPath(REQUEST_MAPPING_VIEW);
    }
    
    private FixVersionDTO constructFormObjectForUpdateForm(FixVersion updated) {
    	FixVersionDTO dto = new FixVersionDTO();

        dto.setId(updated.getId());
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
