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

import ro.sapientia2015.story.dto.BugDTO;
import ro.sapientia2015.story.exception.NotFoundException;
import ro.sapientia2015.story.model.Bug;
import ro.sapientia2015.story.service.BugService;

/**
 * @author Zsok Andrei
 */
@Controller
@RequestMapping("/bug")
@SessionAttributes("bug")
public class BugController {
	
	private static final String MODEL_ATTRIBUTE = "bug";
	private static final String PATH_LIST = "bug/list";
	private static final String PATH_VIEW = "bug/view";
	private static final String PATH_ADD = "bug/add";
	private static final String PATH_UPDATE = "bug/update";
	
	@Resource
	private BugService bugService;
	
	@Resource
    private MessageSource messageSource;
	
	@RequestMapping(value = "/list", method = RequestMethod.GET)
    public String listBugs(Model model) {
		List<Bug> models = bugService.findAll();
        model.addAttribute("bugs", models);

        return PATH_LIST;
    }
	
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public String viewBug(@PathVariable("id") Long id, Model model) throws NotFoundException {
        Bug found = bugService.findById(id);
        model.addAttribute(MODEL_ATTRIBUTE, found);
        return PATH_VIEW;
    }
	
	@RequestMapping(value = "/add", method = RequestMethod.GET)
    public String showAddForm(Model model) {
        BugDTO formObject = new BugDTO();
        model.addAttribute(MODEL_ATTRIBUTE, formObject);

        return PATH_ADD;
    }
	
	@RequestMapping(value = "/add", method = RequestMethod.POST)
    public String addBug(@Valid @ModelAttribute(MODEL_ATTRIBUTE) BugDTO dto, BindingResult result, RedirectAttributes attributes) {
        if (result.hasErrors()) {
            return PATH_ADD;
        }

        Bug bug = bugService.add(dto);
        addFeedbackMessage(attributes, "Bug added", bug.getTitle());
        attributes.addAttribute("id", bug.getId());

        return createRedirectViewPath("list");
    }
	
	@RequestMapping(value = "/update/{id}", method = RequestMethod.GET)
    public String showUpdateForm(@PathVariable("id") Long id, Model model) throws NotFoundException {
        Bug updated = bugService.findById(id);
        BugDTO formObject = constructFormObjectForUpdateForm(updated);
        model.addAttribute(MODEL_ATTRIBUTE, formObject);

        return PATH_UPDATE;
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public String update(@Valid @ModelAttribute(MODEL_ATTRIBUTE) BugDTO dto, BindingResult result, RedirectAttributes attributes) throws NotFoundException {
        if (result.hasErrors()) {
            return PATH_UPDATE;
        }

        Bug bug = bugService.update(dto);
        addFeedbackMessage(attributes, "Bug updated", bug.getTitle());
        attributes.addAttribute("id", bug.getId());

        return createRedirectViewPath("{id}");
    }
    
    private BugDTO constructFormObjectForUpdateForm(Bug bug) {
        BugDTO dto = new BugDTO();

        dto.setId(bug.getId());
        dto.setDescription(bug.getDescription());
        dto.setTitle(bug.getTitle());

        return dto;
    }
	
	@RequestMapping(value = "/bug/delete/{id}", method = RequestMethod.GET)
    public String deleteById(@PathVariable("id") Long id, RedirectAttributes attributes) throws NotFoundException {
        Bug bug = bugService.delete(id);
        addFeedbackMessage(attributes, "Bug deleted", bug.getTitle());
        return createRedirectViewPath("list");
    }
	
	private void addFeedbackMessage(RedirectAttributes attributes, String messageCode, Object... messageParameters) {
        String localizedFeedbackMessage = getMessage(messageCode, messageParameters);
        attributes.addFlashAttribute("bug.added", localizedFeedbackMessage);
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
