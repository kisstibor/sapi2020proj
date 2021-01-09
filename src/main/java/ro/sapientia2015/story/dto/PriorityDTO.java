package ro.sapientia2015.story.dto;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import ro.sapientia2015.story.model.Priority;
import ro.sapientia2015.story.model.Story;

/**
 * @author Kapas Krisztina
 */
public class PriorityDTO 
{

    private Long id;

    @NotEmpty
    @Length( max = Priority.MAX_LENGTH_NAME )
    private String name;

    public PriorityDTO()
    {
    }

    public Long getId()
    {
        return id;
    }

    public void setId( Long id )
    {
        this.id = id;
    }

    public String getName() 
    {
        return name;
    }

    public void setName( String name )
    {
        this.name = name;
    }

    @Override
    public String toString()
    {
        return ToStringBuilder.reflectionToString( this );
    }
}
