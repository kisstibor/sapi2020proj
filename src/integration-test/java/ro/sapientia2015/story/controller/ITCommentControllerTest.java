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
import org.springframework.test.web.server.MvcResult;
import org.springframework.test.web.server.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.ExpectedDatabase;
import com.github.springtestdbunit.assertion.DatabaseAssertionMode;

import ro.sapientia2015.common.controller.ErrorController;
import ro.sapientia2015.config.ExampleApplicationContext;
import ro.sapientia2015.context.WebContextLoader;
import ro.sapientia2015.story.CommentTestUtil;
import ro.sapientia2015.story.StoryTestUtil;
import ro.sapientia2015.story.dto.CommentDTO;
import ro.sapientia2015.story.dto.StoryDTO;
import ro.sapientia2015.story.model.Comment;
import ro.sapientia2015.story.model.Story;
import ro.sapientia2015.story.model.StoryStatus;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = WebContextLoader.class, classes = {ExampleApplicationContext.class})
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class,
        DirtiesContextTestExecutionListener.class,
        TransactionalTestExecutionListener.class,
        DbUnitTestExecutionListener.class })
@DatabaseSetup("commentData.xml")
public class ITCommentControllerTest {

    private static final String FORM_FIELD_MESSAGE = "message";
    private static final String FORM_FIELD_ID = "id";

