package ro.sapientia2015.story.controller;

import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.Locale;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.MutablePropertyValues;
import org.springframework.context.MessageSource;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

public class ControllerTestBase {
	
	protected static final String FEEDBACK_MESSAGE = "feedbackMessage";
	
	protected MessageSource messageSourceMock;
	
	@Resource
    private Validator validator;
	
	protected void setUp() {        
        messageSourceMock = mock(MessageSource.class);
	}
	
	protected void initMessageSourceForFeedbackMessage(String feedbackMessageCode) {
        when(messageSourceMock.getMessage(eq(feedbackMessageCode), any(Object[].class), any(Locale.class))).thenReturn(FEEDBACK_MESSAGE);
    }
	
	protected void assertFeedbackMessage(RedirectAttributes attributes, String messageCode, String expectedMessageKey) {
        assertFlashMessages(attributes, messageCode, expectedMessageKey);
    }
	
	protected void assertFlashMessages(RedirectAttributes attributes, String messageCode, String flashMessageParameterName) {
        Map<String, ?> flashMessages = attributes.getFlashAttributes();
        Object message = flashMessages.get(flashMessageParameterName);

        assertNotNull(message);
        flashMessages.remove(message);
        assertTrue(flashMessages.isEmpty());

        verify(messageSourceMock, times(1)).getMessage(eq(messageCode), any(Object[].class), any(Locale.class));
        verifyNoMoreInteractions(messageSourceMock);
    }
	
	protected BindingResult bindAndValidate(HttpServletRequest request, Object formObject) {
		WebDataBinder binder = new WebDataBinder(formObject);
		binder.setValidator(validator);
		binder.bind(new MutablePropertyValues(request.getParameterMap()));
		binder.getValidator().validate(binder.getTarget(), binder.getBindingResult());
		return binder.getBindingResult();
	}
}
