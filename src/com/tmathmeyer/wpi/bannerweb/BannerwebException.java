package com.tmathmeyer.wpi.bannerweb;

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
