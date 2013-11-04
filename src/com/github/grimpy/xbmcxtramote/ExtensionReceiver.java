package com.github.grimpy.xbmcxtramote;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * The extension receiver receives the extension intents and starts the
 * extension service when it arrives.
 */
public class ExtensionReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(final Context context, final Intent intent) {

        Log.d(MyExtensionService.LOG_TAG, "onReceive: " + intent.getAction());
        intent.setClass(context, MyExtensionService.class);
        context.startService(intent);
    }
}
