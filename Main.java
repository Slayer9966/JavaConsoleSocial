
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        String file = "accounts_record.ser";
        // UserRepositery.printAllObjectsFromFile(file);

        Scanner input = new Scanner(System.in);
        while (true) {
            System.out.println(
                    "\n\n-----Welcome To Slayer Media---------\n\nPress\n1 For Login\n2 For Registeration\n-1 To Exit:");
            String choice = input.nextLine();
            if (choice.equals("1")) {

                Login login = new Login(input);

                // Check if the login is successful

                if (login.CheckUserName(file)) {
                    if (login.CheckPassword(file)) {
                        System.out.println("\n\n\u001B[32m Welcome " + login.UserName + "\u001B[0m\n\n");

                        // Successfully logged in, display the friend management menu
                        Account user = UserValidator.getAccountByUsername(file, login.UserName);
                        displayUserManagementMenu(input, user, file);
                    } else {
                        System.out.println("\n\n\u001B[31m Credentials didn't match!!\u001B[0m\n\n");
                    }

                } else {
                    System.out.println("\n\n\u001B[31m User is not Registered \u001B[0m\n\n");
                }
            } else if (choice.equals("2")) {
                // to register new account

                new Register(file, input);

            } else if (choice.equals("-1")) {
                break;
            } else {
                System.out.println("\n\n\u001B[31m Please Enter Valid Input \u001B[0m\n\n");
            }
        }
        input.close();
    }

    private static void displayUserManagementMenu(Scanner scanner, Account account, String file) {
        String choice;
        while (true) {

            Account disAcc = UserValidator.getAccountByUsername(file, account.UserName);
            System.out.println("\nUser Management Menu:");
            System.out.println("1.   [+] Add Friend");
            if (!disAcc.Friends.isEmpty()) {
                System.out.println("2.   \u001B[31m[-] Remove Friend\u001B[0m");
            } else {
                System.out.println("2.   [-] Remove Friend");
            }
            if (!disAcc.pendingFriendRequests.isEmpty()) {
                System.out.println("3.   \u001B[32m[A] Accept Friend Request\u001B[0m");
                System.out.println("4.   \u001B[31m[R] Reject Friend Request\u001B[0m");
            } else {
                System.out.println("3.   [A] Accept Friend Request");
                System.out.println("4.   [R] Reject Friend Request");
            }
            if (!disAcc.Friends.isEmpty()) {
                System.out.println("5.   \u001B[32m[S] Show Friends\u001B[0m");
            } else {
                System.out.println("5.   [S] Show Friends");
            }
            if (!disAcc.pendingFriendRequests.isEmpty()) {
                System.out.println("6.   \u001B[36m[P] Show Pending Friend Requests\u001B[0m");
            } else {
                System.out.println("6.   [P] Show Pending Friend Requests");
            }
            if (!disAcc.pendingFriendRequestsSent.isEmpty()) {
                System.out.println("7.   \u001B[33m[S] Show Pending Sent Friend Requests\u001B[0m");
                System.out.println("8.   \u001B[31m[X] Remove Sent Requests\u001B[0m");
            } else {
                System.out.println("7.   [S] Show Pending Sent Friend Requests");
                System.out.println("8.   [X] Remove Sent Requests");
            }

            System.out.println("9.   \u001B[32m[+] Add a Post\u001B[0m");
            System.out.println("10.   \u001B[31m[D] Delete a Post\u001B[0m");

            System.out.println("11.  [L] Display Posts");
            System.out.println("12.  [F] Show Posts of Friends");
            if (!disAcc.LikedPosts.isEmpty()) {
                System.out.println("13.  \u001B[32m[L] Show Liked Posts\u001B[0m");
                System.out.println("14.  \u001B[31m[U] Remove Like from Posts\u001B[0m");
            } else {
                System.out.println("13.  [L] Show Liked Posts");
                System.out.println("14.  [U] Remove Like from Posts");
            }
            System.out.println("15.  \u001B[34m[S] To Search A user name\u001B[0m"); // Blue
            System.out.println("16.  \u001B[36m[P] To Change user privacy\u001B[0m"); // Cyan
            System.out.println("0.   \u001B[33m[O] Logout\u001B[0m"); // Yellow

            System.out.print("Choose an option: ");
            choice = scanner.nextLine();
            if (choice.equals("1")) {
                // Add Friend
                System.out.print("Enter friend's username: ");
                String friendUsername = scanner.nextLine();
                String username = account.UserName;
                Friends.addFriendToFriendList(file, friendUsername, username);
            } else if (choice.equals("2")) {
                // Remove Friend

                Friends.removeFriendFromFriendList(file, account.getUSER(), scanner);

            } else if (choice.equals("3")) {

                // Accept Friend Request

                Friends.acceptFriendFromPendingRequest(file, account.getUSER(), scanner);

            } else if (choice.equals("4")) {
                // Reject Friend Request

                Friends.rejectFriendFromPendingRequest(file, account.getUSER(), scanner);

            } else if (choice.equals("5")) {
                // Show Friends
                Friends.showFriendList(file, account.getUSER());

            } else if (choice.equals("6")) {

                // show pending requests

                Friends.showPendingRequests(file, account.getUSER());

            } else if (choice.equals("7")) {
                // show friends
                Friends.showSentList(file, account.getUSER());

            } else if (choice.equals("8")) {
                // remove friends
                Friends.removeFriendFromSentRequest(file, account.getUSER(), scanner);
            } else if (choice.equals("9")) {
                // add new post
                PostManager.AddPost(file, account.getUSER(), scanner);

            } else if (choice.equals("10")) {
                // remove post
                PostManager.RemovePost(file, account.getUSER(), scanner);

            } else if (choice.equals("11")) {
                // show post of user
                PostManager.showposts(file, account.getUSER());
            } else if (choice.equals("12")) {
                // show post of friends
                while (true) {
                    Friends.showFriendList(file, account.getUSER());
                    System.out.print(
                            "Select the friend whose post you want to see by entering the list number: \nOr press -1 to go back..");
                    String indexStr = scanner.nextLine();
                    if (indexStr.equals("-1")) {
                        break;
                    }
                    ArrayList<Account> accounts = UserRepositery.readAllObjectsFromFile(file);

                    if (UserValidator.containsOnlyNumbers(indexStr)) {
                        int index = Integer.parseInt(indexStr);

                        // Find the friend within the loop
                        for (Account account1 : accounts) {
                            if (account1.UserName.equals(account.UserName)) {
                                if (index >= 0 && index < account1.Friends.size()) {
                                    Account selectedFriend = account1.Friends.get(index);
                                    PostManager.showpostsofFriend(file, selectedFriend, account, scanner);

                                    break;
                                } else {
                                    System.out.println("Invalid index");
                                }
                            }
                        }

                    } else {
                        System.out.println("Invalid input. Please enter a valid number.");
                    }

                    System.out.print("Do you want to continue? (yes/no): ");
                    String continueChoice = scanner.nextLine();
                    if ("no".equalsIgnoreCase(continueChoice)) {
                        break;
                    }
                }
            } else if (choice.equals("13")) {
                PostManager.displayLikedPosts(account.UserName, file);

            } else if (choice.equals("14")) {
                PostManager.removeLikeFromLikedPost(account, scanner, file);

            } else if (choice.equals("15")) {
                SearchUser.search(scanner, file, account);

            } else if (choice.equals("16")) {
                ChangePrivacy.ChangePrivacyofUser(scanner, file, account.getUSER());

            } else if (choice.equals("0")) {
                System.out.println("Logging out...");
                break;
            } else {
                System.out.println("Invalid Input");
            }
        }
    }
}
