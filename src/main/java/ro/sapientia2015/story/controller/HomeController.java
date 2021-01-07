package ro.sapientia2015.story.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ro.sapientia2015.story.model.Story;
import ro.sapientia2015.story.service.StoryService;
import ro.sapientia2015.story.service.TaskService;
import javax.annotation.Resource;
import java.util.List;

@Controller
@SessionAttributes("home")
public class HomeController {


    protected static final String MODEL_ATTRIBUTE = "story";
    protected static final String MODEL_ATTRIBUTE_LIST = "stories";

    protected static final String PARAMETER_ID = "id";

    protected static final String REQUEST_MAPPING_LIST = "/";

    protected static final String VIEW_LIST = "home/list";


    @Resource
    private StoryService storyService;
    
    @Resource
    private TaskService taskService;


    @RequestMapping(value = REQUEST_MAPPING_LIST, method = RequestMethod.GET)
    public String findAll(Model model) {
        List<Story> models = storyService.findAll();
        model.addAttribute(MODEL_ATTRIBUTE_LIST, models);
        return VIEW_LIST;
    }
}
