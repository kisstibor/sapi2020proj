package ro.sapientia2015.story.controller;

import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.isEmptyOrNullString;
import static org.hamcrest.Matchers.nullValue;
import static org.springframework.test.web.server.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.server.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.server.result.MockMvcResultMatchers.flash;
import static org.springframework.test.web.server.result.MockMvcResultMatchers.forwardedUrl;
import static org.springframework.test.web.server.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.server.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.server.result.MockMvcResultMatchers.view;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.test.web.server.MockMvc;
import org.springframework.test.web.server.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.ExpectedDatabase;
import com.github.springtestdbunit.assertion.DatabaseAssertionMode;

import ro.sapientia2015.config.ExampleApplicationContext;
import ro.sapientia2015.context.WebContextLoader;
import ro.sapientia2015.story.GoalTestUtil;
import ro.sapientia2015.story.StoryTestUtil;
import ro.sapientia2015.story.dto.GoalDTO;
import ro.sapientia2015.story.dto.StoryDTO;
import ro.sapientia2015.story.model.Goal;
import ro.sapientia2015.story.model.Story;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = WebContextLoader.class, classes = {ExampleApplicationContext.class})
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class,
        DirtiesContextTestExecutionListener.class,
        TransactionalTestExecutionListener.class,
        DbUnitTestExecutionListener.class })
@DatabaseSetup("goalData.xml")
public class ITGoalControllerTest {
    private static final String FORM_FIELD_ID = "id";
    private static final String FORM_FIELD_GOAL = "goal";
    private static final String FORM_FIELD_METHOD = "method";
    private static final String FORM_FIELD_METRICS = "metrics";

