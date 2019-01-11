package viola1.agrovc.com.tonguefinal.data.database;

import android.app.SearchManager;
import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.provider.BaseColumns;

import java.util.HashMap;

@Entity(tableName = "languages", indices = {@Index(value = {"language_code"}, unique = true)})
public class Language {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "language_id")
    private int language_id;

    @ColumnInfo(name = "name")
    private String name;

    @ColumnInfo(name = "language_code")
    private String language_code;

    private int color = -1;

    @Ignore
    public Language(String name, String language_code){
        this.name = name;
        this.language_code = language_code;
    }

    public Language(int language_id, String name, String language_code){
        this.language_id = language_id;
        this.name = name;
        this.language_code = language_code;
    }

    public String getName() {
        return name;
    }

    public String getLanguage_code() {
        return language_code;
    }

    public int getLanguage_id() {
        return language_id;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

}
