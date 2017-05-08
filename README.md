# Introduction

From http://imss-www.upmf-grenoble.fr/~davidjer/javaee/Projet2015.odt

Le but du projet est de créer une application de gestion d'albums photos et d'annotation des photos.

L'application sera multi-utilisateurs. Un utilisateur pourra créer des albums (nom, date de création, et un ensemble de photos), et les partager avec les utilisateurs de son choix. Les albums seront modifiables (ajout/suppression de photos, de partages, mise à jour des descriptions). Un utilisateur peut visualiser ses albums et les albums qui sont partagés avec lui.

L'utilisateur pourra annoter les photos de ses albums (i.e. ceux qu'il a créé). Les annotations possibles sur une photo sont : le créateur, quand la photo a été prise, où la photo a été prise, qui est sur la photo et quoi est sur la photo. Les lieux, personnes, et objets seront des entités à part entière. Cela vous permettra de créer des relations entre elles : un lieu peut être contenu dans un autre lieu, un objet (par exemple un monument) est localisé dans un lieu, et finalement on peut créer des groupes de personnes.

Une interface de recherche multi-critères (qui, quoi, quand) sera proposée.

Un formulaire de création de compte sera disponible ainsi qu'une interface d'admin permettant d'éditer et de supprimer un compte.

Au niveau de l'architecture du logiciel, il faut veiller à séparer la partie modèle, des contrôleurs (servlets) et des vues Facelets). L'application devra être internationalisée. La persistance sera gérée par JPA et éventuellement Apache JENA TDB (pour la partie web sémantique).


# Installation

