package fr.uga.miashs.album.service;

import java.util.List;

import javax.persistence.Query;

import fr.uga.miashs.album.model.Album;
import fr.uga.miashs.album.model.AppUser;


public class AlbumService extends JpaService<Long,Album> {

	private int i = 0;

	public void create(Album a) throws ServiceException {
		// teacher version
		// a.setOwner(getEm().merge(getEm().merge( a.getOwner())));
		// doesn't work
		// AppUser ownerLoc = null; a.setOwner(getEm().merge( ownerLoc ));
		// changed by this action ?
		System.out.println("Hello from albumService " + i + a.getOwner()); // => print reference
		a.setOwner(getEm().merge( a.getOwner()));
		// getEm().merge( a.getOwner()); // == > doesn't work, why ?
		// Why no reference updated in penultimate line, and previous line doesn't work ?
		i++;
		System.out.println("Hello from albumService " + i + a.getOwner()); // => print reference
		i++;
		super.create(a);
	}

	public List<Album> listAlbumOwnedBy(AppUser a) throws ServiceException {
		//La requete est définie dans la classe Album grâce à une annotation
		Query query = getEm().createNamedQuery("Album.findAllOwned");
		query.setParameter("owner", getEm().merge(a));
		return query.getResultList();
	}

}

// vim: sw=4 ts=4 noet:
