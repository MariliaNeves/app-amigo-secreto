package com.example.mcandi.amigosecreto;

import android.util.Base64;

import java.io.UnsupportedEncodingException;

/**
 * Created by mcandi on 09/11/2017.
 */

public class Amigo {

  public String nomeParticipante;
  public String telefone;
  public  String seuAmigoSecreto;

  public String getSeuAmigoSecreto() {
    return seuAmigoSecreto;
  }

  public void setSeuAmigoSecreto(String seuAmigoSecreto) {
    this.seuAmigoSecreto = seuAmigoSecreto;
  }

  public String getTelefone() {
    return telefone;
  }

  public void setTelefone(String telefone) {
    this.telefone = telefone;
  }

  public String getNomeParticipante() {
    return nomeParticipante;
  }

  public void setNomeParticipante(String nomeParticipante) {
    this.nomeParticipante = nomeParticipante;
  }

  @Override
  public String toString(){
    if(getSeuAmigoSecreto() == null || getSeuAmigoSecreto().equals("")) {
      return getNomeParticipante() + " - " + getTelefone();
    }else{

      byte[] encrpt= new byte[0];
      String base64 = "";
      try {
        encrpt = getSeuAmigoSecreto().getBytes("UTF-8");

        base64 = Base64.encodeToString(encrpt, Base64.DEFAULT);
      } catch (UnsupportedEncodingException e) {
        e.printStackTrace();
      }

      return getNomeParticipante() + " - " + getTelefone() + " ("+ base64 +")";
    }
  }


}
