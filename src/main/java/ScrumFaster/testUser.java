package ScrumFaster;

/* Test Cases for User Class */
public class testUser {

    public static void main(String[] args) {

        int tests = 0;
        int passed = 0;

        // Create a new user
        User testUser = new User("Chi", "#FF0000");

        tests++;
        // Check that the user attributes are correct
        if (testUser.getName().equals("Chi") && testUser.getColour().equals("#FF0000")) {
            passed++;
        } else {
            System.out.println("Error: User attributes not initiated properly");
        }

        tests++;
        // Change test user's name
        testUser.setName("Amanda");
        if (testUser.getName().equals("Amanda")) {
            passed++;
        } else {
            System.out.println("Error: User name not changed properly");
        }

        tests++;
        // Change test user's colour
        testUser.setColour("#00FF00");
        if (testUser.getColour().equals("#00FF00")) {
            passed++;
        } else {
            System.out.println("Error: User colour not changed properly");
        }

        tests += 2;
        // Create a new user story and assign to test user
        UserStory story1 = new UserStory("Katia", "Add Users", "Add users to board", "Backlog", 3);
        testUser.addUserStory(story1);

        if (testUser.getStories().size() == 1) {
            // check that user story was added
            passed++;
            // check that all attributes of the user story are correct
            if (testUser.getStories().get(0).getPersona().equals("Katia")
                    && testUser.getStories().get(0).getTitle().equals("Add Users")
                    && testUser.getStories().get(0).getDescription().equals("Add users to board")
                    && testUser.getStories().get(0).getStatus().equals("Backlog")
                    && testUser.getStories().get(0).getPriority() == 3) {
                passed++;
            } else {
                System.out.println("Error: User story attributes not initiated properly");
            }

        } else {
            System.out.println("Error: User story not added to user");
        }

        // add second user stories
        UserStory story2 = new UserStory("Katia", "Add User Stories", "Add user stories to board", "Backlog", 4);
        testUser.addUserStory(story2);

        tests++;
        // check that the user story was added
        if (testUser.getStories().size() == 2) {
            passed++;
        } else {
            System.out.println("Error: failed to add user story to user with 1 existing story");
        }

        UserStory story3 = new UserStory("Sam", "Add User Stories", "Add user stories to board", "Backlog", 4);
        testUser.addUserStory(story3);

        tests++;
        // check that the user story was added
        if (testUser.getStories().size() == 3) {
            passed++;
        } else {
            System.out.println("Error: failed to add user story to user with multiple existing stories");
        }

        tests ++;
        // remove user story
        try {
            testUser.removeUserStory(story2);
            if (testUser.getStories().size() == 2) {
                // check that the correct user story was removed
                if (testUser.getStories().get(0).getPersona().equals("Katia")
                        && testUser.getStories().get(0).getTitle().equals("Add Users")
                        && testUser.getStories().get(0).getDescription().equals("Add users to board")
                        && testUser.getStories().get(0).getStatus().equals("Backlog")
                        && testUser.getStories().get(0).getPriority() == 3
                        && testUser.getStories().get(1).getPersona().equals("Sam")
                        && testUser.getStories().get(1).getTitle().equals("Add User Stories")
                        && testUser.getStories().get(1).getDescription().equals("Add user stories to board")
                        && testUser.getStories().get(1).getStatus().equals("Backlog")
                        && testUser.getStories().get(1).getPriority() == 4) {
                    passed++;
                } else {
                    System.out.println("Error: User story not removed properly");
                }
            } else {
                System.out.println("Error: failed to remove user story from user with multiple existing stories");
            }
        } catch (Exception e) {
            System.out.println(
                    "Error: unexpected error thrown when removing user story from user with multiple existing stories");
        }

        // remove another user story
        try {
            tests++;
            testUser.removeUserStory(story3);
            // check that the user story was removed
            if (testUser.getStories().size() == 1) {
                // check that the correct user story was removed
                if (testUser.getStories().get(0).getPersona().equals("Katia")
                        && testUser.getStories().get(0).getTitle().equals("Add Users")
                        && testUser.getStories().get(0).getDescription().equals("Add users to board")
                        && testUser.getStories().get(0).getStatus().equals("Backlog")
                        && testUser.getStories().get(0).getPriority() == 3) {
                    passed++;
                } else {
                    System.out.println("Error: User story not removed properly");
                }
            } else {
                System.out.println("Error: failed to remove user story from user with 2 existing stories");
            }
        } catch (Exception e) {
            System.out.println(
                    "Error: unexpected exception thrown when removing user story from user with 2 existing stories");
        }

        // remove last user story
        try {
            tests++;
            testUser.removeUserStory(story1);
            // check that the user story was removed
            if (testUser.getStories().size() == 0) {
                passed++;
            } else {
                System.out.println("Error: failed to remove user story from user with 1 existing story");
            }
        } catch (Exception e) {
            System.out.println(
                    "Error: unexpected exception thrown when removing user story from user with 1 existing story");
        }

        // remove user story from empty list
        try {
            tests++;
            testUser.removeUserStory(story1);
            System.out.println("Error: failed to throw exception when removing user story from empty list");
        } catch (Exception e) {
            passed++;
        }

        // compare two users that are the same: user object to compare to is itself
        tests++;
        if (testUser.equals(testUser)) {
            passed++;
        } else {
            System.out.println("Error: failed to compare user to itself");
        }

        // compare two users that are the same: 
        // two user objects with same name and colour,
        // neither have any user stories
        tests++;
        User testUser2 = new User("Amanda", "#00FF00");
        if (testUser.equals(testUser2)) {
            passed++;
        } else {
            System.out.println("Error: failed to compare two users that are the same");
        }

        // compare two users that are different: 
        // two user objects with same name and colour,
        // but different user stories
        tests++;
        testUser.addUserStory(story1);
        if (!testUser.equals(testUser2)) {
            passed++;
        } else {
            System.out.println("Error: failed to compare two users that are different");
        }

        // compare two users that are the same: 
        // two user objects with same name, colour, and user stories
        tests++;
        testUser2.addUserStory(story1);
        if (testUser.equals(testUser2)) {
            passed++;
        } else {
            System.out.println("Error: failed to compare two users that are the same");
        }

        // compare two users that are different: object to compare to is null
        tests++;
        if (!testUser.equals(null)) {
            passed++;
        } else {
            System.out.println("Error: failed to compare user to null");
        }

        // compare two users that are different: object to compare to is not a user
        tests++;
        if (!testUser.equals(story1)) {
            passed++;
        } else {
            System.out.println("Error: failed to compare user to non-user object");
        }

        // compare two users that are different: two user objects with different attributes
        tests++;
        User testUser3 = new User("Katia", "#FF0000");
        if (!testUser.equals(testUser3)) {
            passed++;
        } else {
            System.out.println("Error: failed to compare two users that are different");
        }

        System.out.println("Testing complete: Passed " + passed + " out of " + tests + " tests.");
    }

}