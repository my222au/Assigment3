package project.my222au.assignment3.callCollector;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class CallCollectorDatabaseHelper  extends SQLiteOpenHelper{
    public static final String DATABASE_NAME = "CallCollector.db";
    public static final String TABLE_NAME = "callstabel";
    public static  final String COL_ID = "_ID";
    public static  final String COL_NUMBER= "NUMBER";

    public CallCollectorDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
        SQLiteDatabase database = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

 sqLiteDatabase.execSQL("create table "+ TABLE_NAME +" (" + COL_ID +" INTEGER PRIMARY KEY AUTOINCREMENT, "+ COL_NUMBER +" TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABEL IF EXISTS"+ TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
