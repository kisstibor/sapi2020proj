package ro.sapientia2015.story.service;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.springframework.test.util.ReflectionTestUtils;

import ro.sapientia2015.project.dto.ProjectDTO;
import ro.sapientia2015.project.model.Project;
import ro.sapientia2015.project.repository.ProjectRepository;
import ro.sapientia2015.project.service.RepositoryProjectService;
import ro.sapientia2015.story.ProjectTestUtil;
import ro.sapientia2015.story.exception.NotFoundException;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.*;


public class RepositoryProjectServiceTest {

    private RepositoryProjectService service;

    private ProjectRepository repositoryMock;

    @Before
    public void setUp() {

    	service = new RepositoryProjectService();
        repositoryMock = mock(ProjectRepository.class);
        ReflectionTestUtils.setField(service, "repository", repositoryMock);
    }


    @Test
    public void findAll() {
        List<Project> models = new ArrayList<Project>();
        when(repositoryMock.findAll()).thenReturn(models);

        List<Project> actual = service.findAll();

        verify(repositoryMock, times(1)).findAll();
        verifyNoMoreInteractions(repositoryMock);

        assertEquals(models, actual);
    }
    
    @Test
    public void findById() throws NotFoundException, javassist.NotFoundException {
    	Project model = ProjectTestUtil.createModel(ProjectTestUtil.ID, ProjectTestUtil.DESCRIPTION, ProjectTestUtil.TITLE);
        when(repositoryMock.findOne(ProjectTestUtil.ID)).thenReturn(model);

        Project actual = service.findById(ProjectTestUtil.ID);

        verify(repositoryMock, times(1)).findOne(ProjectTestUtil.ID);
        verifyNoMoreInteractions(repositoryMock);

        assertEquals(model, actual);
    }

    

    @Test(expected = NotFoundException.class)
    public void findByIdWhenIsNotFound() throws NotFoundException, javassist.NotFoundException {
        when(repositoryMock.findOne(ProjectTestUtil.ID)).thenReturn(null);

        service.findById(ProjectTestUtil.ID);

        verify(repositoryMock, times(1)).findOne(ProjectTestUtil.ID);
        verifyNoMoreInteractions(repositoryMock);
    }

    @Test
    public void addTest1() {
        
    	ProjectDTO dto = new ProjectDTO();
    	dto.setTitle("Dude");
    	dto.setDescription("Da Dude");
        Project.Builder builder = mock(Project.Builder.class);
    	
    	
    	Project model = Project.getBuilder(dto.getTitle())
                .description(dto.getDescription())
                .build();
    	dto.setBuilder(builder);
    	when(builder.build()).thenReturn(model);
    	when(builder.setTitle(dto.getTitle())).thenReturn(builder);
    	when(builder.description(dto.getDescription())).thenReturn(builder);
    	
    	model.setCreationTime(DateTime.now());
    	model.setId(0l);
    	model.setModificationTime(DateTime.now());
    	model.setVersion(0);
    	
        when(repositoryMock.save(model)).thenReturn(model);

        Project actual = service.add(dto);

        verify(repositoryMock, times(1)).save(model);
        verifyNoMoreInteractions(repositoryMock);

        assertEquals(model, actual);
    }
    
    @Test
    public void deleteById() throws NotFoundException, javassist.NotFoundException {
    	Project model = ProjectTestUtil.createModel(ProjectTestUtil.ID, ProjectTestUtil.DESCRIPTION, ProjectTestUtil.TITLE);
        when(repositoryMock.findOne(ProjectTestUtil.ID)).thenReturn(model);

        Project actual = service.deleteById(ProjectTestUtil.ID);

        verify(repositoryMock, times(1)).findOne(ProjectTestUtil.ID);
        verify(repositoryMock, times(1)).delete(model);
        verifyNoMoreInteractions(repositoryMock);

        assertEquals(model, actual);
    }
    
    @Test(expected = NotFoundException.class)
    public void deleteByIdWhenIsNotFound() throws NotFoundException, javassist.NotFoundException {
        when(repositoryMock.findOne(ProjectTestUtil.ID)).thenReturn(null);

        service.deleteById(ProjectTestUtil.ID);

        verify(repositoryMock, times(1)).findOne(ProjectTestUtil.ID);
        verifyNoMoreInteractions(repositoryMock);
    }
    
 }
