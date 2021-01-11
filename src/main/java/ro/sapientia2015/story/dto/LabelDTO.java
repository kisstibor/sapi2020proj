package ro.sapientia2015.story.dto;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import ro.sapientia2015.story.model.Label;

public class LabelDTO {
	
    private Label.Builder builder = new Label.Builder();

    private Long id;

    @NotEmpty
    @Length(max = 20)
    private String title;

    public LabelDTO() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

	public Label.Builder getBuilder() {
		return builder;
	}

	public void setBuilder(Label.Builder builder) {
		this.builder = builder;
	}
}
