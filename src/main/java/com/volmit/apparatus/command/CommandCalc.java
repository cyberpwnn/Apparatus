package com.volmit.apparatus.command;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.TextChannel;

public class CommandCalc extends CommandBase
{
	public CommandCalc()
	{
		super("math", "js", "calc", "/calc");
	}

	@SuppressWarnings("deprecation")
	@Override
	public void onCommand(String[] a, Guild g, TextChannel c, Message m)
	{
		if(!isMember(g, m))
		{
			c.sendMessage(sendError("You need the member role to use this.").build()).queue();
			return;
		}

		if(a.length == 0)
		{
			c.sendMessage(sendError("Use it like this /calc 2 + 2").build()).queue();
		}

		String r[] = {""};

		for(String i : a)
		{
			r[0] += i.replaceAll("\n", " ") + " ";
		}

		Thread tt = new Thread()
		{
			@Override
			public void run()
			{
				try
				{
					String function = r[0];
					function = function.trim().replaceAll("```", "").replaceAll("`", "");
					ScriptEngineManager mgr = new ScriptEngineManager();
					ScriptEngine scriptEngine = mgr.getEngineByName("JavaScript");
					String value = scriptEngine.eval(function).toString();
					c.sendMessage(send("Ding! -> " + value).build()).queue();
				}

				catch(Throwable e)
				{
					c.sendMessage(sendError("ERR -> " + e.getMessage()).build()).queue();

				}
			}
		};

		tt.start();

		try
		{
			tt.join(500);

			if(tt.isAlive())
			{
				System.out.println("OVERFLOW");
				c.sendMessage(sendError("Computational Overflow").addField("Stop it.", "", true).build()).queue();
				tt.interrupt();
				tt.stop();
				tt.destroy();
			}
		}

		catch(Throwable e1)
		{

		}
	}
}
