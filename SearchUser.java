import java.util.*;

public class SearchUser {
    public static void search(Scanner input, String file, Account User) {
        outerWhile: while (true) {
            ArrayList<Account> accounts = UserRepositery.readAllObjectsFromFile(file);
            ArrayList<Account> searchedAccounts = new ArrayList<>();

            System.out.print("Enter the username: \nOr Press -1 to go back: ");
            String username = input.nextLine();
            if (username.equals("-1")) {
                break;
            } else if (username.equals(User.UserName)) {
                System.out.println("Sorry You can't search your own account here. Try Another....");
                continue;
            }

            int count = 0;
            for (Account account : accounts) {
                if (account.UserName.startsWith(username) && !account.UserName.equals(User.getUSER())) {
                    System.out.println(count + "." + account.UserName);
                    count++;
                    searchedAccounts.add(account);
                }
            }

            if (searchedAccounts.isEmpty()) {
                System.out.println("No matching users found.");
                continue;
            }

            System.out.print("Select the username by entering the list number or press -1 to exit: ");
            String indexStr = input.nextLine().trim();

            if (UserValidator.containsOnlyNumbers(indexStr)) {
                int index = Integer.parseInt(indexStr);

                if (index >= 0 && index < searchedAccounts.size()) {
                    Account account = searchedAccounts.get(index);
                    account.ToString();

                    while (true) {
                        System.out.println("Wanna see this account's friend or posts:\n1.Posts\n2.Friends\n3.Exit");
                        String choice = input.nextLine().trim();
                        int c = 0;

                        if (choice.equals("1")) {
                            for (Account account2 : account.Friends) {
                                if (account2.getUSER().equals(User.UserName)) {
                                    c = 1;
                                }
                            }
                            if (account.isPrivate == false) {
                                c = 1;
                            }
                            if (c == 1) {
                                PostManager.showpostsofFriend(file, account, User, input);
                            } else if (c == 0) {
                                System.out.println("Sorry, this account is private");
                            }
                        } else if (choice.equals("2")) {
                            for (Account account2 : account.Friends) {
                                if (account2.getUSER().equals(User.UserName)) {
                                    c = 1;
                                }
                            }
                            if (account.isPrivate == false) {
                                c = 1;
                            }
                            if (c == 1) {
                                Friends.showFriendsOfFriend(file, account, User, input);
                            }
                            if (c == 0) {
                                System.out.println("Sorry, this account is private");
                            }
                        } else {
                            System.out.println("Exiting....");
                            break;
                        }
                    }
                } else {
                    System.out.println("\u001B[31mInvalid index. Please enter a valid index.\u001B[0m"); // Red color
                }
            } else if (indexStr.equals("-1")) {
                System.out.println("Ok, you are exiting now.......");
                break;
            } else {
                System.out.println("\u001B[31mInvalid input. Please enter a valid number.\u001B[0m"); // Red color
            }

            while (true) {
                System.out.println("Do you want to continue searching for another user? (yes/no)");
                String st = input.nextLine().trim();

                if (st.equals("yes")) {
                    break;

                } else if (st.equals("no")) {
                    System.out.println("Ok, exiting....");
                    break outerWhile;
                } else {
                    System.out.println("Kindly enter a valid input (yes/no)");
                }
            }
        }
    }
}
