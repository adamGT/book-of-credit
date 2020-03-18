package inc.bado.app.storage.userStorage;


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import inc.bado.app.models.User;

@Dao
public interface UserDao {
    @Query("SELECT * FROM user")
    LiveData<List<User>> getAllUser();

    @Insert
    void insertUser(User user);

    @Delete
    void deleteUser(User user);
}
