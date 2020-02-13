package inc.bado.app.storage.generaStorage;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

import inc.bado.app.models.Debit;
import inc.bado.app.models.General;
import inc.bado.app.storage.AppDatabase;

//import javax.inject.Inject;

public class GeneralRepository {

    private GeneralDao generalDao;
    private LiveData<List<General>> allGeneral;

    //    @Inject
    public GeneralRepository (Application application){
        AppDatabase database = AppDatabase.getInstance(application);
        generalDao = database.generalDao();
        allGeneral = generalDao.getAllGeneral();
    }

    public void insetGeneral(General general){
        new InsertGeneralAsyncTask(generalDao).execute(general);
    }

    public void deleteGeneral(General general){
        new DeleteGeneralAsyncTask(generalDao).execute(general);
    }

    public LiveData<List<General>> getEveryGeneral (){
        return allGeneral;
    }



    //don't use AsyncTask

    private static class InsertGeneralAsyncTask extends AsyncTask<General ,Void ,Void>{
        private GeneralDao generalDao;

        private InsertGeneralAsyncTask(GeneralDao generalDao){
            this.generalDao = generalDao;
        }

        @Override
        protected Void doInBackground(General... generals) {
            generalDao.insertGeneral(generals[0]);
            return null;
        }
    }
    private static class DeleteGeneralAsyncTask extends AsyncTask<General ,Void ,Void>{
        private GeneralDao generalDao;

        private DeleteGeneralAsyncTask(GeneralDao generalDao){
            this.generalDao = generalDao;
        }

        @Override
        protected Void doInBackground(General... generals) {
            generalDao.deleteGeneral(generals[0]);
            return null;
        }
    }

}
