
import java.util.ArrayList;

import java.util.Scanner;

public class Friends {

    public static void removeFriendFromSentRequest(String file, String username, Scanner input) {
        ArrayList<Account> accounts = UserRepositery.readAllObjectsFromFile(file);

        for (Account account : accounts) {
            if (account.getUSER().equals(username)) {
                account.removeSentRequest(input);
            }
        }

        UserRepositery.writeAllObjectsToFile(file, accounts);
    }

    public static void showFriendsOfFriend(String filepath, Account friend, Account account, Scanner input) {
        ArrayList<Account> accounts = UserRepositery.readAllObjectsFromFile(filepath);
        for (Account account1 : accounts) {
            if (account1.getUSER().equals(friend.getUSER())) {
                while (true) {
                    showFriendList(filepath, friend.getUSER());
                    System.out.print("\u001B[36m"); // Cyan color
                    System.out.print(
                            "Select the friend by entering the list number to Send Friend Request or press -1 to exit:\u001B[0m ");
                    String indexStr = input.nextLine();

                    if (UserValidator.containsOnlyNumbers(indexStr)) {
                        int index = Integer.parseInt(indexStr);

                        if (index >= 0 && index < account1.Friends.size()) {
                            Account friend1 = account1.Friends.get(index);
                            addFriendToFriendList(filepath, friend1.getUSER(), account.getUSER());

                        }

                        else {
                            System.out.println("\u001B[31mInvalid index. Please enter a valid index.\u001B[0m"); // Red
                                                                                                                 // color
                        }
                    } else if (indexStr.equals("-1")) {
                        break;
                    } else {
                        System.out.println("\u001B[31mInvalid input. Please enter a valid number.\u001B[0m"); // Red
                                                                                                              // color
                    }

                }
            }
        }
    }

    public static void rejectFriendFromPendingRequest(String file, String username, Scanner input) {
        ArrayList<Account> accounts = UserRepositery.readAllObjectsFromFile(file);

        for (Account account : accounts) {
            if (account.getUSER().equals(username)) {
                account.rejectFriendRequest(input);
            }
        }

        UserRepositery.writeAllObjectsToFile(file, accounts);
    }

    public static void acceptFriendFromPendingRequest(String file, String username, Scanner input) {
        ArrayList<Account> accounts = UserRepositery.readAllObjectsFromFile(file);

        for (Account account : accounts) {
            if (account.getUSER().equals(username)) {
                account.acceptFriendRequest(input);
            }
        }

        UserRepositery.writeAllObjectsToFile(file, accounts);
    }

    public static void addFriendToFriendList(String file, String friendUsername, String username) {
        ArrayList<Account> accounts = UserRepositery.readAllObjectsFromFile(file);

        Account friend = findAccountByUsername(accounts, friendUsername);

        if (friend != null) {
            for (Account account : accounts) {
                if (account.getUSER().equals(username)) {
                    account.addFriend(friend);
                }
            }
        } else {
            System.out.println("Sorry, User not Found!");
        }

        UserRepositery.writeAllObjectsToFile(file, accounts);
    }

    public static void removeFriendFromFriendList(String file, String username, Scanner input) {
        ArrayList<Account> accounts = UserRepositery.readAllObjectsFromFile(file);

        for (Account account : accounts) {
            if (account.getUSER().equals(username)) {
                account.removeFriend(input);
            }
        }

        UserRepositery.writeAllObjectsToFile(file, accounts);
    }

    protected static void showFriendList(String filePath, String username) {
        ArrayList<Account> accounts = UserRepositery.readAllObjectsFromFile(filePath);

        for (Account account : accounts) {
            if (account.getUSER().equals(username)) {
                account.displayFriends();
            }
        }
    }

    protected static void showSentList(String filePath, String username) {
        ArrayList<Account> accounts = UserRepositery.readAllObjectsFromFile(filePath);

        for (Account account : accounts) {
            if (account.getUSER().equals(username)) {
                account.displaySentRequests();
            }
        }
    }

    protected static void showPendingRequests(String filePath, String username) {
        ArrayList<Account> accounts = UserRepositery.readAllObjectsFromFile(filePath);

        for (Account account : accounts) {
            if (account.getUSER().equals(username)) {
                account.displayPendingFriendRequests();
            }
        }
    }

    private static Account findAccountByUsername(ArrayList<Account> accounts, String username) {
        for (Account account : accounts) {
            if (account.getUSER().equals(username)) {
                return account;
            }
        }
        return null;
    }

}
