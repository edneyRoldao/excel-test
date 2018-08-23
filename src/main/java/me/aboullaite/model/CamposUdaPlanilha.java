package me.aboullaite.model;

import java.util.List;

public class CamposUdaPlanilha {

    private String nomeUda;
    private List<String> valoresUda;

    public CamposUdaPlanilha(String nomeUda, List<String> valoresUda) {
        this.nomeUda = nomeUda;
        this.valoresUda = valoresUda;
    }

    public String getNomeUda() {
        return nomeUda;
    }

    public void setNomeUda(String nomeUda) {
        this.nomeUda = nomeUda;
    }

    public List<String> getValoresUda() {
        return valoresUda;
    }

    public void setValoresUda(List<String> valoresUda) {
        this.valoresUda = valoresUda;
    }
}
