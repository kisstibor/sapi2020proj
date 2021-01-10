package ro.sapientia2015.story.controller;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributesModelMap;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.context.MessageSource;
import org.springframework.mock.web.MockHttpServletRequest;

import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;

import java.util.Locale;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import ro.sapientia2015.story.config.UnitTestContext;
import ro.sapientia2015.story.dto.DailyDTO;
import ro.sapientia2015.story.exception.NotFoundException;
import ro.sapientia2015.story.model.Daily;
import ro.sapientia2015.story.service.DailyService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {UnitTestContext.class})
public class DailyControllerTest {

	private static final String FEEDBACK_MESSAGE = "feedbackMessage";
	private static final String FIELD_DESCRIPTION = "description";
    private static final String FIELD_TITLE = "title";
    private static final String FIELD_DATEE = "datee";
    private static final String FIELD_DURATION = "duration";
    private static final String FIELD_ID = "id";
    private String TITLE = "title";
    private String DESCRIPTION = "description";
    private String DURATION = "12";
    private String UPDATED_DURATION= "34";
    private String DATEE = "11-01-2021";
    private String UPDATED_DATEE = "17-06-2021";
    private DailyDTO dailyDTO;
    private Daily daily;
    
    private DailyController controller;
    private MessageSource messageSource;
    private DailyService service;
    @Resource
    private Validator validator;
    
    @Before
    public void setUp() {
    	controller = new DailyController();
    	messageSource = mock(MessageSource.class);
    	service = mock(DailyService.class);
    	
        ReflectionTestUtils.setField(controller, "messageSource", messageSource);
        ReflectionTestUtils.setField(controller, "service", service);
        setUpTestingObj();
    }
    
    private void setUpTestingObj() {
		dailyDTO = new DailyDTO();
		dailyDTO.setDatee(DATEE);
		dailyDTO.setDescription(DESCRIPTION);
		dailyDTO.setDuration(DURATION);
		dailyDTO.setTitle(TITLE);
		
		daily = Daily.getBuilder(TITLE)
				.datee(UPDATED_DATEE)
				.description(DESCRIPTION)
				.duration(UPDATED_DURATION)
				.build();
	}
    
    @Test
    public void add() {
    	dailyDTO.setId(1011L);
        ReflectionTestUtils.setField(daily, "id", 1011L);
        
        when(service.add(dailyDTO)).thenReturn(daily);
        MockHttpServletRequest request = new MockHttpServletRequest("POST", "/daily/add");
        BindingResult result = bindAndValidate(request, dailyDTO);
        
        RedirectAttributesModelMap attributes = new RedirectAttributesModelMap();
        when(messageSource.getMessage(eq(FEEDBACK_MESSAGE), any(Object[].class), any(Locale.class))).thenReturn(FEEDBACK_MESSAGE);
        
        String view = controller.add(dailyDTO, result, attributes);
        verify(service, times(1)).add(dailyDTO);
        verifyNoMoreInteractions(service);
        
        String expected = createRedirectViewPath(DailyController.REQUEST_MAPPING_VIEW);
        assertEquals(expected, view);
        assertEquals(Long.valueOf((String)attributes.get(DailyController.PARAMETER_ID)), daily.getId());
    }
    
    @Test
    public void addWithTooLongDuration() {
    	dailyDTO.setId(1012L);
        ReflectionTestUtils.setField(daily, "id", 1012L);
        
        dailyDTO.setDuration("1234");
        
        MockHttpServletRequest request = new MockHttpServletRequest("POST", "/daily/add");
        BindingResult result = bindAndValidate(request, dailyDTO);
        
        RedirectAttributesModelMap attributes = new RedirectAttributesModelMap();

        verifyZeroInteractions(service, messageSource);
        
        assertEquals(DailyController.VIEW_ADD, controller.add(dailyDTO, result, attributes));
        assertFieldErrors(result, FIELD_DURATION);
    }
    
    @Test
    public void addWithWrongDuration() { //duration = "qw"
    	dailyDTO.setId(1012L);
        ReflectionTestUtils.setField(daily, "id", 1012L);
        
        dailyDTO.setDuration("qw");
        
        MockHttpServletRequest request = new MockHttpServletRequest("POST", "/daily/add");
        BindingResult result = bindAndValidate(request, dailyDTO);
        
        RedirectAttributesModelMap attributes = new RedirectAttributesModelMap();

        verifyZeroInteractions(service, messageSource);
        
        String view = controller.add(dailyDTO, result, attributes);
        
        String expectedView = createRedirectViewPath("/daily/add");
        assertEquals(expectedView, view);
    }
    
    @Test
    public void addWithWrongDatee() { //datee = "12-12-122y"
    	dailyDTO.setId(1012L);
        ReflectionTestUtils.setField(daily, "id", 1012L);
        
        dailyDTO.setDatee("12-12-122y");
        
        MockHttpServletRequest request = new MockHttpServletRequest("POST", "/daily/add");
        BindingResult result = bindAndValidate(request, dailyDTO);
        
        RedirectAttributesModelMap attributes = new RedirectAttributesModelMap();

        verifyZeroInteractions(service, messageSource);
        
        String view = controller.add(dailyDTO, result, attributes);
        
        String expectedView = createRedirectViewPath("/daily/add");
        assertEquals(expectedView, view);
    }
    
    @Test
    public void update() throws NotFoundException {
    	dailyDTO.setId(1012L);
        ReflectionTestUtils.setField(daily, "id", 1012L);
        
        when(service.update(dailyDTO)).thenReturn(daily);
        
        MockHttpServletRequest request = new MockHttpServletRequest("POST", "/daily/add");
        BindingResult result = bindAndValidate(request, daily);
        
        RedirectAttributesModelMap attributes = new RedirectAttributesModelMap();

        initMessageSourceForFeedbackMessage(DailyController.FEEDBACK_MESSAGE_KEY_UPDATED);

        String view = controller.update(dailyDTO, result, attributes);
        
        verify(service, times(1)).update(dailyDTO);
        verifyNoMoreInteractions(service);

        String expectedView = createRedirectViewPath(DailyController.REQUEST_MAPPING_VIEW);
        assertEquals(expectedView, view);

        assertEquals(Long.valueOf((String) attributes.get(DailyController.PARAMETER_ID)), daily.getId());

        assertFeedbackMessage(attributes, DailyController.FEEDBACK_MESSAGE_KEY_UPDATED);

    }
    
    @Test
    public void delete() throws NotFoundException {
    	dailyDTO.setId(1012L);
        ReflectionTestUtils.setField(daily, "id", 1012L);
        
        RedirectAttributesModelMap attributes = new RedirectAttributesModelMap();

        when(service.deleteById(1012L)).thenReturn(daily);

        initMessageSourceForFeedbackMessage(DailyController.FEEDBACK_MESSAGE_KEY_DELETED);

        String view = controller.deleteById(1012L, attributes);

        verify(service, times(1)).deleteById(1012L);
        verifyNoMoreInteractions(service);

        assertFeedbackMessage(attributes, DailyController.FEEDBACK_MESSAGE_KEY_DELETED);

        String expectedView = createRedirectViewPath(DailyController.REQUEST_MAPPING_LIST);
        assertEquals(expectedView, view);
    }
    
    @Test
    public void testIsNumeric() {
    	assertEquals(true, DailyController.isNumeric("22"));
    	assertEquals(false, DailyController.isNumeric("5.05"));
    	assertEquals(true, DailyController.isNumeric("-200")); 
    	assertEquals(false, DailyController.isNumeric("10.0d"));
    	assertEquals(false, DailyController.isNumeric("   22   "));
    	 
    	assertEquals(false, DailyController.isNumeric(null));
    	assertEquals(false, DailyController.isNumeric(""));
    	assertEquals(false, DailyController.isNumeric("abc"));
    }
    
    public static String createRedirectViewPath(String path) {
        StringBuilder redirectViewPath = new StringBuilder();
        redirectViewPath.append("redirect:");
        redirectViewPath.append(path);
        return redirectViewPath.toString();
    }
    
    private void assertFieldErrors(BindingResult result, String... fieldNames) {
        assertEquals(fieldNames.length, result.getFieldErrorCount());
        for (String fieldName : fieldNames) {
            assertNotNull(result.getFieldError(fieldName));
        }
    }
    
    private void assertFeedbackMessage(RedirectAttributes attributes, String messageCode) {
        assertFlashMessages(attributes, messageCode, DailyController.FLASH_MESSAGE_KEY_FEEDBACK);
    }
    
    private void assertFlashMessages(RedirectAttributes attributes, String messageCode, String flashMessageParameterName) {
        Map<String, ?> flashMessages = attributes.getFlashAttributes();
        Object message = flashMessages.get(flashMessageParameterName);
        assertNotNull(message);
        
        flashMessages.remove(message);
        assertTrue(flashMessages.isEmpty());
        verify(messageSource, times(1)).getMessage(eq(messageCode), any(Object[].class), any(Locale.class));
        verifyNoMoreInteractions(messageSource);
    }
    
    private BindingResult bindAndValidate(HttpServletRequest request, Object formObject) {
        WebDataBinder binder = new WebDataBinder(formObject);
        binder.setValidator(validator);
        binder.bind(new MutablePropertyValues(request.getParameterMap()));
        binder.getValidator().validate(binder.getTarget(), binder.getBindingResult());
        return binder.getBindingResult();
    }
    
    private void initMessageSourceForFeedbackMessage(String feedbackMessageCode) {
        when(messageSource.getMessage(eq(feedbackMessageCode), any(Object[].class), any(Locale.class))).thenReturn(FEEDBACK_MESSAGE);
    }
}
