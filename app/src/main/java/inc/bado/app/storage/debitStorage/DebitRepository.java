package inc.bado.app.storage.debitStorage;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

import inc.bado.app.models.Debit;
import inc.bado.app.storage.AppDatabase;

//import javax.inject.Inject;

public class DebitRepository {

    private DebitDao debitDao;
    private LiveData<List<Debit>> allDebit;

    //    @Inject
    public DebitRepository (Application application){
        AppDatabase database = AppDatabase.getInstance(application);
        debitDao = database.debitDao();
        allDebit = debitDao.getAllDebit();
    }

    public void insetDebit(Debit debit){
        new InsertDebitAsyncTask(debitDao).execute(debit);
    }

    public void deleteDebit(Debit debit){
        new DeleteDebitAsyncTask(debitDao).execute(debit);
    }

    public LiveData<List<Debit>> getEveryDebit (){
        return allDebit;
    }



    //don't use AsyncTask

    private static class InsertDebitAsyncTask extends AsyncTask<Debit ,Void ,Void>{
        private DebitDao debitDao;

        private InsertDebitAsyncTask(DebitDao debitDao){
            this.debitDao = debitDao;
        }

        @Override
        protected Void doInBackground(Debit... debits) {
            debitDao.insertDebit(debits[0]);
            return null;
        }
    }
    private static class DeleteDebitAsyncTask extends AsyncTask<Debit ,Void ,Void>{
        private DebitDao debitDao;

        private DeleteDebitAsyncTask(DebitDao debitDao){
            this.debitDao = debitDao;
        }

        @Override
        protected Void doInBackground(Debit... debits) {
            debitDao.deleteDebit(debits[0]);
            return null;
        }
    }

}
