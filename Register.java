
import java.util.ArrayList;

import java.util.Scanner;

public class Register {

    public Register(String file, Scanner input) {
        Account newAccount = new Account(input, file);
        saveObjectToFile(file, newAccount);

    }

    // to save the object in the file
    private static void saveObjectToFile(String filePath, Account account) {

        // Add the new object to the list
        ArrayList<Account> accounts = UserRepositery.readAllObjectsFromFile(filePath);
        accounts.add(account);

        // Save the entire list to the file
        UserRepositery.writeAllObjectsToFile(filePath, accounts);
    }

}
