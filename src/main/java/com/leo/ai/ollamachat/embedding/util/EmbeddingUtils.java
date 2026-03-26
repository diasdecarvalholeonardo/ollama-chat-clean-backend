package com.leo.ai.ollamachat.embedding.util;

public class EmbeddingUtils {

    public static String toVectorString(float[] vector) {

        StringBuilder sb = new StringBuilder("[");

        for (int i = 0; i < vector.length; i++) {

            sb.append(vector[i]);

            if (i < vector.length - 1) {
                sb.append(",");
            }

        }

        sb.append("]");

        return sb.toString();
    }
}
