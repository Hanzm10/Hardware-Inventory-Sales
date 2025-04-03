package com.murico.app.model.user;

public enum UserGenders {
  Male, Female, Unknown;

  public static UserGenders fromString(String gender) {
    for (UserGenders userGender : UserGenders.values()) {
      if (userGender.name().equalsIgnoreCase(gender)) {
        return userGender;
      }
    }

    throw new IllegalArgumentException(
        "No enum constant " + UserGenders.class.getCanonicalName() + "." + gender);
  }
}
