package fr.uga.miashs.album.control;

import java.io.Serializable;

import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpSession;

import fr.uga.miashs.album.model.Album;
import fr.uga.miashs.album.model.AppUser;
import fr.uga.miashs.album.service.AlbumService;
import fr.uga.miashs.album.service.AppUserService;
import fr.uga.miashs.album.service.ServiceException;
import fr.uga.miashs.album.util.Pages;

/**
 * NamedBean pour la gestion de la session utilisateur et de la page de login.
 * @author jdavid
 *
 */
@Named
@SessionScoped
public class AppUserSession implements Serializable {

	@Inject
	private AppUserService appUserService;
	
	@Inject
	private AlbumService albumService;
	
	private String email;
	private String password;
	
	private Album currentAlbum;
	private AppUser connectedUser;
	
	public String login() {
		
		try {
			connectedUser = appUserService.login(email, password);
		} catch (ServiceException e) {
			FacesContext facesContext = FacesContext.getCurrentInstance();
			FacesMessage facesMessage = new FacesMessage("Email ou mot de passe incorrects");
			// ajoute le message pour toute la page (1er param==null)
			facesContext.addMessage(null, facesMessage);
			return null;
		}
		return Pages.list_album;
	}
	
	public String logout() {
		FacesContext context = FacesContext.getCurrentInstance();
		((HttpSession) context.getExternalContext().getSession(false)).invalidate();
		connectedUser=null;
		return "";
	}
	
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	public AppUser getConnectedUser() {
		return connectedUser;
	}
}
