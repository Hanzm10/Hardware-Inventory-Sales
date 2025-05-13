package com.github.hanzm_10.murico.swingapp.lib.database.entity.user;

import java.sql.Timestamp;

import org.jetbrains.annotations.NotNull;

public record UserMetadata(@NotNull int _userId, @NotNull Timestamp _createdAt, @NotNull Timestamp updatedAt,
		@NotNull String displayName, @NotNull String displayImage, @NotNull UserGender gender,
		@NotNull String firstName, @NotNull String lastName, @NotNull String biography, @NotNull String roles,
		@NotNull String email, @NotNull boolean verificationStatus, @NotNull Timestamp verifiedAt) {

}
