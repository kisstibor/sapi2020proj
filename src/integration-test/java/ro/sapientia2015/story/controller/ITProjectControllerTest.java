package ro.sapientia2015.story.controller;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.ExpectedDatabase;
import com.github.springtestdbunit.assertion.DatabaseAssertionMode;

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

import ro.sapientia2015.common.controller.ErrorController;
import ro.sapientia2015.config.ExampleApplicationContext;
import ro.sapientia2015.context.WebContextLoader;
import ro.sapientia2015.project.dto.ProjectDTO;
import ro.sapientia2015.project.model.Project;
import ro.sapientia2015.story.ProjectTestUtil;
import ro.sapientia2015.story.controller.ProjectController;

import javax.annotation.Resource;

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.isEmptyOrNullString;
import static org.hamcrest.Matchers.nullValue;
import static org.springframework.test.web.server.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.server.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.server.result.MockMvcResultMatchers.forwardedUrl;
import static org.springframework.test.web.server.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.server.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.server.result.MockMvcResultMatchers.view;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = WebContextLoader.class, classes = {ExampleApplicationContext.class})
//@ContextConfiguration(loader = WebContextLoader.class, locations = {"classpath:exampleApplicationContext.xml"})
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class,
        DirtiesContextTestExecutionListener.class,
        TransactionalTestExecutionListener.class,
        DbUnitTestExecutionListener.class })
@DatabaseSetup("projectData.xml")
public class ITProjectControllerTest {

    private static final String FORM_FIELD_DESCRIPTION = "description";
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
    @ExpectedDatabase(value = "projectData.xml", assertionMode = DatabaseAssertionMode.NON_STRICT)
    public void showAddForm() throws Exception {
        mockMvc.perform(get("/project/add"))
                .andExpect(status().isOk())
                .andExpect(view().name(ProjectController.VIEW_ADD))
                .andExpect(forwardedUrl("/WEB-INF/jsp/project/add.jsp"))
                .andExpect(model().attribute(ProjectController.MODEL_ATTRIBUTE, hasProperty("id", nullValue())))
                .andExpect(model().attribute(ProjectController.MODEL_ATTRIBUTE, hasProperty("description", isEmptyOrNullString())))
                .andExpect(model().attribute(ProjectController.MODEL_ATTRIBUTE, hasProperty("title", isEmptyOrNullString())));
    }

    @Test
    @ExpectedDatabase(value = "projectData.xml", assertionMode = DatabaseAssertionMode.NON_STRICT)
    public void addEmpty() throws Exception {
        mockMvc.perform(post("/project/add")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .sessionAttr(ProjectController.MODEL_ATTRIBUTE, new ProjectDTO())
        )
                .andExpect(status().isOk())
                .andExpect(view().name(ProjectController.VIEW_ADD))
                .andExpect(forwardedUrl("/WEB-INF/jsp/project/add.jsp"))
                .andExpect(model().attributeHasFieldErrors(ProjectController.MODEL_ATTRIBUTE, "title"))
                .andExpect(model().attribute(ProjectController.MODEL_ATTRIBUTE, hasProperty("id", nullValue())))
                .andExpect(model().attribute(ProjectController.MODEL_ATTRIBUTE, hasProperty("description", isEmptyOrNullString())))
                .andExpect(model().attribute(ProjectController.MODEL_ATTRIBUTE, hasProperty("title", isEmptyOrNullString())));
    }

    @Test
    @ExpectedDatabase(value = "projectData.xml", assertionMode = DatabaseAssertionMode.NON_STRICT)
    public void addWhenTitleAndDescriptionAreTooLong() throws Exception {
        String title = ProjectTestUtil.createStringWithLength(Project.MAX_LENGTH_TITLE + 1);
        String description = ProjectTestUtil.createStringWithLength(Project.MAX_LENGTH_DESCRIPTION + 1);

        mockMvc.perform(post("/project/add")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param(FORM_FIELD_DESCRIPTION, description)
                .param(FORM_FIELD_TITLE, title)
                .sessionAttr(ProjectController.MODEL_ATTRIBUTE, new ProjectDTO())
        )
                .andExpect(status().isOk())
                .andExpect(view().name(ProjectController.VIEW_ADD))
                .andExpect(forwardedUrl("/WEB-INF/jsp/project/add.jsp"))
                .andExpect(model().attributeHasFieldErrors(ProjectController.MODEL_ATTRIBUTE, "title"))
                .andExpect(model().attributeHasFieldErrors(ProjectController.MODEL_ATTRIBUTE, "description"))
                .andExpect(model().attribute(ProjectController.MODEL_ATTRIBUTE, hasProperty("id", nullValue())))
                .andExpect(model().attribute(ProjectController.MODEL_ATTRIBUTE, hasProperty("description", is(description))))
                .andExpect(model().attribute(ProjectController.MODEL_ATTRIBUTE, hasProperty("title", is(title))));
    }

