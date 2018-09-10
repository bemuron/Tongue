package viola1.agrovc.com.tonguefinal.data.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface LanguagesDao {

    /*
    insert languages into db
     */
    @Insert
    void insertLanguage(Language[] language);

    @Query("Delete from languages")
    void deleteAll();

    @Query("SELECT * from languages ORDER BY language_id ASC")
    LiveData<List<Language>> getAllLanguages();
}
