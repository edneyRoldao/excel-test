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
import java.util.Collection;
import java.util.Map;

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
            montarTemplatePlanilha(sheet, camposPlanilha, key);
            montarHeaderPlanilha(sheet, camposPlanilha, key);
            montarCamposPlanilha(sheet, camposPlanilha, key);
        }

    }

    private void montarTemplatePlanilha(Sheet sheet, PlanilhaImportadorMassivoDTO planilha, String categoriaKey) {
        try {
            // formatando as colunas da primeira linha para adicionar a imagem
            Row templateRow = sheet.createRow(0);
            templateRow.setHeight((short) 1600);

            sheet.setColumnWidth(0, 12000);

            // calculando a quantidade de campos da sheet e fazendo o merge delas
            int qtdCamposBase = planilha.getCamposBase().values().stream().mapToInt(Collection::size).sum();
            int qtdCamposTotal = planilha.getUdasPorCategoria().get(categoriaKey).size() + qtdCamposBase - 1;
            sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, qtdCamposTotal));

            // adicionando a imagem com o logo viavarejo ao template
            Workbook workbook = sheet.getWorkbook();
            CreationHelper helper = workbook.getCreationHelper();
            ClientAnchor anchor = helper.createClientAnchor();
            anchor.setAnchorType(ClientAnchor.MOVE_AND_RESIZE);

            // todo - colocar o caminha de imagem em um properties
            String imagePath = "/Users/edneyroldao/Workspace/java/projetosDeEstudo/excel-test/tt.jpg";
            FileInputStream streamLogoViavarejo = new FileInputStream(imagePath);

            int picIndex = workbook.addPicture(IOUtils.toByteArray(streamLogoViavarejo), Workbook.PICTURE_TYPE_JPEG);
            anchor.setCol1(0);
            anchor.setRow1(0);

            Drawing drawing = sheet.createDrawingPatriarch();
            Picture pict = drawing.createPicture(anchor, picIndex);
            pict.resize();

            // Formatando titulo do template
            Font font = workbook.createFont();
            CellStyle cellStyle = workbook.createCellStyle();
            Cell templateCell = templateRow.createCell(0);

            font.setBold(true);
            font.setFontHeightInPoints((short) 22);

            cellStyle.setFont(font);
            cellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);

            templateCell.setCellStyle(cellStyle);
            templateCell.setCellValue("                                   Planilha de Importação massiva de produtos");

        } catch (Exception e) {
            System.out.println("Ocorreu um erro na montagem do template da planilha" + e.getMessage());
            e.printStackTrace();
        }
    }

    private void montarCamposPlanilha(Sheet sheet, PlanilhaImportadorMassivoDTO planilha, String categoriaKey) {
        boolean trocaCorFundo = false;
        int indexColumn = 0;

        Row row = sheet.createRow(2);
        row.setHeight((short) 800);

        for (String baseKey : planilha.getCamposBase().keySet()) {

            for (String base : planilha.getCamposBase().get(baseKey)) {
                Cell cell = row.createCell(indexColumn);
                formatarCell(sheet.getWorkbook(), cell, trocaCorFundo);
                cell.setCellValue(base);
                indexColumn++;
            }

            trocaCorFundo = !trocaCorFundo;
        }

        for (CamposUdaPlanilha udas : planilha.getUdasPorCategoria().get(categoriaKey)) {
            Cell cell = row.createCell(indexColumn);
            formatarCell(sheet.getWorkbook(), cell, trocaCorFundo);
            cell.setCellValue(udas.getNomeUda());

            // todo - implementar aqui o drowpdown menu da tabela e os campos multivalorados

            indexColumn++;
        }
    }

    private void montarHeaderPlanilha(Sheet sheet, PlanilhaImportadorMassivoDTO planilha, String categoriaKey) {
        int rowNumber = 1;
        Row rowHeader = sheet.createRow(rowNumber);
        rowHeader.setHeight((short) 800);

        int cellIndex = 0;
        int alternaCor = 0;

        for (String baseKey : planilha.getCamposBase().keySet()) {
            Cell cell = rowHeader.createCell(cellIndex);
            formatarCellHeader(sheet.getWorkbook(), cell, alternaCor);

            int qtdCamposBase = planilha.getCamposBase().get(baseKey).size();

            // aplicando o merge das colunas quando necessário
            if(qtdCamposBase > 1) {
                int lastCol = qtdCamposBase + cellIndex - 1;
                sheet.addMergedRegion(new CellRangeAddress(rowNumber, rowNumber, cellIndex, lastCol));
                cellIndex = lastCol;
            }

            cellIndex++;
            alternaCor++;
            cell.setCellValue(baseKey.toUpperCase());
        }

        // Tratando cellHeader para os campos da categoria
        Cell cell = rowHeader.createCell(cellIndex);
        formatarCellHeader(sheet.getWorkbook(), cell, alternaCor);

        int qtdCamposCategoria = planilha.getUdasPorCategoria().get(categoriaKey).size();

        if(qtdCamposCategoria > 1) {
            int lastCol = qtdCamposCategoria + cellIndex - 1;
            sheet.addMergedRegion(new CellRangeAddress(rowNumber, rowNumber, cellIndex, lastCol));
        }

        cell.setCellValue(categoriaKey.toUpperCase());

        // ajustando o tamanho da coluna
        for (int i = 0; i <= cellIndex + 1; i++) {
            sheet.setColumnWidth(i, 12000);
        }
    }

    private void formatarCellHeader(Workbook workbook, Cell cell, int alternaCor) {
        Font font = workbook.createFont();
        CellStyle cellStyle = workbook.createCellStyle();

        // formatando a font do texto
        font.setBold(true);
        font.setFontHeightInPoints((short) 15);
        font.setColor(HSSFColor.WHITE.index);
        cellStyle.setFont(font);

        // Ajustando a posicao do texto na coluna
        cellStyle.setBorderTop(BorderStyle.THIN);
        cellStyle.setBorderLeft(BorderStyle.THIN);
        cellStyle.setBorderRight(BorderStyle.THIN);
        cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        cellStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
        cellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        cellStyle.setFillForegroundColor((short) (alternaCor % 2 == 0 ? 17 : 3));

        cell.setCellStyle(cellStyle);
    }

    private void formatarCell(Workbook workbook, Cell cell, boolean trocaCorFundo) {
        CellStyle cellStyle = workbook.createCellStyle();

        cellStyle.setBorderTop(BorderStyle.THIN);
        cellStyle.setBorderLeft(BorderStyle.THIN);
        cellStyle.setBorderRight(BorderStyle.THIN);
        cellStyle.setBorderBottom(BorderStyle.THIN);
        cellStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
        cellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);

        if(trocaCorFundo)
            cellStyle.setFillForegroundColor((short) 47);
        else
            cellStyle.setFillForegroundColor((short) 1);

        cell.setCellStyle(cellStyle);
    }

}
