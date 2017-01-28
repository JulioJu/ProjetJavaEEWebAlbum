package fr.uga.miashs.album.model;

import java.util.Calendar;
import java.util.Set;

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
	// s => AppUser
	@NamedQuery(name="Album.findAllShared",
				query="SELECT a FROM Album a join a.sharedWith s WHERE s.id=:current_user"),
})
public class Album {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	@NotNull
	private String title;

	private String description;

	@NotNull
	@ManyToOne
	private AppUser owner;

	@ManyToMany
	private Set<AppUser> sharedWith;

	@Transient
	private Long[] sharedWithArray;

	@Transient
	private Long[] removeSharedWithArray;

	@OneToMany(mappedBy="album")
	private Set<Picture> pictures;

	@Temporal(TemporalType.TIME)
	private Calendar dateCreated;

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

	public Long[] getRemoveSharedWithArray() {
		return removeSharedWithArray;
	}

	public void setRemoveSharedWithArray(Long[] removeSharedWithArray) {
		this.removeSharedWithArray = removeSharedWithArray;
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
