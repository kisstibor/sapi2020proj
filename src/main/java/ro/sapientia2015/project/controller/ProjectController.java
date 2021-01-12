package ro.sapientia2015.project.controller;

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

import ro.sapientia2015.project.dto.ProjectDTO;
import ro.sapientia2015.story.exception.NotFoundException;
import ro.sapientia2015.project.model.Project;
import ro.sapientia2015.project.service.ProjectService;

@Controller
@SessionAttributes("project")
public class ProjectController {

	protected static final String FEEDBACK_MESSAGE_KEY_ADDED = "feedback.message.project.added";
	protected static final String FEEDBACK_MESSAGE_KEY_UPDATED = "feedback.message.project.updated";
	protected static final String FEEDBACK_MESSAGE_KEY_DELETED = "feedback.message.project.deleted";

	protected static final String FLASH_MESSAGE_KEY_ERROR = "errorMessage";
	protected static final String FLASH_MESSAGE_KEY_FEEDBACK = "feedbackMessage";

	protected static final String MODEL_ATTRIBUTE = "project";
	protected static final String MODEL_ATTRIBUTE_LIST = "projects";

	protected static final String PARAMETER_ID = "id";

	protected static final String REQUEST_MAPPING_LIST = "/projects";
	protected static final String REQUEST_MAPPING_VIEW = "/project/{id}";

	protected static final String VIEW_ADD = "project/add";
	protected static final String VIEW_LIST = "project/list";
	protected static final String VIEW_UPDATE = "project/update";
	protected static final String VIEW_VIEW = "project/view";

	@Resource
	private ProjectService service;

	@Resource
	private MessageSource messageSource;

	@RequestMapping(value = "/project/add", method = RequestMethod.GET)
	public String showAddForm(Model model) {
		ProjectDTO formObject = new ProjectDTO();
		model.addAttribute(MODEL_ATTRIBUTE, formObject);
		return VIEW_ADD;
	}

	@RequestMapping(value = "/project/add", method = RequestMethod.POST)
	public String add(@Valid @ModelAttribute(MODEL_ATTRIBUTE) ProjectDTO dto, BindingResult result,
			RedirectAttributes attributes) {
		if (result.hasErrors()) {
			return VIEW_ADD;
		}

		Project added = service.add(dto);
		addFeedbackMessage(attributes, FEEDBACK_MESSAGE_KEY_ADDED, added.getName());
		attributes.addAttribute(PARAMETER_ID, added.getId());

		return createRedirectViewPath(REQUEST_MAPPING_VIEW);
	}

	@RequestMapping(value = "/project/delete/{id}", method = RequestMethod.GET)
	public String deleteById(@PathVariable("id") Long id, RedirectAttributes attributes) throws NotFoundException {
		Project deleted = service.deleteById(id);
		addFeedbackMessage(attributes, FEEDBACK_MESSAGE_KEY_DELETED, deleted.getName());
		return createRedirectViewPath(REQUEST_MAPPING_LIST);
	}

	@RequestMapping(value = REQUEST_MAPPING_LIST, method = RequestMethod.GET)
	public String findAll(Model model) {
		List<Project> models = service.findAll();
		model.addAttribute(MODEL_ATTRIBUTE_LIST, models);
		return VIEW_LIST;
	}

	@RequestMapping(value = REQUEST_MAPPING_VIEW, method = RequestMethod.GET)
	public String findById(@PathVariable("id") Long id, Model model) throws NotFoundException {
		Project found = service.findById(id);
		model.addAttribute(MODEL_ATTRIBUTE, found);
		return VIEW_VIEW;
	}

	@RequestMapping(value = "/project/update/{id}", method = RequestMethod.GET)
	public String showUpdateForm(@PathVariable("id") Long id, Model model) throws NotFoundException {
		Project updated = service.findById(id);
		ProjectDTO formObject = constructFormObjectForUpdateForm(updated);
		model.addAttribute(MODEL_ATTRIBUTE, formObject);

		return VIEW_UPDATE;
	}

	@RequestMapping(value = "/project/update", method = RequestMethod.POST)
	public String update(@Valid @ModelAttribute(MODEL_ATTRIBUTE) ProjectDTO dto, BindingResult result,
			RedirectAttributes attributes) throws NotFoundException {
		if (result.hasErrors()) {
			return VIEW_UPDATE;
		}

		Project updated = service.update(dto);
		addFeedbackMessage(attributes, FEEDBACK_MESSAGE_KEY_UPDATED, updated.getName());
		attributes.addAttribute(PARAMETER_ID, updated.getId());

		return createRedirectViewPath(REQUEST_MAPPING_VIEW);
	}

	private ProjectDTO constructFormObjectForUpdateForm(Project updated) {
		ProjectDTO dto = new ProjectDTO();
		dto.setId(updated.getId());
		dto.setName(updated.getName());
		dto.setProductOwner(updated.getProductOwner());
		dto.setScrumMaster(updated.getScrumMaster());
		dto.setMembers(updated.getMembers());
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
