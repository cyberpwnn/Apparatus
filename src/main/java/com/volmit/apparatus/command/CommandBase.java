package com.volmit.apparatus.command;

import java.awt.Color;

import com.volmit.apparatus.Config;
import com.volmit.apparatus.ICommand;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.entities.TextChannel;

public abstract class CommandBase implements ICommand
{
	private String[] a;

	public CommandBase(String... a)
	{
		this.a = a;
	}

	@Override
	public String[] getRoot()
	{
		return a;
	}

	@Override
	public abstract void onCommand(String[] a, Guild g, TextChannel c, Message m);

	public boolean isBot(Guild g, Message m)
	{
		return m.getAuthor().isBot();
	}

	public boolean isOwner(Guild g, Message m)
	{
		return g.getMember(m.getAuthor()).isOwner();
	}

	public boolean isStaff(Guild g, Message m)
	{
		if(isOwner(g, m) || hasRole(g, m, g.getRoleById(Config.i.STAFF_ROLE)))
		{
			return true;
		}

		return false;
	}

	public boolean isVet(Guild g, Message m)
	{
		if(isOwner(g, m) || hasRole(g, m, g.getRoleById(Config.i.VETERAN_ROLE)))
		{
			return true;
		}

		return false;
	}

	public boolean isMember(Guild g, Message m)
	{
		if(isOwner(g, m) || hasRole(g, m, g.getRoleById(Config.i.MEMBER_ROLE)))
		{
			return true;
		}

		return false;
	}

	public boolean isCommunity(Guild g, Message m)
	{
		if(isOwner(g, m) || hasRole(g, m, g.getRoleById(Config.i.COMMUNITY_ROLE)))
		{
			return true;
		}

		return false;
	}

	public boolean isBanished(Guild g, Message m)
	{
		if(isOwner(g, m) || hasRole(g, m, g.getRoleById(Config.i.BANISHED_ROLE)))
		{
			return true;
		}

		return false;
	}

	public boolean hasPermission(Guild g, Message m, Permission... perms)
	{
		return g.getMember(m.getAuthor()).hasPermission(perms);
	}

	public boolean hasRole(Guild g, Message m, Role r)
	{
		return g.getMember(m.getAuthor()).getRoles().contains(r);
	}

	public boolean hasRole(Guild g, Message m, long r)
	{
		for(Role i : g.getMember(m.getAuthor()).getRoles())
		{
			if(i.getIdLong() == r)
			{
				return true;
			}
		}

		return false;
	}

	public EmbedBuilder send(String msg)
	{
		EmbedBuilder b = new EmbedBuilder();
		b.setDescription(msg);

		return b;
	}

	public EmbedBuilder sendError(String msg)
	{
		EmbedBuilder b = new EmbedBuilder();
		b.setDescription(msg);
		b.setColor(Color.red);

		return b;
	}

	public EmbedBuilder send(String msg, Color c)
	{
		EmbedBuilder b = send(msg);
		b.setColor(c);

		return b;
	}
}
