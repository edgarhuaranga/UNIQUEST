package edhuar.home.com.uniquest;

import android.Manifest;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Handler;
import android.os.RemoteException;
import android.os.Vibrator;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
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
    int PERMISSION_ALL = 1;
    String[] PERMISSIONS = {Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.WAKE_LOCK};


    LinearLayout p0, p1, p2, p3, p4, p5;
    Button notButton, stopButton, setButton;

    NotificationManager notificationManager;
    boolean isNotificActive = false;
    int notifID = 33;
    boolean ganador;

    ///GIO
    Sonido sonido;
    private int TiempoCiclo=500;
    long mtime;
    private boolean activo;
    int muactual;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        ganador = false;
        sonido=new Sonido(this);
        sonido.Play(R.raw.deathnotel);
        if(!hasPermissions(this, PERMISSIONS)){
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
        }
        activo =true;
        p0 = (LinearLayout) findViewById(R.id.piece0);
        p1 = (LinearLayout) findViewById(R.id.piece1);
        p2 = (LinearLayout) findViewById(R.id.piece2);
        p3 = (LinearLayout) findViewById(R.id.piece3);
        p4 = (LinearLayout) findViewById(R.id.piece4);
        p5 = (LinearLayout) findViewById(R.id.piece5);

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

                Log.d("UNIQUEST2", "El dispositivo "+device.getRssi()+" fue descubierto "+System.currentTimeMillis());
                if(device.getMacAddress().equalsIgnoreCase("CF:4A:EC:23:47:99") ){//NBe01
                    setMtime();
                    if((device.getRssi()>-85) )
                        p0.setVisibility(View.INVISIBLE);
                    ((Vibrator) getSystemService(VIBRATOR_SERVICE)).vibrate(500);
                }
                if(device.getMacAddress().equalsIgnoreCase("DD:3F:F4:CF:13:66") ){//NBe02
                    setMtime();
                    if((device.getRssi()>-85) ){
                        p1.setVisibility(View.INVISIBLE);
                        ((Vibrator) getSystemService(VIBRATOR_SERVICE)).vibrate(500);}
                }
                if(device.getMacAddress().equalsIgnoreCase("E2:BE:2C:EC:C0:E2") ){//Be04
                    setMtime();
                    if((device.getRssi()>-85) ){
                        p2.setVisibility(View.INVISIBLE);
                        ((Vibrator) getSystemService(VIBRATOR_SERVICE)).vibrate(500);}
                }
                if(device.getMacAddress().equalsIgnoreCase("FB:D3:B5:B9:89:F2") ){//Be08
                    setMtime();
                    if((device.getRssi()>-85) ){
                        p3.setVisibility(View.INVISIBLE);
                        ((Vibrator) getSystemService(VIBRATOR_SERVICE)).vibrate(500);}
                }
                if(device.getMacAddress().equalsIgnoreCase("C9:EC:7A:17:D8:D5")){//Be10
                    setMtime();
                    if((device.getRssi()>-85) ){
                        ((Vibrator) getSystemService(VIBRATOR_SERVICE)).vibrate(500);
                        p4.setVisibility(View.INVISIBLE);}
                }
                if(device.getMacAddress().equalsIgnoreCase("EA:EF:87:2F:4D:93")){///Be11
                    setMtime();
                    if((device.getRssi()>-85) ){
                        ((Vibrator) getSystemService(VIBRATOR_SERVICE)).vibrate(500);
                        p5.setVisibility(View.INVISIBLE);}
                }
            }
        });

        final LinearLayout backG = (LinearLayout) findViewById(R.id.backGround);
        //Ojala no se cague
        final AnimationDrawable drawable = new AnimationDrawable();
        final Handler handler = new Handler();

        drawable.addFrame(new ColorDrawable(Color.WHITE), 400);
        drawable.addFrame(new ColorDrawable(Color.GREEN), 400);
        drawable.setOneShot(false);

        backG.setBackground(drawable);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.d("ganador",ganador+"");
                if(!ganador){
                    winornot();
                    Log.d("ifganador",ganador+"");
                }
                drawable.start();
            }
        }, 100);
    }

    public static boolean hasPermissions(Context context, String... permissions) {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
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
                    if(time>4000){
                        SuenaMuActual(muactual);

                    }
                    else {
                        Log.d("UNIQUEST2","Suena Radar_" + time);

                    }
                    esperoTiempo(TiempoCiclo);
                    //winornot();
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
        builder.setTitle("Pistas").setMessage("En caso no lo tengas, tu telefono se puede rayar, o lo puedes usar como decoracion");
        builder.setPositiveButton("Entendido", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();

    }

    public void showClue1(View view){
        AlertDialog.Builder builder = new AlertDialog.Builder(GameActivity.this);
        builder.setTitle("Pistas").setMessage("Una fecha especial, algo debes entregar y en este lugar lo puedes comprar");
        builder.setPositiveButton("Entendido", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void showClue2(View view){
        AlertDialog.Builder builder = new AlertDialog.Builder(GameActivity.this);
        builder.setTitle("Pistas").setMessage("De los primeros electrodomesticos que compras al mudarte");
        builder.setPositiveButton("Entendido", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void showClue3(View view){
        AlertDialog.Builder builder = new AlertDialog.Builder(GameActivity.this);
        builder.setTitle("Pistas").setMessage("Stand rosado");
        builder.setPositiveButton("Entendido", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void showClue4(View view){
        AlertDialog.Builder builder = new AlertDialog.Builder(GameActivity.this);
        builder.setTitle("Pistas").setMessage("No son necesarias pero a la gente les gusta usarlas, a veces son simbolo de compromiso");
        builder.setPositiveButton("Entendido", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void showClue5(View view){
        AlertDialog.Builder builder = new AlertDialog.Builder(GameActivity.this);
        //builder.setTitle("Pistas").setMessage("");
        builder.setTitle("Pistas").setMessage("Lugar muy concurrido aqui en megaplaza, principalmente en verano, aunque el heladero del Parque es una opcion");
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

        //SuenaMuActual(R.raw.darthmaultheme);
        activo=true;
        HiloAlternativo();
        winornot();
    }

    public void winornot(){
        if(p1.getVisibility()==View.INVISIBLE && p2.getVisibility()==View.INVISIBLE && p3.getVisibility()==View.INVISIBLE && p0.getVisibility()==View.INVISIBLE && p4.getVisibility()==View.INVISIBLE && p5.getVisibility()==View.INVISIBLE){
            AlertDialog.Builder builder = new AlertDialog.Builder(GameActivity.this);
            builder.setTitle("Felicitaciones").setMessage("Regresa al stand de la UNI por tu premio :3");
            builder.setPositiveButton("Entendido", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });

            AlertDialog dialog = builder.create();
            dialog.show();
            ganador = true;
        }
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