package com.leo.ai.ollamachat.domain.document;

import jakarta.persistence.*;

@Entity
@Table(name = "document_chunks")
public class DocumentChunk {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Conteúdo textual do chunk
     */
    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    /**
     * Embedding vetorial (pgvector)
     * IMPORTANTE: PostgreSQL column must be vector(768)
     */
    @Column(name = "embedding", columnDefinition = "vector(768)")
    private float[] embedding;

    /**
     * Origem lógica (ex: PDF, WEB, TEXT, filename, URL, etc)
     */
    @Column(name = "source")
    private String source;

    /**
     * Metadata adicional em JSON string
     */
    @Column(name = "metadata", columnDefinition = "TEXT")
    private String metadata;

    /**
     * ID lógico do documento (para agrupar chunks)
     */
    @Column(name = "document_id")
    private String documentId;

    /**
     * Índice do chunk dentro do documento
     */
    @Column(name = "chunk_index")
    private Integer chunkIndex;

    // ---------- CONSTRUCTORS ----------
    
    

    public DocumentChunk() {
    }

    public DocumentChunk(String content, float[] embedding, String source, String metadata) {
        this.content = content;
        this.embedding = embedding;
        this.source = source;
        this.metadata = metadata;
    }

    public DocumentChunk(String content, float[] embedding, String source, String metadata,
                         String documentId, Integer chunkIndex) {
        this.content = content;
        this.embedding = embedding;
        this.source = source;
        this.metadata = metadata;
        this.documentId = documentId;
        this.chunkIndex = chunkIndex;
    }

    public DocumentChunk(String content, String source, String metadata) {
        this.content = content;
        this.source = source;
        this.metadata = metadata;
    }

    // ---------- GETTERS ----------

    public Long getId() {
        return id;
    }

    public String getContent() {
        return content;
    }

    public float[] getEmbedding() {
        return embedding;
    }

    public String getSource() {
        return source;
    }

    public String getMetadata() {
        return metadata;
    }

    public String getDocumentId() {
        return documentId;
    }

    public Integer getChunkIndex() {
        return chunkIndex;
    }

    // ---------- SETTERS ----------

    public void setId(Long id) {
        this.id = id;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setEmbedding(float[] embedding) {
        this.embedding = embedding;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public void setMetadata(String metadata) {
        this.metadata = metadata;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public void setChunkIndex(Integer chunkIndex) {
        this.chunkIndex = chunkIndex;
    }
}