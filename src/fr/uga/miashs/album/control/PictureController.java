package fr.uga.miashs.album.control;

import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
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
                || picture.getAlbum().getOwner().equals(appUserSession.getConnectedUser()))
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
        if (!this.isAllowedModify())
            return Pages.error_403;
        try {
            pictureService.deleteById(pictureId);
        } catch (ServiceException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return Pages.list_picture_owned;
    }

    public String create() {
        try {
            pictureService.create(picture);
        } catch (ServiceException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return Pages.list_picture_owned;
    }

    public String edit() {
        Picture pictureTmp = picture;
        FacesContext ctx = FacesContext.getCurrentInstance();
        String idString = (String) ctx.getExternalContext().
                getRequestParameterMap().get("id");
        Long pictureIdRetrieveFromView = Long.valueOf(idString);
        try {
            this.picture = pictureService.read(pictureIdRetrieveFromView);
        } catch (ServiceException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        if (!this.isAllowedModify())
            return Pages.error_403;
        picture.setAlbumId(pictureTmp.getAlbumId());
        picture.setTitle(pictureTmp.getTitle());
        System.out.println(pictureTmp.getPart());
        System.out.println(pictureTmp.getPart().getSize()+"coucou0");
        if (pictureTmp.getPart()!=null && pictureTmp.getPart().getSize()>0)
            picture.setPart(pictureTmp.getPart());
        try {
            pictureService.edit(picture);
        } catch (ServiceException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }
        return Pages.list_picture_owned;
    }

    public String viewEditPage(Long pictureIdRetrieveFromView) {
        try {
            this.picture = pictureService.read(pictureIdRetrieveFromView);
        } catch (ServiceException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return Pages.edit_picture;
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
