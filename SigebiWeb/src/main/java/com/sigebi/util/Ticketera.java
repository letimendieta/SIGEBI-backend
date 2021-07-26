package com.sigebi.util;

import com.sigebi.util.exceptions.SigebiException;

public class Ticketera {
    public static void main (String args[]) throws SigebiException {
        //Aca llenamos los articulos, sustituyelo por lo de tu eleccion
        String items = "2   Articulo Prueba   15.00\n"+
                "7   Articulo Tar" +
                "a tara   25.00\n"+
                "4   Super articulo   55.39";
        String store = "Picharras Ltd.";
        String venue = "Molas, Yuc.";
        String date = "01/enero/2012";
        String caissier = "Josue Camara";
        Ticket ticket = new Ticket(store, venue, "5", "99", caissier, date, items, "100.00", "16.00", "116.00", "150", "34");
   ticket.print();

    }
}
