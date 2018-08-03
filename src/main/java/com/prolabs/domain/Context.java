package com.prolabs.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;


/**
 * A Context.
 */
@Entity
@Table(name = "context")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "context")
public class Context implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "context_name")
    private String contextName;

    @Column(name = "context_description")
    private String contextDescription;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContextName() {
        return contextName;
    }

    public Context contextName(String contextName) {
        this.contextName = contextName;
        return this;
    }

    public void setContextName(String contextName) {
        this.contextName = contextName;
    }

    public String getContextDescription() {
        return contextDescription;
    }

    public Context contextDescription(String contextDescription) {
        this.contextDescription = contextDescription;
        return this;
    }

    public void setContextDescription(String contextDescription) {
        this.contextDescription = contextDescription;
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
        Context context = (Context) o;
        if (context.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), context.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Context{" +
            "id=" + getId() +
            ", contextName='" + getContextName() + "'" +
            ", contextDescription='" + getContextDescription() + "'" +
            "}";
    }
}
