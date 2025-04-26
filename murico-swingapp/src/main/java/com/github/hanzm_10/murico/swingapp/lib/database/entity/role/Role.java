package com.github.hanzm_10.murico.swingapp.lib.database.entity.role;

import java.sql.Timestamp;

public record Role(int _roleId, Timestamp _roleCreatedAt, String roleName, String roleDescription) {

    public static class Builder {
        private int roleId;
        private Timestamp roleCreatedAt;
        private String roleName;
        private String roleDescription;

        public Role build() {
            return new Role(roleId, roleCreatedAt, roleName, roleDescription);
        }

        public Builder setRoleId(int roleId) throws IllegalArgumentException {
            if (roleId <= 0) {
                throw new IllegalArgumentException("Role ID must be greater than 0");
            }
            this.roleId = roleId;
            return this;
        }

        public Builder setRoleCreatedAt(Timestamp roleCreatedAt) {
            this.roleCreatedAt = roleCreatedAt;
            return this;
        }

        public Builder setRoleName(String roleName) {
            this.roleName = roleName;
            return this;
        }

        public Builder setRoleDescription(String roleDescription) {
            this.roleDescription = roleDescription;
            return this;
        }
    }
}
