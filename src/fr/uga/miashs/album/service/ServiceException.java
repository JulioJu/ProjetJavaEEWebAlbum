package fr.uga.miashs.album.service;

public class ServiceException extends Exception {

	private static final long serialVersionUID = 5710590006378073805L;

	public ServiceException() {
		super();
		// TODO Auto-generated constructor stub
	}

	public ServiceException(String message) {
		super(message);
	}

	public ServiceException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

	public ServiceException(String message, Throwable e) {
		super(message,e);
	}

}

// vim: sw=4 ts=4 noet:
