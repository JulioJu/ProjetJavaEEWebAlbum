package fr.uga.miashs.album.control;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;

import fr.uga.miashs.album.model.Album;
import fr.uga.miashs.album.service.AlbumService;
import fr.uga.miashs.album.service.ServiceException;

@FacesConverter("src.fr.miashs.album.control.AlbumConvertor")
public class AlbumConvertor implements Converter {

    // @Inject : doesn't work
    AlbumService albumService = new AlbumService();

    @Override
    public Object getAsObject(FacesContext facesContext,
            UIComponent component, String value) {

        Long albumIdLong;

        try {
            albumIdLong = Long.parseLong(value);
        }
        catch ( NumberFormatException e) {
            FacesMessage msg = new FacesMessage("Error converting value to number",
                    "Invalid url parameter");
            msg.setSeverity(FacesMessage.SEVERITY_ERROR);
            throw new ConverterException(msg);
        }

        Album album = null;
        try {
            album = albumService.read(albumIdLong);
        } catch (ServiceException e) {
            FacesMessage msg = new FacesMessage("No album found");
            msg.setSeverity(FacesMessage.SEVERITY_ERROR);
            throw new ConverterException(msg);
        }

        return album;
    }

    @Override
    public String getAsString(FacesContext facesContext,
            UIComponent component, Object value) {
        return String.valueOf(((Album)value).getId());
    }
}

// vim: sw=4 ts=4 et:
