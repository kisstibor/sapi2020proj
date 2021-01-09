package ro.sapientia2015.story.service;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.test.util.ReflectionTestUtils;

import ro.sapientia2015.story.*;
import ro.sapientia2015.story.dto.DailyDTO;
import ro.sapientia2015.story.exception.NotFoundException;
import ro.sapientia2015.story.model.Daily;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNull;
import static org.mockito.Mockito.*;

import ro.sapientia2015.story.repository.DailyRepository;

public class RepositoryDailyServiceTest {
	private RepositoryDailyService service;
	private DailyRepository repositoryMock;
	
	private String TITLE = "title";
    private String DESCRIPTION = "description";
    private String DURATION = "12";
    private String UPDATED_DURATION= "34";
    private String DATEE = "11-01-2021";
    private String UPDATED_DATEE = "17-06-2021";
    private DailyDTO dailyDTO;
    private Daily daily;
	
	@Before
	public void setUp() {
		service = new RepositoryDailyService();
		repositoryMock = mock(DailyRepository.class);
		
        ReflectionTestUtils.setField(service, "repository", repositoryMock);

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
	public void delete() {
		dailyDTO.setId(1L);
        ReflectionTestUtils.setField(daily, "id", 1L);
        
        when(repositoryMock.findOne(1L)).thenReturn(daily);
        
        try {
			Daily d = service.deleteById(1L);
			
			verify(repositoryMock, times(1)).findOne(1L);
	        verify(repositoryMock, times(1)).delete(daily);
	        verifyNoMoreInteractions(repositoryMock);

	        assertEquals(daily, d);
		} catch (NotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}        
	}
	
	@Test
	public void add() {
		service.add(dailyDTO);
		
		ArgumentCaptor<Daily> arguments = ArgumentCaptor.forClass(Daily.class);
		
		verify(repositoryMock, times(1)).save(arguments.capture());
        verifyNoMoreInteractions(repositoryMock);
        
        assertEquals(DATEE, arguments.getValue().getDatee());
        assertEquals(DURATION, arguments.getValue().getDuration());
        assertEquals(TITLE, arguments.getValue().getTitle());
        assertEquals(DESCRIPTION, arguments.getValue().getDescription());
        assertNull(arguments.getValue().getId());
	}
	
	@Test
	public void update() {
		dailyDTO.setId(1L);
        ReflectionTestUtils.setField(daily, "id", 1L);
		
		when(repositoryMock.findOne(dailyDTO.getId())).thenReturn(daily);
	
		Daily d = new Daily();

		try {
			d = service.update(dailyDTO);
		} catch (NotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
        verify(repositoryMock, times(1)).findOne(dailyDTO.getId());
        verifyNoMoreInteractions(repositoryMock);
        
        assertEquals(dailyDTO.getDatee(), d.getDatee());
        assertEquals(dailyDTO.getDescription(), d.getDescription());
        assertEquals(dailyDTO.getDuration(), d.getDuration());
        assertEquals(dailyDTO.getId(), d.getId());
        assertEquals(dailyDTO.getTitle(), d.getTitle());

	}
}
