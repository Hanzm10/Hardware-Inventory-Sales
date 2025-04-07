package com.murico.app.dal.User;

import com.murico.app.dal.database.mysql.DatabaseManagerMySQL;

public class UserDAL {
  public static String getTableName() {
    return "users";
  }

  /*
   * Create a new user in the database and return the generatead user ID.
   * 
   * @return The generated user ID.
   */
  public static String createUser() {
    try {
      var conn = DatabaseManagerMySQL.getInstance().connect();
      var sql = "INSERT INTO users DEFAULT VALUES RETURNING _user_id;";
      var preparedStatement = conn.prepareStatement(sql);

      var resultSet = preparedStatement.executeQuery();

      if (!resultSet.next()) {
        throw new Exception("Failed to create user");
      }

      var userId = resultSet.getString("_user_id");

      preparedStatement.close();
      DatabaseManagerMySQL.getInstance().disconnect(conn);

      return userId;
    } catch (Exception e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

    return null;
  }
}
