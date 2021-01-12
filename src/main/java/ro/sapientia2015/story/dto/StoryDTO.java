package ro.sapientia2015.story.dto;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import ro.sapientia2015.story.model.FixVersion;
import ro.sapientia2015.story.model.Story;

/**
 * @author Kiss Tibor
 */
public class StoryDTO {

    private Long id;

    @Length(max = Story.MAX_LENGTH_DESCRIPTION)
    private String description;

    @NotEmpty
    @Length(max = Story.MAX_LENGTH_TITLE)
    private String title;
    
    private long fixVersion;

    public StoryDTO() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
    
    public long getFixVersion() {
        return fixVersion;
    }

    public void setFixVersion(long fixVersion) {
        this.fixVersion = fixVersion;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
