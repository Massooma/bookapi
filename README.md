# Book API - Spring Boot

## Description

Ce projet est une API REST simple développée avec Spring Boot permettant de gérer des livres.

Il a été créé principalement dans un but d'apprentissage et de prise en main de **Spring Boot et Elasticsearch** :

- création d'une API REST
- utilisation de Spring Web
- persistance avec Spring Data JPA
- utilisation d'une base de données embarquée H2
- découverte d'Elasticsearch pour la recherche de livres
- création de tests unitaires avec JUnit et Mockito

---

## Technologies utilisées

- Java 17
- Spring Boot
- Spring Web
- Spring Data JPA
- Spring Data Elasticsearch
- H2 Database
- Elasticsearch
- Maven
- JUnit 5
- Mockito
- Postman

---

## Fonctionnalités

L'API permet de :

- récupérer la liste des livres
- récupérer un livre par son identifiant
- rechercher des livres avec Elasticsearch
- ajouter un livre
- modifier un livre
- supprimer un livre

---

## Endpoints principaux

```text
GET    /books
GET    /books/{id}
GET    /books/search?q=Gilead
POST   /books
PUT    /books/{id}
DELETE /books/{id}
```

---

## Données

Les données utilisées pour l'application sont présentes dans le fichier :

```text
src/main/resources/books.csv
```

Au démarrage de l'application, les livres sont importés depuis le fichier CSV et enregistrés dans H2.
Ils sont ensuite indexés dans Elasticsearch afin de pouvoir effectuer des recherches.

---

## Elasticsearch

Elasticsearch est utilisé principalement pour la recherche de livres.
La recherche porte sur plusieurs champs :

- titre
- auteur
- catégorie
- description

Le titre et l'auteur ont un poids plus important dans le classement des résultats.

Exemple :

```text
GET /books/search?q=Gilead
```

---

## Tests

Des tests unitaires ont été réalisés avec **JUnit 5** et **Mockito** afin de tester principalement les fonctionnalités du `BookService`.

Pour lancer les tests :

```bash
./mvnw clean test
```

Sous Windows :

```powershell
.\mvnw.cmd clean test
```

---

## Lancer le projet

Elasticsearch doit être démarré avant de lancer l'application.

Pour démarrer l'application avec Maven :

```powershell
.\mvnw.cmd spring-boot:run
```

L'API est ensuite accessible à :

```text
http://localhost:8080
```

Les différents endpoints peuvent être testés avec Postman.
