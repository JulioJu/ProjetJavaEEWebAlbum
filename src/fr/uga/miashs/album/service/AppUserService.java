package fr.uga.miashs.album.service;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import javax.persistence.EntityExistsException;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.RollbackException;

import fr.uga.miashs.album.model.AppUser;

@Named
@ApplicationScoped
public class AppUserService extends JpaService<Long,AppUser> {

	private static final long serialVersionUID = 3975934852005119859L;

	private void setField(AppUser v) throws ServiceException {
		try {
			super.create(v);
		}
		catch (RollbackException e) {
			if (e.getCause() instanceof EntityExistsException) {
				throw new ServiceException("Un utilisateur avec l'email "+v.getEmail()+" existe déjà",e);
			}
			else {
				new ServiceException(e);
			}
		}
	}

	@Override
	public void create(AppUser v) throws ServiceException {
		v.setDateCreated();
		this.setField(v);
		super.create(v);
	}

	@Override
	public void edit(AppUser v) throws ServiceException {
		this.setField(v);
		super.edit(v);
	}


	public AppUser login(String email, String password) throws ServiceException {
		Query query = getEm().createNamedQuery("AppUser.login");
		query.setParameter("email", email);
		query.setParameter("password", password);
		try {
			return (AppUser) query.getSingleResult();
		}
		catch (NoResultException e) {
			throw new ServiceException("Utilisateur Inconnu",e);
		}
	}

	public List<AppUser> listUsers() throws ServiceException {
		 Query query = getEm().createNamedQuery("AppUser.findAll");
		 return query.getResultList();
	}
}

// vim: sw=4 ts=4 noet:
