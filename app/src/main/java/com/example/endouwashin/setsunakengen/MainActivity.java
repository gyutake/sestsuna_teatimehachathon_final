package com.example.endouwashin.setsunakengen;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ClipDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.CountDownTimer;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.RuntimePermissions;

import static com.example.endouwashin.setsunakengen.R.drawable.hanabi1;
import static com.example.endouwashin.setsunakengen.R.id.list_item;
import static com.example.endouwashin.setsunakengen.R.id.myImageView;

@RuntimePermissions
public class MainActivity extends AppCompatActivity {
    int RESULT_PICK_FILENAME = 1;
    Adapter adapter = null;
    ProgressBar progressBar = null;
    boolean check=true;
    boolean closecheck=false;
    @Override
    protected void onStart()
    {
        super.onStart();
        if(check==true){
        final Dialog dialog=new Dialog(this);
        dialog.setContentView(R.layout.dialog);
        dialog.show();
        new CountDownTimer(7000,1000){
            @Override
            public void onTick(long millisUntilFinished){

            }
            @Override
            public void onFinish(){
                dialog.dismiss();
            }
        }.start();
        closecheck=true;
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        getActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#1c2833")));
//        ProgressBar progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        //  progressBar.setVisibility(View.VISIBLE);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        FloatingActionButton actionButton = (FloatingActionButton) findViewById(R.id.floatingActionButton);
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        actionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickFilenameFromGallery();
            }
        });

        recyclerView.setHasFixedSize(true);

        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(1, 1);
        recyclerView.setLayoutManager(staggeredGridLayoutManager);

        List<Drawable> data = new ArrayList<>();


        Drawable drawable1 = getResources().getDrawable(R.drawable.hanabi1);
        Drawable drawable2 = getResources().getDrawable(R.drawable.hanabi2);
        Drawable drawable3 = getResources().getDrawable(R.drawable.hanabi3);
        Drawable drawable4 = getResources().getDrawable(R.drawable.hanabi4);
        Drawable drawable5 = getResources().getDrawable(R.drawable.hanabi5);
        data.add(drawable1);
        data.add(drawable2);
        data.add(drawable3);
        data.add(drawable4);
        data.add(drawable5);

        adapter = new Adapter(data);
        recyclerView.setAdapter(adapter);
        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null) {
            // Device does not support Bluetooth
        }
        if (!mBluetoothAdapter.isEnabled()) {
            int REQUEST_ENABLE_BT = 2;
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);

        }


            // Register for broadcasts when a device is discovered.
            IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
            registerReceiver(mReceiver, filter);

            Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
            int DISCOVER_DURATION = 3600;
            discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, DISCOVER_DURATION);
            startActivity(discoverableIntent);

    }

    @NeedsPermission(Manifest.permission.BLUETOOTH)
    void showBluetooth() {

    }

    @OnPermissionDenied(Manifest.permission.BLUETOOTH)
    void showDeniedForBluetooth() {

    }

    @OnNeverAskAgain(Manifest.permission.BLUETOOTH)
    void showNeverAskForBluetooth() {

    }

    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                // Discovery has found a device. Get the BluetoothDevice
                // object and its info from the Intent.
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                String deviceName = device.getName();
                String deviceHardwareAddress = device.getAddress(); // MAC address
            }
        }
    };

    private void pickFilenameFromGallery() {
        Intent i = new Intent(
                Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, RESULT_PICK_FILENAME);
    }

    @Override
    protected void onActivityResult(
            int requestCode,
            int resultCode,
            final Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_PICK_FILENAME
                && resultCode == RESULT_OK
                && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};

            Cursor cursor = getContentResolver().query(
                    selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex
                    = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            Log.e("bitmap", "" + picturePath);
            cursor.close();

//            ImageView imageView = (ImageView) findViewById(R.id.sendingImage);
            //画像パスから表示
            BitmapFactory.Options imageOptions = new BitmapFactory.Options();
            check=false;
            imageOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;
//            imageOptions.inJustDecodeBounds = true;
            imageOptions.inSampleSize = 3;
            final Bitmap bmImg = BitmapFactory.decodeFile(picturePath, imageOptions);

            Drawable drawable = new BitmapDrawable(getResources().getSystem(), bmImg);
            adapter.addItem(drawable);

//            imageView.setImageBitmap(bmImg);

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("選択した画像を送信しますか？");
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int id) {
                    BluetoothConnection bluetoothConnection = new BluetoothConnection();
                    bluetoothConnection.connectToServer(bmImg);
                    progressBar.setVisibility(View.VISIBLE);
                    new ButtonWait().execute(new Long(3));

                }
            });

            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int id) {
                    // CANCELボタンをクリックしたときの動
//                    ImageView v = (ImageView)findViewById(R.id.sendingImage);
//                    v.setImageBitmap(null);
                    adapter.removeItem();
                }
            });
            builder.show();


        }

    }

    class ButtonWait extends AsyncTask<Long, Void, Date> {

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
            Toast.makeText(MainActivity.this, "送信されました", Toast.LENGTH_SHORT).show();
            progressBar.setVisibility(View.INVISIBLE);
        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            Log.d("onClick", "before Thread thread id = " + Thread.currentThread().getId());
        }
    }

}
