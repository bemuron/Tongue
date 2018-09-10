package viola1.agrovc.com.tonguefinal.models;

public class TongueLanguage {
    private int language_id;
    private String language_name;
    private String language_code;

    public TongueLanguage(int language_id, String language_name, String language_code) {
        this.language_id = language_id;
        this.language_name = language_name;
        this.language_code = language_code;
    }

    public TongueLanguage(String language_name, String language_code) {
        this.language_name = language_name;
        this.language_code = language_code;
    }

    public int getLanguage_id() {
        return language_id;
    }

    public void setLanguage_id(int language_id) {
        this.language_id = language_id;
    }

    public String getLanguage_name() {
        return language_name;
    }

    public void setLanguage_name(String language_name) {
        this.language_name = language_name;
    }

    public String getLanguage_code() {
        return language_code;
    }

    public void setLanguage_code(String language_code) {
        this.language_code = language_code;
    }
}
