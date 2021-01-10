package ro.sapientia2015.story.controller;

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
import org.springframework.test.util.ReflectionTestUtils;
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
import ro.sapientia2015.story.dto.DailyDTO;
import ro.sapientia2015.story.model.Daily;

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

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = WebContextLoader.class, classes = { ExampleApplicationContext.class })
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class, DirtiesContextTestExecutionListener.class,
		TransactionalTestExecutionListener.class, DbUnitTestExecutionListener.class })
@DatabaseSetup("dailyData.xml")
public class ITDailyControllerTest {
	private static final String FORM_FIELD_DESCRIPTION = "description";
	private static final String FORM_FIELD_ID = "id";
	private static final String FORM_FIELD_TITLE = "title";
	private static final String FORM_FIELD_DATEE = "datee";
	private static final String FORM_FIELD_DURATION = "duration";

	private String TITLE = "title";
	private String DESCRIPTION = "description";
	private String UPDATED_DURATION = "34";
	private String UPDATED_DATEE = "17-06-2021";

	private Daily daily;

	@Resource
	private WebApplicationContext webApplicationContext;

	private MockMvc mockMvc;

	@Before
	public void setUp() {
		mockMvc = MockMvcBuilders.webApplicationContextSetup(webApplicationContext).build();
		setUpTestingObj();
	}

	private void setUpTestingObj() {

		daily = Daily.getBuilder(TITLE).datee(UPDATED_DATEE).description(DESCRIPTION).duration(UPDATED_DURATION)
				.build();
	}

	@Test
	@ExpectedDatabase(value = "dailyData.xml", assertionMode = DatabaseAssertionMode.NON_STRICT)
	public void showForm() throws Exception {
		mockMvc.perform(get("/daily/add")).andExpect(status().isOk()).andExpect(view().name(DailyController.VIEW_ADD))
				.andExpect(forwardedUrl("/WEB-INF/jsp/daily/add.jsp"))
				.andExpect(model().attribute(DailyController.MODEL_ATTRIBUTE, hasProperty("id", nullValue())))
				.andExpect(model().attribute(DailyController.MODEL_ATTRIBUTE,
						hasProperty("description", isEmptyOrNullString())))
				.andExpect(
						model().attribute(DailyController.MODEL_ATTRIBUTE, hasProperty("title", isEmptyOrNullString())))
				.andExpect(
						model().attribute(DailyController.MODEL_ATTRIBUTE, hasProperty("datee", isEmptyOrNullString())))
				.andExpect(model().attribute(DailyController.MODEL_ATTRIBUTE,
						hasProperty("title", isEmptyOrNullString())));
	}

	@Test
	@ExpectedDatabase(value = "dailyData.xml", assertionMode = DatabaseAssertionMode.NON_STRICT)
	public void addWhenDurationIsTooLong() throws Exception {

		ReflectionTestUtils.setField(daily, "duration", "1234");

		mockMvc.perform(post("/daily/add").contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.param(FORM_FIELD_DATEE, daily.getDatee()).param(FORM_FIELD_DURATION, daily.getDuration())
				.param(FORM_FIELD_TITLE, daily.getTitle()).param(FORM_FIELD_DESCRIPTION, daily.getDescription())
				.sessionAttr(DailyController.MODEL_ATTRIBUTE, new DailyDTO())).andExpect(status().isOk())
				.andExpect(view().name(DailyController.VIEW_ADD)).andExpect(forwardedUrl("/WEB-INF/jsp/daily/add.jsp"))
				.andExpect(model().attributeHasFieldErrors(DailyController.MODEL_ATTRIBUTE, "duration"))
				.andExpect(model().attribute(DailyController.MODEL_ATTRIBUTE, hasProperty("id", nullValue())))
				.andExpect(model().attribute(DailyController.MODEL_ATTRIBUTE,
						hasProperty("description", is(daily.getDescription()))))
				.andExpect(
						model().attribute(DailyController.MODEL_ATTRIBUTE, hasProperty("title", is(daily.getTitle()))))
				.andExpect(
						model().attribute(DailyController.MODEL_ATTRIBUTE, hasProperty("datee", is(daily.getDatee()))));
	}

	@Test
	@ExpectedDatabase(value = "dailyData-add-expected.xml", assertionMode = DatabaseAssertionMode.NON_STRICT)
	public void add() throws Exception {

		ReflectionTestUtils.setField(daily, "description", "daily4");
		ReflectionTestUtils.setField(daily, "duration", "30");
		ReflectionTestUtils.setField(daily, "datee", "10-01-2021");
		ReflectionTestUtils.setField(daily, "title", "daily title4");

		String expectedView = createRedirectViewPath(DailyController.REQUEST_MAPPING_VIEW);
		mockMvc.perform(post("/daily/add").contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.param(FORM_FIELD_DATEE, daily.getDatee()).param(FORM_FIELD_DURATION, daily.getDuration())
				.param(FORM_FIELD_TITLE, daily.getTitle()).param(FORM_FIELD_DESCRIPTION, daily.getDescription())
				.sessionAttr(DailyController.MODEL_ATTRIBUTE, new DailyDTO())).andExpect(status().isOk())
				.andExpect(view().name(expectedView))
				.andExpect(model().attribute(DailyController.PARAMETER_ID, is("4"))).andExpect(flash()
						.attribute(DailyController.FLASH_MESSAGE_KEY_FEEDBACK, is("Daily: daily title4 was added.")));
	}

