package com.famoco.myfirstjhipster.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Lob;

/**
 * A DTO for the {@link com.famoco.myfirstjhipster.domain.FileName} entity.
 */
public class FileNameDTO implements Serializable {

    private Long id;

    private String name;

    @Lob
    private byte[] content;

    private String contentContentType;
    private String description;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }

    public String getContentContentType() {
        return contentContentType;
    }

    public void setContentContentType(String contentContentType) {
        this.contentContentType = contentContentType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FileNameDTO)) {
            return false;
        }

        FileNameDTO fileNameDTO = (FileNameDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, fileNameDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FileNameDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", content='" + getContent() + "'" +
            ", description='" + getDescription() + "'" +
            "}";
    }
}
