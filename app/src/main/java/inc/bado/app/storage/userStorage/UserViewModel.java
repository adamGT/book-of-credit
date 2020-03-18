package inc.bado.app.storage.userStorage;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import inc.bado.app.models.User;

public class UserViewModel extends AndroidViewModel {

    private UserRepository repository;
    private LiveData<List<User>> allUsers;

    public UserViewModel(@NonNull Application application) {
        super(application);
        repository = new UserRepository(application);
        allUsers = repository.getEveryUser();
    }

    public void insert(User user){
        repository.insetUser(user);
    }

    public void delete(User user){
        repository.deleteUser(user);
    }

    public LiveData<List<User>> getAllUsers(){
        return allUsers;
    }
}
