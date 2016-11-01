package project.my222au.assignment3.callCollector;

import android.content.Context;
import android.telephony.PhoneStateListener;
import android.util.Log;

import java.sql.SQLException;

/****
 * this Class Listens to the state  of the phone, if the phone rings it saves the number to the databaase
 */

   public class CallPhoneStateListener extends PhoneStateListener {
       private static final int RINGING = 1;
       private static final String TAG = CallPhoneStateListener.class.getSimpleName() ;
       Context mContext;
    IncommingCall call = new IncommingCall();
       public CallPhoneStateListener(Context context) {
           mContext = context;
       }
       public void onCallStateChanged(int state, String incomingNumber) {
           if (state == RINGING) {

                try {

                    call.setNumber(incomingNumber);
                    CallCollectorDataSource callDataSource = new CallCollectorDataSource(mContext);


                    callDataSource.open();
                    callDataSource.createCallinfromation(call);
                    callDataSource.Close();
                } catch (SQLException e) {
                    Log.e(TAG, "Couldent open database", e);
                }
            }

        }
    }

