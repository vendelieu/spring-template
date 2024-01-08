/*
 * This file is generated by jOOQ.
 */
package eu.vendeli.jooq.generated.tables.pojos;


import eu.vendeli.jooq.generated.tables.interfaces.IUser;

import java.time.LocalDateTime;

import javax.annotation.processing.Generated;


/**
 * This class is generated by jOOQ.
 */
@Generated(
    value = {
        "https://www.jooq.org",
        "jOOQ version:3.19.1",
        "schema version:1"
    },
    comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class User implements IUser {

    private static final long serialVersionUID = 1L;

    private Long id;
    private String username;
    private String name;
    private String email;
    private LocalDateTime createdAt;

    public User() {}

    public User(IUser value) {
        this.id = value.getId();
        this.username = value.getUsername();
        this.name = value.getName();
        this.email = value.getEmail();
        this.createdAt = value.getCreatedAt();
    }

    public User(
        Long id,
        String username,
        String name,
        String email,
        LocalDateTime createdAt
    ) {
        this.id = id;
        this.username = username;
        this.name = name;
        this.email = email;
        this.createdAt = createdAt;
    }

    /**
     * Getter for <code>exm.user.id</code>.
     */
    @Override
    public Long getId() {
        return this.id;
    }

    /**
     * Setter for <code>exm.user.id</code>.
     */
    @Override
    public User setId(Long id) {
        this.id = id;
        return this;
    }

    /**
     * Getter for <code>exm.user.username</code>.
     */
    @Override
    public String getUsername() {
        return this.username;
    }

    /**
     * Setter for <code>exm.user.username</code>.
     */
    @Override
    public User setUsername(String username) {
        this.username = username;
        return this;
    }

    /**
     * Getter for <code>exm.user.name</code>.
     */
    @Override
    public String getName() {
        return this.name;
    }

    /**
     * Setter for <code>exm.user.name</code>.
     */
    @Override
    public User setName(String name) {
        this.name = name;
        return this;
    }

    /**
     * Getter for <code>exm.user.email</code>.
     */
    @Override
    public String getEmail() {
        return this.email;
    }

    /**
     * Setter for <code>exm.user.email</code>.
     */
    @Override
    public User setEmail(String email) {
        this.email = email;
        return this;
    }

    /**
     * Getter for <code>exm.user.created_at</code>.
     */
    @Override
    public LocalDateTime getCreatedAt() {
        return this.createdAt;
    }

    /**
     * Setter for <code>exm.user.created_at</code>.
     */
    @Override
    public User setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final User other = (User) obj;
        if (this.id == null) {
            if (other.id != null)
                return false;
        }
        else if (!this.id.equals(other.id))
            return false;
        if (this.username == null) {
            if (other.username != null)
                return false;
        }
        else if (!this.username.equals(other.username))
            return false;
        if (this.name == null) {
            if (other.name != null)
                return false;
        }
        else if (!this.name.equals(other.name))
            return false;
        if (this.email == null) {
            if (other.email != null)
                return false;
        }
        else if (!this.email.equals(other.email))
            return false;
        if (this.createdAt == null) {
            if (other.createdAt != null)
                return false;
        }
        else if (!this.createdAt.equals(other.createdAt))
            return false;
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((this.id == null) ? 0 : this.id.hashCode());
        result = prime * result + ((this.username == null) ? 0 : this.username.hashCode());
        result = prime * result + ((this.name == null) ? 0 : this.name.hashCode());
        result = prime * result + ((this.email == null) ? 0 : this.email.hashCode());
        result = prime * result + ((this.createdAt == null) ? 0 : this.createdAt.hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("User (");

        sb.append(id);
        sb.append(", ").append(username);
        sb.append(", ").append(name);
        sb.append(", ").append(email);
        sb.append(", ").append(createdAt);

        sb.append(")");
        return sb.toString();
    }

    // -------------------------------------------------------------------------
    // FROM and INTO
    // -------------------------------------------------------------------------

    @Override
    public void from(IUser from) {
        setId(from.getId());
        setUsername(from.getUsername());
        setName(from.getName());
        setEmail(from.getEmail());
        setCreatedAt(from.getCreatedAt());
    }

    @Override
    public <E extends IUser> E into(E into) {
        into.from(this);
        return into;
    }
}
