package com.itq.code.util;

import java.util.HashMap;
import java.util.Map;

public class Diccionario {
    static Map<Character, Integer> diccionario_encrypt = new HashMap<>();
    static Map<Integer, Character> diccionario_decrypt = new HashMap<>();

    // Método para obtener los primeros números primos
    private static int[] obtenerNumerosPrimos(int cantidad) {
        int[] numerosPrimos = new int[cantidad];
        int numero = 2; // Comienza desde el primer número primo
        int contador = 0;

        while (contador < cantidad) {
            if (esNumeroPrimo(numero)) {
                numerosPrimos[contador] = numero;
                contador++;
            }
            numero++;
        }
        return numerosPrimos;
    }

    // Verifica si un número es primo
    private static boolean esNumeroPrimo(int num) {
        if (num <= 1) return false;
        for (int i = 2; i <= Math.sqrt(num); i++) {
            if (num % i == 0) return false;
        }
        return true;
    }

    static {
        // Inicializando los diccionarios con números primos
        String caracteres = "ABCDEFGHIJKLMNÑOPQRSTUVWXYZ0123456789.,:? ";
        int[] numerosPrimos = obtenerNumerosPrimos(caracteres.length());

        for (int i = 0; i < caracteres.length(); i++) {
            int numeroPrimo = numerosPrimos[i];
            diccionario_encrypt.put(caracteres.charAt(i), numeroPrimo);
            diccionario_decrypt.put(numeroPrimo, caracteres.charAt(i));
        }
    }

    public static void main(String[] args) {
        // Ejemplo de prueba
        System.out.println("diccionario_encrypt: " + diccionario_encrypt);
        System.out.println("diccionario_decrypt: " + diccionario_decrypt);
    }
}

