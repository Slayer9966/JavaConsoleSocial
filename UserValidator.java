import java.io.EOFException;
import java.io.FileInputStream;

import java.io.IOException;
import java.io.ObjectInputStream;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UserValidator {

    // get the the username
    protected static Account getAccountByUsername(String filePath, String username) {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filePath))) {
            while (true) {
                try {
                    Object obj = ois.readObject();
                    if (obj instanceof Account) {
                        Account account = (Account) obj;
                        if (account.getUSER().equals(username)) {
                            return account;
                        }
                    }
                } catch (EOFException e) {
                    // End of the file reached, account not found
                    break;
                } catch (ClassNotFoundException e) {
                    // Handle class not found exception
                    e.printStackTrace(); // or log the exception
                }
            }
        } catch (IOException e) {
            e.printStackTrace(); // or log the exception
        }
        return null; // Account not found
    }

    // Checks that the string only cotais number or not
    public static boolean containsOnlyNumbers(String input) {
        // Use regular expression to check if the string contains only numbers
        return input.matches("\\d+");
    }

    // read all objects from the file and add it to the arraylist to add objects
    // accurately

    // Checkin if username exists

    protected static boolean usernameExists(String filePath, String username) {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filePath))) {
            while (true) {
                try {
                    Object obj = ois.readObject();
                    if (obj instanceof Account) {
                        if (((Account) obj).UserName.equals(username)) {
                            return true;
                        }
                    }
                } catch (EOFException e) {

                    break;
                } catch (ClassNotFoundException e) {

                    return false;
                }
            }

        } catch (IOException e) {
            return false;

        }
        return false;
    }

    // Checks if the email exists

    protected static boolean emailExists(String filePath, String email) {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filePath))) {
            while (true) {
                try {
                    Object obj = ois.readObject();
                    if (obj instanceof Account) {
                        if (((Account) obj).Email.equals(email)) {
                            return true;
                        }
                    }
                } catch (EOFException e) {

                    break;
                } catch (ClassNotFoundException e) {

                    return false;
                }
            }

        } catch (IOException e) {
            return false;

        }
        return false;
    }

    // Getting password for the username

    // Checks if the email is valid
    protected static boolean isValidEmail(String email) {
        // Define the regular expression for a valid email address
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";

        // Create a Pattern object
        Pattern pattern = Pattern.compile(emailRegex);

        // Create a matcher object
        Matcher matcher = pattern.matcher(email);

        // Return whether the email matches the pattern
        return matcher.matches();
    }

    // to update both the objects

}
