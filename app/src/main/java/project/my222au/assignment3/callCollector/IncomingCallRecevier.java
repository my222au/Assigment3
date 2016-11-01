package project.my222au.assignment3.callCollector;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;

;


public class IncomingCallRecevier extends BroadcastReceiver {
    private static final String TAG = IncomingCallRecevier.class.getSimpleName() ;
    Context mContext;
    private TelephonyManager mTelephonyManager;
    CallPhoneStateListener mCallPhoneStateListener;

    @Override
    public void onReceive(Context context, Intent intent) {
        mContext=context;
        mTelephonyManager= (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);

        if(mCallPhoneStateListener == null)
       mCallPhoneStateListener = new CallPhoneStateListener(context);
        mTelephonyManager.listen(mCallPhoneStateListener,PhoneStateListener.LISTEN_CALL_STATE);


        }






    }



