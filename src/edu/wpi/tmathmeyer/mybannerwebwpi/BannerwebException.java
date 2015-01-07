package edu.wpi.tmathmeyer.mybannerwebwpi;

public class BannerwebException extends Exception
{
    public BannerwebException(String string)
    {
	    super(string);
    }

	public BannerwebException()
    {
	    this("");
    }

	private static final long serialVersionUID = -4801245993276417855L;

}
