package mbp.alexpon.com.hkbike;

/**
 * Created by apple on 15/7/30.
 */
public class User {
    String name, username, password, dis;
    int age;

    public User(String name, int age, String username, String password, String dis){
        this.name = name;
        this.age = age;
        this.username = username;
        this.password = password;
        this.dis = dis;
    }

    public User(String username, String password){
        this.name = "";
        this.age = -1;
        this.username = username;
        this.password = password;
        this.dis = "";
    }

    public User(String username, int age, String dis){
        this.name = "";
        this.age = age;
        this.username = username;
        this.password = "";
        this.dis = dis;
    }



}
