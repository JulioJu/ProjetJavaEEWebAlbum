package fr.uga.miashs.album.control;

import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.inject.Inject;
import javax.inject.Named;

import fr.uga.miashs.album.model.Album;
import fr.uga.miashs.album.model.AppUser;
import fr.uga.miashs.album.model.Picture;
import fr.uga.miashs.album.service.AlbumService;
import fr.uga.miashs.album.service.AppUserService;
import fr.uga.miashs.album.service.ServiceException;
import fr.uga.miashs.album.util.Pages;

@Named
@ConversationScoped
// In Album.java, @Inject AppUserService appUserService doesn't work, I don't know why
// In edit-album.xhtml, in list "Add share with user", when one is selected, in a view appeare
// "xx:noSharedWithArray: Validation Error: Value is not valid"
// See http://stackoverflow.com/questions/9069379/validation-error-value-is-not-valid

// In Facelet, immediate=true attribute cause a NullPointerException (objects are destroyed)
// In command button, pass them as <f:param…> no resolve this problem
// With <h:link…>, methode is executed before page loading.

// An other scope cause error when submit form in edit_album.xhtml
// @ViewScoped doesn't work with cdi Bean. I've tried with Omnifaces @ViewScoped, but crash tomee.
// Some persons say @ConversationScoped it's not good
// http://stackoverflow.com/questions/6419442/cdi-weld-how-to-handle-browser-page-refresh-after-ending-conversation

// For fix it, I've also tried to put Set<AppUser>noSharedWithArray in album model many hours,
// but it raise a NullPointerException on login, with method "appUserService.listUser()"

// When append ?cid=[number], raise error with Java EE.
// This parameter is add when we leave a page with long-running conversation (conversation.bigin())
// without end conversation (conversation.end()).
// Fix this issue with http://www.programcreek.com/java-api-examples/index.php?source_dir=primefaces-starter-master/primefaces-webapp/src/main/java/com/mycompany/lifecycle/ConversationExceptionFilter.java
// See also http://www.programcreek.com/java-api-examples/index.php?api=javax.enterprise.context.NonexistentConversationException
// Works only in Production mode (configure it in web.xml)
// Line 100 of ConversationExceptionFilter.java, same thing with NonexistentConversationException
// Maybe try http://www.programcreek.com/java-api-examples/index.php?source_dir=seam-booking-ogm-master/src/main/java/org/jboss/seam/examples/booking/exceptioncontrol/ConversationExceptionHandler.java with Jboss

public class AlbumController implements Serializable {

	private static final long serialVersionUID = 2729195466703888571L;
	@Inject
	private AppUserSession appUserSession;
	@Inject
	private AlbumService albumService;
	@Inject
	private AppUserService appUserService;
	@Inject
	private Conversation conversation;

	private Album album;

	public Conversation getConversation() {
		return conversation;
	}

	public void setConversation(Conversation conversation) {
		this.conversation = conversation;
	}

	public void initConversation(){
		conversation.begin();
	}

	public String endConversation(){
		conversation.end();
		// Mandotory, otherwise append "?cid=[nubmer]"
		return "?faces-redirect=true";
	}

	public String viewPictureByAlbum(Long albumIdRetrieveFromView) {
		try {
			this.album = albumService.read(albumIdRetrieveFromView);
		} catch (ServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return Pages.list_picture_by_album;
	}

	public Album getAlbum() {
		// useful when launch view add-album.xhtml
		if (album==null) {
			album = new Album(appUserSession.getConnectedUser());
		}
		return album;
	}

	public void setAlbum(Album album) {
		this.album = album;
	}

	public String blop() {
		return Pages.login;
	}

	private boolean isAllowedModify() {
		if (appUserSession.getConnectedUser().isAdmin()
				|| album.getOwner().equals(appUserSession.getConnectedUser()))
			return true;
		return false;
	}

	public String create() {
		if (!this.isAllowedModify())
			return Pages.error_403;
		try {
			albumService.create(album);
		} catch (ServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return Pages.list_album_owned;
	}

	public String edit() {
		if (!this.isAllowedModify())
			return Pages.error_403;
		try {
			albumService.edit(album);
		} catch (ServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return Pages.list_album_owned + this.endConversation();
	}

	public String delete(long albumId) {
		try {
			album = albumService.read(albumId);
		} catch (ServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (album == null) {
			// TODO : give message
			throw new NullPointerException("The album with id " + albumId + " that you want to destroy not exists in database");
		}
		if (!isAllowedModify())
			return Pages.error_403;
		try {
			albumService.deleteById(albumId);
		} catch (ServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return Pages.list_album_owned;
	}

	public List<Album> getListAlbumOwnedByCurrentUser() {
		try {
			return albumService.listAlbumOwnedBy(appUserSession.getConnectedUser());
		} catch (ServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public List<Album> getListAlbumShared() {
		try {
			return albumService.listAlbumShared(appUserSession.getConnectedUser());
		} catch (ServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public Set<AppUser> getNoSharedWith(){
		Set<AppUser> userList = new HashSet<AppUser>();
		try {
			userList = new HashSet<AppUser>(appUserService.listUsers());
		} catch (ServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		userList.removeAll(album.getSharedWith());
		return userList;
	}

	public Set<Picture> getPictures(){
		return this.album.getPictures();
	}

}

// vim: sw=4 ts=4 noet:
