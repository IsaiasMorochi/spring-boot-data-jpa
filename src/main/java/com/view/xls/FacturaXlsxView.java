package com.view.xls;


import com.models.entity.Factura;
import com.models.entity.ItemFactura;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.view.document.AbstractXlsxView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@Component("factura/ver.xlsx")
public class FacturaXlsxView extends AbstractXlsxView {

    @Override
    protected void buildExcelDocument(Map<String, Object> model, Workbook workbook, HttpServletRequest request, HttpServletResponse response) throws Exception {

        Factura factura = (Factura) model.get("factura");

        Sheet sheet = workbook.createSheet("Factura Spring"); //planilla

        Row row = sheet.createRow(0); //fila

        Cell cell = row.createCell(0); //celda

        cell.setCellValue("Datos del Cliente");

        row = sheet.createRow(1);
        cell = row.createCell(0);
        cell.setCellValue(factura.getCliente().getNombre() + " " + factura.getCliente().getApellido());

        row = sheet.createRow(2);
        cell = row.createCell(0);
        cell.setCellValue(factura.getCliente().getEmail());

        sheet.createRow(4).createCell(0).setCellValue("Datos de la Factura");
        sheet.createRow(5).createCell(0).setCellValue("Folio: " + factura.getId());
        sheet.createRow(6).createCell(0).setCellValue("Descripcion: " + factura.getDescripcion());
        sheet.createRow(7).createCell(0).setCellValue("Fecha: " + factura.getCreateAt());

        Row header = sheet.createRow(9);
        header.createCell(0).setCellValue("Producto");
        header.createCell(1).setCellValue("Precio");
        header.createCell(2).setCellValue("Cantidad");
        header.createCell(3).setCellValue("Total");

        int rownum = 10;
        for (ItemFactura item : factura.getItems()){
            Row fila = sheet.createRow(rownum ++);
            fila.createCell(0).setCellValue(item.getProducto().getNombre());
            fila.createCell(1).setCellValue(item.getProducto().getPrecio());
            fila.createCell(2).setCellValue(item.getCantidad());
            fila.createCell(3).setCellValue(item.calcularImporte());
        }

        Row filatotal = sheet.createRow(rownum);
        filatotal.createCell(2).setCellValue("Gran Total: ");
        filatotal.createCell(3).setCellValue(factura.getTotal());

    }
}
