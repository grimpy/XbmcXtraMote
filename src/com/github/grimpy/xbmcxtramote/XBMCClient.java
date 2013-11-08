package com.github.grimpy.xbmcxtramote;

import java.net.HttpCookie;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;

import com.thetransactioncompany.jsonrpc2.JSONRPC2Request;
import com.thetransactioncompany.jsonrpc2.client.ConnectionConfigurator;
import com.thetransactioncompany.jsonrpc2.client.JSONRPC2Session;
import com.thetransactioncompany.jsonrpc2.client.JSONRPC2SessionException;

import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Base64;

class HttpConfigurator implements ConnectionConfigurator {
    private String username;
    private String password;

    @Override
    public void configure(HttpURLConnection con) {
        String head = String.format("%s:%s", username, password);
        head = Base64.encodeToString(head.getBytes(), Base64.DEFAULT);
        con.setRequestProperty("Authorization", String.format("Basic %s", head));
    }

    public HttpConfigurator(String username, String password) {
        super();
        this.username = username;
        this.password = password;
    }
    
}


public class XBMCClient {
    private JSONRPC2Session mySession;

    public XBMCClient(String url, String username, String password) {
        super();
        URL urlobject;
        try {
            urlobject = new URL(url);
        } catch (MalformedURLException e) {
            return;
        }
        mySession = new JSONRPC2Session(urlobject);
        if (!TextUtils.isEmpty(password)) {
            HttpConfigurator conf = new HttpConfigurator(username, password);
            mySession.setConnectionConfigurator(conf);
        }
        mySession.getOptions().setConnectTimeout(1000);
    }

    public void sendLeft() {
        sendCmd("Input.Left", null);
    }
    
    public void sendRight() {
        sendCmd("Input.Right", null);
    }
    
    public void sendUp() {
        sendCmd("Input.Up", null);
    }
    
    public void sendDown() {
        sendCmd("Input.Down", null);
    }
    
    public void sendBack() {
        sendCmd("Input.Back", null);
    }
    
    public void askInfo() {
        sendCmd("Input.Info", null);
    }
    
    public void sendSelect() {
        sendCmd("Input.Select", null);
    }
    
    public void forward() {
        inputAction("fastforward");
    }
    
    public void rewind() {
        inputAction("rewind");
    }
    
    public void previousChapter() {
        inputAction("bigstepback");
    }
    
    public void next() {
        inputAction("stepforward");
    }
    
    public void previous() {
        inputAction("stepback");
    }
    
    public void nextChapter() {
        inputAction("bigstepforward");
    }
    
    public void showContextMenu() {
        sendCmd("Input.ContextMenu", null);
    }

    public void showTvShows() {
        activateWindow("videos", "TvShowTitles");
    }
    
    public void showMovies() {
        activateWindow("videos", "MovieTitles");
    }
    
    public void showFavourites() {
        activateWindow("favourites", "1");
    }
    
    public void showHome() {
        activateWindow("home", "1");
    }
    
    
    private void inputAction(String action) {
        Map<String,Object> params = new HashMap<String,Object>();
        params.put("action", action);
        sendCmd("Input.ExecuteAction", params);
    }

    
    private void activateWindow(String name, String option) {
        Map<String,Object> params = new HashMap<String,Object>();
        params.put("window", name);
        params.put("parameters", new JSONArray().put(option));
        sendCmd("GUI.ActivateWindow", params);
    }

    public void playPause() {
        sendToPlayer("Player.PlayPause");
    }
    
    public void stop() {
        sendToPlayer("Player.Stop");
    }
    
    public void decreaseVolume() {
        inputAction("volumedown");
    }
    
    public void increaseVolume() {
        inputAction("volumeup");
    }
    
    public void mute() {
        inputAction("mute");
    }


    
    private void sendToPlayer(String cmd) {
        Map<String,Object> params = new HashMap<String,Object>();
        params.put("playerid", Integer.valueOf(1));
        sendCmd(cmd, params);
    }
    
    class SendCMD extends AsyncTask<JSONRPC2Request, Void, Void> {
        protected Void doInBackground(JSONRPC2Request... reqs) {
            try {
                mySession.send(reqs[0]);
            } catch (JSONRPC2SessionException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return null;
        }
    }

    
    private void sendCmd(String cmd, Map<String,Object> params) {
        JSONRPC2Request req = new JSONRPC2Request(cmd, params, 1);
        new SendCMD().execute(req);
    }
    
}