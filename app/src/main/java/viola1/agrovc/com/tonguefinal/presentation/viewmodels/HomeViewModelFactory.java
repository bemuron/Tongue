package viola1.agrovc.com.tonguefinal.presentation.viewmodels;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;

import viola1.agrovc.com.tonguefinal.data.TongueRepository;

/**
 * Factory method that allows us to create a ViewModel with a constructor that takes a
 * {@link TongueRepository}
 */
public class HomeViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private final TongueRepository mRepository;

    public HomeViewModelFactory(TongueRepository repository) {
        this.mRepository = repository;
    }

    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        //noinspection unchecked
        return (T) new HomeActivityViewModel(mRepository);
    }
}
