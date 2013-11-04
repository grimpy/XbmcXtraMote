package com.github.grimpy.xbmcxtramote;

import com.github.grimpy.xbmcxtramote.R;

import android.app.Dialog;
import android.os.Bundle;
import android.preference.PreferenceActivity;


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
