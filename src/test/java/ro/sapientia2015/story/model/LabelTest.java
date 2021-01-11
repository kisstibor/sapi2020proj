package ro.sapientia2015.story.model;

import org.junit.Test;

import ro.sapientia2015.story.model.Label;
import static junit.framework.Assert.*;


public class LabelTest {

    private String TITLE = "title";

    @Test
    public void buildWithMandatoryInformation() {
        Label built = Label.getBuilder(TITLE).build();

        assertNull(built.getId());
        assertNull(built.getCreationTime());
        assertEquals(TITLE, built.getTitle());
        assertEquals(0L, built.getVersion());
    }

    @Test
    public void buildWithAllInformation() {
        Label built = Label.getBuilder(TITLE)
                .build();

        assertNull(built.getId());
        assertNull(built.getCreationTime());
        assertEquals(TITLE, built.getTitle());
        assertEquals(0L, built.getVersion());
    }

    @Test
    public void prePersist() {
        Label label = new Label();
        label.prePersist();

        assertNull(label.getId());
        assertNotNull(label.getCreationTime());
        assertNull(label.getTitle());
        assertEquals(0L, label.getVersion());
    }
}
