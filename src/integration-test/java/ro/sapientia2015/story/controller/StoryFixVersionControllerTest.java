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
import ro.sapientia2015.story.FixVersionTestUtil;
import ro.sapientia2015.story.dto.FixVersionDTO;
import ro.sapientia2015.story.model.FixVersion;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = WebContextLoader.class, classes = {ExampleApplicationContext.class})
//@ContextConfiguration(loader = WebContextLoader.class, locations = {"classpath:exampleApplicationContext.xml"})
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class,
        DirtiesContextTestExecutionListener.class,
        TransactionalTestExecutionListener.class,
        DbUnitTestExecutionListener.class })
@DatabaseSetup("fixVersionData.xml")
public class StoryFixVersionControllerTest {

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
    @ExpectedDatabase(assertionMode=DatabaseAssertionMode.NON_STRICT, value="fixVersionData.xml")
    public void showAddForm() throws Exception {
        mockMvc.perform(get("/fixVersion/add"))
                .andExpect(status().isOk())
                .andExpect(view().name(FixVersionController.VIEW_ADD))
                .andExpect(forwardedUrl("/WEB-INF/jsp/fixVersion/add.jsp"))
                .andExpect(model().attribute(FixVersionController.MODEL_ATTRIBUTE, hasProperty("id", nullValue())))
                .andExpect(model().attribute(FixVersionController.MODEL_ATTRIBUTE, hasProperty("name", isEmptyOrNullString())));
    }
    
    @Test
    @ExpectedDatabase(assertionMode=DatabaseAssertionMode.NON_STRICT, value="fixVersionData.xml")
    public void addEmpty() throws Exception {
        mockMvc.perform(post("/fixVersion/add")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .sessionAttr(FixVersionController.MODEL_ATTRIBUTE, new FixVersionDTO())
        )
                .andExpect(status().isOk())
                .andExpect(view().name(FixVersionController.VIEW_ADD))
                .andExpect(forwardedUrl("/WEB-INF/jsp/fixVersion/add.jsp"))
                .andExpect(model().attributeHasFieldErrors(FixVersionController.MODEL_ATTRIBUTE, "name"))
                .andExpect(model().attribute(FixVersionController.MODEL_ATTRIBUTE, hasProperty("id", nullValue())))
                .andExpect(model().attribute(FixVersionController.MODEL_ATTRIBUTE, hasProperty("name", isEmptyOrNullString())));
    }
    
    @Test
    @ExpectedDatabase(assertionMode=DatabaseAssertionMode.NON_STRICT, value="fixVersionData.xml")
    public void addWhenNameTooLong() throws Exception {
        String name = FixVersionTestUtil.createStringWithLength(FixVersion.MAX_LENGTH_NAME + 1);

        mockMvc.perform(post("/fixVersion/add")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param(FORM_FIELD_NAME, name)
                .sessionAttr(FixVersionController.MODEL_ATTRIBUTE, new FixVersionDTO())
        )
                .andExpect(status().isOk())
                .andExpect(view().name(FixVersionController.VIEW_ADD))
                .andExpect(forwardedUrl("/WEB-INF/jsp/fixVersion/add.jsp"))
                .andExpect(model().attributeHasFieldErrors(FixVersionController.MODEL_ATTRIBUTE, "name"))
                .andExpect(model().attribute(FixVersionController.MODEL_ATTRIBUTE, hasProperty("id", nullValue())))
                .andExpect(model().attribute(FixVersionController.MODEL_ATTRIBUTE, hasProperty("name", is(name))));
    }
    
    @Test
    @ExpectedDatabase(value="fixVersionData-add-expected.xml", assertionMode = DatabaseAssertionMode.NON_STRICT)
    public void add() throws Exception {
        String expectedRedirectViewPath = FixVersionTestUtil.createRedirectViewPath(FixVersionController.REQUEST_MAPPING_VIEW);

        mockMvc.perform(post("/fixVersion/add")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param(FORM_FIELD_NAME, "name")
                .sessionAttr(FixVersionController.MODEL_ATTRIBUTE, new FixVersionDTO())
        )
                .andExpect(status().isOk())
                .andExpect(view().name(expectedRedirectViewPath))
                .andExpect(model().attribute(FixVersionController.PARAMETER_ID, is("3")))
                .andExpect(flash().attribute(FixVersionController.FLASH_MESSAGE_KEY_FEEDBACK, is("Fix Version entry: name was added.")));
    }
    
