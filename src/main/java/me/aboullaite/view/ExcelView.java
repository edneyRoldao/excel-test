package me.aboullaite.view;

import me.aboullaite.model.CamposBasePlanilha;
import me.aboullaite.model.CamposCategoriaPlanilha;
import me.aboullaite.model.CamposUdaPlanilha;
import me.aboullaite.model.PlanilhaImportadorMassivoDTO;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.RegionUtil;
import org.apache.poi.util.IOUtils;
import org.springframework.web.servlet.view.document.AbstractXlsxView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.util.Map;

public class ExcelView extends AbstractXlsxView {

    @Override
    protected void buildExcelDocument(Map<String, Object> model,
                                      Workbook workbook,
                                      HttpServletRequest request,
                                      HttpServletResponse response) throws Exception {

        response.setHeader("Content-Disposition", "attachment; filename=\"planilha-importe-massivo.xlsx\"");

        PlanilhaImportadorMassivoDTO camposPlanilha = (PlanilhaImportadorMassivoDTO) model.get("importador-massivo");


        Sheet s = workbook.createSheet("teste");
        Row r = s.createRow(0);
        r.setHeight((short) 1600);

        s.setColumnWidth(0, 12000);
        s.addMergedRegion(new CellRangeAddress(0, 0, 0, 40));

        addImage(workbook, s);

        Cell c1 = r.createCell(0);
        c1.setCellValue("                          Planilha de Importação massiva de produtos");

        CellStyle cs1 = workbook.createCellStyle();
        Font font1 = workbook.createFont();
        font1.setBold(true);
        font1.setFontHeightInPoints((short) 15);

        cs1.setFont(font1);
//        cs1.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        cs1.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);

        c1.setCellStyle(cs1);




        Row r2 = s.createRow(1);
        r2.setHeight((short) 800);
        Cell c = r2.createCell(0);
        c.setCellValue("CÓDIGO AGRUPADOR DE PRODUTOS");

        CellStyle cs = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        font.setFontHeightInPoints((short) 15);
        font.setColor(HSSFColor.WHITE.index);

        cs.setFont(font);
        cs.setFillBackgroundColor(HSSFColor.GREEN.index);
        cs.setFillPattern(CellStyle.BIG_SPOTS);
        cs.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        cs.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);

        c.setCellStyle(cs);

//        for(CamposCategoriaPlanilha ccp : camposPlanilha.getCamposCategorias()) {
//            int columnNumber = 0;
//            Sheet sheet = workbook.createSheet(ccp.getNomeCategoria());
//            Row headerRow = sheet.createRow(0);
//
//            for(CamposBasePlanilha cbp : camposPlanilha.getCamposBase())
//                headerRow.createCell(columnNumber++).setCellValue(cbp.getNomeCampo());
//
//            for(CamposUdaPlanilha cup : ccp.getUdas())
//                headerRow.createCell(columnNumber++).setCellValue(cup.getNomeUda());
//
//        }


    }

    private void addImage(Workbook workbook, Sheet sheet) throws Exception {
        FileInputStream stream = new FileInputStream("/home/edneyroldao/Documents/viavarejo/sprint-11/tt.jpg");
        CreationHelper helper = workbook.getCreationHelper();
        Drawing drawing = sheet.createDrawingPatriarch();
        ClientAnchor anchor = helper.createClientAnchor();
        anchor.setAnchorType(ClientAnchor.MOVE_AND_RESIZE);

        int picIndex = workbook.addPicture(IOUtils.toByteArray(stream), Workbook.PICTURE_TYPE_JPEG);
        anchor.setCol1(0);
        anchor.setRow1(0);

        Picture pict = drawing.createPicture(anchor, picIndex);
        pict.resize();
    }

}
