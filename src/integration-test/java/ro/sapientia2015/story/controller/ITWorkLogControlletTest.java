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

import org.hamcrest.core.IsAnything;
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
import ro.sapientia2015.story.StoryTestUtil;
import ro.sapientia2015.story.WorkLogTestUtil;
import ro.sapientia2015.story.dto.StoryDTO;
import ro.sapientia2015.story.dto.WorkLogDTO;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = WebContextLoader.class, classes = {ExampleApplicationContext.class})
//@ContextConfiguration(loader = WebContextLoader.class, locations = {"classpath:exampleApplicationContext.xml"})
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class,
        DirtiesContextTestExecutionListener.class,
        TransactionalTestExecutionListener.class,
        DbUnitTestExecutionListener.class })
@DatabaseSetup("storyData.xml")
public class ITWorkLogControlletTest {
	private static final Long FIELD_STORY_ID = Long.valueOf(1);
	private static final String FIELD_STORY_TITLE = "story_title";
	private static final String FIELD_DESCRIPTION = "description";
	private static final String FIELD_LOGGED_AT = "logged_at";
	private static final String FIELD_STARTED_AT = "started_at";
	private static final String FIELD_STARTED_AT_POST = "10:30";
	private static final String FIELD_ENDED_AT = "ended_at";
	private static final String FIELD_ENDED_AT_POST = "11:30";

    @Resource
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders.webApplicationContextSetup(webApplicationContext)
                .build();
    }

    @Test
    @ExpectedDatabase("workLogData.xml")
    public void showAddForm() throws Exception {
        mockMvc.perform(get("/worklog/add"))
                .andExpect(status().isOk())
                .andExpect(view().name(WorkLogController.VIEW_ADD))
                .andExpect(forwardedUrl("/WEB-INF/jsp/worklog/add.jsp"))
                .andExpect(model().attribute(WorkLogController.MODEL_ATTRIBUTE, hasProperty("story_title", isEmptyOrNullString())))
                .andExpect(model().attribute(WorkLogController.MODEL_ATTRIBUTE, hasProperty("description", isEmptyOrNullString())));
    }
    
    @Test
    @ExpectedDatabase(value="workLogData-add-expected.xml", assertionMode = DatabaseAssertionMode.NON_STRICT)
    public void add() throws Exception {
        String expectedRedirectViewPath = WorkLogTestUtil.createRedirectViewPath(WorkLogController.REQUEST_MAPPING_VIEW);

        mockMvc.perform(post("/worklog/add")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param(FIELD_STORY_TITLE, "New Story")
                .param(FIELD_LOGGED_AT, FIELD_LOGGED_AT)
                .param(FIELD_STARTED_AT, "10:30")
                .param(FIELD_ENDED_AT, "11:30")
                .param(FIELD_DESCRIPTION, "New Description")
                .sessionAttr(WorkLogController.MODEL_ATTRIBUTE, new WorkLogDTO())
        )
                .andExpect(status().isOk())
                .andExpect(view().name(expectedRedirectViewPath))
                .andExpect(model().attribute(WorkLogController.PARAMETER_ID, is("2")))
                .andExpect(flash().attribute(WorkLogController.FLASH_MESSAGE_KEY_FEEDBACK, is("feedbackMessage_worklog")));
    }
    
    @Test
    @ExpectedDatabase("workLogData-delete-expected.xml")
    public void deleteById() throws Exception {
        String expectedRedirectViewPath = WorkLogTestUtil.createRedirectViewPath(WorkLogController.REQUEST_MAPPING_LIST);
        mockMvc.perform(get("/worklog/delete/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(view().name(expectedRedirectViewPath))
                .andExpect(flash().attribute(WorkLogController.FLASH_MESSAGE_KEY_FEEDBACK, is("feedbackMessage_worklog")));
    }

}
