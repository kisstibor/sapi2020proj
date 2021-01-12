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
import ro.sapientia2015.story.LogTestUtil;
import ro.sapientia2015.story.dto.LogDTO;
import ro.sapientia2015.story.model.Log;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = WebContextLoader.class, classes = {ExampleApplicationContext.class})
//@ContextConfiguration(loader = WebContextLoader.class, locations = {"classpath:exampleApplicationContext.xml"})
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class,
        DirtiesContextTestExecutionListener.class,
        TransactionalTestExecutionListener.class,
        DbUnitTestExecutionListener.class })
@DatabaseSetup("logData.xml")
public class ITLogControllerTest {

    private static final String FORM_FIELD_DESCRIPTION = "description";
    private static final String FORM_FIELD_ID = "id";
    private static final String FORM_FIELD_TITLE = "title";
    private static final String FORM_FIELD_ASSIGNTO = "assignto";
    private static final String FORM_FIELD_STATUS = "status";
    private static final String FORM_FIELD_DOC = "doc";

    @Resource
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders.webApplicationContextSetup(webApplicationContext)
                .build();
    }

    @Test
    @ExpectedDatabase(assertionMode=DatabaseAssertionMode.NON_STRICT, value= "logData.xml")
    public void showAddForm() throws Exception {
        mockMvc.perform(get("/log/add"))
                .andExpect(status().isOk())
                .andExpect(view().name(LogController.VIEW_ADD))
                .andExpect(forwardedUrl("/WEB-INF/jsp/log/add.jsp"))
                .andExpect(model().attribute(LogController.MODEL_ATTRIBUTE, hasProperty("id", nullValue())))
                .andExpect(model().attribute(LogController.MODEL_ATTRIBUTE, hasProperty("description", isEmptyOrNullString())))
                .andExpect(model().attribute(LogController.MODEL_ATTRIBUTE, hasProperty("title", isEmptyOrNullString())));
    }

    @Test
    @ExpectedDatabase(assertionMode=DatabaseAssertionMode.NON_STRICT, value= "logData.xml")
    public void addEmpty() throws Exception {
        mockMvc.perform(post("/log/add")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .sessionAttr(LogController.MODEL_ATTRIBUTE, new LogDTO())
        )
                .andExpect(status().isOk())
                .andExpect(view().name(LogController.VIEW_ADD))
                .andExpect(forwardedUrl("/WEB-INF/jsp/log/add.jsp"))
                .andExpect(model().attributeHasFieldErrors(LogController.MODEL_ATTRIBUTE, "title"))
                .andExpect(model().attribute(LogController.MODEL_ATTRIBUTE, hasProperty("id", nullValue())))
                .andExpect(model().attribute(LogController.MODEL_ATTRIBUTE, hasProperty("description", isEmptyOrNullString())))
                .andExpect(model().attribute(LogController.MODEL_ATTRIBUTE, hasProperty("title", isEmptyOrNullString())))
                .andExpect(model().attribute(LogController.MODEL_ATTRIBUTE, hasProperty("assignTo", isEmptyOrNullString())))
                .andExpect(model().attribute(LogController.MODEL_ATTRIBUTE, hasProperty("status", isEmptyOrNullString())));
    }

    @Test
    @ExpectedDatabase(assertionMode=DatabaseAssertionMode.NON_STRICT, value= "logData.xml")
    public void addWhenTitleAndDescriptionAreTooLong() throws Exception {
        String title = LogTestUtil.createStringWithLength(Log.MAX_LENGTH_TITLE + 1);
        String description = LogTestUtil.createStringWithLength(Log.MAX_LENGTH_DESCRIPTION + 1);
        String assignTo = LogTestUtil.createStringWithLength(Log.MAX_LENGTH_ASSIGNTO + 1);
        String status = LogTestUtil.createStringWithLength(Log.MAX_LENGTH_STATUS + 1);

        mockMvc.perform(post("/log/add")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param(FORM_FIELD_DESCRIPTION, description)
                .param(FORM_FIELD_TITLE, title)
                .param(FORM_FIELD_ASSIGNTO, assignTo)
                .param(FORM_FIELD_STATUS, status)
                .sessionAttr(LogController.MODEL_ATTRIBUTE, new LogDTO())
        )
                .andExpect(status().isOk())
                .andExpect(view().name(LogController.VIEW_ADD))
                .andExpect(forwardedUrl("/WEB-INF/jsp/log/add.jsp"))
                .andExpect(model().attributeHasFieldErrors(LogController.MODEL_ATTRIBUTE, "title"))
                .andExpect(model().attributeHasFieldErrors(LogController.MODEL_ATTRIBUTE, "description"))
                .andExpect(model().attribute(LogController.MODEL_ATTRIBUTE, hasProperty("id", nullValue())))
                .andExpect(model().attribute(LogController.MODEL_ATTRIBUTE, hasProperty("description", is(description))))
               .andExpect(model().attribute(LogController.MODEL_ATTRIBUTE, hasProperty("title", is(title))))
             //   .andExpect(model().attribute(LogController.MODEL_ATTRIBUTE, hasProperty("assignto", is(assignTo))))
                .andExpect(model().attribute(LogController.MODEL_ATTRIBUTE, hasProperty("status", is(status))))
                ;
    }

    @Test
    @ExpectedDatabase(value="logData-add-expected.xml", assertionMode = DatabaseAssertionMode.NON_STRICT)
    public void add() throws Exception {
       String expectedRedirectViewPath = LogTestUtil.createRedirectViewPath(LogController.REQUEST_MAPPING_VIEW);
    
        mockMvc.perform(post("/log/add")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param(FORM_FIELD_TITLE, "title")
                .param(FORM_FIELD_DESCRIPTION, "desc")
                .param(FORM_FIELD_ASSIGNTO, "assignto")
                .param(FORM_FIELD_STATUS, "status")
                .param(FORM_FIELD_DOC, "doc")
                .sessionAttr(LogController.MODEL_ATTRIBUTE, new LogDTO())
        )
                .andExpect(status().isOk())
                .andExpect(view().name(expectedRedirectViewPath))
                .andExpect(model().attribute(LogController.PARAMETER_ID, is("3")))
                .andExpect(flash().attribute(LogController.FLASH_MESSAGE_KEY_FEEDBACK, is("Log entry: title was added.")));
    }

    @Test
    @ExpectedDatabase(assertionMode=DatabaseAssertionMode.NON_STRICT, value= "logData.xml")
    public void findAll() throws Exception {
        mockMvc.perform(get("/log"))
                .andExpect(status().isOk())
                .andExpect(view().name(LogController.VIEW_LIST))
                .andExpect(forwardedUrl("/WEB-INF/jsp/log/list.jsp"))
                .andExpect(model().attribute(LogController.MODEL_ATTRIBUTE_LIST, hasSize(2)))
                .andExpect(model().attribute(LogController.MODEL_ATTRIBUTE_LIST, hasItem(
                        allOf(
                                hasProperty("id", is(1L)),
                                hasProperty("description", is("desc")),
                                hasProperty("title", is("title1")),
                                hasProperty("status", is("s1")),
                                hasProperty("assignTo", is("a1"))
                        )
                )))
                .andExpect(model().attribute(LogController.MODEL_ATTRIBUTE_LIST, hasItem(
                        allOf(
                                hasProperty("id", is(2L)),
                                hasProperty("description", is("desc")),
                                hasProperty("title", is("title2")),
                                hasProperty("status", is("s2")),                          
                                hasProperty("assignTo", is("a2"))
                        )
                )));
    }

    @Test
    //@ExpectedDatabase("logData.xml")
    @ExpectedDatabase(assertionMode=DatabaseAssertionMode.NON_STRICT, value= "logData.xml")
    public void findById() throws Exception {
        mockMvc.perform(get("/log/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(view().name(LogController.VIEW_VIEW))
                .andExpect(forwardedUrl("/WEB-INF/jsp/log/view.jsp"))
                .andExpect(model().attribute(LogController.MODEL_ATTRIBUTE, hasProperty("id", is(1L))))
                .andExpect(model().attribute(LogController.MODEL_ATTRIBUTE, hasProperty("description", is("desc"))))
                .andExpect(model().attribute(LogController.MODEL_ATTRIBUTE, hasProperty("title", is("title1"))))
        .andExpect(model().attribute(LogController.MODEL_ATTRIBUTE, hasProperty("assignTo", is("a1"))))
        .andExpect(model().attribute(LogController.MODEL_ATTRIBUTE, hasProperty("status", is("s1"))));
    }

    @Test
    @ExpectedDatabase(assertionMode=DatabaseAssertionMode.NON_STRICT, value= "logData.xml")
    public void findByIdWhenIsNotFound() throws Exception {
        mockMvc.perform(get("/log/{id}", 3L))
                .andExpect(status().isNotFound())
                .andExpect(view().name(ErrorController.VIEW_NOT_FOUND))
                .andExpect(forwardedUrl("/WEB-INF/jsp/error/404.jsp"));
    }

    @Test
    @ExpectedDatabase(assertionMode=DatabaseAssertionMode.NON_STRICT, value= "logData-delete-expected.xml")
    public void deleteById() throws Exception {
        String expectedRedirectViewPath = LogTestUtil.createRedirectViewPath(LogController.REQUEST_MAPPING_LIST);
        mockMvc.perform(get("/log/delete/{id}", 2L))
                .andExpect(status().isOk())
                .andExpect(view().name(expectedRedirectViewPath))
                .andExpect(flash().attribute(LogController.FLASH_MESSAGE_KEY_FEEDBACK, is("Log entry: title2 was deleted.")));
    }

    @Test    
    @ExpectedDatabase(assertionMode=DatabaseAssertionMode.NON_STRICT, value= "logData.xml")
    public void deleteByIdWhenIsNotFound() throws Exception {
        mockMvc.perform(get("/log/delete/{id}", 3L))
                .andExpect(status().isNotFound())
                .andExpect(view().name(ErrorController.VIEW_NOT_FOUND))
                .andExpect(forwardedUrl("/WEB-INF/jsp/error/404.jsp"));
    }

    @Test
    @ExpectedDatabase(assertionMode=DatabaseAssertionMode.NON_STRICT, value= "logData.xml")
    public void showUpdateForm() throws Exception {
        mockMvc.perform(get("/log/update/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(view().name(LogController.VIEW_UPDATE))
                .andExpect(forwardedUrl("/WEB-INF/jsp/log/update.jsp"))
                .andExpect(model().attribute(LogController.MODEL_ATTRIBUTE, hasProperty("id", is(1L))))
                .andExpect(model().attribute(LogController.MODEL_ATTRIBUTE, hasProperty("description", is("desc"))))
                .andExpect(model().attribute(LogController.MODEL_ATTRIBUTE, hasProperty("title", is("title1"))));
    }

    @Test
    @ExpectedDatabase(assertionMode=DatabaseAssertionMode.NON_STRICT, value= "logData.xml")
    public void showUpdateFormWhenIsNotFound() throws Exception {
        mockMvc.perform(get("/log/update/{id}", 3L))
                .andExpect(status().isNotFound())
                .andExpect(view().name(ErrorController.VIEW_NOT_FOUND))
                .andExpect(forwardedUrl("/WEB-INF/jsp/error/404.jsp"));
    }

    @Test
    @ExpectedDatabase(assertionMode=DatabaseAssertionMode.NON_STRICT, value= "logData.xml")
    public void updateEmpty() throws Exception {
        mockMvc.perform(post("/log/update")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param(FORM_FIELD_ID, "1")
                .sessionAttr(LogController.MODEL_ATTRIBUTE, new LogDTO())
        )
                .andExpect(status().isOk())
                .andExpect(view().name(LogController.VIEW_UPDATE))
                .andExpect(forwardedUrl("/WEB-INF/jsp/log/update.jsp"))
                .andExpect(model().attributeHasFieldErrors(LogController.MODEL_ATTRIBUTE, "title"))
                .andExpect(model().attribute(LogController.MODEL_ATTRIBUTE, hasProperty("id", is(1L))))
                .andExpect(model().attribute(LogController.MODEL_ATTRIBUTE, hasProperty("description", isEmptyOrNullString())))
                .andExpect(model().attribute(LogController.MODEL_ATTRIBUTE, hasProperty("title", isEmptyOrNullString())));
    }

    @Test
    @ExpectedDatabase(assertionMode=DatabaseAssertionMode.NON_STRICT, value= "logData.xml")
    public void updateWhenTitleAndDescriptionAreTooLong() throws Exception {
        String title = LogTestUtil.createStringWithLength(Log.MAX_LENGTH_TITLE + 1);
        String description = LogTestUtil.createStringWithLength(Log.MAX_LENGTH_DESCRIPTION + 1);

        mockMvc.perform(post("/log/update")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param(FORM_FIELD_DESCRIPTION, description)
                .param(FORM_FIELD_ID, "1")
                .param(FORM_FIELD_TITLE, title)
                .sessionAttr(LogController.MODEL_ATTRIBUTE, new LogDTO())
        )
                .andExpect(status().isOk())
                .andExpect(view().name(LogController.VIEW_UPDATE))
                .andExpect(forwardedUrl("/WEB-INF/jsp/log/update.jsp"))
                .andExpect(model().attributeHasFieldErrors(LogController.MODEL_ATTRIBUTE, "title"))
                .andExpect(model().attributeHasFieldErrors(LogController.MODEL_ATTRIBUTE, "description"))
                .andExpect(model().attribute(LogController.MODEL_ATTRIBUTE, hasProperty("id", is(1L))))
                .andExpect(model().attribute(LogController.MODEL_ATTRIBUTE, hasProperty("description", is(description))))
                .andExpect(model().attribute(LogController.MODEL_ATTRIBUTE, hasProperty("title", is(title))));
    }

    @Test
    @ExpectedDatabase(value="logData-update-expected.xml", assertionMode = DatabaseAssertionMode.NON_STRICT)
    public void update() throws Exception {
        String expectedRedirectViewPath = LogTestUtil.createRedirectViewPath(LogController.REQUEST_MAPPING_VIEW);

        mockMvc.perform(post("/log/update")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param(FORM_FIELD_DESCRIPTION, "description")
                .param(FORM_FIELD_ID, "1")
                .param(FORM_FIELD_TITLE, "title")
                .sessionAttr(LogController.MODEL_ATTRIBUTE, new LogDTO())
        )
                .andExpect(status().isOk())
                .andExpect(view().name(expectedRedirectViewPath))
                .andExpect(model().attribute(LogController.PARAMETER_ID, is("1")))
                .andExpect(flash().attribute(LogController.FLASH_MESSAGE_KEY_FEEDBACK, is("Log entry: title was updated.")));
    }

    @Test
    @ExpectedDatabase(assertionMode=DatabaseAssertionMode.NON_STRICT, value= "logData.xml")
    public void updateWhenIsNotFound() throws Exception {
        mockMvc.perform(post("/log/update")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param(FORM_FIELD_DESCRIPTION, "desc2")
                .param(FORM_FIELD_ID, "4")
                .param(FORM_FIELD_TITLE, "title12")
                .sessionAttr(LogController.MODEL_ATTRIBUTE, new LogDTO())
        )
                .andExpect(status().isNotFound())
                //.andExpect(status().isNotFound())
                .andExpect(view().name(ErrorController.VIEW_NOT_FOUND))
                .andExpect(forwardedUrl("/WEB-INF/jsp/error/404.jsp"));
    }
}

