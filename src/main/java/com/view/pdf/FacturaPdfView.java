package com.view.pdf;


import com.lowagie.text.Document;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.models.entity.Factura;
import com.models.entity.ItemFactura;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.view.document.AbstractPdfView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.util.Locale;
import java.util.Map;

/**
 * @author imorochi
 * @apiNote https://itextpdf.com/es/resources/examples
 *
 */
@Component("factura/ver")
public class FacturaPdfView extends AbstractPdfView {

    // 1era forma
    @Autowired private MessageSource messageSource;
    @Autowired private LocaleResolver localeResolver;


    @Override
    protected void buildPdfDocument(Map<String, Object> map, Document document, PdfWriter pdfWriter,
                                    HttpServletRequest request, HttpServletResponse response) throws Exception {

        Factura factura = (Factura) map.get("factura");

        // 1era forma obtener el locale y messageSource
        Locale locale = localeResolver.resolveLocale(request);

        // 2da forma obtener el locale y messageSource
        MessageSourceAccessor mensajes = getMessageSourceAccessor();

        // crea una celda
        PdfPCell cell = null;

        // crea una tabla
        PdfPTable tabla1 = new PdfPTable(1);
        tabla1.setSpacingAfter(20);
        cell = new PdfPCell(new Phrase(messageSource.getMessage("text.factura.ver.datos.cliente", null, locale)));
        cell.setBackgroundColor(new Color(184, 218, 255));
        cell.setPadding(8f);
        tabla1.addCell(cell);
        tabla1.addCell(factura.getCliente().getNombre() + " " + factura.getCliente().getApellido());
        tabla1.addCell(factura.getCliente().getEmail());


        PdfPTable tabla2 = new PdfPTable(1);
        tabla2.setSpacingAfter(20);
        cell = new PdfPCell(new Phrase(messageSource.getMessage("text.factura.ver.datos.factura", null, locale)));
        cell.setBackgroundColor(new Color(195, 230, 203));
        cell.setPadding(8f);
        tabla2.addCell(cell);
        tabla2.addCell(mensajes.getMessage("text.cliente.factura.folio") + ": " + factura.getId());
        tabla2.addCell(mensajes.getMessage("text.cliente.factura.descripcion") + ": " + factura.getDescripcion());
        tabla2.addCell(mensajes.getMessage("text.cliente.factura.fecha") + ": " + factura.getCreateAt());

        document.add(tabla1);
        document.add(tabla2);

        PdfPTable tabla3 = new PdfPTable(4);
        tabla3.setWidths(new float[] {3.5f, 1, 1, 1});
        tabla3.addCell(mensajes.getMessage("text.factura.form.item.nombre"));
        tabla3.addCell(mensajes.getMessage("text.factura.form.item.precio"));
        tabla3.addCell(mensajes.getMessage("text.factura.form.item.cantidad"));
        tabla3.addCell(mensajes.getMessage("text.factura.form.item.total"));

        for (ItemFactura item: factura.getItems()){
            tabla3.addCell(item.getProducto().getNombre());
            tabla3.addCell(item.getProducto().getPrecio().toString());

            // tabla3.addCell(item.getCantidad().toString()); //otro formato
            cell = new PdfPCell(new Phrase((item.getCantidad().toString())));
            cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
            tabla3.addCell(cell);

            tabla3.addCell(item.calcularImporte().toString());
        }
        cell = new PdfPCell(new Phrase( mensajes.getMessage("text.factura.form.total")));
        cell.setColspan(3); //ocupe 3 columnas
        cell.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
        tabla3.addCell(cell);
        tabla3.addCell(factura.getTotal().toString());

        document.add(tabla3);
    }
}
