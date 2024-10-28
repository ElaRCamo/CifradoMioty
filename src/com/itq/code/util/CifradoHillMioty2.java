package com.itq.code.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class CifradoHillMioty2 {
    
    static Map<Character, Integer> diccionario_encrypt = new HashMap<>();
    static Map<Integer, Character> diccionario_decrypt = new HashMap<>();

    static {
        // Inicializando los diccionarios
        String caracteres = "ABCDEFGHIJKLMNÑOPQRSTUVWXYZ0123456789.,:? ";
        for (int i = 0; i < caracteres.length(); i++) {
            diccionario_encrypt.put(caracteres.charAt(i), i);
            diccionario_decrypt.put(i, caracteres.charAt(i));
        }
    }

    static int[] primos = {
            2, 3, 5, 7, 11, 13, 17, 19, 23, 29, 
            31, 37, 41, 43, 47, 53, 59, 61, 67, 71, 
            73, 79, 83, 89, 97, 101, 103, 107, 109, 113, 
            127, 131, 137, 139, 149, 151, 157, 163, 167, 173, 
            179, 181
        };

        public static int[][] generarClaveHill(int tamaño) {
            int[][] matrix;
            int det;
            do {
                matrix = new int[tamaño][tamaño];
                Random random = new Random();
                
                for (int i = 0; i < tamaño; i++) {
                    for (int j = 0; j < tamaño; j++) {
                        // Usar un primo aleatorio del array primos
                        matrix[i][j] = primos[random.nextInt(42)];
                    }
                }

                // Calculamos el determinante y verificamos si tiene un inverso en el módulo 42
                det = determinante(matrix, 42);
            } while (gcd(det, 42) != 1);  // Repetir hasta que el determinante y 42 sean coprimos
            
        

            return matrix;
        }
        
        public static void imprimirMatriz(int[][] matrix) {
            for (int i = 0; i < matrix.length; i++) {
                for (int j = 0; j < matrix[i].length; j++) {
                    System.out.print(matrix[i][j] + " ");
                }
                System.out.println();
            }
        }


     // Función para calcular el máximo común divisor (GCD)
        private static int gcd(int a, int b) {
            a = Math.abs(a);
            b = Math.abs(b);
            
            while (b != 0) {
                int temp = b;
                b = a % b;
                a = temp;
            }
            return a;
        }


        public static String cifrarMensajeHill(String mensaje, int[][] clave) {
            mensaje = mensaje.toUpperCase();
            StringBuilder ciphertext = new StringBuilder();
            List<Integer> listTemp = new ArrayList<>();

            // Aseguramos que el mensaje tenga una longitud igual a la matriz clave
            mensaje = rellenarMensaje(mensaje, clave.length);

            // Caso cuando el mensaje es más corto o igual al tamaño de la clave
            if (mensaje.length() <= clave.length) {
                int[] matrixMensaje = new int[mensaje.length()];
                for (int i = 0; i < mensaje.length(); i++) {
                    matrixMensaje[i] = diccionario_encrypt.get(mensaje.charAt(i));
                }

                // Multiplicación de matriz con el vector del mensaje en módulo 42
                int[] cifrado = multiplicarMatriz(clave, matrixMensaje, 42);
                for (int valor : cifrado) {
                    ciphertext.append(diccionario_decrypt.get(valor));
                }
            } else {
                // Caso cuando el mensaje es más largo que el tamaño de la clave
                for (int i = 0; i < mensaje.length(); i += clave.length) {
                    for (int j = 0; j < clave.length; j++) {
                        listTemp.add(diccionario_encrypt.get(mensaje.charAt(i + j)));
                    }
                    int[] bloqueCifrado = multiplicarMatriz(clave, listTemp.stream().mapToInt(Integer::intValue).toArray(), 42);
                    for (int valor : bloqueCifrado) {
                        ciphertext.append(diccionario_decrypt.get(valor));
                    }
                    listTemp.clear();
                }
            }

            return ciphertext.toString();
        }

        

        // Método para rellenar el mensaje a la longitud deseada
        private static String rellenarMensaje(String mensaje, int longitud) {
            while (mensaje.length() % longitud != 0) {
                mensaje += "X";  // Usa 'X' o cualquier otro carácter como relleno
            }
            return mensaje;
        }

        public static String descifrarMensajeHill(String mensaje, int[][] clave) {
            StringBuilder plaintext = new StringBuilder();
            List<Integer> listTemp = new ArrayList<>();

            // Generar la clave inversa en módulo 42
            int[][] claveInversa = invertirModulo(clave, 42);

            for (int i = 0; i < mensaje.length(); i += clave.length) {
                for (int j = 0; j < clave.length; j++) {
                    listTemp.add(diccionario_encrypt.get(mensaje.charAt(i + j)));
                }
                int[] bloqueDescifrado = multiplicarMatriz(claveInversa, listTemp.stream().mapToInt(Integer::intValue).toArray(), 42);
                for (int valor : bloqueDescifrado) {
                    plaintext.append(diccionario_decrypt.get(valor));
                }
                listTemp.clear();
            }

            // Eliminar el carácter de relleno ('X') al final, si existe
            while (plaintext.length() > 0 && plaintext.charAt(plaintext.length() - 1) == 'X') {
                plaintext.setLength(plaintext.length() - 1);
            }

            return plaintext.toString();
        }


     // Método para multiplicar matrices en un módulo específico
        private static int[] multiplicarMatriz(int[][] clave, int[] vector, int mod) {
            int[] resultado = new int[clave.length];
            for (int i = 0; i < clave.length; i++) {
                int suma = 0;
                for (int j = 0; j < clave[i].length; j++) {
                    suma += clave[i][j] * vector[j];
                }
                resultado[i] = suma % mod;
            }
            return resultado;
        }

        private static int[][] invertirModulo(int[][] matriz, int mod) {
            int n = matriz.length;
            int det = determinante(matriz, mod);  // Calcula el determinante en módulo
            int detInverso = inversoMultiplicativo(det, mod);  // Inverso del determinante en módulo

            if (detInverso == -1) {
                throw new ArithmeticException("La matriz no es invertible en el módulo " + mod);
            }

            int[][] adjunta = matrizAdjunta(matriz, mod);  // Calcula la adjunta en módulo
            int[][] inversa = new int[n][n];

            // Multiplica la matriz adjunta por el inverso del determinante en módulo
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    inversa[i][j] = (adjunta[i][j] * detInverso) % mod;
                    if (inversa[i][j] < 0) inversa[i][j] += mod;  // Asegura valores positivos
                }
            }
            return inversa;
        }

        // Método para calcular el inverso multiplicativo en módulo específico
        private static int inversoMultiplicativo(int a, int mod) {
            // Implementación del algoritmo extendido de Euclides para encontrar el inverso
            int t = 0, newT = 1;
            int r = mod, newR = a;

            while (newR != 0) {
                int quotient = r / newR;
                int tempT = t;
                t = newT;
                newT = tempT - quotient * newT;

                int tempR = r;
                r = newR;
                newR = tempR - quotient * newR;
            }

            if (r > 1) return -1;  // No existe el inverso
            if (t < 0) t += mod;  // Ajusta a un valor positivo en el rango del módulo
            return t;
        }

        // Método para calcular la matriz adjunta en módulo específico
        private static int[][] matrizAdjunta(int[][] matriz, int mod) {
            int n = matriz.length;
            int[][] adjunta = new int[n][n];

            // Calculo de cofactores para la adjunta en el módulo
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    int signo = ((i + j) % 2 == 0) ? 1 : -1;
                    int[][] submatriz = obtenerSubmatriz(matriz, i, j);
                    adjunta[j][i] = (signo * determinante(submatriz, mod)) % mod;
                    if (adjunta[j][i] < 0) adjunta[j][i] += mod;
                }
            }
            return adjunta;
        }

        // Método para obtener la submatriz excluyendo la fila `excluirFila` y la columna `excluirColumna`
        private static int[][] obtenerSubmatriz(int[][] matriz, int excluirFila, int excluirColumna) {
            int n = matriz.length;
            int[][] submatriz = new int[n - 1][n - 1];
            int row = 0;
            for (int i = 0; i < n; i++) {
                if (i == excluirFila) continue;
                int col = 0;
                for (int j = 0; j < n; j++) {
                    if (j == excluirColumna) continue;
                    submatriz[row][col] = matriz[i][j];
                    col++;
                }
                row++;
            }
            return submatriz;
        }


        private static int determinante(int[][] matriz, int mod) {
            int n = matriz.length;

            // Caso base: Si la matriz es de 1x1, su determinante es el único elemento en el módulo especificado
            if (n == 1) return matriz[0][0] % mod;

            int det = 0;

            // Expansión de Laplace sobre la primera fila
            for (int i = 0; i < n; i++) {
                // Generar submatriz excluyendo la fila 0 y la columna i
                int[][] subMatriz = new int[n - 1][n - 1];
                for (int j = 1; j < n; j++) {  // Comienza en la segunda fila
                    int col = 0;
                    for (int k = 0; k < n; k++) {
                        if (k == i) continue;  // Omitir columna i
                        subMatriz[j - 1][col++] = matriz[j][k];
                    }
                }

                // Calcula el cofactor y lo suma al determinante en el módulo especificado
                int cofactor = matriz[0][i] * (int) Math.pow(-1, i) * determinante(subMatriz, mod);
                det = (det + cofactor) % mod;
            }

            // Asegurarse de que el resultado esté en un rango positivo en el módulo
            return (det + mod) % mod;
        }

/*
    public static void main(String[] args) {
        int[][] clave = generarClaveHill(3);  // Ejemplo para una matriz 3x3
        String mensaje = "EN LA MONTAÑA HACE UNA TEMPERTURA DE 20.5 GRADOS CELCIUS";
        
        String mensajeCifrado = cifrarMensajeHill(mensaje, clave);
        System.out.println("Mensaje cifrado: " + mensajeCifrado);

        String mensajeDescifrado = descifrarMensajeHill(mensajeCifrado, clave);
        System.out.println("Mensaje descifrado: " + mensajeDescifrado);
    }
   */ 
}
