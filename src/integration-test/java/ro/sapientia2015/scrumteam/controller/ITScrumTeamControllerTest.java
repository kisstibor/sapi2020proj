package ro.sapientia2015.scrumteam.controller;

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
import ro.sapientia2015.scrumteam.ScrumTeamTestUtil;
import ro.sapientia2015.scrumteam.dto.ScrumTeamDTO;
import ro.sapientia2015.scrumteam.model.ScrumTeam;

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

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = WebContextLoader.class, classes = {ExampleApplicationContext.class})
//@ContextConfiguration(loader = WebContextLoader.class, locations = {"classpath:exampleApplicationContext.xml"})
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class,
        DirtiesContextTestExecutionListener.class,
        TransactionalTestExecutionListener.class,
        DbUnitTestExecutionListener.class })
@DatabaseSetup("scrumTeamData.xml")
public class ITScrumTeamControllerTest {

    private static final String FORM_FIELD_ID = "id";
    private static final String FORM_FIELD_NAME = "name";
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
    @ExpectedDatabase("scrumTeamData.xml")
    public void showAddForm() throws Exception {
        mockMvc.perform(get("/scrumteam/add"))
                .andExpect(status().isOk())
                .andExpect(view().name(ScrumTeamController.VIEW_ADD))
                .andExpect(forwardedUrl("/WEB-INF/jsp/scrumteam/add.jsp"))
                .andExpect(model().attribute(ScrumTeamController.MODEL_ATTRIBUTE, hasProperty("id", nullValue())))
                .andExpect(model().attribute(ScrumTeamController.MODEL_ATTRIBUTE, hasProperty("name", isEmptyOrNullString())))
                .andExpect(model().attribute(ScrumTeamController.MODEL_ATTRIBUTE, hasProperty("members", isEmptyOrNullString())));
    }
	
    @Test
    @ExpectedDatabase("scrumTeamData.xml")
    public void addEmpty() throws Exception {
        mockMvc.perform(post("/scrumteam/add")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .sessionAttr(ScrumTeamController.MODEL_ATTRIBUTE, new ScrumTeamDTO())
        )
                .andExpect(status().isOk())
                .andExpect(view().name(ScrumTeamController.VIEW_ADD))
                .andExpect(forwardedUrl("/WEB-INF/jsp/scrumteam/add.jsp"))
                .andExpect(model().attributeHasFieldErrors(ScrumTeamController.MODEL_ATTRIBUTE, "name"))
                .andExpect(model().attribute(ScrumTeamController.MODEL_ATTRIBUTE, hasProperty("id", nullValue())))
                .andExpect(model().attribute(ScrumTeamController.MODEL_ATTRIBUTE, hasProperty("members", isEmptyOrNullString())))
                .andExpect(model().attribute(ScrumTeamController.MODEL_ATTRIBUTE, hasProperty("name", isEmptyOrNullString())));
    }
	
    @Test
    @ExpectedDatabase("scrumTeamData.xml")
    public void addWhenTitleAndDescriptionAreTooLong() throws Exception {
        String name = ScrumTeamTestUtil.createStringWithLength(ScrumTeam.MAX_LENGTH_NAME + 1);
        String members = ScrumTeamTestUtil.createStringWithLength(ScrumTeam.MAX_LENGTH_MEMBERS + 1);

        mockMvc.perform(post("/scrumteam/add")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param(FORM_FIELD_MEMBERS, members)
                .param(FORM_FIELD_NAME, name)
                .sessionAttr(ScrumTeamController.MODEL_ATTRIBUTE, new ScrumTeamDTO())
        )
                .andExpect(status().isOk())
                .andExpect(view().name(ScrumTeamController.VIEW_ADD))
                .andExpect(forwardedUrl("/WEB-INF/jsp/scrumteam/add.jsp"))
                .andExpect(model().attributeHasFieldErrors(ScrumTeamController.MODEL_ATTRIBUTE, "name"))
                .andExpect(model().attributeHasFieldErrors(ScrumTeamController.MODEL_ATTRIBUTE, "members"))
                .andExpect(model().attribute(ScrumTeamController.MODEL_ATTRIBUTE, hasProperty("id", nullValue())))
                .andExpect(model().attribute(ScrumTeamController.MODEL_ATTRIBUTE, hasProperty("members", is(members))))
                .andExpect(model().attribute(ScrumTeamController.MODEL_ATTRIBUTE, hasProperty("name", is(name))));
    }

