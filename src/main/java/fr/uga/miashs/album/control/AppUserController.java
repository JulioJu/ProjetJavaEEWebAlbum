package fr.uga.miashs.album.control;

import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import fr.uga.miashs.album.model.AppUser;
import fr.uga.miashs.album.service.AppUserService;
import fr.uga.miashs.album.service.ServiceException;
import fr.uga.miashs.album.util.Pages;


/*
 * L'annotation @Named permet de créer un bean CDI
 * Le bean CDI va remplacer les ManagedBean de JSF à partir de JSF 2.3
 * Leur intéret est qu'ils sont utilisables en dehors du contexte JSF.
 * On peut les utiliser aussi via l'annotation @Inject
 * Il faut faire attention que l'annotation @RequestScoped vienne bien du package
 * javax.enterprise.context et non de l'ancien package javax.faces.bean
 */
@Named
@RequestScoped
public class AppUserController {

	@Inject
	private AppUserService appUserService;
	@Inject
	private AppUserSession appUserSession;

	private AppUser user;

	public AppUser getUser() {
		if (user == null)
			user = new AppUser();
		return user;
	}

	private boolean isAllowedModify() {
		if (appUserSession.getConnectedUser().isAdmin()
				|| appUserSession.getConnectedUser().equals(user))
			return true;
		return false;
	}

	public String create() {
		try {
			user.setAdmin(false);
			appUserService.create(user);
		} catch (ServiceException e) {
			FacesContext facesContext = FacesContext.getCurrentInstance();
			FacesMessage facesMessage = new FacesMessage(e.getLocalizedMessage());
			facesContext.addMessage("email", facesMessage);
			return null;
		}
		return Pages.list_user;
	}

	public String edit() {
		AppUser userTmp = user;
		FacesContext ctx = FacesContext.getCurrentInstance();
		Long userId;
		try {
		userId = Long.valueOf(ctx.getExternalContext().
				getRequestParameterMap().get("userId"));
		}
		catch (NumberFormatException e) {
			// TODO
			throw new RuntimeException("You have passed wrong parameters");
		}
		try {
			user = appUserService.read(userId);
		} catch (ServiceException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		if (!this.isAllowedModify())
			return Pages.error_403;
		user.setFirstname(userTmp.getFirstname());
		user.setLastname(userTmp.getLastname());
		if (appUserSession.getConnectedUser().isAdmin())
				user.setAdmin(userTmp.getAdminString().equals("true"));
		user.setPassword(userTmp.getPassword());
		try {
			appUserService.edit(user);
		} catch (ServiceException e) {
			FacesContext facesContext = FacesContext.getCurrentInstance();
			FacesMessage facesMessage = new FacesMessage(e.getLocalizedMessage());
			facesContext.addMessage("email", facesMessage);
			return null;
		}
		return Pages.list_user;
	}

	public String viewEditPage(Long userIdRetrieveFromView) {
		try {
			this.user = appUserService.read(userIdRetrieveFromView);
		} catch (ServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return Pages.edit_user;
	}

	public String delete(long userId) {
		try {
			appUserService.deleteById(userId);
		} catch (ServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (!this.isAllowedModify())
			return Pages.error_403;
		return Pages.list_user;
	}

	public List<AppUser> listUsers() throws ServiceException {
		return appUserService.listUsers();
	}

}

// vim: sw=4 ts=4 noet:
