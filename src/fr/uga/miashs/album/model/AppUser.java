package fr.uga.miashs.album.model;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;


@Entity
@NamedQueries({
    @NamedQuery(name="AppUser.findAll",
                query="SELECT u FROM AppUser u"),
    @NamedQuery(name="AppUser.login",
    query="SELECT u FROM AppUser u WHERE u.email=:email AND u.password=:password")
})
@Table(uniqueConstraints=@UniqueConstraint(columnNames="email"))
public class AppUser {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;


    @Pattern(regexp="^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", message="{appuser.email.regex}")
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
        this.admin = admin;
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
