package ro.sapientia2015.story.model;



import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.joda.time.DateTime;


@Entity
@Table(name="vacation")
public class Vacation {
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
	
    @Column(name = "vacationStartDate", nullable = false)
    private String vacationStartDate;
    
    @Column(name = "vacationEndDate", nullable = false)
    private String vacationEndDate;

    public Vacation() {
    }
    
    @PrePersist
    public void prePersist() {
//        DateTime now = DateTime.now();
//        vacationStartDate= now.toString();
//        vacationEndDate = now.toString();
    }

    @PreUpdate
    public void preUpdate() {
//    	 vacationStartDate= DateTime.now().toString();
//         vacationEndDate = DateTime.now().toString();
    }
    
    public void update(String vacationStartDate, String vacationEndDate) {
        this.vacationStartDate = vacationStartDate;
        this.vacationEndDate = vacationEndDate;
    }    
    
    public Long getId() {
        return id;
    }
    
    public String getVacationStartDate() {
        return vacationStartDate;
    }
    
    public String getVacationEndDate() {
        return vacationEndDate;
    }
    
    public static Builder getBuilder(String vacationStartDate, String vacationEndDate){
        return new Builder(vacationStartDate,vacationEndDate);
    }
    
    public static class Builder {

        private Vacation built;

        public Builder(String vacationStartDate, String vacationEndDate) {
            built = new Vacation();
            built.vacationStartDate = vacationStartDate;
            built.vacationEndDate = vacationEndDate;
        }

        public Vacation build() {
            return built;
        }
    }
    
    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
