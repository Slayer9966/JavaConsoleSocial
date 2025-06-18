import java.util.ArrayList;
import java.util.Scanner;

public class PostManager {

    public static void showposts(String filepath, String username) {

        Account account = UserValidator.getAccountByUsername(filepath, username);
        if (account != null) {
            account.displayPosts();
            return;
        } else {
            System.out.println("UserName don't exist");
            return;
        }

    }

    public static void showpostsofFriend(String filepath, Account friend, Account account, Scanner scanner) {
        showposts(filepath, friend.UserName);
        while (true) {

            System.out.print("Do you wanna like the post? \nPress 1 for 'Yes' \nPress 2 for 'No'");
            String indexStr = scanner.nextLine();

            if (UserValidator.containsOnlyNumbers(indexStr)) {
                int index = Integer.parseInt(indexStr);

                if (index == 1) {
                    while (true) {
                        showposts(filepath, friend.UserName);
                        System.out.println(
                                "Kindly select from these post by entering the list number : \nOr press -1 to exit");
                        String indexfr = scanner.nextLine();
                        if (indexfr.equals("-1")) {
                            break;
                        }

                        if (UserValidator.containsOnlyNumbers(indexfr)) {
                            int indexi = Integer.parseInt(indexfr);

                            if (indexi >= 0 && indexi < friend.posts.size()) {
                                Post post = friend.posts.get(indexi);
                                likePost(account, friend, scanner, post, filepath);

                                break;
                            } else {
                                System.out.println("Invalid index. Please enter a valid index.");
                            }
                        } else {
                            System.out.println("Invalid input. Please enter a valid number.");
                        }
                    }
                    break;
                } else if (index == 2) {
                    System.out.println("exiting ......");
                    break;
                } else {
                    System.out.println("Invalid input");
                }
            } else {
                System.out.println("Invalid input. Please enter a valid number.");
            }
        }
    }

    public static void AddPost(String filepath, String username, Scanner input) {
        ArrayList<Account> accounts = UserRepositery.readAllObjectsFromFile(filepath);

        for (Account account : accounts) {
            if (account.UserName.equals(username)) {
                while (true) {
                    System.out.println("Which type of Post you wanna post\n1.Text\n2.Image\nPress -1 to Go Back");
                    String choose = input.nextLine();
                    if (choose.equals("1")) {
                        account.createTextPost(input);
                        break;
                    } else if (choose.equals("2")) {
                        account.createImagePost(input);
                        break;
                    } else if (choose.equals("-1")) {
                        break;
                    } else {
                        System.out.println("Kindly select from the given options");
                    }
                }
            }
        }
        UserRepositery.writeAllObjectsToFile(filepath, accounts);
    }

    public static void RemovePost(String filepath, String username, Scanner input) {
        ArrayList<Account> accounts = UserRepositery.readAllObjectsFromFile(filepath);

        for (Account account : accounts) {
            if (account.UserName.equals(username)) {
                account.removePost(input, accounts);
            }
        }
        UserRepositery.writeAllObjectsToFile(filepath, accounts);
    }

    public static void likePost(Account user, Account friend, Scanner input, Post post, String file) {
        ArrayList<Account> accounts = UserRepositery.readAllObjectsFromFile(file);

        // Find the friend's account in the list of accounts
        for (Account account : accounts) {

            if (account.getUSER().equals(friend.getUSER())) {
                // Find the post in the friend's account
                for (Post p : account.posts) {
                    if (p.timestamp.equals(post.timestamp)) {
                        // Update the post in the friend's account
                        for (Account account1 : accounts) {
                            if (account1.getUSER().equals(user.getUSER()) && !account1.LikedPosts.contains(p)) {
                                account1.LikedPosts.add(p);
                                if (p instanceof TextPost) {
                                    ((TextPost) p).like();
                                } else if (p instanceof ImagePost) {
                                    ((ImagePost) p).like();
                                }
                                p.AccountsThatLiked.add(user);
                                System.out.println("Post liked successfully !!");
                                UserRepositery.writeAllObjectsToFile(file, accounts);
                                return;

                            }
                        }

                        System.out.println("You already liked this post !!");

                        return;

                    }
                }
            }
        }

    }

    public static void displayLikedPosts(String username, String file) {
        ArrayList<Account> accounts = new ArrayList<>();
        accounts = UserRepositery.readAllObjectsFromFile(file);

        for (Account account : accounts) {

            if (account.getUSER().equals(username)) {
                for (Post likedpost : account.LikedPosts) {
                    System.out.println("You liked the post : ");
                    likedpost.display();
                    System.out.println("---------------");
                }
            }
        }
    }

    public static void displayPostLikes(Post post) {
        System.out.println("Users who liked the post:");
        for (Account likedAccount : post.AccountsThatLiked) {
            System.out.println(likedAccount.FirstName + " " + likedAccount.LastName);
        }
    }

    public static void removeLikeFromLikedPost(Account user, Scanner scanner, String file) {
        ArrayList<Account> accounts = UserRepositery.readAllObjectsFromFile(file);

        // Display liked posts for the user
        displayLikedPosts(user.getUSER(), file);

        System.out.print("Select the liked post by entering the list number: ");
        String indexStr = scanner.nextLine();

        if (UserValidator.containsOnlyNumbers(indexStr)) {
            int index = Integer.parseInt(indexStr);

            for (Account account : accounts) {
                if (account.getUSER().equals(user.getUSER())) {
                    if (index >= 0 && index < account.LikedPosts.size()) {
                        Post likedPost = account.LikedPosts.get(index);
                        for (Post post : account.LikedPosts) {
                            if (post.timestamp.equals(likedPost.timestamp)) {
                                likedPost.likes--;

                                // Remove the user from the list of accounts that liked the post
                                likedPost.AccountsThatLiked.remove(user);

                                // Remove the post from the liked posts array
                                account.LikedPosts.remove(likedPost);

                                System.out.println("You removed the like from the post:");
                                likedPost.display();
                                System.out.println("---------------");

                                // Update the modified account in the accounts list
                                UserRepositery.writeAllObjectsToFile(file, accounts);
                                return;
                            }
                        }
                        // Remove the like from the post

                    } else {
                        System.out.println("Invalid index. Please enter a valid index.");
                    }
                }
            }
        } else {
            System.out.println("Invalid input. Please enter a valid number.");
        }

        System.out.println("Failed to remove the like.");
    }

}
