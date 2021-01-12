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
import ro.sapientia2015.story.CommonTestUtil;
import ro.sapientia2015.story.dto.UserDTO;
import ro.sapientia2015.story.model.User;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = WebContextLoader.class, classes = {ExampleApplicationContext.class})
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class,
    DirtiesContextTestExecutionListener.class,
    TransactionalTestExecutionListener.class,
    DbUnitTestExecutionListener.class })
@DatabaseSetup("storyData.xml")
public class ITUserControllerTest {
	
	private static final String FORM_FIELD_USERNAME = "username";
    private static final String FORM_FIELD_PASSWORD = "password";

	@Resource
    private WebApplicationContext webApplicationContext;
	
	private MockMvc mockMvc;

    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders.webApplicationContextSetup(webApplicationContext)
                .build();
    }
    
    @Test
    @ExpectedDatabase("storyData.xml")
    public void showAddForm() throws Exception {
        mockMvc.perform(get("/user/add"))
                .andExpect(status().isOk())
                .andExpect(view().name(UserController.VIEW_ADD))
                .andExpect(forwardedUrl("/WEB-INF/jsp/user/add.jsp"))
                .andExpect(model().attribute(UserController.MODEL_ATTRIBUTE, hasProperty("id", nullValue())))
                .andExpect(model().attribute(UserController.MODEL_ATTRIBUTE, hasProperty("username", isEmptyOrNullString())))
                .andExpect(model().attribute(UserController.MODEL_ATTRIBUTE, hasProperty("password", isEmptyOrNullString())));
    }
    
    @Test
    @ExpectedDatabase("storyData.xml")
    public void addEmpty() throws Exception {
        mockMvc.perform(post("/user/add")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .sessionAttr(UserController.MODEL_ATTRIBUTE, new UserDTO())
        )
                .andExpect(status().isOk())
                .andExpect(view().name(UserController.VIEW_ADD))
                .andExpect(forwardedUrl("/WEB-INF/jsp/user/add.jsp"))
                .andExpect(model().attributeHasFieldErrors(UserController.MODEL_ATTRIBUTE, "username"))
                .andExpect(model().attributeHasFieldErrors(UserController.MODEL_ATTRIBUTE, "password"))
                .andExpect(model().attribute(UserController.MODEL_ATTRIBUTE, hasProperty("id", nullValue())))
                .andExpect(model().attribute(UserController.MODEL_ATTRIBUTE, hasProperty("username", isEmptyOrNullString())))
                .andExpect(model().attribute(UserController.MODEL_ATTRIBUTE, hasProperty("password", isEmptyOrNullString())));
    }
    
    @Test
    @ExpectedDatabase("storyData.xml")
    public void addWhenUsernameAndPasswordAreTooLong() throws Exception {
        String username = CommonTestUtil.createStringWithLength(User.MAX_LENGTH_USERNAME + 1);
        String password = CommonTestUtil.createStringWithLength(User.MAX_LENGTH_PASSWORD + 1);

        mockMvc.perform(post("/user/add")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param(FORM_FIELD_USERNAME, username)
                .param(FORM_FIELD_PASSWORD, password)
                .sessionAttr(UserController.MODEL_ATTRIBUTE, new UserDTO())
        )
                .andExpect(status().isOk())
                .andExpect(view().name(UserController.VIEW_ADD))
                .andExpect(forwardedUrl("/WEB-INF/jsp/user/add.jsp"))
                .andExpect(model().attributeHasFieldErrors(UserController.MODEL_ATTRIBUTE, "username"))
                .andExpect(model().attributeHasFieldErrors(UserController.MODEL_ATTRIBUTE, "password"))
                .andExpect(model().attribute(UserController.MODEL_ATTRIBUTE, hasProperty("id", nullValue())))
                .andExpect(model().attribute(UserController.MODEL_ATTRIBUTE, hasProperty("username", is(username))))
                .andExpect(model().attribute(UserController.MODEL_ATTRIBUTE, hasProperty("password", is(password))));
    }
    
    @Test
    @ExpectedDatabase(value="storyData-adduser-expected.xml", assertionMode = DatabaseAssertionMode.NON_STRICT)
    public void add() throws Exception {
        String expectedRedirectViewPath = CommonTestUtil.createRedirectViewPath(UserController.REQUEST_MAPPING_VIEW);

        mockMvc.perform(post("/user/add")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param(FORM_FIELD_USERNAME, "username")
                .param(FORM_FIELD_PASSWORD, "password")
                .sessionAttr(UserController.MODEL_ATTRIBUTE, new UserDTO())
        )
                .andExpect(status().isOk())
                .andExpect(view().name(expectedRedirectViewPath))
                .andExpect(flash().attribute(UserController.FLASH_MESSAGE_KEY_FEEDBACK, is("User entry: username was added.")));
    }
    
}
