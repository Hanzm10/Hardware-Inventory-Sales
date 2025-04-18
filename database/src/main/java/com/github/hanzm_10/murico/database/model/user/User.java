/** Copyright 2025
 *  - Aaron Ragudos
 *  - Hanz Mapua
 *  - Peter Dela Cruz
 *  - Jerick Remo
 *  - Kurt Raneses
 *
 *  Permission is hereby granted, free of charge, to any
 *  person obtaining a copy of this software and associated
 *  documentation files (the “Software”), to deal in the Software
 *  without restriction, including without limitation the rights
 *  to use, copy, modify, merge, publish, distribute, sublicense,
 *  and/or sell copies of the Software, and to permit persons
 *  to whom the Software is furnished to do so, subject to the
 *  following conditions:
 *
 *  The above copyright notice and this permission notice shall be
 *  included in all copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED “AS IS”, WITHOUT WARRANTY OF ANY KIND,
 *  EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES
 *  OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 *  IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR
 *  ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF
 *  CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
 *  WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package com.github.hanzm_10.murico.database.model.user;

import java.sql.Timestamp;

import org.jetbrains.annotations.NotNull;

/**
 * Represents a user in the application.
 *
 * <p>
 * This class includes the user's ID, creation time, display name and image,
 * gender, and role.
 *
 * @param _userId
 *            The unique identifier of the user.
 * @param _userCreatedAt
 *            The timestamp when the user was created.
 * @param userDisplayName
 *            The user's display name.
 * @param userDisplayImage
 *            The URL or path to the user's display image.
 * @param userGender
 *            The user's gender.
 * @param userRole
 *            The user's role in the system.
 */
public record User(int _userId, Timestamp _userCreatedAt, String userDisplayName, String userDisplayImage,
		UserGender userGender, UserRole userRole) {
	/**
	 * Builder class for constructing a {@link User} instance with validation and
	 * chaining support.
	 */
	public static class Builder {
		private int userId;
		private Timestamp userCreatedAt;
		private String userDisplayName;
		private String userDisplayImage;
		private UserGender userGender;
		private UserRole userRole;

		/**
		 * Builds and returns the {@link User} object after validating required fields.
		 *
		 * @return a fully constructed and valid {@link User} instance
		 * @throws IllegalStateException
		 *             if any required field is invalid or missing
		 */
		public @NotNull User build() throws IllegalStateException {
			if (userId <= 0) {
				throw new IllegalStateException("User ID must be greater than 0");
			}
			if (userCreatedAt == null) {
				throw new IllegalStateException("User creation time cannot be null");
			}
			if (userDisplayName == null || userDisplayName.isBlank()) {
				throw new IllegalStateException("Display name cannot be null or empty");
			}
			if (userGender == null) {
				throw new IllegalStateException("User gender cannot be null");
			}
			if (userRole == null) {
				throw new IllegalStateException("User role cannot be null");
			}

			return new User(userId, userCreatedAt, userDisplayName, userDisplayImage, userGender, userRole);
		}

		public Builder setUserCreatedAt(Timestamp userCreatedAt) {
			this.userCreatedAt = userCreatedAt;
			return this;
		}

		public Builder setUserDisplayImage(String userDisplayImage) {
			this.userDisplayImage = userDisplayImage;
			return this;
		}

		public Builder setUserDisplayName(String userDisplayName) {
			this.userDisplayName = userDisplayName;
			return this;
		}

		public Builder setUserGender(UserGender userGender) {
			this.userGender = userGender;
			return this;
		}

		public Builder setUserId(int userId) {
			this.userId = userId;
			return this;
		}

		public Builder setUserRole(UserRole userRole) {
			this.userRole = userRole;
			return this;
		}
	}
}
