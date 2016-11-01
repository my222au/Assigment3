package project.my222au.assignment3.callCollector;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.sql.SQLException;

import project.my222au.assignment3.R;

public class CallCollector extends AppCompatActivity {
    private static final String TAG = CallCollector.class.getSimpleName() ;
    private ListView mListView;
    private TelephonyManager mTelephonyManager;
    private IncommingCall incommingCall;
    private CallCollectorDataSource mCallCollectorDS;
    private CallCollecterAdapter mCallCollecterAdapter;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call_collector);

        mListView = (ListView) findViewById(R.id.listView);
        mTelephonyManager= (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
        mCallCollectorDS = new CallCollectorDataSource(this);
        try {
            mCallCollectorDS.open();
        } catch (SQLException e) {
          Log.e(TAG,"Cloudent open the database" ,e);
        }

        mCallCollecterAdapter = new CallCollecterAdapter(this);
       mCallCollecterAdapter.addAll(mCallCollectorDS.getCalls());  // Add all the calls stored in the  database to listview

        mListView.setAdapter(mCallCollecterAdapter);


        registerForContextMenu(mListView);

    }


    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {

        menu.setHeaderTitle(R.string.options);
        menu.add(0, R.id.Call, 0, R.string.Call);
        menu.add(0, R.id.Sms, 0, R.string.send_sms);
    }



    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo adpapterinfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        incommingCall =  mCallCollecterAdapter.getItem(adpapterinfo.position);
        String number = "tel:"+incommingCall.getNumber().toString();
        String smsNumber= "smsto"+incommingCall.getNumber().toString();
        switch (item.getItemId()) {
            case R.id.Call:
                makecall(number);
                return true;
            case R.id.Sms:
               sendSms(smsNumber);
                return true;


            default:
                return super.onContextItemSelected(item);
        }
    }

    private void sendSms(String number) {
        Intent sms = new Intent(Intent.ACTION_SEND,Uri.parse(number));
       sms.setType("text/plain");
        sms.putExtra(Intent.EXTRA_SUBJECT, R.string.subject);
        sms.putExtra(Intent.EXTRA_TEXT, number);
        startActivity(Intent.createChooser(sms, "Share"));




    }

    private void makecall(String number) {
        startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse(number)));

    }

    @Override
    protected void onDestroy() {
        mCallCollectorDS.Close();
        super.onDestroy();
    }

    class CallCollecterAdapter extends ArrayAdapter<IncommingCall> {
        private TextView mCallnr;

        public CallCollecterAdapter(Context context) {
            super(context, R.layout.call_list_item);

        }
        public View getView(int position, View convertView, ViewGroup parent) {

            View row;

            if (convertView == null) {    // Create new row view object
                LayoutInflater inflater = getLayoutInflater();
                row = inflater.inflate(R.layout.call_list_item, parent, false);
            } else
                row = convertView;



           mCallnr= (TextView) row.findViewById(R.id.numberLabel);
            Log.i("TAG ADAPTER", (getItem(position).getNumber()));
            mCallnr.setText(getItem(position).getNumber());



            return row;
        }

    }

}

