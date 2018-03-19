package com.volmit.apparatus.command;

import java.awt.Color;

import com.volmit.apparatus.Apparatus;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;

public class CommandPardon extends CommandBase
{
	public CommandPardon()
	{
		super("pardon", "unbanish", "unban");
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
			c.sendMessage(pardon(g, i)).queue();
		}
	}

	public MessageEmbed pardon(Guild g, User u)
	{
		g.getController().removeRolesFromMember(g.getMember(u), g.getMember(u).getRoles()).complete();
		Apparatus.inst.getData(g).handle(u);
		EmbedBuilder e = new EmbedBuilder();
		e.setColor(Color.RED);
		e.setDescription(u.getName() + " Has been pardoned.");
		return e.build();
	}
}
