package br.com.sentinelx.core.domain.event;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class EvidenceReference {

    @Column(name = "evidence_key", length = 255)
    private String key;

    @Column(name = "evidence_hash", length = 255)
    private String hash;

    @Column(name = "evidence_content_type", length = 100)
    private String contentType;

    @Column(name = "evidence_size_bytes")
    private Long sizeBytes;

    protected EvidenceReference() {
    }

    public EvidenceReference(String key, String hash, String contentType, Long sizeBytes) {
        this.key = key;
        this.hash = hash;
        this.contentType = contentType;
        this.sizeBytes = sizeBytes;
    }

    public String getKey() {
        return key;
    }

    public String getHash() {
        return hash;
    }

    public String getContentType() {
        return contentType;
    }

    public Long getSizeBytes() {
        return sizeBytes;
    }
}