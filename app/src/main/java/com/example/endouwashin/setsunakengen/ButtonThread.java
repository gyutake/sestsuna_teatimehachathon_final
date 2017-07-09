package com.example.endouwashin.setsunakengen;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import java.util.Date;

/**
 * Created by MIKI on 2017/07/09.
 */

class ButtonThread extends AsyncTask<Long, Void, Date> {

    @Override
    protected Date doInBackground(Long... params) {
        // TODO Auto-generated method stub
        Log.d("onClick", "in Thread thread id = " + Thread.currentThread().getId());
        try {
            Thread.sleep(params[0] * 1000); // n秒待つ
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return new Date();
    }

    @Override
    protected void onPostExecute(Date result) {
        // TODO Auto-generated method stub
        super.onPostExecute(result);
        Log.d("onClick", "date = " + result);
        Log.d("onClick", "runOnUiThread thread id = " + Thread.currentThread().getId());
//        Toast.makeText(ButtonThread.this, "Click button1", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onPreExecute() {
        // TODO Auto-generated method stub
        super.onPreExecute();
        Log.d("onClick", "before Thread thread id = " + Thread.currentThread().getId());
    }
}