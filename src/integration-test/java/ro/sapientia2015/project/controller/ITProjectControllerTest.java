package ro.sapientia2015.project.controller;

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
import ro.sapientia2015.project.ProjectTestUtil;
import ro.sapientia2015.project.dto.ProjectDTO;
import ro.sapientia2015.project.model.Project;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = WebContextLoader.class, classes = {ExampleApplicationContext.class})
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class,
        DirtiesContextTestExecutionListener.class,
        TransactionalTestExecutionListener.class,
        DbUnitTestExecutionListener.class })
@DatabaseSetup("projectData.xml")
public class ITProjectControllerTest {

    private static final String FORM_FIELD_ID = "id";
    private static final String FORM_FIELD_NAME = "name";
    private static final String FORM_FIELD_PRODUCTOWNER = "productOwner";
    private static final String FORM_FIELD_SCRUMMASTER = "scrumMaster";
    private static final String FORM_FIELD_MEMBERS = "members";

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
                .andExpect(model().attribute(ProjectController.MODEL_ATTRIBUTE, hasProperty("name", isEmptyOrNullString())))
                .andExpect(model().attribute(ProjectController.MODEL_ATTRIBUTE, hasProperty("productOwner", isEmptyOrNullString())))
                .andExpect(model().attribute(ProjectController.MODEL_ATTRIBUTE, hasProperty("scrumMaster", isEmptyOrNullString())))
                .andExpect(model().attribute(ProjectController.MODEL_ATTRIBUTE, hasProperty("members", nullValue())));
    }

    @Test
    @ExpectedDatabase(value="projectData.xml", assertionMode = DatabaseAssertionMode.NON_STRICT)
    public void addEmpty() throws Exception {
        mockMvc.perform(post("/project/add")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .sessionAttr(ProjectController.MODEL_ATTRIBUTE, new ProjectDTO())
        )
                .andExpect(status().isOk())
                .andExpect(view().name(ProjectController.VIEW_ADD))
                .andExpect(forwardedUrl("/WEB-INF/jsp/project/add.jsp"))
                .andExpect(model().attributeHasFieldErrors(ProjectController.MODEL_ATTRIBUTE, "name"))
                .andExpect(model().attribute(ProjectController.MODEL_ATTRIBUTE, hasProperty("id", nullValue())))
                .andExpect(model().attribute(ProjectController.MODEL_ATTRIBUTE, hasProperty("name", isEmptyOrNullString())))
                .andExpect(model().attribute(ProjectController.MODEL_ATTRIBUTE, hasProperty("productOwner", isEmptyOrNullString())))
                .andExpect(model().attribute(ProjectController.MODEL_ATTRIBUTE, hasProperty("scrumMaster", isEmptyOrNullString())))
                .andExpect(model().attribute(ProjectController.MODEL_ATTRIBUTE, hasProperty("members", nullValue())));
    }

    @Test
    @ExpectedDatabase(value = "projectData.xml", assertionMode = DatabaseAssertionMode.NON_STRICT)
    public void addWhenNameAndProductOwnerAndScrumMasterAreTooLong() throws Exception {
        String name = ProjectTestUtil.createStringWithLength(Project.MAX_LENGTH_NAME + 1);
        String productOwner = ProjectTestUtil.createStringWithLength(Project.MAX_LENGTH_PRODUCTOWNER + 1);
        String scrumMaster = ProjectTestUtil.createStringWithLength(Project.MAX_LENGTH_SCRUMMASTER + 1);

        mockMvc.perform(post("/project/add")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param(FORM_FIELD_NAME, name)
                .param(FORM_FIELD_PRODUCTOWNER, productOwner)
                .param(FORM_FIELD_SCRUMMASTER, scrumMaster)
                .sessionAttr(ProjectController.MODEL_ATTRIBUTE, new ProjectDTO())
        )
                .andExpect(status().isOk())
                .andExpect(view().name(ProjectController.VIEW_ADD))
                .andExpect(forwardedUrl("/WEB-INF/jsp/project/add.jsp"))
                .andExpect(model().attributeHasFieldErrors(ProjectController.MODEL_ATTRIBUTE, "name"))
                .andExpect(model().attributeHasFieldErrors(ProjectController.MODEL_ATTRIBUTE, "productOwner"))
                .andExpect(model().attributeHasFieldErrors(ProjectController.MODEL_ATTRIBUTE, "scrumMaster"))
                .andExpect(model().attribute(ProjectController.MODEL_ATTRIBUTE, hasProperty("id", nullValue())))
                .andExpect(model().attribute(ProjectController.MODEL_ATTRIBUTE, hasProperty("name", is(name))))
                .andExpect(model().attribute(ProjectController.MODEL_ATTRIBUTE, hasProperty("productOwner", is(productOwner))))
                .andExpect(model().attribute(ProjectController.MODEL_ATTRIBUTE, hasProperty("scrumMaster", is(scrumMaster))));
    }

    @Test
    @ExpectedDatabase(value="projectData-add-expected.xml", assertionMode = DatabaseAssertionMode.NON_STRICT)
    public void add() throws Exception {
        String expectedRedirectViewPath = ProjectTestUtil.createRedirectViewPath(ProjectController.REQUEST_MAPPING_VIEW);

        mockMvc.perform(post("/project/add")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param(FORM_FIELD_NAME, "name")
                .param(FORM_FIELD_PRODUCTOWNER, "productOwner")
                .param(FORM_FIELD_SCRUMMASTER, "scrumMaster")
                .param(FORM_FIELD_MEMBERS, "members")
                .sessionAttr(ProjectController.MODEL_ATTRIBUTE, new ProjectDTO())
        )
                .andExpect(status().isOk())
                .andExpect(view().name(expectedRedirectViewPath))
                .andExpect(model().attribute(ProjectController.PARAMETER_ID, is("4")))
                .andExpect(flash().attribute(ProjectController.FLASH_MESSAGE_KEY_FEEDBACK, is("Project entry: name was added.")));
    }

    @Test
    @ExpectedDatabase(value = "projectData.xml", assertionMode = DatabaseAssertionMode.NON_STRICT)
    public void findAll() throws Exception {
        mockMvc.perform(get("/projects"))
                .andExpect(status().isOk())
                .andExpect(view().name(ProjectController.VIEW_LIST))
                .andExpect(forwardedUrl("/WEB-INF/jsp/project/list.jsp"))
                .andExpect(model().attribute(ProjectController.MODEL_ATTRIBUTE_LIST, hasSize(3)))
                .andExpect(model().attribute(ProjectController.MODEL_ATTRIBUTE_LIST, hasItem(
                        allOf(
                                hasProperty("id", is(1L)),
                                hasProperty("name", is("Lorem ipsum")),
                                hasProperty("productOwner", is("Foo")),
                                hasProperty("scrumMaster", is("Qwerty1")),
                                hasProperty("members", is("asd1"))
                        )
                )))
                .andExpect(model().attribute(ProjectController.MODEL_ATTRIBUTE_LIST, hasItem(
                        allOf(
                                hasProperty("id", is(2L)),
                                hasProperty("name", is("Lorem ipsum 2")),
                                hasProperty("productOwner", is("Foo 2")),
                                hasProperty("scrumMaster", is("Qwerty2")),
                                hasProperty("members", is("asd2"))
                        )
                )))
                .andExpect(model().attribute(ProjectController.MODEL_ATTRIBUTE_LIST, hasItem(
                        allOf(
                        		hasProperty("id", is(3L)),
                                hasProperty("name", is("Lorem ipsum 3")),
                                hasProperty("productOwner", is("Foo 3")),
                                hasProperty("scrumMaster", is("Qwerty3")),
                                hasProperty("members", is("asd3"))
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
                .andExpect(model().attribute(ProjectController.MODEL_ATTRIBUTE, hasProperty("name", is("Lorem ipsum"))))
                .andExpect(model().attribute(ProjectController.MODEL_ATTRIBUTE, hasProperty("productOwner", is("Foo"))))
                .andExpect(model().attribute(ProjectController.MODEL_ATTRIBUTE, hasProperty("scrumMaster", is("Qwerty1"))))
                .andExpect(model().attribute(ProjectController.MODEL_ATTRIBUTE, hasProperty("members", is("asd1"))));
    }

    @Test
    @ExpectedDatabase(value = "projectData.xml", assertionMode = DatabaseAssertionMode.NON_STRICT)
    public void findByIdWhenIsNotFound() throws Exception {
        mockMvc.perform(get("/project/{id}", 4L))
                .andExpect(status().isNotFound())
                .andExpect(view().name(ErrorController.VIEW_NOT_FOUND))
                .andExpect(forwardedUrl("/WEB-INF/jsp/error/404.jsp"));
    }

    @Test
    @ExpectedDatabase(value = "projectData-delete-expected.xml", assertionMode = DatabaseAssertionMode.NON_STRICT)
    public void deleteById() throws Exception {
        String expectedRedirectViewPath = ProjectTestUtil.createRedirectViewPath(ProjectController.REQUEST_MAPPING_LIST);
        mockMvc.perform(get("/project/delete/{id}", 3L))
                .andExpect(status().isOk())
                .andExpect(view().name(expectedRedirectViewPath))
                .andExpect(flash().attribute(ProjectController.FLASH_MESSAGE_KEY_FEEDBACK, is("Project entry: Lorem ipsum 3 was deleted.")));
    }

    @Test
    @ExpectedDatabase(value = "projectData.xml", assertionMode = DatabaseAssertionMode.NON_STRICT)
    public void deleteByIdWhenIsNotFound() throws Exception {
        mockMvc.perform(get("/project/delete/{id}", 4L))
                .andExpect(status().isNotFound())
                .andExpect(view().name(ErrorController.VIEW_NOT_FOUND))
                .andExpect(forwardedUrl("/WEB-INF/jsp/error/404.jsp"));
    }

    @Test
    @ExpectedDatabase(value = "projectData.xml", assertionMode = DatabaseAssertionMode.NON_STRICT)
    public void showUpdateForm() throws Exception {
        mockMvc.perform(get("/project/update/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(view().name(ProjectController.VIEW_UPDATE))
                .andExpect(forwardedUrl("/WEB-INF/jsp/project/update.jsp"))
                .andExpect(model().attribute(ProjectController.MODEL_ATTRIBUTE, hasProperty("id", is(1L))))
                .andExpect(model().attribute(ProjectController.MODEL_ATTRIBUTE, hasProperty("name", is("Lorem ipsum"))))
                .andExpect(model().attribute(ProjectController.MODEL_ATTRIBUTE, hasProperty("productOwner", is("Foo"))))
                .andExpect(model().attribute(ProjectController.MODEL_ATTRIBUTE, hasProperty("scrumMaster", is("Qwerty1"))))
                .andExpect(model().attribute(ProjectController.MODEL_ATTRIBUTE, hasProperty("members", is("asd1"))));
    }

    @Test
    @ExpectedDatabase(value = "projectData.xml", assertionMode = DatabaseAssertionMode.NON_STRICT)
    public void showUpdateFormWhenIsNotFound() throws Exception {
        mockMvc.perform(get("/project/update/{id}", 4L))
                .andExpect(status().isNotFound())
                .andExpect(view().name(ErrorController.VIEW_NOT_FOUND))
                .andExpect(forwardedUrl("/WEB-INF/jsp/error/404.jsp"));
    }

    @Test
    @ExpectedDatabase(value = "projectData.xml", assertionMode = DatabaseAssertionMode.NON_STRICT)
    public void updateEmpty() throws Exception {
        mockMvc.perform(post("/project/update")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param(FORM_FIELD_ID, "1")
                .sessionAttr(ProjectController.MODEL_ATTRIBUTE, new ProjectDTO())
        )
                .andExpect(status().isOk())
                .andExpect(view().name(ProjectController.VIEW_UPDATE))
                .andExpect(forwardedUrl("/WEB-INF/jsp/project/update.jsp"))
                .andExpect(model().attributeHasFieldErrors(ProjectController.MODEL_ATTRIBUTE, "name"))
                .andExpect(model().attribute(ProjectController.MODEL_ATTRIBUTE, hasProperty("id", is(1L))))
                .andExpect(model().attribute(ProjectController.MODEL_ATTRIBUTE, hasProperty("name", isEmptyOrNullString())))
                .andExpect(model().attribute(ProjectController.MODEL_ATTRIBUTE, hasProperty("productOwner", isEmptyOrNullString())))
                .andExpect(model().attribute(ProjectController.MODEL_ATTRIBUTE, hasProperty("scrumMaster", isEmptyOrNullString())))
                .andExpect(model().attribute(ProjectController.MODEL_ATTRIBUTE, hasProperty("members", isEmptyOrNullString())));
    }

    @Test
    @ExpectedDatabase(value = "projectData.xml", assertionMode = DatabaseAssertionMode.NON_STRICT)
    public void updateWhenNameAndProductOwnerAndScrumMasterAreTooLong() throws Exception {
        String name = ProjectTestUtil.createStringWithLength(Project.MAX_LENGTH_NAME + 1);
        String productOwner = ProjectTestUtil.createStringWithLength(Project.MAX_LENGTH_PRODUCTOWNER + 1);
        String scrumMaster = ProjectTestUtil.createStringWithLength(Project.MAX_LENGTH_SCRUMMASTER + 1);

        mockMvc.perform(post("/project/update")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param(FORM_FIELD_ID, "1")
                .param(FORM_FIELD_NAME, name)
                .param(FORM_FIELD_PRODUCTOWNER, productOwner)
                .param(FORM_FIELD_SCRUMMASTER, scrumMaster)
                .sessionAttr(ProjectController.MODEL_ATTRIBUTE, new ProjectDTO())
        )
                .andExpect(status().isOk())
                .andExpect(view().name(ProjectController.VIEW_UPDATE))
                .andExpect(forwardedUrl("/WEB-INF/jsp/project/update.jsp"))
                .andExpect(model().attributeHasFieldErrors(ProjectController.MODEL_ATTRIBUTE, "name"))
                .andExpect(model().attributeHasFieldErrors(ProjectController.MODEL_ATTRIBUTE, "productOwner"))
                .andExpect(model().attributeHasFieldErrors(ProjectController.MODEL_ATTRIBUTE, "scrumMaster"))
                .andExpect(model().attribute(ProjectController.MODEL_ATTRIBUTE, hasProperty("id", is(1L))))
                .andExpect(model().attribute(ProjectController.MODEL_ATTRIBUTE, hasProperty("name", is(name))))
                .andExpect(model().attribute(ProjectController.MODEL_ATTRIBUTE, hasProperty("productOwner", is(productOwner))))
                .andExpect(model().attribute(ProjectController.MODEL_ATTRIBUTE, hasProperty("scrumMaster", is(scrumMaster))));
    }

    @Test
    @ExpectedDatabase(value="projectData-update-expected.xml", assertionMode = DatabaseAssertionMode.NON_STRICT)
    public void update() throws Exception {
        String expectedRedirectViewPath = ProjectTestUtil.createRedirectViewPath(ProjectController.REQUEST_MAPPING_VIEW);

        mockMvc.perform(post("/project/update")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param(FORM_FIELD_ID, "1")
                .param(FORM_FIELD_NAME, "name")
                .param(FORM_FIELD_PRODUCTOWNER, "productOwner")
                .param(FORM_FIELD_SCRUMMASTER, "scrumMaster")
                .param(FORM_FIELD_MEMBERS, "members")
                .sessionAttr(ProjectController.MODEL_ATTRIBUTE, new ProjectDTO())
        )
                .andExpect(status().isOk())
                .andExpect(view().name(expectedRedirectViewPath))
                .andExpect(model().attribute(ProjectController.PARAMETER_ID, is("1")))
                .andExpect(flash().attribute(ProjectController.FLASH_MESSAGE_KEY_FEEDBACK, is("Project entry: name was updated.")));
    }

    @Test
    @ExpectedDatabase(value = "projectData.xml", assertionMode = DatabaseAssertionMode.NON_STRICT)
    public void updateWhenIsNotFound() throws Exception {
        mockMvc.perform(post("/project/update")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param(FORM_FIELD_ID, "4")
                .param(FORM_FIELD_NAME, "name")
                .param(FORM_FIELD_PRODUCTOWNER, "productOwner")
                .param(FORM_FIELD_SCRUMMASTER, "scrumMaster")
                .param(FORM_FIELD_MEMBERS, "members")
                .sessionAttr(ProjectController.MODEL_ATTRIBUTE, new ProjectDTO())
        )
                .andExpect(status().isNotFound())
                .andExpect(view().name(ErrorController.VIEW_NOT_FOUND))
                .andExpect(forwardedUrl("/WEB-INF/jsp/error/404.jsp"));
    }
}

