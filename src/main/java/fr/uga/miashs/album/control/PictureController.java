package fr.uga.miashs.album.control;

import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import fr.uga.miashs.album.model.Picture;
import fr.uga.miashs.album.service.AlbumService;
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
    @Inject
    private AlbumService albumService;
    private Picture picture;

    public Picture getPicture() {
        // useful when launch view add-picture.xhtml
        if (picture == null) {
            picture = new Picture();
        }
        return picture;
    }

    public void setPicture(Picture picture) {
        this.picture = picture;
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
        FacesContext ctx = FacesContext.getCurrentInstance();
        String idString = ctx.getExternalContext().
                getRequestParameterMap().get("albumId");
        Long albumIdRetrieveFromView;
        if (idString != null && !idString.isEmpty()) {
            try {
                albumIdRetrieveFromView = Long.valueOf(idString);
                this.getPicture().setAlbum(albumService.read(albumIdRetrieveFromView));
            }
            catch (NumberFormatException e) {
                // TODO
                throw new RuntimeException("You have passed wrong parameters");
            } catch (ServiceException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        try {
            pictureService.create(picture);
        } catch (ServiceException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        if (idString == null || idString.equals(""))
            return Pages.list_picture_owned+"?faces-redirect=true";
        return Pages.list_picture_by_album + "?faces-redirect=true&albumId="
            + idString;
    }

    public String edit() {
        // tests
        Picture pictureTmp = picture;
        FacesContext ctx = FacesContext.getCurrentInstance();
        long pictureId;
        try {
        pictureId = Long.valueOf(ctx.getExternalContext().
                getRequestParameterMap().get("pictureId"));
        }
        catch (NumberFormatException e) {
            // TODO
            throw new RuntimeException("You have passed wrong parameters");
        }
        long albumId;
        try {
        albumId = Long.valueOf(ctx.getExternalContext().
                getRequestParameterMap().get("albumId"));
        }
        catch (NumberFormatException e) {
            // TODO
            throw new RuntimeException("You have passed wrong parameters");
        }
        String fromPage = ctx.getExternalContext().
                getRequestParameterMap().get("fromPage");
        // TODO not RuntimeException, but info
        if (!fromPage.equals(Pages.list_picture_owned)
                && !fromPage.equals(Pages.list_picture_by_album))
                throw new IllegalArgumentException("You have passed wrong parameters");
        try {
            this.picture = pictureService.read(pictureId);
        } catch (ServiceException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        if (!this.isAllowedModify())
            return Pages.error_403;
        // ** perform modifications
        picture.setAlbumId(pictureTmp.getAlbumId());
        picture.setTitle(pictureTmp.getTitle());
        if (pictureTmp.getPart()!=null && pictureTmp.getPart().getSize()>0){
            picture.setPart(pictureTmp.getPart());
        }
        try {
            pictureService.edit(picture);
        } catch (ServiceException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }
        return fromPage+"?faces-redirect=true&albumId="+albumId;
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

    public List<Picture> getListPictureSharedWithCurrentUser() {
        try {
            return pictureService.listPictureSharedWith(appUserSession.getConnectedUser());
        } catch (ServiceException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

}

// vim: set sw=4 ts=4 et:
