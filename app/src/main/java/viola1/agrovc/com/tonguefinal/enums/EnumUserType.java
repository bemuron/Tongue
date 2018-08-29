package viola1.agrovc.com.tonguefinal.enums;

/**
 * Created by VIOLA1 on 12-Oct-17.
 */


public enum EnumUserType {
    SUPPLIER("Supplier"),
    FARMER("Farmer"),

    COLLECTION_CENTER("Collection Center"),
    CONSUMER("Consumer"),
    DEFAULT("Admin"),
    ;

    private String value;

    EnumUserType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}

