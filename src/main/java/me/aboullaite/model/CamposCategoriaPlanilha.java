package me.aboullaite.model;

import java.util.List;

public class CamposCategoriaPlanilha {

    private String nomeCategoria;
    private List<CamposUdaPlanilha> udas;

    public CamposCategoriaPlanilha(String nomeCategoria, List<CamposUdaPlanilha> udas) {
        this.nomeCategoria = nomeCategoria;
        this.udas = udas;
    }

    public String getNomeCategoria() {
        return nomeCategoria;
    }

    public void setNomeCategoria(String nomeCategoria) {
        this.nomeCategoria = nomeCategoria;
    }

    public List<CamposUdaPlanilha> getUdas() {
        return udas;
    }

    public void setUdas(List<CamposUdaPlanilha> udas) {
        this.udas = udas;
    }
}
