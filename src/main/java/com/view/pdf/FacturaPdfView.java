package com.view.pdf;


import com.lowagie.text.Document;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfTable;
import com.lowagie.text.pdf.PdfWriter;
import com.models.entity.Factura;
import com.models.entity.ItemFactura;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.view.document.AbstractPdfView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@Component("factura/ver")
public class FacturaPdfView extends AbstractPdfView {


    @Override
    protected void buildPdfDocument(Map<String, Object> map, Document document, PdfWriter pdfWriter,
                                    HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws Exception {

        Factura factura = (Factura) map.get("factura");

        PdfPTable tabla1 = new PdfPTable(1);
        tabla1.setSpacingAfter(20);
        tabla1.addCell("Datos del cliente");
        tabla1.addCell(factura.getCliente().getNombre() + " " + factura.getCliente().getApellido());
        tabla1.addCell(factura.getCliente().getEmail());

        PdfPTable tabla2 = new PdfPTable(1);
        tabla2.setSpacingAfter(20);
        tabla2.addCell("Datos de la Factura");
        tabla2.addCell("Folio: " + factura.getId());
        tabla2.addCell("Descripcion: " + factura.getDescripcion());
        tabla2.addCell("Fecha: " + factura.getCreateAt());

        document.add(tabla1);
        document.add(tabla2);

        PdfPTable tabla3 = new PdfPTable(4);
        tabla3.addCell("Producto");
        tabla3.addCell("Precio");
        tabla3.addCell("Cantidad");
        tabla3.addCell("Total");

        for (ItemFactura item: factura.getItems()){
            tabla3.addCell(item.getProducto().getNombre());
            tabla3.addCell(item.getProducto().getPrecio().toString());
            tabla3.addCell(item.getCantidad().toString());
            tabla3.addCell(item.calcularImporte().toString());
        }

        PdfPCell cell = new PdfPCell(new Phrase("Total: "));
        cell.setColspan(3); //ocupe 3 columnas
        cell.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
        tabla3.addCell(cell);
        tabla3.addCell(factura.getTotal().toString());

        document.add(tabla3);
    }
}
