package com.upc.matchpoint.iam.domain.model.entities;

import com.upc.matchpoint.iam.domain.model.valueobjects.Roles;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.With;

import java.util.List;

/**
 * Role entity
 * <p>
 *     This entity represents the role of a user in the system.
 *     It is used to define the permissions of a user.
 * </p>
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@With
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private Roles name;

    public Role(Roles name) {
        this.name = name;
    }

    /**
     * Get the name of the role as a string
     * @return the name of the role as a string
     */
    public String getStringName() {
        return name.name();
    }

    /**
     * Get the default role
     * @return the default role
     */
    public static Role getDefaultRole() {
        return new Role(Roles.ROLE_USER);
    }

    /**
     * Get the role from its name
     * @param name the name of the role
     * @return the role
     */
    public static Role toRoleFromName(String name) {
        if (name == null) throw new IllegalArgumentException("Role name cannot be null");
        var upper = name.trim().toUpperCase();
        // Try direct match with enum
        try {
            return new Role(Roles.valueOf(upper));
        } catch (IllegalArgumentException ignored) {
        }
        // Try with ROLE_ prefix
        try {
            var prefixed = upper.startsWith("ROLE_") ? upper : "ROLE_" + upper;
            return new Role(Roles.valueOf(prefixed));
        } catch (IllegalArgumentException ignored) {
        }
        // Heuristics: map common terms to existing roles
        if (upper.contains("ADMIN")) return new Role(Roles.ROLE_ADMIN);
        if (upper.contains("INSTRUCTOR")) return new Role(Roles.ROLE_INSTRUCTOR);
        if (upper.contains("USER")) return new Role(Roles.ROLE_USER);

        throw new IllegalArgumentException("Unknown role name: " + name);
    }

    /**
     * Validate the role set
     * <p>
     *     This method validates the role set and returns the default role if the set is empty.
     * </p>
     * @param roles the role set
     * @return the role set
     */
    public static List<Role> validateRoleSet(List<Role> roles) {
        if (roles == null || roles.isEmpty()) {
            return List.of(getDefaultRole());
        }
        return roles;
    }

}
