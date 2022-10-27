package ScrumFaster;

public class UserStory implements Comparable<UserStory>{
    private String persona;
    private String title;
    private String description;
    private String assignee;
    private String status;
    private int priority;
    
    public UserStory(String persona, String title, String description, String assignee, String status, int priority) {
        this.persona = persona;
        this.title = title;
        this.description = description;
        this.assignee = assignee;
        this.status = status;
        this.priority = priority;
    }

    public String getPersona() {return persona; }

    public String getTitle() {
        return title;
    }
    
    public String getDescription() {
        return description;
    }

    public String getAssignee() {
        return assignee;
    }

    public String getStatus() {
        return status;
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

    public void setAssignee(String assignee) {
        this.assignee = assignee;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }
    
    public String toString() {
        return "Persona: " + persona + "Title: " + title + " Description: " + description + " Assignee: " + assignee + " Status: " + status + " Priority: " + priority;
    }

    public int compareTo(UserStory story) {
        return Integer.compare(this.priority, story.priority) * -1;
    }
}
