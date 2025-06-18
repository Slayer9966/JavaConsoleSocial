import java.io.EOFException;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class UserRepositery {

    protected static ArrayList<Account> readAllObjectsFromFile(String filePath) {
        ArrayList<Account> accounts = new ArrayList<>();

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filePath))) {
            while (true) {
                try {
                    Object obj = ois.readObject();
                    if (obj instanceof Account) {
                        accounts.add((Account) obj);
                    }
                } catch (EOFException e) {
                    break;
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return accounts;
    }

    // print all the accounts
    protected static void printAllObjectsFromFile(String filePath) {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filePath))) {
            while (true) {
                try {
                    Object obj = ois.readObject();
                    if (obj instanceof Account) {
                        System.out.println("Username: " + ((Account) obj).getUSER());
                        System.out.println("Password: " + ((Account) obj).getPassword());
                        // Add more fields as needed
                        System.out.println("---------------");
                    }
                } catch (EOFException e) {
                    System.out.println("End of the file reached no previous record found");
                    break;
                } catch (ClassNotFoundException e) {
                    System.out.println("No accounts in the file");
                }
            }

        } catch (IOException e) {
            System.out.println("No record in the file");
        }
    }

    protected static void writeAllObjectsToFile(String filePath, ArrayList<Account> accounts) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filePath))) {
            for (Account account : accounts) {
                oos.writeObject(account);
            }
        } catch (IOException e) {
            e.printStackTrace(); // Handle the exception appropriately, e.g., log it
        }
    }
}
