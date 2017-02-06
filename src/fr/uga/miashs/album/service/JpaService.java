package fr.uga.miashs.album.service;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceUnit;;



/*
 * On a choisi ApplicationScoped car une seule instance de chaque service suffit à l'application
 * Ce choix de reporte sur toute les sous classes
 * Si on ne met rien @RequestScoped est choisi par défaut
 * ==> @TODO say to teacher : wrong :
 * https://docs.oracle.com/javaee/7/tutorial/cdi-basic008.htm
 * Default scope is Dependent
 */
@ApplicationScoped
public abstract class JpaService<K,V> implements GenericService<K,V>, Serializable {

	private static final long serialVersionUID = -4220123326848708491L;

	private Class<V> cls;

	@PersistenceUnit( unitName = "EssaiJPA" )
	private EntityManagerFactory emf;

	private static EntityManager em=null;

	public JpaService() {
		cls= (Class<V>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[1];
	}

	@PostConstruct
	public void init () {
		em = emf.createEntityManager();
	}

	@PreDestroy
	public void close() {
		em.close();
	}

	protected EntityManager getEm() {
		return em;
	}

	@Override
	public void create(V v) throws ServiceException {
		EntityTransaction t = em.getTransaction();
		t.begin();
		em.persist(v);
		t.commit();
	}

	@Override
	public void edit(V v) throws ServiceException {
		// EntityTransaction t = em.getTransaction();
		// t.begin();
		// // em.persist(v);
		// t.commit();
		this.create(v);
	}

	@Override
	public V read(K id) throws ServiceException {
		return em.find(cls, id);
	}

	@Override
	public V update(V v) throws ServiceException {
		return em.merge(v);
	}

	@Override
	public void delete(V v) throws ServiceException {
		EntityTransaction t = em.getTransaction();
		t.begin();
		em.remove(v);
		t.commit();
	}

	@Override
	public void deleteById(K id) throws ServiceException {
		EntityTransaction t = em.getTransaction();
		t.begin();
		// getReference (contrairement à find) permet de charger seulement l'id
		// et non toutes les données de l'objet
		em.remove(em.getReference(cls, id));
		t.commit();
	}
}

// vim: sw=4 ts=4 noet:
