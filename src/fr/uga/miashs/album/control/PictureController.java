package fr.uga.miashs.album.control;

import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import fr.uga.miashs.album.model.Picture;
import fr.uga.miashs.album.service.PictureService;
import fr.uga.miashs.album.service.ServiceException;
import fr.uga.miashs.album.util.Pages;

@Named
@RequestScoped
public class PictureController {

    @Inject
    private AppUserSession appUserSession;

    @Inject
    private PictureService pictureService;

    private Picture picture;

    public Picture getPicture() {
        // useful when launch view add-picture.xhtml
        if (picture == null) {
            picture = new Picture();
        }
        return picture;
    }

    public String delete(long pictureId) {
        picture = pictureService.read(pictureId);
        if (picture == null) {
            throw new NullPointerException("The album with id " + pictureId + " that you want to destroy not exists in database");
        }
        pictureService.deleteById(pictureId);
        return Pages.list_picture;
    }

    public String createPicture() {

        // TODO a folder named ~/workspace/PhotoAlbumJavaEEPictures should be created
        try {
            pictureService.create(picture);
        } catch (ServiceException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return Pages.list_picture;
    }


    public List<Picture> getListPictureOwnedByCurrentUser() {
        try {
            return pictureService.listPictureOwnedBy(appUserSession.getConnectedUser());
        } catch (ServiceException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

}

// vim: set sw=4 ts=4 et:
