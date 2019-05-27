package com.example.notebox;

public class Note {
    private String baslik;
    private String aciklama;
    private String olusturulma;
    private String guncellenme;
    private String oncelik;
    private String renk;
    private String dosyaYolu;

    public Note(String baslik, String aciklama, String olusturulma, String guncellenme, String oncelik, String renk, String dosyaYolu) {
        this.baslik = baslik;
        this.aciklama = aciklama;
        this.olusturulma = olusturulma;
        this.guncellenme = guncellenme;
        this.oncelik = oncelik;
        this.renk = renk;
        this.dosyaYolu = dosyaYolu;
    }

    public String getBaslik() {
        return baslik;
    }

    public void setBaslik(String baslik) {
        this.baslik = baslik;
    }

    public String getAciklama() {
        return aciklama;
    }

    public void setAciklama(String aciklama) {
        this.aciklama = aciklama;
    }

    public String getOlusturulma() {
        return olusturulma;
    }

    public void setOlusturulma(String olusturulma) {
        this.olusturulma = olusturulma;
    }

    public String getGuncellenme() {
        return guncellenme;
    }

    public void setGuncellenme(String guncellenme) {
        this.guncellenme = guncellenme;
    }

    public String getOncelik() {
        return oncelik;
    }

    public void setOncelik(String oncelik) {
        this.oncelik = oncelik;
    }

    public String getRenk() {
        return renk;
    }

    public void setRenk(String renk) {
        this.renk = renk;
    }

    public String getDosyaYolu() {
        return dosyaYolu;
    }

    public void setDosyaYolu(String dosyaYolu) {
        this.dosyaYolu = dosyaYolu;
    }
}
