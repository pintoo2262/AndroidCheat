package com.smn.myapplicationmap;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

class ReadTask extends AsyncTask<String, Void, String> {

    private Context context;
    private MainActivity.mapClicked mapClicked;

    public ReadTask(Context context, MainActivity.mapClicked mapClicked) {

        this.context = context;
        this.mapClicked = mapClicked;
    }


    @Override
    protected String doInBackground(String... url) {
        // TODO Auto-generated method stub
        String data = "";
        try {
            MapHttpConnection http = new MapHttpConnection();
            data = http.readUr(url[0]);


        } catch (Exception e) {
            // TODO: handle exception
            Log.d("Background Task", e.toString());
        }
        return data;
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        new ParserTask(context, mapClicked).execute(result);
    }

}