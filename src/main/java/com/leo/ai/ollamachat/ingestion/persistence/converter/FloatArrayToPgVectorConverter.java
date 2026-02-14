package com.leo.ai.ollamachat.ingestion.persistence.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.StringJoiner;

@Converter(autoApply = false)
public class FloatArrayToPgVectorConverter
        implements AttributeConverter<float[], String> {

    @Override
    public String convertToDatabaseColumn(float[] attribute) {
        if (attribute == null) {
            return null;
        }

        // pgvector espera: [0.1,0.2,0.3,...]
        StringJoiner joiner = new StringJoiner(",", "[", "]");
        for (float v : attribute) {
            joiner.add(Float.toString(v));
        }
        return joiner.toString();
    }

    @Override
    public float[] convertToEntityAttribute(String dbData) {
        if (dbData == null || dbData.isBlank()) {
            return null;
        }

        // Remove [ ]
        String cleaned = dbData.replace("[", "").replace("]", "");
        String[] parts = cleaned.split(",");

        float[] vector = new float[parts.length];
        for (int i = 0; i < parts.length; i++) {
            vector[i] = Float.parseFloat(parts[i].trim());
        }
        return vector;
    }
}

