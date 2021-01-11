package ro.sapientia2015.story.dto;


import org.apache.commons.lang.builder.ToStringBuilder;

public class VacationDTO {

	private Long id;
    private String vacationStartDate;
    private String vacationEndDate;

    public VacationDTO() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getVacationStartDate() {
        return vacationStartDate;
    }

    public void setVacationStartDate(String vacationStartDate) {
        this.vacationStartDate = vacationStartDate;
    }


    public String getVacationEndDate() {
        return vacationEndDate;
    }

    public void setVacationEndDate(String vacationEndDate) {
        this.vacationEndDate = vacationEndDate;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}
