package com.example.notebox;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.SearchView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {
    private RecyclerView rv;
    private MyAdapter adapter;
    private FloatingActionButton fabAdd,fabDelete,fabNote;
    private Animation fabacik,fabkapali,geridon,ileridon;
    private Boolean fabDurum = false,deleteDurum;
    private Veritabani vt;
    private Toolbar toolbar;
    private ArrayList<Note> aramaNotlar = new ArrayList<>(),veriTabaniNotlar,notlarSıralanmis ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fabAdd = findViewById(R.id.fab_add);
        fabNote = findViewById(R.id.fab_addNote);
        fabDelete = findViewById(R.id.fab_addImage);

        fabacik = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fabacik);
        fabkapali = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fabkapali);
        geridon = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.geridon);
        ileridon = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.ileridon);

        rv = findViewById(R.id.recyclerView);
        rv.setHasFixedSize(true);
        rv.setLayoutManager(new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL));

        SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        deleteDurum = sharedPref.getBoolean("deleteDurum",false);

        vt = new Veritabani(this);
        veriTabaniNotlar = new Veritabanidao().tumNotlar(vt);
        notlarSıralanmis = new ArrayList<>();

        for(Note aNot : veriTabaniNotlar){
            if(aNot.getOncelik().compareTo("evet")==0){
                notlarSıralanmis.add(aNot);
                Log.e("hata",aNot.getOncelik()+" "+aNot.getBaslik());
            }
        }
        for(Note aNot : veriTabaniNotlar){
            if(aNot.getOncelik().compareTo("hayir")==0){
                notlarSıralanmis.add(aNot);
                Log.e("hata",aNot.getOncelik()+" "+aNot.getBaslik());
            }
        }
        adapter = new MyAdapter(this,veriTabaniNotlar,deleteDurum);
        rv.setAdapter(adapter);

        fabAdd.setOnClickListener(new View.OnClickListener( ) {
            @Override
            public void onClick(View v) {
                if(fabDurum){
                    fabAdd.startAnimation(geridon);
                    fabDelete.startAnimation(fabkapali);
                    fabNote.startAnimation(fabkapali);
                    fabDelete.setClickable(false);
                    fabNote.setClickable(false);
                    fabDurum = false;
                }
                else{
                    fabAdd.startAnimation(ileridon);
                    fabDelete.startAnimation(fabacik);
                    fabNote.startAnimation(fabacik);
                    fabDelete.setClickable(true);
                    fabNote.setClickable(true);
                    fabDurum = true;
                }
            }
        });
        fabNote.setOnClickListener(new View.OnClickListener( ) {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this,ActivityAddNote.class);
                startActivity(i);
            }
        });
        fabDelete.setOnClickListener(new View.OnClickListener( ) {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                if (deleteDurum == false){
                    editor.putBoolean("deleteDurum",true); //int değer kayıt eklemek için kullanıyoruz.
                    editor.commit();
                }
                else if(deleteDurum == true){
                    editor.putBoolean("deleteDurum",false); //int değer kayıt eklemek için kullanıyoruz.
                    editor.commit();
                }
                Intent i = new Intent(MainActivity.this,MainActivity.class);
                startActivity(i);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu,menu);
        MenuItem item = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) item.getActionView();
        searchView.setOnQueryTextListener(this);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_sirala_zaman:
                adapter = new MyAdapter(this,veriTabaniNotlar,deleteDurum);
                rv.setAdapter(adapter);
                return true;
            case R.id.action_sirala_onem:
                adapter = new MyAdapter(this,notlarSıralanmis,deleteDurum);
                rv.setAdapter(adapter);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onQueryTextSubmit(String s) {

        Log.e("TextSubmit",s);

        vt = new Veritabani(this);
        ArrayList<Note> veriTabaniNotlar = new Veritabanidao().tumNotlar(vt);
        for (Note not : veriTabaniNotlar){
            if (not.getGuncellenme().indexOf(s)!=-1){
                aramaNotlar.add(not);
                Log.e("guncellenme",not.getGuncellenme());
                Log.e("s",s);
            }
        }
        adapter = new MyAdapter(this,aramaNotlar,deleteDurum);
        rv.setAdapter(adapter);
        if (s.compareTo("hepsi")==0){
            Intent i = new Intent(MainActivity.this,MainActivity.class);
            startActivity(i);
        }
        return false;
    }

    @Override
    public boolean onQueryTextChange(String s) {

        Log.e("TextChange",s);

        return false;
    }
}
