package me.aboullaite.controller;

import me.aboullaite.model.CamposUdaPlanilha;
import me.aboullaite.model.PlanilhaImportadorMassivoDTO;
import me.aboullaite.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.*;

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

        List<String> listaBase1 = Arrays.asList("ID Item - utilizado para agrupar os skus");
        List<String> listaBase2 = Arrays.asList(
                "Nome Produto *",
                "Descrição Produto",
                "SKU Lojista",
                "SKU Via Varejo",
                "Estoque (Unidades) *",
                "EAN ou ISBN",
                "Marca");

        List<String> listaBase3 = Arrays.asList("Preço de Venda *");
        List<String> listaBase4 = Arrays.asList("Altura (cm) *", "Largura (cm) *", "Profundidade (cm) *", "Peso (Kg) *");
        List<String> listaBase5 = Arrays.asList("Tempo Compra / Fabricação", "Garantia");
        List<String> listaBase6 = Arrays.asList("url imagem 1 *", "url imagem 2", "url imagem 3", "url imagem 4");

        Map<String, List<String>> camposBase = new LinkedHashMap<>();
        camposBase.put("código agrupador de produtos", listaBase1);
        camposBase.put("", listaBase2);
        camposBase.put("preço", listaBase3);
        camposBase.put("dimensões", listaBase4);
        camposBase.put("dados da compra", listaBase5);
        camposBase.put("imagens", listaBase6);

        dto.setCamposBase(camposBase);

        CamposUdaPlanilha uda1 = new CamposUdaPlanilha();
        uda1.setNomeUda("Cor");
        uda1.setValoresUda(Arrays.asList("azul", "vermelho", "preto"));

        CamposUdaPlanilha uda2 = new CamposUdaPlanilha();
        uda2.setNomeUda("Tamanho");
        uda2.setValoresUda(Arrays.asList("p", "m", "g"));

        CamposUdaPlanilha uda3 = new CamposUdaPlanilha();
        uda3.setNomeUda("Armazenamento");
        uda3.setValoresUda(Arrays.asList("32", "64", "128"));

        CamposUdaPlanilha uda4 = new CamposUdaPlanilha();
        uda4.setNomeUda("Voltagem");
        uda4.setValoresUda(Arrays.asList("110", "220", "bivolt"));

        Map<String, List<CamposUdaPlanilha>> camposCat = new LinkedHashMap<>();
        camposCat.put("Roupas", Arrays.asList(uda1, uda2));
        camposCat.put("Eletronicos", Arrays.asList(uda3, uda4));

        dto.setUdasPorCategoria(camposCat);

        return dto;
    }

}
