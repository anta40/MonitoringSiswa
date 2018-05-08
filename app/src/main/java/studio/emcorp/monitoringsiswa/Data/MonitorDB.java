package studio.emcorp.monitoringsiswa.Data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

/**
 * Created by anta40 on 14-Jun-17.
 */

public class MonitorDB extends SQLiteAssetHelper {

    private static final String DATABASE_NAME = "monitor.db";
    private static final int DATABASE_VERSION = 1;

    public MonitorDB(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

}
