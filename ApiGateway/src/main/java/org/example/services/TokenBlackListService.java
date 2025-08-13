package org.example.services;

import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Service
public class TokenBlackListService {
    private final ConcurrentMap<String, Date> blacklist = new ConcurrentHashMap<>();

    public void blacklistToken(String token, Date expiration) {
        blacklist.put(token, expiration);
    }

    public boolean isBlacklisted(String token) {
        Date exp = blacklist.get(token);
        if (exp == null) return false;
        if (exp.before(new Date())) {
            blacklist.remove(token);
            return false;
        }
        return true;
    }
}
