package me.aboullaite.controller;

import me.aboullaite.model.CamposBasePlanilha;
import me.aboullaite.model.CamposCategoriaPlanilha;
import me.aboullaite.model.CamposUdaPlanilha;
import me.aboullaite.model.PlanilhaImportadorMassivoDTO;
import me.aboullaite.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Arrays;
import java.util.List;

@Controller
public class ExportController {

    @Autowired
    private UserService userService;

    /**
     * Handle request to download an Excel document
     */
    @GetMapping("/download")
    public void download(Model model) {
        model.addAttribute("importador-massivo", build());
    }

    @SuppressWarnings("unchecked")
    private PlanilhaImportadorMassivoDTO build() {
        PlanilhaImportadorMassivoDTO dto  = new PlanilhaImportadorMassivoDTO();

        List<String> valoresUda1 = Arrays.asList("azul", "verde", "amarelo");
        List<String> valoresUda2 = Arrays.asList("pequeno", "medio", "grande");
        List<String> valoresUda3 = Arrays.asList("azul1", "verde1", "amarelo1");
        List<String> valoresUda4 = Arrays.asList("pequeno1", "medio1", "grande1");

        List<CamposUdaPlanilha> udas1 = Arrays.asList(new CamposUdaPlanilha("cor", valoresUda1), new CamposUdaPlanilha("tamanho", valoresUda2));
        List<CamposUdaPlanilha> udas2 = Arrays.asList(new CamposUdaPlanilha("cor1", valoresUda3), new CamposUdaPlanilha("tamanho1", valoresUda4));

        List<CamposCategoriaPlanilha> categorias = Arrays.asList(new CamposCategoriaPlanilha("eletro", udas1), new CamposCategoriaPlanilha("alimentos", udas2));

        List<CamposBasePlanilha> base = Arrays.asList(new CamposBasePlanilha("produto"),
                                                      new CamposBasePlanilha("titulo"),
                                                      new CamposBasePlanilha("descricao"),
                                                      new CamposBasePlanilha("preco"));

        dto.setCamposBase(base);
        dto.setCamposCategorias(categorias);

        return dto;
    }

}
