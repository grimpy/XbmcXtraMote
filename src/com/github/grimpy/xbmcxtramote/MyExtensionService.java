package com.github.grimpy.xbmcxtramote;

import android.os.Handler;
import android.util.Log;

import com.sonyericsson.extras.liveware.extension.util.ExtensionService;
import com.sonyericsson.extras.liveware.extension.util.control.ControlExtension;
import com.sonyericsson.extras.liveware.extension.util.registration.RegistrationInformation;

/**
 * The Sample Extension Service handles registration and keeps track of all
 * controls on all accessories.
 */
public class MyExtensionService extends ExtensionService {

    public static final String EXTENSION_KEY = "com.github.grimpy.xbmcxtramote.key";

    public static final String LOG_TAG = "XBCMXtraMote";

    public MyExtensionService() {
        super(EXTENSION_KEY);
    }

    /**
     * {@inheritDoc}
     *
     * @see android.app.Service#onCreate()
     */
    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(MyExtensionService.LOG_TAG, "MyControlService: onCreate");
    }

    @Override
    protected RegistrationInformation getRegistrationInformation() {
        return new MyRegistrationInformation(this);
    }

    /*
     * (non-Javadoc)
     * @see com.sonyericsson.extras.liveware.aef.util.ExtensionService#
     * keepRunningWhenConnected()
     */
    @Override
    protected boolean keepRunningWhenConnected() {
        return false;
    }

    @Override
    public ControlExtension createControlExtension(String hostAppPackageName) {
        // First we check if the API level and screen size required for
        // SampleControlSmartWatch2 is supported
        Log.d(MyExtensionService.LOG_TAG, "create control exteions " + hostAppPackageName);
        return new ControlSmartWatch2(hostAppPackageName, this, new Handler());
     }
}
