package com.volmit.apparatus;

import java.awt.Color;
import java.io.IOException;

import javax.security.auth.login.LoginException;

import org.cyberpwn.gformat.F;
import org.cyberpwn.glang.GList;
import org.cyberpwn.glang.GMap;
import org.cyberpwn.gmath.M;

import com.volmit.apparatus.command.CommandBanish;
import com.volmit.apparatus.command.CommandCalc;
import com.volmit.apparatus.command.CommandConfig;
import com.volmit.apparatus.command.CommandPardon;
import com.volmit.apparatus.command.CommandReboot;
import com.volmit.apparatus.command.CommandUser;

import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.events.Event;
import net.dv8tion.jda.core.events.message.guild.GuildMessageDeleteEvent;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.core.exceptions.RateLimitedException;
import net.dv8tion.jda.core.hooks.EventListener;

public class Apparatus implements EventListener
{
	private JDA jda;
	private GMap<Guild, ServerData> data;
	private GList<ICommand> cmd;
	public static Apparatus inst;

	public Apparatus(AuthSet auth) throws LoginException, IllegalArgumentException, InterruptedException, RateLimitedException, ClassNotFoundException, IOException
	{
		jda = new JDABuilder(AccountType.BOT).setToken(auth.getToken()).addEventListener(this).buildBlocking();
		data = new GMap<Guild, ServerData>();
		cmd = new GList<ICommand>();
		inst = this;
		cmd.add(new CommandConfig());
		cmd.add(new CommandUser());
		cmd.add(new CommandBanish());
		cmd.add(new CommandPardon());
		cmd.add(new CommandReboot());
		cmd.add(new CommandCalc());
		Config.load();
	}

	public JDA getJDA()
	{
		return jda;
	}

	@Override
	public void onEvent(Event event)
	{
		if(event instanceof GuildMessageDeleteEvent)
		{
			GuildMessageDeleteEvent e = (GuildMessageDeleteEvent) event;
			ServerData data = getData(e.getGuild());
			TextChannel channel = e.getChannel();
			Member member = null;
			String content = null;
			TempMessage mv = null;

			for(TempMessage i : data.getMsgs())
			{
				if(i.getId() == e.getMessageIdLong())
				{
					mv = i;
					content = i.getMessageContent();
					Member m = e.getGuild().getMemberById(i.getMember());

					if(m != null)
					{
						member = m;
					}

					break;
				}
			}

			if(content != null)
			{
				EmbedBuilder b = new EmbedBuilder();
				b.setTitle("Someone deleted a message");
				b.setColor(new Color(66, 244, 89));
				b.setDescription(content);
				b.addField("Author", member != null ? member.getAsMention() : "No longer here.", true);
				b.addField("Deleted", F.time(M.ms() - mv.getAt(), 0) + " ago", true);
				channel.getGuild().getTextChannelById(Config.i.STAFF_CHANNEL).sendMessage(b.build()).queue();
			}
		}

		if(event instanceof GuildMessageReceivedEvent)
		{
			Guild g = ((GuildMessageReceivedEvent) event).getGuild();
			Message m = ((GuildMessageReceivedEvent) event).getMessage();
			TextChannel c = ((GuildMessageReceivedEvent) event).getChannel();

			if(m.getAuthor().isBot())
			{
				return;
			}

			getData(g).saveMessage(m);

			if(m.getContent().startsWith("/"))
			{
				String command = m.getContent().substring(1);

				for(ICommand i : cmd)
				{
					for(String j : i.getRoot())
					{
						if(command.toLowerCase().startsWith(j))
						{
							String s = command.replace(j, "").trim();
							String[] a = s.split(" ");

							if(a.length == 1 && a[0].trim().equalsIgnoreCase(""))
							{
								a = new String[0];
							}

							m.addReaction("⛏").queue();
							i.onCommand(a, g, c, m);
							m.clearReactions().queue();
							return;
						}
					}
				}
			}
		}
	}

	public ServerData getData(Guild g)
	{
		if(!data.containsKey(g))
		{
			ServerData ss = new ServerData(g);

			try
			{
				ss.read();
			}

			catch(IOException e)
			{
				e.printStackTrace();
			}

			data.put(g, ss);
		}

		return data.get(g);
	}

	public void sendMessage(TextChannel c, Message msg)
	{
		sendTyping(c);
		c.sendMessage(msg);
	}

	public void sendMessage(TextChannel c, MessageEmbed msg)
	{
		sendTyping(c);
		c.sendMessage(msg);
	}

	public void sendTyping(TextChannel c)
	{
		c.sendTyping().queue();
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
		catch(ClassNotFoundException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch(IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
