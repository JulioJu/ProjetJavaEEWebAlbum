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
		return Pages.list_album;
	}

	public String logout() {
		FacesContext context = FacesContext.getCurrentInstance();
		((HttpSession) context.getExternalContext().getSession(false)).invalidate();
		connectedUser=null;
		return Pages.logout;
	}

	public String informationOrCreateUserString() {
		if (connectedUser == null)
			return "Create user";
		else
			return "User " + this.getConnectedUser().getFirstname() + this.getConnectedUser().getLastname()  ;
	}

	public String informationOrCreateUserPage() {
		if (connectedUser == null)
			return Pages.add_user;
		else
			return Pages.user_current_information;
	}

	public String loginOrLogoutPage() {
		if (connectedUser == null)
			return Pages.login;
		else
			return this.logout();
	}

	public String loginOrLogoutString() {
		if (connectedUser == null)
			return "Login";
		else
			return "Logout";
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
