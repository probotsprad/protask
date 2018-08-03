package com.prolabs.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import com.prolabs.domain.enumeration.YesNoFlag;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Objects;


/**
 * A UserMst.
 */
@Entity
@Table(name = "user_mst")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "usermst")
/*@SqlResultSetMappings({ @SqlResultSetMapping(name = "userProfile", classes = { @ConstructorResult(targetClass = UserMst.class, columns = {
		@ColumnResult(name = "id", type = Long.class),
		@ColumnResult(name = "user_id", type = String.class),
		@ColumnResult(name = "user_email", type = String.class),
		@ColumnResult(name = "first_name", type = String.class),
		@ColumnResult(name = "last_name", type = String.class),
		@ColumnResult(name = "name", type = String.class),
		@ColumnResult(name = "active", type = String.class),
		@ColumnResult(name = "profile_img_url", type = String.class)
		}) 
	})
})*/
public class UserMst implements Serializable {

	public UserMst() {

	}

	public UserMst(Long id, String userId, String userEmail, String firstName,
			String lastName, String fullName, String active, String profileImgUrl) {
		super();
		this.id = id;
		this.userId = userId;
		this.userEmail = userEmail;
		this.firstName = firstName;
		this.lastName = lastName;
		this.fullName = fullName;
		this.active = YesNoFlag.valueOf(active);
		this.profileImgUrl = profileImgUrl;
	}


	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotNull
	@Size(max = 300)
	@Column(name = "user_id", length = 300, nullable = false)
	private String userId;

	@NotNull
	@Size(max = 500)
	@Column(name = "user_email", length = 500, nullable = false)
	private String userEmail;

	@NotNull
	@Size(max = 200)
	@Column(name = "first_name", length = 200, nullable = false)
	private String firstName;

	@NotNull
	@Size(max = 200)
	@Column(name = "last_name", length = 200, nullable = false)
	private String lastName;

	@Enumerated(EnumType.STRING)
	@Column(name = "active")
	private YesNoFlag active;

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

	@ManyToMany(fetch = FetchType.EAGER)
	// cascade=CascadeType.MERGE,
	@JoinTable(name = "USER_ROLE", joinColumns = { @JoinColumn(name = "user_id", referencedColumnName = "user_id") }, inverseJoinColumns = { @JoinColumn(name = "role_id", referencedColumnName = "id") })
	private List<RoleMst> userRoles;
	
	
	
	@Transient
	private String fullName;
	
	@Transient
	private String profileImgUrl;
	
	

	// jhipster-needle-entity-add-field - Jhipster will add fields here, do not
	// remove
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUserId() {
		return userId;
	}

	public UserMst userId(String userId) {
		this.userId = userId;
		return this;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserEmail() {
		return userEmail;
	}

	public UserMst userEmail(String userEmail) {
		this.userEmail = userEmail;
		return this;
	}

	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}

	public String getFirstName() {
		return firstName;
	}

	public UserMst firstName(String firstName) {
		this.firstName = firstName;
		return this;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public UserMst lastName(String lastName) {
		this.lastName = lastName;
		return this;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public YesNoFlag getActive() {
		return active;
	}

	public UserMst active(YesNoFlag active) {
		this.active = active;
		return this;
	}

	public void setActive(YesNoFlag active) {
		this.active = active;
	}

	public ZonedDateTime getCreatedOn() {
		return createdOn;
	}

	public UserMst createdOn(ZonedDateTime createdOn) {
		this.createdOn = createdOn;
		return this;
	}

	public void setCreatedOn(ZonedDateTime createdOn) {
		this.createdOn = createdOn;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public UserMst createdBy(String createdBy) {
		this.createdBy = createdBy;
		return this;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public ZonedDateTime getUpdatedOn() {
		return updatedOn;
	}

	public UserMst updatedOn(ZonedDateTime updatedOn) {
		this.updatedOn = updatedOn;
		return this;
	}

	public void setUpdatedOn(ZonedDateTime updatedOn) {
		this.updatedOn = updatedOn;
	}

	public String getUpdatedBy() {
		return updatedBy;
	}

	public UserMst updatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
		return this;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	// jhipster-needle-entity-add-getters-setters - Jhipster will add getters
	// and setters here, do not remove

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		UserMst userMst = (UserMst) o;
		if (userMst.getId() == null || getId() == null) {
			return false;
		}
		return Objects.equals(getId(), userMst.getId());
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(getId());
	}

	@Override
	public String toString() {
		return "UserMst{" + "id=" + getId() + ", userId='" + getUserId() + "'"
				+ ", userEmail='" + getUserEmail() + "'" + ", firstName='"
				+ getFirstName() + "'" + ", lastName='" + getLastName() + "'"
				+ ", active='" + getActive() + "'" + ", createdOn='"
				+ getCreatedOn() + "'" + ", createdBy='" + getCreatedBy() + "'"
				+ ", updatedOn='" + getUpdatedOn() + "'" + ", updatedBy='"
				+ getUpdatedBy() + "'" + "}";
	}

	public List<RoleMst> getUserRoles() {
		return userRoles;
	}

	public void setUserRoles(List<RoleMst> userRoles) {
		this.userRoles = userRoles;
	}

	public String getProfileImgUrl() {
		return profileImgUrl;
	}

	public void setProfileImgUrl(String profileImgUrl) {
		this.profileImgUrl = profileImgUrl;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

}
