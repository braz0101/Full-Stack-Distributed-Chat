# 🟢 Chat en temps réel — REST (Representational State Transfer)

## 📌 Description
Implémentation d'un salon de chat multi-utilisateurs via une **API REST** avec **Jakarta EE / Jersey**.  
Le serveur expose une ressource `/messages` accessible via les méthodes HTTP standard (`GET`, `POST`, `DELETE`). Le client console rafraîchit les messages automatiquement grâce à un **thread de polling** toutes les 2 secondes.

---

## 🏗️ Architecture

```
ChatResource — /api/messages (déployé sur Tomcat)
    ├── GET    /api/messages  → retourne tous les messages (JSON)
    ├── POST   /api/messages  → ajoute un message horodaté (form-urlencoded)
    └── DELETE /api/messages  → vide l'historique

ChatClientRest (console)
    ├── Thread de refresh (toutes les 2s) → GET /api/messages
    └── Boucle principale                 → POST (message) ou DELETE (clear)
```

---

## 📁 Structure

```
REST/
├── src/main/
│   ├── java/com/example/chatroomrest/
│   │   ├── ChatResource.java       # Ressource REST (endpoints JAX-RS)
│   │   ├── RestApplication.java    # Point d'entrée JAX-RS (@ApplicationPath)
│   │   └── ChatClientRest.java     # Client console avec thread de refresh
│   └── resources/META-INF/
│       └── beans.xml               # Activation CDI Jakarta EE
└── README.md
```

---

## ⚙️ Concepts clés

| Concept | Rôle |
|---|---|
| `@Path("/messages")` | Déclare l'URI de la ressource REST |
| `@GET / @POST / @DELETE` | Mappe les méthodes HTTP aux méthodes Java |
| `@Produces(APPLICATION_JSON)` | Le serveur sérialise automatiquement la liste en JSON |
| `@Consumes(FORM_URLENCODED)` | Le serveur accepte les données de formulaire en POST |
| `@FormParam` | Extrait les paramètres du corps du POST |
| `@ApplicationPath("/api")` | Préfixe global de toutes les routes JAX-RS |
| `HttpClient` (Java 11+) | Client HTTP natif utilisé côté client console |
| **Thread démon** | Rafraîchit les messages toutes les 2s sans bloquer la saisie |

---

## 🌐 Endpoints disponibles

| Méthode | URL | Description |
|---|---|---|
| `GET` | `/api/messages` | Récupère tous les messages en JSON |
| `POST` | `/api/messages` | Envoie un message (`pseudo` + `texte` en form-data) |
| `DELETE` | `/api/messages` | Efface tous les messages |

---

## 🚀 Déploiement et exécution

### Prérequis
- Java 17+
- Apache Tomcat 10+ (Jakarta EE)
- Maven pour la compilation
- Jersey (JAX-RS) dans les dépendances Maven

### Déployer le serveur
```bash
# Compiler et packager
mvn clean package

# Déployer le WAR dans Tomcat
cp target/ChatroomREST.war $TOMCAT_HOME/webapps/

# Démarrer Tomcat
$TOMCAT_HOME/bin/startup.sh
```

Le service sera accessible à :
```
http://localhost:8080/ChatroomREST_war_exploded/api/messages
```

### Lancer le client
```bash
java -cp "target/classes" com.example.chatroomrest.ChatClientRest
```

---

## 🖥️ Fonctionnement

1. Le serveur démarre sur Tomcat et expose la ressource `/api/messages`
2. Les messages sont stockés en mémoire (`Vector<String>`) côté serveur
3. Chaque message est **horodaté** au format `[HH:mm:ss]` lors du POST
4. Le client saisit son pseudo puis démarre un **thread de rafraîchissement** (toutes les 2s)
5. Le thread compare le nombre de messages reçus avec le compteur local pour afficher uniquement les nouveaux
6. Commandes disponibles dans le client :
   - `exit` — quitter le chat
   - `clear` — envoyer une requête `DELETE` pour vider les messages
   - Tout autre texte — envoyer un `POST` avec le pseudo et le message

---

## ⚠️ Prérequis

- Java 17+
- Apache Tomcat 10+ (Jakarta EE 10)
- Maven avec dépendance Jersey (JAX-RS)
- Le serveur Tomcat doit être lancé **avant** le client
