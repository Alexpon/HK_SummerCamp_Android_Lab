package mbp.alexpon.com.hkbike;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by apple on 15/8/4.
 */
public class StoreRank {
    public static final String SP_NAME = "rankDetails";
    SharedPreferences userRankDatabase;

    public StoreRank(Context context){
        userRankDatabase = context.getSharedPreferences(SP_NAME, 0);
    }

    public void storeRank(User[] user){
        SharedPreferences.Editor spEditor = userRankDatabase.edit();

        spEditor.putString("username1", user[0].username);
        spEditor.putInt("age1", user[0].age);
        spEditor.putString("distance1", user[0].dis);

        spEditor.putString("username2", user[1].username);
        spEditor.putInt("age2", user[1].age);
        spEditor.putString("distance2", user[1].dis);

        spEditor.putString("username3", user[2].username);
        spEditor.putInt("age3", user[2].age);
        spEditor.putString("distance3", user[2].dis);

        spEditor.putString("username4", user[3].username);
        spEditor.putInt("age4", user[3].age);
        spEditor.putString("distance4", user[3].dis);

        spEditor.putString("username5", user[4].username);
        spEditor.putInt("age5", user[4].age);
        spEditor.putString("distance5", user[4].dis);

        spEditor.commit();
    }

    public User[] getRank(){
        User[] userRank = new User[5];

        String username = userRankDatabase.getString("username1", "");
        int age = userRankDatabase.getInt("age1", -1);
        String dis = userRankDatabase.getString("distance1", "");
        userRank[0] = new User(username, age, dis);

        username = userRankDatabase.getString("username2", "");
        age = userRankDatabase.getInt("age2", -1);
        dis = userRankDatabase.getString("distance2", "");
        userRank[1] = new User(username, age, dis);

        username = userRankDatabase.getString("username3", "");
        age = userRankDatabase.getInt("age3", -1);
        dis = userRankDatabase.getString("distance3", "");
        userRank[2] = new User(username, age, dis);

        username = userRankDatabase.getString("username4", "");
        age = userRankDatabase.getInt("age4", -1);
        dis = userRankDatabase.getString("distance4", "");
        userRank[3] = new User(username, age, dis);

        username = userRankDatabase.getString("username5", "");
        age = userRankDatabase.getInt("age5", -1);
        dis = userRankDatabase.getString("distance5", "");
        userRank[4] = new User(username, age, dis);

        return userRank;
    }
}
