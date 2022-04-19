package com.famoco.myfirstjhipster.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.LocalDate;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.domain.Persistable;

/**
 * A ODF.
 */
@Entity
@Table(name = "odf")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class ODF implements Serializable, Persistable<String> {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "status")
    private String status;

    @Column(name = "operation_name")
    private String operationName;

    @Column(name = "dateofcreation")
    private LocalDate dateofcreation;

    @Column(name = "dateofmodification")
    private LocalDate dateofmodification;

    @Column(name = "lastmodificationuser")
    private String lastmodificationuser;

    @Transient
    private boolean isPersisted;

    @JsonIgnoreProperties(value = { "country", "oDF" }, allowSetters = true)
    @OneToOne
    @JoinColumn(unique = true)
    private People people;

    @JsonIgnoreProperties(value = { "oDF" }, allowSetters = true)
    @OneToOne
    @JoinColumn(unique = true)
    private JobHistory jobHistory;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public String getId() {
        return this.id;
    }

    public ODF id(String id) {
        this.setId(id);
        return this;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStatus() {
        return this.status;
    }

    public ODF status(String status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getOperationName() {
        return this.operationName;
    }

    public ODF operationName(String operationName) {
        this.setOperationName(operationName);
        return this;
    }

    public void setOperationName(String operationName) {
        this.operationName = operationName;
    }

    public LocalDate getDateofcreation() {
        return this.dateofcreation;
    }

    public ODF dateofcreation(LocalDate dateofcreation) {
        this.setDateofcreation(dateofcreation);
        return this;
    }

    public void setDateofcreation(LocalDate dateofcreation) {
        this.dateofcreation = dateofcreation;
    }

    public LocalDate getDateofmodification() {
        return this.dateofmodification;
    }

    public ODF dateofmodification(LocalDate dateofmodification) {
        this.setDateofmodification(dateofmodification);
        return this;
    }

    public void setDateofmodification(LocalDate dateofmodification) {
        this.dateofmodification = dateofmodification;
    }

    public String getLastmodificationuser() {
        return this.lastmodificationuser;
    }

    public ODF lastmodificationuser(String lastmodificationuser) {
        this.setLastmodificationuser(lastmodificationuser);
        return this;
    }

    public void setLastmodificationuser(String lastmodificationuser) {
        this.lastmodificationuser = lastmodificationuser;
    }

    @Transient
    @Override
    public boolean isNew() {
        return !this.isPersisted;
    }

    public ODF setIsPersisted() {
        this.isPersisted = true;
        return this;
    }

    @PostLoad
    @PostPersist
    public void updateEntityState() {
        this.setIsPersisted();
    }

    public People getPeople() {
        return this.people;
    }

    public void setPeople(People people) {
        this.people = people;
    }

    public ODF people(People people) {
        this.setPeople(people);
        return this;
    }

    public JobHistory getJobHistory() {
        return this.jobHistory;
    }

    public void setJobHistory(JobHistory jobHistory) {
        this.jobHistory = jobHistory;
    }

    public ODF jobHistory(JobHistory jobHistory) {
        this.setJobHistory(jobHistory);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ODF)) {
            return false;
        }
        return id != null && id.equals(((ODF) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ODF{" +
            "id=" + getId() +
            ", status='" + getStatus() + "'" +
            ", operationName='" + getOperationName() + "'" +
            ", dateofcreation='" + getDateofcreation() + "'" +
            ", dateofmodification='" + getDateofmodification() + "'" +
            ", lastmodificationuser='" + getLastmodificationuser() + "'" +
            "}";
    }
}
