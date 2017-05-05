package fr.uga.miashs.album.model;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

@Entity
// @Table(uniqueConstraints=@UniqueConstraint(columnNames="title"))
@NamedQueries({
	@NamedQuery(name="Album.findAllOwned",
				query="SELECT a FROM Album a WHERE a.owner=:owner"),
	@NamedQuery(name="Album.findAllShared",
				// s.id refers to AppUser.id
				query="SELECT a FROM Album a join a.sharedWith s WHERE s.id=:currentUser"),
})
public class Album implements Serializable {

	private static final long serialVersionUID = 48489996319161129L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	@NotNull
	private String title;

	private String description;

	@NotNull
	@ManyToOne
	private AppUser owner;

	// https://vladmihalcea.com/2015/03/05/a-beginners-guide-to-jpa-and-hibernate-cascade-types/
	@ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
	private Set<AppUser> sharedWith;

	// http://www.objectdb.com/java/jpa/persistence/delete
	@OneToMany(mappedBy="album", cascade = CascadeType.ALL, orphanRemoval=true)
	private Set<Picture> pictures;

	@NotNull
	@Temporal(TemporalType.TIME)
	private Calendar dateCreated;

	@Transient
	private Long[] sharedWithArray;

	@Transient
	private Long[] noSharedWithArray;

	public Album(AppUser owner) {
		this.owner=owner;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public AppUser getOwner() {
		return owner;
	}

	public void setOwner(AppUser owner) {
		this.owner = owner;
	}

	public long getId() {
		return id;
	}

	public Set<AppUser> getSharedWith() {
		return sharedWith;
	}

	public void setSharedWith(Set<AppUser> sharedWith) {
		this.sharedWith = sharedWith;
	}

	public Long[] getSharedWithArray() {
		return sharedWithArray;
	}

	public void setSharedWithArray(Long[] sharedWithArray) {
		this.sharedWithArray = sharedWithArray;
	}

	public Long[] getNoSharedWithArray() {
		return noSharedWithArray;
	}

	public void setNoSharedWithArray(Long[] noSharedWithArray) {
		this.noSharedWithArray = noSharedWithArray;
	}

	public Set<Picture> getPictures() {
		return pictures;
	}

	public Calendar getDateCreated() {
		return dateCreated;
	}

	public void setDateCreated() {
		this.dateCreated = Calendar.getInstance();
	}

}

// vim: sw=4 ts=4 noet:
