package fr.uga.miashs.album.service;

import java.util.HashSet;
import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.Query;

import fr.uga.miashs.album.control.AppUserSession;
import fr.uga.miashs.album.model.Album;
import fr.uga.miashs.album.model.AppUser;


@Stateless
public class AlbumService extends JpaService<Long,Album> {

	private static final long serialVersionUID = -2061392070773108977L;
	@Inject
	private AppUserService appUserService;
	@Inject
	private AppUserSession appUserSession;

	private void setSharedWithField(Album a) throws ServiceException {
		if (a.getSharedWithArray() != null)
			for (int i=0; i<a.getSharedWithArray().length ; i++) {
				AppUser user = appUserService.read(a.getSharedWithArray()[i]);
				if (!user.equals(appUserSession.getConnectedUser()))
					a.getSharedWith().add(user);
			}
	}

	@Override
	public void create(Album a) throws ServiceException {
		a.setSharedWith(new HashSet<AppUser>());
		this.setSharedWithField(a);
		a.setOwner(getEm().merge( a.getOwner()));
		a.setDateCreated();
		super.create(a);
	}

	@Override
	public void edit(Album a) throws ServiceException {
		this.setSharedWithField(a);
		for (int i=0; i<a.getNoSharedWithArray().length ; i++){
			AppUser user = appUserService.read(a.getNoSharedWithArray()[i]);
			a.getSharedWith().remove(user);
		}
		super.edit(a);
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
		query.setParameter("currentUser", a.getId());
		return query.getResultList();
	}

}

// vim: sw=4 ts=4 noet:
