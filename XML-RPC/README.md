# 🟡 Chat en temps réel — XML-RPC

## 📌 Description
Implémentation d'un salon de chat multi-utilisateurs via **XML-RPC**.  
Le client appelle des procédures distantes sur le serveur (HTTP + XML) pour poster et récupérer des messages. La mise à jour est assurée par un mécanisme de **polling** côté client toutes les secondes.

---

## 🏗️ Architecture

```
ServeurChat (WebServer port 8080)
    ├── chat.posterMessage(pseudo, message)  → stocke dans l'historique
    └── chat.lireMessages(index)             → retourne les messages manquants

ClientChat (GUI Swing)
    ├── envoyerMessage()      → appelle chat.posterMessage via XML-RPC
    └── lancerPolling()       → appelle chat.lireMessages toutes les 1s
```

---

## 📁 Structure

```
XML-RPC/
├── src/
│   ├── ServeurChat.java    # Serveur XML-RPC (WebServer port 8080)
│   └── ClientChat.java     # Client GUI (Swing) avec polling
├── lib/
│   ├── xmlrpc-2.0.jar      # Librairie Apache XML-RPC
│   └── commons-codec-1.15.jar
├── build.xml               # Script Ant (compile + jar)
└── README.md
```

---

## ⚙️ Concepts clés

| Concept | Rôle |
|---|---|
| `WebServer` | Serveur HTTP intégré d'Apache XML-RPC |
| `addHandler("chat", obj)` | Expose les méthodes de `ServeurChat` sous le namespace `chat` |
| `XmlRpcClient.execute()` | Appel de procédure distante depuis le client |
| `Vector historique` | Stockage en mémoire des messages côté serveur |
| **Polling** | Le client interroge le serveur toutes les 1s pour détecter les nouveaux messages |
| **Index de messages** | Chaque client garde son propre index pour ne pas retélécharger les anciens messages |

---

## 🚀 Compilation et exécution

### Avec Ant
```bash
# Compiler et créer le JAR
ant all

# Nettoyer les fichiers générés
ant clean
```

### Manuellement
```bash
# Compiler (avec les librairies dans lib/)
javac -cp "lib/*" -d build src/*.java

# Lancer le serveur
java -cp "build:lib/*" ServeurChat

# Lancer un client (dans un autre terminal)
java -cp "build:lib/*" ClientChat
```

> **Windows** : remplacer `:` par `;` dans les classpaths.

---

## 🖥️ Fonctionnement

1. Le serveur démarre un `WebServer` sur le port `8080` et enregistre le handler `chat`
2. Chaque client saisit son pseudo via une boîte de dialogue Swing
3. Le client se connecte à `http://localhost:8080/`
4. Un `Timer` Swing déclenche `verifierNouveauxMessages()` toutes les **1 seconde**
5. Le client envoie son index courant → le serveur retourne uniquement les messages manquants
6. L'index du client est mis à jour après chaque réception

---

## ⚠️ Prérequis

- Java 17+
- Apache Ant (pour le build automatisé)
- Les JARs `xmlrpc-2.0.jar` et `commons-codec-1.15.jar` présents dans `lib/`
- Le serveur doit être lancé **avant** les clients