    @Resource
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders.webApplicationContextSetup(webApplicationContext)
                .build();
    }

    @Test
    @ExpectedDatabase("commentData.xml")
    public void showAddForm() throws Exception {
        mockMvc.perform(get("/comment/add/{id}",1L))
                .andExpect(status().isOk())
                .andExpect(view().name(CommentController.VIEW_ADD))
                .andExpect(forwardedUrl("/WEB-INF/jsp/comment/add.jsp"))
                .andExpect(model().attribute(CommentController.MODEL_ATTRIBUTE, hasProperty("id", nullValue())))
                .andExpect(model().attribute(CommentController.MODEL_ATTRIBUTE, hasProperty("message", isEmptyOrNullString())));
    }

    @Test
    @ExpectedDatabase("commentData.xml")
    public void addEmpty() throws Exception {
        mockMvc.perform(post("/comment/add")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("storyId", "1")
                .sessionAttr(CommentController.MODEL_ATTRIBUTE, new CommentDTO())
        )
                .andExpect(status().isOk())
                .andExpect(view().name(CommentController.VIEW_ADD))
                .andExpect(forwardedUrl("/WEB-INF/jsp/comment/add.jsp"))
                .andExpect(model().attribute(CommentController.MODEL_ATTRIBUTE, hasProperty("id", nullValue())))
                .andExpect(model().attribute(CommentController.MODEL_ATTRIBUTE, hasProperty("storyId", is(1L))))
                .andExpect(model().attribute(CommentController.MODEL_ATTRIBUTE, hasProperty("message", isEmptyOrNullString())));
    }

    @Test
    @ExpectedDatabase("commentData.xml")
    public void addWhenMessageIsTooLong() throws Exception {
        String message = CommentTestUtil.createStringWithLength(Comment.MAX_LENGTH_DESCRIPTION + 1);

        mockMvc.perform(post("/comment/add")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param(FORM_FIELD_MESSAGE, message)
                .param("storyId", "1")
                .sessionAttr(CommentController.MODEL_ATTRIBUTE, new CommentDTO())
                
        )
                .andExpect(status().isOk())
                .andExpect(view().name(CommentController.VIEW_ADD))
                .andExpect(forwardedUrl("/WEB-INF/jsp/comment/add.jsp"))
                .andExpect(model().attributeHasFieldErrors(CommentController.MODEL_ATTRIBUTE, "message"))
                .andExpect(model().attribute(CommentController.MODEL_ATTRIBUTE, hasProperty("id", nullValue())))
                .andExpect(model().attribute(CommentController.MODEL_ATTRIBUTE, hasProperty("storyId", is(1L))))
                .andExpect(model().attribute(CommentController.MODEL_ATTRIBUTE, hasProperty("message", is(message))));
    }
 
    
    @Test
    @ExpectedDatabase(value="commentData-add-expected.xml", assertionMode = DatabaseAssertionMode.NON_STRICT)
    public void add() throws Exception {
        String expectedRedirectViewPath = StoryTestUtil.createRedirectViewPath(StoryController.REQUEST_MAPPING_VIEW);

        mockMvc.perform(post("/comment/add")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param(FORM_FIELD_MESSAGE, "test text for message")
                .param("storyId", "1")
                .sessionAttr(CommentController.MODEL_ATTRIBUTE, new CommentDTO())
        )
                .andExpect(status().isOk())
                .andExpect(view().name(expectedRedirectViewPath))
                .andExpect(model().attribute(StoryController.PARAMETER_ID, is("1")))
                .andExpect(flash().attribute(StoryController.FLASH_MESSAGE_KEY_FEEDBACK, is("feedback.message.comment.added")));
    }

   
    @Test
    @ExpectedDatabase("commentData-delete-expected.xml")
    public void deleteById() throws Exception {
        String expectedRedirectViewPath = StoryTestUtil.createRedirectViewPath(StoryController.REQUEST_MAPPING_VIEW);
        mockMvc.perform(get("/comment/delete/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(view().name(expectedRedirectViewPath))
                .andExpect(flash().attribute(StoryController.FLASH_MESSAGE_KEY_FEEDBACK, is("feedback.message.comment.deleted")));
    }

    @Test
    @ExpectedDatabase("commentData.xml")
    public void deleteByIdWhenIsNotFound() throws Exception {
        mockMvc.perform(get("/comment/delete/{id}", 3L))
                .andExpect(status().isNotFound())
                .andExpect(view().name(ErrorController.VIEW_NOT_FOUND))
                .andExpect(forwardedUrl("/WEB-INF/jsp/error/404.jsp"));
    }

    @Test
    @ExpectedDatabase("commentData.xml")
    public void showUpdateForm() throws Exception {
        mockMvc.perform(get("/comment/update/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(view().name(CommentController.VIEW_UPDATE))
                .andExpect(forwardedUrl("/WEB-INF/jsp/comment/update.jsp"))
                .andExpect(model().attribute(CommentController.MODEL_ATTRIBUTE, hasProperty("id", is(1L))))
                .andExpect(model().attribute(CommentController.MODEL_ATTRIBUTE, hasProperty("storyId", is(1L))))
                .andExpect(model().attribute(CommentController.MODEL_ATTRIBUTE, hasProperty("message", is("message test"))));
    }

    @Test
    @ExpectedDatabase("commentData.xml")
    public void showUpdateFormWhenIsNotFound() throws Exception {
        mockMvc.perform(get("/comment/update/{id}", 3L))
                .andExpect(status().isNotFound())
                .andExpect(view().name(ErrorController.VIEW_NOT_FOUND))
                .andExpect(forwardedUrl("/WEB-INF/jsp/error/404.jsp"));
    }

    @Test
    @ExpectedDatabase("commentData.xml")
    public void updateEmpty() throws Exception {
        mockMvc.perform(post("/comment/update")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param(FORM_FIELD_ID, "1")
                .param("storyId", "1")
                .sessionAttr(CommentController.MODEL_ATTRIBUTE, new CommentDTO())
        )
                .andExpect(status().isOk())
                .andExpect(view().name(CommentController.VIEW_UPDATE))
                .andExpect(forwardedUrl("/WEB-INF/jsp/comment/update.jsp"))
                .andExpect(model().attributeHasFieldErrors(CommentController.MODEL_ATTRIBUTE, "message"))
                .andExpect(model().attribute(CommentController.MODEL_ATTRIBUTE, hasProperty("id", is(1L))))
                .andExpect(model().attribute(CommentController.MODEL_ATTRIBUTE, hasProperty("storyId", is(1L))))
                .andExpect(model().attribute(CommentController.MODEL_ATTRIBUTE, hasProperty("message", isEmptyOrNullString())));
    }

    @Test
    @ExpectedDatabase("commentData.xml")
    public void updateWhenMessageIsTooLong() throws Exception {
    	String message = CommentTestUtil.createStringWithLength(Comment.MAX_LENGTH_DESCRIPTION + 1);

        mockMvc.perform(post("/comment/update")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param(FORM_FIELD_MESSAGE, message)
                .param(FORM_FIELD_ID, "1")
                .param("storyId", "1")
                .sessionAttr(CommentController.MODEL_ATTRIBUTE, new CommentDTO())
        )
                .andExpect(status().isOk())
                .andExpect(view().name(CommentController.VIEW_UPDATE))
                .andExpect(forwardedUrl("/WEB-INF/jsp/comment/update.jsp"))
                .andExpect(model().attributeHasFieldErrors(CommentController.MODEL_ATTRIBUTE, "message"))
                .andExpect(model().attribute(CommentController.MODEL_ATTRIBUTE, hasProperty("id", is(1L))))
                .andExpect(model().attribute(CommentController.MODEL_ATTRIBUTE, hasProperty("message", is(message))))
                .andExpect(model().attribute(CommentController.MODEL_ATTRIBUTE, hasProperty("storyId", is(1L))));
    }

    @Test
    @ExpectedDatabase(value="commentData-update-expected.xml", assertionMode = DatabaseAssertionMode.NON_STRICT)
    public void update() throws Exception {
        String expectedRedirectViewPath = StoryTestUtil.createRedirectViewPath(StoryController.REQUEST_MAPPING_VIEW);

        mockMvc.perform(post("/comment/update")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param(FORM_FIELD_MESSAGE, "updated message test updated")
                .param(FORM_FIELD_ID, "1")
                .param("storyId", "1")
                .sessionAttr(CommentController.MODEL_ATTRIBUTE, new CommentDTO())
        )
                .andExpect(status().isOk())
                .andExpect(view().name(expectedRedirectViewPath))
                .andExpect(model().attribute(CommentController.PARAMETER_ID, is("1")))
                .andExpect(flash().attribute(CommentController.FLASH_MESSAGE_KEY_FEEDBACK, is("feedback.message.comment.updated")));
    }

    @Test
    @ExpectedDatabase("commentData.xml")
    public void updateWhenIsNotFound() throws Exception {
        mockMvc.perform(post("/comment/update")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param(FORM_FIELD_MESSAGE, "message")
                .param(FORM_FIELD_ID, "3")
                .param("storyId", "1")
                .sessionAttr(CommentController.MODEL_ATTRIBUTE, new CommentDTO())
        )
                .andExpect(status().isNotFound())
                .andExpect(view().name(ErrorController.VIEW_NOT_FOUND))
                .andExpect(forwardedUrl("/WEB-INF/jsp/error/404.jsp"));
    }
}
