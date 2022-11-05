package ScrumFaster;

import java.util.ArrayList;

public class User {

    private ArrayList<UserStory> stories;
    private String name;
    private String colour;

    /**
     * Class constructor.
     *
     * @param name The name for the new user.
     * @param colour The colour for the new user.
     */
    public User(String name, String colour)
    {
        this.name = name;
        this.colour = colour;
        this.stories = new ArrayList<UserStory>();
    }

    // Getters and Setters.
    public ArrayList<UserStory> getStories() {
        return stories;
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
        this.stories.add(story);
    }


    /**
     * Removes a UserStory object from the array list of this users assigned stories.
     *
     * @param story The UserStory object to remove from this user.
     * @throws Exception If the story is not assigned to this user
     */
    public void removeUserStory(UserStory story) throws Exception {
        // check if the story is assigned to this user
        if (this.stories.contains(story)) {
            story.setUser(null);
            this.stories.remove(story);
        } else {
            throw new Exception("Story is not assigned to this user");
        }
    }
}

/*
 * Test Cases for User Class
 */
