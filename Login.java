import java.util.*;

public class Login {

    protected String UserName;
    protected String Password;

    public Login(Scanner input) {
        System.out.println("WELCOME TO LOGIN PAGE ");
        this.SetUserName(input);
        this.SetPassword(input);

    }

    public void SetUserName(Scanner input) {
        System.out.print("Enter your UserName : ");
        this.UserName = input.nextLine();
    }

    public void SetPassword(Scanner input) {
        System.out.print("Enter your Password : ");
        String password = input.nextLine();
        while (password.length() < 8) {
            System.out.println("\u001B[31mPassword must be at least 8 characters\u001B[0m");
            System.out.print("Kindly re-enter the Password here : ");
            password = input.nextLine();
        }
        this.Password = password;
    }

    public boolean CheckUserName(String file) {
        if (UserValidator.usernameExists(file, this.UserName)) {
            return true;
        } else {
            return false;
        }
    }

    public boolean CheckPassword(String file) {
        String storedPassword = UserValidator.getAccountByUsername(file, this.UserName).getPassword();
        if (this.Password.equals(storedPassword)) {
            return true;
        } else {
            return false;
        }
    }
}
