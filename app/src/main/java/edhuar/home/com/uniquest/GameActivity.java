package edhuar.home.com.uniquest;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.media.MediaPlayer;
import android.os.RemoteException;
import android.os.Vibrator;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.List;

import jaalee.sdk.*;
import jaalee.sdk.utils.L;

public class GameActivity extends AppCompatActivity {

    private BeaconManager beaconManager;
    private LeDeviceListAdapter adapter;
    private static final Region ALL_BEACONS_REGION = new Region("rid", null, null, null);
    public static final int REQUEST_ENABLE_BLUETOOTH = 1234;



    LinearLayout p0, p1, p2, p3;
    Button notButton, stopButton, setButton;

    NotificationManager notificationManager;
    boolean isNotificActive = false;
    int notifID = 33;


    ///GIO
    Sonidos sonido;
    private int TiempoCiclo=500;
    long mtime;
    private boolean activo;
    int muactual;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        sonido=new Sonidos(this);
        sonido.Play(R.raw.deathnotel);

        activo =true;
        p0 = (LinearLayout) findViewById(R.id.piece0);
        p1 = (LinearLayout) findViewById(R.id.piece1);
        p2 = (LinearLayout) findViewById(R.id.piece2);
        p3 = (LinearLayout) findViewById(R.id.piece3);

        notButton = (Button) findViewById(R.id.not_button);
        stopButton = (Button) findViewById(R.id.stop_button);
        setButton = (Button) findViewById(R.id.alerta_button);

        //Notify("hi", "there");
        adapter = new LeDeviceListAdapter(this);
        /*Iniciar sonidos DBZ*/

        //GIO
        mtime= System.currentTimeMillis();
        muactual=R.raw.dbz;


