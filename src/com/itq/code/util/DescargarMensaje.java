package com.itq.code.util;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.JOptionPane;

public class DescargarMensaje {

	
	public void descargar(String mensajeCifrado, String nombreArchivo, char tipoProceso, int[][] clave) {
	    System.out.println("Tipo cifrado: " + tipoProceso);
	    if (tipoProceso == 'c') {
	        File archivo = new File(nombreArchivo + ".camo");

	        try (FileWriter escritor = new FileWriter(archivo)) {
	            // Escribir la matriz de clave en el archivo
	            for (int i = 0; i < clave.length; i++) {
	                for (int j = 0; j < clave[i].length; j++) {
	                    escritor.write(clave[i][j] + (j < clave[i].length - 1 ? "," : ""));
	                }
	                escritor.write("\n"); // Nueva línea después de cada fila
	            }

	            // Escribir el mensaje cifrado en el archivo
	            escritor.write(mensajeCifrado);
	            JOptionPane.showMessageDialog(null, "El archivo se ha descargado correctamente en: \n" + archivo.getAbsolutePath(), "Cifrado exitoso", JOptionPane.INFORMATION_MESSAGE);
	        } catch (IOException e) {
	            JOptionPane.showMessageDialog(null, "Ocurrió un error al descargar el archivo: \n" + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
	        }
	    } else if (tipoProceso == 'd') {
	        File archivo = new File(nombreArchivo + ".txt");

	        try (FileWriter escritor = new FileWriter(archivo)) {
	            // Escribir el mensaje cifrado en el archivo
	            escritor.write(mensajeCifrado);
	            JOptionPane.showMessageDialog(null, "El archivo se ha descargado correctamente en: \n" + archivo.getAbsolutePath(), "Descifrado exitoso", JOptionPane.INFORMATION_MESSAGE);
	        } catch (IOException e) {
	            JOptionPane.showMessageDialog(null, "Ocurrió un error al descargar el archivo: \n" + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
	        }
	    }
	}

}