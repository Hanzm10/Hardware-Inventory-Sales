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

import java.util.Locale;

public enum UserRole {
    ADMIN, SUPPLIER, MANAGER, CLERK, EMPLOYEE, TRAINEE, TO_BE_ASSIGNED;

    /**
     * Returns the UserRole corresponding to the given string.
     *
     * @param role
     *            the string representation of the UserRole
     * @return the UserRole corresponding to the given string
     * @throws IllegalArgumentException
     *             if the string does not match any UserRole
     */
    public static UserRole fromString(String role) throws IllegalArgumentException {
        return switch (role.toUpperCase(Locale.ENGLISH)) {
            case "ADMIN" -> ADMIN;
            case "SUPPLIER" -> SUPPLIER;
            case "MANAGER" -> MANAGER;
            case "CLERK" -> CLERK;
            case "EMPLOYEE" -> EMPLOYEE;
            case "TRAINEE" -> TRAINEE;
            case "TO_BE_ASSIGNED" -> TO_BE_ASSIGNED;
            default -> throw new IllegalArgumentException("Invalid role: " + role);
        };
    }

    @Override
    public String toString() {
        return switch (this) {
            case ADMIN -> "admin";
            case SUPPLIER -> "supplier";
            case MANAGER -> "manager";
            case CLERK -> "clerk";
            case EMPLOYEE -> "employee";
            case TRAINEE -> "trainee";
            case TO_BE_ASSIGNED -> "to_be_assigned";
        };
    }
}
