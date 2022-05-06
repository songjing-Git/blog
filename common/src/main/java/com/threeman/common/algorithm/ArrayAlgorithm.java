package com.threeman.common.algorithm;

/**
 * 数组结构
 *
 * @author songjing
 * @version 1.0
 * @date 2021/12/2 17:47
 */
public class ArrayAlgorithm {

    /**
     * 稀疏数组
     *
     * @param chessArray 一般数组
     * @return 稀疏数组
     */
    public static int[][] sparseArray(int[][] chessArray) {
        if (chessArray == null || chessArray.length == 0) {
            return chessArray;
        }
        int count = 0;
        int colNum = 0;
        //遍历数组 row 行元素
        for (int[] row : chessArray) {
            //data 数组元素
            for (int data : row) {
                if (data != 0) {
                    count++;
                }
                colNum = row.length;
            }
        }

        int[][] resultArray = new int[count + 1][3];
        resultArray[0][0] = chessArray.length;
        resultArray[0][1] = colNum;
        resultArray[0][2] = count;
        int colIndex = 0;
        int rowIndex = 1;
        for (int ci = 0; ci < chessArray.length; ci++) {
            for (int i = 0; i < chessArray[ci].length; i++) {
                if (chessArray[ci][i] != 0) {
                    resultArray[rowIndex][colIndex] = ci;
                    resultArray[rowIndex][colIndex + 1] = i;
                    resultArray[rowIndex][colIndex + 2] = chessArray[ci][i];
                    rowIndex++;
                }
            }
        }
        return resultArray;
    }

    /**
     * 稀疏数组转为一般数组
     *
     * @param arraySparse 稀疏数组
     * @return 一般数组
     */
    public static int[][] arraySparse(int[][] arraySparse) {
        if (arraySparse == null || arraySparse.length == 0) {
            return arraySparse;
        }

        int rowNumber = arraySparse[0][0];
        int colNumber = arraySparse[0][1];
        int count = arraySparse[0][2];
        int[][] array = new int[rowNumber][colNumber];
        for (int i = 1; i < arraySparse.length; i++) {
            int row = arraySparse[i][0];
            int col = arraySparse[i][1];
            array[row][col] = arraySparse[i][2];
        }
        return array;

    }
}
