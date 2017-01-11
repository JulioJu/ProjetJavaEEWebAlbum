package fr.uga.miashs.album.model;

import java.net.URI;
import java.nio.file.Path;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

@Entity
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
	
	@NotNull
	private Path localfile;
	
	
}
