package ScrumFaster;

import java.util.ArrayList;

public class User {

    private ArrayList<UserStory> assignedStories;
    private String name;
    private String colour;

    /**
     * Class constructor.
     *
     * @param UserName The name for the new user.
     * @param TaskColour The colour for the new user.
     */
    public User(String UserName, String TaskColour)
    {
        name = UserName;
        colour = TaskColour;
        assignedStories = new ArrayList<UserStory>();
    }

    // Getters and Setters.
    public ArrayList<UserStory> getStories() {
        return assignedStories;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getColour() {
        return colour;
    }

    public void setColour(String colour) {
        this.colour = colour;
    }

    // Class methods
    /**
     * Adds a UserStory object to the array list of assigned stories for this user.
     *
     * @param story The UserStory object to be assigned to the user.
     */
    public void addUserStory(UserStory story){
        this.assignedStories.add(story);
    }


    /**
     * Removes a UserStory object from the array list of this users assigned stories.
     *
     * @param story The UserStory object to remove from this user.
     */
    public void removeStoryFromThisUser(UserStory story){
        this.assignedStories.remove(story);
    }
}
