package com.example.notebox;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v4.content.res.TypedArrayUtils;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;


public class MyAdapter extends RecyclerView.Adapter<MyAdapter.CardTutucu>{
    private Context context;
    private ArrayList<Note> notlar;
    private Boolean deleteDurum;
    private Veritabani vt;

    public MyAdapter(Context context, ArrayList<Note> notlar, Boolean deleteDurum) {
        this.context = context;
        this.notlar = notlar;
        this.deleteDurum = deleteDurum;
    }

    public class CardTutucu extends RecyclerView.ViewHolder {
        private CardView cardView;
        private TextView textViewNote,textViewAciklama;
        private ImageView imageYildiz;
        private Button buttonDelete;
        private Animation fabacik,fabkapali;

        public CardTutucu(@NonNull View view) {
            super(view);
            cardView = view.findViewById(R.id.noteCard);
            textViewNote = view.findViewById(R.id.textViewNote);
            textViewAciklama = view.findViewById(R.id.textViewAciklama);
            imageYildiz = view.findViewById(R.id.imageYildiz);
            buttonDelete = view.findViewById(R.id.buttonDelete);
            fabacik = AnimationUtils.loadAnimation(context,R.anim.fabacik);
            fabkapali = AnimationUtils.loadAnimation(context,R.anim.fabkapali);
        }
    }

    public CardTutucu onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_tasarim,parent,false);
        return new CardTutucu(v);
    }

    public void onBindViewHolder(@NonNull final CardTutucu cardTutucu, final int i) {
        if(deleteDurum == false){
            cardTutucu.buttonDelete.setVisibility(View.INVISIBLE);
            cardTutucu.buttonDelete.setClickable(false);
        }
        else if(deleteDurum == true){
            cardTutucu.buttonDelete.setVisibility(View.VISIBLE);
            cardTutucu.buttonDelete.setClickable(true);
        }
        String text = notlar.get(i).getBaslik();
        String aciklama = notlar.get(i).getAciklama();
        cardTutucu.textViewNote.setText(text);
        cardTutucu.textViewAciklama.setText(aciklama);
        if(notlar.get(i).getRenk().compareTo("mavi")==0) {
            cardTutucu.textViewNote.setTextColor(Color.parseColor("#5C1D23"));
        }
        if(notlar.get(i).getRenk().compareTo("kirmizi")==0) {
            cardTutucu.textViewNote.setTextColor(Color.parseColor("#DB646E"));
        }
        if(notlar.get(i).getRenk().compareTo("yesil")==0) {
            cardTutucu.textViewNote.setTextColor(Color.parseColor("#CF9D88"));
        }
        if(notlar.get(i).getRenk().compareTo("gri")==0) {
            cardTutucu.textViewNote.setTextColor(Color.parseColor("#616161"));
        }
        if(notlar.get(i).getOncelik().compareTo("evet")==0){
            cardTutucu.imageYildiz.setVisibility(View.VISIBLE);
        }
        if(notlar.get(i).getOncelik().compareTo("hayir")==0){
            cardTutucu.imageYildiz.setVisibility(View.INVISIBLE);
        }
        cardTutucu.textViewNote.setOnClickListener(new View.OnClickListener( ) {
            @Override
            public void onClick(View v) {
                Intent intentAdapter = new Intent(context,ActivityAddNote.class);
                intentAdapter.putExtra("baslikAdapter",notlar.get(i).getBaslik());
                intentAdapter.putExtra("aciklamaAdapter",notlar.get(i).getAciklama());
                intentAdapter.putExtra("olusturulmaAdapter",notlar.get(i).getOlusturulma());
                intentAdapter.putExtra("guncellenmeAdapter",notlar.get(i).getGuncellenme());
                intentAdapter.putExtra("oncelikAdapter",notlar.get(i).getOncelik());
                intentAdapter.putExtra("renkAdapter",notlar.get(i).getRenk());
                intentAdapter.putExtra("dosyaYoluAdapter",notlar.get(i).getDosyaYolu());
                context.startActivity(intentAdapter);
            }
        });
        cardTutucu.buttonDelete.setOnClickListener(new View.OnClickListener( ) {
            @Override
            public void onClick(View v) {
                vt = new Veritabani(context);
                new Veritabanidao().notSil(vt,notlar.get(i));
                notlar.remove(notlar.get(i));
                Intent intentAdapter = new Intent(context,MainActivity.class);
                context.startActivity(intentAdapter);
            }
        });
    }
    public int getItemCount() {
        return notlar.size();
    }

}