    @Test
    @ExpectedDatabase(value="scrumTeamData-add-expected.xml", assertionMode = DatabaseAssertionMode.NON_STRICT)
    public void add() throws Exception {
        String expectedRedirectViewPath = ScrumTeamTestUtil.createRedirectViewPath(ScrumTeamController.REQUEST_MAPPING_VIEW);

        mockMvc.perform(post("/scrumteam/add")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param(FORM_FIELD_MEMBERS, "members")
                .param(FORM_FIELD_NAME, "name")
                .sessionAttr(ScrumTeamController.MODEL_ATTRIBUTE, new ScrumTeamDTO())
        )
                .andExpect(status().isOk())
                .andExpect(view().name(expectedRedirectViewPath))
                .andExpect(model().attribute(ScrumTeamController.PARAMETER_ID, is("3")))
                .andExpect(flash().attribute(ScrumTeamController.FLASH_MESSAGE_KEY_FEEDBACK, is("Scrum Team entry: name was added.")));
    }

    @Test
    @ExpectedDatabase("scrumTeamData.xml")
    public void findAll() throws Exception {
        mockMvc.perform(get("/scrumteam"))
                .andExpect(status().isOk())
                .andExpect(view().name(ScrumTeamController.VIEW_LIST))
                .andExpect(forwardedUrl("/WEB-INF/jsp/scrumteam/list.jsp"))
                .andExpect(model().attribute(ScrumTeamController.MODEL_ATTRIBUTE_LIST, hasSize(2)))
                .andExpect(model().attribute(ScrumTeamController.MODEL_ATTRIBUTE_LIST, hasItem(
                        allOf(
                                hasProperty("id", is(1L)),
                                hasProperty("members", is("Lorem ipsum")),
                                hasProperty("name", is("Foo"))
                        )
                )))
                .andExpect(model().attribute(ScrumTeamController.MODEL_ATTRIBUTE_LIST, hasItem(
                        allOf(
                                hasProperty("id", is(2L)),
                                hasProperty("members", is("Lorem ipsum")),
                                hasProperty("name", is("Bar"))
                        )
                )));
    }

