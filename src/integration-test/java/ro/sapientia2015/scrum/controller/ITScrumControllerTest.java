package ro.sapientia2015.scrum.controller;

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.hasSize;
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

import ro.sapientia2015.common.controller.ErrorController;
import ro.sapientia2015.config.ExampleApplicationContext;
import ro.sapientia2015.context.WebContextLoader;
import ro.sapientia2015.scrum.ScrumTestUtil;
import ro.sapientia2015.scrum.dto.ScrumDTO;
import ro.sapientia2015.scrum.model.Scrum;
import ro.sapientia2015.story.controller.StoryController;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = WebContextLoader.class, classes = {ExampleApplicationContext.class})
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class,
    DirtiesContextTestExecutionListener.class,
    TransactionalTestExecutionListener.class,
    DbUnitTestExecutionListener.class })
@DatabaseSetup("scrumData.xml")
public class ITScrumControllerTest {
	private static final String FORM_FIELD_MEMBERS = "members";
    private static final String FORM_FIELD_ID = "id";
    private static final String FORM_FIELD_TITLE = "title";

    @Resource
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders.webApplicationContextSetup(webApplicationContext)
                .build();
    }
    
    @Test
    @ExpectedDatabase("scrumData.xml")
    public void showAddForm() throws Exception {
        mockMvc.perform(get("/scrum/add"))
                .andExpect(status().isOk())
                .andExpect(view().name(ScrumController.VIEW_ADD))
                .andExpect(forwardedUrl("/WEB-INF/jsp/scrum/add.jsp"))
                .andExpect(model().attribute(ScrumController.MODEL_ATTRIBUTE, hasProperty("id", nullValue())))
                .andExpect(model().attribute(ScrumController.MODEL_ATTRIBUTE, hasProperty("members", isEmptyOrNullString())))
                .andExpect(model().attribute(ScrumController.MODEL_ATTRIBUTE, hasProperty("title", isEmptyOrNullString())));
    }
    
    @Test
    @ExpectedDatabase("scrumData.xml")
    public void addEmpty() throws Exception {
        mockMvc.perform(post("/scrum/add")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .sessionAttr(ScrumController.MODEL_ATTRIBUTE, new ScrumDTO())
        )
                .andExpect(status().isOk())
                .andExpect(view().name(ScrumController.VIEW_ADD))
                .andExpect(forwardedUrl("/WEB-INF/jsp/scrum/add.jsp"))
                .andExpect(model().attributeHasFieldErrors(ScrumController.MODEL_ATTRIBUTE, "title"))
                .andExpect(model().attribute(ScrumController.MODEL_ATTRIBUTE, hasProperty("id", nullValue())))
                .andExpect(model().attribute(ScrumController.MODEL_ATTRIBUTE, hasProperty("members", isEmptyOrNullString())))
                .andExpect(model().attribute(ScrumController.MODEL_ATTRIBUTE, hasProperty("title", isEmptyOrNullString())));
    }

    @Test
    @ExpectedDatabase("scrumData.xml")
    public void addWhenTitleAndMembersAreTooLong() throws Exception {
        String title = ScrumTestUtil.createStringWithLength(Scrum.MAX_LENGTH_NAME + 1);
        String description = ScrumTestUtil.createStringWithLength(Scrum.MAX_LENGTH_MEM + 1);

        mockMvc.perform(post("/scrum/add")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param(FORM_FIELD_MEMBERS, description)
                .param(FORM_FIELD_TITLE, title)
                .sessionAttr(ScrumController.MODEL_ATTRIBUTE, new ScrumDTO())
        )
                .andExpect(status().isOk())
                .andExpect(view().name(ScrumController.VIEW_ADD))
                .andExpect(forwardedUrl("/WEB-INF/jsp/scrum/add.jsp"))
                .andExpect(model().attributeHasFieldErrors(ScrumController.MODEL_ATTRIBUTE, "title"))
                .andExpect(model().attributeHasFieldErrors(ScrumController.MODEL_ATTRIBUTE, "members"))
                .andExpect(model().attribute(ScrumController.MODEL_ATTRIBUTE, hasProperty("id", nullValue())))
                .andExpect(model().attribute(ScrumController.MODEL_ATTRIBUTE, hasProperty("members", is(description))))
                .andExpect(model().attribute(ScrumController.MODEL_ATTRIBUTE, hasProperty("title", is(title))));
    }
    
    @Test
    @ExpectedDatabase(value="scrumData-add-expected.xml", assertionMode = DatabaseAssertionMode.NON_STRICT)
    public void add() throws Exception {
        String expectedRedirectViewPath = ScrumTestUtil.createRedirectViewPath(ScrumController.REQUEST_MAPPING_VIEW);

        mockMvc.perform(post("/scrum/add")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param(FORM_FIELD_MEMBERS, "members")
                .param(FORM_FIELD_TITLE, "title")
                .sessionAttr(ScrumController.MODEL_ATTRIBUTE, new ScrumDTO())
        )
                .andExpect(status().isOk())
                .andExpect(view().name(expectedRedirectViewPath))
                .andExpect(model().attribute(ScrumController.PARAMETER_ID, is("3")))
                .andExpect(flash().attribute(ScrumController.FLASH_MESSAGE_KEY_FEEDBACK, is("Scrum entry: title was added.")));
    }

    @Test
    @ExpectedDatabase("scrumData.xml")
    public void findAll() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name(ScrumController.VIEW_LIST))
                .andExpect(forwardedUrl("/WEB-INF/jsp/scrum/list.jsp"))
                .andExpect(model().attribute(ScrumController.MODEL_ATTRIBUTE_LIST, hasSize(2)))
                .andExpect(model().attribute(ScrumController.MODEL_ATTRIBUTE_LIST, hasItem(
                        allOf(
                                hasProperty("id", is(1L)),
                                hasProperty("members", is("Lorem, Ipsum")),
                                hasProperty("title", is("Foo"))
                        )
                )))
                .andExpect(model().attribute(ScrumController.MODEL_ATTRIBUTE_LIST, hasItem(
                        allOf(
                                hasProperty("id", is(2L)),
                                hasProperty("members", is("LoreM, iPsum")),
                                hasProperty("title", is("Bar"))
                        )
                )));
    }
    
    @Test
    @ExpectedDatabase("scrumData.xml")
    public void findById() throws Exception {
        mockMvc.perform(get("/scrum/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(view().name(ScrumController.VIEW_VIEW))
                .andExpect(forwardedUrl("/WEB-INF/jsp/scrum/view.jsp"))
                .andExpect(model().attribute(ScrumController.MODEL_ATTRIBUTE, hasProperty("id", is(1L))))
                .andExpect(model().attribute(ScrumController.MODEL_ATTRIBUTE, hasProperty("members", is("Lorem, Ipsum"))))
                .andExpect(model().attribute(ScrumController.MODEL_ATTRIBUTE, hasProperty("title", is("Foo"))));
    }

    @Test
    @ExpectedDatabase("scrumData.xml")
    public void findByIdWhenIsNotFound() throws Exception {
        mockMvc.perform(get("/scrum/{id}", 3L))
                .andExpect(status().isNotFound())
                .andExpect(view().name(ErrorController.VIEW_NOT_FOUND))
                .andExpect(forwardedUrl("/WEB-INF/jsp/error/404.jsp"));
    }
    
    
}
