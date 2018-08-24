package me.aboullaite.view;

import me.aboullaite.model.CamposUdaPlanilha;
import me.aboullaite.model.PlanilhaImportadorMassivoDTO;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.util.IOUtils;
import org.springframework.web.servlet.view.document.AbstractXlsxView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ExcelView extends AbstractXlsxView {

    @Override
    protected void buildExcelDocument(Map<String, Object> model,
                                      Workbook workbook,
                                      HttpServletRequest request,
                                      HttpServletResponse response) throws Exception {

        response.setHeader("Content-Disposition", "attachment; filename=\"planilha-importe-massivo.xlsx\"");
        PlanilhaImportadorMassivoDTO camposPlanilha = (PlanilhaImportadorMassivoDTO) model.get("importador-massivo");

        for (String key : camposPlanilha.getUdasPorCategoria().keySet()) {
            Sheet sheet = workbook.createSheet(key);
            montarBasePlanilha(sheet, camposPlanilha, key);
            montarCamposPlanilha(sheet, camposPlanilha, key);
        }

    }

    private void montarBasePlanilha(Sheet sheet, PlanilhaImportadorMassivoDTO planilha, String categoriaKey) {
        // montando o template
        int qtdBase = getTotalColunasCamposBase(planilha.getCamposBase());
        int qtdTotal = planilha.getUdasPorCategoria().get(categoriaKey).size() + qtdBase;
        montarTemplatePlanilha(sheet, qtdTotal);

        // Adicionando os campos base ao header
        for (String baseKey : planilha.getCamposBase().keySet())
            montarHeaderPlanilha(sheet, baseKey.toUpperCase(), planilha.getCamposBase().get(baseKey).size());

        // Adicionando os campos da categoria ao header
        montarHeaderPlanilha(sheet, categoriaKey.toUpperCase(), planilha.getUdasPorCategoria().get(categoriaKey).size());
    }

    private void montarTemplatePlanilha(Sheet sheet, int totalColunas) {
        try {
            // formatando as colunas da primeira linha para adicionar a imagem
            Row templateRow = sheet.createRow(0);
            templateRow.setHeight((short) 1600);
            sheet.setColumnWidth(0, 12000);
            sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, totalColunas));

            // adicionando a imagem do template
            Workbook workbook = sheet.getWorkbook();
            CreationHelper helper = workbook.getCreationHelper();
            ClientAnchor anchor = helper.createClientAnchor();
            anchor.setAnchorType(ClientAnchor.MOVE_AND_RESIZE);

            String imagePath = "/home/edneyroldao/Documents/viavarejo/sprint-11/tt.jpg";
            FileInputStream streamLogoViavarejo = new FileInputStream(imagePath);

            int picIndex = workbook.addPicture(IOUtils.toByteArray(streamLogoViavarejo), Workbook.PICTURE_TYPE_JPEG);
            anchor.setCol1(0);
            anchor.setRow1(0);

            Drawing drawing = sheet.createDrawingPatriarch();
            Picture pict = drawing.createPicture(anchor, picIndex);
            pict.resize();

            // Formatando texto do template
            Font font = workbook.createFont();
            CellStyle cellStyle = workbook.createCellStyle();
            Cell templateCell = templateRow.createCell(0);

            font.setBold(true);
            font.setFontHeightInPoints((short) 15);

            cellStyle.setFont(font);
            cellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);

            templateCell.setCellStyle(cellStyle);
            templateCell.setCellValue("                          Planilha de Importação massiva de produtos");

        } catch (Exception e) {
            System.out.println("Ocorreu um erro na montagem do template da planilha" + e.getMessage());
            e.printStackTrace();
        }
    }

    private void montarCamposPlanilha(Sheet sheet, PlanilhaImportadorMassivoDTO camposPlanilha, String categoriaKey) {
        int indexColumn = 0;

        Row row = sheet.createRow(2);
        row.setHeight((short) 400);

        for (String baseKey : camposPlanilha.getCamposBase().keySet()) {
            sheet.setColumnWidth(indexColumn, 12000);

            for (String base : camposPlanilha.getCamposBase().get(baseKey)) {
                row.createCell(indexColumn).setCellValue(base);
                indexColumn++;
            }
        }

        for (CamposUdaPlanilha udas : camposPlanilha.getUdasPorCategoria().get(categoriaKey)) {
            row.createCell(indexColumn).setCellValue(udas.getNomeUda());

            // todo - implementar aqui o drowpdown menu da tabela e os campos multivalorados

            indexColumn++;
        }
    }

    private void montarHeaderPlanilha(Sheet sheet, String textoHeader, int qtdMergeColunas) {
        int rowHeaderIndex = 1;
        Workbook workbook = sheet.getWorkbook();

        Row rowHeader = sheet.getRow(rowHeaderIndex);
        if(rowHeader == null) {
            rowHeader = sheet.createRow(rowHeaderIndex);
            rowHeader.setHeight((short) 800);
        }

        int cellIndex = rowHeader.getLastCellNum() == -1 ? 0 : rowHeader.getLastCellNum();
        Cell cellHeader = rowHeader.createCell(cellIndex);

        // ajustando o tamanho da coluna
        sheet.setColumnWidth(cellIndex, 12000);

        // aplicando o merge das colunas quando necessário
        if(qtdMergeColunas > 1) {

            int firstCol = cellIndex;
            int lastCol = cellIndex + qtdMergeColunas - 1;
            // todo - getLastColToMerge

            sheet.addMergedRegion(new CellRangeAddress(rowHeaderIndex, rowHeaderIndex, firstCol, lastCol));
        }

        Font font = workbook.createFont();
        CellStyle cellStyle = workbook.createCellStyle();

        // formatando a font do texto
        font.setBold(true);
        font.setFontHeightInPoints((short) 15);
        font.setColor(HSSFColor.WHITE.index);
        cellStyle.setFont(font);

        // Ajustando a posicao do texto na coluna
        cellStyle.setFillPattern(CellStyle.BIG_SPOTS);
        cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        cellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        cellStyle.setFillBackgroundColor((short) (cellIndex % 2 == 0 ? 17 : 7)); // verdeEscuro = 17 e verdeClaro = 7

        cellHeader.setCellStyle(cellStyle);
        cellHeader.setCellValue(textoHeader);
    }

    private int getTotalColunasCamposBase(Map<String, List<String>> mapaCamposBase) {
        Set<String> keys = mapaCamposBase.keySet();
        int total = 0;

        for(String key : keys) {
            total += mapaCamposBase.get(key).size();
        }

        return total;
    }

}
