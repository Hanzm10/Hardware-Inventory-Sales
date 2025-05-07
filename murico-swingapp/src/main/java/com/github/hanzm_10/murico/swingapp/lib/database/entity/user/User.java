/** 
 *  Copyright 2025 Aaron Ragudos, Hanz Mapua, Peter Dela Cruz, Jerick Remo, Kurt Raneses, and the contributors of the project.
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the “Software”),
 *  to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense,
 *  and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED “AS IS”, WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 *  WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package com.github.hanzm_10.murico.swingapp.lib.database.entity.user;

import java.sql.Timestamp;

/**
 * Represents a user in the system. Contains both profile information and
 * metadata such as creation time.
 *
 * @param _userId
 *            Unique identifier for the user (primary key).
 * @param _createdAt
 *            Timestamp when the user was created.
 * @param updatedAt
 *            Timestamp of the last update to the user profile.
 * @param displayName
 *            The display name shown publicly (e.g., "JohnDoe42").
 * @param displayImage
 *            URL or path to the user's profile image.
 * @param gender
 *            The user's gender (enum).
 * @param firstName
 *            The user's first name.
 * @param lastName
 *            The user's last name.
 * @param biography
 *            A short personal biography or description.
 */
public record User(int _userId, Timestamp _createdAt, Timestamp updatedAt, String displayName, String displayImage,
		UserGender gender, String firstName, String lastName, String biography) {
	public static int MINIMUM_USERNAME_LENGTH = 3;
	public static int MAXIMUM_USERNAME_LENGTH = 50;
	public static int MINIMUM_FIRSTNAME_LENGTH = 3;
	public static int MAXIMUM_FIRSTNAME_LENGTH = 100;
	public static int MINIMUM_LASTNAME_LENGTH = 3;
	public static int MAXIMUM_LASTNAME_LENGTH = 100;
	public static int MAXIMUM_BIOGRAPHY_LENGTH = 255;
}
