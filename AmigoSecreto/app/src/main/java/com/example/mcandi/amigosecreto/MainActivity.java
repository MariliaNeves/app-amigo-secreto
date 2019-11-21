package com.example.mcandi.amigosecreto;

import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {


    private List<Amigo> amigos = new ArrayList<>();
//    private ArrayAdapter<Amigo> adapter;
//    ListView listaAmigos;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_main);
//      listaAmigos = (ListView) findViewById(R.id.listaAmigos);
//      adapter = new ArrayAdapter<Amigo>(MainActivity.this, android.R.layout.activity_list_item,amigos);
//      listaAmigos.setAdapter(adapter);
    }


    public void adcionarAmigo(View view) {
        EditText nome = (EditText) findViewById(R.id.nome);
        EditText telefone = (EditText) findViewById(R.id.telefone);
        Amigo amigo = new Amigo();
        amigo.setNomeParticipante(nome.getText().toString());
        amigo.setTelefone(telefone.getText().toString());
        amigos.add(amigo);
        limpaCampos();
       // adapter.notifyDataSetChanged();
        Toast.makeText(MainActivity.this, "Amigo Adcionado!",
                Toast.LENGTH_SHORT).show();
    }

    public void enviaNome(View view){
      List<Amigo> amigosSelecionados = sortiarAmigoSecreto(amigos);
      for(int i=0; i<amigosSelecionados.size(); i++){
            enviaSMS((amigosSelecionados.get(i)));
        }
        amigos=null;
        Toast.makeText(MainActivity.this, "Sorteio Finalizado!",
                Toast.LENGTH_SHORT).show();
    }

    public void limpaCampos(){
        EditText nome = (EditText) findViewById(R.id.nome);
        nome.requestFocus();
        EditText telefone = (EditText) findViewById(R.id.telefone);
        nome.setText("");
        telefone.setText("");
    }



    public List<Amigo> sortiarAmigoSecreto(List<Amigo> amigos) {
        List<Amigo> amigosSelecionados = amigos;
        Random randon = new Random();
        List<String> lista = new ArrayList<>();

        while (lista.size() < amigos.size()) {
            int valor = randon.nextInt(amigos.size());
            if (!lista.contains(amigos.get(valor).getNomeParticipante())) {
                lista.add(amigos.get(valor).getNomeParticipante());
            }
        }
        int index = 0;
        while (index < amigos.size()) {
            for (int i = 0; i < amigos.size(); i++) {
                if (amigosSelecionados.get(i).getNomeParticipante() != lista.get(i)) {
                    for (int i1 = 0; i1 < amigos.size(); i1++) {
                        if (amigosSelecionados.get(i1).getSeuAmigoSecreto() != lista.get(i)) {
                            amigosSelecionados.get(i).setSeuAmigoSecreto(lista.get(i));
                            index++;
                            break;
                        }
                    }

                }
            }
        }

        return amigosSelecionados;

    }

    public void enviaSMS(Amigo amigo) {
        try {
            SmsManager.getDefault().sendTextMessage(amigo.getTelefone(), null,
                    "Olá " + amigo.getNomeParticipante() + " o seu amigo secreto é: " + amigo.getSeuAmigoSecreto(), null, null);
        } catch (Exception e) {
            AlertDialog.Builder alertDialogBuilder = new
                    AlertDialog.Builder(this);
            AlertDialog dialog = alertDialogBuilder.create();
            dialog.setMessage(e.getMessage());
            dialog.show();
        }
    }




}
