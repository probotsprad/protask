package com.prolabs.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A PermissionMst.
 */
@Entity
@Table(name = "permission_mst")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "permissionmst")
public class PermissionMst implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(max = 300)
    @Column(name = "prm_name", length = 300, nullable = false)
    private String prmName;

    @Size(max = 500)
    @Column(name = "prm_desc", length = 500)
    private String prmDesc;

    @Column(name = "created_on")
    private ZonedDateTime createdOn;

    @Size(max = 100)
    @Column(name = "created_by", length = 100)
    private String createdBy;

    @Column(name = "updated_on")
    private ZonedDateTime updatedOn;

    @Size(max = 100)
    @Column(name = "updated_by", length = 100)
    private String updatedBy;

    // jhipster-needle-entity-add-field - Jhipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPrmName() {
        return prmName;
    }

    public PermissionMst prmName(String prmName) {
        this.prmName = prmName;
        return this;
    }

    public void setPrmName(String prmName) {
        this.prmName = prmName;
    }

    public String getPrmDesc() {
        return prmDesc;
    }

    public PermissionMst prmDesc(String prmDesc) {
        this.prmDesc = prmDesc;
        return this;
    }

    public void setPrmDesc(String prmDesc) {
        this.prmDesc = prmDesc;
    }

    public ZonedDateTime getCreatedOn() {
        return createdOn;
    }

    public PermissionMst createdOn(ZonedDateTime createdOn) {
        this.createdOn = createdOn;
        return this;
    }

    public void setCreatedOn(ZonedDateTime createdOn) {
        this.createdOn = createdOn;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public PermissionMst createdBy(String createdBy) {
        this.createdBy = createdBy;
        return this;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public ZonedDateTime getUpdatedOn() {
        return updatedOn;
    }

    public PermissionMst updatedOn(ZonedDateTime updatedOn) {
        this.updatedOn = updatedOn;
        return this;
    }

    public void setUpdatedOn(ZonedDateTime updatedOn) {
        this.updatedOn = updatedOn;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public PermissionMst updatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
        return this;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }
    // jhipster-needle-entity-add-getters-setters - Jhipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        PermissionMst permissionMst = (PermissionMst) o;
        if (permissionMst.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), permissionMst.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "PermissionMst{" +
            "id=" + getId() +
            ", prmName='" + getPrmName() + "'" +
            ", prmDesc='" + getPrmDesc() + "'" +
            ", createdOn='" + getCreatedOn() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", updatedOn='" + getUpdatedOn() + "'" +
            ", updatedBy='" + getUpdatedBy() + "'" +
            "}";
    }
}
