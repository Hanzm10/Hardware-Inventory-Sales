package com.github.hanzm_10.murico.swingapp.lib.database.entity.user;

import java.sql.Timestamp;

import org.jetbrains.annotations.NotNull;

/**
 *
 * @author Aaron Ragudos
 *
 * @param _userId            The user ID
 * @param _createdAt         The date and time when the user was created
 * @param updatedAt          The date and time when the user was last updated
 * @param displayName        The display name of the user
 * @param displayImage
 * @param gender
 * @param firstName          The first name of the user
 * @param lastName           The last name of the user
 * @param biography          The biography of the user
 * @param roles              The roles of the user
 * @param email              The email of the user
 * @param verificationStatus The verification status of the user
 * @param verifiedAt         The date and time when the user was verified
 *
 */
public record UserMetadata(@NotNull int _userId, @NotNull Timestamp _createdAt, @NotNull Timestamp updatedAt,
		@NotNull String displayName, @NotNull String displayImage, @NotNull UserGender gender,
		@NotNull String firstName, @NotNull String lastName, @NotNull String biography, @NotNull String roles,
		@NotNull String email, @NotNull boolean verificationStatus, @NotNull Timestamp verifiedAt) {

}
