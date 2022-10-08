package ScrumFaster;

public class UserStory {
    private String title;
    private String description;
    private String assignee;
    private String acceptanceCriteria;
    private String status;
    private int priority;
    
    public UserStory(String title, String description, String assignee, String acceptanceCriteria, String status, int priority) {
        this.title = title;
        this.description = description;
        this.assignee = assignee;
        this.acceptanceCriteria = acceptanceCriteria;
        this.status = status;
        this.priority = priority;
    }

    public String getTitle() {
        return title;
    }
    
    public String getDescription() {
        return description;
    }

    public String getAssignee() {
        return assignee;
    }

    public String getAcceptanceCriteria() {
        return acceptanceCriteria;
    }

    public String getStatus() {
        return status;
    }

    public int getPriority() {
        return priority;
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

    public void setAcceptanceCriteria(String acceptanceCriteria) {
        this.acceptanceCriteria = acceptanceCriteria;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }
    
    public String toString() {
        return "Title: " + title + " Description: " + description + " Assignee: " + assignee + " Acceptance Criteria: " + acceptanceCriteria + " Status: " + status + " Priority: " + priority;
    }
}
