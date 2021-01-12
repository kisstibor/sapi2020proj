package ro.sapientia2015.story.model;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;

/**
 * @author Kapas Krisztina
 */
@Entity
@Table(name="priority")
public class Priority
{
	public static final int MAX_LENGTH_NAME = 100;
	
    @Id
    @GeneratedValue( strategy = GenerationType.AUTO )
    private Long id;

    @Column( name = "creation_time", nullable = false )
    @Type( type="org.jadira.usertype.dateandtime.joda.PersistentDateTime" )
    private DateTime creationTime;

    @Column( name = "name", nullable = false, length = MAX_LENGTH_NAME )
    private String name;
    
    @Column( name = "modification_time", nullable = false )
    @Type( type="org.jadira.usertype.dateandtime.joda.PersistentDateTime" )
    private DateTime modificationTime;

    @Version
    private long version;
    
//    
//    @OneToMany(
//            mappedBy = "priority"
//        )
//    private List<Story> stories;
//    // = new ArrayList<Story>();

    public Priority() 
    {

    }

    public static Builder getBuilder( String name ) 
    {
        return new Builder( name );
    }

    public Long getId()
    {
        return id;
    }

    public DateTime getCreationTime() 
    {
        return creationTime;
    }

    public String getName() 
    {
        return name;
    }

    public long getVersion() 
    {
        return version;
    }
    
    public DateTime getModificationTime() 
    {
        return modificationTime;
    }

    @PrePersist
    public void prePersist() 
    {
        DateTime now 		= DateTime.now();
        creationTime 		= now;
        modificationTime	= now;
    }

    @PreUpdate
    public void preUpdate() 
    {
        modificationTime = DateTime.now();
    }

    public void update( String name ) 
    {
        this.name = name;
    }

    public static class Builder 
    {
        private Priority built;

        public Builder( String name )
        {
            built 		= new Priority();
            built.name	= name;
        }

        public Priority build() 
        {
            return built;
        }
    }

    @Override
    public String toString()
    {
        return ToStringBuilder.reflectionToString( this );
    }
}
