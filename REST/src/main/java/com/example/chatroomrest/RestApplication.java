package com.example.chatroomrest;

import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;

@ApplicationPath("/api")
public class RestApplication extends Application {
    // Vide, mais l'annotation est OBLIGATOIRE
}