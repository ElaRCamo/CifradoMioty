package com.itq.code.util;

import java.util.Random;

public class CifradoHill {

    public static String[] procesarMensaje(String mensaje, char cifrado, String nipUser) {
        if (cifrado == 'c') {
            String nip = generarNIP(); // Generar NIP con determinante válido
            System.out.println("NIP generado: " + nip);

            int[][] matriz = obtenerMatrizClave(nip);
            String mensajeCifrado = cifrarMensaje(mensaje, matriz);
            return new String[]{nip, mensajeCifrado};

        } else if (cifrado == 'd') {
            try {
                System.out.println("NIP recibido para descifrado: " + nipUser);

                // Validar el formato del NIP
                if (nipUser.length() != 4 || !nipUser.matches("\\d+")) {
                    throw new IllegalArgumentException("El NIP recibido no es válido.");
                }

                int[][] matriz = obtenerMatrizClave(nipUser);
                int det = calcularDeterminante(matriz) % 26; // Calcular determinante de la matriz clave
                if (det < 0) {
                    det += 26;
                }
                System.out.println("Determinante: " + det);

                // Verificar que el determinante sea diferente de cero y tenga inverso
                if (det == 0 || !tieneInversoModulo(det, 26)) {
                    throw new IllegalArgumentException("No se puede descifrar, determinante no tiene inverso.");
                }

                // Filtrar el mensaje para asegurarse de que solo contenga caracteres válidos
                String mensajeFiltrado = filtrarMensaje(mensaje); // Filtrar caracteres no válidos
                // Verificar que el mensaje filtrado tenga longitud par
                if (mensajeFiltrado.length() % 2 != 0) {
                    throw new IllegalArgumentException("El mensaje cifrado debe tener longitud par.");
                }

                String mensajeDescifrado = descifrarMensaje(mensajeFiltrado, matriz);
                System.out.println("Mensaje descifrado: " + mensajeDescifrado);
                return new String[]{nipUser, mensajeDescifrado};

            } catch (Exception e) {
                System.err.println("Error al descifrar el mensaje: " + e.getMessage());
                return new String[]{"", "Error en el mensaje cifrado"};
            }
        }

        return new String[]{nipUser, mensaje};
    }

    // Método para filtrar caracteres no válidos del mensaje
    private static String filtrarMensaje(String mensaje) {
        return mensaje.replaceAll("[^A-Z]", ""); // Solo mantiene letras mayúsculas
    }

    // Método para generar un NIP válido con determinante
    private static String generarNIP() {
        Random random = new Random();
        int a, b, c, d;
        int det;

        do {
            a = random.nextInt(10); // dígito entre 0 y 9
            b = random.nextInt(10);
            c = random.nextInt(10);
            d = random.nextInt(10);

            // Crear matriz con los dígitos generados
            int[][] matriz = {
                {a, b},
                {c, d}
            };
            // Calcular el determinante
            det = calcularDeterminante(matriz);

        } while (det == 0 || !tieneInversoModulo(det, 26));

        // Devolver el NIP como un String
        return String.valueOf(a) + b + c + d;
    }

    // Método para calcular el determinante de una matriz 2x2
    private static int calcularDeterminante(int[][] matriz) {
        return matriz[0][0] * matriz[1][1] - matriz[0][1] * matriz[1][0];
    }

    private static boolean tieneInversoModulo(int num, int mod) {
        return gcd(num, mod) == 1;
    }

    private static int gcd(int a, int b) {
        while (b != 0) {
            int temp = b;
            b = a % b;
            a = temp;
        }
        return Math.abs(a);
    }

    public static int[][] obtenerMatrizClave(String nip) {
        if (nip.length() != 4 || !nip.matches("\\d+")) {
            throw new IllegalArgumentException("El NIP debe tener exactamente 4 dígitos.");
        }

        // Crear una matriz de 2x2
        int[][] matriz = new int[2][2];

        // Llenar la matriz con los dígitos del NIP
        matriz[0][0] = Character.getNumericValue(nip.charAt(0));
        matriz[0][1] = Character.getNumericValue(nip.charAt(1));
        matriz[1][0] = Character.getNumericValue(nip.charAt(2));
        matriz[1][1] = Character.getNumericValue(nip.charAt(3));

        // Imprimir la matriz clave
        System.out.println("Matriz clave:");
        System.out.println(matriz[0][0] + " " + matriz[0][1]);
        System.out.println(matriz[1][0] + " " + matriz[1][1]);

        return matriz;
    }

    public static String cifrarMensaje(String mensaje, int[][] matriz) {
        StringBuilder mensajeCifrado = new StringBuilder();
        mensaje = mensaje.replace(" ", ""); // quitar espacios

        // Asegurarnos de que el mensaje tenga una longitud par
        if (mensaje.length() % 2 != 0) {
            mensaje += 'X'; // Agregar un 'X' si es impar
        }

        // Cifrar el mensaje en pares
        for (int i = 0; i < mensaje.length(); i += 2) {
            int[] par = new int[2];
            par[0] = mensaje.charAt(i) - 'A'; // Convertir a 0-25
            par[1] = mensaje.charAt(i + 1) - 'A';

            int[] resultado = new int[2];
            resultado[0] = (matriz[0][0] * par[0] + matriz[0][1] * par[1]) % 26;
            resultado[1] = (matriz[1][0] * par[0] + matriz[1][1] * par[1]) % 26;

            mensajeCifrado.append((char) (resultado[0] + 'A'));
            mensajeCifrado.append((char) (resultado[1] + 'A'));
        }

        return mensajeCifrado.toString();
    }

    public static String descifrarMensaje(String mensaje, int[][] matriz) {
        // Encontrar la inversa de la matriz
        int determinante = calcularDeterminante(matriz) % 26;
        if (determinante < 0) {
            determinante += 26;
        }
        int inverso = encontrarInverso(determinante, 26);
        if (inverso < 0) {
            throw new IllegalArgumentException("No se puede descifrar, determinante no tiene inverso.");
        }

        // Calcular la matriz inversa
        int[][] matrizInversa = {
            {matriz[1][1] * inverso % 26, -matriz[0][1] * inverso % 26},
            {-matriz[1][0] * inverso % 26, matriz[0][0] * inverso % 26}
        };

        // Ajustar la matriz inversa a módulo 26
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 2; j++) {
                matrizInversa[i][j] = (matrizInversa[i][j] + 26) % 26;
            }
        }

        StringBuilder mensajeDescifrado = new StringBuilder();

        // Descifrar el mensaje en pares
        for (int i = 0; i < mensaje.length(); i += 2) {
            int[] par = new int[2];
            par[0] = mensaje.charAt(i) - 'A';
            par[1] = mensaje.charAt(i + 1) - 'A';

            int[] resultado = new int[2];
            resultado[0] = (matrizInversa[0][0] * par[0] + matrizInversa[0][1] * par[1]) % 26;
            resultado[1] = (matrizInversa[1][0] * par[0] + matrizInversa[1][1] * par[1]) % 26;

            mensajeDescifrado.append((char) (resultado[0] + 'A'));
            mensajeDescifrado.append((char) (resultado[1] + 'A'));
        }

        return mensajeDescifrado.toString();
    }

    private static int encontrarInverso(int determinante, int mod) {
        for (int i = 1; i < mod; i++) {
            if ((determinante * i) % mod == 1) {
                return i;
            }
        }
        return -1; // No se encontró inverso
    }
}

