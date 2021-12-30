package sp.com.p2020358assignment;

public class Constants {
    // db name
    public static final String DB_NAME = "FISH_INFO_DB";
    // db version
    public static final int DB_VERSION = 1;
    // db table
    public static final String TABLE_NAME = "FISH_INFO_TABLE";
    // table columns
    public static final String C_ID = "ID";
    public static final String C_NAME = "NAME";
    public static final String C_DATE = "DATE";
    public static final String C_LENGTH = "LENGTH";
    public static final String C_WEIGHT = "WEIGHT";
    public static final String C_LAT = "LAT";
    public static final String C_LON = "LON";
    public static final String C_IMAGE = "IMAGE";
    public static final String C_ADD_TIMESTAMP = "ADD_TIMESTAMP";
    public static final String C_UPDATE_TIMESTAMP = "UPDATE_TIMESTAMP";
    // create query for table
    public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " ("
            +C_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            +C_NAME + " TEXT,"
            +C_DATE + " TEXT,"
            +C_LENGTH + " TEXT,"
            +C_WEIGHT + " TEXT,"
            +C_LAT + " REAL,"
            +C_LON + " REAL,"
            +C_IMAGE + " BLOB,"
            +C_ADD_TIMESTAMP + " TEXT,"
            +C_UPDATE_TIMESTAMP + " TEXT"
            + ");";

}
