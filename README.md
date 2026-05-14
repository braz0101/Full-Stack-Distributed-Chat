# 🌐 Distributed Chatroom: From RMI to REST

![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=java&logoColor=white)
![Jakarta EE](https://img.shields.io/badge/Jakarta_EE-001010?style=for-the-badge&logo=jakartaee&logoColor=white)
![Tomcat](https://img.shields.io/badge/Apache_Tomcat-F8DC75?style=for-the-badge&logo=apache-tomcat&logoColor=black)

Une exploration complète des architectures distribuées à travers la création d'une application de chat multi-utilisateurs. Ce projet démontre ma capacité à implémenter une logique métier identique via quatre protocoles de communication différents.

---

## 🚀 Les 4 Visages du Projet

Ce dépôt n'est pas juste un chat, c'est une étude comparative de l'évolution des services web :

1. **[RMI (Remote Method Invocation)](./RMI/README.md)** :
   - L'approche "Pure Java". Manipulation d'objets distants via le registre RMI.
   - *Mots-clés : Stub/Skeleton, Registry, Sérialisation.*

2. **[XML-RPC](./XML-RPC/README.md)** :
   - L'ancêtre universel. Communication inter-langages via des appels de procédures codés en XML.
   - *Mots-clés : Apache XML-RPC, HTTP Transport, XML Parsing.*

3. **[SOAP (Simple Object Access Protocol)](./SOAP/README.md)** :
   - La rigueur industrielle. Services Web structurés avec contrats formels.
   - *Mots-clés : JAX-WS, WSDL, SOAP Envelopes.*

4. **[REST (Representational State Transfer)](./REST/README.md)** :
   - Le standard moderne. Architecture orientée ressources utilisant le protocole HTTP et le format JSON.
   - *Mots-clés : JAX-RS (Jersey), Introspection, JSON, Stateless.*

---

## 📊 Tableau Comparatif

| Critère | RMI | XML-RPC | SOAP | REST |
|---|---|---|---|---|
| **Transport** | JRMP (Java) | HTTP | HTTP | HTTP |
| **Format** | Binaire Java | XML | XML (enveloppe) | JSON |
| **Couplage** | Fort (Java only) | Moyen | Moyen | Faible |
| **Contrat** | Interface Java | Implicite | WSDL | OpenAPI (optionnel) |
| **Complexité** | Faible | Moyenne | Élevée | Faible |
| **Interopérabilité** | Java uniquement | Multi-langages | Multi-langages | Universelle |
| **Standard actuel** | ❌ Legacy | ❌ Legacy | ⚠️ Entreprise | ✅ Moderne |
| **Build** | Ant | Ant | IntelliJ | Maven |

---

## 🛠️ Stack Technique

| Outil | Rôle |
|---|---|
| Java 17+ | Langage principal |
| Apache Tomcat 11 | Serveur d'application |
| Jersey (Jakarta EE) | Framework REST (JAX-RS) |
| Apache Axis2 | Framework SOAP (JAX-WS) |
| Apache XML-RPC 2.0 | Librairie XML-RPC |
| Maven / Ant | Build tools |

---

## 📁 Organisation du Code

Chaque technologie possède son propre environnement pour isoler les mécanismes de communication :

```
Full-Stack-Distributed-Chat/
├── RMI/        # Serveur & Clients basés sur Naming.lookup
├── XML-RPC/    # Intégration des librairies xmlrpc-2.0
├── SOAP/       # Définition des services via services.xml
└── REST/       # API moderne avec endpoints @GET, @POST, @DELETE
```

---

## 📖 Ce que j'ai appris

- **Gestion du multi-threading** : Rafraîchissement automatique des messages côté client sans bloquer l'interface.
- **Introspection & Réflexion** : Utilisation des annotations pour le mapping automatique des ressources.
- **Architecture Client-Serveur** : Gestion de la persistance temporaire des données (`Vector`/`List`) côté serveur.
- **Évolution des standards** : Comprendre pourquoi REST a remplacé SOAP, et pourquoi RMI reste pertinent dans un contexte purement Java.

> **Note personnelle** : Ce projet m'a permis de comprendre que derrière chaque "clic", il y a une orchestration complexe de protocoles ! 🚀
