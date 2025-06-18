import java.util.*;
public class ChangePrivacy {
    public static void ChangePrivacyofUser(Scanner input,String file,String username){
        ArrayList<Account> accounts=UserRepositery.readAllObjectsFromFile(file);
        for(Account account:accounts){
            if(account.UserName.equals(username)){
                account.setAccountPrivacy(input);
            }
        }
    UserRepositery.writeAllObjectsToFile(file, accounts);
    
    }
}
