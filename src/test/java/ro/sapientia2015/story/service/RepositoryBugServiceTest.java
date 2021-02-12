package ro.sapientia2015.story.service;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.test.util.ReflectionTestUtils;

import ro.sapientia2015.story.BugTestUtil;
import ro.sapientia2015.story.dto.BugDTO;
import ro.sapientia2015.story.exception.NotFoundException;
import ro.sapientia2015.story.model.Bug;
import ro.sapientia2015.story.repository.BugRepository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

public class RepositoryBugServiceTest {

	private RepositoryBugService service;

	private BugRepository repositoryMock;

	@Before
	public void setUp() {
		service = new RepositoryBugService();

		repositoryMock = mock(BugRepository.class);
		ReflectionTestUtils.setField(service, "repository", repositoryMock);
	}

	@Test
	public void add() {
		BugDTO dto = BugTestUtil.createFormObject(null, BugTestUtil.TITLE, BugTestUtil.DESCRIPTION, BugTestUtil.STATUS);

		service.add(dto);

		ArgumentCaptor<Bug> bugArgument = ArgumentCaptor.forClass(Bug.class);
		verify(repositoryMock, times(1)).save(bugArgument.capture());
		verifyNoMoreInteractions(repositoryMock);

		Bug model = bugArgument.getValue();

		assertNull(model.getId());
		assertEquals(dto.getTitle(), model.getTitle());
		assertEquals(dto.getDescription(), model.getDescription());
		assertEquals(dto.getStatus(), model.getStatus());
	}

	@Test
	public void deleteById() throws NotFoundException {
		Bug model = BugTestUtil.createModel(BugTestUtil.ID, BugTestUtil.TITLE, BugTestUtil.DESCRIPTION, BugTestUtil.STATUS);
		when(repositoryMock.findOne(BugTestUtil.ID)).thenReturn(model);

		Bug actual = service.deleteById(BugTestUtil.ID);

		verify(repositoryMock, times(1)).findOne(BugTestUtil.ID);
		verify(repositoryMock, times(1)).delete(model);
		verifyNoMoreInteractions(repositoryMock);

		assertEquals(model, actual);
	}

	@Test(expected = NotFoundException.class)
	public void deleteByIdWhenIsNotFound() throws NotFoundException {
		when(repositoryMock.findOne(BugTestUtil.ID)).thenReturn(null);

		service.deleteById(BugTestUtil.ID);

		verify(repositoryMock, times(1)).findOne(BugTestUtil.ID);
		verifyNoMoreInteractions(repositoryMock);
	}

	@Test
	public void findAll() {
		List<Bug> models = new ArrayList<Bug>();
		when(repositoryMock.findAll()).thenReturn(models);

		List<Bug> actual = service.findAll();

		verify(repositoryMock, times(1)).findAll();
		verifyNoMoreInteractions(repositoryMock);

		assertEquals(models, actual);
	}

	@Test
	public void findById() throws NotFoundException {
		Bug model = BugTestUtil.createModel(BugTestUtil.ID, BugTestUtil.TITLE, BugTestUtil.DESCRIPTION, BugTestUtil.STATUS);
		when(repositoryMock.findOne(BugTestUtil.ID)).thenReturn(model);

		Bug actual = service.findById(BugTestUtil.ID);

		verify(repositoryMock, times(1)).findOne(BugTestUtil.ID);
		verifyNoMoreInteractions(repositoryMock);

		assertEquals(model, actual);
	}

	@Test(expected = NotFoundException.class)
	public void findByIdWhenIsNotFound() throws NotFoundException {
		when(repositoryMock.findOne(BugTestUtil.ID)).thenReturn(null);

		service.findById(BugTestUtil.ID);

		verify(repositoryMock, times(1)).findOne(BugTestUtil.ID);
		verifyNoMoreInteractions(repositoryMock);
	}

	@Test
	public void update() throws NotFoundException {
		BugDTO dto = BugTestUtil.createFormObject(BugTestUtil.ID, BugTestUtil.TITLE_UPDATED, BugTestUtil.DESCRIPTION_UPDATED, BugTestUtil.STATUS_UPDATED);
		Bug model = BugTestUtil.createModel(BugTestUtil.ID, BugTestUtil.TITLE, BugTestUtil.DESCRIPTION, BugTestUtil.STATUS);
		when(repositoryMock.findOne(dto.getId())).thenReturn(model);

		Bug actual = service.update(dto);

		verify(repositoryMock, times(1)).findOne(dto.getId());
		verifyNoMoreInteractions(repositoryMock);

		assertEquals(dto.getId(), actual.getId());
		assertEquals(dto.getTitle(), actual.getTitle());
		assertEquals(dto.getDescription(), actual.getDescription());
		assertEquals(dto.getStatus(), actual.getStatus());
	}

	@Test(expected = NotFoundException.class)
	public void updateWhenIsNotFound() throws NotFoundException {
		BugDTO dto = BugTestUtil.createFormObject(BugTestUtil.ID, BugTestUtil.TITLE_UPDATED, BugTestUtil.DESCRIPTION_UPDATED, BugTestUtil.STATUS_UPDATED);
		when(repositoryMock.findOne(dto.getId())).thenReturn(null);

		service.update(dto);

		verify(repositoryMock, times(1)).findOne(dto.getId());
		verifyNoMoreInteractions(repositoryMock);
	}
}
