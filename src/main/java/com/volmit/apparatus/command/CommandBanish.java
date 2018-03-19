package com.volmit.apparatus.command;

import java.awt.Color;

import com.volmit.apparatus.Config;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;

public class CommandBanish extends CommandBase
{
	public CommandBanish()
	{
		super("ban", "banish");
	}

	@Override
	public void onCommand(String[] a, Guild g, TextChannel c, Message m)
	{
		if(!isStaff(g, m))
		{
			c.sendMessage(sendError("No").build()).queue();
			return;
		}

		if(m.getMentionedUsers().isEmpty())
		{
			c.sendMessage(sendError("No mentions").build()).queue();
			return;
		}

		for(User i : m.getMentionedUsers())
		{
			c.sendMessage(ban(g, i)).queue();
		}
	}

	public MessageEmbed ban(Guild g, User u)
	{
		Role r = g.getRoleById(Config.i.BANISHED_ROLE);

		g.getController().removeRolesFromMember(g.getMember(u), g.getMember(u).getRoles()).complete();
		g.getController().addSingleRoleToMember(g.getMember(u), r).complete();

		EmbedBuilder e = new EmbedBuilder();
		e.setColor(Color.RED);
		e.setDescription(u.getName() + " HAS BEEN BANISHED TO THE DEPTHS OF GARBAGE");
		return e.build();
	}
}
