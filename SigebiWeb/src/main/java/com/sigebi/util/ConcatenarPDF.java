package com.sigebi.util;

import java.io.FileOutputStream;
import java.io.IOException;

import com.lowagie.text.DocumentException;
import com.lowagie.text.pdf.PdfCopyFields;
import com.lowagie.text.pdf.PdfReader;

public class ConcatenarPDF {

    public  PdfReader unlockPdf(PdfReader reader) {
        if (reader == null) {
            return reader;
        }
        try {
            java.lang.reflect.Field f = reader.getClass().getDeclaredField("encrypted");
            f.setAccessible(true);
            f.set(reader, false);
        } catch (Exception e) { /* ignore */ }
        return reader;
    }

    public void copiar(String path) throws IOException, DocumentException {

        PdfReader reader1 = new PdfReader(path + "servicios_salud.pdf");
        unlockPdf(reader1);
        PdfReader reader2 = new PdfReader(path + "reporte_atenciones.pdf");
        unlockPdf(reader2);
        PdfReader reader3 = new PdfReader(path + "reporte_totales.pdf");
        unlockPdf(reader3);
        PdfCopyFields copy =
                new PdfCopyFields(new FileOutputStream(path + "reporte_general.pdf"));
        copy.addDocument(reader1);
        copy.addDocument(reader2);
        copy.addDocument(reader3);
        copy.close();

    }



}