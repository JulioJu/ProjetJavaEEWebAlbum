package fr.uga.miashs.album.service;

import java.util.List;

import javax.inject.Inject;
import javax.persistence.Query;

import fr.uga.miashs.album.model.Album;
import fr.uga.miashs.album.model.AppUser;
import fr.uga.miashs.album.model.Picture;


public class PictureService extends JpaService<Long,Picture> {

    private static final long serialVersionUID = 8759786961691423964L;
    @Inject
    private AlbumService albumService;

    @Override
    public void create(Picture p) throws ServiceException {
        p.setDateCreated();
        p.setUri(p.getUriString());
        p.setAlbum(albumService.read(p.getAlbumId()));
        super.create(p);
    }

    public List<Picture> listPictureOwnedBy(AppUser a) throws ServiceException {
        //La requete est définie dans la classe Picture grâce à une annotation
        Query query = getEm().createNamedQuery("Picture.findAllOwned");
        query.setParameter("owner", getEm().merge(a));
        return query.getResultList();
    }

    public List<Picture> listPictureFromOneAlbum(Album a) throws ServiceException {
        //La requete est définie dans la classe Picture grâce à une annotation
        Query query = getEm().createNamedQuery("Picture.findAllFromOneAlbum");
        query.setParameter("album", getEm().merge(a));
        return query.getResultList();
    }

}

// vim: sw=4 ts=4 et:
