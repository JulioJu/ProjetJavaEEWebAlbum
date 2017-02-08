package fr.uga.miashs.album.util;

public interface Pages {

	// For implementation of interface Filter, always add extension .xhtml, even if it isn't
	// mandatory for jsf. Also, do not put « / ».

	String add_album = "add-album.xhtml";
	String list_album_owned = "list-album-owned.xhtml";
	String add_user = "add-user.xhtml";
	String list_user = "list-user.xhtml";
	String login = "login.xhtml";
	String logout = "logout.xhtml";
	String list_picture_owned = "list-picture-owned.xhtml";
	String list_album_shared = "list-album-shared.xhtml";
	String add_picture = "add-picture.xhtml";
	String add_picture_in_spec_album = "add-picture-in-specific-album.xhtml";
	String information_current_user = "information-current-user.xhtml";
	String edit_user = "edit-user.xhtml";
	String edit_album = "edit-album.xhtml";
	String error_403 = "error-403.xhtml";
	String error_404 = "error-404.xhtml";
	String edit_picture = "edit-picture.xhtml";
	String list_picture_shared = "list-picture-shared.xhtml";
	String list_picture_by_album = "list-picture-by-album.xhtml";

	String[] FILTEREDPAGE_CONNECTED = {
		add_album,
		list_album_owned,
		list_picture_owned,
		list_album_shared,
		add_picture,
		add_picture_in_spec_album,
		information_current_user,
		edit_user,
		edit_album,
		edit_picture,
		list_picture_shared,
		list_picture_by_album
	};

}

// vim: sw=4 ts=4 noet:
