package com.example.chatroomrest;

import java.net.URI;
import java.net.http.*;
import java.util.Scanner;

public class ChatClientRest {
    // URL CORRIGÉE POUR CORRESPONDRE À TON TOMCAT
    private static final String BASE_URL = "http://localhost:8080/ChatroomREST_war_exploded/api/messages";
    private static int lastMsgCount = 0;

    public static void main(String[] args) throws Exception {
        Scanner sc = new Scanner(System.in);
        HttpClient client = HttpClient.newHttpClient();

        System.out.print("Pseudo REST : ");
        String pseudo = sc.nextLine();
        System.out.println("Chat REST prêt ! ('exit' pour quitter, 'clear' pour vider)");

        // THREAD DE RAFRAICHISSEMENT (GET)
        Thread refreshThread = new Thread(() -> {
            while (true) {
                try {
                    HttpRequest request = HttpRequest.newBuilder()
                            .uri(URI.create(BASE_URL))
                            .GET()
                            .build();

                    HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

                    // On nettoie un peu l'affichage du JSON pour que ce soit lisible
                    String body = response.body().replace("[", "").replace("]", "").replace("\"", "");
                    String[] currentMessages = body.isEmpty() ? new String[0] : body.split(",");

                    if (currentMessages.length > lastMsgCount) {
                        for (int i = lastMsgCount; i < currentMessages.length; i++) {
                            System.out.println("\n[Nouveau] " + currentMessages[i].trim());
                        }
                        lastMsgCount = currentMessages.length;
                        System.out.print("> ");
                    } else if (currentMessages.length < lastMsgCount) {
                        System.out.println("\n--- Le chat a été réinitialisé ---");
                        lastMsgCount = 0;
                        System.out.print("> ");
                    }
                    Thread.sleep(2000);
                } catch (Exception e) { /* Erreur silencieuse lors du rafraichissement */ }
            }
        });
        refreshThread.setDaemon(true);
        refreshThread.start();

        // BOUCLE D'ENVOI (POST et DELETE)
        while (true) {
            System.out.print("> ");
            String texte = sc.nextLine();

            if (texte.equalsIgnoreCase("exit")) break;

            if (texte.equalsIgnoreCase("clear")) {
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create(BASE_URL))
                        .DELETE()
                        .build();
                client.send(request, HttpResponse.BodyHandlers.ofString());
            } else {
                // Envoi en POST (Formulaire)
                String formData = "pseudo=" + pseudo + "&texte=" + texte;
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create(BASE_URL))
                        .header("Content-Type", "application/x-www-form-urlencoded")
                        .POST(HttpRequest.BodyPublishers.ofString(formData))
                        .build();
                client.send(request, HttpResponse.BodyHandlers.ofString());
            }
        }
    }
}