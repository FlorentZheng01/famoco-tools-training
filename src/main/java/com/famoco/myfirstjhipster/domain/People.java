package com.famoco.myfirstjhipster.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * The Employee entity.
 */
@Schema(description = "The Employee entity.")
@Entity
@Table(name = "people")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class People implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    /**
     * The firstname attribute.
     */
    @Schema(description = "The firstname attribute.")
    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "email")
    private String email;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "peoplename")
    private String peoplename;

    @JsonIgnoreProperties(value = { "people", "personne" }, allowSetters = true)
    @OneToOne(mappedBy = "people")
    private Country country;

    @JsonIgnoreProperties(value = { "people", "jobHistory" }, allowSetters = true)
    @OneToOne(mappedBy = "people")
    private ODF oDF;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public People id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public People firstName(String firstName) {
        this.setFirstName(firstName);
        return this;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return this.lastName;
    }

    public People lastName(String lastName) {
        this.setLastName(lastName);
        return this;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return this.email;
    }

    public People email(String email) {
        this.setEmail(email);
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return this.phoneNumber;
    }

    public People phoneNumber(String phoneNumber) {
        this.setPhoneNumber(phoneNumber);
        return this;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPeoplename() {
        return this.peoplename;
    }

    public People peoplename(String peoplename) {
        this.setPeoplename(peoplename);
        return this;
    }

    public void setPeoplename(String peoplename) {
        this.peoplename = peoplename;
    }

    public Country getCountry() {
        return this.country;
    }

    public void setCountry(Country country) {
        if (this.country != null) {
            this.country.setPeople(null);
        }
        if (country != null) {
            country.setPeople(this);
        }
        this.country = country;
    }

    public People country(Country country) {
        this.setCountry(country);
        return this;
    }

    public ODF getODF() {
        return this.oDF;
    }

    public void setODF(ODF oDF) {
        if (this.oDF != null) {
            this.oDF.setPeople(null);
        }
        if (oDF != null) {
            oDF.setPeople(this);
        }
        this.oDF = oDF;
    }

    public People oDF(ODF oDF) {
        this.setODF(oDF);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof People)) {
            return false;
        }
        return id != null && id.equals(((People) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "People{" +
            "id=" + getId() +
            ", firstName='" + getFirstName() + "'" +
            ", lastName='" + getLastName() + "'" +
            ", email='" + getEmail() + "'" +
            ", phoneNumber='" + getPhoneNumber() + "'" +
            ", peoplename='" + getPeoplename() + "'" +
            "}";
    }
}
