package com.example.mcandi.amigosecreto;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    private static final int MY_PERMISSIONS_REQUEST_SEND_SMS = 0;

    private List<Amigo> amigos = new ArrayList<>();
    private Context context;
    EditText nome, telefone;
    ListView listaAmigos;
    Button btnAdicionar, btnSortear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = MainActivity.this;
        nome = findViewById(R.id.nome);
        telefone = findViewById(R.id.telefone);
        listaAmigos = findViewById(R.id.listaAmigos);
        btnAdicionar = findViewById(R.id.btnAddAmigo);
        btnSortear = findViewById(R.id.btnSortear);

        btnAdicionar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adcionarAmigo();
                atualizarAmigos();
            }
        });

        btnSortear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enviaNome();
            }
        });

    }

    private void atualizarAmigos(){
        try{
            ArrayAdapter<Amigo> adapter = new ArrayAdapter<>(context, android.R.layout.simple_list_item_1, amigos);
            listaAmigos.setAdapter(adapter);

        }catch (Exception ex){
            Log.e("ATUALIZA", ex.getMessage());
        }
    }

    private void adcionarAmigo() {
        try{

            Amigo amigo = new Amigo();

            String tel = telefone.getText().toString().trim();
            String nom = nome.getText().toString().trim();

            amigo.setNomeParticipante(nom);
            amigo.setTelefone(tel);

            for (Amigo item : amigos) {
                if(item.getTelefone().equals(tel)){
                    Toast.makeText(context, "Já existe uma pessoa cadastrada com o telefone " + tel,
                            Toast.LENGTH_SHORT).show();
                    return;
                }
            }

            amigos.add(amigo);
            limpaCampos();

            // adapter.notifyDataSetChanged();
            Toast.makeText(context, "Amigo Adcionado!",
                    Toast.LENGTH_SHORT).show();

        }catch (Exception ex){

        }
    }

    private void enviaNome(){
        if (ContextCompat.checkSelfPermission(context,
                Manifest.permission.SEND_SMS)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context,
                    Manifest.permission.SEND_SMS)) {
            } else {
                ActivityCompat.requestPermissions((Activity) context,
                        new String[]{Manifest.permission.SEND_SMS},
                        MY_PERMISSIONS_REQUEST_SEND_SMS);
            }
        }
    }

    private void limpaCampos(){
        nome.requestFocus();
        nome.setText("");
        telefone.setText("");
    }

    private List<Amigo> sortearAmigoSecreto(List<Amigo> amigos) {
        List<Amigo> amigosSelecionados = amigos;
        Random randon = new Random();
        List<String> lista = new ArrayList<>();

        while (lista.size() < amigos.size()) {
            int valor = randon.nextInt(amigos.size());
            if (!lista.contains(amigos.get(valor).getNomeParticipante())) {
                lista.add(amigos.get(valor).getNomeParticipante());
            }
        }

        String nomePart, nomeAmigo;
        int index = 0;
        while (index < amigos.size()) {
            for (int i = 0; i < amigos.size(); i++) {
                nomePart = amigosSelecionados.get(i).getNomeParticipante();
                    for (int i1 = 0; i1 < amigos.size(); i1++) {
                        nomeAmigo = lista.get(i1);
                        if (nomePart != nomeAmigo) {
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

    private void enviaSMS(Amigo amigo) {
        try {
            String mensagem = "Olá " + amigo.getNomeParticipante() + " o seu amigo secreto é: " +
                    amigo.getSeuAmigoSecreto();

            String mensagem_limpa = deAccent(mensagem);

            SmsManager.getDefault().sendTextMessage(amigo.getTelefone(), null,
                    mensagem_limpa, null, null);

        } catch (Exception e) {
            AlertDialog.Builder alertDialogBuilder = new
                    AlertDialog.Builder(this);
            AlertDialog dialog = alertDialogBuilder.create();
            dialog.setMessage(e.getMessage());
            dialog.show();
        }
    }

    private String deAccent(String str) {
        String nfdNormalizedString = Normalizer.normalize(str, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        return pattern.matcher(nfdNormalizedString).replaceAll("");
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_SEND_SMS: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    List<Amigo> amigosSelecionados = sortearAmigoSecreto(amigos);
                    for(int i=0; i<amigosSelecionados.size(); i++){
                        enviaSMS(amigosSelecionados.get(i));
                    }

                    //amigos = null;
                    Toast.makeText(context, "Sorteio Finalizado!",
                            Toast.LENGTH_SHORT).show();

                    atualizarAmigos();

                } else {
                    Toast.makeText(getApplicationContext(),
                            "Permita o envio de SMS para o App", Toast.LENGTH_LONG).show();
                    return;
                }
            }
        }

    }

}
