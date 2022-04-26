package com.famoco.myfirstjhipster.service.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.famoco.myfirstjhipster.domain.Device} entity.
 */
public class DeviceDTO implements Serializable {

    private Long id;

    @NotNull
    private Integer famocoId;

    @NotNull
    private String macAddress;

    @NotNull
    private LocalDate dateCreation;

    private RetailerDTO retailer;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getFamocoId() {
        return famocoId;
    }

    public void setFamocoId(Integer famocoId) {
        this.famocoId = famocoId;
    }

    public String getMacAddress() {
        return macAddress;
    }

    public void setMacAddress(String macAddress) {
        this.macAddress = macAddress;
    }

    public LocalDate getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(LocalDate dateCreation) {
        this.dateCreation = dateCreation;
    }

    public RetailerDTO getRetailer() {
        return retailer;
    }

    public void setRetailer(RetailerDTO retailer) {
        this.retailer = retailer;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DeviceDTO)) {
            return false;
        }

        DeviceDTO deviceDTO = (DeviceDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, deviceDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DeviceDTO{" +
            "id=" + getId() +
            ", famocoId=" + getFamocoId() +
            ", macAddress='" + getMacAddress() + "'" +
            ", dateCreation='" + getDateCreation() + "'" +
            ", retailer=" + getRetailer() +
            "}";
    }
}
