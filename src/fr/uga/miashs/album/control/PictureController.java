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

    private boolean isAllowedModify() {
        if (appUserSession.getConnectedUser().isAdmin()
                || picture.getOwner().equals(appUserSession.getConnectedUser()))
            return true;
        return false;
    }

    public String delete(long pictureId) {
        try {
            picture = pictureService.read(pictureId);
        } catch (ServiceException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        if (picture == null) {
            throw new NullPointerException("The album with id " + pictureId + " that you want to destroy not exists in database");
        }
        if (this.isAllowedModify())
            return Pages.error_403;
        try {
            pictureService.deleteById(pictureId);
        } catch (ServiceException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return Pages.list_picture_owned;
    }

    public String createPicture() {
        try {
            pictureService.create(picture);
        } catch (ServiceException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return Pages.list_picture_owned;
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
