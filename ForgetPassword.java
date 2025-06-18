import java.util.*;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import java.io.EOFException;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

class ForgetPassword {
    protected String Answer;
    protected String UserName;

    public ForgetPassword(String file, Scanner input) {
        System.out.print("Enter your username: ");
        this.UserName = input.nextLine();
        if (UserValidator.usernameExists(file, this.UserName)) {
            // If the username exists, retrieve the security question
            String securityQuestion = UserValidator.getAccountByUsername(file, this.UserName).getQuestion();

            if (securityQuestion != null) {
                System.out.println("Security Question: " + securityQuestion);
                System.out.print("Enter your answer: ");
                String userAnswer = input.nextLine();

                // Check if the entered answer matches the correct answer
                if (userAnswer.equals(UserValidator.getAccountByUsername(file, this.UserName).getAnswer())) {
                    System.out.println("Security answer is correct. You can now change your password.");

                    changePassword(file, this.UserName, input);

                } else {
                    System.out.println("Incorrect answer. Access denied.");
                }
            } else {
                System.out.println("Error retrieving security question. Access denied.");
            }
        } else {
            System.out.println("Username does not exist. Access denied.");
        }
    }

    public static void changePassword(String file, String username, Scanner input) {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            List<Account> accounts = new ArrayList<>();

            // Read all objects from the file
            while (true) {
                try {
                    Object obj = ois.readObject();
                    if (obj instanceof Account) {
                        Account account = (Account) obj;
                        accounts.add(account);
                    }
                } catch (EOFException e) {
                    // End of the file reached
                    break;
                } catch (ClassNotFoundException e) {
                    // Handle class not found exception
                    e.printStackTrace(); // or log the exception
                }
            }

            // Modify the password for the specified username
            for (Account account : accounts) {
                if (account.getUSER().equals(username)) {
                    account.setPassword(input);
                }
            }

            // Write the modified objects back to the file
            try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file))) {
                for (Account account : accounts) {
                    oos.writeObject(account);
                }
            } catch (IOException e) {
                e.printStackTrace(); // or log the exception
            }

        } catch (IOException e) {
            e.printStackTrace(); // or log the exception
        }
    }

}
