package inc.bado.app.storage.creditStorage;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import inc.bado.app.models.Credit;

public class CreditViewModel extends AndroidViewModel {

    private CreditRepository repository;
    private LiveData<List<Credit>> allCredits;

    public CreditViewModel(@NonNull Application application) {
        super(application);
        repository = new CreditRepository(application);
        allCredits = repository.getEveryCredit();
    }

    public void insert(Credit credit){
        repository.insetCredit(credit);
    }

    public void delete(Credit credit){
        repository.deleteCredit(credit);
    }

    public LiveData<List<Credit>> getAllCredits(){
        return allCredits;
    }
}