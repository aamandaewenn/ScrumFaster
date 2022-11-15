package ScrumFaster;

import java.io.Serializable;
import java.util.ArrayList;

public class User implements Serializable {

    private ArrayList<UserStory> stories;
    private String name;
    private String colour;
    // an id to access users based on and hash users
    private int id;

    /**
     * Class constructor.
     *
     * @param name   The name for the new user.
     * @param colour The colour for the new user.
     */
    public User(String name, String colour) {
        this.name = name;
        this.colour = colour;
        this.stories = new ArrayList<UserStory>();
        this.id = this.name.hashCode();
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

    public int getID(){
        return this.id;
    }

    // Class methods
    /**
     * Adds a UserStory object to the array list of assigned stories for this user.
     *
     * @param story The UserStory object to be assigned to the user.
     */
    public void addUserStory(UserStory story) {
        this.stories.add(story);
    }

    /**
     * Removes a UserStory object from the array list of this users assigned
     * stories.
     *
     * @param story The UserStory object to remove from this user.
     * @throws Exception If the story is not assigned to this user
     */
    public void removeUserStory(UserStory story) throws Exception {
        // check if the story is assigned to this user
        if (this.stories.contains(story)) {
            this.stories.remove(story);
        } else {
            throw new Exception("Story is not assigned to this user");
        }
    }

    /**
     * Compare two User objects for equality
     *
     * @param other The User object to compare to this user.
     */
    @Override
    public boolean equals(Object other) {
        if (other == null) {
            return false;
        }
        if (other == this) {
            return true;
        }
        if (!(other instanceof User)) {
            return false;
        }

        User user = (User) other;
        // all attributes must be equal
        if (this.name.equals(user.getName()) && this.colour.equals(user.getColour())
                && this.stories.size() == user.getStories().size()) {
            // check that user stories assigned to this user are equal
            for (int i = 0; i < this.stories.size(); i++) {
                if (!this.getStories().get(i).equals(user.getStories().get(i))) {
                    return false;
                }
            }
            return true;
        } else {
            return false;
        }
    }
}
