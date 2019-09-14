package com.view.pdf;


import com.lowagie.text.Document;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.models.entity.Factura;
import com.models.entity.ItemFactura;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.view.document.AbstractPdfView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.util.Map;

/**
 * @author imorochi
 * @apiNote https://itextpdf.com/es/resources/examples
 *
 */
@Component("factura/ver")
public class FacturaPdfView extends AbstractPdfView {



    @Override
    protected void buildPdfDocument(Map<String, Object> map, Document document, PdfWriter pdfWriter,
                                    HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws Exception {

        Factura factura = (Factura) map.get("factura");

        // crea una celda
        PdfPCell cell = null;

        // crea una tabla
        PdfPTable tabla1 = new PdfPTable(1);
        tabla1.setSpacingAfter(20);
        cell = new PdfPCell(new Phrase("Datos del cliente"));
        cell.setBackgroundColor(new Color(184, 218, 255));
        cell.setPadding(8f);
        tabla1.addCell(cell);
        tabla1.addCell(factura.getCliente().getNombre() + " " + factura.getCliente().getApellido());
        tabla1.addCell(factura.getCliente().getEmail());


        PdfPTable tabla2 = new PdfPTable(1);
        tabla2.setSpacingAfter(20);
        cell = new PdfPCell(new Phrase("Datos de la Factura"));
        cell.setBackgroundColor(new Color(195, 230, 203));
        cell.setPadding(8f);
        tabla2.addCell(cell);
        tabla2.addCell("Folio: " + factura.getId());
        tabla2.addCell("Descripcion: " + factura.getDescripcion());
        tabla2.addCell("Fecha: " + factura.getCreateAt());

        document.add(tabla1);
        document.add(tabla2);

        PdfPTable tabla3 = new PdfPTable(4);
        tabla3.setWidths(new float[] {3.5f, 1, 1, 1});
        tabla3.addCell("Producto");
        tabla3.addCell("Precio");
        tabla3.addCell("Cantidad");
        tabla3.addCell("Total");

        for (ItemFactura item: factura.getItems()){
            tabla3.addCell(item.getProducto().getNombre());
            tabla3.addCell(item.getProducto().getPrecio().toString());

            cell = new PdfPCell(new Phrase((item.getCantidad().toString())));
            cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
            tabla3.addCell(cell);
            tabla3.addCell(item.getCantidad().toString());
            tabla3.addCell(item.calcularImporte().toString());
        }
        cell = new PdfPCell(new Phrase("Total: "));
        cell.setColspan(3); //ocupe 3 columnas
        cell.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
        tabla3.addCell(cell);
        tabla3.addCell(factura.getTotal().toString());

        document.add(tabla3);
    }
}
