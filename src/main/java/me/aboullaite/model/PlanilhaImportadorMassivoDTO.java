package me.aboullaite.model;

import java.util.List;

public class PlanilhaImportadorMassivoDTO {

    private List<CamposBasePlanilha> camposBase;
    private List<CamposCategoriaPlanilha> camposCategorias;

    public List<CamposBasePlanilha> getCamposBase() {
        return camposBase;
    }

    public void setCamposBase(List<CamposBasePlanilha> camposBase) {
        this.camposBase = camposBase;
    }

    public List<CamposCategoriaPlanilha> getCamposCategorias() {
        return camposCategorias;
    }

    public void setCamposCategorias(List<CamposCategoriaPlanilha> camposCategorias) {
        this.camposCategorias = camposCategorias;
    }
}
