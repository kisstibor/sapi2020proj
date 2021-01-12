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
import org.springframework.web.servlet.mvc.support.RedirectAttributesModelMap;

import ro.sapientia2015.project.dto.EpicDTO;
import ro.sapientia2015.project.dto.ProjectDTO;
import ro.sapientia2015.project.model.Epic;
import ro.sapientia2015.project.model.Project;
import ro.sapientia2015.project.service.EpicService;
import ro.sapientia2015.project.service.ProjectService;
import ro.sapientia2015.story.config.UnitTestContext;
import ro.sapientia2015.story.exception.NotFoundException;
import ro.sapientia2015.story.ProjectTestUtil;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;



@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {UnitTestContext.class})
public class ProjectControllerTest {
	
    private static final String FEEDBACK_MESSAGE = "feedbackMessage";

    private ProjectController controller;
    
    
    private MessageSource messageSourceMock;

    private ProjectService serviceMock;
    private EpicService serviceMockEpic;

    @Resource
    private Validator validator;
    
    @Before
    public void setUp() {
        controller = new ProjectController();
        
        messageSourceMock = mock(MessageSource.class);
        //ReflectionTestUtils.setField(controller, "messageSource", messageSourceMock);

        serviceMock = mock(ProjectService.class);
        ReflectionTestUtils.setField(controller, "service", serviceMock);
    }

    @Test
    public void projectList1() {
        BindingAwareModelMap model = new BindingAwareModelMap();

        String view = controller.listProject(model);

        assertEquals("project/list", view);
     }

    @Test
    public void projectList2() {
        BindingAwareModelMap model = new BindingAwareModelMap();

        controller.listProject(model);

        List<Project> projects = (List<Project>)model.asMap().get("projects");
        assertNotNull(projects);
     }

    @Test
    public void projectList3() {
        BindingAwareModelMap model = new BindingAwareModelMap();

        controller.listProject(model);

        verify(serviceMock, times(1)).findAll();
        verifyNoMoreInteractions(serviceMock);
     }
    
    @Test
    public void projectList4() {
        BindingAwareModelMap model = new BindingAwareModelMap();       
        List<Project> list = new ArrayList<Project>();
        list.add(new Project());
        when(serviceMock.findAll()).thenReturn(list);

        controller.listProject(model);
        
        List<Project> projects =  (List<Project>)model.asMap().get("projects");

        assertEquals(1, projects.size());
     }
    private BindingResult bindAndValidate(HttpServletRequest request, Object formObject) {
        WebDataBinder binder = new WebDataBinder(formObject);
        binder.setValidator(validator);
        binder.bind(new MutablePropertyValues(request.getParameterMap()));
        binder.getValidator().validate(binder.getTarget(), binder.getBindingResult());
        return binder.getBindingResult();
    }

    @Test
    public void add() {
    	ProjectDTO formObject = new ProjectDTO();

        formObject.setTitle("title");
        formObject.setDescription("desc");
        
        Project model = Project.getBuilder("title")
        		.description("desc").build();
        
        when(serviceMock.add(formObject)).thenReturn(model);

        MockHttpServletRequest mockRequest = new MockHttpServletRequest("POST", "/project/add");
        BindingResult result = bindAndValidate(mockRequest, formObject);

        RedirectAttributesModelMap attributes = new RedirectAttributesModelMap();


        String view = controller.add(formObject, result, attributes);

        verify(serviceMock, times(1)).add(formObject);
        verifyNoMoreInteractions(serviceMock);

        String expectedView = "redirect:/project/list";
        assertEquals(expectedView, view);
    }

    
//    @Test
//    public void addEpic() {
//    	ProjectDTO formObject = new ProjectDTO();
//    	EpicDTO formObjectEpic = new EpicDTO();
//
//        formObject.setTitle("title");
//        formObject.setDescription("desc");
//        formObjectEpic.setTitle("title");
//        formObjectEpic.setDescription("desc");
//        
//        Project model = Project.getBuilder("title")
//        		.description("desc").build();
//        Epic modelEpic = Epic.getBuilder("title")
//        		.description("desc").build();
//        
//        when(serviceMock.add(formObject)).thenReturn(model);
//        when(serviceMockEpic.add(formObjectEpic)).thenReturn(modelEpic);
//
//        MockHttpServletRequest mockRequest = new MockHttpServletRequest("POST", "/project/add");
//        BindingResult result = bindAndValidate(mockRequest, formObject);
//        
//        MockHttpServletRequest mockRequestEpic = new MockHttpServletRequest("POST", "/project/{id}/epic/add");
//        BindingResult resultEpic = bindAndValidate(mockRequestEpic, formObjectEpic);
//        
//        
//
//        RedirectAttributesModelMap attributes = new RedirectAttributesModelMap();
//
//
//        String view = controller.add(formObject, result, attributes);
//        
//        String viewEpic = controller.addEpic(formObjectEpic,1L , resultEpic, attributes);
//
//        verify(serviceMock, times(1)).add(formObject);
//        verifyNoMoreInteractions(serviceMock);
//        
//        verify(serviceMockEpic, times(1)).add(formObjectEpic);
//        verifyNoMoreInteractions(serviceMockEpic);
//
//        String expectedView = "redirect:/project/1";
//        assertEquals(expectedView, viewEpic);
//    }
//    
    @Test
    public void addEmptyProject1() {
    	
    	ProjectDTO formObject = new ProjectDTO();

        formObject.setTitle("");
        formObject.setDescription("");
       
        MockHttpServletRequest mockRequest = new MockHttpServletRequest("POST", "/project/add");
        BindingResult result = bindAndValidate(mockRequest, formObject);

        RedirectAttributesModelMap attributes = new RedirectAttributesModelMap();

        String view = controller.add(formObject, result, attributes);

        assertEquals(ProjectController.VIEW_ADD, view);
    }
    
    @Test
    public void addTooLongProjectTitle() {
    	
    	ProjectDTO formObject = new ProjectDTO();

        formObject.setTitle("TooLongTitleeeeeeeeeeeeee");
        formObject.setDescription("");
       
        MockHttpServletRequest mockRequest = new MockHttpServletRequest("POST", "/project/add");
        BindingResult result = bindAndValidate(mockRequest, formObject);

        RedirectAttributesModelMap attributes = new RedirectAttributesModelMap();

        String view = controller.add(formObject, result, attributes);

        assertEquals(ProjectController.VIEW_ADD, view);
    }
    
    @Test
    public void deleteProject() throws NotFoundException, javassist.NotFoundException {
        RedirectAttributesModelMap attributes = new RedirectAttributesModelMap();

        Project model = ProjectTestUtil.createModel(ProjectTestUtil.ID, ProjectTestUtil.DESCRIPTION, ProjectTestUtil.TITLE);
        when(serviceMock.deleteById(ProjectTestUtil.ID)).thenReturn(model);

        String view = controller.deleteProject(ProjectTestUtil.ID);

        verify(serviceMock, times(1)).deleteById(ProjectTestUtil.ID);
        verifyNoMoreInteractions(serviceMock);

        String expectedView = ProjectTestUtil.createRedirectViewPath(ProjectController.REQUEST_MAPPING_LIST);
        assertEquals(expectedView, view);
    }
        
}
