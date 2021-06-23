package com.sigebi.util;

import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ImprimirFactura {
  void imprimir(){
      try
      {
          String carpeta = "/home/leticia/ticket";
          String archivo = "/home/leticia/ticket/#LPT1.txt";
          File folder = new File(carpeta);
          folder.mkdirs();
          File fichero = new File(archivo);
          BufferedWriter bw;
          FileWriter fw;
          if(!fichero.exists())
          {
              fichero.createNewFile();
              fw = new FileWriter(fichero);
              bw = new BufferedWriter(fw);
              bw.write("MOTEL OASIS"+"\r\n"
                      +"AV ESPINOZA 267, COL OBRERA\r\n"
                      +"ENSENADA, B.C.\r\n"
                      +"\r\n"
                      +"DIA EXTRA\r\n"

                      +"\r\n"
                      +"GRACIAS POR SU PREFERENCIA");
              bw.close();
          }
          else if(fichero.exists())
          {
              int R=0;
              String archivo2 = "/home/leticia/ticket/#LPT1.txt";
              String D2=null;
              FileReader f2 = null;
              try {
                  f2 = new FileReader(archivo2);
              } catch (FileNotFoundException ex) {
                  Logger.getLogger("").log(Level.SEVERE, null, ex);
              }
              BufferedReader b2 = new BufferedReader(f2);
              try {
                  D2=b2.readLine();
              } catch (IOException ex) {
                  Logger.getLogger("").log(Level.SEVERE, null, ex);
              }
              fw = new FileWriter(fichero);
              bw = new BufferedWriter(fw);
              bw.write("MOTEL OASIS"+"\r\n"
                      +"AV ESPINOZA 267, COL OBRERA\r\n"
                      +"ENSENADA, B.C.\r\n"
                      +"\r\n"
                      +"DIA EXTRA\r\n"
                      +"Fecha : \r\n"
                      +"\r\n"

                      +"\r\n"
                      +"GRACIAS POR SU PREFERENCIA");
              bw.close();
          }
      }
      catch (IOException ex)
      {
          Logger.getLogger("").log(Level.SEVERE, null, ex);
      }
  }
    public static void main (String args[]) {
        ImprimirFactura im = new ImprimirFactura();
        im.imprimir();
    }
}
