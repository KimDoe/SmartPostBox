package com.example.smartpostbox;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import android.app.Application;
//import io.particle.android.sdk.cloud.ParticleCloudSDK;
import android.content.Context;

import io.particle.android.sdk.cloud.ParticleCloud;
import io.particle.android.sdk.cloud.ParticleCloudSDK;
import io.particle.android.sdk.cloud.ParticleDevice;
import io.particle.android.sdk.cloud.exceptions.ParticleCloudException;
import io.particle.android.sdk.utils.Async;
import io.particle.android.sdk.utils.Toaster;

public class SmartpostboxMain extends AppCompatActivity { //AppCompatActivity

    Button btnLogout;
    ImageView imgVollSma, imgLeerSma;
    TextView TVAnzeigeSma, TVSmart;
    CheckBox CBAnzeigeAktivierenSma;
    FloatingActionButton btnPlusSma;

    final String url_particle = "console.particle.io/devices/e00fce68b3c08327b9815931";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ParticleCloudSDK.init(this);
        setContentView(R.layout.activity_smartpostbox_main);

        btnLogout = (Button) findViewById(R.id.btnLogout);
        btnPlusSma = (FloatingActionButton) findViewById(R.id.btnPlusSma);

        imgLeerSma = (ImageView) findViewById(R.id.imgLeer_);
        imgVollSma = (ImageView) findViewById(R.id.imgVoll_);

        TVAnzeigeSma = (TextView) findViewById(R.id.TVAnzeigeSma);
        TVSmart = (TextView) findViewById(R.id.TVSmart);

        CBAnzeigeAktivierenSma = (CheckBox) findViewById(R.id.CBAnzeigeAktivierenSma);

        imgLeerSma.setVisibility(View.VISIBLE);
        imgVollSma.setVisibility(View.INVISIBLE);

        //Zurück Button -> Main
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openMain();
            }
        });

        //Button Plus -> Registrierung
        btnPlusSma.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openRegistrierung();
            }
        });

                if (internetAvailable()){
                    sendToServer();
                    System.out.println("Daten werden ausgelesen");
                }else{
                    System.out.println("Keine sichere Verbindung");
                }
        try {
            particle_call();
            System.out.println("try1");
        } catch (ParticleCloudException e) {
            e.printStackTrace();
            System.out.println("catch1");
        }
    }

    //Zurück zur Startseite -> über einen Button
    public void openMain () {
        Intent Main = new Intent(this, MainActivity.class);
        startActivity(Main);
    }

    //Zurück zu Registrierung -> über einen Button
    public void openRegistrierung () {
        Intent Reg = new Intent(this, Registrierung.class);
        startActivity(Reg);
    }

    //Die Steuerung der Anzeige
    public void postboxAnzeige (){
                if (time == "true"){
                    imgVollSma.setVisibility(View.VISIBLE);
                    imgLeerSma.setVisibility(View.INVISIBLE);
                    TVAnzeigeSma.setText("Sie haben Post!");
                    notification();
                }
                else {
                    imgVollSma.setVisibility(View.INVISIBLE);
                    imgLeerSma.setVisibility(View.VISIBLE);
                    TVAnzeigeSma.setText("Der Briefkasten ist leer!");
                }
    }

    //Senden der Benachrichtung an das Handy -> Push up Nachrichten
    public void notification(){
        if (CBAnzeigeAktivierenSma.isChecked()){
        NotificationCompat.Builder notificationBuilder = (NotificationCompat.Builder) new NotificationCompat.Builder(this)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher_foreground))
                .setContentTitle("Nachricht von SmartPostbox")
                .setContentText("Sie haben Post!");
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1, notificationBuilder.build());
    }else{}
    }

    //Daten vom Server empfangen/Daten anfragen
    public void sendToServer() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL scriptur1 = new URL(url_particle);
                    HttpURLConnection connection = (HttpURLConnection) scriptur1.openConnection();
                    connection.setDoInput(true);

                    InputStream datenHolen = connection.getInputStream();
                    final String antwort = getTextFromCloud(datenHolen);

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            TVSmart.setText(antwort);
                        }
                    });
                    datenHolen.close();
                    connection.disconnect();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                    System.out.println("Keine Verbindung");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }).start();
    }

   //Internet Verbindung überprüfen
    public boolean internetAvailable(){
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnectedOrConnecting();
    }

   //Textdatein von der Cloud erhalten
    public String getTextFromCloud(InputStream is){
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder stringBuilder = new StringBuilder();
        String aktuelleZeile;

        try {
            while ((aktuelleZeile = reader.readLine()) != null){
                stringBuilder.append(aktuelleZeile);
                stringBuilder.append("\n");

            }
        }catch (IOException e){
            e.printStackTrace();
        }

        return stringBuilder.toString().trim();
    }

    public void updateUI(int time){
        TVSmart.setText(time);
    }

    public void particle_call() throws ParticleCloudException {

        Async.executeAsync(ParticleCloudSDK.getCloud(), new Async.ApiWork<ParticleCloud, Integer>() {
            public Integer callApi(ParticleCloud particleCloud)
                    throws ParticleCloudException, IOException {
                    particleCloud.logIn("", "");//Login Eingaben reinschreiben
                    ParticleDevice dev = particleCloud.getDevice("E00FCE68B3C08327B9815931");
                    int time;
                try {
                    time= dev.getIntVariable("PostInBox");

                    System.out.println(time);
                    System.out.println("Error stage 1");

                } catch (ParticleDevice.VariableDoesNotExistException e) {
                    e.printStackTrace();
                    time = 2345;
                    System.out.println("Error stage 1_2");
                }
                return time;
            }
            @Override
            public void onSuccess(Integer value) {
                System.out.println(value);
                System.out.println("This meas success!");
            }

            @Override
            public void onFailure(ParticleCloudException e) {
                Log.e("some tag", "Something went wrong making an SDK call: ", e);
                System.out.println("Faaaaiiiill");
                //Toaster.l(MyActivity.this, "Uh oh, something went wrong.");
            }

            protected void onPostExecute(Integer value){
                TVSmart.setText(value);
            }

        });

    }
}