        ListView list = (ListView) findViewById(R.id.lista_beacons);
        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });

        L.enableDebugLogging(false);//que carajos sera esto xD
        //bien csm lo descubri luego de las elecciones, es habilitar los mensajes de log de la libreria
        Log.d("Jaalee", "BEACON MANAGER");

        beaconManager = new BeaconManager(this);
        beaconManager.setRangingListener(new RangingListener() {
            @Override
            public void onBeaconsDiscovered(Region paramRegion, final List<Beacon> paramList) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        List<Beacon> JaaleeBeacons = filterBeacons(paramList);
                        adapter.replaceWith(JaaleeBeacons);
                    }
                });
            }
        });

        Log.d("Jaalee", "SET DEVICE Discover");

        beaconManager.setDeviceDiscoverListener(new DeviceDiscoverListener() {
            @Override
            public void onBLEDeviceDiscovered(BLEDevice device) {
                //Log.i("JAALEE", "On bluetooth low energy discovery:" + device.getMacAddress());
                setMtime();

                Log.d("UNIQUEST2", "El dispositivo "+device.getRssi()+" fue descubierto "+System.currentTimeMillis());
                if(device.getMacAddress().equalsIgnoreCase("F6:91:19:70:6A:4E") && (device.getRssi()>-70) ){
                    p0.setVisibility(View.INVISIBLE);
                    //((Vibrator) getSystemService(VIBRATOR_SERVICE)).vibrate(500);
                }
                if(device.getMacAddress().equalsIgnoreCase("F9:47:58:EB:AC:A0") && (device.getRssi()>-70)){
                    p1.setVisibility(View.INVISIBLE);
                    ((Vibrator) getSystemService(VIBRATOR_SERVICE)).vibrate(500);
                }
                if(device.getMacAddress().equalsIgnoreCase("CC:1E:66:4C:E6:93") && (device.getRssi()>-70)){
                    p2.setVisibility(View.INVISIBLE);
                    p3.setVisibility(View.INVISIBLE);
                    ((Vibrator) getSystemService(VIBRATOR_SERVICE)).vibrate(500);
                }
            }
        });
    }



    private synchronized void setMtime(){
        mtime = System.currentTimeMillis();
    }
    private synchronized long getMtime(){
        return mtime;
    }


    private void HiloAlternativo() {

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (activo) {
                    long time = System.currentTimeMillis()-getMtime();
                    if(time>3000){
                        SuenaMuActual(muactual);
                        Log.d("UNIQUEST2","Restar_Naruto" + time);
                    }
                    else {
                        Log.d("UNIQUEST2","Suena Radar_" + time);
                        sonido.Play(R.raw.radar);
                    }
                    esperoTiempo(TiempoCiclo);
                }
            }
        }).start();
    }

    public void SuenaMuActual(int val){
        muactual =val;
        sonido.Play(muactual);
    }

    public void esperoTiempo(int Time){
        try {
            Thread.sleep(Time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private List<Beacon> filterBeacons(List<Beacon> beacons){
        List<Beacon> filteredBeacons = new ArrayList<>(beacons.size());
        for(Beacon beacon: beacons){
            filteredBeacons.add(beacon);

            Log.d("infoJAALEEBLE",beacon.toString());
        }
        return filteredBeacons;
    }

    public void showClue(View view){
        /*Toast.makeText(getApplicationContext(), "Pistas",Toast.LENGTH_LONG).show();
        Dialog dialog = new Dialog(GameActivity.this);
        dialog.setContentView(R.layout.clue_dialog);
        dialog.setTitle("Pista");

        dialog.show();*/
        AlertDialog.Builder builder = new AlertDialog.Builder(GameActivity.this);
        builder.setTitle("Pistas").setMessage("La siguiente imagen se encuentra en una tienda que es sponsor de grandes equipos de futbol europeo");
        builder.setPositiveButton("Entendido", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();

    }

    public void showNotification(View view){
        NotificationCompat.Builder notificBuilder = new NotificationCompat.Builder(GameActivity.this)
                .setContentTitle("Message")
                .setContentText("New message")
                .setTicker("Alert new message!")
                .setSmallIcon(R.drawable.treasure_chest_help);
        Intent intent = new Intent(GameActivity.this,Main2Activity.class);

        TaskStackBuilder taskStackBuilder = TaskStackBuilder.create(GameActivity.this);
        taskStackBuilder.addParentStack(Main2Activity.class);
        taskStackBuilder.addNextIntent(intent);

        PendingIntent pendingIntent = taskStackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        notificBuilder.setContentIntent(pendingIntent);
        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(notifID, notificBuilder.build());

        isNotificActive = true;
    }

    public void stopNotification(View view){
        if(isNotificActive){
            notificationManager.cancel(notifID);
        }
    }

    public void setAlarm(View view){
        Long alertTime = new GregorianCalendar().getTimeInMillis()+10*1000;

        Intent alertIntent = new Intent(GameActivity.this, AlertReceiver.class);
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        alarmManager.set(AlarmManager.RTC_WAKEUP, alertTime, PendingIntent.getBroadcast(GameActivity.this, 1, alertIntent, PendingIntent.FLAG_UPDATE_CURRENT));

    }

    @Override
    protected void onResume(){
        super.onResume();
        //sonido.Play(R.raw.godofwar);
        SuenaMuActual(R.raw.darthmaultheme);
        activo=true;
        HiloAlternativo();
    }

    @Override
    protected void onStart() {
        super.onStart();
        //start sonidos

        // Check if device supports Bluetooth Low Energy.
        if (!beaconManager.hasBluetooth()) {
            Toast.makeText(this, "Device does not have Bluetooth Low Energy", Toast.LENGTH_LONG).show();
            return;
        }

        // If Bluetooth is not enabled, let user enable it.
        if (!beaconManager.isBluetoothEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BLUETOOTH);
        } else {
            connectToService();
        }
    }

    private void connectToService(){
        adapter.replaceWith(Collections.<Beacon>emptyList());
        beaconManager.connect(new ServiceReadyCallback() {
            @Override
            public void onServiceReady() {
                try {
                    beaconManager.startRangingAndDiscoverDevice(ALL_BEACONS_REGION);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        beaconManager.disconnect();
        //sonido destroy
        super.onDestroy();
        activo=false;
        sonido.liberar(0);
    }

    @Override
    protected void onStop() {
<<<<<<< HEAD
        //mediaPlayer.stop();
=======
        mediaPlayer.pause();
>>>>>>> 24514812d72eaf0d318420632f2870ffa5c35ae4
        try {
            beaconManager.stopRanging(ALL_BEACONS_REGION);
        } catch (RemoteException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        super.onStop();
        activo =false;
        sonido.liberar(0);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //Alternativa 1
        getMenuInflater().inflate(R.menu.game_sound_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.sdPokemon:
                SuenaMuActual(R.raw.pokemon);
                return true;
            case R.id.sdMario:
                SuenaMuActual(R.raw.mario);
                return true;
            case R.id.sdNaruto:
                SuenaMuActual(R.raw.naruto);
                return true;
            case R.id.sdGow:
                SuenaMuActual(R.raw.godofwar);
                return true;
            case R.id.sdDeathNote:
                SuenaMuActual(R.raw.deathnotel);
                return true;
            case R.id.sdDMtheme:
                SuenaMuActual(R.raw.darthmaultheme);
                return true;
            case R.id.sdMegaman:
                SuenaMuActual(R.raw.mx6sigma);
                return true;
            case R.id.sdDbz:
                SuenaMuActual(R.raw.dbz);
                return true;
            case R.id.sdRadar:
                SuenaMuActual(R.raw.radar);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
