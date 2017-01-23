package fr.uga.miashs.album.model;

import java.net.URI;
import java.net.URISyntaxException;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

@Entity
@NamedQueries({
    @NamedQuery(name="Picture.findAllOwned",
                query="SELECT p FROM Picture p Join p.album a WHERE a.owner=:owner"),
    @NamedQuery(name="Picture.findAllFromOneAlbum",
                query="SELECT p FROM Picture p WHERE p.album=:album"),
})
public class Picture {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @ManyToOne
    private Album album;

    @NotNull
    private String title;

    @NotNull
    private URI uri;

    @Transient
    private AppUser owner;

    @Transient
    private String albumId;

    @Transient
    private String uriString;

    public Picture() {
        // For when server bind to database
        if (album != null)
            this.setOwner(album.getOwner());
    }

    public Picture(AppUser owner) {
        this.owner=owner;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Album getAlbum() {
        return album;
    }

    public void setAlbum(Album album) {
        if (album == null) {
            throw new NullPointerException("Album is null");
        }
        // TODO Check if album is in Database
        // else if ()
        this.album = album;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public URI getUri() {
        return uri;
    }

    public void setUri(URI uri) {
        try {
            this.uri = new URI(uri.toString());

        } catch (URISyntaxException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void setUri(String uriString) {
        try {
            this.uri = new URI("file://", this.getAlbumId() + "/" + this.getUriString(), "");

        } catch (URISyntaxException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public AppUser getOwner() {
        return owner;
    }

    public void setOwner(AppUser owner) {
        this.owner = owner;
    }

    public String getAlbumId() {
        return albumId;
    }

    public void setAlbumId(String albumId) {
        this.albumId = albumId;
    }

    public String getUriString() {
        return uriString;
    }

    public void setUriString(String fileString) {
        this.uriString = fileString;
    }

    public void setOwner() {
        this.owner = album.getOwner();
    }

}

// vim: set sw=4 ts=4 et:
