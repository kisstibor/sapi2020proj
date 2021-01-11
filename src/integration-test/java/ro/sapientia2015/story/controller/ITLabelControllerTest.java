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

import ro.sapientia2015.config.ExampleApplicationContext;
import ro.sapientia2015.context.WebContextLoader;
import ro.sapientia2015.story.controller.LabelController;
import ro.sapientia2015.story.dto.LabelDTO;

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
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class,
        DirtiesContextTestExecutionListener.class,
        TransactionalTestExecutionListener.class,
        DbUnitTestExecutionListener.class })
@DatabaseSetup("storyData.xml")
public class ITLabelControllerTest {

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
    @ExpectedDatabase("storyData.xml")
    public void showAddForm() throws Exception {
        mockMvc.perform(get("/label/add"))
                .andExpect(status().isOk())
                .andExpect(view().name(LabelController.VIEW_ADD))
                .andExpect(forwardedUrl("/WEB-INF/jsp/label/add.jsp"))
                .andExpect(model().attribute(LabelController.MODEL_ATTRIBUTE, hasProperty("id", nullValue())))
                .andExpect(model().attribute(LabelController.MODEL_ATTRIBUTE, hasProperty("title", isEmptyOrNullString())));
    }

    @Test
    public void add() throws Exception {
        mockMvc.perform(post("/label/add")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param(FORM_FIELD_TITLE, "Cimke")
                .sessionAttr(LabelController.MODEL_ATTRIBUTE, new LabelDTO())
        )
                .andExpect(status().isOk())
                .andExpect(view().name("redirect:/label"))
                .andExpect(flash().attribute(LabelController.FLASH_MESSAGE_KEY_FEEDBACK, is("Label: Cimke was added")));
    }

    @Test
    @ExpectedDatabase("storyData.xml")
    public void listLabels() throws Exception {
        mockMvc.perform(get("/label"))
                .andExpect(status().isOk())
                .andExpect(view().name(LabelController.VIEW_LIST))
                .andExpect(forwardedUrl("/WEB-INF/jsp/label/list.jsp"))
                .andExpect(model().attribute(LabelController.MODEL_ATTRIBUTE_LIST, hasSize(2)))
                .andExpect(model().attribute(LabelController.MODEL_ATTRIBUTE_LIST, hasItem(
                        allOf(
                                hasProperty("id", is(1L)),
                                hasProperty("title", is("Cimke 1"))
                        )
                )))
                .andExpect(model().attribute(LabelController.MODEL_ATTRIBUTE_LIST, hasItem(
                        allOf(
                                hasProperty("id", is(2L)),
                                hasProperty("title", is("Cimke 2"))
                        )
                )));
    }
}