    @Resource
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders.webApplicationContextSetup(webApplicationContext)
                .build();
    }
    
    @Test
    @ExpectedDatabase(value="goalData-add-expected.xml", assertionMode = DatabaseAssertionMode.NON_STRICT)
    public void add() throws Exception {
        String expectedRedirectViewPath = GoalTestUtil.createRedirectViewPath(GoalController.REQUEST_MAPPING_VIEW);

        mockMvc.perform(post("/goal/add")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param(FORM_FIELD_GOAL, "Goal III")
                .param(FORM_FIELD_METHOD, "Method III")
                .param(FORM_FIELD_METRICS, "Metrics III")
                .sessionAttr(GoalController.MODEL_ATTRIBUTE, new GoalDTO())
        )
                .andExpect(status().isOk())
                .andExpect(view().name(expectedRedirectViewPath))
                .andExpect(model().attribute(GoalController.PARAMETER_ID, is("3")))
                .andExpect(flash().attribute(GoalController.FLASH_MESSAGE_KEY_FEEDBACK, is("Goal entry: Goal III was added.")));
    }
    
    @Test
    @ExpectedDatabase(value="goalData-update-expected.xml", assertionMode = DatabaseAssertionMode.NON_STRICT)
    public void update() throws Exception {
        String expectedRedirectViewPath = GoalTestUtil.createRedirectViewPath(GoalController.REQUEST_MAPPING_VIEW);

        mockMvc.perform(post("/goal/update")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param(FORM_FIELD_ID, "1")
                .param(FORM_FIELD_GOAL, "Goal I modified")
                .param(FORM_FIELD_METHOD, "Method I")
                .param(FORM_FIELD_METRICS, "Metrics I")
                .sessionAttr(GoalController.MODEL_ATTRIBUTE, new GoalDTO())
        )
                .andExpect(status().isOk())
                .andExpect(view().name(expectedRedirectViewPath))
                .andExpect(model().attribute(GoalController.PARAMETER_ID, is("1")))
                .andExpect(flash().attribute(GoalController.FLASH_MESSAGE_KEY_FEEDBACK, is("Goal entry: Goal I modified was updated.")));
    }
    
    @Test
    @ExpectedDatabase(value = "goalData.xml", assertionMode = DatabaseAssertionMode.NON_STRICT)
    public void showAddForm() throws Exception {
        mockMvc.perform(get("/goal/add"))
                .andExpect(status().isOk())
                .andExpect(view().name(GoalController.VIEW_ADD))
                .andExpect(forwardedUrl("/WEB-INF/jsp/goal/add.jsp"))
                .andExpect(model().attribute(GoalController.MODEL_ATTRIBUTE, hasProperty("id", nullValue())))
                .andExpect(model().attribute(GoalController.MODEL_ATTRIBUTE, hasProperty("goal", isEmptyOrNullString())))
                .andExpect(model().attribute(GoalController.MODEL_ATTRIBUTE, hasProperty("method", isEmptyOrNullString())))
                .andExpect(model().attribute(GoalController.MODEL_ATTRIBUTE, hasProperty("metrics", isEmptyOrNullString())));
    }
    
    @Test
    @ExpectedDatabase(value = "goalData.xml", assertionMode = DatabaseAssertionMode.NON_STRICT)
    public void addEmpty() throws Exception {
        mockMvc.perform(post("/goal/add")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .sessionAttr(GoalController.MODEL_ATTRIBUTE, new GoalDTO())
        )
                .andExpect(status().isOk())
                .andExpect(view().name(GoalController.VIEW_ADD))
                .andExpect(forwardedUrl("/WEB-INF/jsp/goal/add.jsp"))
                .andExpect(model().attributeHasFieldErrors(GoalController.MODEL_ATTRIBUTE, "goal"))
                .andExpect(model().attribute(GoalController.MODEL_ATTRIBUTE, hasProperty("id", nullValue())))
                .andExpect(model().attribute(GoalController.MODEL_ATTRIBUTE, hasProperty("goal", isEmptyOrNullString())))
                .andExpect(model().attribute(GoalController.MODEL_ATTRIBUTE, hasProperty("method", isEmptyOrNullString())))
                .andExpect(model().attribute(GoalController.MODEL_ATTRIBUTE, hasProperty("metrics", isEmptyOrNullString())));
    }
    
    @Test
    @ExpectedDatabase(value = "goalData.xml", assertionMode = DatabaseAssertionMode.NON_STRICT)
    public void addWhenGoalAndMethodAndMetricsAreTooLong() throws Exception {
        String goal = GoalTestUtil.createStringWithLength(Goal.MAX_LENGTH + 1);
        String method = GoalTestUtil.createStringWithLength(Goal.MAX_LENGTH + 1);
        String metrics = GoalTestUtil.createStringWithLength(Goal.MAX_LENGTH + 1);

        mockMvc.perform(post("/goal/add")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param(FORM_FIELD_GOAL, goal)
                .param(FORM_FIELD_METHOD, method)
                .param(FORM_FIELD_METRICS, metrics)
                .sessionAttr(GoalController.MODEL_ATTRIBUTE, new GoalDTO())
        )
                .andExpect(status().isOk())
                .andExpect(view().name(GoalController.VIEW_ADD))
                .andExpect(forwardedUrl("/WEB-INF/jsp/goal/add.jsp"))
                .andExpect(model().attributeHasFieldErrors(GoalController.MODEL_ATTRIBUTE, "goal"))
                .andExpect(model().attributeHasFieldErrors(GoalController.MODEL_ATTRIBUTE, "method"))
                .andExpect(model().attributeHasFieldErrors(GoalController.MODEL_ATTRIBUTE, "metrics"))
                .andExpect(model().attribute(GoalController.MODEL_ATTRIBUTE, hasProperty("id", nullValue())))
                .andExpect(model().attribute(GoalController.MODEL_ATTRIBUTE, hasProperty("goal", is(goal))))
                .andExpect(model().attribute(GoalController.MODEL_ATTRIBUTE, hasProperty("method", is(method))))
                .andExpect(model().attribute(GoalController.MODEL_ATTRIBUTE, hasProperty("metrics", is(metrics))));
    }
}
