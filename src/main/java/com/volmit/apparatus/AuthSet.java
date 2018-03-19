package com.volmit.apparatus;

import java.security.InvalidParameterException;

public class AuthSet
{
	private String token;

	public AuthSet(String[] puke) throws InvalidParameterException
	{
		int k = 0;

		for(String i : puke)
		{
			if(i.startsWith("-token:"))
			{
				token = i.split(":")[1];
				System.out.println("Token: " + token);
				k++;
			}
		}

		if(k != 1)
		{
			throw new InvalidParameterException();
		}
	}

	public String getToken()
	{
		return token;
	}
}