    @Test
    @ExpectedDatabase(value="projectData-add-expected.xml", assertionMode = DatabaseAssertionMode.NON_STRICT)
    public void add() throws Exception {
        String expectedRedirectViewPath = ProjectTestUtil.createRedirectViewPath(ProjectController.REQUEST_MAPPING_VIEW);

        mockMvc.perform(post("/project/add")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param(FORM_FIELD_DESCRIPTION, "description")
                .param(FORM_FIELD_TITLE, "title")
                .sessionAttr(ProjectController.MODEL_ATTRIBUTE, new ProjectDTO())
        )
                .andExpect(status().isOk())
                .andExpect(view().name(expectedRedirectViewPath));
                //.andExpect(model().attribute(ProjectController.PARAMETER_ID, is("3")));
                //.andExpect(flash().attribute(ProjectController.FLASH_MESSAGE_KEY_FEEDBACK, is("Project entry: title was added.")));
    }

    @Test
    @ExpectedDatabase(value = "projectData.xml", assertionMode = DatabaseAssertionMode.NON_STRICT)
    public void findAll() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name(ProjectController.VIEW_LIST))
                .andExpect(forwardedUrl("/WEB-INF/jsp/project/list.jsp"))
                .andExpect(model().attribute(ProjectController.MODEL_ATTRIBUTE_LIST, hasSize(2)))
                .andExpect(model().attribute(ProjectController.MODEL_ATTRIBUTE_LIST, hasItem(
                        allOf(
                                hasProperty("id", is(1L)),
                                hasProperty("description", is("Lorem ipsum")),
                                hasProperty("title", is("Foo"))
                        )
                )))
                .andExpect(model().attribute(ProjectController.MODEL_ATTRIBUTE_LIST, hasItem(
                        allOf(
                                hasProperty("id", is(2L)),
                                hasProperty("description", is("Lorem ipsum")),
                                hasProperty("title", is("Bar"))
                        )
                )));
    }

    @Test
    @ExpectedDatabase(value = "projectData.xml", assertionMode = DatabaseAssertionMode.NON_STRICT)
    public void findById() throws Exception {
        mockMvc.perform(get("/project/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(view().name(ProjectController.VIEW_VIEW))
                .andExpect(forwardedUrl("/WEB-INF/jsp/project/view.jsp"))
                .andExpect(model().attribute(ProjectController.MODEL_ATTRIBUTE, hasProperty("id", is(1L))))
                .andExpect(model().attribute(ProjectController.MODEL_ATTRIBUTE, hasProperty("description", is("Lorem ipsum"))))
                .andExpect(model().attribute(ProjectController.MODEL_ATTRIBUTE, hasProperty("title", is("Foo"))));
    }

    @Test
    @ExpectedDatabase(value = "projectData.xml", assertionMode = DatabaseAssertionMode.NON_STRICT)
    public void findByIdWhenIsNotFound() throws Exception {
        mockMvc.perform(get("/project/{id}", 3L))
                .andExpect(status().isNotFound())
                .andExpect(view().name(ErrorController.VIEW_NOT_FOUND))
                .andExpect(forwardedUrl("/WEB-INF/jsp/error/404.jsp"));
    }

    @Test
    @ExpectedDatabase(value = "projectData-delete-expected.xml", assertionMode = DatabaseAssertionMode.NON_STRICT)
    public void deleteById() throws Exception {
        String expectedRedirectViewPath = ProjectTestUtil.createRedirectViewPath(ProjectController.REQUEST_MAPPING_LIST);
        mockMvc.perform(get("/project/delete/{id}", 2L))
                .andExpect(status().isOk())
                .andExpect(view().name(expectedRedirectViewPath));
                //.andExpect(flash().attribute(ProjectController.FLASH_MESSAGE_KEY_FEEDBACK, is("Project entry: Bar was deleted.")));
    }

    @Test
    @ExpectedDatabase(value = "projectData.xml", assertionMode = DatabaseAssertionMode.NON_STRICT)
    public void deleteByIdWhenIsNotFound() throws Exception {
        mockMvc.perform(get("/project/delete/{id}", 3L))
                .andExpect(status().isNotFound())
                .andExpect(view().name(ErrorController.VIEW_NOT_FOUND))
                .andExpect(forwardedUrl("/WEB-INF/jsp/error/404.jsp"));
    }

}

