package fr.uga.miashs.album.service;

public interface GenericService<K, V> {

	public void create(V v) throws ServiceException;
	public void edit(V v) throws ServiceException;
	public V read(K id) throws ServiceException;
	public V update(V v) throws ServiceException;
	public void delete(V v) throws ServiceException;
	public void deleteById(K id) throws ServiceException;

}

// vim: sw=4 ts=4 noet:
