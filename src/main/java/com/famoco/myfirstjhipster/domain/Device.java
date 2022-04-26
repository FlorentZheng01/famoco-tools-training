package com.famoco.myfirstjhipster.domain;

import java.io.Serializable;
import java.time.LocalDate;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Device.
 */
@Entity
@Table(name = "device")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Device implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "famoco_id", nullable = false)
    private Integer famocoId;

    @NotNull
    @Column(name = "mac_address", nullable = false)
    private String macAddress;

    @NotNull
    @Column(name = "date_creation", nullable = false)
    private LocalDate dateCreation;

    @ManyToOne(optional = false)
    @NotNull
    private Retailer retailer;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Device id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getFamocoId() {
        return this.famocoId;
    }

    public Device famocoId(Integer famocoId) {
        this.setFamocoId(famocoId);
        return this;
    }

    public void setFamocoId(Integer famocoId) {
        this.famocoId = famocoId;
    }

    public String getMacAddress() {
        return this.macAddress;
    }

    public Device macAddress(String macAddress) {
        this.setMacAddress(macAddress);
        return this;
    }

    public void setMacAddress(String macAddress) {
        this.macAddress = macAddress;
    }

    public LocalDate getDateCreation() {
        return this.dateCreation;
    }

    public Device dateCreation(LocalDate dateCreation) {
        this.setDateCreation(dateCreation);
        return this;
    }

    public void setDateCreation(LocalDate dateCreation) {
        this.dateCreation = dateCreation;
    }

    public Retailer getRetailer() {
        return this.retailer;
    }

    public void setRetailer(Retailer retailer) {
        this.retailer = retailer;
    }

    public Device retailer(Retailer retailer) {
        this.setRetailer(retailer);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Device)) {
            return false;
        }
        return id != null && id.equals(((Device) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Device{" +
            "id=" + getId() +
            ", famocoId=" + getFamocoId() +
            ", macAddress='" + getMacAddress() + "'" +
            ", dateCreation='" + getDateCreation() + "'" +
            "}";
    }
}
