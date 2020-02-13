package inc.bado.app.storage.generaStorage;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import inc.bado.app.models.General;

@Dao
public interface GeneralDao {

    @Query("SELECT * FROM general")
    LiveData<List<General>> getAllGeneral();

    @Insert
    void insertGeneral(General general);

    @Delete
    void deleteGeneral(General general);

}
