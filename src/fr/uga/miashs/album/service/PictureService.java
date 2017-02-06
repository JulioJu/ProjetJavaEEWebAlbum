package fr.uga.miashs.album.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.activation.MimetypesFileTypeMap;
import javax.inject.Inject;
import javax.persistence.Query;

import fr.uga.miashs.album.model.Album;
import fr.uga.miashs.album.model.AppUser;
import fr.uga.miashs.album.model.Picture;
import fr.uga.miashs.album.util.IOUtilsCust;


public class PictureService extends JpaService<Long,Picture> {

    private static final long serialVersionUID = 8759786961691423964L;
    @Inject
    private AlbumService albumService;

    // @BUG from tomee. With edit-picture.xhtml, this method is executed
    // even if this.setField(p) is commented in edit(Picture) methodd
    private void setField(Picture p) throws ServiceException {
        // Or with File.io (older) see for example http://javabydeveloper.com/save-image-working-large-objects/
        // Image size must be smaller than 2 GB with Files.readAllBytes(Path)
        if (p.getPart() != null) {
            byte[] picBytes;
            try {
                // if Files.size < 0.9 MB
                // By Default MySql restrict file > 1MB
                // http://www.codejava.net/coding/upload-files-to-database-servlet-jsp-mysql
                if (p.getPart().getSize() > 900000) {
                    throw new ServiceException("Not allowed to save file > 1MB in Database");
                }
                InputStream partInputStream = p.getPart().getInputStream();
                picBytes = IOUtilsCust.toByteArray(partInputStream);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                throw new ServiceException("IOException with upload of file " + p.getPart().getName());
            }
            p.setFile(picBytes);
            // See also http://www.ramkitech.com/2013/06/file-upload-is-easy-in-jsf22.html
            p.setContentType(p.getPart().getHeader("Content-Type"));
        }
    }

    @Override
    public void create(Picture p) throws ServiceException {
        this.setField(p);
        p.setAlbum(albumService.read(p.getAlbumId()));
        p.setDateCreated();
        super.create(p);
    }

    @Override
    public void edit(Picture p) throws ServiceException {
        if (p.getPart() != null && p.getPart().getSize() > 0)
            this.setField(p);
        p.setAlbum(albumService.read(p.getAlbumId()));
        super.edit(p);
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