    @Test
    @ExpectedDatabase(assertionMode=DatabaseAssertionMode.NON_STRICT, value="fixVersionData.xml")
    public void findAll() throws Exception {
        mockMvc.perform(get("/fixVersions"))
                .andExpect(status().isOk())
                .andExpect(view().name(FixVersionController.VIEW_LIST))
                .andExpect(forwardedUrl("/WEB-INF/jsp/fixVersion/list.jsp"))
                .andExpect(model().attribute(FixVersionController.MODEL_ATTRIBUTE_LIST, hasSize(2)))
                .andExpect(model().attribute(FixVersionController.MODEL_ATTRIBUTE_LIST, hasItem(
                        allOf(
                                hasProperty("id", is(1L)),
                                hasProperty("name", is("Foo"))
                        )
                )))
                .andExpect(model().attribute(FixVersionController.MODEL_ATTRIBUTE_LIST, hasItem(
                        allOf(
                                hasProperty("id", is(2L)),
                                hasProperty("name", is("Bar"))
                        )
                )));
    }
    
    @Test
    @ExpectedDatabase(assertionMode=DatabaseAssertionMode.NON_STRICT, value="fixVersionData.xml")
    public void findById() throws Exception {
        mockMvc.perform(get("/fixVersion/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(view().name(FixVersionController.VIEW_VIEW))
                .andExpect(forwardedUrl("/WEB-INF/jsp/fixVersion/view.jsp"))
                .andExpect(model().attribute(FixVersionController.MODEL_ATTRIBUTE, hasProperty("id", is(1L))))
                .andExpect(model().attribute(FixVersionController.MODEL_ATTRIBUTE, hasProperty("name", is("Foo"))));
    }
    
    @Test
    @ExpectedDatabase(assertionMode=DatabaseAssertionMode.NON_STRICT, value="fixVersionData.xml")
    public void findByIdWhenIsNotFound() throws Exception {
        mockMvc.perform(get("/fixVersion/{id}", 3L))
                .andExpect(status().isNotFound())
                .andExpect(view().name(ErrorController.VIEW_NOT_FOUND))
                .andExpect(forwardedUrl("/WEB-INF/jsp/error/404.jsp"));
    }
    
    @Test
    @ExpectedDatabase(assertionMode=DatabaseAssertionMode.NON_STRICT, value="fixVersionData-delete-expected.xml")
    public void deleteById() throws Exception {
        String expectedRedirectViewPath = FixVersionTestUtil.createRedirectViewPath(FixVersionController.REQUEST_MAPPING_LIST);
        mockMvc.perform(get("/fixVersion/delete/{id}", 2L))
                .andExpect(status().isOk())
                .andExpect(view().name(expectedRedirectViewPath))
                .andExpect(flash().attribute(FixVersionController.FLASH_MESSAGE_KEY_FEEDBACK, is("Fix Version entry: Bar was deleted.")));
    }
    
    @Test
    @ExpectedDatabase(assertionMode=DatabaseAssertionMode.NON_STRICT, value="fixVersionData.xml")
    public void deleteByIdWhenIsNotFound() throws Exception {
        mockMvc.perform(get("/fixVersion/delete/{id}", 3L))
                .andExpect(status().isNotFound())
                .andExpect(view().name(ErrorController.VIEW_NOT_FOUND))
                .andExpect(forwardedUrl("/WEB-INF/jsp/error/404.jsp"));
    }

    @Test
    @ExpectedDatabase(assertionMode=DatabaseAssertionMode.NON_STRICT, value="fixVersionData.xml")
    public void showUpdateForm() throws Exception {
        mockMvc.perform(get("/fixVersion/update/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(view().name(FixVersionController.VIEW_UPDATE))
                .andExpect(forwardedUrl("/WEB-INF/jsp/fixVersion/update.jsp"))
                .andExpect(model().attribute(FixVersionController.MODEL_ATTRIBUTE, hasProperty("id", is(1L))))
                .andExpect(model().attribute(FixVersionController.MODEL_ATTRIBUTE, hasProperty("name", is("Foo"))));
    }

    @Test
    @ExpectedDatabase(assertionMode=DatabaseAssertionMode.NON_STRICT, value="fixVersionData.xml")
    public void showUpdateFormWhenIsNotFound() throws Exception {
        mockMvc.perform(get("/fixVersion/update/{id}", 3L))
                .andExpect(status().isNotFound())
                .andExpect(view().name(ErrorController.VIEW_NOT_FOUND))
                .andExpect(forwardedUrl("/WEB-INF/jsp/error/404.jsp"));
    }
    
    @Test
    @ExpectedDatabase(assertionMode=DatabaseAssertionMode.NON_STRICT, value="fixVersionData.xml")
    public void updateEmpty() throws Exception {
        mockMvc.perform(post("/fixVersion/update")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param(FORM_FIELD_ID, "1")
                .sessionAttr(FixVersionController.MODEL_ATTRIBUTE, new FixVersionDTO())
        )
                .andExpect(status().isOk())
                .andExpect(view().name(FixVersionController.VIEW_UPDATE))
                .andExpect(forwardedUrl("/WEB-INF/jsp/fixVersion/update.jsp"))
                .andExpect(model().attributeHasFieldErrors(FixVersionController.MODEL_ATTRIBUTE, "name"))
                .andExpect(model().attribute(FixVersionController.MODEL_ATTRIBUTE, hasProperty("id", is(1L))))
                .andExpect(model().attribute(FixVersionController.MODEL_ATTRIBUTE, hasProperty("name", isEmptyOrNullString())));
    }
    
    @Test
    @ExpectedDatabase(assertionMode=DatabaseAssertionMode.NON_STRICT, value="fixVersionData.xml")
    public void updateWhenTitleAndDescriptionAreTooLong() throws Exception {
        String name = FixVersionTestUtil.createStringWithLength(FixVersion.MAX_LENGTH_NAME + 1);

        mockMvc.perform(post("/fixVersion/update")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param(FORM_FIELD_ID, "1")
                .param(FORM_FIELD_NAME, name)
                .sessionAttr(FixVersionController.MODEL_ATTRIBUTE, new FixVersionDTO())
        )
                .andExpect(status().isOk())
                .andExpect(view().name(FixVersionController.VIEW_UPDATE))
                .andExpect(forwardedUrl("/WEB-INF/jsp/fixVersion/update.jsp"))
                .andExpect(model().attributeHasFieldErrors(FixVersionController.MODEL_ATTRIBUTE, "name"))
                .andExpect(model().attribute(FixVersionController.MODEL_ATTRIBUTE, hasProperty("id", is(1L))))
                .andExpect(model().attribute(FixVersionController.MODEL_ATTRIBUTE, hasProperty("name", is(name))));
    }
    
    @Test
    @ExpectedDatabase(value="fixVersionData-update-expected.xml", assertionMode = DatabaseAssertionMode.NON_STRICT)
    public void update() throws Exception {
        String expectedRedirectViewPath = FixVersionTestUtil.createRedirectViewPath(FixVersionController.REQUEST_MAPPING_VIEW);

        mockMvc.perform(post("/fixVersion/update")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param(FORM_FIELD_ID, "1")
                .param(FORM_FIELD_NAME, "name")
                .sessionAttr(FixVersionController.MODEL_ATTRIBUTE, new FixVersionDTO())
        )
                .andExpect(status().isOk())
                .andExpect(view().name(expectedRedirectViewPath))
                .andExpect(model().attribute(FixVersionController.PARAMETER_ID, is("1")))
                .andExpect(flash().attribute(FixVersionController.FLASH_MESSAGE_KEY_FEEDBACK, is("Fix Version entry: name was updated.")));
    }
    
    @Test
    @ExpectedDatabase(assertionMode=DatabaseAssertionMode.NON_STRICT, value="fixVersionData.xml")
    public void updateWhenIsNotFound() throws Exception {
        mockMvc.perform(post("/fixVersion/update")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param(FORM_FIELD_ID, "3")
                .param(FORM_FIELD_NAME, "name")
                .sessionAttr(FixVersionController.MODEL_ATTRIBUTE, new FixVersionDTO())
        )
                .andExpect(status().isNotFound())
                .andExpect(view().name(ErrorController.VIEW_NOT_FOUND))
                .andExpect(forwardedUrl("/WEB-INF/jsp/error/404.jsp"));
    }
}
