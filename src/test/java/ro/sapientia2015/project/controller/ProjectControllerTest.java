package ro.sapientia2015.project.controller;

import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.context.MessageSource;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;
import org.springframework.validation.support.BindingAwareModelMap;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributesModelMap;

import ro.sapientia2015.project.ProjectTestUtil;
import ro.sapientia2015.project.dto.ProjectDTO;
import ro.sapientia2015.project.model.Project;
import ro.sapientia2015.project.service.ProjectService;
import ro.sapientia2015.story.config.UnitTestContext;
import ro.sapientia2015.story.exception.NotFoundException;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { UnitTestContext.class })
public class ProjectControllerTest {

	private static final String FEEDBACK_MESSAGE = "feedbackMessage";
	private static final String FIELD_NAME = "name";
	private static final String FIELD_PRODUCTOWNER = "productOwner";
	private static final String FIELD_SCRUMMASTER = "scrumMaster";

	private ProjectController controller;

	private MessageSource messageSourceMock;

	private ProjectService serviceMock;

	@Resource
	private Validator validator;

	@Before
	public void setUp() {
		controller = new ProjectController();

		messageSourceMock = mock(MessageSource.class);
		ReflectionTestUtils.setField(controller, "messageSource", messageSourceMock);

		serviceMock = mock(ProjectService.class);
		ReflectionTestUtils.setField(controller, "service", serviceMock);
	}

	@Test
	public void showAddProjectForm() {
		BindingAwareModelMap model = new BindingAwareModelMap();

		String view = controller.showAddForm(model);

		verifyZeroInteractions(messageSourceMock, serviceMock);
		assertEquals(ProjectController.VIEW_ADD, view);

		ProjectDTO formObject = (ProjectDTO) model.asMap().get(ProjectController.MODEL_ATTRIBUTE);

		assertNull(formObject.getId());
		assertNull(formObject.getName());
		assertNull(formObject.getProductOwner());
		assertNull(formObject.getScrumMaster());
		assertNull(formObject.getMembers());
	}

	@Test
	public void add() {
		ProjectDTO formObject = ProjectTestUtil.createFormObject(null, ProjectTestUtil.NAME,
				ProjectTestUtil.PRODUCTOWNER, ProjectTestUtil.SCRUMMASTER, ProjectTestUtil.MEMBERS);

		Project model = ProjectTestUtil.createModel(ProjectTestUtil.ID, ProjectTestUtil.NAME,
				ProjectTestUtil.PRODUCTOWNER, ProjectTestUtil.SCRUMMASTER, ProjectTestUtil.MEMBERS);
		when(serviceMock.add(formObject)).thenReturn(model);

		MockHttpServletRequest mockRequest = new MockHttpServletRequest("POST", "/project/add");
		BindingResult result = bindAndValidate(mockRequest, formObject);

		RedirectAttributesModelMap attributes = new RedirectAttributesModelMap();

		initMessageSourceForFeedbackMessage(ProjectController.FEEDBACK_MESSAGE_KEY_ADDED);

		String view = controller.add(formObject, result, attributes);

		verify(serviceMock, times(1)).add(formObject);
		verifyNoMoreInteractions(serviceMock);

		String expectedView = ProjectTestUtil.createRedirectViewPath(ProjectController.REQUEST_MAPPING_VIEW);
		assertEquals(expectedView, view);

		assertEquals(Long.valueOf((String) attributes.get(ProjectController.PARAMETER_ID)), model.getId());

		assertFeedbackMessage(attributes, ProjectController.FEEDBACK_MESSAGE_KEY_ADDED);
	}

	@Test
	public void addEmptyProject() {
		ProjectDTO formObject = ProjectTestUtil.createFormObject(null, "", "", "", "");

		MockHttpServletRequest mockRequest = new MockHttpServletRequest("POST", "/project/add");
		BindingResult result = bindAndValidate(mockRequest, formObject);

		RedirectAttributesModelMap attributes = new RedirectAttributesModelMap();

		String view = controller.add(formObject, result, attributes);

		verifyZeroInteractions(serviceMock, messageSourceMock);

		assertEquals(ProjectController.VIEW_ADD, view);
		assertFieldErrors(result, FIELD_NAME);
	}

	@Test
	public void addWithTooLongNameAndProductOwnerAndTitle() {
		String name = ProjectTestUtil.createStringWithLength(Project.MAX_LENGTH_NAME + 1);
		String productOwner = ProjectTestUtil.createStringWithLength(Project.MAX_LENGTH_PRODUCTOWNER + 1);
		String scrumMaster = ProjectTestUtil.createStringWithLength(Project.MAX_LENGTH_SCRUMMASTER + 1);

		ProjectDTO formObject = ProjectTestUtil.createFormObject(null, name, productOwner, scrumMaster, null);

		MockHttpServletRequest mockRequest = new MockHttpServletRequest("POST", "/project/add");
		BindingResult result = bindAndValidate(mockRequest, formObject);

		RedirectAttributesModelMap attributes = new RedirectAttributesModelMap();

		String view = controller.add(formObject, result, attributes);

		verifyZeroInteractions(serviceMock, messageSourceMock);

		assertEquals(ProjectController.VIEW_ADD, view);
		assertFieldErrors(result, FIELD_NAME, FIELD_PRODUCTOWNER, FIELD_SCRUMMASTER);
	}

	@Test
	public void deleteById() throws NotFoundException {
		RedirectAttributesModelMap attributes = new RedirectAttributesModelMap();

		Project model = ProjectTestUtil.createModel(ProjectTestUtil.ID, ProjectTestUtil.NAME,
				ProjectTestUtil.PRODUCTOWNER, ProjectTestUtil.SCRUMMASTER, ProjectTestUtil.MEMBERS);
		when(serviceMock.deleteById(ProjectTestUtil.ID)).thenReturn(model);

		initMessageSourceForFeedbackMessage(ProjectController.FEEDBACK_MESSAGE_KEY_DELETED);

		String view = controller.deleteById(ProjectTestUtil.ID, attributes);

		verify(serviceMock, times(1)).deleteById(ProjectTestUtil.ID);
		verifyNoMoreInteractions(serviceMock);

		assertFeedbackMessage(attributes, ProjectController.FEEDBACK_MESSAGE_KEY_DELETED);

		String expectedView = ProjectTestUtil.createRedirectViewPath(ProjectController.REQUEST_MAPPING_LIST);
		assertEquals(expectedView, view);
	}

	@Test(expected = NotFoundException.class)
	public void deleteByIdWhenIsNotFound() throws NotFoundException {
		RedirectAttributesModelMap attributes = new RedirectAttributesModelMap();

		when(serviceMock.deleteById(ProjectTestUtil.ID)).thenThrow(new NotFoundException(""));

		controller.deleteById(ProjectTestUtil.ID, attributes);

		verify(serviceMock, times(1)).deleteById(ProjectTestUtil.ID);
		verifyNoMoreInteractions(serviceMock);
		verifyZeroInteractions(messageSourceMock);
	}

	@Test
	public void findAll() {
		BindingAwareModelMap model = new BindingAwareModelMap();

		List<Project> models = new ArrayList<Project>();
		when(serviceMock.findAll()).thenReturn(models);

		String view = controller.findAll(model);

		verify(serviceMock, times(1)).findAll();
		verifyNoMoreInteractions(serviceMock);
		verifyZeroInteractions(messageSourceMock);

		assertEquals(ProjectController.VIEW_LIST, view);
		assertEquals(models, model.asMap().get(ProjectController.MODEL_ATTRIBUTE_LIST));
	}

	@Test
	public void findById() throws NotFoundException {
		BindingAwareModelMap model = new BindingAwareModelMap();

		Project found = ProjectTestUtil.createModel(ProjectTestUtil.ID, ProjectTestUtil.NAME,
				ProjectTestUtil.PRODUCTOWNER, ProjectTestUtil.SCRUMMASTER, ProjectTestUtil.MEMBERS);
		when(serviceMock.findById(ProjectTestUtil.ID)).thenReturn(found);

		String view = controller.findById(ProjectTestUtil.ID, model);

		verify(serviceMock, times(1)).findById(ProjectTestUtil.ID);
		verifyNoMoreInteractions(serviceMock);
		verifyZeroInteractions(messageSourceMock);

		assertEquals(ProjectController.VIEW_VIEW, view);
		assertEquals(found, model.asMap().get(ProjectController.MODEL_ATTRIBUTE));
	}

	@Test(expected = NotFoundException.class)
	public void findByIdWhenIsNotFound() throws NotFoundException {
		BindingAwareModelMap model = new BindingAwareModelMap();

		when(serviceMock.findById(ProjectTestUtil.ID)).thenThrow(new NotFoundException(""));

		controller.findById(ProjectTestUtil.ID, model);

		verify(serviceMock, times(1)).findById(ProjectTestUtil.ID);
		verifyNoMoreInteractions(serviceMock);
		verifyZeroInteractions(messageSourceMock);
	}

	@Test
	public void showUpdateStoryForm() throws NotFoundException {
		BindingAwareModelMap model = new BindingAwareModelMap();

		Project updated = ProjectTestUtil.createModel(ProjectTestUtil.ID, ProjectTestUtil.NAME,
				ProjectTestUtil.PRODUCTOWNER, ProjectTestUtil.SCRUMMASTER, ProjectTestUtil.MEMBERS);
		when(serviceMock.findById(ProjectTestUtil.ID)).thenReturn(updated);

		String view = controller.showUpdateForm(ProjectTestUtil.ID, model);

		verify(serviceMock, times(1)).findById(ProjectTestUtil.ID);
		verifyNoMoreInteractions(serviceMock);
		verifyZeroInteractions(messageSourceMock);

		assertEquals(ProjectController.VIEW_UPDATE, view);

		ProjectDTO formObject = (ProjectDTO) model.asMap().get(ProjectController.MODEL_ATTRIBUTE);

		assertEquals(updated.getId(), formObject.getId());
		assertEquals(updated.getName(), formObject.getName());
		assertEquals(updated.getProductOwner(), formObject.getProductOwner());
		assertEquals(updated.getScrumMaster(), formObject.getScrumMaster());
		assertEquals(updated.getMembers(), formObject.getMembers());
	}

	@Test(expected = NotFoundException.class)
	public void showUpdateStoryFormWhenIsNotFound() throws NotFoundException {
		BindingAwareModelMap model = new BindingAwareModelMap();

		when(serviceMock.findById(ProjectTestUtil.ID)).thenThrow(new NotFoundException(""));

		controller.showUpdateForm(ProjectTestUtil.ID, model);

		verify(serviceMock, times(1)).findById(ProjectTestUtil.ID);
		verifyNoMoreInteractions(serviceMock);
		verifyZeroInteractions(messageSourceMock);
	}

	@Test
	public void update() throws NotFoundException {
		ProjectDTO formObject = ProjectTestUtil.createFormObject(ProjectTestUtil.ID, ProjectTestUtil.NAME_UPDATED,
				ProjectTestUtil.PRODUCTOWNER_UPDATED, ProjectTestUtil.SCRUMMASTER_UPDATED, ProjectTestUtil.MEMBERS_UPDATED);

		Project model = ProjectTestUtil.createModel(ProjectTestUtil.ID, ProjectTestUtil.NAME_UPDATED,
				ProjectTestUtil.PRODUCTOWNER_UPDATED, ProjectTestUtil.SCRUMMASTER_UPDATED, ProjectTestUtil.MEMBERS_UPDATED);
		when(serviceMock.update(formObject)).thenReturn(model);

		MockHttpServletRequest mockRequest = new MockHttpServletRequest("POST", "/project/add");
		BindingResult result = bindAndValidate(mockRequest, formObject);

		RedirectAttributesModelMap attributes = new RedirectAttributesModelMap();

		initMessageSourceForFeedbackMessage(ProjectController.FEEDBACK_MESSAGE_KEY_UPDATED);

		String view = controller.update(formObject, result, attributes);

		verify(serviceMock, times(1)).update(formObject);
		verifyNoMoreInteractions(serviceMock);

		String expectedView = ProjectTestUtil.createRedirectViewPath(ProjectController.REQUEST_MAPPING_VIEW);
		assertEquals(expectedView, view);

		assertEquals(Long.valueOf((String) attributes.get(ProjectController.PARAMETER_ID)), model.getId());

		assertFeedbackMessage(attributes, ProjectController.FEEDBACK_MESSAGE_KEY_UPDATED);
	}

