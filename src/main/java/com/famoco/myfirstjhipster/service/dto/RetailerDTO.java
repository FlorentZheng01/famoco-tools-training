package com.famoco.myfirstjhipster.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.famoco.myfirstjhipster.domain.Retailer} entity.
 */
public class RetailerDTO implements Serializable {

    private Long id;

    @NotNull
    private String name;

    @NotNull
    private Integer phoneNumber;

    @NotNull
    private String address;

    @NotNull
    private String mail;

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

    public Integer getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(Integer phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RetailerDTO)) {
            return false;
        }

        RetailerDTO retailerDTO = (RetailerDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, retailerDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "RetailerDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", phoneNumber=" + getPhoneNumber() +
            ", address='" + getAddress() + "'" +
            ", mail='" + getMail() + "'" +
            "}";
    }
}
