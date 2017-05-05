# Projet Java EE

From http://imss-www.upmf-grenoble.fr/~davidjer/javaee/Projet2015.odt

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

### ConversationExceptionFilter

ConversationExceptionFilter works only in Production mode (see web.xml).

Maybe try http://www.programcreek.com/java-api-examples/index.php?source_dir=seam-booking-ogm-master/src/main/java/org/jboss/seam/examples/booking/exceptioncontrol/ConversationExceptionHandler.java with Jboss.

See also comments in AlbumController.java.

### Contextual access denied error page for pictures

See informations in FilterPage.java

## @TODO
* Inform exceptions in Facelet view
* Manage JpaService.read(key) issues (key not found in database)
* Test if client send good POST values
* ~~Make a custom header for edit-album.xhtml (all link should end conversation), or try with ReachFaces ([see also](http://stackoverflow.com/questions/9983904/is-there-a-way-to-call-a-method-upon-leaving-a-page-with-jsf-or-primefaces) (but ReachFaces is not yes supported))~~

* See strange bug (fixed) in PictureService.java (see comment in this file). Is it only with tomee ?
* Ask confirmation before perform an action (in JavaScript)
* Remove Omnifaces and change Pages interface to an CDI bean (issues, could not
        be injected in all classes)
* Move templates in WEB-INF, and access it with CDI bean Pages
* Put bootstrap col in template
* Try https://jsflive.wordpress.com/2013/07/17/jsf22-cdi-view-scope/ (replace ConversationScoped)
* Resolve this anti-patern « The session-per-application is also considered an anti-pattern. The Hibernate Session, like the JPA EntityManager, is not a thread-safe object and it is intended to be confined to a single thread at once. If the Session is shared among multiple threads, there will be race conditions as well as visibility issues , so beware of this. » https://docs.jboss.org/hibernate/orm/5.2/userguide/html_single/Hibernate_User_Guide.html#PoEAA

## Know issues
* When there is more than 10 pictures in Database :
```
Multiple concurrent threads attempted to access a single broker. By default brokers are not thread safe; if you require and/or intend a broker to be accessed by more than one thread, set the openjpa.Multithreaded property to true to override the default behavior.
(…)
Caused by:
org.apache.openjpa.persistence.PersistenceException - Multiple concurrent threads attempted to access a single broker. By default brokers are not thread safe; if you require and/or intend a broker to be accessed by more than one thread, set the openjpa.Multithreaded property to true to override the default behavior.  at org.apache.openjpa.kernel.BrokerImpl.endOperation(BrokerImpl.java:1987)
```
=> Maybe, configure better Database, and use Hibernate + Mysql
* Sometimes, even we refres, deconnect, reconnect, an picture not inserted is no displayed
==> restart TomEE for fix this issue.

# Info
* Info : only admin or owner can delete or change album or picture. Delete link do nothing, and edit link send 403 error.

# Postgresql + Wildfly

https://tomylab.wordpress.com/2016/07/24/how-to-add-a-datasource-to-wildfly/ :
```
module add --name=org.postgresql --resources=~/workspace/postgresql-9.4.1212.jre7.jar --dependencies=javax.api,javax.transaction.api
See cmd in https://github.com/wildfly/quickstart/blob/11.x/configure-postgresql.cli
data-source add --jndi-name=java:jboss/datasources/EssaiJPA --name=EssaiJPA --connection-url=jdbc:postgresql://localhost/albumDS --driver-name=postgresql --user-name=postgres
```

<!-- vim: sw=4 ts=4 et:
-->
