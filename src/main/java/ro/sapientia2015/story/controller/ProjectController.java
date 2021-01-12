/**
 * 
 */
package ro.sapientia2015.story.controller;

import java.util.List;

import javax.annotation.Resource;
import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import ro.sapientia2015.story.exception.NotFoundException;

import ro.sapientia2015.project.dto.EpicDTO;
import ro.sapientia2015.project.dto.ProjectDTO;
import ro.sapientia2015.project.model.Project;
import ro.sapientia2015.project.service.ProjectService;

@Controller
public class ProjectController {

	@Resource
	private ProjectService service;

//	@Autowired
//	public void setService(SprintService service) {
//		this.service = service;
//	}

	public static final String VIEW_LIST = "project/list";
	public static final String VIEW_ADD = "project/add";
	private static final String VIEW_SINGLE_PROJECT = "project/single_project";

	static final String MODEL_ATTRIBUTE = "project";

    protected static final String MODEL_ATTRIBUTE_LIST = "projects";

    protected static final String PARAMETER_ID = "id";

    protected static final String REQUEST_MAPPING_LIST = "/";
    protected static final String REQUEST_MAPPING_VIEW = "/project/list";
    
    protected static final String VIEW_VIEW = "project/view";
    

	@RequestMapping(value = "/project/list", method = RequestMethod.GET)
	public String listProject(Model model) {
		System.out.println("OK");
		List<Project> projects = service.findAll();
		model.addAttribute("projects", projects);
		return VIEW_LIST;
	}

	@RequestMapping(value = "/project/{id}", method = RequestMethod.GET)
	public String singleProject(@PathVariable Long id, Model model) {
		System.out.println("OK");
		try {
			Project project = service.findById(id);
			model.addAttribute("project", project);
		} catch (NotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return VIEW_SINGLE_PROJECT;
	}

	private String createRedirectViewPath(String requestMapping) {
		StringBuilder redirectViewPath = new StringBuilder();
		redirectViewPath.append("redirect:");
		redirectViewPath.append(requestMapping);
		return redirectViewPath.toString();
	}

	@RequestMapping(value = "/project/add", method = RequestMethod.GET)
	public String showForm(Model model) {

		ProjectDTO project = new ProjectDTO();
		model.addAttribute("project", project);
		return VIEW_ADD;
	}

	@RequestMapping(value = "/project/{id}/epic/add", method = RequestMethod.POST)
	public String addEpic(@Valid @ModelAttribute(MODEL_ATTRIBUTE) EpicDTO dto, @PathVariable Long id,
			BindingResult result, RedirectAttributes attributes) {
		service.add(dto, id);
		return createRedirectViewPath("/project/" + id);
	}

	@RequestMapping(value = "/project/add", method = RequestMethod.POST)
	public String add(@Valid @ModelAttribute(MODEL_ATTRIBUTE) ProjectDTO dto, BindingResult result,
			RedirectAttributes attributes) {
		if (result.hasErrors()) {
			return VIEW_ADD;
		}
		service.add(dto);
		return createRedirectViewPath("/project/list");
	}

	@RequestMapping(value = "/project/{id}/delete", method = RequestMethod.POST)
	public String deleteProject(@PathVariable Long id) {
		try {
			Project project = service.deleteById(id);
		} catch (NotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return createRedirectViewPath("/project/list");
	}

}
