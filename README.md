From http://imss-www.upmf-grenoble.fr/~davidjer/javaee/Projet2015.odt

# Projet Java EE

Le but du projet est de créer une application de gestion d'albums photos et d'annotation des photos.

L'application sera multi-utilisateurs. Un utilisateur pourra créer des albums (nom, date de création, et un ensemble de photos), et les partager avec les utilisateurs de son choix. Les albums seront modifiables (ajout/suppression de photos, de partages, mise à jour des descriptions). Un utilisateur peut visualiser ses albums et les albums qui sont partagés avec lui.

L'utilisateur pourra annoter les photos de ses albums (i.e. ceux qu'il a créé). Les annotations possibles sur une photo sont : le créateur, quand la photo a été prise, où la photo a été prise, qui est sur la photo et quoi est sur la photo. Les lieux, personnes, et objets seront des entités à part entière. Cela vous permettra de créer des relations entre elles : un lieu peut être contenu dans un autre lieu, un objet (par exemple un monument) est localisé dans un lieu, et finalement on peut créer des groupes de personnes.

Une interface de recherche multi-critères (qui, quoi, quand) sera proposée.

Un formulaire de création de compte sera disponible ainsi qu'une interface d'admin permettant d'éditer et de supprimer un compte.

Au niveau de l'architecture du logiciel, il faut veiller à séparer la partie modèle, des contrôleurs (servlets) et des vues Facelets). L'application devra être internationalisée. La persistance sera gérée par JPA et éventuellement Apache JENA TDB (pour la partie web sémantique).


## Installation

### js and css dependencies
* Install bootstrap v3.x (in bootstrap.css, perform this remplacment :
        http://stackoverflow.com/questions/22129467/java-server-faces-with-custom-font-defined-in-css
* Put jQuery in web/resources/js/
* Install http://www.jasny.net/bootstrap/ as below

### jar dependencies
Into your apache-tomee-jaxrx-1.7.4/lib
* Use JSF 2.2 implementation (e.g. myfaces, jar "myfaces-api-2.2.11.jar" and "myfaces-impl-2.2.11.jar"). With TomeEE 7, delete myfaces 2.1 into your "apache-tomee-jaxrs-1.7.4/lib" directory.
* Install Derby (only "Derby.jar")
* Install http://showcase.omnifaces.org/ v 2.4 (with my TomeEE config, doesn't work with v 2.5, even with web.xml and context.xml and bean.xml configured as they want)
* N.B. Derby not allow concurrent connection.

### IDE
* Eclipse specific instruction
** Use "Dynamic Web Project", and configure it properly.
** With Eclipse, see (Stackoverflow)[http://stackoverflow.com/questions/38594094/java-lang-nullpointerexception-at-org-omnifaces-cdi-eager-eagerbeansphaselistene]
* IntelliJ
** Use JSF project, do not download librairies. Add librairies in /File/ProjectStructure/lib
** IntelliJ is better for database visualisation, but it must be configured for autobuild and autodeployment (otherwise you should restart tomee for each source modification).


### TomEE configuration
#### tomee.xml
With Eclipse, in the "Server" project Directory, with IntelliJ, in apache-tomee-jaxrs-1.7.4/conf/tomee.xml, you can use this `<Resource>` in tomee.xml (respect carriage return)
```
<Resource id="albumDS" type="DataSource">
    JdbcDriver org.apache.derby.jdbc.EmbeddedDriver
    JdbcUrl jdbc:derby:/path/to/your/DB;create=true
    JtaManaged=false
</Resource>
```

### Use Interface to resolve page name:

In an Facelet page, check you have
```
<html
    (…)
    xmlns:o="http://omnifaces.org/ui"
>
(…)
<o:importConstants type="fr.uga.miashs.album.util.Pages" />
```

<!-- vim: sw=4 ts=4 et:
-->
