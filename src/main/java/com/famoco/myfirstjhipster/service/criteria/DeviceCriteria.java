package com.famoco.myfirstjhipster.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.BooleanFilter;
import tech.jhipster.service.filter.DoubleFilter;
import tech.jhipster.service.filter.Filter;
import tech.jhipster.service.filter.FloatFilter;
import tech.jhipster.service.filter.IntegerFilter;
import tech.jhipster.service.filter.LocalDateFilter;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link com.famoco.myfirstjhipster.domain.Device} entity. This class is used
 * in {@link com.famoco.myfirstjhipster.web.rest.DeviceResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /devices?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
public class DeviceCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private IntegerFilter famocoId;

    private StringFilter macAddress;

    private LocalDateFilter dateCreation;

    private LongFilter retailerId;

    private Boolean distinct;

    public DeviceCriteria() {}

    public DeviceCriteria(DeviceCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.famocoId = other.famocoId == null ? null : other.famocoId.copy();
        this.macAddress = other.macAddress == null ? null : other.macAddress.copy();
        this.dateCreation = other.dateCreation == null ? null : other.dateCreation.copy();
        this.retailerId = other.retailerId == null ? null : other.retailerId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public DeviceCriteria copy() {
        return new DeviceCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public LongFilter id() {
        if (id == null) {
            id = new LongFilter();
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public IntegerFilter getFamocoId() {
        return famocoId;
    }

    public IntegerFilter famocoId() {
        if (famocoId == null) {
            famocoId = new IntegerFilter();
        }
        return famocoId;
    }

    public void setFamocoId(IntegerFilter famocoId) {
        this.famocoId = famocoId;
    }

    public StringFilter getMacAddress() {
        return macAddress;
    }

    public StringFilter macAddress() {
        if (macAddress == null) {
            macAddress = new StringFilter();
        }
        return macAddress;
    }

    public void setMacAddress(StringFilter macAddress) {
        this.macAddress = macAddress;
    }

    public LocalDateFilter getDateCreation() {
        return dateCreation;
    }

    public LocalDateFilter dateCreation() {
        if (dateCreation == null) {
            dateCreation = new LocalDateFilter();
        }
        return dateCreation;
    }

    public void setDateCreation(LocalDateFilter dateCreation) {
        this.dateCreation = dateCreation;
    }

    public LongFilter getRetailerId() {
        return retailerId;
    }

    public LongFilter retailerId() {
        if (retailerId == null) {
            retailerId = new LongFilter();
        }
        return retailerId;
    }

    public void setRetailerId(LongFilter retailerId) {
        this.retailerId = retailerId;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final DeviceCriteria that = (DeviceCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(famocoId, that.famocoId) &&
            Objects.equals(macAddress, that.macAddress) &&
            Objects.equals(dateCreation, that.dateCreation) &&
            Objects.equals(retailerId, that.retailerId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, famocoId, macAddress, dateCreation, retailerId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DeviceCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (famocoId != null ? "famocoId=" + famocoId + ", " : "") +
            (macAddress != null ? "macAddress=" + macAddress + ", " : "") +
            (dateCreation != null ? "dateCreation=" + dateCreation + ", " : "") +
            (retailerId != null ? "retailerId=" + retailerId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
