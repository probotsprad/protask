package com.prolabs.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Objects;

/**
 * A RoleMst.
 */
@Entity
@Table(name = "role_mst")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "rolemst")
public class RoleMst implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(max = 300)
    @Column(name = "role_name", length = 300, nullable = false)
    private String roleName;

    @Size(max = 500)
    @Column(name = "role_desc", length = 500)
    private String roleDesc;

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
    
    @ManyToMany(cascade=CascadeType.MERGE, fetch = FetchType.EAGER) //
    @JoinTable(
            name="ROLE_PERMISSION",
            joinColumns={@JoinColumn(name="ROLE_ID", referencedColumnName="ID")},
            inverseJoinColumns={@JoinColumn(name="PRM_ID", referencedColumnName="ID")})
    private List<PermissionMst> userAccessLevels;

    // jhipster-needle-entity-add-field - Jhipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRoleName() {
        return roleName;
    }

    public RoleMst roleName(String roleName) {
        this.roleName = roleName;
        return this;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getRoleDesc() {
        return roleDesc;
    }

    public RoleMst roleDesc(String roleDesc) {
        this.roleDesc = roleDesc;
        return this;
    }

    public void setRoleDesc(String roleDesc) {
        this.roleDesc = roleDesc;
    }

    public ZonedDateTime getCreatedOn() {
        return createdOn;
    }

    public RoleMst createdOn(ZonedDateTime createdOn) {
        this.createdOn = createdOn;
        return this;
    }

    public void setCreatedOn(ZonedDateTime createdOn) {
        this.createdOn = createdOn;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public RoleMst createdBy(String createdBy) {
        this.createdBy = createdBy;
        return this;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public ZonedDateTime getUpdatedOn() {
        return updatedOn;
    }

    public RoleMst updatedOn(ZonedDateTime updatedOn) {
        this.updatedOn = updatedOn;
        return this;
    }

    public void setUpdatedOn(ZonedDateTime updatedOn) {
        this.updatedOn = updatedOn;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public RoleMst updatedBy(String updatedBy) {
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
        RoleMst roleMst = (RoleMst) o;
        if (roleMst.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), roleMst.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "RoleMst{" +
            "id=" + getId() +
            ", roleName='" + getRoleName() + "'" +
            ", roleDesc='" + getRoleDesc() + "'" +
            ", createdOn='" + getCreatedOn() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", updatedOn='" + getUpdatedOn() + "'" +
            ", updatedBy='" + getUpdatedBy() + "'" +
            "}";
    }

	public List<PermissionMst> getUserAccessLevels() {
		return userAccessLevels;
	}

	public void setUserAccessLevels(List<PermissionMst> userAccessLevels) {
		this.userAccessLevels = userAccessLevels;
	}
    
    
}
