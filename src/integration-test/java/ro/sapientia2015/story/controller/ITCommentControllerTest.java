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
import ro.sapientia2015.story.CommentTestUtil;
import ro.sapientia2015.story.StoryTestUtil;
import ro.sapientia2015.story.dto.CommentDTO;
import ro.sapientia2015.story.dto.StoryDTO;
import ro.sapientia2015.story.model.Comment;
import ro.sapientia2015.story.model.Story;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = WebContextLoader.class, classes = { ExampleApplicationContext.class })
//@ContextConfiguration(loader = WebContextLoader.class, locations = {"classpath:exampleApplicationContext.xml"})
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class, DirtiesContextTestExecutionListener.class,
		TransactionalTestExecutionListener.class, DbUnitTestExecutionListener.class })
@DatabaseSetup("storyData.xml")
public class ITCommentControllerTest {

	private static final String FORM_FIELD_ID = "id";
	private static final String FORM_FIELD_MESSAGE = "message";
	private static final String FORM_FIELD_STORY = "storyId";

	@Resource
	private WebApplicationContext webApplicationContext;

	private MockMvc mockMvc;

	@Before
	public void setUp() {
		mockMvc = MockMvcBuilders.webApplicationContextSetup(webApplicationContext).build();
	}

	@Test
	@ExpectedDatabase("storyData.xml")
	public void showAddForm() throws Exception {
		mockMvc.perform(get("/story/1/comment/add")).andExpect(status().isOk())
				.andExpect(view().name(CommentController.VIEW_ADD))
				.andExpect(forwardedUrl("/WEB-INF/jsp/comment/add.jsp"))
				.andExpect(model().attribute(CommentController.MODEL_ATTRIBUTE, hasProperty("id", nullValue())))
				.andExpect(model().attribute(CommentController.MODEL_ATTRIBUTE,
						hasProperty("message", isEmptyOrNullString())))
				.andExpect(model().attribute(CommentController.MODEL_ATTRIBUTE, hasProperty("storyId", is(1L))))
				.andExpect(model().attribute(CommentController.STORY_ID, is(1L)));
	}

	@Test
	@ExpectedDatabase("storyData.xml")
	public void addEmpty() throws Exception {
		CommentDTO input = new CommentDTO();
		input.setStoryId(1L);

		mockMvc.perform(post("/story/1/comment/add").contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.sessionAttr(CommentController.MODEL_ATTRIBUTE, input)).andExpect(status().isOk())
				.andExpect(view().name(CommentController.VIEW_ADD))
				.andExpect(forwardedUrl("/WEB-INF/jsp/comment/add.jsp"))
				.andExpect(model().attributeHasFieldErrors(CommentController.MODEL_ATTRIBUTE, "message"))
				.andExpect(model().attribute(CommentController.MODEL_ATTRIBUTE, hasProperty("id", nullValue())))
				.andExpect(model().attribute(CommentController.MODEL_ATTRIBUTE,
						hasProperty("message", isEmptyOrNullString())))
				.andExpect(model().attribute(CommentController.MODEL_ATTRIBUTE, hasProperty("storyId", is(1L))));
	}

	@Test
	@ExpectedDatabase("storyData.xml")
	public void addWhenMessageAreTooLong() throws Exception {
		String message = CommentTestUtil.createStringWithLength(Comment.MAX_LENGTH_MESSAGE + 1);
		CommentDTO input = new CommentDTO();
		input.setStoryId(1L);

		mockMvc.perform(post("/story/1/comment/add").contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.param(FORM_FIELD_MESSAGE, message).param(FORM_FIELD_STORY, "1")
				.sessionAttr(CommentController.MODEL_ATTRIBUTE, new CommentDTO())).andExpect(status().isOk())
				.andExpect(view().name(CommentController.VIEW_ADD))
				.andExpect(forwardedUrl("/WEB-INF/jsp/comment/add.jsp"))
				.andExpect(model().attributeHasFieldErrors(CommentController.MODEL_ATTRIBUTE, "message"))
				.andExpect(model().attribute(CommentController.MODEL_ATTRIBUTE, hasProperty("id", nullValue())))
				.andExpect(model().attribute(CommentController.MODEL_ATTRIBUTE, hasProperty("message", is(message))))
				.andExpect(model().attribute(CommentController.MODEL_ATTRIBUTE, hasProperty("storyId", is(1L))));
	}

	@Test
	@ExpectedDatabase(value = "commentData-add-expected.xml", assertionMode = DatabaseAssertionMode.NON_STRICT)
	public void add() throws Exception {
		String expectedRedirectViewPath = CommentTestUtil
				.createRedirectViewPath(CommentController.REQUEST_MAPPING_STORY_VIEW);

		mockMvc.perform(post("/story/1/comment/add").contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.param(FORM_FIELD_MESSAGE, "message")
				.param(FORM_FIELD_STORY, "1")
				.sessionAttr(CommentController.MODEL_ATTRIBUTE, new CommentDTO()))
				.andExpect(status().isOk())
				.andExpect(view().name(expectedRedirectViewPath))
				.andExpect(model().attribute(CommentController.STORY_ID, is("1")));
	}

	@Test
	@ExpectedDatabase(value = "commentData-delete-expected.xml")
	public void deleteById() throws Exception {
		String expectedRedirectViewPath = CommentTestUtil.createRedirectViewPath(CommentController.REQUEST_MAPPING_STORY_VIEW);
		mockMvc.perform(get("/story/1/comment/delete/{id}", 1L))
				.andExpect(status().isOk())
				.andExpect(view().name(expectedRedirectViewPath))
				.andExpect(model().attribute(CommentController.STORY_ID, is("1")));
	}

	@Test
	@ExpectedDatabase("storyData.xml")
	public void deleteByIdWhenIsNotFound() throws Exception {
		mockMvc.perform(get("/story/1/comment/delete/{id}", 100L)).andExpect(status().isNotFound())
				.andExpect(view().name(ErrorController.VIEW_NOT_FOUND))
				.andExpect(forwardedUrl("/WEB-INF/jsp/error/404.jsp"));
	}

}
