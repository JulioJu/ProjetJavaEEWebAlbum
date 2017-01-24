package fr.uga.miashs.album.service;

import java.util.HashSet;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.Query;

import fr.uga.miashs.album.model.Album;
import fr.uga.miashs.album.model.AppUser;


public class AlbumService extends JpaService<Long,Album> {

	private static final long serialVersionUID = -2061392070773108977L;
	@Inject
	private AppUserService appUserService;

	public void create(Album a) throws ServiceException {
		a.setSharedWith(new HashSet<AppUser>());
		for (int i=0; i<a.getSharedWithArray().length ; i++) {
			a.getSharedWith().add(appUserService.read(a.getSharedWithArray()[i]));
		}
		a.setOwner(getEm().merge( a.getOwner()));
		super.create(a);
	}

	public List<Album> listAlbumOwnedBy(AppUser a) throws ServiceException {
		//La requete est définie dans la classe Album grâce à une annotation
		Query query = getEm().createNamedQuery("Album.findAllOwned");
		query.setParameter("owner", getEm().merge(a));
		return query.getResultList();
	}

	public List<Album> listAlbumShared(AppUser a) throws ServiceException {
		//La requete est définie dans la classe Album grâce à une annotation
		Query query = getEm().createNamedQuery("Album.findAllShared");
		query.setParameter("current_user", a.getId());
		return query.getResultList();
	}

}

// vim: sw=4 ts=4 noet:
