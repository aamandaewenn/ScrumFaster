package ScrumFaster;

// /* Test Cases for USer Story Class */
public class TestUserStory {
    public static void main(String[] args) {
        int tests = 0;
        int passed = 0;

        // Create a user story with no assigned user
        tests++;
        UserStory story1 = new UserStory("Katia", "Create User Story",
                "Create a user story to complete during a sprint", "To Do", 5);

        // check that story was created
        if (story1 != null) {
            passed++;

            // check that all attributes of the user story are correct
            tests++;
            if (story1.getPersona().equals("Katia")
                    && story1.getTitle().equals("Create User Story")
                    && story1.getDescription().equals("Create a user story to complete during a sprint")
                    && story1.getStatus().equals("To Do")
                    && story1.getPriority() == 5
                    && story1.getColour().equals("#FFFFFF")) {
                passed++;
            } else {
                System.out.println("Error: User story added, but attributes are not initiated properly");
            }

        } else {
            System.out.println("Error: User story not created");
        }

        // Create a user story with an assigned user
        tests++;
        User user1 = new User("Chi", "#FF0000");
        UserStory story2 = new UserStory("Katia", "Create User Story",
                "Create a user story to complete during a sprint", user1, "To Do", 1);

        // check that story was created
        if (story2 != null) {
            passed++;

            // check that all attributes of the user story are correct
            tests++;
            if (story2.getPersona().equals("Katia")
                    && story2.getTitle().equals("Create User Story")
                    && story2.getDescription().equals("Create a user story to complete during a sprint")
                    && story2.getStatus().equals("To Do")
                    && story2.getPriority() == 1
                    && story2.getColour().equals("#FF0000")) {
                passed++;
                user1.addUserStory(story2);
            } else {
                System.out.println("Error: User story added, but attributes are not initiated properly");
            }

        } else {
            System.out.println("Error: User story not created");
        }

        // check that user story was added to user
        tests++;
        if (user1.getStories().size() == 1) {
            passed++;

            // check that the right user story was added
            tests++;
            if (user1.getStories().get(0).equals(story2)) {
                passed++;
            } else {
                System.out.println("Error: incorrect user story added to user");
            }
        } else {
            System.out.println("Error: User story not added to user");
        }

        // add user story to user with existing user stories
        tests++;
        UserStory story3 = new UserStory("Katia", "Delete User Story", "Delete a user story from the board", user1,
                "To Do", 2);

        // check that story was created
        if (story3 != null) {
            passed++;

            // check that all attributes of the user story are correct
            tests++;
            if (story3.getPersona().equals("Katia")
                    && story3.getTitle().equals("Delete User Story")
                    && story3.getDescription().equals("Delete a user story from the board")
                    && story3.getStatus().equals("To Do")
                    && story3.getPriority() == 2
                    && story3.getColour().equals("#FF0000")) {
                passed++;
                user1.addUserStory(story3);
            } else {
                System.out.println("Error: User story added, but attributes are not initiated properly");
            }

        } else {
            System.out.println("Error: User story not created");
        }

        tests++;
        // check that user story was added to user
        if (user1.getStories().size() == 2) {
            passed++;

            // check that the right user story was added
            tests++;
            if (user1.getStories().get(1).equals(story3)) {
                passed++;
            } else {
                System.out.println("Error: incorrect user story added to user");
            }
        } else {
            System.out.println("Error: User story not added to user with existing stories");
        }

        // add one more user story to user with existing user stories
        tests++;
        UserStory story4 = new UserStory("Katia", "Edit User Story", "Edit a user story on the board", user1, "To Do",
                3);
        user1.addUserStory(story4);

        // check that user story was added to user
        if (user1.getStories().size() == 3) {
            passed++;

            // check that the right user story was added
            tests++;
            if (user1.getStories().get(2).equals(story4)) {
                passed++;
            } else {
                System.out.println("Error: Failed to add correct user story to user with multiple existing stories");
            }
        } else {
            System.out.println("Error: Failed to add user story to user with multiple existing stories");
        }

        // comparing user stories with different priority
        tests++;
        if (story1.compareTo(story2) != 0) {
            passed++;
        } else {
            System.out.println("Error: Failed to compare user story with different priority");
        }

        // test setter for priority
        tests++;
        story2.setPriority(5);
        if (story1.getPriority() == 5) {
            passed++;
        } else {
            System.out.println("Error: Failed to set priority");
        }

        // comparing user stories with same priority
        tests++;
        if (story1.compareTo(story2) == 0) {
            passed++;
        } else {
            System.out.println("Error: Failed to compare user story with same priority");
        }

        System.out.println("Testing complete: Passed " + passed + " out of " + tests + " tests.");

    }
}