	@Test
	@ExpectedDatabase(value = "dailyData-update-expected.xml", assertionMode = DatabaseAssertionMode.NON_STRICT)
	public void updateDaily() throws Exception {
		ReflectionTestUtils.setField(daily, "description", "description");
		ReflectionTestUtils.setField(daily, "duration", "30");
		ReflectionTestUtils.setField(daily, "datee", "10-01-2021");
		ReflectionTestUtils.setField(daily, "title", "title");
		ReflectionTestUtils.setField(daily, "id", 3L);

		String expected = createRedirectViewPath(DailyController.REQUEST_MAPPING_VIEW);

		mockMvc.perform(post("/daily/update").contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.param(FORM_FIELD_DATEE, daily.getDatee()).param(FORM_FIELD_DURATION, daily.getDuration())
				.param(FORM_FIELD_TITLE, daily.getTitle()).param(FORM_FIELD_ID, "3")
				.param(FORM_FIELD_DESCRIPTION, daily.getDescription())
				.sessionAttr(DailyController.MODEL_ATTRIBUTE, new DailyDTO())).andExpect(status().isOk())
				.andExpect(view().name(expected)).andExpect(model().attribute(DailyController.PARAMETER_ID, is("3")))
				.andExpect(
						flash().attribute(DailyController.FLASH_MESSAGE_KEY_FEEDBACK, is("Daily: title was updated.")));
	}

	@Test
	@ExpectedDatabase(value = "dailyData.xml", assertionMode = DatabaseAssertionMode.NON_STRICT)
	public void updateDailyNotFound() throws Exception {
		ReflectionTestUtils.setField(daily, "description", "daily4");
		ReflectionTestUtils.setField(daily, "duration", "30");
		ReflectionTestUtils.setField(daily, "datee", "10-01-2021");
		ReflectionTestUtils.setField(daily, "title", "daily title4");
		ReflectionTestUtils.setField(daily, "id", 4L);

		mockMvc.perform(post("/daily/update").contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.param(FORM_FIELD_DATEE, daily.getDatee()).param(FORM_FIELD_DURATION, daily.getDuration())
				.param(FORM_FIELD_TITLE, daily.getTitle()).param(FORM_FIELD_ID, "4")
				.param(FORM_FIELD_DESCRIPTION, daily.getDescription())
				.sessionAttr(DailyController.MODEL_ATTRIBUTE, new DailyDTO())).andExpect(status().isNotFound())
				.andExpect(view().name(ErrorController.VIEW_NOT_FOUND));
	}

	@Test
	@ExpectedDatabase(value = "dailyData.xml", assertionMode = DatabaseAssertionMode.NON_STRICT)
	public void showUpdateDailyForm() throws Exception {
		mockMvc.perform(get("/daily/update/{id}", 1L)).andExpect(status().isOk())
				.andExpect(view().name(DailyController.VIEW_UPDATE))
				.andExpect(forwardedUrl("/WEB-INF/jsp/daily/update.jsp"))
				.andExpect(model().attribute(DailyController.MODEL_ATTRIBUTE, hasProperty("id", is(1L))))
				.andExpect(model().attribute(DailyController.MODEL_ATTRIBUTE, hasProperty("duration", is("30"))))
				.andExpect(model().attribute(DailyController.MODEL_ATTRIBUTE, hasProperty("datee", is("10-01-2021"))))
				.andExpect(model().attribute(DailyController.MODEL_ATTRIBUTE, hasProperty("description", is("daily1"))))
				.andExpect(
						model().attribute(DailyController.MODEL_ATTRIBUTE, hasProperty("title", is("daily title1"))));
	}

	@Test
	@ExpectedDatabase(value = "dailyData-delete-expected.xml", assertionMode = DatabaseAssertionMode.NON_STRICT)
	public void deleteDaily() throws Exception {
		String expected = createRedirectViewPath(DailyController.REQUEST_MAPPING_LIST);
		mockMvc.perform(get("/daily/delete/{id}", 3L)).andExpect(status().isOk()).andExpect(view().name(expected))
				.andExpect(flash().attribute(DailyController.FLASH_MESSAGE_KEY_FEEDBACK,
						is("Daily: daily title3 was deleted.")));
		mockMvc.perform(get("/daily/delete/{id}", 2L)).andExpect(status().isOk()).andExpect(view().name(expected))
				.andExpect(flash().attribute(DailyController.FLASH_MESSAGE_KEY_FEEDBACK,
						is("Daily: daily title2 was deleted.")));
	}

	public static String createRedirectViewPath(String path) {
		StringBuilder redirectViewPath = new StringBuilder();
		redirectViewPath.append("redirect:");
		redirectViewPath.append(path);
		return redirectViewPath.toString();
	}

}
