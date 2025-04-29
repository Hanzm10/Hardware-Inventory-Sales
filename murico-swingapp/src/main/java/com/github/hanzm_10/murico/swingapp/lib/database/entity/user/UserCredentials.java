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

import org.jetbrains.annotations.NotNull;

public record UserCredentials(int _userId, String userEmail, String userPassword, String userFirstName, String userLastName, String userPhoneNumber) {
	public static class Builder{
		private int userId;
		private String userEmail;
		private String password;
		private String firstName;
		private String lastName;
		private String phoneNum;
		
		public @NotNull UserCredentials build() throws IllegalStateException{
			if (userId <= 0) {
                throw new IllegalStateException("User ID must be greater than 0");
                
            }
			return new UserCredentials(userId, userEmail, password, firstName, lastName, phoneNum);
		}
		public Builder setUserId(int userId) {
			this.userId = userId;
			return this;
		}
		public Builder setUserEmail(String userEmail) {
			this.userEmail = userEmail;
			return this;
		}
		public Builder setUserPassword(String password) {
			this.password = password;
			return this;
		}
		public Builder setFirstName(String firstName) {
			this.firstName = firstName;
			return this;
		}
		public Builder setLastName(String lastName) {
			this.lastName = lastName;
			return this;
		}
		public Builder setPhoneNum(String phoneNum) {
			this.phoneNum = phoneNum;
			return this;
		}
	}
}
