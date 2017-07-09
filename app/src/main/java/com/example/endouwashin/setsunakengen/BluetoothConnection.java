package com.example.endouwashin.setsunakengen;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.graphics.Bitmap;
import android.util.Log;
import java.util.Set;

/**
 * Created by MIKI on 2017/07/08.
 */

public class BluetoothConnection {

    void connectToServer(Bitmap bitmap) {
        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null) {
            // Device does not support Bluetooth
        }

        if (!mBluetoothAdapter.enable()) {
           // Context context = BluetoothConnection.getInstance().getApplicationContext();
            // AlertDialog.Builder builder2 = new AlertDialog.Builder(context);
            //builder2.setMessage("ご利用の端末はBluetoothに対応していないので、このアプリはご利用いただけません");
            //builder2.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            //    @Override
            //    public void onClick(DialogInterface dialog, int id) {
                    // OKボタンをクリックしたときの動作 何もしない

        } else {
            Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
            if (pairedDevices.size() > 0) {
                // Loop through paired devices
                for (BluetoothDevice device : pairedDevices) {
                    // Add the name and address to an array adapter to show in a ListView
                    Log.e("found device", "is" + device.getName() + device.getAddress());
//                    mArrayAdapter.add(device.getName() + "\n" + device.getAddress());
                    ConnectThread connectThread = new ConnectThread(device, bitmap);
                    connectThread.run();
                }
            }
        }
    }

    static void manageConnectedSocket(BluetoothSocket bluetoothSocket){
        ConnectedThread connectedThread2 = new ConnectedThread(bluetoothSocket, null);
        connectedThread2.start();
    }

    static void sendBitmapToDevice(BluetoothSocket socket, Bitmap sendBitmap){
        ConnectedThread connectedThread2 = new ConnectedThread(socket, sendBitmap);
        connectedThread2.start();
    }
}
