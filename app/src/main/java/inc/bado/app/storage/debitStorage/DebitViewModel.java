package inc.bado.app.storage.debitStorage;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import inc.bado.app.models.Debit;

public class DebitViewModel extends AndroidViewModel {

    private DebitRepository repository;
    private LiveData<List<Debit>> allDebits;

    public DebitViewModel(@NonNull Application application) {
        super(application);
        repository = new DebitRepository(application);
        allDebits = repository.getEveryDebit();
    }

    public void insert(Debit debit){
        repository.insetDebit(debit);
    }

    public void delete(Debit debit){
        repository.deleteDebit(debit);
    }

    public LiveData<List<Debit>> getAllDebits(){
        return allDebits;
    }
}
