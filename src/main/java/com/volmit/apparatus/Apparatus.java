package com.volmit.apparatus;

import java.awt.Color;

import javax.security.auth.login.LoginException;

import org.cyberpwn.gformat.F;
import org.cyberpwn.glang.GMap;
import org.cyberpwn.gmath.M;

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

	public Apparatus(AuthSet auth) throws LoginException, IllegalArgumentException, InterruptedException, RateLimitedException
	{
		jda = new JDABuilder(AccountType.BOT).setToken(auth.getToken()).addEventListener(this).buildBlocking();
		data = new GMap<Guild, ServerData>();
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
				channel.sendMessage(b.build()).queue();
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
		}
	}

	public ServerData getData(Guild g)
	{
		if(!data.containsKey(g))
		{
			ServerData ss = new ServerData(g);
			ss.read();
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
	}
}
