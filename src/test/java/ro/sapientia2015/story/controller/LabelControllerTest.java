package ro.sapientia2015.story.controller;

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

import ro.sapientia2015.story.LabelTestUtil;
import ro.sapientia2015.story.config.UnitTestContext;
import ro.sapientia2015.story.controller.LabelController;
import ro.sapientia2015.story.dto.LabelDTO;
import ro.sapientia2015.story.model.Label;
import ro.sapientia2015.story.service.LabelService;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {UnitTestContext.class})
public class LabelControllerTest {

    private static final String FEEDBACK_MESSAGE = "feedbackMessage";

    private LabelController controller;

    private MessageSource messageSourceMock;

    private LabelService serviceMock;

    @Resource
    private Validator validator;

    @Before
    public void setUp() {
        controller = new LabelController();

        messageSourceMock = mock(MessageSource.class);
        ReflectionTestUtils.setField(controller, "messageSource", messageSourceMock);

        serviceMock = mock(LabelService.class);
        ReflectionTestUtils.setField(controller, "service", serviceMock);
    }

    @Test
    public void showAddForm() {
        BindingAwareModelMap model = new BindingAwareModelMap();

        String view = controller.showAddForm(model);

        verifyZeroInteractions(messageSourceMock, serviceMock);
        assertEquals(LabelController.VIEW_ADD, view);

        LabelDTO formObject = (LabelDTO) model.asMap().get(LabelController.MODEL_ATTRIBUTE);

        assertNull(formObject.getId());
        assertNull(formObject.getTitle());
    }

    @Test
    public void add() {
        LabelDTO formObject = LabelTestUtil.createFormObject(null, LabelTestUtil.TITLE);

        Label labelModel = LabelTestUtil.createModel(LabelTestUtil.ID, LabelTestUtil.TITLE);
        when(serviceMock.add(formObject)).thenReturn(labelModel);

        MockHttpServletRequest mockRequest = new MockHttpServletRequest("POST", "/label/add");
        BindingResult result = bindAndValidate(mockRequest, formObject);

        RedirectAttributesModelMap attributes = new RedirectAttributesModelMap();

        initMessageSourceForFeedbackMessage(LabelController.FEEDBACK_MESSAGE_KEY_ADDED);

        String view = controller.add(formObject, result, attributes);

        verify(serviceMock, times(1)).add(formObject);
        verifyNoMoreInteractions(serviceMock);

        assertEquals("redirect:/label", view);

        assertFeedbackMessage(attributes, LabelController.FEEDBACK_MESSAGE_KEY_ADDED);
    }

    @Test
    public void listLabels() {
        BindingAwareModelMap model = new BindingAwareModelMap();

        List<Label> models = new ArrayList<Label>();
        when(serviceMock.findAll()).thenReturn(models);

        String view = controller.listLabels(model);

        verify(serviceMock, times(1)).findAll();
        verifyNoMoreInteractions(serviceMock);
        verifyZeroInteractions(messageSourceMock);

        assertEquals(LabelController.VIEW_LIST, view);
        assertEquals(models, model.asMap().get(LabelController.MODEL_ATTRIBUTE_LIST));
    }

    private void assertFeedbackMessage(RedirectAttributes attributes, String messageCode) {
        assertFlashMessages(attributes, messageCode, LabelController.FLASH_MESSAGE_KEY_FEEDBACK);
    }

    private void assertFlashMessages(RedirectAttributes attributes, String messageCode, String flashMessageParameterName) {
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
        when(messageSourceMock.getMessage(eq(feedbackMessageCode), any(Object[].class), any(Locale.class))).thenReturn(FEEDBACK_MESSAGE);
    }
}
