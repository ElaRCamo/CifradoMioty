package com.itq.code.util;

import java.util.*;

public class CifradoHillMioty {
    
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

    public static int[][] generarClaveHill(int tamaño) {
        int[][] matrix;
        int det;
        do {
            matrix = new int[tamaño][tamaño];
            Random random = new Random();
            
            for (int i = 0; i < tamaño; i++) {
                for (int j = 0; j < tamaño; j++) {
                    matrix[i][j] = random.nextInt(42); // Números entre 0 y 41
                }
            }

            // Calculamos el determinante y verificamos si tiene un inverso en el módulo 42
            det = determinante(matrix, 42);
        } while (gcd(det, 42) != 1);  // Repetir hasta que el determinante y 42 sean coprimos

        return matrix;
    }

    // Función para calcular el máximo común divisor (GCD)
    private static int gcd(int a, int b) {
        while (b != 0) {
            int temp = b;
            b = a % b;
            a = temp;
        }
        return Math.abs(a);
    }


    public static String cifrarMensajeHill(String mensaje, int[][] clave) {
        mensaje = mensaje.toUpperCase();
        StringBuilder ciphertext = new StringBuilder();
        List<Integer> listTemp = new ArrayList<>();

        if (mensaje.length() <= clave.length) {
            mensaje = rellenarMensaje(mensaje, clave.length);

            int[] matrixMensaje = new int[mensaje.length()];
            for (int i = 0; i < mensaje.length(); i++) {
                matrixMensaje[i] = diccionario_encrypt.get(mensaje.charAt(i));
            }

            int[] cifrado = multiplicarMatriz(clave, matrixMensaje);
            for (int valor : cifrado) {
                ciphertext.append(diccionario_decrypt.get(valor));
            }
        } else {
            mensaje = rellenarMensaje(mensaje, clave.length);

            for (int i = 0; i < mensaje.length(); i += clave.length) {
                for (int j = 0; j < clave.length; j++) {
                    listTemp.add(diccionario_encrypt.get(mensaje.charAt(i + j)));
                }
                int[] bloqueCifrado = multiplicarMatriz(clave, listTemp.stream().mapToInt(Integer::intValue).toArray());
                for (int valor : bloqueCifrado) {
                    ciphertext.append(diccionario_decrypt.get(valor));
                }
                listTemp.clear();
            }
        }

        return ciphertext.toString();
    }

    public static String descifrarMensajeHill(String mensaje, int[][] clave) {
        StringBuilder plaintext = new StringBuilder();
        List<Integer> listTemp = new ArrayList<>();
        
        int[][] claveInversa = invertirModulo(clave, 42);

        for (int i = 0; i < mensaje.length(); i += clave.length) {
            for (int j = 0; j < clave.length; j++) {
                listTemp.add(diccionario_encrypt.get(mensaje.charAt(i + j)));
            }
            int[] bloqueDescifrado = multiplicarMatriz(claveInversa, listTemp.stream().mapToInt(Integer::intValue).toArray());
            for (int valor : bloqueDescifrado) {
                plaintext.append(diccionario_decrypt.get(valor));
            }
            listTemp.clear();
        }

        while (plaintext.charAt(plaintext.length() - 1) == 'X') {
            plaintext.setLength(plaintext.length() - 1);
        }

        return plaintext.toString();
    }

    private static String rellenarMensaje(String mensaje, int longitud) {
        StringBuilder sb = new StringBuilder(mensaje);
        while (sb.length() % longitud != 0) {
            sb.append('X');
        }
        return sb.toString();
    }

    private static int[] multiplicarMatriz(int[][] clave, int[] mensaje) {
        int[] resultado = new int[clave.length];
        for (int i = 0; i < clave.length; i++) {
            for (int j = 0; j < clave.length; j++) {
                resultado[i] += clave[i][j] * mensaje[j];
            }
            resultado[i] = resultado[i] % 42;
        }
        return resultado;
    }

    private static int[][] invertirModulo(int[][] matriz, int mod) {
        int n = matriz.length;
        int det = determinante(matriz, mod);  // Calcula el determinante con módulo
        int detInverso = inversoMultiplicativo(det, mod);  // Inverso del determinante en el módulo

        if (detInverso == -1) {
            throw new ArithmeticException("La matriz no es invertible en el módulo " + mod);
        }

        int[][] adjunta = matrizAdjunta(matriz, mod);
        int[][] inversa = new int[n][n];

        // Multiplica la matriz adjunta por el inverso del determinante en el módulo
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                inversa[i][j] = (adjunta[i][j] * detInverso) % mod;
                if (inversa[i][j] < 0) inversa[i][j] += mod;  // Asegura valores positivos
            }
        }
        return inversa;
    }

    private static int determinante(int[][] matriz, int mod) {
        int n = matriz.length;
        if (n == 1) return matriz[0][0] % mod;
        
        int det = 0;
        for (int i = 0; i < n; i++) {
            int[][] subMatriz = new int[n - 1][n - 1];
            for (int j = 1; j < n; j++) {
                for (int k = 0, col = 0; k < n; k++) {
                    if (k == i) continue;
                    subMatriz[j - 1][col++] = matriz[j][k];
                }
            }
            det += matriz[0][i] * (int) Math.pow(-1, i) * determinante(subMatriz, mod);
            det %= mod;
        }
        return (det + mod) % mod;
    }

    private static int inversoMultiplicativo(int a, int mod) {
        for (int i = 1; i < mod; i++) {
            if ((a * i) % mod == 1) {
                return i;
            }
        }
        return -1; // No tiene inverso multiplicativo
    }

    private static int[][] matrizAdjunta(int[][] matriz, int mod) {
        int n = matriz.length;
        int[][] adjunta = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                int[][] subMatriz = new int[n - 1][n - 1];
                for (int k = 0; k < n; k++) {
                    if (k == i) continue;
                    for (int l = 0; l < n; l++) {
                        if (l == j) continue;
                        subMatriz[k < i ? k : k - 1][l < j ? l : l - 1] = matriz[k][l];
                    }
                }
                adjunta[j][i] = (int) Math.pow(-1, i + j) * determinante(subMatriz, mod);
                adjunta[j][i] = (adjunta[j][i] % mod + mod) % mod; // Aplicar el módulo y ajustar al positivo
            }
        }
        return adjunta;
    }

    /*
    public static void main(String[] args) {
        int[][] clave = generarClaveHill(3);  // Ejemplo para una matriz 3x3
        String mensaje = "EN LA MONTAÑA HACE UNA TEMPERTURA DE 20.5 GRADOS CELCIUS";
        
        String mensajeCifrado = cifrarMensajeHill(mensaje, clave);
        System.out.println("Mensaje cifrado: " + mensajeCifrado);

        String mensajeDescifrado = descifrarMensajeHill(mensajeCifrado, clave);
        System.out.println("Mensaje descifrado: " + mensajeDescifrado);
    }*/
    
}
