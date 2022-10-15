package ScrumFaster;

import java.util.ArrayList;

public class User {
    ArrayList<UserStory> assignedStories;
    String name;
    String colour;

    public User(String UserName, String TaskColour)
    {
        name = UserName;
        colour = TaskColour;
    }
}
