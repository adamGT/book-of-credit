package inc.bado.app.models;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "user")
public class User {
    @PrimaryKey(autoGenerate = false)
    @NonNull
    public String uID;

    @ColumnInfo(name = "user_name")
    private String name;
    @ColumnInfo(name = "user_email")
    private String email;
    @ColumnInfo(name = "user_password")
    private String password;
    @ColumnInfo(name = "user_token")
    private String token;


    @Ignore
    public User(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public User(String uID,String name, String email, String token, String password) {
        this.uID = uID;
        this.name = name;
        this.email = email;
        this.token = token;
        this.password = password;
    }

    @Ignore
    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getuID() {
        return uID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public String getToken() {
        return token;
    }
}
