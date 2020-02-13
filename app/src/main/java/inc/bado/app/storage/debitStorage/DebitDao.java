package inc.bado.app.storage.debitStorage;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import inc.bado.app.models.Debit;

@Dao
public interface DebitDao {

    @Query("SELECT * FROM debit")
    LiveData<List<Debit>> getAllDebit();

    @Insert
    void insertDebit(Debit item);

    @Delete
    void deleteDebit(Debit item);

}
