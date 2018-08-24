package me.aboullaite.model;

import java.util.List;
import java.util.Map;

public class PlanilhaImportadorMassivoDTO {

    private Map<String, List<String>> camposBase;
    private Map<String, List<CamposUdaPlanilha>> udasPorCategoria;


    public Map<String, List<String>> getCamposBase() {
        return camposBase;
    }

    public void setCamposBase(Map<String, List<String>> camposBase) {
        this.camposBase = camposBase;
    }

    public Map<String, List<CamposUdaPlanilha>> getUdasPorCategoria() {
        return udasPorCategoria;
    }

    public void setUdasPorCategoria(Map<String, List<CamposUdaPlanilha>> udasPorCategoria) {
        this.udasPorCategoria = udasPorCategoria;
    }

}
