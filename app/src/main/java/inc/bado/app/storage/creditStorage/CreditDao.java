package inc.bado.app.storage.creditStorage;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import inc.bado.app.models.Credit;
@Dao
public interface CreditDao {


    @Query("SELECT * FROM credit")
    LiveData<List<Credit>> getAllCredit();

    @Insert
    void insertCredit(Credit item);

    @Delete
    void deleteCredit(Credit item);

}
