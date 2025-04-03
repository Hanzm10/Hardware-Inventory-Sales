package com.murico.app.model.user;

/**
 * Enum representing different user roles in the application.
 * <p>
 * This enum defines various roles that a user can have, such as Admin, Supplier, Manager, Clerk,
 * Employee, Trainee, and ToBeAssigned.
 * </p>
 *
 * @author Aaron Ragudos
 * @version 1.0
 */
public enum UserRoles {
  Admin, Supplier, Manager, Clerk, Employee, Trainee, ToBeAssigned;

  /**
   * Converts a string representation of a user role to the corresponding UserRoles enum value.
   *
   * @param role The string representation of the user role.
   * @return The corresponding UserRoles enum value.
   * @throws IllegalArgumentException if the provided string does not match any UserRoles enum
   *         value.
   */
  public UserRoles fromString(String role) throws IllegalArgumentException {
    for (UserRoles userRole : UserRoles.values()) {
      if (userRole.name().equalsIgnoreCase(role)) {
        return userRole;
      }
    }

    throw new IllegalArgumentException(
        "No enum constant " + UserRoles.class.getCanonicalName() + "." + role);
  }
}
