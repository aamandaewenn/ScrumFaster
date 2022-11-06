package ScrumFaster;

public class UserStory implements Comparable<UserStory> {
    private String persona;
    private String title;
    private String description;
    private User user;
    private String status;
    private int priority;
    private String colour;

    /* Class contructor for creating use story without assigned user */
    public UserStory(String persona, String title, String description, String status, int priority) {
        this.persona = persona;
        this.title = title;
        this.description = description;
        this.status = status;
        this.priority = priority;
        // set colour to white since there is no user assigned to the story.
        this.colour = "#FFFFFF";

    }

    /* Class constructor for creating user story with assigned user */
    public UserStory(String persona, String title, String description, User user, String status, int priority) {
        this.persona = persona;
        this.title = title;
        this.description = description;
        this.user = user;
        this.status = status;
        this.priority = priority;
        this.colour = this.user.getColour();
    }

    /* Getters and setters for attributes */
    public String getPersona() {
        return persona;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public User getUser() {
        return user;
    }

    public String getStatus() {
        return status;
    }

    public String getColour() {
        return this.colour;
    }

    public int getPriority() {
        return priority;
    }

    public void setPersona(String persona) {
        this.persona = persona;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public String toString() {
        return "Persona: " + persona + "Title: " + title + " Description: " + description + " Assignee: " + user
                + " Status: " + status + " Priority: " + priority;
    }

    /*
     * Compare user stories based on priority
     * 
     * @param story The user story to compare to this user story.
     */
    @Override
    public int compareTo(UserStory story) {
        return Integer.compare(this.getPriority(), story.getPriority());
    }

    /**
     * Compares two user stories for equality
     *
     * @param other The user story to compare to this user story.
     */
    @Override
    public boolean equals(Object other) {
        if (other == null) {
            return false;
        }
        if (other == this) {
            return true;
        }
        if (!(other instanceof UserStory)) {
            return false;
        }
        UserStory story = (UserStory) other;
        // all attributes must be equal for two user stories to be equal
        return this.getPersona().equals(story.getPersona())
                && this.getTitle().equals(story.getTitle())
                && this.getDescription().equals(story.getDescription()) 
                && this.getUser().equals(story.getUser())
                && this.getStatus().equals(story.getStatus()) 
                && this.getPriority() == story.getPriority();
    }
}