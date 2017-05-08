package fr.uga.miashs.album.control;

import java.io.Serializable;

import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpSession;

import fr.uga.miashs.album.model.AppUser;
import fr.uga.miashs.album.service.AppUserService;
import fr.uga.miashs.album.service.ServiceException;
import fr.uga.miashs.album.util.Pages;

/**
 * NamedBean pour la gestion de la session utilisateur et de la page de login.
 * @author jdavid
 *
 */
/*
 * On a choisi ApplicationScoped car une seule instance de chaque service suffit à l'application
 * Ce choix de reporte sur toute les sous classes
 * Si on ne met rien @RequestScoped est choisi par défaut
 * ==> @TODO say to teacher : wrong :
 * https://docs.oracle.com/javaee/7/tutorial/cdi-basic008.htm
 * Default scope is Dependent
 */
@Named
@SessionScoped
public class AppUserSession implements Serializable {

	private static final long serialVersionUID = -1076122964728342081L;

	@Inject
	private AppUserService appUserService;

	private String email;
	private String password;

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
		return Pages.list_album_owned;
	}

	public void logout() {
		FacesContext context = FacesContext.getCurrentInstance();
		((HttpSession) context.getExternalContext().getSession(false)).invalidate();
		connectedUser=null;
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

// vim: set ts=4 sw=4 noet:
