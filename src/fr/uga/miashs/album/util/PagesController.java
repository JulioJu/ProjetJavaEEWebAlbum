package fr.uga.miashs.album.util;

import javax.faces.bean.RequestScoped;
import javax.inject.Named;

@Named
@RequestScoped
public class PagesController {

    private String add_album = "/add-album.xhtml";
    private String list_album = "/list-album.xhtml";
    private String add_user = "/add-user.xhtml";
    private String list_user = "/list-user.xhtml";
    private String login = "/login.xhtml";
    private String logout = "/logout";
    private String list_picture = "/list-picture-owned-by-current-user.xhtml";
    private String list_album_shared = "/list-album-shared";
    private String add_picture = "/add-picture";

    public String getAdd_album() {
        return add_album;
    }

    public String getList_album() {
        return list_album;
    }

    public String getAdd_user() {
        return add_user;
    }

    public String getList_user() {
        return list_user;
    }

    public String getLogin() {
        return login;
    }

    public String getLogout() {
        return logout;
    }

    public String getList_picture() {
        return list_picture;
    }

    public String getList_album_shared() {
        return list_album_shared;
    }

    public String getAdd_picture() {
        return add_picture;
    }

}

// vim: sw=4 ts=4 et:
