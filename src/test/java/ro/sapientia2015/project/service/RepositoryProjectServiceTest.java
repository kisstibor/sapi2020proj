package ro.sapientia2015.project.service;

import static junit.framework.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.test.util.ReflectionTestUtils;

import ro.sapientia2015.project.ProjectTestUtil;
import ro.sapientia2015.project.dto.ProjectDTO;
import ro.sapientia2015.project.model.Project;
import ro.sapientia2015.project.repository.ProjectRepository;
import ro.sapientia2015.story.exception.NotFoundException;

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
	public void add() {
		ProjectDTO dto = ProjectTestUtil.createFormObject(null, ProjectTestUtil.NAME, ProjectTestUtil.PRODUCTOWNER,
				ProjectTestUtil.SCRUMMASTER, ProjectTestUtil.MEMBERS);

		service.add(dto);

		ArgumentCaptor<Project> projectArgument = ArgumentCaptor.forClass(Project.class);
		verify(repositoryMock, times(1)).save(projectArgument.capture());
		verifyNoMoreInteractions(repositoryMock);

		Project model = projectArgument.getValue();

		assertNull(model.getId());
		assertEquals(dto.getName(), model.getName());
		assertEquals(dto.getProductOwner(), model.getProductOwner());
		assertEquals(dto.getScrumMaster(), model.getScrumMaster());
		assertEquals(dto.getMembers(), model.getMembers());
	}

	@Test
	public void deleteById() throws NotFoundException {
		Project model = ProjectTestUtil.createModel(ProjectTestUtil.ID, ProjectTestUtil.NAME, ProjectTestUtil.PRODUCTOWNER,
				ProjectTestUtil.SCRUMMASTER, ProjectTestUtil.MEMBERS);
		when(repositoryMock.findOne(ProjectTestUtil.ID)).thenReturn(model);

		Project actual = service.deleteById(ProjectTestUtil.ID);

		verify(repositoryMock, times(1)).findOne(ProjectTestUtil.ID);
		verify(repositoryMock, times(1)).delete(model);
		verifyNoMoreInteractions(repositoryMock);

		assertEquals(model, actual);
	}

	@Test(expected = NotFoundException.class)
	public void deleteByIdWhenIsNotFound() throws NotFoundException {
		when(repositoryMock.findOne(ProjectTestUtil.ID)).thenReturn(null);

		service.deleteById(ProjectTestUtil.ID);

		verify(repositoryMock, times(1)).findOne(ProjectTestUtil.ID);
		verifyNoMoreInteractions(repositoryMock);
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
	public void findById() throws NotFoundException {
		Project model = ProjectTestUtil.createModel(ProjectTestUtil.ID, ProjectTestUtil.NAME, ProjectTestUtil.PRODUCTOWNER,
				ProjectTestUtil.SCRUMMASTER, ProjectTestUtil.MEMBERS);
		when(repositoryMock.findOne(ProjectTestUtil.ID)).thenReturn(model);

		Project actual = service.findById(ProjectTestUtil.ID);

		verify(repositoryMock, times(1)).findOne(ProjectTestUtil.ID);
		verifyNoMoreInteractions(repositoryMock);

		assertEquals(model, actual);
	}

	@Test(expected = NotFoundException.class)
	public void findByIdWhenIsNotFound() throws NotFoundException {
		when(repositoryMock.findOne(ProjectTestUtil.ID)).thenReturn(null);

		service.findById(ProjectTestUtil.ID);

		verify(repositoryMock, times(1)).findOne(ProjectTestUtil.ID);
		verifyNoMoreInteractions(repositoryMock);
	}

	@Test
	public void update() throws NotFoundException {
		ProjectDTO dto = ProjectTestUtil.createFormObject(ProjectTestUtil.ID, ProjectTestUtil.NAME_UPDATED,
				ProjectTestUtil.PRODUCTOWNER_UPDATED, ProjectTestUtil.SCRUMMASTER_UPDATED, ProjectTestUtil.MEMBERS_UPDATED);
		Project model = ProjectTestUtil.createModel(ProjectTestUtil.ID, ProjectTestUtil.NAME, ProjectTestUtil.PRODUCTOWNER,
				ProjectTestUtil.SCRUMMASTER, ProjectTestUtil.MEMBERS);
		when(repositoryMock.findOne(dto.getId())).thenReturn(model);

		Project actual = service.update(dto);

		verify(repositoryMock, times(1)).findOne(dto.getId());
		verifyNoMoreInteractions(repositoryMock);

		assertEquals(dto.getId().longValue(), actual.getId().longValue());
		assertEquals(dto.getName(), actual.getName());
		assertEquals(dto.getProductOwner(), actual.getProductOwner());
		assertEquals(dto.getScrumMaster(), actual.getScrumMaster());
		assertEquals(dto.getMembers(), actual.getMembers());
	}

	@Test(expected = NotFoundException.class)
	public void updateWhenIsNotFound() throws NotFoundException {
		ProjectDTO dto = ProjectTestUtil.createFormObject(ProjectTestUtil.ID, ProjectTestUtil.NAME_UPDATED,
				ProjectTestUtil.PRODUCTOWNER_UPDATED, ProjectTestUtil.SCRUMMASTER_UPDATED, ProjectTestUtil.MEMBERS_UPDATED);
		when(repositoryMock.findOne(dto.getId())).thenReturn(null);

		service.update(dto);

		verify(repositoryMock, times(1)).findOne(dto.getId());
		verifyNoMoreInteractions(repositoryMock);
	}
}
