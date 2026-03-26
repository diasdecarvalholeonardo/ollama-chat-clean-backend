package com.leo.ai.ollamachat.domain.document;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.*;
import java.util.HashMap;
import java.util.Map;

@Entity
@Table(name = "document_chunks")
public class DocumentChunk {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private double retrievalScore;
    private double rerankScore;
    private double finalScore;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Transient
    private float[] embedding;

    @Column(name = "embedding", columnDefinition = "vector(768)")
    @org.hibernate.annotations.JdbcTypeCode(java.sql.Types.OTHER)
    private String embeddingVector;

    @Column(name = "source")
    private String source;

    // 🔴 PERSISTIDO NO BANCO (JSON STRING)
    @Column(name = "metadata", columnDefinition = "TEXT")
    private String metadata;

    // 🔵 USADO EM RUNTIME (PIPELINE RAG)
    @Transient
    private Map<String, Object> metadataMap;

    @Column(name = "document_id")
    private String documentId;

    @Column(name = "chunk_index")
    private Integer chunkIndex;

    // =====================================================
    // 🧠 🔥 NOVO — SCORE PARA RANKING (RAG PIPELINE)
    // =====================================================
    @Transient
    private double score;

    // ---------- GETTERS ----------

    public Long getId() { return id; }

    public String getContent() { return content; }

    public float[] getEmbedding() { return embedding; }

    public String getEmbeddingVector() { return embeddingVector; }

    public String getSource() { return source; }

    public String getMetadata() { return metadata; }

    public String getDocumentId() { return documentId; }

    public Integer getChunkIndex() { return chunkIndex; }

    public double getScore() { return score; }

    /**
     * 🔥 NOVO GETTER (PIPELINE RAG)
     */
    public Map<String, Object> getMetadataMap() {

        Map<String, Object> map = new HashMap<>();

        if (this.metadata == null || this.metadata.isBlank()) {
            return map;
        }

        String[] entries = this.metadata.split(";");

        for (String entry : entries) {
            String[] kv = entry.split("=");

            if (kv.length == 2) {
                try {
                    map.put(kv[0], Double.parseDouble(kv[1]));
                } catch (Exception e) {
                    map.put(kv[0], kv[1]);
                }
            }
        }

        return map;
    }

    // ---------- SETTERS ----------

    public void setId(Long id) { this.id = id; }

    public void setContent(String content) { this.content = content; }

    public void setEmbedding(float[] embedding) {
        this.embedding = embedding;
        if (embedding != null) {
            this.embeddingVector = convertToVector(embedding);
        }
    }

    public void setEmbeddingVector(String embeddingVector) {
        this.embeddingVector = embeddingVector;
        if (embeddingVector != null) {
            this.embedding = convertToFloatArray(embeddingVector);
        }
    }

    public void setSource(String source) { this.source = source; }

    public void setMetadata(String metadata) {
        this.metadata = metadata;
        this.metadataMap = null; // força reload
    }

    public void setDocumentId(String documentId) { this.documentId = documentId; }

    public void setChunkIndex(Integer chunkIndex) { this.chunkIndex = chunkIndex; }

    public void setScore(double score) { this.score = score; }

    /**
     * 🔥 NOVO SETTER (PIPELINE RAG)
     */
    public void setMetadataMap(Map<String, Object> metadataMap) {
        this.metadataMap = metadataMap;

        try {
            this.metadata = OBJECT_MAPPER.writeValueAsString(metadataMap);
        } catch (Exception e) {
            this.metadata = "{}";
        }
    }

    // ---------- UTIL ----------

    private String convertToVector(float[] embedding) {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < embedding.length; i++) {
            sb.append(embedding[i]);
            if (i < embedding.length - 1) sb.append(",");
        }
        sb.append("]");
        return sb.toString();
    }

    private float[] convertToFloatArray(String vector) {
        vector = vector.replace("[", "").replace("]", "");
        String[] parts = vector.split(",");

        float[] result = new float[parts.length];

        for (int i = 0; i < parts.length; i++) {
            result[i] = Float.parseFloat(parts[i].trim());
        }

        return result;
    }
}