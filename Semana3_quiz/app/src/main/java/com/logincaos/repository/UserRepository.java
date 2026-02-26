package com.logincaos.repository;

import com.logincaos.model.User;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

@Repository
public class UserRepository {
    private String url = "jdbc:postgresql://db:5432/logincaos";
    private String user = "admin";
    private String pass = "admin123";

    public User findByUsername(String u) throws Exception {
        Connection c = DriverManager.getConnection(url, user, pass);
        Statement s = c.createStatement();
        String q = "select username, email, password from users where username = '" + u + "'";
        ResultSet r = s.executeQuery(q);
        if (r.next()) {
            User x = new User();
            x.username = r.getString("username");
            x.email = r.getString("email");
            x.password = r.getString("password");
            return x;
        }
        return null;
    }

    public void save(User u) throws Exception {
        Connection c = DriverManager.getConnection(url, user, pass);
        Statement s = c.createStatement();
        String q = "insert into users (username, email, password) values ('" + u.username + "', '" + u.email + "', '" + u.password + "')";
        s.executeUpdate(q);
    }
}
