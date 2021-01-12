package ro.sapientia2015.story.service;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertNotSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;


import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.test.util.ReflectionTestUtils;

import ro.sapientia2015.story.dto.VacationDTO;
import ro.sapientia2015.story.exception.NotFoundException;
import ro.sapientia2015.story.model.Vacation;
import ro.sapientia2015.story.repository.VacationRepository;

public class RepositoryVacationServiceTest {
	private RepositoryVacationService service;

    private VacationRepository repositoryMock;

    private static final Long ID = 1L;
//	private static final Long ID_UPDATED = 2L;
    private static final String VACATION_STARTDATE= "2020-03-02";
    private static final String VACATION_STARTDATE_UPDATED = "2020-03-12";

    private static final String VACATION_ENDDATE= "2020-12-02";
    private static final String VACATION_ENDDATE_UPDATED = "2020-12-12";

    public static VacationDTO createFormObject(Long id, String vacation_startdate, String vacatin_enddate) {
    	VacationDTO dto = new VacationDTO();

        dto.setId(id);
        dto.setVacationStartDate(vacation_startdate);
        dto.setVacationEndDate(vacatin_enddate);

        return dto;
    }
    
    public static Vacation createModel(Long id, String vacation_startdate, String vacation_enddate) {
        Vacation model = Vacation.getBuilder(vacation_startdate, vacation_enddate)
                .build();

        ReflectionTestUtils.setField(model, "id", id);

        return model;
    }
    

    @Before
    public void setUp() {
        service = new RepositoryVacationService();

        repositoryMock = mock(VacationRepository.class);
        ReflectionTestUtils.setField(service, "repository", repositoryMock);
    }


	@Test
    public void add() {
        VacationDTO dto = createFormObject(null,VACATION_STARTDATE, VACATION_ENDDATE);

        service.add(dto);

        ArgumentCaptor<Vacation> vacationArgument = ArgumentCaptor.forClass(Vacation.class);
        verify(repositoryMock, times(1)).save(vacationArgument.capture());
        verifyNoMoreInteractions(repositoryMock);

        Vacation model = vacationArgument.getValue();
      
        assertNull(model.getId());
        assertEquals(dto.getVacationStartDate(), model.getVacationStartDate());
        assertEquals(dto.getVacationEndDate(), model.getVacationEndDate());
    }

	 @Test
	    public void deleteById() throws NotFoundException {
	        Vacation model = createModel(ID, VACATION_STARTDATE, VACATION_ENDDATE);
	        when(repositoryMock.findOne(ID)).thenReturn(model);

	        Vacation actual = service.deleteById(ID);

	        verify(repositoryMock, times(1)).findOne(ID);
	        verify(repositoryMock, times(1)).delete(model);
	        verifyNoMoreInteractions(repositoryMock);

	        assertEquals(model, actual);
	    }

	 @Test(expected = NotFoundException.class)
	    public void findByIdWhenIsNotFound() throws NotFoundException {
	        when(repositoryMock.findOne(ID)).thenReturn(null);

	        service.findById(ID);

	        verify(repositoryMock, times(1)).findOne(ID);
	        verifyNoMoreInteractions(repositoryMock);
	    }
	 
	 @Test
	    public void update() throws NotFoundException {
	        VacationDTO dto = createFormObject(ID, VACATION_STARTDATE_UPDATED, VACATION_ENDDATE_UPDATED);
	        Vacation model = createModel(ID, VACATION_STARTDATE, VACATION_ENDDATE);
	        when(repositoryMock.findOne(dto.getId())).thenReturn(model);

	        Vacation actual = service.update(dto);

	        verify(repositoryMock, times(1)).findOne(dto.getId());
	        verifyNoMoreInteractions(repositoryMock);

	        assertEquals(dto.getId(), actual.getId());
	        assertEquals(dto.getVacationStartDate(), actual.getVacationStartDate());

	        assertNotSame(model.getId(), actual.getId());
	        assertEquals(model.getVacationStartDate(), actual.getVacationStartDate());
	    }

	    @Test(expected = NotFoundException.class)
	    public void updateWhenIsNotFound() throws NotFoundException {
	        VacationDTO dto = createFormObject(ID, VACATION_STARTDATE_UPDATED, VACATION_ENDDATE_UPDATED);
	        when(repositoryMock.findOne(dto.getId())).thenReturn(null);

	        service.update(dto);

	        verify(repositoryMock, times(1)).findOne(dto.getId());
	        verifyNoMoreInteractions(repositoryMock);
	    }
}
