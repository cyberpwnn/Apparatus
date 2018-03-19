package com.volmit.apparatus.command;

import java.awt.Color;

import org.cyberpwn.gformat.F;

import com.volmit.apparatus.Apparatus;
import com.volmit.apparatus.ServerData;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;

public class CommandUser extends CommandBase
{
	public CommandUser()
	{
		super("stats", "sts", "usr", "user");
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
			c.sendMessage(proper(g, m.getAuthor())).queue();
		}

		else
		{
			c.sendMessage(proper(g, m.getMentionedUsers().get(0))).queue();
		}
	}

	public MessageEmbed proper(Guild g, User u)
	{
		ServerData sd = Apparatus.inst.getData(g);
		long fd = sd.getTimeSinceJoined(u);
		long v = fd;
		String su = "second";

		if(fd / 1000 / 60 > 0)
		{
			v = fd / 1000 / 60;
			su = " minute";

			if(v / 60 > 0)
			{
				v = v / 60;
				su = " hour";

				if(v / 7 > 0)
				{
					v = v / 7;
					su = " week";

					if(v / 4 > 0)
					{
						v = v / 4;
						su = " month";

						if(v / 12 > 0)
						{
							v = v / 12;
							su = " year";
						}
					}
				}
			}
		}

		else
		{
			v = fd / 1000 / 60;
		}

		if(v != 1)
		{
			su += "s";
		}

		EmbedBuilder e = new EmbedBuilder();
		e.setColor(Color.white);
		e.setDescription("Showing Statistics for " + u.getName());
		e.addField("Messages Sent", F.f(sd.getMessagesSent(u)), true);
		e.addField("Joined", v + " " + su + " ago", true);
		return e.build();
	}
}