    @Test
    @ExpectedDatabase("scrumTeamData.xml")
    public void findById() throws Exception {
        mockMvc.perform(get("/scrumteam/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(view().name(ScrumTeamController.VIEW_VIEW))
                .andExpect(forwardedUrl("/WEB-INF/jsp/scrumteam/view.jsp"))
                .andExpect(model().attribute(ScrumTeamController.MODEL_ATTRIBUTE, hasProperty("id", is(1L))))
                .andExpect(model().attribute(ScrumTeamController.MODEL_ATTRIBUTE, hasProperty("members", is("Lorem ipsum"))))
                .andExpect(model().attribute(ScrumTeamController.MODEL_ATTRIBUTE, hasProperty("name", is("Foo"))));
    }

    @Test
    @ExpectedDatabase("scrumTeamData.xml")
    public void findByIdWhenIsNotFound() throws Exception {
        mockMvc.perform(get("/scrumteam/{id}", 3L))
                .andExpect(status().isNotFound())
                .andExpect(view().name(ErrorController.VIEW_NOT_FOUND))
                .andExpect(forwardedUrl("/WEB-INF/jsp/error/404.jsp"));
    }

    /*
    @Test
    @ExpectedDatabase("scrumTeamData-delete-expected.xml")
    public void deleteById() throws Exception {
        String expectedRedirectViewPath = ScrumTeamTestUtil.createRedirectViewPath(ScrumTeamController.REQUEST_MAPPING_LIST);
        mockMvc.perform(get("/scrumteam/delete/{id}", 2L))
                .andExpect(status().isOk())
                .andExpect(view().name(expectedRedirectViewPath))
                .andExpect(flash().attribute(ScrumTeamController.FLASH_MESSAGE_KEY_FEEDBACK, is("Scrum Team entry: Bar was deleted.")));
    }

    @Test
    @ExpectedDatabase("scrumTeamData.xml")
    public void deleteByIdWhenIsNotFound() throws Exception {
        mockMvc.perform(get("/scrumteam/delete/{id}", 3L))
                .andExpect(status().isNotFound())
                .andExpect(view().name(ErrorController.VIEW_NOT_FOUND))
                .andExpect(forwardedUrl("/WEB-INF/jsp/error/404.jsp"));
    }

    @Test
    @ExpectedDatabase("scrumTeamData.xml")
    public void showUpdateForm() throws Exception {
        mockMvc.perform(get("/scrumteam/update/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(view().name(ScrumTeamController.VIEW_UPDATE))
                .andExpect(forwardedUrl("/WEB-INF/jsp/scrumteam/update.jsp"))
                .andExpect(model().attribute(ScrumTeamController.MODEL_ATTRIBUTE, hasProperty("id", is(1L))))
                .andExpect(model().attribute(ScrumTeamController.MODEL_ATTRIBUTE, hasProperty("members", is("Lorem ipsum"))))
                .andExpect(model().attribute(ScrumTeamController.MODEL_ATTRIBUTE, hasProperty("name", is("Foo"))));
    }
    

    @Test
    @ExpectedDatabase("scrumTeamData.xml")
    public void showUpdateFormWhenIsNotFound() throws Exception {
        mockMvc.perform(get("/scrumteam/update/{id}", 3L))
                .andExpect(status().isNotFound())
                .andExpect(view().name(ErrorController.VIEW_NOT_FOUND))
                .andExpect(forwardedUrl("/WEB-INF/jsp/error/404.jsp"));
    }

    @Test
    @ExpectedDatabase("scrumTeamData.xml")
    public void updateEmpty() throws Exception {
        mockMvc.perform(post("/scrumteam/update")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param(FORM_FIELD_ID, "1")
                .sessionAttr(ScrumTeamController.MODEL_ATTRIBUTE, new ScrumTeamDTO())
        )
                .andExpect(status().isOk())
                .andExpect(view().name(ScrumTeamController.VIEW_UPDATE))
                .andExpect(forwardedUrl("/WEB-INF/jsp/scrumteam/update.jsp"))
                .andExpect(model().attributeHasFieldErrors(ScrumTeamController.MODEL_ATTRIBUTE, "name"))
                .andExpect(model().attribute(ScrumTeamController.MODEL_ATTRIBUTE, hasProperty("id", is(1L))))
                .andExpect(model().attribute(ScrumTeamController.MODEL_ATTRIBUTE, hasProperty("members", isEmptyOrNullString())))
                .andExpect(model().attribute(ScrumTeamController.MODEL_ATTRIBUTE, hasProperty("name", isEmptyOrNullString())));
    }

    @Test
    @ExpectedDatabase("scrumTeamData.xml")
    public void updateWhenTitleAndDescriptionAreTooLong() throws Exception {
        String title = ScrumTeamTestUtil.createStringWithLength(ScrumTeam.MAX_LENGTH_NAME + 1);
        String description = ScrumTeamTestUtil.createStringWithLength(ScrumTeam.MAX_LENGTH_MEMBERS + 1);

        mockMvc.perform(post("/scrumteam/update")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param(FORM_FIELD_MEMBERS, description)
                .param(FORM_FIELD_ID, "1")
                .param(FORM_FIELD_NAME, title)
                .sessionAttr(ScrumTeamController.MODEL_ATTRIBUTE, new ScrumTeamDTO())
        )
                .andExpect(status().isOk())
                .andExpect(view().name(ScrumTeamController.VIEW_UPDATE))
                .andExpect(forwardedUrl("/WEB-INF/jsp/scrumteam/update.jsp"))
                .andExpect(model().attributeHasFieldErrors(ScrumTeamController.MODEL_ATTRIBUTE, "name"))
                .andExpect(model().attributeHasFieldErrors(ScrumTeamController.MODEL_ATTRIBUTE, "members"))
                .andExpect(model().attribute(ScrumTeamController.MODEL_ATTRIBUTE, hasProperty("id", is(1L))))
                .andExpect(model().attribute(ScrumTeamController.MODEL_ATTRIBUTE, hasProperty("members", is(description))))
                .andExpect(model().attribute(ScrumTeamController.MODEL_ATTRIBUTE, hasProperty("name", is(title))));
    }

    @Test
    @ExpectedDatabase(value="scrumTeamData-update-expected.xml", assertionMode = DatabaseAssertionMode.NON_STRICT)
    public void update() throws Exception {
        String expectedRedirectViewPath = ScrumTeamTestUtil.createRedirectViewPath(ScrumTeamController.REQUEST_MAPPING_VIEW);

        mockMvc.perform(post("/scrumteam/update")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param(FORM_FIELD_MEMBERS, "members")
                .param(FORM_FIELD_ID, "1")
                .param(FORM_FIELD_NAME, "name")
                .sessionAttr(ScrumTeamController.MODEL_ATTRIBUTE, new ScrumTeamDTO())
        )
                .andExpect(status().isOk())
                .andExpect(view().name(expectedRedirectViewPath))
                .andExpect(model().attribute(ScrumTeamController.PARAMETER_ID, is("1")))
                .andExpect(flash().attribute(ScrumTeamController.FLASH_MESSAGE_KEY_FEEDBACK, is("Scrum Team entry: name was updated.")));
    }

    @Test
    @ExpectedDatabase("scrumTeamData.xml")
    public void updateWhenIsNotFound() throws Exception {
        mockMvc.perform(post("/scrumteam/update")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param(FORM_FIELD_MEMBERS, "members")
                .param(FORM_FIELD_ID, "3")
                .param(FORM_FIELD_NAME, "name")
                .sessionAttr(ScrumTeamController.MODEL_ATTRIBUTE, new ScrumTeamDTO())
        )
                .andExpect(status().isNotFound())
                .andExpect(view().name(ErrorController.VIEW_NOT_FOUND))
                .andExpect(forwardedUrl("/WEB-INF/jsp/error/404.jsp"));
    }
	*/
	
	
}
