package sebi.todos;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Sebi on 26.06.15.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "toto.db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_NAME = "todo";

    public static final String DATE_FIELD_NAME = "date";
    public static final String DATE_FIELD_TYPE = "INTEGER";
    public static final String TITLE_FIELD_NAME = "title";
    public static final String TITLE_FIELD_TYPE = "TEXT";
    public static final String DESCR_FIELD_NAME = "description";
    public static final String DESCR_FIELD_TYPE = "TEXT";

    private static final String TABLE_CREATE = "CREATE TABLE " + TABLE_NAME + "("
            + DATE_FIELD_NAME + " " + DATE_FIELD_TYPE + ", "
            + TITLE_FIELD_NAME + " " + TITLE_FIELD_TYPE + ", "
            + DESCR_FIELD_NAME + " " + DESCR_FIELD_TYPE + ")";

    public DatabaseHelper(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            db.execSQL(TABLE_CREATE);
        }catch (SQLException ex)
        {
            Log.e("DatabaseHelper", "error creating tables", ex);
        }

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }



}
