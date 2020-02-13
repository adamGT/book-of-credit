package inc.bado.app.storage.creditStorage;
import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

import inc.bado.app.models.Credit;
import inc.bado.app.storage.AppDatabase;

//import javax.inject.Inject;

public class CreditRepository {

    private CreditDao creditDao;
    private LiveData<List<Credit>> allDebit;

    //    @Inject
    public CreditRepository (Application application){
        AppDatabase database = AppDatabase.getInstance(application);
        creditDao = database.creditDao();
        allDebit = creditDao.getAllCredit();
    }

    public void insetCredit(Credit credit){
        new InsertCreditAsyncTask(creditDao).execute(credit);
    }

    public void deleteCredit(Credit credit){
        new DeleteCreditAsyncTask(creditDao).execute(credit);
    }

    public LiveData<List<Credit>> getEveryCredit (){
        return allDebit;
    }



    //don't use AsyncTask

    private static class InsertCreditAsyncTask extends AsyncTask<Credit ,Void ,Void>{
        private CreditDao creditDao;

        private InsertCreditAsyncTask(CreditDao creditDao){
            this.creditDao = creditDao;
        }

        @Override
        protected Void doInBackground(Credit... credits) {
            creditDao.insertCredit(credits[0]);
            return null;
        }
    }
    private static class DeleteCreditAsyncTask extends AsyncTask<Credit ,Void ,Void>{
        private CreditDao creditDao;

        private DeleteCreditAsyncTask(CreditDao creditDao){
            this.creditDao = creditDao;
        }

        @Override
        protected Void doInBackground(Credit... credits) {
            creditDao.deleteCredit(credits[0]);
            return null;
        }
    }

}
