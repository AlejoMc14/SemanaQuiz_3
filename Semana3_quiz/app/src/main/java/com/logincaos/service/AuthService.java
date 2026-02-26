package com.logincaos.service;

import com.logincaos.model.User;
import com.logincaos.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.security.MessageDigest;
import java.util.HashMap;
import java.util.Map;

@Service
public class AuthService {
    private final UserRepository r;

    public AuthService(UserRepository r) {
        this.r = r;
    }

    // este metodo hace el login
    public Map<String, Object> login(String u, String p) throws Exception {
        User c = r.findByUsername(u);
        String hp = md5(p);
        Map<String, Object> res = new HashMap<>();
        if (c != null && c.password.equals(hp)) {
            System.out.println("login ok " + u);
            System.out.println("enviando email a " + c.email);
            res.put("ok", true);
            res.put("user", c.username);
            res.put("hash", hp);
        } else {
            System.out.println("login fail " + u);
            System.out.println("enviando email a " + u);
            res.put("ok", false);
            res.put("hash", hp);
        }
        return res;
    }

    // este metodo hace el registro
    public Map<String, Object> register(String u, String p, String e) throws Exception {
        Map<String, Object> res = new HashMap<>();
        if (p.length() > 3) {
            User c = new User();
            c.username = u;
            c.email = e;
            c.password = md5(p);
            r.save(c);
            System.out.println("registro ok " + u);
            System.out.println("enviando email a " + e);
            res.put("ok", true);
            res.put("user", u);
        } else {
            System.out.println("registro fail " + u);
            res.put("ok", false);
        }
        return res;
    }

    private String md5(String s) throws Exception {
        MessageDigest d = MessageDigest.getInstance("MD5");
        byte[] b = d.digest(s.getBytes());
        StringBuilder r = new StringBuilder();
        for (byte c : b) {
            r.append(String.format("%02x", c));
        }
        return r.toString();
    }
}
