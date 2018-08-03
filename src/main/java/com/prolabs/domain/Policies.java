package com.prolabs.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;


/**
 * A Policies.
 */
@Entity
@Table(name = "policies")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "policies")
public class Policies implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "policy_name")
    private String policyName;

    @Column(name = "policy_description")
    private String policyDescription;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPolicyName() {
        return policyName;
    }

    public Policies policyName(String policyName) {
        this.policyName = policyName;
        return this;
    }

    public void setPolicyName(String policyName) {
        this.policyName = policyName;
    }

    public String getPolicyDescription() {
        return policyDescription;
    }

    public Policies policyDescription(String policyDescription) {
        this.policyDescription = policyDescription;
        return this;
    }

    public void setPolicyDescription(String policyDescription) {
        this.policyDescription = policyDescription;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Policies policies = (Policies) o;
        if (policies.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), policies.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Policies{" +
            "id=" + getId() +
            ", policyName='" + getPolicyName() + "'" +
            ", policyDescription='" + getPolicyDescription() + "'" +
            "}";
    }
}
