package inc.bado.app.storage.userStorage;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

import inc.bado.app.models.User;
import inc.bado.app.storage.AppDatabase;

public class UserRepository {

    private UserDao userDao;
    private LiveData<List<User>> allUser;

    //    @Inject
    public UserRepository (Application application){
        AppDatabase database = AppDatabase.getInstance(application);
        userDao = database.userDao();
        allUser = userDao.getAllUser();
    }

    public void insetUser(User user){
        new InsertUserAsyncTask(userDao).execute(user);
    }

    public void deleteUser(User user){
        new DeleteUserAsyncTask(userDao).execute(user);
    }

    public LiveData<List<User>> getEveryUser (){
        return allUser;
    }



    //don't use AsyncTask

    private static class InsertUserAsyncTask extends AsyncTask<User ,Void ,Void> {
        private UserDao userDao;

        private InsertUserAsyncTask(UserDao userDao){
            this.userDao = userDao;
        }

        @Override
        protected Void doInBackground(User... users) {
            userDao.insertUser(users[0]);
            return null;
        }
    }
    private static class DeleteUserAsyncTask extends AsyncTask<User ,Void ,Void>{
        private UserDao userDao;

        private DeleteUserAsyncTask(UserDao creditDao){
            this.userDao = creditDao;
        }

        @Override
        protected Void doInBackground(User... users) {
            userDao.deleteUser(users[0]);
            return null;
        }
    }
}
