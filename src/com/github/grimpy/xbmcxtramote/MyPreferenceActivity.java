package com.github.grimpy.xbmcxtramote;

import com.github.grimpy.xbmcxtramote.R;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;
import android.util.Log;


/**
 * The sample control preference activity handles the preferences for the sample
 * control extension.
 */
public class MyPreferenceActivity extends PreferenceActivity {

    @SuppressWarnings("deprecation")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.preference);

    }

    @Override
    protected Dialog onCreateDialog(int id) {
        Dialog dialog = null;
        return dialog;
    }


}
