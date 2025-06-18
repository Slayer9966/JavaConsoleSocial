
import java.io.Serializable;
import java.util.*;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

interface Postable {
    void createTextPost(Scanner input);

    void createImagePost(Scanner input);

    void removePost(Scanner input, ArrayList<Account> accounts);

    void displayPosts();
}

interface Friendable {
    void addFriend(Account friend);

    void removeFriend(Scanner input);

    void displayFriends();

}

public class Account implements Serializable, Friendable, Postable {
    // Data members
    protected String FirstName;
    protected String LastName;
    protected String UserName;
    protected String Password;
    protected String Email;
    protected String SecurityQuestion;
    protected String Question;
    protected boolean isPrivate;
    protected ArrayList<Account> Friends;
    protected ArrayList<Account> pendingFriendRequests;
    protected ArrayList<Account> pendingFriendRequestsSent;
    protected ArrayList<Post> posts;
    protected ArrayList<Post> LikedPosts;

    // Constructor
    protected Account(Scanner input, String file) {
        System.out.print("\u001B[36m"); // Cyan color
        System.out.print("Kindly enter your first name: ");
        this.FirstName = input.nextLine();
        System.out.print("Kindly enter your last name: ");
        this.LastName = input.nextLine();
        setUsername(input, file);
        setEmail(input, file);
        setPassword(input);
        setAccountPrivacy(input);
        setSecurityQuestion(input);
        this.Friends = new ArrayList<>();
        this.pendingFriendRequests = new ArrayList<>();
        this.pendingFriendRequestsSent = new ArrayList<>();
        this.posts = new ArrayList<>();
        this.LikedPosts = new ArrayList<>();
        System.out.println("User is Registered SuccessFully. Login and Enjoy...!");
    }

    // Methods for posts
    public void createTextPost(Scanner input) {
        System.out.print("\u001B[36m"); // Cyan color
        System.out.print("Write the post you want to post: ");
        String content = input.nextLine();
        TextPost textPost = new TextPost(this, content);
        posts.add(textPost);
        System.out.println("\u001B[32mText post created!\u001B[0m"); // Green color
    }

    public void createImagePost(Scanner input) {
        System.out.print("\u001B[36m"); // Cyan color
        System.out.println("Write the content of the post: ");
        String content = input.nextLine();
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new FileNameExtensionFilter("Image files", "jpg", "jpeg", "png", "gif"));

        int result = fileChooser.showOpenDialog(null);

