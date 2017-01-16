From http://imss-www.upmf-grenoble.fr/~davidjer/javaee/Projet2015.odt

# Projet Java EE

Le but du projet est de créer une application de gestion d'albums photos et d'annotation des photos.

L'application sera multi-utilisateurs. Un utilisateur pourra créer des albums (nom, date de création, et un ensemble de photos), et les partager avec les utilisateurs de son choix. Les albums seront modifiables (ajout/suppression de photos, de partages, mise à jour des descriptions). Un utilisateur peut visualiser ses albums et les albums qui sont partagés avec lui.

L'utilisateur pourra annoter les photos de ses albums (i.e. ceux qu'il a créé). Les annotations possibles sur une photo sont : le créateur, quand la photo a été prise, où la photo a été prise, qui est sur la photo et quoi est sur la photo. Les lieux, personnes, et objets seront des entités à part entière. Cela vous permettra de créer des relations entre elles : un lieu peut être contenu dans un autre lieu, un objet (par exemple un monument) est localisé dans un lieu, et finalement on peut créer des groupes de personnes.

Une interface de recherche multi-critères (qui, quoi, quand) sera proposée.

Un formulaire de création de compte sera disponible ainsi qu'une interface d'admin permettant d' éditer et de supprimer un compte.

Au niveau de l'architecture du logiciel, il faut veiller à séparer la partie modèle, des contrôleurs (servlets) et des vues JSP). Les JSP ne contiendront si possible pas de scriptlets mais utiliseront massivement la JSTL. L'application devra être internationalisée. La persistance sera gérée par JPA et éventuellement Apache JENA TDB (pour la partie web sémantique).


## Installation

* Install bootstrap v3.x (see https://www.youtube.com/watch?v=gg7MlRGuWTQ)
* Put jQuery in web/resources/js/
* Install http://www.jasny.net/bootstrap/