## js and css dependencies
* Install bootstrap v3.x (in bootstrap.css, perform this remplacment :
        http://stackoverflow.com/questions/22129467/java-server-faces-with-custom-font-defined-in-css
* Put jQuery in web/resources/js/
* Install http://www.jasny.net/bootstrap/ as below

## jar dependencies
Into your apache-tomee-jaxrx-1.7.4/lib
* Use JSF 2.2 implementation (e.g. myfaces, jar "myfaces-api-2.2.11.jar" and "myfaces-impl-2.2.11.jar"). With TomeEE 7, delete myfaces 2.1 into your "apache-tomee-jaxrs-1.7.4/lib" directory.
* Install Derby (only "Derby.jar")
* Install http://showcase.omnifaces.org/ v 2.4 (with my TomeEE config, doesn't work with v 2.5, even with web.xml and context.xml and bean.xml configured as they want)
* N.B. Derby not allow concurrent connection.

## IDE
* Eclipse specific instruction
  * Use "Dynamic Web Project", and configure it properly.
  * With Eclipse, see (Stackoverflow)[http://stackoverflow.com/questions/38594094/java-lang-nullpointerexception-at-org-omnifaces-cdi-eager-eagerbeansphaselistene]
* IntelliJ
  * Use JSF project, do not download librairies. Add librairies in /File/ProjectStructure/lib
  * IntelliJ is better for database visualisation, but it must be configured for autobuild and autodeployment (otherwise you should restart tomee for each source modification).


## TomEE configuration
### tomee.xml
The config is now in WEB-INF/resources.xml (see http://tomee.apache.org/datasource-config.html)
~~With Eclipse, in the "Server" project Directory, with IntelliJ, in apache-tomee-jaxrs-1.7.4/conf/tomee.xml, you can use this `<Resource>` in tomee.xml (respect carriage return)
```
<Resource id="albumDS" type="DataSource">
    JdbcDriver org.apache.derby.jdbc.EmbeddedDriver
    JdbcUrl jdbc:derby:/path/to/your/DB;create=true
    JtaManaged=false
</Resource>
```
~~

## Use Interface to resolve page name:

In an Facelet page, check you have
```
<html
    (…)
    xmlns:o="http://omnifaces.org/ui"
>
(…)
<o:importConstants type="fr.uga.miashs.album.util.Pages" />
```

## ConversationExceptionFilter

ConversationExceptionFilter works only in Production mode (see web.xml).

Maybe try http://www.programcreek.com/java-api-examples/index.php?source_dir=seam-booking-ogm-master/src/main/java/org/jboss/seam/examples/booking/exceptioncontrol/ConversationExceptionHandler.java with Jboss.

See also comments in AlbumController.java.

## Contextual access denied error page for pictures

See informations in FilterPage.java

## Postgresql + Wildfly configuration (not used)

https://tomylab.wordpress.com/2016/07/24/how-to-add-a-datasource-to-wildfly/ :
```
module add --name=org.postgresql --resources=~/workspace/postgresql-9.4.1212.jre7.jar --dependencies=javax.api,javax.transaction.api
See cmd in https://github.com/wildfly/quickstart/blob/11.x/configure-postgresql.cli
data-source add --jndi-name=java:jboss/datasources/EssaiJPA --name=EssaiJPA --connection-url=jdbc:postgresql://localhost/albumDS --driver-name=postgresql --user-name=postgres
```

# @TODO
* Inform exceptions in Facelet view
* Manage JpaService.read(key) issues (key not found in database)
* Test if client send good POST values
* ~~Make a custom header for edit-album.xhtml (all link should end conversation), or try with ReachFaces ([see also](http://stackoverflow.com/questions/9983904/is-there-a-way-to-call-a-method-upon-leaving-a-page-with-jsf-or-primefaces) (but ReachFaces is not yes supported))~~
* See strange bug (fixed) in PictureService.java (see comment in this file). Is it only with tomee ? (Probably fixed, method JpaService.java/edit(){} must extend JpaService.java/upload(){} and not JpaService.java/create, but it's stranged that a private method is accessed from another class)
* Ask confirmation before perform an action (in JavaScript)
* Remove Omnifaces and change Pages interface to an CDI bean (issues, could not
        be injected in all classes)
* Move templates in WEB-INF, and access it with CDI bean Pages
* Put bootstrap col in template
* Try https://jsflive.wordpress.com/2013/07/17/jsf22-cdi-view-scope/ (replace ConversationScoped)
* As teacher sayed, move pictures resolution from FilterPage.java to a new Servlet

## Know issues
* **Regression :** CORRECT BUGS WITH ALBUM VISUALISATION AND PICTURE'S CREATION IN ALBUM VISUALISATION, ETC (REGRESSION)
* ~~When there is more than 10 pictures in Database :
=> Maybe, configure better Database, and use Hibernate + Mysql~~ (fixed by resolution of bug with EntityManager per application anti-patern)
* Sometimes, even we refresh, deconnect, reconnect, an picture not inserted is no displayed
==> restart TomEE for fix this issue (maybe fixed when bug with EntityManager per application anti-patern is fixed).

## EntityManager per application anti-patern (bug introduced by teacher: fixed)

Anti-patern:« The session-per-application is also considered an anti-pattern. The Hibernate Session, like the JPA EntityManager, is not a thread-safe object and it is intended to be confined to a single thread at once. If the Session is shared among multiple threads, there will be race conditions as well as visibility issues , so beware of this. » https://docs.jboss.org/hibernate/orm/5.2/userguide/html_single/Hibernate_User_Guide.html#PoEA~ (bug fixed)
(source [https://docs.jboss.org/hibernate/orm/5.2/userguide/html_single/Hibernate_User_Guide.html])
```
Multiple concurrent threads attempted to access a single broker. By default brokers are not thread safe; if you require and/or intend a broker to be accessed by more than one thread, set the openjpa.Multithreaded property to true to override the default behavior.
(…)
Caused by:
org.apache.openjpa.persistence.PersistenceException - Multiple concurrent threads attempted to access a single broker. By default brokers are not thread safe; if you require and/or intend a broker to be accessed by more than one thread, set the openjpa.Multithreaded property to true to override the default behavior.  at org.apache.openjpa.kernel.BrokerImpl.endOperation(BrokerImpl.java:1987)
```
* It was not au problem with OpenJpa or Derby, but a problem in utilization of anti-patern in JpaService.java (one EntityManager per application).
* It's seems to be better to use EJB, and not CDI Bean).
  * In Tomee, Hibernate have some problems with CDI bean, no out-of-box (see [http://tomee-openejb.979440.n4.nabble.com/Migrating-JBoss-CDI-Hibernate-app-to-TomEE-Could-not-access-BeanManager-ListenerFactory-class-td4680977.html])
  * If you want begin and commit automatically, you must use an EJB (see [http://tomee.apache.org/examples-trunk/injection-of-entitymanager/README.html], be carefull with this link, it's better to use @Stateless) (and I've readen it's better Container Managed Persistance context + JTA + CMT)
### EJB
Jérôme LAFOSSe, *Developpements n-tiers avac Java EE* ,ENI, 2011
«
La plate-forme Java EE propose le développement des couches métier et persistance avec les EJB. Les Enterprise JavaBeans permettent le développement d’applications distribuées, transactionnelles, sécurisées et autres.

Les transactions EJB respectent l’acronyme ACID (Atomicité, Cohérence, Isolation et Durabilité) et grâce aux EJB, le développeur n’est pas obligé de gérer les limites des transactions (begin, commit).
Comme cela est expliqué dans le chapitre précédent, la mise en place d’un
mécanisme de persistance avec JPA est largement fonctionnelle et suffisante pour
certains projets. (…) Le développeur devra donc développer ses classes entités
et réaliser des requêtes JPQL en utilisant les transactions pour les
manipulations de données. Cependant, afin de séparer au maximum la couche de
persistance de la couche présentation, il est recommandé d’utiliser une logique
métier en ajoutant une gestion optimisée des transactions et de la sécurité.
Avec Java EE, cette couche métier utilise les Enterprise JavaBeans. » (p. 591)

« Un session bean propose un accès local (interface Local) ou distant (interface
Remote) ou les deux. Désormais, l’utilisation de ces interfaces est
optionnelle, et il est alors possible d’accéder directement à un EJB sans passer
par ses interfaces. De façon implicite, un EJB possède une interface de type
local, c’est le comportement par défaut. » (p. 592)

« Avec une mise en place JPA comme dans le chapitre précédent, la couche métier
est bien isolée mais elle ne permet pas un accès autonome, automatiquement
transactionnel et sécurisé par d’autres applications clientes. Avec les EJB, une
nouvelle couche intermédiaire est proposée ; elle permet alors des appels et
utilisations par différentes applications clientes. Le développeur peut alors se
concentrer sur les couches de présentation (tiers Présentation), la logique
fonctionnelle (tiers Contrôleur), la logique métier (tiers Métier) et sur le
système de stockage utilisé (SGBD, ERP, fichiers...). » (p. 592)
### See also
* http://piotrnowicki.com/2012/11/types-of-entitymanagers-application-managed-entitymanager/
* https://docs.jboss.org/hibernate/orm/5.2/userguide/html_single/Hibernate_User_Guide.html
* http://www.theserverside.com/news/thread.tss?thread_id=19718
* http://sunmingtao.blogspot.fr/2011/09/transaction-vs-extended-persistence.html
* http://www.kumaranuj.com/2013/06/jpa-2-entitymanagers-transactions-and.htmlo
* http://www.byteslounge.com/tutorials/container-vs-application-managed-entitymanager
* http://www.byteslounge.com/tutorials/jpa-extended-persistence-contexto
* **https://www.tutorialspoint.com/ejb/ejb_persistence.htm**

## Feature
* Only admin or owner can delete or change album or picture. Delete link do nothing, and edit link send 403 error.
<!-- vim: sw=4 ts=4 et:
-->
