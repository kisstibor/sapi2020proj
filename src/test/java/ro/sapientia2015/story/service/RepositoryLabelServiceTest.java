package ro.sapientia2015.story.service;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.test.util.ReflectionTestUtils;

import ro.sapientia2015.story.LabelTestUtil;
import ro.sapientia2015.story.dto.LabelDTO;
import ro.sapientia2015.story.exception.NotFoundException;
import ro.sapientia2015.story.model.Label;
import ro.sapientia2015.story.repository.LabelRepository;
import ro.sapientia2015.story.service.RepositoryLabelService;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNull;
import static org.mockito.Mockito.*;


public class RepositoryLabelServiceTest {

    private RepositoryLabelService service;

    private LabelRepository repositoryMock;

    @Before
    public void setUp() {
        service = new RepositoryLabelService();

        repositoryMock = mock(LabelRepository.class);
        ReflectionTestUtils.setField(service, "repository", repositoryMock);
    }

    @Test
    public void add() {
        LabelDTO dto = LabelTestUtil.createFormObject(null, LabelTestUtil.TITLE);

        service.add(dto);

        ArgumentCaptor<Label> labelArgument = ArgumentCaptor.forClass(Label.class);
        verify(repositoryMock, times(1)).save(labelArgument.capture());
        verifyNoMoreInteractions(repositoryMock);

        Label model = labelArgument.getValue();

        assertNull(model.getId());
        assertEquals(dto.getTitle(), model.getTitle());
    }

    @Test
    public void findAll() {
        List<Label> models = new ArrayList<Label>();
        when(repositoryMock.findAll()).thenReturn(models);

        List<Label> actual = service.findAll();

        verify(repositoryMock, times(1)).findAll();
        verifyNoMoreInteractions(repositoryMock);

        assertEquals(models, actual);
    }

    @Test
    public void findById() throws NotFoundException {
        Label model = LabelTestUtil.createModel(LabelTestUtil.ID, LabelTestUtil.TITLE);
        when(repositoryMock.findOne(LabelTestUtil.ID)).thenReturn(model);

        Label actual = service.findById(LabelTestUtil.ID);

        verify(repositoryMock, times(1)).findOne(LabelTestUtil.ID);
        verifyNoMoreInteractions(repositoryMock);

        assertEquals(model, actual);
    }
}
