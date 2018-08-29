package viola1.agrovc.com.tonguefinal.enums;

/**
 * Created by VIOLA1 on 12-Oct-17.
 */


public enum EnumLocationType {
    MANDATORY("mandatory"),
    OPTIONAL("optional");
    String value;

    EnumLocationType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
