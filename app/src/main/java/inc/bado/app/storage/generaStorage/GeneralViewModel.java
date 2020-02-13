package inc.bado.app.storage.generaStorage;

import android.app.Application;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import inc.bado.app.models.Debit;
import inc.bado.app.models.General;

public class GeneralViewModel extends AndroidViewModel {

    private GeneralRepository repository;
    private LiveData<List<General>> allGeneral;

    public GeneralViewModel(@NonNull Application application) {
        super(application);
        repository = new GeneralRepository(application);
        allGeneral = repository.getEveryGeneral();
    }

    public void insert(General general){
        repository.insetGeneral(general);
    }

    public void delete(General general){
        repository.deleteGeneral(general);
    }

    public LiveData<List<General>> getAllDebits(){
        return allGeneral;
    }
}