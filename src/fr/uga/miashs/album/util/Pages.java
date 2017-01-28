package fr.uga.miashs.album.util;

public interface Pages {

	// For implementation of interface Filter, always add extension .xhtml, even if it isn't
	// mandatory for jsf. Also, do not put « / ».

	String add_album = "add-album.xhtml";
	String list_album = "list-album.xhtml";
	String add_user = "add-user.xhtml";
	String list_user = "list-user.xhtml";
	String login = "login.xhtml";
	String logout = "logout.xhtml";
	String list_picture_owned = "list-picture-owned-by-current-user.xhtml";
	String list_album_shared = "list-album-shared.xhtml";
	String add_picture = "add-picture.xhtml";
	String information_current_user = "information-current-user.xhtml";
	String edit_user = "edit-user.xhtml";
	String edit_album = "edit-album.xhtml";
	String error_403 = "error-403.xhtml";

	String[] FILTEREDPAGE_CONNECTED = {
		add_album,
		list_album,
		list_picture_owned,
		add_picture,
		information_current_user,
        edit_user,
		edit_album,
	};

}

// vim: sw=4 ts=4 noet:
