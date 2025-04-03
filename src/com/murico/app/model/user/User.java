package com.murico.app.model.user;

import java.sql.Timestamp;

public record User(
    int _userId,
    Timestamp _userCreatedAt,
    String userDisplayImage,
    UserGenders userGender,
    UserRoles userRole
    ) {
      public static class UserBuilder {
        private int _userId;
        private Timestamp _userCreatedAt;
        private String userDisplayImage;
        private UserGenders userGender;
        private UserRoles userRole;

        public UserBuilder setUserId(int _userId) {
          assert _userId > 0 : "User ID must be greater than 0";

          this._userId = _userId;
          return this;
        }

        public UserBuilder setUserCreatedAt(Timestamp _userCreatedAt) {
          assert _userCreatedAt != null : "User created at timestamp cannot be null";

          this._userCreatedAt = _userCreatedAt;
          return this;
        }

        public UserBuilder setUserDisplayImage(String userDisplayImage) {
          this.userDisplayImage = userDisplayImage;
          return this;
        }

        public UserBuilder setUserGender(UserGenders userGender) {
          this.userGender = userGender;
          return this;
        }

        public UserBuilder setUserRole(UserRoles userRole) {
          this.userRole = userRole;
          return this;
        }

        public User build() {
          assert _userId > 0 : "User ID must be greater than 0";
          assert _userCreatedAt != null : "User created at timestamp cannot be null";

          return new User(_userId, _userCreatedAt, userDisplayImage, userGender, userRole);
        }
      }
}
