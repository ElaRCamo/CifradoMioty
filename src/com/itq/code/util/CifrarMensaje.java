package com.itq.code.util;


import javax.swing.JOptionPane;

public class CifrarMensaje {

    // Método inicial que retorna un array de String
    public static String[] procesarMensaje(String mensaje, char cifrado, String nipUser) {

        if (cifrado == 'c') {
        	// Mensaje invertido
            String mensajeInvertido = invertirMensaje(mensaje);
            // Generar NIP
            String nipString = generarNIP();
            int nip = Integer.parseInt(nipString);
            System.out.println("NIP: " + nip);
            String mensajeCifrado = cifrarMensaje(mensajeInvertido, nip);
            System.out.println("Mensaje cifrado: " + mensajeCifrado);
            
            return new String[]{nipString, mensajeCifrado};

        } else if (cifrado == 'd') {
            
            String mensajeDescifrado = "";
            String mensajeInvertido = "";
            if (nipUser != "") {
            	
            	  try {
            		  int nipInt = Integer.parseInt(nipUser);
            		  mensajeDescifrado = descifrarMensaje(mensaje, nipInt);
                      mensajeInvertido = invertirMensaje(mensajeDescifrado);
                      
                      System.out.println("Mensaje descifrado: " + mensajeInvertido);
            	    } catch (NumberFormatException e) {
            	    	
            	    	JOptionPane.showMessageDialog(null, "Ingrese un nip valido", "Error", JOptionPane.ERROR_MESSAGE);
            	    }
            }else {
            	JOptionPane.showMessageDialog(null, "Ingrese un nip valido", "Error", JOptionPane.ERROR_MESSAGE);
            }
            
            return new String[]{nipUser, mensajeInvertido};
        }

        return new String[]{"", mensaje};
    }

    // Método que invierte el mensaje
    public static String invertirMensaje(String mensaje) {
        StringBuilder sb = new StringBuilder(mensaje);
        return sb.reverse().toString();
    }
    
    // Método que genera la matriz base de caracteres imprimibles
    public static char[][] lenguajeOriginal() {
        char[][] matrizAscii = new char[12][12];
        int asciiValue = 32; // Primer carácter imprimible
        int startAscii = 32; // Primer carácter imprimible
        int endAscii = 126; // Último carácter imprimible

        // Llenar la matriz con los caracteres imprimibles del 32 al 126
        for (int i = 0; i < 12; i++) {
            for (int j = 0; j < 12; j++) {
                matrizAscii[i][j] = (char) asciiValue;
                asciiValue++;
                // Reiniciar el valor ASCII si se supera el rango de 126
                if (asciiValue > endAscii) {
                    asciiValue = startAscii;
                }
            }
        }

        return matrizAscii;
    }
    
    public static char[][] matrizNip(int nipUser) {
        char[][] matrizAscii = new char[12][12];
        int asciiValue = 32; // Primer carácter imprimible
        int startAscii = 32; // Primer carácter imprimible
        int endAscii = 126; // Último carácter imprimible
        int rango = endAscii - startAscii + 1; // Cantidad de caracteres imprimibles

        // Ajustar el valor inicial según el nipUser
        asciiValue = startAscii + (nipUser % rango);

        // Llenar la matriz con los caracteres imprimibles desplazados según el nipUser
        for (int i = 0; i < 12; i++) {
            for (int j = 0; j < 12; j++) {
                matrizAscii[i][j] = (char) asciiValue;
                asciiValue++;
                // Reiniciar el valor ASCII si se supera el rango de 126
                if (asciiValue > endAscii) {
                    asciiValue = startAscii;
                }
            }
        }

        return matrizAscii;
    }
    