	@Test
	public void updateEmpty() throws NotFoundException {
		ProjectDTO formObject = ProjectTestUtil.createFormObject(ProjectTestUtil.ID, "", "", "", "");

		MockHttpServletRequest mockRequest = new MockHttpServletRequest("POST", "/project/add");
		BindingResult result = bindAndValidate(mockRequest, formObject);

		RedirectAttributesModelMap attributes = new RedirectAttributesModelMap();

		String view = controller.update(formObject, result, attributes);

		verifyZeroInteractions(messageSourceMock, serviceMock);

		assertEquals(ProjectController.VIEW_UPDATE, view);
		assertFieldErrors(result, FIELD_NAME);
	}

	@Test
	public void updateWhenNameAndProductOwnerAndScrumMasterAreTooLong() throws NotFoundException {
		String name = ProjectTestUtil.createStringWithLength(Project.MAX_LENGTH_NAME + 1);
		String productOwner = ProjectTestUtil.createStringWithLength(Project.MAX_LENGTH_PRODUCTOWNER + 1);
		String scrumMaster = ProjectTestUtil.createStringWithLength(Project.MAX_LENGTH_SCRUMMASTER + 1);

		ProjectDTO formObject = ProjectTestUtil.createFormObject(ProjectTestUtil.ID, name, productOwner, scrumMaster, null);

		MockHttpServletRequest mockRequest = new MockHttpServletRequest("POST", "/project/add");
		BindingResult result = bindAndValidate(mockRequest, formObject);

		RedirectAttributesModelMap attributes = new RedirectAttributesModelMap();

		String view = controller.update(formObject, result, attributes);

		verifyZeroInteractions(messageSourceMock, serviceMock);

		assertEquals(ProjectController.VIEW_UPDATE, view);
		assertFieldErrors(result, FIELD_NAME, FIELD_PRODUCTOWNER, FIELD_SCRUMMASTER);
	}

	@Test(expected = NotFoundException.class)
	public void updateWhenIsNotFound() throws NotFoundException {
		ProjectDTO formObject = ProjectTestUtil.createFormObject(ProjectTestUtil.ID, ProjectTestUtil.NAME_UPDATED,
				ProjectTestUtil.PRODUCTOWNER_UPDATED, ProjectTestUtil.SCRUMMASTER_UPDATED, ProjectTestUtil.MEMBERS_UPDATED);

		when(serviceMock.update(formObject)).thenThrow(new NotFoundException(""));

		MockHttpServletRequest mockRequest = new MockHttpServletRequest("POST", "/project/add");
		BindingResult result = bindAndValidate(mockRequest, formObject);

		RedirectAttributesModelMap attributes = new RedirectAttributesModelMap();

		controller.update(formObject, result, attributes);

		verify(serviceMock, times(1)).update(formObject);
		verifyNoMoreInteractions(serviceMock);
		verifyZeroInteractions(messageSourceMock);
	}

	private void assertFeedbackMessage(RedirectAttributes attributes, String messageCode) {
		assertFlashMessages(attributes, messageCode, ProjectController.FLASH_MESSAGE_KEY_FEEDBACK);
	}

	private void assertFieldErrors(BindingResult result, String... fieldNames) {
		assertEquals(fieldNames.length, result.getFieldErrorCount());
		for (String fieldName : fieldNames) {
			assertNotNull(result.getFieldError(fieldName));
		}
	}

	private void assertFlashMessages(RedirectAttributes attributes, String messageCode,
			String flashMessageParameterName) {
		Map<String, ?> flashMessages = attributes.getFlashAttributes();
		Object message = flashMessages.get(flashMessageParameterName);

		assertNotNull(message);
		flashMessages.remove(message);
		assertTrue(flashMessages.isEmpty());

		verify(messageSourceMock, times(1)).getMessage(eq(messageCode), any(Object[].class), any(Locale.class));
		verifyNoMoreInteractions(messageSourceMock);
	}

	private BindingResult bindAndValidate(HttpServletRequest request, Object formObject) {
		WebDataBinder binder = new WebDataBinder(formObject);
		binder.setValidator(validator);
		binder.bind(new MutablePropertyValues(request.getParameterMap()));
		binder.getValidator().validate(binder.getTarget(), binder.getBindingResult());
		return binder.getBindingResult();
	}

	private void initMessageSourceForFeedbackMessage(String feedbackMessageCode) {
		when(messageSourceMock.getMessage(eq(feedbackMessageCode), any(Object[].class), any(Locale.class)))
				.thenReturn(FEEDBACK_MESSAGE);
	}
}
