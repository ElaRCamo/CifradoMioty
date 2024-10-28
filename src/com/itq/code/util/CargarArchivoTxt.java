package com.itq.code.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import javax.swing.*;

public class CargarArchivoTxt {

    // Método para abrir el cuadro de diálogo de selección de archivos
    public static Object[] cargarArchivo(JFrame parent) {
        JFileChooser fileChooser = new JFileChooser();
        int result = fileChooser.showOpenDialog(parent);

        if (result == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            return leerArchivo(file);  // Devolver el contenido del archivo
        }
        return null;
    }

    // Método que lee el contenido del archivo seleccionado y separa clave y contenido
    public static Object[] leerArchivo(File file) {
        StringBuilder contenidoBuilder = new StringBuilder();
        int[][] clave = null;
        int rowCount = 0; // Contador de filas de la clave
        boolean isKeySection = true; // Bandera para identificar la sección de la clave

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) {
                    continue; // Saltar líneas vacías
                }

                // Si encontramos una línea que no contiene solo dígitos y comas, asumimos que es contenido
                if (isKeySection && !line.matches("[0-9,]+")) {
                    isKeySection = false; // Cambiar a sección de contenido
                }

                if (isKeySection) {
                    // Procesar la clave
                    String[] valores = line.split(","); // Separar por comas
                    if (clave == null) {
                        // Inicializar la matriz con un número máximo esperado de columnas
                        clave = new int[5][valores.length]; // Cambia 5 por el número esperado de filas de la clave
                    }
                    // Agregar los valores a la matriz
                    for (int i = 0; i < valores.length; i++) {
                        clave[rowCount][i] = Integer.parseInt(valores[i]); // Convertir a int
                    }
                    rowCount++;
                } else {
                    // Agregar al contenido
                    contenidoBuilder.append(line).append("\n"); // Agregar al contenido
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error al cargar el archivo", "Error", JOptionPane.ERROR_MESSAGE);
        }

        // Ajustar el tamaño de la clave a la cantidad de filas leídas
        if (rowCount > 0) {
            int[][] claveFinal = new int[rowCount][clave[0].length];
            for (int i = 0; i < rowCount; i++) {
                claveFinal[i] = clave[i];
            }
            clave = claveFinal;
        } else {
            clave = new int[0][0]; // Asignar una matriz vacía si no se leyó ninguna clave
        }

        return new Object[]{clave, contenidoBuilder.toString().trim()}; // Devolver clave y contenido
    }
}