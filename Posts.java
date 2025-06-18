import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

interface Likeable {
    void like();
}

class Post implements Serializable {
    protected Account account;
    protected ArrayList<Account> AccountsThatLiked;
    protected String content;
    protected Date timestamp;
    protected int likes;

    public Post(Account account, String content) {
        this.account = account;
        this.content = content;
        this.likes = 0;
        this.timestamp = new Date();
        this.AccountsThatLiked = new ArrayList<>();
    }

    public void display() {
        System.out.println("Username: " + account.getUSER());
        System.out.println("Content: " + this.content);
        System.out.println("Timestamp: " + this.timestamp);
        System.out.println("Likes: " + this.likes);
    }
}

class TextPost extends Post implements Likeable {
    public TextPost(Account account, String content) {
        super(account, content);
    }

    @Override
    public void like() {
        likes++;
        System.out.println("Post liked!");
    }

    @Override
    public void display() {
        super.display();
        System.out.println("This is a text post");
    }
}

class ImagePost extends Post implements Likeable {
    private String imageUrl;

    public ImagePost(Account account, String content, String imageUrl) {
        super(account, content);
        this.imageUrl = imageUrl;
    }

    @Override
    public void like() {
        likes++;
        System.out.println("Post liked!");
    }

    @Override
    public void display() {
        System.out.println("Image: " + this.imageUrl);
        super.display();
        System.out.println("This is an image post");
    }

    public void displayImage() {
        System.out.println("Displaying image from URL: " + imageUrl);
    }
}
