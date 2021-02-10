package ro.sapientia2015.story.controller;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;

import ro.sapientia2015.story.model.Bug;
import ro.sapientia2015.story.model.Story;
import ro.sapientia2015.story.service.BugService;
import ro.sapientia2015.story.service.StoryService;

@Controller
@SessionAttributes("home")
public class HomeController {

	protected static final String MODEL_ATTRIBUTE_LIST_STORY = "stories";
    protected static final String MODEL_ATTRIBUTE_LIST_BUG = "bugs";

    protected static final String REQUEST_MAPPING_LIST = "/";

    protected static final String VIEW_LIST = "/home/list";

    @Resource
    private StoryService storyService;

    @Resource
    private BugService bugService;


    @RequestMapping(value = REQUEST_MAPPING_LIST, method = RequestMethod.GET)
    public String findAll(Model model) {
        List<Story> storyModels = storyService.findAll();
        List<Bug> bugModels = bugService.findAll();
        model.addAttribute(MODEL_ATTRIBUTE_LIST_STORY, storyModels);
        model.addAttribute(MODEL_ATTRIBUTE_LIST_BUG, bugModels);
        return VIEW_LIST;
    }
}
