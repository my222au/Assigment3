package project.my222au.assignment3.callCollector;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

// since i didnt know anything about sql lite database i followed
// this website http://www.vogella.com/tutorials/AndroidSQLite/article.html

public class CallCollectorDataSource {
    private SQLiteDatabase mSQLiteDatabase;
    private CallCollectorDatabaseHelper mCalldbHelper;
    private String[] allColumns= {
            CallCollectorDatabaseHelper.COL_ID,
            CallCollectorDatabaseHelper.COL_NUMBER
    };

    public CallCollectorDataSource(Context context) {
        mCalldbHelper = new CallCollectorDatabaseHelper(context);
    }


    public void open() throws SQLException{
        mSQLiteDatabase = mCalldbHelper.getWritableDatabase();

    }

    public void Close(){
      mCalldbHelper.close();

    }
    public IncommingCall createCallinfromation(IncommingCall call ){
        ContentValues cValues = new ContentValues();
        cValues.put(CallCollectorDatabaseHelper.COL_NUMBER,call.getNumber());
        Long insertId= mSQLiteDatabase.insert(CallCollectorDatabaseHelper.TABLE_NAME,null, cValues);

        Cursor cursor = mSQLiteDatabase.query(CallCollectorDatabaseHelper.TABLE_NAME, allColumns,
                CallCollectorDatabaseHelper.COL_ID+ " = "+insertId,null,null,null,null);
         cursor.moveToFirst();
        IncommingCall newIncommingCall= cursorToCall(cursor);
        cursor.close();

        return newIncommingCall;


    }

    private IncommingCall cursorToCall(Cursor cursor) {
        IncommingCall incommingCall = new IncommingCall();
        incommingCall.setID(cursor.getLong(0));
        incommingCall.setNumber(cursor.getString(1));
        return incommingCall;
    }


     public List<IncommingCall> getCalls(){
         List<IncommingCall> calls = new ArrayList<IncommingCall>();
         Cursor cursor= mSQLiteDatabase.query(CallCollectorDatabaseHelper.TABLE_NAME,allColumns,null,
         null,null,null,null);
         cursor.moveToFirst();
         while (!cursor.isAfterLast()){
           IncommingCall incommingCall=  cursorToCall(cursor);
             calls.add(incommingCall);
             cursor.moveToNext();
         }
         cursor.close();
         return calls;


     }






}