    public static String cifrarMensaje(String mensaje, int nipUser) {
        char[][] matrizNip = matrizNip(nipUser); // Obtener la matriz NIP
        char[][] matrizOriginal = lenguajeOriginal(); // Obtener la matriz base
        int[][] ubicaciones = obtenerUbicaciones(mensaje, matrizOriginal); // Obtener las ubicaciones en la matriz original

        // Cifrar el mensaje
        StringBuilder mensajeCifrado = new StringBuilder();
        for (int index = 0; index < mensaje.length(); index++) {
            char c = mensaje.charAt(index);
            int[] ubicacion = ubicaciones[index];

            // Verificar si el carácter fue encontrado en la matriz original
            if (ubicacion[0] != -1 && ubicacion[1] != -1) {
                mensajeCifrado.append(matrizNip[ubicacion[0]][ubicacion[1]]);
            } else {
                // Si el carácter no está en la matriz, agregarlo sin cambios
                mensajeCifrado.append(c);
            }
        }
        
        
        System.out.println("\nMatriz original:");
        imprimirMatriz(matrizOriginal);
        
        System.out.println("\nMatriz generada con nip:");
        imprimirMatriz(matrizNip);

        return mensajeCifrado.toString();
    }
    
    public static String descifrarMensaje(String mensajeCifrado, int nipUser) {
        char[][] matrizNip = matrizNip(nipUser); // Obtener la matriz NIP
        char[][] matrizOriginal = lenguajeOriginal(); // Obtener la matriz base
        int[][] ubicaciones = obtenerUbicaciones(mensajeCifrado, matrizNip);// Obtener las ubicaciones en la matriz nip

        /// Descifrar el mensaje
        StringBuilder mensajeDescifrado = new StringBuilder();
        for (int index = 0; index < mensajeCifrado.length(); index++) {
            char c = mensajeCifrado.charAt(index);
            int[] ubicacion = ubicaciones[index];

            // Verificar si el carácter fue encontrado en la matriz nip
            if (ubicacion[0] != -1 && ubicacion[1] != -1) {
            	mensajeDescifrado.append(matrizOriginal[ubicacion[0]][ubicacion[1]]);
            } else {
                // Si el carácter no está en la matriz, agregarlo sin cambios
            	mensajeDescifrado.append(c);
            }
        }
        
        
        System.out.println("\nMatriz original:");
        imprimirMatriz(matrizOriginal);
        
        System.out.println("\nMatriz generada con nip:");
        imprimirMatriz(matrizNip);

        return mensajeDescifrado.toString();
    }



    // Método que imprime una matriz de caracteres
    public static void imprimirMatriz(char[][] matriz) {
        for (int i = 0; i < 12; i++) {
            for (int j = 0; j < 12; j++) {
                System.out.print(matriz[i][j] + " ");
            }
            System.out.println();
        }
    }

    // Generar un NIP de 4 dígitos
    public static String generarNIP() {
        StringBuilder nip = new StringBuilder(4);

        // Generar 4 dígitos aleatorios
        for (int i = 0; i < 4; i++) {
            int digitoAleatorio = (int) (Math.random() * 10); // Genera un número entre 0 y 9
            nip.append(digitoAleatorio);
        }

        return nip.toString();
    }
    
    public static int[][] obtenerUbicaciones(String mensaje, char[][] matriz) {
        int[][] ubicaciones = new int[mensaje.length()][2];

        for (int i = 0; i < mensaje.length(); i++) {
            char c = mensaje.charAt(i);
            boolean encontrado = false;

            // Buscar la posición del carácter en la matriz
            for (int fila = 0; fila < matriz.length && !encontrado; fila++) {
                for (int col = 0; col < matriz[fila].length && !encontrado; col++) {
                    if (matriz[fila][col] == c) {
                        ubicaciones[i][0] = fila;
                        ubicaciones[i][1] = col;
                        encontrado = true;
                    }
                }
            }

            // Si no se encuentra el carácter, asignar ubicaciones no válidas
            if (!encontrado) {
                ubicaciones[i][0] = 0;
                ubicaciones[i][1] = 0;
            }

           /* // Imprimir la ubicación del carácter
            if (encontrado) {
                System.out.println("Carácter: '" + c + "' encontrado en la posición [" + ubicaciones[i][0] + "][" + ubicaciones[i][1] + "]");
            } else {
                System.out.println("Carácter: '" + c + "' no encontrado en la matriz.");
            }*/
        }

        return ubicaciones;
    }



    
}
