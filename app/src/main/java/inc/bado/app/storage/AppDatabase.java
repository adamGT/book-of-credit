package inc.bado.app.storage;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import inc.bado.app.models.Credit;
import inc.bado.app.models.Debit;
import inc.bado.app.models.General;
import inc.bado.app.storage.creditStorage.CreditDao;
import inc.bado.app.storage.debitStorage.DebitDao;
import inc.bado.app.storage.generaStorage.GeneralDao;
import inc.bado.app.utils.DateTypeConverter;

@Database(entities = {Debit.class, Credit.class, General.class}, version = 1, exportSchema = false)
@TypeConverters(DateTypeConverter.class)
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase instance;

    public abstract DebitDao debitDao();
    public abstract CreditDao creditDao();
    public abstract GeneralDao generalDao();

    public static synchronized AppDatabase getInstance(Context context){
        if (instance == null){
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    AppDatabase.class, "shame_database")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }
}