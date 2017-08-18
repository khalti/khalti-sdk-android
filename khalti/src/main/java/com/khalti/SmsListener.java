package com.khalti;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Telephony;
import android.telephony.SmsMessage;

import com.khalti.utils.RxBus;
import com.utila.LogUtil;

import java.util.ArrayList;
import java.util.List;

public class SmsListener extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        List<String> allowedSenders = new ArrayList<>();
        allowedSenders.add("SparrowSMS");
        allowedSenders.add("Khalti");
        allowedSenders.add("36001");

        LogUtil.log("allowed senders", allowedSenders);

        Bundle bundle = intent.getExtras();
        if (bundle == null) {
            return;
        }

        Object[] pdus = (Object[]) bundle.get("pdus");
        if (pdus != null) {
            for (Object pdu : pdus) {
                SmsMessage smsMessage;

                if (Build.VERSION.SDK_INT >= 19) {
                    SmsMessage[] msg = Telephony.Sms.Intents.getMessagesFromIntent(intent);
                    smsMessage = msg[0];
                } else {
                    smsMessage = SmsMessage.createFromPdu((byte[]) pdu);
                }

                String sender = smsMessage.getOriginatingAddress();
                String msg = smsMessage.getMessageBody();

                LogUtil.log("message", msg);

                if (allowedSenders.contains(sender)) {
                    RxBus.getInstance().post("wallet_code", msg);
                }
            }
        }
    }
}
