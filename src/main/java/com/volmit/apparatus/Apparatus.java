package com.volmit.apparatus;

import javax.security.auth.login.LoginException;

import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.events.Event;
import net.dv8tion.jda.core.exceptions.RateLimitedException;
import net.dv8tion.jda.core.hooks.EventListener;

public class Apparatus implements EventListener
{
	private AuthSet auth;
	private JDA jda;

	public Apparatus(AuthSet set) throws LoginException, IllegalArgumentException, InterruptedException, RateLimitedException
	{
		jda = new JDABuilder(AccountType.BOT).setToken(auth.getToken()).addEventListener(this).buildBlocking();
	}

	public JDA getJDA()
	{
		return jda;
	}

	@Override
	public void onEvent(Event event)
	{
		// Something happened somewhere on the fucking planet
	}

	public static void main(String[] a)
	{
		try
		{
			new Apparatus(new AuthSet(a));
		}

		catch(LoginException | IllegalArgumentException | InterruptedException | RateLimitedException e)
		{
			e.printStackTrace();
		}
	}
}
