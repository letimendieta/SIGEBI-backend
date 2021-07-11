package com.sigebi.util;

import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.sigebi.util.exceptions.SigebiException;
import com.sigebi.util.exceptions.SigebiException.InternalServerError;

public class ImprimirFactura {
  void imprimir() throws InternalServerError{
      try
      {
          String carpeta = "/home/leticia/ticket";
          String archivo = "/home/leticia/ticket/#LPT1.txt";
          File folder = new File(carpeta);
          folder.mkdirs();
          File fichero = new File(archivo);
          BufferedWriter bw = null;
          FileWriter fw = null;
          if(!fichero.exists())
          {              
              try {
            	  
            	  if (fichero.createNewFile())
                      System.out.println("File created");
                  else
                      System.out.println("File already exists");
            	  
            	  fw = new FileWriter(fichero);
	              bw = new BufferedWriter(fw);
	              bw.write("MOTEL OASIS"+"\r\n"
	                      +"AV ESPINOZA 267, COL OBRERA\r\n"
	                      +"ENSENADA, B.C.\r\n"
	                      +"\r\n"
	                      +"DIA EXTRA\r\n"
	
	                      +"\r\n"
	                      +"GRACIAS POR SU PREFERENCIA");	
	          	} catch (Exception e) {
	          		throw new SigebiException.InternalServerError(e.getMessage());
				} finally {
					if (fw != null) fw.close();
					if (bw != null) bw.close();
				}	              
          }
          else if(fichero.exists()) {
              int R=0;
              String archivo2 = "/home/leticia/ticket/#LPT1.txt";
              String D2=null;
              FileReader f2 = null;
              BufferedReader b2 = null;
              try {
                  f2 = new FileReader(archivo2);
                  b2 = new BufferedReader(f2);              
                  D2=b2.readLine();
              } catch (IOException ex) {
                  Logger.getLogger("").log(Level.SEVERE, null, ex);
              }finally {
            	  if( f2 != null) f2.close();
            	  if( b2 != null) b2.close();
              }
              
              try {
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
              }catch (Exception e) {
            	  throw new SigebiException.InternalServerError(e.getMessage());
              }finally {
            	  if (fw != null) fw.close();
            	  if (bw != null) bw.close();
              }
              
          }
      }
      catch (IOException ex)
      {
          Logger.getLogger("").log(Level.SEVERE, null, ex);
      }
  }
    public static void main (String args[]) throws InternalServerError {
        ImprimirFactura im = new ImprimirFactura();
        im.imprimir();
    }
}
