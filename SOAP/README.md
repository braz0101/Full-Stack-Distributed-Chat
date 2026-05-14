# 🟠 Chat en temps réel — SOAP (Simple Object Access Protocol)

## 📌 Description
Implémentation d'un salon de chat multi-utilisateurs via **SOAP avec Apache Axis2**.  
Le service est déployé sur un serveur Axis2/Tomcat et exposé via un contrat formel défini dans `services.xml`. Le client appelle les opérations distantes via `RPCServiceClient` et rafraîchit les messages automatiquement grâce à un **thread de polling** en arrière-plan.

---

## 🏗️ Architecture

```
ChatService (déployé sur Axis2/Tomcat port 8080)
    ├── envoyerMessage(pseudo, texte)  → ajoute un message horodaté
    ├── recupererMessages()            → retourne tous les messages
    └── effacerMessages()              → vide l'historique

ChatClient (console)
    ├── Thread de refresh (toutes les 2s) → appelle recupererMessages()
    └── Boucle principale               → appelle envoyerMessage() ou effacerMessages()
```

---

## 📁 Structure

```
SOAP/
├── src/
│   ├── META-INF/
│   │   └── services.xml    # Descripteur de déploiement Axis2
│   ├── ChatService.java    # Logique métier du service SOAP
│   ├── ChatClient.java     # Client console avec thread de refresh
│   └── Main.java           # Classe de test générée par l'IDE
├── ChatroomSOAP.iml        # Fichier de module IntelliJ IDEA
└── README.md
```

---

## ⚙️ Concepts clés

| Concept | Rôle |
|---|---|
| `services.xml` | Déclare les opérations exposées et leur `MessageReceiver` |
| `RPCMessageReceiver` | Géré automatiquement par Axis2 pour mapper les appels SOAP aux méthodes Java |
| `RPCServiceClient` | Client Axis2 pour invoquer les opérations distantes |
| `QName` | Identifie une opération SOAP par son namespace et son nom |
| `invokeBlocking()` | Appel synchrone avec réponse attendue |
| `invokeRobust()` | Appel sans attente de réponse (fire-and-forget) |
| **Thread démon** | Rafraîchit les messages toutes les 2s sans bloquer l'interface |

---

## 🚀 Déploiement et exécution

### Prérequis
- Java 17+
- Apache Tomcat + Axis2 installés et configurés
- IntelliJ IDEA (recommandé) ou tout autre IDE Java

### Déployer le service
1. Compiler le projet (`ChatService.java` + `services.xml`)
2. Créer un fichier `.aar` (Axis2 Archive) :
```
ChatService.aar
└── META-INF/
    └── services.xml
    └── ChatService.class
```
3. Déposer le `.aar` dans le dossier `webapps/axis2/WEB-INF/services/` de Tomcat
4. Démarrer Tomcat — le service sera accessible à :
```
http://localhost:8080/axis2/services/ChatService
```

### Lancer le client
```bash
java -cp "build:lib/*" ChatClient
```

---

## 🖥️ Fonctionnement

1. Le service `ChatService` est déployé sur Axis2 et stocke les messages en mémoire (`Vector`)
2. Chaque message est **horodaté** au format `[HH:mm:ss]` lors de l'envoi
3. Le client saisit son pseudo puis démarre un **thread de rafraîchissement** (toutes les 2s)
4. Le thread compare la taille du tableau de messages reçu avec celui en mémoire locale pour afficher uniquement les nouveaux
5. Commandes disponibles dans le client :
   - `exit` — quitter le chat
   - `clear` — effacer tous les messages (appelle `effacerMessages`)
   - Tout autre texte — envoie un message

---

## ⚠️ Prérequis

- Java 17+
- Apache Axis2 + Tomcat configurés
- Librairies Axis2 dans le classpath (`axis2-*.jar`, etc.)
- Le serveur Tomcat/Axis2 doit être lancé **avant** le client
