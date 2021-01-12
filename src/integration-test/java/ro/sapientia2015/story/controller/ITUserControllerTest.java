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
import ro.sapientia2015.story.UserTestUtil;
import ro.sapientia2015.story.dto.UserDTO;
import ro.sapientia2015.story.model.User;

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
import static org.springframework.test.web.server.result.MockMvcResultMatchers.flash;
import static org.springframework.test.web.server.result.MockMvcResultMatchers.forwardedUrl;
import static org.springframework.test.web.server.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.server.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.server.result.MockMvcResultMatchers.view;

/**
 * This test uses the annotation based application context configuration.
 * @author Kiss Tibor
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = WebContextLoader.class, classes = {ExampleApplicationContext.class})
//@ContextConfiguration(loader = WebContextLoader.class, locations = {"classpath:exampleApplicationContext.xml"})
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class,
        DirtiesContextTestExecutionListener.class,
        TransactionalTestExecutionListener.class,
        DbUnitTestExecutionListener.class })
@DatabaseSetup("userData.xml")
public class ITUserControllerTest {

    private static final String FORM_FIELD_TYPE = "type";
    private static final String FORM_FIELD_ID = "id";
    private static final String FORM_FIELD_NAME = "name";

    @Resource
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders.webApplicationContextSetup(webApplicationContext)
                .build();
    }

    @Test
    @ExpectedDatabase("userData.xml")
    public void showAddForm() throws Exception {
        mockMvc.perform(get("/user/add"))
                .andExpect(status().isOk())
                .andExpect(view().name(UserController.VIEW_ADD))
                .andExpect(forwardedUrl("/WEB-INF/jsp/user/add.jsp"))
                .andExpect(model().attribute(UserController.MODEL_ATTRIBUTE, hasProperty("id", nullValue())))
                .andExpect(model().attribute(UserController.MODEL_ATTRIBUTE, hasProperty("type", isEmptyOrNullString())))
                .andExpect(model().attribute(UserController.MODEL_ATTRIBUTE, hasProperty("name", isEmptyOrNullString())));
    }

    @Test
    @ExpectedDatabase("userData.xml")
    public void addEmpty() throws Exception {
        mockMvc.perform(post("/user/add")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .sessionAttr(UserController.MODEL_ATTRIBUTE, new UserDTO())
        )
                .andExpect(status().isOk())
                .andExpect(view().name(UserController.VIEW_ADD))
                .andExpect(forwardedUrl("/WEB-INF/jsp/user/add.jsp"))
                .andExpect(model().attributeHasFieldErrors(UserController.MODEL_ATTRIBUTE, "name"))
                .andExpect(model().attribute(UserController.MODEL_ATTRIBUTE, hasProperty("id", nullValue())))
                .andExpect(model().attribute(UserController.MODEL_ATTRIBUTE, hasProperty("type", isEmptyOrNullString())))
                .andExpect(model().attribute(UserController.MODEL_ATTRIBUTE, hasProperty("name", isEmptyOrNullString())));
    }

    @Test
    @ExpectedDatabase("userData.xml")
    public void addWhenNameAndTypeAreTooLong() throws Exception {
        String name = UserTestUtil.createStringWithLength(User.MAX_LENGTH_NAME + 1);
        String type = UserTestUtil.createStringWithLength(User.MAX_LENGTH_TYPE + 1);

        mockMvc.perform(post("/user/add")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param(FORM_FIELD_TYPE, type)
                .param(FORM_FIELD_NAME, name)
                .sessionAttr(UserController.MODEL_ATTRIBUTE, new UserDTO())
        )
                .andExpect(status().isOk())
                .andExpect(view().name(UserController.VIEW_ADD))
                .andExpect(forwardedUrl("/WEB-INF/jsp/user/add.jsp"))
                .andExpect(model().attributeHasFieldErrors(UserController.MODEL_ATTRIBUTE, "name"))
                .andExpect(model().attributeHasFieldErrors(UserController.MODEL_ATTRIBUTE, "type"))
                .andExpect(model().attribute(UserController.MODEL_ATTRIBUTE, hasProperty("id", nullValue())))
                .andExpect(model().attribute(UserController.MODEL_ATTRIBUTE, hasProperty("type", is(type))))
                .andExpect(model().attribute(UserController.MODEL_ATTRIBUTE, hasProperty("name", is(name))));
    }

    @Test
    @ExpectedDatabase(value="userData-add-expected.xml", assertionMode = DatabaseAssertionMode.NON_STRICT)
    public void add() throws Exception {
        String expectedRedirectViewPath = UserTestUtil.createRedirectViewPath(UserController.REQUEST_MAPPING_VIEW);

        mockMvc.perform(post("/user/add")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param(FORM_FIELD_TYPE, "type")
                .param(FORM_FIELD_NAME, "name")
                .sessionAttr(UserController.MODEL_ATTRIBUTE, new UserDTO())
        )
                .andExpect(status().isOk())
                .andExpect(view().name(expectedRedirectViewPath))
               // .andExpect(model().attribute(UserController.PARAMETER_ID, is("4")))
                .andExpect(flash().attribute(UserController.FLASH_MESSAGE_KEY_FEEDBACK, is("User entry: name was added.")));
    }

    @Test
    @ExpectedDatabase("userData.xml")
    public void findAll() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name(UserController.VIEW_LIST))
                .andExpect(forwardedUrl("/WEB-INF/jsp/user/list.jsp"))
                .andExpect(model().attribute(UserController.MODEL_ATTRIBUTE_LIST, hasSize(2)))
                .andExpect(model().attribute(UserController.MODEL_ATTRIBUTE_LIST, hasItem(
                        allOf(
                                hasProperty("id", is(1L)),
                                hasProperty("type", is("Lorem ipsum")),
                                hasProperty("name", is("Foo"))
                        )
                )))
                .andExpect(model().attribute(UserController.MODEL_ATTRIBUTE_LIST, hasItem(
                        allOf(
                                hasProperty("id", is(2L)),
                                hasProperty("type", is("Lorem ipsum")),
                                hasProperty("name", is("Bar"))
                        )
                )));
    }

    @Test
    @ExpectedDatabase("userData.xml")
    public void findById() throws Exception {
        mockMvc.perform(get("/user/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(view().name(UserController.VIEW_VIEW))
                .andExpect(forwardedUrl("/WEB-INF/jsp/user/view.jsp"))
                .andExpect(model().attribute(UserController.MODEL_ATTRIBUTE, hasProperty("id", is(1L))))
                .andExpect(model().attribute(UserController.MODEL_ATTRIBUTE, hasProperty("type", is("Lorem ipsum"))))
                .andExpect(model().attribute(UserController.MODEL_ATTRIBUTE, hasProperty("name", is("Foo"))));
    }

    @Test
    @ExpectedDatabase("userData.xml")
    public void findByIdWhenIsNotFound() throws Exception {
        mockMvc.perform(get("/user/{id}", 3L))
                .andExpect(status().isNotFound())
                .andExpect(view().name(ErrorController.VIEW_NOT_FOUND))
                .andExpect(forwardedUrl("/WEB-INF/jsp/error/404.jsp"));
    }

    @Test
    @ExpectedDatabase("userData-delete-expected.xml")
    public void deleteById() throws Exception {
        String expectedRedirectViewPath = UserTestUtil.createRedirectViewPath(UserController.REQUEST_MAPPING_LIST);
        mockMvc.perform(get("/user/delete/{id}", 2L))
                .andExpect(status().isOk())
                .andExpect(view().name(expectedRedirectViewPath))
                .andExpect(flash().attribute(UserController.FLASH_MESSAGE_KEY_FEEDBACK, is("User entry: Bar was deleted.")));
    }

    @Test
    @ExpectedDatabase("userData.xml")
    public void deleteByIdWhenIsNotFound() throws Exception {
        mockMvc.perform(get("/user/delete/{id}", 3L))
                .andExpect(status().isNotFound())
                .andExpect(view().name(ErrorController.VIEW_NOT_FOUND))
                .andExpect(forwardedUrl("/WEB-INF/jsp/error/404.jsp"));
    }

    @Test
    @ExpectedDatabase("userData.xml")
    public void showUpdateForm() throws Exception {
        mockMvc.perform(get("/user/update/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(view().name(UserController.VIEW_UPDATE))
                .andExpect(forwardedUrl("/WEB-INF/jsp/user/update.jsp"))
                .andExpect(model().attribute(UserController.MODEL_ATTRIBUTE, hasProperty("id", is(1L))))
                .andExpect(model().attribute(UserController.MODEL_ATTRIBUTE, hasProperty("type", is("Lorem ipsum"))))
                .andExpect(model().attribute(UserController.MODEL_ATTRIBUTE, hasProperty("name", is("Foo"))));
    }

    @Test
    @ExpectedDatabase("userData.xml")
    public void showUpdateFormWhenIsNotFound() throws Exception {
        mockMvc.perform(get("/user/update/{id}", 3L))
                .andExpect(status().isNotFound())
                .andExpect(view().name(ErrorController.VIEW_NOT_FOUND))
                .andExpect(forwardedUrl("/WEB-INF/jsp/error/404.jsp"));
    }

    @Test
    @ExpectedDatabase("userData.xml")
    public void updateEmpty() throws Exception {
        mockMvc.perform(post("/user/update")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param(FORM_FIELD_ID, "1")
                .sessionAttr(UserController.MODEL_ATTRIBUTE, new UserDTO())
        )
                .andExpect(status().isOk())
                .andExpect(view().name(UserController.VIEW_UPDATE))
                .andExpect(forwardedUrl("/WEB-INF/jsp/user/update.jsp"))
                .andExpect(model().attributeHasFieldErrors(UserController.MODEL_ATTRIBUTE, "name"))
                .andExpect(model().attribute(UserController.MODEL_ATTRIBUTE, hasProperty("id", is(1L))))
                .andExpect(model().attribute(UserController.MODEL_ATTRIBUTE, hasProperty("type", isEmptyOrNullString())))
                .andExpect(model().attribute(UserController.MODEL_ATTRIBUTE, hasProperty("name", isEmptyOrNullString())));
    }

    @Test
    @ExpectedDatabase("userData.xml")
    public void updateWhenNameAndTypeAreTooLong() throws Exception {
        String name = UserTestUtil.createStringWithLength(User.MAX_LENGTH_NAME + 1);
        String type = UserTestUtil.createStringWithLength(User.MAX_LENGTH_TYPE + 1);

        mockMvc.perform(post("/user/update")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param(FORM_FIELD_TYPE, type)
                .param(FORM_FIELD_ID, "1")
                .param(FORM_FIELD_NAME, name)
                .sessionAttr(UserController.MODEL_ATTRIBUTE, new UserDTO())
        )
                .andExpect(status().isOk())
                .andExpect(view().name(UserController.VIEW_UPDATE))
                .andExpect(forwardedUrl("/WEB-INF/jsp/user/update.jsp"))
                .andExpect(model().attributeHasFieldErrors(UserController.MODEL_ATTRIBUTE, "name"))
                .andExpect(model().attributeHasFieldErrors(UserController.MODEL_ATTRIBUTE, "type"))
                .andExpect(model().attribute(UserController.MODEL_ATTRIBUTE, hasProperty("id", is(1L))))
                .andExpect(model().attribute(UserController.MODEL_ATTRIBUTE, hasProperty("type", is(type))))
                .andExpect(model().attribute(UserController.MODEL_ATTRIBUTE, hasProperty("name", is(name))));
    }

    @Test
    @ExpectedDatabase(value="userData-update-expected.xml", assertionMode = DatabaseAssertionMode.NON_STRICT)
    public void update() throws Exception {
        String expectedRedirectViewPath = UserTestUtil.createRedirectViewPath(UserController.REQUEST_MAPPING_VIEW);

        mockMvc.perform(post("/user/update")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param(FORM_FIELD_TYPE, "type")
                .param(FORM_FIELD_ID, "1")
                .param(FORM_FIELD_NAME, "name")
                .sessionAttr(UserController.MODEL_ATTRIBUTE, new UserDTO())
        )
                .andExpect(status().isOk())
                .andExpect(view().name(expectedRedirectViewPath))
                .andExpect(model().attribute(UserController.PARAMETER_ID, is("1")))
                .andExpect(flash().attribute(UserController.FLASH_MESSAGE_KEY_FEEDBACK, is("User entry: name was updated.")));
    }

    @Test
    @ExpectedDatabase("userData.xml")
    public void updateWhenIsNotFound() throws Exception {
        mockMvc.perform(post("/user/update")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param(FORM_FIELD_TYPE, "type")
                .param(FORM_FIELD_ID, "3")
                .param(FORM_FIELD_NAME, "name")
                .sessionAttr(UserController.MODEL_ATTRIBUTE, new UserDTO())
        )
                .andExpect(status().isNotFound())
                .andExpect(view().name(ErrorController.VIEW_NOT_FOUND))
                .andExpect(forwardedUrl("/WEB-INF/jsp/error/404.jsp"));
    }
}

