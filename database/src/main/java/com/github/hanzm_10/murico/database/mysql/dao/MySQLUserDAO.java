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
package com.github.hanzm_10.murico.database.mysql.dao;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

import com.github.hanzm_10.murico.database.dao.UserDAO;
import com.github.hanzm_10.murico.database.model.user.User;

public class MySQLUserDAO implements UserDAO {

	@Override
	public boolean deleteUser() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public User getUserByDisplayName(@NotNull String _userDisplayName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public User getUserByEmail(@NotNull String _userEmail) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public User getUserById(@Range(from = 0, to = 2147483647) int _userId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int insertUser() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean userExists(@Range(from = 0, to = 2147483647) int _userId) {
		// TODO Auto-generated method stub
		return false;
	}
}
