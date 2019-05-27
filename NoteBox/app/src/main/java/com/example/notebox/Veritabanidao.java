package com.example.notebox;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

public class Veritabanidao {
    public void notEkle(Veritabani vt,Note not){
        SQLiteDatabase dbx = vt.getWritableDatabase();
        ContentValues degerler = new ContentValues();

        degerler.put("not_baslik",not.getBaslik());
        degerler.put("not_aciklama",not.getAciklama());
        degerler.put("not_olusturulma",not.getOlusturulma());
        degerler.put("not_guncellenme",not.getGuncellenme());
        degerler.put("not_oncelik",not.getOncelik());
        degerler.put("not_renk",not.getRenk());
        degerler.put("not_dosyaYolu",not.getDosyaYolu());

        dbx.insertOrThrow("notlar",null,degerler);
        dbx.close();
    }

    public void notGuncelle(Veritabani vt,Note not,String date){
        SQLiteDatabase dbx = vt.getWritableDatabase();
        ContentValues degerler = new ContentValues();

        degerler.put("not_baslik",not.getBaslik());
        degerler.put("not_aciklama",not.getAciklama());
        degerler.put("not_guncellenme",date);
        degerler.put("not_oncelik",not.getOncelik());
        degerler.put("not_renk",not.getRenk());
        degerler.put("not_dosyaYolu",not.getDosyaYolu());

        dbx.update("notlar",degerler,"not_baslik=?",new String[]{not.getBaslik()});
        dbx.close();
    }

    public void oncelikGuncelle(Veritabani vt,String baslik,String oncelik){
        SQLiteDatabase dbx = vt.getWritableDatabase();
        ContentValues degerler = new ContentValues();

        degerler.put("not_oncelik",oncelik);
        dbx.update("notlar",degerler,"not_baslik=?",new String[]{baslik});
        dbx.close();
    }

    public void notSil(Veritabani vt,Note note){
        SQLiteDatabase dbx = vt.getWritableDatabase();

        dbx.delete("notlar","not_baslik=?",new String[]{note.getBaslik()});
        dbx.close();
    }

    public ArrayList<Note> tumNotlar(Veritabani vt){
        ArrayList<Note> noteArrayList = new ArrayList<>();
        SQLiteDatabase dbx = vt.getWritableDatabase();

        Cursor c = dbx.rawQuery("SELECT * FROM notlar",null);

        while(c.moveToNext()){
            Note not =  new Note(c.getString(c.getColumnIndex("not_baslik"))
                    ,c.getString(c.getColumnIndex("not_aciklama"))
                    ,c.getString(c.getColumnIndex("not_olusturulma"))
                    ,c.getString(c.getColumnIndex("not_guncellenme"))
                    ,c.getString(c.getColumnIndex("not_oncelik"))
                    ,c.getString(c.getColumnIndex("not_renk"))
                    ,c.getString(c.getColumnIndex("not_dosyaYolu")));
            noteArrayList.add(not);
        }
        return noteArrayList;
    }

}
