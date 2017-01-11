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
 * On a choisi ApplicationScoped car une seule instance de chque service suffit à l'application
 * Ce choix de reporte sur toute les sous classes
 * Si on ne met rien @RequestScoped est choisi par défaut
 */
@ApplicationScoped
public abstract class JpaService<K,V> implements GenericService<K,V>, Serializable {

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
	
	public void create(V v) throws ServiceException {
		EntityTransaction t = em.getTransaction();
		t.begin();
		em.persist(v);	
		t.commit();
	}
	
	public V read(K id) {
		return em.find(cls, id);
	}
	
	public V update(V v) {
		return em.merge(v);
	}
	
	public void delete(V v) {
		EntityTransaction t = em.getTransaction();
		t.begin();
		em.remove(v);
		t.commit();
	}
	
	public void deleteById(K id) {
		EntityTransaction t = em.getTransaction();
		t.begin();
		// getReference (contrairement à find) permet de charger seulement l'id
		// et non toutes les données de l'objet
		em.remove(em.getReference(cls, id));
		t.commit();
	}
}
