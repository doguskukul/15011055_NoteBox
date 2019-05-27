package com.example.notebox;

import android.Manifest;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class ActivityAddNote extends AppCompatActivity {
    private NotificationCompat.Builder builder;
    private static final int READ_REQUEST_CODE = 42;
    private EditText editBaslik,editAciklama,editSaat,editDakika,editTekrar;
    private TextView textDateOlusturulma,textDateGuncellenme,textDosyaYolu;
    private Button buttonOnay,buttonDosyaEkle,buttonDosyaAc,buttonHatirlatma;
    private Switch onceliklimi;
    private CheckBox mavi,kirmizi,yesil;
    private Veritabani vt;
    private int saat,dakika;
    private Boolean hatirlatmaDurum = false;
    private Animation fabacik,fabkapali,geridon,ileridon;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);

        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        != PackageManager.PERMISSION_GRANTED){
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1001);
        }

        vt = new Veritabani(this);
        final ArrayList<Note> notlar = new Veritabanidao().tumNotlar(vt);
        editBaslik = findViewById(R.id.editTextBaslik);
        editAciklama = findViewById(R.id.editTextAciklama);
        buttonOnay = findViewById(R.id.buttonAccept);
        textDateOlusturulma = findViewById(R.id.textViewDateOlusturulma);
        textDateGuncellenme = findViewById(R.id.textViewDateGuncellenme);
        onceliklimi = findViewById(R.id.switchOnceliklimi);
        mavi = findViewById(R.id.checkBoxMavi);
        kirmizi = findViewById(R.id.checkBoxKirmizi);
        yesil = findViewById(R.id.checkBoxYesil);
        textDosyaYolu = findViewById(R.id.textViewDosyaYolu);
        editSaat = findViewById(R.id.editSaat);
        editDakika = findViewById(R.id.editDakika);
        editTekrar = findViewById(R.id.editTekrar);

        buttonDosyaEkle = findViewById(R.id.buttonDosyaEkle);
        buttonDosyaAc = findViewById(R.id.buttonDosyaAc);

        fabacik = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fabacik);
        fabkapali = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fabkapali);
        geridon = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.geridon);
        ileridon = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.ileridon);
        buttonHatirlatma = findViewById(R.id.buttonHatirlatma);
        editTekrar.setVisibility(View.INVISIBLE);
        editDakika.setVisibility(View.INVISIBLE);
        editSaat.setVisibility(View.INVISIBLE);
        buttonHatirlatma.setOnClickListener(new View.OnClickListener( ) {
            @Override
            public void onClick(View v) {
                if(hatirlatmaDurum){
                    buttonHatirlatma.startAnimation(geridon);
                    editSaat.startAnimation(fabkapali);
                    editDakika.startAnimation(fabkapali);
                    editTekrar.startAnimation(fabkapali);
                    editSaat.setFocusableInTouchMode(false);
                    editDakika.setFocusableInTouchMode(false);
                    editTekrar.setFocusableInTouchMode(false);
                    editTekrar.setVisibility(View.INVISIBLE);
                    editDakika.setVisibility(View.INVISIBLE);
                    editSaat.setVisibility(View.INVISIBLE);
                    hatirlatmaDurum = false;
                }
                else{
                    buttonHatirlatma.startAnimation(ileridon);
                    editSaat.startAnimation(fabacik);
                    editDakika.startAnimation(fabacik);
                    editTekrar.startAnimation(fabacik);
                    editSaat.setFocusableInTouchMode(true);
                    editDakika.setFocusableInTouchMode(true);
                    editTekrar.setFocusableInTouchMode(true);
                    editTekrar.setVisibility(View.VISIBLE);
                    editDakika.setVisibility(View.VISIBLE);
                    editSaat.setVisibility(View.VISIBLE);
                    hatirlatmaDurum = true;
                }
            }
        });

        Intent intentAddNote = getIntent();
        editBaslik.setText(intentAddNote.getStringExtra("baslikAdapter"));
        editAciklama.setText(intentAddNote.getStringExtra("aciklamaAdapter"));
        textDateOlusturulma.setText(intentAddNote.getStringExtra("olusturulmaAdapter"));
        textDateGuncellenme.setText(intentAddNote.getStringExtra("guncellenmeAdapter"));
        textDosyaYolu.setText(intentAddNote.getStringExtra("dosyaYoluAdapter"));

        String pattern = "dd.MM.yyyy, HH:mm";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        Date suan = new Date();
        suan.setHours(suan.getHours());
        final String date = simpleDateFormat.format(suan);
        if(editBaslik.getText().length()==0) {
            textDateOlusturulma.setText(date);
            textDateGuncellenme.setText(date);
        }
        final String oncelik = "hayir";
        final String renk = "gri";

        final Note not = new Note(editBaslik.getText().toString(),editAciklama.getText().toString(),date,date,oncelik,renk,textDosyaYolu.getText().toString());
        buttonOnay.setOnClickListener(new View.OnClickListener( ) {
            public void onClick(View v) {
                not.setBaslik(editBaslik.getText().toString());
                not.setAciklama(editAciklama.getText().toString());
                not.setDosyaYolu(textDosyaYolu.getText().toString());
                int kontrol = 0;
                for(Note aNot:notlar){
                    if(aNot.getBaslik().compareTo(not.getBaslik())==0){
                        new Veritabanidao().notGuncelle(vt,not,date);
                        Log.e("info","guncellendi");
                        kontrol = 1;
                    }
                }
                if(kontrol==0){
                    new Veritabanidao().notEkle(vt,not);
                    Log.e("info","olusturuldu");
                }
                Intent intentNote = new Intent(ActivityAddNote.this,MainActivity.class);
                durumaBagli();
                startActivity(intentNote);
            }
        });

        onceliklimi.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener( ) {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked==true) {
                    not.setOncelik("evet");
                }
                if(isChecked==false) {
                    for(Note aNote:notlar){
                        if(aNote.getBaslik().compareTo(not.getBaslik())==0){
                            not.setOncelik(aNote.getOncelik());
                        }
                    }
                }
            }
        });

        mavi.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener( ) {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked==true){
                    not.setRenk("mavi");
                    editBaslik.setTextColor(Color.parseColor("#5C1D23"));
                    editAciklama.setTextColor(Color.parseColor("#5C1D23"));
                    kirmizi.setClickable(false);
                    yesil.setClickable(false);
                }
                if(isChecked==false){
                    for(Note aNote:notlar){
                        if(aNote.getBaslik().compareTo(not.getBaslik())==0){
                            not.setRenk(aNote.getRenk());
                        }
                    }
                    kirmizi.setClickable(true);
                    yesil.setClickable(true);
                }
            }
        });

        kirmizi.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener( ) {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked==true){
                    not.setRenk("kirmizi");
                    editBaslik.setTextColor(Color.parseColor("#DB646E"));
                    editAciklama.setTextColor(Color.parseColor("#DB646E"));
                    mavi.setClickable(false);
                    yesil.setClickable(false);
                }
                if(isChecked==false){
                    for(Note aNote:notlar){
                        if(aNote.getBaslik().compareTo(not.getBaslik())==0){
                            not.setRenk(aNote.getRenk());
                        }
                    }
                    mavi.setClickable(true);
                    yesil.setClickable(true);
                }
            }
        });

        yesil.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener( ) {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked==true){
                    not.setRenk("yesil");
                    editBaslik.setTextColor(Color.parseColor("#CF9D88"));
                    editAciklama.setTextColor(Color.parseColor("#CF9D88"));
                    kirmizi.setClickable(false);
                    mavi.setClickable(false);
                }
                if(isChecked==false){
                    for(Note aNote:notlar){
                        if(aNote.getBaslik().compareTo(not.getBaslik())==0){
                            not.setRenk(aNote.getRenk());
                        }
                    }
                    kirmizi.setClickable(true);
                    mavi.setClickable(true);
                }
            }
        });

        if(mavi.isChecked()==false && kirmizi.isChecked()==false && yesil.isChecked()==false){
            not.setRenk("gri");
        }

        buttonDosyaEkle.setOnClickListener(new View.OnClickListener( ) {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("*/*");
                startActivityForResult(intent, READ_REQUEST_CODE);
            }
        });

        buttonDosyaAc.setOnClickListener(new View.OnClickListener( ) {
            @Override
            public void onClick(View v) {
                OpenPDFFile(textDosyaYolu.getText().toString());
            }
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null) {
            Uri uploadfileuri = data.getData();
            File file = new File(uploadfileuri.getPath());
            Log.e("filepath",uploadfileuri.getPath());
            String filePath = uploadfileuri.getPath();
            String newFilePath = filePath.replace("/document/raw:/storage/emulated/0","/sdcard");
            Log.e("filePath",newFilePath);
            textDosyaYolu.setText(newFilePath);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case 1001:{
                if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    Toast.makeText(this,"Permission granted",Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(this,"Permission not granted",Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        }
    }
    public void OpenPDFFile(String filePath) {
        Log.e("OpenPDFFile", "girdi");
        final Uri data = FileProvider.getUriForFile(this, "com.example.notebox", new File(filePath));
        this.grantUriPermission(this.getPackageName(), data, Intent.FLAG_GRANT_READ_URI_PERMISSION);
        final Intent intent = new Intent(Intent.ACTION_VIEW)
                .setDataAndType(data, "*/*")
                .addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        this.startActivity(intent);
    }

    public void durumaBagli(){

        SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        int requestCode = sharedPref.getInt("id",10);

        NotificationManager bildirimYoneticisi =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        Intent intent = new Intent(ActivityAddNote.this,MainActivity.class);
        PendingIntent gidilecekIntent =
                PendingIntent.getActivity(this,requestCode,intent,PendingIntent.FLAG_UPDATE_CURRENT);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            String kanalId = "kanalId";
            String kanalAd = "kanalAd";
            String kanalTanim = "kanalTanim";
            final ArrayList<Note> notlar = new Veritabanidao().tumNotlar(vt);
            String oncelik = "hayir";
            int kanalOnceligi = NotificationManager.IMPORTANCE_DEFAULT;
            for(Note not: notlar){
                if(not.getBaslik().compareTo(editBaslik.getText().toString())==0){
                    oncelik = not.getOncelik();
                }
            }
            if(oncelik.compareTo("evet")==0){
                kanalOnceligi = NotificationManager.IMPORTANCE_HIGH;
            }
            else if(oncelik.compareTo("hayir")==0){
                kanalOnceligi = NotificationManager.IMPORTANCE_DEFAULT;
            }


            NotificationChannel kanal = bildirimYoneticisi.getNotificationChannel(kanalId);
            if(kanal==null){
                kanal = new NotificationChannel(kanalId,kanalAd,kanalOnceligi);
                kanal.setDescription(kanalTanim);
                bildirimYoneticisi.createNotificationChannel(kanal);
            }
            builder = new NotificationCompat.Builder(this,kanalId);
            builder.setContentTitle(editBaslik.getText().toString());
            builder.setContentText(editAciklama.getText().toString());
            builder.setSmallIcon(R.drawable.uyari);
            builder.setAutoCancel(true);
            builder.setContentIntent(gidilecekIntent);

        }
        else{
            builder = new NotificationCompat.Builder(this);
            builder.setContentTitle(editBaslik.getText().toString());
            builder.setContentText(editAciklama.getText().toString());
            builder.setSmallIcon(R.drawable.uyari);
            builder.setAutoCancel(true);
            builder.setContentIntent(gidilecekIntent);
            builder.setPriority(Notification.PRIORITY_HIGH);
        }

        Intent broadcastIntent = new Intent(ActivityAddNote.this,BildirimYakalayici.class);
        broadcastIntent.putExtra("nesne",builder.build());
        broadcastIntent.putExtra("id",requestCode);
        PendingIntent gidilecekBroadcast =
                PendingIntent.getBroadcast(this,requestCode,broadcastIntent,PendingIntent.FLAG_UPDATE_CURRENT);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        saat = Integer.parseInt(editSaat.getText().toString());
        dakika = Integer.parseInt(editDakika.getText().toString());
        calendar.set(Calendar.HOUR_OF_DAY,saat);
        calendar.set(Calendar.MINUTE,dakika);
        calendar.set(Calendar.SECOND,0);
        int tekrar = Integer.parseInt(editTekrar.getText().toString())*1000*60;
        editor.putInt("id",requestCode+1); //int değer kayıt eklemek için kullanıyoruz.
        editor.commit();
        Log.e("request", " "+requestCode);
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),tekrar,gidilecekBroadcast);
    }
}
