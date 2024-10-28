package com.itq.code.util;

public class numerenturutil {

    public static int Determinante(int[][] matriz, int n) {
        int det = 0;
        if (n == 1) {
            return matriz[0][0];
        } else if (n == 2) {
            return matriz[0][0] * matriz[1][1] - matriz[0][1] * matriz[1][0];
        } else {
            for (int x = 0; x < n; x++) {
                int[][] submatriz = new int[n - 1][n - 1];
                for (int i = 1; i < n; i++) {
                    int j = 0;
                    for (int k = 0; k < n; k++) {
                        if (k != x) {
                            submatriz[i - 1][j++] = matriz[i][k];
                        }
                    }
                }
                det += Math.pow(-1, x) * matriz[0][x] * Determinante(submatriz, n - 1);
            }
        }
        return det;
    }

    public static int[][] cofactor(int[][] matriz, int n, int TalfabetoCif) {
        int[][] cofactores = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                int[][] submatriz = new int[n - 1][n - 1];
                for (int k = 0; k < n; k++) {
                    for (int l = 0; l < n; l++) {
                        if (k != i && l != j) {
                            submatriz[k < i ? k : k - 1][l < j ? l : l - 1] = matriz[k][l];
                        }
                    }
                }
                cofactores[i][j] = (int) Math.pow(-1, i + j) * Determinante(submatriz, n - 1);
            }
        }
        return cofactores;
    }
}

