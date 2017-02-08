package fr.uga.miashs.album.control;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;

import fr.uga.miashs.album.model.Picture;
import fr.uga.miashs.album.service.PictureService;
import fr.uga.miashs.album.service.ServiceException;

@FacesConverter("src.fr.miashs.album.control.PictureConvertor")
public class PictureConvertor implements Converter {

    // @Inject : doesn't work
    PictureService pictureService = new PictureService();

    @Override
    public Object getAsObject(FacesContext facesContext,
            UIComponent component, String value) {

        Long pictureIdLong;

        try {
            pictureIdLong = Long.parseLong(value);
        }
        catch ( NumberFormatException e) {
            FacesMessage msg = new FacesMessage("Error converting value to number",
                    "Invalid url parameter");
            msg.setSeverity(FacesMessage.SEVERITY_ERROR);
            throw new ConverterException(msg);
        }

        Picture picture = null;
        try {
            picture = pictureService.read(pictureIdLong);
        } catch (ServiceException e) {
            FacesMessage msg = new FacesMessage("No album found");
            msg.setSeverity(FacesMessage.SEVERITY_ERROR);
            throw new ConverterException(msg);
        }

        return picture;
    }

    @Override
    public String getAsString(FacesContext facesContext,
            UIComponent component, Object value) {
        return String.valueOf(((Picture)value).getId());
    }

}

// vim: sw=4 ts=4 et:
