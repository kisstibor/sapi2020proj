package ro.sapientia2015.story.model;

public enum StoryStatus {
	TODO("To Do"),
	INPROGRESS("In Progress"),
	UNDERTESTING("Under Testing"),
	DONE("Done");
	
	private final String displayName;

	StoryStatus(final String name) {
        this.displayName = name;
    }

    public String getDisplayName() {
        return displayName;
    }
}
