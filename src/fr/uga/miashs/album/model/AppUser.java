package fr.uga.miashs.album.model;

import java.io.Serializable;
import java.util.Calendar;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import fr.uga.miashs.album.control.AppUserSession;


@Entity
@NamedQueries({
	@NamedQuery(name="AppUser.findAll",
				query="SELECT u FROM AppUser u"),
	@NamedQuery(name="AppUser.login",
	query="SELECT u FROM AppUser u WHERE u.email=:email AND u.password=:password")
})
@Table(uniqueConstraints=@UniqueConstraint(columnNames="email"))
public class AppUser implements Serializable {

	private static final long serialVersionUID = 6859900262663311670L;

	@Inject
	private AppUserSession appUserSession;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	// https://www.w3.org/TR/2012/WD-html-markup-20120320/input.email.html
	@Pattern(regexp="^[a-zA-Z0-9.!#$%&’*+/=?^_`{|}~-]+@[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)*$"
	,message="{appuser.email.regex}")
	@NotNull
	private String email;

	@NotNull
	private String lastname;

	@NotNull(message="{appuser.firstname.notnull}")
	private String firstname;

	@NotNull
	private String password;

	@OneToMany(mappedBy="owner")
	private List<Album> userAlbums;

	@NotNull
	private boolean admin;

	@NotNull
	@Temporal(TemporalType.TIME)
	private Calendar dateCreated;

	public AppUser() {
	}

	public long getId() {
		return id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public List<Album> getUserAlbums() {
		return userAlbums;
	}

	public boolean isAdmin() {
		return admin;
	}

	public void setAdmin(boolean admin) {
		if (appUserSession != null
				&& appUserSession.getConnectedUser() != null
				&& !appUserSession.getConnectedUser().isAdmin()) {
			throw new SecurityException("An unauthorized user have tried to grant his privileges");
		}
		else if (appUserSession != null
				&& appUserSession.getConnectedUser() != null)
			this.admin = admin;
	}

	public Calendar getDateCreated() {
		return dateCreated;
	}

	public void setDateCreated() {
		this.dateCreated = Calendar.getInstance();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (id ^ (id >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AppUser other = (AppUser) obj;
		if (id != other.id)
			return false;
		return true;
	}

}

// vim: sw=4 ts=4 noet:
