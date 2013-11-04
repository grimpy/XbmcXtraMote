package com.github.grimpy.xbmcxtramote;

import org.alexd.jsonrpc.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;

public class XBMCClient {
    private final String cmdKey = "hack_cmd_to_pass";
    private JSONRPCClient client;

    public XBMCClient(String url) {
        super();
        client = JSONRPCClient.create(url, JSONRPCParams.Versions.VERSION_2);
        client.setSoTimeout(1000);
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
    
    public void previous() {
        inputAction("bigstepback");
    }
    
    public void next() {
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
        JSONObject params = new JSONObject();
        try {
            params.put("action", action);
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        sendCmd("Input.ExecuteAction", params);
    }

    
    private void activateWindow(String name, String option) {
        JSONObject params = new JSONObject();
        try {
            params.put("window", name);
            params.put("parameters", new JSONArray().put(option));
        } catch (JSONException e) {
            e.printStackTrace();
        }
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
        JSONObject params = new JSONObject();
        try {
            params.put("playerid", Integer.valueOf(1));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        sendCmd(cmd, params);
    }
    
    class SendCMD extends AsyncTask<JSONObject, Void, Void> {
        protected Void doInBackground(JSONObject... params) {
            String cmd = (String) params[0].remove(cmdKey);
            try {
                client.call(cmd, params[0]);
            } catch (JSONRPCException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    
    private void sendCmd(String cmd, JSONObject params) {
        if (params == null) {
            params = new JSONObject();
        }
        try {
            params.put(cmdKey, cmd);
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        new SendCMD().execute(params);
    }
    
}