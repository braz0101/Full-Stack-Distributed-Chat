# 🔵 Chat en temps réel — RMI (Remote Method Invocation)

## 📌 Description
Implémentation d'un salon de chat multi-utilisateurs en **Pure Java** via RMI.  
Le serveur expose un objet distant (`ChatRoom`) que les clients récupèrent via le registre RMI et utilisent pour s'abonner, envoyer et recevoir des messages en temps réel.

---

## 🏗️ Architecture

```
ChatRoom (interface Remote)
    └── ChatRoomImpl (serveur)
            └── notifie → ChatUser (interface Remote)
                              └── ChatUserImpl (client GUI Swing)
```

---

## 📁 Structure

```
RMI/
├── src/
│   ├── ChatRoom.java       # Interface distante du serveur
│   ├── ChatRoomImpl.java   # Implémentation serveur + main
│   ├── ChatUser.java       # Interface distante du client
│   └── ChatUserImpl.java   # Client GUI (Swing) + main
├── build.xml               # Script Ant (compile, jar, javadoc)
└── README.md
```

---

## ⚙️ Concepts clés

| Concept | Rôle |
|---|---|
| `UnicastRemoteObject` | Expose les objets Java sur le réseau |
| `LocateRegistry` | Crée le registre RMI sur le port 1099 |
| `Naming.rebind` | Enregistre le serveur dans le registre |
| `Naming.lookup` | Permet au client de retrouver le serveur |
| `Stub/Skeleton` | Proxy réseau généré automatiquement par RMI |

---

## 🚀 Compilation et exécution

### Avec Ant
```bash
# Compiler, créer le JAR et générer la Javadoc
ant all

# Nettoyer les fichiers générés
ant clean
```

### Manuellement
```bash
# Compiler
javac -d build src/*.java

# Lancer le serveur
java -cp build ChatRoomImpl

# Lancer un client (dans un autre terminal)
java -cp build ChatUserImpl
```

---

## 🖥️ Fonctionnement

1. Le serveur démarre et crée le registre RMI sur le port `1099`
2. Chaque client saisit son pseudo via une boîte de dialogue Swing
3. Le client s'abonne au `ChatRoom` distant via `Naming.lookup`
4. Les messages sont broadcastés en temps réel à tous les utilisateurs connectés
5. À la fermeture de la fenêtre, le client se désinscrit proprement

---

## ⚠️ Prérequis

- Java 17+
- Apache Ant (pour le build automatisé)
- Le serveur doit être lancé **avant** les clients
