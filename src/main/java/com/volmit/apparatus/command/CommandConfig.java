package com.volmit.apparatus.command;

import java.io.IOException;

import com.volmit.apparatus.Config;

import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.TextChannel;

public class CommandConfig extends CommandBase
{
	public CommandConfig()
	{
		super("configuration", "config", "conf", "cfg");
	}

	@Override
	public void onCommand(String[] a, Guild g, TextChannel c, Message m)
	{
		if(!isStaff(g, m))
		{
			c.sendMessage(sendError("No").build()).queue();
			return;
		}

		if(a.length == 0)
		{
			try
			{
				c.sendMessage(Config.getConfig().build()).queue();
			}

			catch(IllegalArgumentException | IllegalAccessException e)
			{
				e.printStackTrace();
				c.sendMessage(sendError("Failed to read: ```" + e.toString() + "```").build()).queue();

			}
		}

		if(a.length >= 2)
		{
			try
			{
				if(a[0].toLowerCase().endsWith("_role"))
				{
					if(m.getMentionedRoles().size() == 1)
					{
						c.sendMessage(Config.set(a[0], "" + m.getMentionedRoles().get(0).getIdLong()).build()).queue();
					}

					else
					{
						c.sendMessage(sendError("Too few or too many mentions").build()).queue();
					}
				}

				else if(a[0].toLowerCase().endsWith("_channel"))
				{
					if(m.getMentionedChannels().size() == 1)
					{
						c.sendMessage(Config.set(a[0], "" + m.getMentionedChannels().get(0).getIdLong()).build()).queue();
					}

					else
					{
						c.sendMessage(sendError("Too few or too many mentions").build()).queue();
					}
				}

				else
				{
					c.sendMessage(Config.set(a[0], a[1]).build()).queue();
				}
			}

			catch(IOException e)
			{
				e.printStackTrace();
			}
		}

		if(a.length == 1)
		{
			if(a[0].equalsIgnoreCase("reload"))
			{
				try
				{
					Config.save();
					Config.load();
					c.sendMessage(send("Configuration Reloaded").build()).queue();
				}

				catch(Exception e)
				{
					e.printStackTrace();
					c.sendMessage(sendError("Configuration Failed to reload").build()).queue();
				}
			}
		}
	}
}