        if (result == JFileChooser.APPROVE_OPTION) {
            String imageUrl = fileChooser.getSelectedFile().toURI().toString();

            ImagePost imagePost = new ImagePost(this, content, imageUrl);
            posts.add(imagePost);
            System.out.println("\u001B[32mImage post created!\u001B[0m"); // Green color
        } else {
            System.out.println("Image selection canceled.");
        }
    }

    public void removePost(Scanner input, ArrayList<Account> accounts) {
        while (true) {
            this.displayPosts();
            System.out.print("Select the post by entering the list number: \nPress -1 To Exit");
            String indexStr = input.nextLine();

            if (indexStr.equals("-1")) {
                break;
            }
            if (UserValidator.containsOnlyNumbers(indexStr)) {
                int index = Integer.parseInt(indexStr);

                if (index >= 0 && index < posts.size()) {
                    Post post = posts.get(index);
                    for (Account acc : accounts) {
                        acc.LikedPosts.remove(post);
                        // System.out.println(acc.LikedPosts.toString());
                    }
                    posts.remove(post);

                    System.out.println("\u001B[32mPost successfully deleted.\u001B[0m"); // Green color
                    break;
                } else {
                    System.out.println("\u001B[31mInvalid index. Please enter a valid index.\u001B[0m"); // Red color
                }
            } else {
                System.out.println("\u001B[31mInvalid input. Please enter a valid number.\u001B[0m"); // Red color
            }
        }
    }

    public void displayPosts() {
        if (posts.isEmpty()) {
            System.out.println("\u001B[33mNo posts available.\u001B[0m"); // Yellow color
        } else {
            int count = 0;
            System.out.println("\u001B[36mAll Posts:\u001B[0m"); // Cyan color
            for (Post post : posts) {
                System.out.print(count + ". ");
                post.display();
                System.out.println("-----");
                count = count + 1;
            }
        }
    }

    // Method to remove pendingFriendRequestsSent
    public void removeSentRequest(Scanner input) {
        displaySentRequests();
        while (true) {
            System.out.print("\u001B[36m"); // Cyan color
            System.out.print("Select the request by entering the list number: \nOr -1 to Go Back....");
            String indexStr = input.nextLine();
            if (indexStr.equals("-1")) {
                break;
            }
            if (UserValidator.containsOnlyNumbers(indexStr)) {
                int index = Integer.parseInt(indexStr);

                if (index >= 0 && index < pendingFriendRequestsSent.size()) {
                    Account account = pendingFriendRequestsSent.get(index);
                    pendingFriendRequestsSent.remove(account);
                    account.pendingFriendRequests.remove(this);
                    System.out.println("\u001B[32mFriend request to " + account.FirstName + " " + account.LastName
                            + " deleted.\u001B[0m"); // Green color
                    break;
                } else {
                    System.out.println("\u001B[31mInvalid index. Please enter a valid index.\u001B[0m"); // Red color
                }
            } else {
                System.out.println("\u001B[31mInvalid input. Please enter a valid number.\u001B[0m"); // Red color
            }
        }
    }

    // Method to display the Sent requests
    public void displaySentRequests() {
        if (pendingFriendRequestsSent.isEmpty()) {
            System.out.println("\u001B[33mYou don't have any Sent friend requests.\u001B[0m"); // Yellow color
        } else {
            System.out.println("\u001B[36mSent Friend Requests:\u001B[0m"); // Cyan color
            for (int i = 0; i < pendingFriendRequestsSent.size(); i++) {
                Account account = pendingFriendRequestsSent.get(i);
                System.out.println(i + ". " + account.FirstName + " " + account.LastName);
            }
        }
    }

    // Method to reject a friend request by index
    public void rejectFriendRequest(Scanner input) {
        displayPendingFriendRequests();

        while (true) {
            System.out.print("\u001B[36m"); // Cyan color
            System.out.print("Select the friend by entering the list number: \nOr Press -1 to Exit...");
            String indexStr = input.nextLine();
            if (indexStr.equals("-1")) {
                break;
            }

            if (UserValidator.containsOnlyNumbers(indexStr)) {
                int index = Integer.parseInt(indexStr);

                if (index >= 0 && index < pendingFriendRequests.size()) {
                    Account friend = pendingFriendRequests.get(index);
                    pendingFriendRequests.remove(friend);
                    friend.pendingFriendRequestsSent.remove(this);
                    System.out.println("\u001B[32mFriend request from " + friend.FirstName + " " + friend.LastName
                            + " rejected.\u001B[0m"); // Green color
                    break;
                } else {
                    System.out.println("\u001B[31mInvalid index. Please enter a valid index.\u001B[0m"); // Red color
                }
            } else {
                System.out.println("\u001B[31mInvalid input. Please enter a valid number.\u001B[0m"); // Red color
            }
        }
    }

    // Method to accept a friend request by index
    public void acceptFriendRequest(Scanner input) {
        displayPendingFriendRequests();

        while (true) {
            System.out.print("\u001B[36m"); // Cyan color
            System.out.print("Select the friend by entering the list number: \n-1 TO Exit");
            String indexStr = input.nextLine();
            if (indexStr.equals("-1")) {
                break;
            }

            if (UserValidator.containsOnlyNumbers(indexStr)) {
                int index = Integer.parseInt(indexStr);

                if (index >= 0 && index < pendingFriendRequests.size()) {
                    Account friend = pendingFriendRequests.get(index);
                    Friends.add(friend);
                    pendingFriendRequests.remove(friend);

                    friend.Friends.add(this);
                    friend.pendingFriendRequestsSent.remove(this);

                    System.out.println("\u001B[32mFriend request from " + friend.FirstName + " " + friend.LastName
                            + " accepted.\u001B[0m"); // Green color
                    break;
                } else {
                    System.out.println("\u001B[31mInvalid index. Please enter a valid index.\u001B[0m"); // Red color
                }
            } else {
                System.out.println("\u001B[31mInvalid input. Please enter a valid number.\u001B[0m"); // Red color
            }
        }
    }

    // Method to add a friend to the list
    public void addFriend(Account friend) {
        if (this.pendingFriendRequests.contains(friend)) {
            System.out.println("\u001B[33mYou have already received a friend request from this account.\u001B[0m"); // Yellow
                                                                                                                    // color
            return;
        }
        if (this.pendingFriendRequestsSent.contains(friend)) {
            System.out.println("\u001B[33mYou have already sent a friend request to this account.\u001B[0m"); // Yellow
                                                                                                              // color
            return;
        }
        if (this.Friends.contains(friend)) {
            System.out.println("\u001B[33mYou both are already friends.\u001B[0m"); // Yellow color
            return;
        }
        if (this.getUSER().equals(friend.getUSER())) {
            System.out.println("You can not send friend request to your own account");
            return;
        }
        friend.pendingFriendRequests.add(this);
        this.pendingFriendRequestsSent.add(friend);
        System.out.println("\u001B[32mFriend request sent to: " + friend.UserName + "\u001B[0m"); // Green color
    }

    // to remove friend
    public void removeFriend(Scanner input) {
        displayFriends();

        while (true) {
            System.out.print("\u001B[36m"); // Cyan color
            System.out.print("Select the friend by entering the list number: \nOr Press -1  to Exit: ");
            String indexStr = input.nextLine();
            if (indexStr.equals("-1")) {
                System.out.println("Getting Back....");
                break;
            }
            if (UserValidator.containsOnlyNumbers(indexStr)) {
                int index = Integer.parseInt(indexStr);

                if (index >= 0 && index < Friends.size()) {
                    Account friend = Friends.get(index);
                    Friends.remove(friend);
                    friend.Friends.remove(this);
                    System.out.println(
                            "\u001B[32mFriend " + friend.FirstName + " " + friend.LastName + " removed.\u001B[0m"); // Green
                                                                                                                    // color
                    break;
                } else {
                    System.out.println("\u001B[31mInvalid index. Please enter a valid index.\u001B[0m"); // Red color
                }
            } else {
                System.out.println("\u001B[31mInvalid input. Please enter a valid number.\u001B[0m"); // Red color
            }
        }
    }

    // Function to accept or reject a friend request

    // Function to show friendRequests
    public void displayPendingFriendRequests() {
        if (pendingFriendRequests.isEmpty()) {
            System.out.println("\u001B[33mYou don't have any pending friend requests.\u001B[0m"); // Yellow color
        } else {
            System.out.println("\u001B[36mPending Friend Requests:\u001B[0m"); // Cyan color
            for (int i = 0; i < pendingFriendRequests.size(); i++) {
                Account friend = pendingFriendRequests.get(i);
                System.out.println(i + ". " + friend.FirstName + " " + friend.LastName);
            }
        }
    }

    // To display friends
    public void displayFriends() {
        if (Friends.isEmpty()) {
            System.out.println("\u001B[33mYou don't have any friends.\u001B[0m"); // Yellow color
        } else {
            System.out.println("\u001B[36mFriends:\u001B[0m"); // Cyan color
            for (int i = 0; i < Friends.size(); i++) {
                Account friend = Friends.get(i);
                System.out.println(i + ". " + friend.FirstName + " " + friend.LastName);
            }
        }
    }

    // Setters
    public void setAccountPrivacy(Scanner input) {
        while (true) {
            System.out.println(
                    "What account privacy you want to set to your account\nPress 1 for 'Private'\nPress 2 for 'Public'\nPress -1 to go back:");
            String choice = input.nextLine();
            if (choice.equals("1")) {

                this.isPrivate = true;
                System.out.println("Your Account Privacy is set to Private");
                break;

            } else if (choice.equals("2")) {
                this.isPrivate = false;
                System.out.println("Your Account Privacy is set to Public");
                break;
            } else if (choice.equals("-1")) {
                System.out.println("Exiting...");
                break;
            } else {
                System.out.println("Kindly enter from the mentioned inputs");
            }
        }
    }

    public void setSecurityQuestion(Scanner input) {
        System.out.print("\u001B[34m"); // Blue color
        System.out.println("Kindly answer one of the following questions: ");
        System.out.println("1. What is your pet's name?");
        System.out.println("2. What is your favorite color?");
        System.out.println("3. What is the name of your grandmother?");
        System.out.println("4. Who is your favorite teacher?");
        System.out.println("5. What is the name of your primary school?\u001B[0m");
        System.out.print("Enter the number corresponding to your choice: ");
        String choice = input.nextLine();

        while (true) {
            if (choice.equals("1")) {
                String question = "What is your pet's name?";
                System.out.println(question);
                this.SecurityQuestion = input.nextLine();
                this.Question = question;
                break;
            } else if (choice.equals("2")) {
                String question = "What is your favorite color?";
                System.out.println(question);
                this.SecurityQuestion = input.nextLine();
                this.Question = question;
                break;
            } else if (choice.equals("3")) {
                String question = "What is the name of your grandmother?";
                System.out.println(question);
                this.SecurityQuestion = input.nextLine();
                this.Question = question;
                break;
            } else if (choice.equals("4")) {
                String question = "Who is your favorite teacher?";
                System.out.println(question);
                this.SecurityQuestion = input.nextLine();
                this.Question = question;
                break;
            } else if (choice.equals("5")) {
                String question = "What is the name of your primary school?";
                System.out.println(question);
                this.SecurityQuestion = input.nextLine();
                this.Question = question;
                break;
            } else {
                System.out.print("\u001B[31m"); // Red color
                System.out.println("Wrong input. Please select from the options below:");
                System.out.println("1. What is your pet's name?");
                System.out.println("2. What is your favorite color?");
                System.out.println("3. What is the name of your grandmother?");
                System.out.println("4. Who is your favorite teacher?");
                System.out.println("5. What is the name of your primary school?");
                System.out.print("Enter the number corresponding to your choice: ");
                choice = input.nextLine();
            }
        }
    }

    public void setUsername(Scanner input, String file) {
        while (true) {
            System.out.print("\u001B[34m"); // Blue color
            System.out.print("Kindly enter your UserName: ");
            String baseUsername = input.nextLine();

            if (UserValidator.usernameExists(file, baseUsername)) {
                System.out.print("\u001B[31m"); // Red color
                System.out.println("This UserName already exists");
                System.out.print("\u001B[0m"); // Reset color
                List<String> suggestions = generateUsernameSuggestions(file, baseUsername);

                if (!suggestions.isEmpty()) {
                    System.out.println("Suggestions:");
                    for (int i = 0; i < suggestions.size(); i++) {
                        System.out.println((i + 1) + ". " + suggestions.get(i));
                    }
                }

                System.out.print("\u001B[34m"); // Blue color
                System.out.print("Choose one of the suggestions or press Enter to use your own username: \\033[0m");
                String choice = input.nextLine();

                if (choice.isEmpty()) {
                    // If the user pressed Enter, use the entered username

                } else if (choice.matches("\\d+")) {
                    // If the user entered a number corresponding to a suggestion, use that
                    // suggestion
                    int index = Integer.parseInt(choice) - 1;
                    if (index >= 0 && index < suggestions.size()) {
                        this.UserName = suggestions.get(index);
                        break;
                    } else {
                        System.out.println("\\u001B[31mInvalid choice. Please try again.\\033[0m");
                    }
                } else {
                    System.out.println("\\u001B[31mInvalid input. Please try again.\\u001B[0m");
                }
            } else {
                this.UserName = baseUsername;
                break;
            }
        }
    }

    public void setEmail(Scanner input, String file) {
        while (true) {
            System.out.print("\u001B[34m"); // Blue color
            System.out.print("Kindly enter your Email: ");
            String email = input.nextLine();

            if (!UserValidator.isValidEmail(email)) {
                System.out.print("\u001B[31m"); // Red color
                System.out.println("Email is not valid");
                System.out.print("\u001B[0m"); // Reset color
            } else if (UserValidator.emailExists(file, email)) {
                System.out.print("\u001B[31m"); // Red color
                System.out.println("Email already exists!");
                System.out.print("\u001B[0m"); // Reset color
            } else {
                this.Email = email;
                break;
            }
        }
    }

    public void setPassword(Scanner input) {
        System.out.print("\u001B[34m"); // Blue color
        System.out.print("Kindly enter your Password: ");
        System.out.print("\u001B[0m"); // Reset color
        String password = input.nextLine();

        while (password.length() < 8) {
            System.out.print("\u001B[31m"); // Red color
            System.out.println("Password must be at least 8 characters");
            System.out.print("\u001B[0m"); // Reset color
            System.out.print("Kindly re-enter the Password: ");
            password = input.nextLine();
        }

        this.Password = password;
        System.out.print("\u001B[0m"); // Reset color

        System.out.print("Kindly re-enter the password: ");
        String rePassword = input.nextLine();

        while (!rePassword.equals(this.Password)) {
            System.out.print("\u001B[31m"); // Red color
            System.out.println("Password didn't matched");
            System.out.print("Kindly re-enter the Password: ");
            rePassword = input.nextLine();
        }

        System.out.print("\u001B[0m"); // Reset color
    }

    // Method To save object in file

    // Method to give user suggestions
    private List<String> generateUsernameSuggestions(String file, String baseUsername) {
        List<String> suggestions = new ArrayList<>();

        // Append numbers to the base username
        for (int i = 1; i <= 5; i++) {
            String suggestion = baseUsername + i;
            if (!UserValidator.usernameExists(file, suggestion)) {
                suggestions.add(suggestion);
            }
        }

        // Appending random characters to the base username
        for (int i = 0; i < 3; i++) {
            String randomSuffix = generateRandomString(3); // length of the random suffix is Adjustable
            String suggestion = baseUsername + randomSuffix;
            if (!UserValidator.usernameExists(file, suggestion)) {
                suggestions.add(suggestion);
            }
        }

        return suggestions;
    }

    // Method to generate a random string of a specified length
    private String generateRandomString(int length) {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder randomString = new StringBuilder();

        for (int i = 0; i < length; i++) {
            int index = (int) (Math.random() * characters.length());
            randomString.append(characters.charAt(index));
        }

        return randomString.toString();
    }

    // Getters
    public String getPassword() {
        return this.Password;
    }

    public String getUSER() {
        return this.UserName;
    }

    public String getQuestion() {
        return this.Question;
    }

    public String getAnswer() {
        return this.SecurityQuestion;
    }

    public String email() {
        return this.Email;
    }

    public ArrayList<Account> getFriends() {
        return Friends;
    }

    public ArrayList<Account> getPendingAccounts() {
        return pendingFriendRequests;
    }

    public ArrayList<Post> getPosts() {
        return posts;
    }

    public void ToString() {
        System.out.println("UserName : " + this.getUSER());
        System.out.println("Number of friends : " + this.Friends.size());
        System.out.println("Number of Posts : " + this.posts.size());
        if (isPrivate) {
            System.out.println("Privacy : Private");
        } else {
            System.out.println("Privacy : Public");
        }
        return;
    }
}