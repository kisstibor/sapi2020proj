package ro.sapientia2015.story.controller;

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
import ro.sapientia2015.story.EpicTestUtil;
import ro.sapientia2015.story.dto.EpicDTO;
import ro.sapientia2015.story.model.Epic;

/**
 * This test uses the annotation based application context configuration.
 * @author Hunor Szatmari
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = WebContextLoader.class, classes = {ExampleApplicationContext.class})
//@ContextConfiguration(loader = WebContextLoader.class, locations = {"classpath:exampleApplicationContext.xml"})
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class,
        DirtiesContextTestExecutionListener.class,
        TransactionalTestExecutionListener.class,
        DbUnitTestExecutionListener.class })
@DatabaseSetup("epicData.xml")
public class ITEpicControllerTest {
	   private static final String FORM_FIELD_DESCRIPTION = "description";
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
	    @ExpectedDatabase(value="epicData.xml" , assertionMode = DatabaseAssertionMode.NON_STRICT)
	    public void showAddForm() throws Exception {
	        mockMvc.perform(get("/epic/add"))
	                .andExpect(status().isOk())
	                .andExpect(view().name(EpicController.VIEW_ADD))
	                .andExpect(forwardedUrl("/WEB-INF/jsp/epic/add.jsp"))
	                .andExpect(model().attribute(EpicController.MODEL_ATTRIBUTE, hasProperty("id", nullValue())))
	                .andExpect(model().attribute(EpicController.MODEL_ATTRIBUTE, hasProperty("description", isEmptyOrNullString())))
	                .andExpect(model().attribute(EpicController.MODEL_ATTRIBUTE, hasProperty("title", isEmptyOrNullString())));
	    }

	    @Test
	    @ExpectedDatabase(value="epicData.xml" , assertionMode = DatabaseAssertionMode.NON_STRICT)
	    public void addEmpty() throws Exception {
	        mockMvc.perform(post("/epic/add")
	                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
	                .sessionAttr(EpicController.MODEL_ATTRIBUTE, new EpicDTO())
	        )
	                .andExpect(status().isOk())
	                .andExpect(view().name(EpicController.VIEW_ADD))
	                .andExpect(forwardedUrl("/WEB-INF/jsp/epic/add.jsp"))
	                .andExpect(model().attributeHasFieldErrors(EpicController.MODEL_ATTRIBUTE, "title"))
	                .andExpect(model().attribute(EpicController.MODEL_ATTRIBUTE, hasProperty("id", nullValue())))
	                .andExpect(model().attribute(EpicController.MODEL_ATTRIBUTE, hasProperty("description", isEmptyOrNullString())))
	                .andExpect(model().attribute(EpicController.MODEL_ATTRIBUTE, hasProperty("title", isEmptyOrNullString())));
	    }

	    @Test
	    @ExpectedDatabase(value="epicData.xml" , assertionMode = DatabaseAssertionMode.NON_STRICT)
	    public void addWhenTitleAndDescriptionAreTooLong() throws Exception {
	        String title = EpicTestUtil.createStringWithLength(Epic.MAX_LENGTH_TITLE + 1);
	        String description = EpicTestUtil.createStringWithLength(Epic.MAX_LENGTH_DESCRIPTION + 1);

	        mockMvc.perform(post("/epic/add")
	                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
	                .param(FORM_FIELD_DESCRIPTION, description)
	                .param(FORM_FIELD_TITLE, title)
	                .sessionAttr(EpicController.MODEL_ATTRIBUTE, new EpicDTO())
	        )
	                .andExpect(status().isOk())
	                .andExpect(view().name(EpicController.VIEW_ADD))
	                .andExpect(forwardedUrl("/WEB-INF/jsp/epic/add.jsp"))
	                .andExpect(model().attributeHasFieldErrors(EpicController.MODEL_ATTRIBUTE, "title"))
	                .andExpect(model().attributeHasFieldErrors(EpicController.MODEL_ATTRIBUTE, "description"))
	                .andExpect(model().attribute(EpicController.MODEL_ATTRIBUTE, hasProperty("id", nullValue())))
	                .andExpect(model().attribute(EpicController.MODEL_ATTRIBUTE, hasProperty("description", is(description))))
	                .andExpect(model().attribute(EpicController.MODEL_ATTRIBUTE, hasProperty("title", is(title))));
	    }

	    @Test
	    @ExpectedDatabase(value="epicData-add-expected.xml", assertionMode = DatabaseAssertionMode.NON_STRICT)
	    public void add() throws Exception {
	        String expectedRedirectViewPath = EpicTestUtil.createRedirectViewPath(EpicController.REQUEST_MAPPING_VIEW);

	        mockMvc.perform(post("/epic/add")
	                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
	                .param(FORM_FIELD_DESCRIPTION, "description")
	                .param(FORM_FIELD_TITLE, "title")
	                .sessionAttr(EpicController.MODEL_ATTRIBUTE, new EpicDTO())
	        )
	                .andExpect(status().isOk())
	                .andExpect(view().name(expectedRedirectViewPath))
	                .andExpect(model().attribute(EpicController.PARAMETER_ID, is("3")))
	                .andExpect(flash().attribute(EpicController.FLASH_MESSAGE_KEY_FEEDBACK, is("Epic entry: title was added.")));
	    }

	    @Test
	    @ExpectedDatabase(value="epicData.xml" , assertionMode = DatabaseAssertionMode.NON_STRICT)
	    public void findAll() throws Exception {
	        mockMvc.perform(get("/epic"))
	                .andExpect(status().isOk())
	                .andExpect(view().name(EpicController.VIEW_LIST))
	                .andExpect(forwardedUrl("/WEB-INF/jsp/epic/list.jsp"))
	                .andExpect(model().attribute(EpicController.MODEL_ATTRIBUTE_LIST, hasSize(2)))
	                .andExpect(model().attribute(EpicController.MODEL_ATTRIBUTE_LIST, hasItem(
	                        allOf(
	                                hasProperty("id", is(1L)),
	                                hasProperty("description", is("Lorem ipsum")),
	                                hasProperty("title", is("Foo"))
	                        )
	                )))
	                .andExpect(model().attribute(EpicController.MODEL_ATTRIBUTE_LIST, hasItem(
	                        allOf(
	                                hasProperty("id", is(2L)),
	                                hasProperty("description", is("Lorem ipsum")),
	                                hasProperty("title", is("Bar"))
	                        )
	                )));
	    }

	    @Test
	    @ExpectedDatabase(value="epicData.xml" , assertionMode = DatabaseAssertionMode.NON_STRICT)
	    public void findById() throws Exception {
	        mockMvc.perform(get("/epic/{id}", 1L))
	                .andExpect(status().isOk())
	                .andExpect(view().name(EpicController.VIEW_VIEW))
	                .andExpect(forwardedUrl("/WEB-INF/jsp/epic/view.jsp"))
	                .andExpect(model().attribute(EpicController.MODEL_ATTRIBUTE, hasProperty("id", is(1L))))
	                .andExpect(model().attribute(EpicController.MODEL_ATTRIBUTE, hasProperty("description", is("Lorem ipsum"))))
	                .andExpect(model().attribute(EpicController.MODEL_ATTRIBUTE, hasProperty("title", is("Foo"))));
	    }

	    @Test
	    @ExpectedDatabase(value="epicData.xml" , assertionMode = DatabaseAssertionMode.NON_STRICT)
	    public void findByIdWhenIsNotFound() throws Exception {
	        mockMvc.perform(get("/epic/{id}", 4L))
	                .andExpect(status().isNotFound())
	                .andExpect(view().name(ErrorController.VIEW_NOT_FOUND))
	                .andExpect(forwardedUrl("/WEB-INF/jsp/error/404.jsp"));
	    }

	    @Test
	    @ExpectedDatabase(value="epicData-delete-expected.xml", assertionMode = DatabaseAssertionMode.NON_STRICT)
	    public void deleteById() throws Exception {
	        String expectedRedirectViewPath = EpicTestUtil.createRedirectViewPath(EpicController.REQUEST_MAPPING_LIST);
	        mockMvc.perform(get("/epic/delete/{id}", 2L))
	                .andExpect(status().isOk())
	                .andExpect(view().name(expectedRedirectViewPath))
	                .andExpect(flash().attribute(EpicController.FLASH_MESSAGE_KEY_FEEDBACK, is("Epic entry: Bar was deleted.")));
	    }

	    @Test
	    @ExpectedDatabase(value="epicData.xml" , assertionMode = DatabaseAssertionMode.NON_STRICT)
	    public void deleteByIdWhenIsNotFound() throws Exception {
	        mockMvc.perform(get("/epic/delete/{id}", 3L))
	                .andExpect(status().isNotFound())
	                .andExpect(view().name(ErrorController.VIEW_NOT_FOUND))
	                .andExpect(forwardedUrl("/WEB-INF/jsp/error/404.jsp"));
	    }

	    @Test
	    @ExpectedDatabase(value="epicData.xml" , assertionMode = DatabaseAssertionMode.NON_STRICT)
	    public void showUpdateForm() throws Exception {
	        mockMvc.perform(get("/epic/update/{id}", 1L))
	                .andExpect(status().isOk())
	                .andExpect(view().name(EpicController.VIEW_UPDATE))
	                .andExpect(forwardedUrl("/WEB-INF/jsp/epic/update.jsp"))
	                .andExpect(model().attribute(EpicController.MODEL_ATTRIBUTE, hasProperty("id", is(1L))))
	                .andExpect(model().attribute(EpicController.MODEL_ATTRIBUTE, hasProperty("description", is("Lorem ipsum"))))
	                .andExpect(model().attribute(EpicController.MODEL_ATTRIBUTE, hasProperty("title", is("Foo"))));
	    }

	    @Test
	    @ExpectedDatabase(value="epicData.xml" , assertionMode = DatabaseAssertionMode.NON_STRICT)
	    public void showUpdateFormWhenIsNotFound() throws Exception {
	        mockMvc.perform(get("/epic/update/{id}", 3L))
	                .andExpect(status().isNotFound())
	                .andExpect(view().name(ErrorController.VIEW_NOT_FOUND))
	                .andExpect(forwardedUrl("/WEB-INF/jsp/error/404.jsp"));
	    }

	    @Test
	    @ExpectedDatabase(value="epicData.xml" , assertionMode = DatabaseAssertionMode.NON_STRICT)
	    public void updateEmpty() throws Exception {
	        mockMvc.perform(post("/epic/update")
	                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
	                .param(FORM_FIELD_ID, "1")
	                .sessionAttr(EpicController.MODEL_ATTRIBUTE, new EpicDTO())
	        )
	                .andExpect(status().isOk())
	                .andExpect(view().name(EpicController.VIEW_UPDATE))
	                .andExpect(forwardedUrl("/WEB-INF/jsp/epic/update.jsp"))
	                .andExpect(model().attributeHasFieldErrors(EpicController.MODEL_ATTRIBUTE, "title"))
	                .andExpect(model().attribute(EpicController.MODEL_ATTRIBUTE, hasProperty("id", is(1L))))
	                .andExpect(model().attribute(EpicController.MODEL_ATTRIBUTE, hasProperty("description", isEmptyOrNullString())))
	                .andExpect(model().attribute(EpicController.MODEL_ATTRIBUTE, hasProperty("title", isEmptyOrNullString())));
	    }

	    @Test
	    @ExpectedDatabase(value="epicData.xml" , assertionMode = DatabaseAssertionMode.NON_STRICT)
	    public void updateWhenTitleAndDescriptionAreTooLong() throws Exception {
	        String title = EpicTestUtil.createStringWithLength(Epic.MAX_LENGTH_TITLE + 1);
	        String description = EpicTestUtil.createStringWithLength(Epic.MAX_LENGTH_DESCRIPTION + 1);

	        mockMvc.perform(post("/epic/update")
	                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
	                .param(FORM_FIELD_DESCRIPTION, description)
	                .param(FORM_FIELD_ID, "1")
	                .param(FORM_FIELD_TITLE, title)
	                .sessionAttr(EpicController.MODEL_ATTRIBUTE, new EpicDTO())
	        )
	                .andExpect(status().isOk())
	                .andExpect(view().name(EpicController.VIEW_UPDATE))
	                .andExpect(forwardedUrl("/WEB-INF/jsp/epic/update.jsp"))
	                .andExpect(model().attributeHasFieldErrors(EpicController.MODEL_ATTRIBUTE, "title"))
	                .andExpect(model().attributeHasFieldErrors(EpicController.MODEL_ATTRIBUTE, "description"))
	                .andExpect(model().attribute(EpicController.MODEL_ATTRIBUTE, hasProperty("id", is(1L))))
	                .andExpect(model().attribute(EpicController.MODEL_ATTRIBUTE, hasProperty("description", is(description))))
	                .andExpect(model().attribute(EpicController.MODEL_ATTRIBUTE, hasProperty("title", is(title))));
	    }

	    @Test
	    @ExpectedDatabase(value="epicData-update-expected.xml", assertionMode = DatabaseAssertionMode.NON_STRICT)
	    public void update() throws Exception {
	        String expectedRedirectViewPath = EpicTestUtil.createRedirectViewPath(EpicController.REQUEST_MAPPING_VIEW);

	        mockMvc.perform(post("/epic/update")
	                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
	                .param(FORM_FIELD_DESCRIPTION, "description")
	                .param(FORM_FIELD_ID, "1")
	                .param(FORM_FIELD_TITLE, "title")
	                .sessionAttr(EpicController.MODEL_ATTRIBUTE, new EpicDTO())
	        )
	                .andExpect(status().isOk())
	                .andExpect(view().name(expectedRedirectViewPath))
	                .andExpect(model().attribute(EpicController.PARAMETER_ID, is("1")))
	                .andExpect(flash().attribute(EpicController.FLASH_MESSAGE_KEY_FEEDBACK, is("Epic entry: title was updated.")));
	    }

	    @Test
	    @ExpectedDatabase(value="epicData.xml" , assertionMode = DatabaseAssertionMode.NON_STRICT)
	    public void updateWhenIsNotFound() throws Exception {
	        mockMvc.perform(post("/epic/update")
	                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
	                .param(FORM_FIELD_DESCRIPTION, "description")
	                .param(FORM_FIELD_ID, "3")
	                .param(FORM_FIELD_TITLE, "title")
	                .sessionAttr(EpicController.MODEL_ATTRIBUTE, new EpicDTO())
	        )
	                .andExpect(status().isNotFound())
	                .andExpect(view().name(ErrorController.VIEW_NOT_FOUND))
	                .andExpect(forwardedUrl("/WEB-INF/jsp/error/404.jsp"));
	    }
}
