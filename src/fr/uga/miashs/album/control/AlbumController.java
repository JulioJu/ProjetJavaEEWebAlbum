package fr.uga.miashs.album.control;

import java.io.IOException;
import java.nio.file.FileSystemNotFoundException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import fr.uga.miashs.album.model.Album;
import fr.uga.miashs.album.service.AlbumService;
import fr.uga.miashs.album.service.ServiceException;
import fr.uga.miashs.album.util.Pages;

@Named
@RequestScoped
public class AlbumController {

	@Inject
	private AppUserSession appUserSession;

	@Inject
	private AlbumService albumService;

	private Album album;

	public Album getAlbum() {
		// useful when launch view add-album.xhtml
		if (album==null) {
			album = new Album(appUserSession.getConnectedUser());
		}
		return album;
	}

	public String createAlbum() {

		// TODO a folder named ~/workspace/PhotoAlbumJavaEEPictures should be created
		String homeDir = System.getProperty("user.home");
		Path directory = Paths.get(homeDir,"/workspace/PhotoAlbumJavaEEPictures");
		if (!Files.isDirectory(directory)) {
			throw new FileSystemNotFoundException(directory + " does not exist or is not a directory. Please create a directory named '" + directory + "'.");
		}

		try {
			albumService.create(album);
		} catch (ServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Path albumDirectory = Paths.get(directory + "/" + album.getId());
		if (Files.exists(albumDirectory)) {
			albumService.deleteById(album.getId());
			throw new FileSystemNotFoundException("Please delete '" + albumDirectory + "'. No action performed");
		}
		try {
			Files.createDirectory(albumDirectory);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return Pages.list_album;
	}

	// TODO EntityTransaction commit method is void. But how to check than commit
	// was performed with success ? See JpaService.java
	public String delete(long albumId) {

		album = albumService.read(albumId);
		if (album == null) {
			throw new NullPointerException("The album with id " + albumId + " that you want to destroy not exists in database");
		}

		// TODO test with bool value
		albumService.deleteById(albumId);

		// TODO a folder named ~/workspace/PhotoAlbumJavaEEPictures should be created
		String homeDir = System.getProperty("user.home");
		Path directory = Paths.get(homeDir,"/workspace/PhotoAlbumJavaEEPictures");
		if (!Files.isDirectory(directory)) {
			throw new FileSystemNotFoundException("Root diretory '" + directory + "does not exist or is not a directory.");
		}
		Path albumDirectory = Paths.get(directory + "/" + album.getId());
		if (!Files.isDirectory(albumDirectory)) {
			throw new FileSystemNotFoundException("'" + albumDirectory + "' is not a directory. Fatal error.");
		}
		try {
			Files.delete(albumDirectory);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		return Pages.list_album;
	}

	public List<Album> getListAlbumOwnedByCurrentUser() {
		try {
			return albumService.listAlbumOwnedBy(appUserSession.getConnectedUser());
		} catch (ServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}

// vim: sw=4 ts=4 noet:
