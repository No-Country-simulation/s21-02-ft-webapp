package com.wallex.financial_platform.services.impl;

import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class JwtBlacklistService {

    private final Set<String> blacklist = ConcurrentHashMap.newKeySet();

    public void invalidateToken(String token) {
        blacklist.add(token); // Agrega el token a la lista negra
    }

    public boolean isTokenBlacklisted(String token) {
        return blacklist.contains(token); // Verifica si el token está en la lista negra
    }
}