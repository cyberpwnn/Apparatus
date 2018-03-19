package com.volmit.apparatus.command;

import java.awt.EventQueue;

import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.TextChannel;

public class CommandReboot extends CommandBase
{
	public CommandReboot()
	{
		super("die", "reset", "reboot");
	}

	@Override
	public void onCommand(String[] a, Guild g, TextChannel c, Message m)
	{
		if(!isStaff(g, m))
		{
			c.sendMessage(sendError("No").build()).queue();
			return;
		}

		c.sendMessage(send("Kay bbs bby ly2 cya glhf bai.").build()).complete();

		EventQueue.invokeLater(new Runnable()
		{
			@Override
			public void run()
			{
				try
				{
					Thread.sleep(1000);
				}

				catch(InterruptedException e)
				{
					e.printStackTrace();
				}

				System.exit(0);
			}
		});
	}
}
