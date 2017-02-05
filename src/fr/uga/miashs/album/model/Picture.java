package fr.uga.miashs.album.model;

import java.io.Serializable;
import java.util.Calendar;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.servlet.http.Part;
import javax.validation.constraints.NotNull;

import fr.uga.miashs.album.service.ServiceException;

@Entity
@NamedQueries({
    @NamedQuery(name="Picture.findAllOwned",
                query="SELECT p FROM Picture p Join p.album a WHERE a.owner=:owner"),
    @NamedQuery(name="Picture.findAllFromOneAlbum",
                query="SELECT p FROM Picture p WHERE p.album=:album"),
})
public class Picture implements Serializable {

    private static final long serialVersionUID = 7546332103904404129L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @ManyToOne
    private Album album;

    @NotNull
    private String title;

    @Temporal(TemporalType.TIME)
    private Calendar dateCreated;

    @Lob
    @NotNull
    private byte[] file;

    private String contentType;

    @Transient
    private AppUser owner;

    @Transient
    private long albumId;

    @Transient
    private Part part;

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

    public Calendar getDateCreated() {
        return dateCreated;
    }

    public byte[] getFile() {
        return file;
    }

    public void setFile(byte[] file) throws ServiceException {
        // if Files.size < 0.9 MB
        // By Default MySql restrict file > 1MB
        // http://www.codejava.net/coding/upload-files-to-database-servlet-jsp-mysql
        if (file.length > 900000) {
            throw new ServiceException("Not allowed to save file > 1MB in Database");
        }
        this.file = file;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public AppUser getOwner() {
        return owner;
    }

    public void setOwner(AppUser owner) {
        this.owner = owner;
    }

    public Long getAlbumId() {
        return albumId;
    }

    public Part getPart() {
        return part;
    }

    public void setPart(Part part) {
        this.part = part;
    }

    public void setAlbumId(Long albumId) {
        this.albumId = albumId;
    }

    public void setOwner() {
        this.owner = album.getOwner();
    }

    public void setDateCreated() {
        this.dateCreated = Calendar.getInstance();
    }

}

// vim: set sw=4 ts=4 et:
