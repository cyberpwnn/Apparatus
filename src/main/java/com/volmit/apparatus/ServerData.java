package com.volmit.apparatus;

import java.awt.EventQueue;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.OffsetDateTime;
import java.util.concurrent.TimeUnit;

import org.cyberpwn.glang.GList;
import org.cyberpwn.glang.GMap;
import org.cyberpwn.gmath.M;
import org.cyberpwn.json.JSONArray;
import org.cyberpwn.json.JSONObject;

import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;

public class ServerData
{
	private Guild guild;
	private GList<TempMessage> msgs;
	private GMap<Long, Integer> msd;

	public ServerData(Guild guild)
	{
		this.guild = guild;
		msgs = new GList<TempMessage>();
		msd = new GMap<Long, Integer>();
	}

	public void update()
	{
		for(TempMessage i : msgs.copy())
		{
			if(M.ms() - i.getAt() > TimeUnit.MINUTES.toMillis(Config.i.STORE_MESSAGES_MINUTES))
			{
				msgs.remove(i);
			}
		}
	}

	public JSONObject save()
	{
		update();
		JSONObject js = new JSONObject();
		js.put("guild-id", guild.getIdLong());
		JSONArray a = new JSONArray();

		for(TempMessage i : msgs)
		{
			a.put(i.toJSON());
		}

		JSONObject ja = new JSONObject();

		for(long i : msd.k())
		{
			ja.put("u" + i, msd.get(i));
		}

		js.put("messages", a);
		js.put("message-stats", ja);

		return js;
	}

	public void write() throws FileNotFoundException
	{
		File fx = new File("g-" + guild.getIdLong() + ".json");
		PrintWriter pw = new PrintWriter(fx);
		pw.println(save().toString(4));
		pw.close();
	}

	public void read() throws IOException
	{
		File fx = new File("g-" + guild.getIdLong() + ".json");

		if(fx.exists())
		{
			String dx = "";
			BufferedReader bu = new BufferedReader(new FileReader(fx));
			String l = "";

			while((l = bu.readLine()) != null)
			{
				dx += l + "\n";
			}

			bu.close();
			load(new JSONObject(dx));
		}
	}

	public void load(JSONObject o)
	{
		msgs.clear();

		for(int i = 0; i < o.getJSONArray("messages").length(); i++)
		{
			TempMessage tm = new TempMessage(o.getJSONArray("messages").getJSONObject(i));
			msgs.add(tm);
		}

		msd.clear();
		JSONObject ol = o.getJSONObject("message-stats");

		for(String i : ol.keySet())
		{
			String id = i.substring(1);
			msd.put(Long.valueOf(id), (int) ol.getInt(i));
		}
	}

	public int getMessagesSent(User user)
	{
		return msd.containsKey(user.getIdLong()) ? msd.get(user.getIdLong()) : 0;
	}

	public long getTimeSinceJoined(User u)
	{
		long then = guild.getMember(u).getJoinDate().toInstant().toEpochMilli();
		long now = OffsetDateTime.now(guild.getMember(u).getJoinDate().getOffset()).toInstant().toEpochMilli();

		return now - then;
	}

	public void saveMessage(Message m)
	{
		msgs.add(new TempMessage(m));
		update();

		if(!msd.containsKey(m.getAuthor().getIdLong()))
		{
			msd.put(m.getAuthor().getIdLong(), 0);
		}

		msd.put(m.getAuthor().getIdLong(), msd.get(m.getAuthor().getIdLong()) + 1);

		handle(m.getAuthor());
	}

	public void handle(User u)
	{
		EventQueue.invokeLater(new Runnable()
		{
			@Override
			public void run()
			{
				try
				{
					Thread.sleep(250);
				}

				catch(InterruptedException e1)
				{
					e1.printStackTrace();
				}

				if(guild.getMember(u).getRoles().contains(guild.getRoleById(Config.i.BANISHED_ROLE)))
				{
					return;
				}

				int sent = msd.get(u.getIdLong());
				long since = getTimeSinceJoined(u);
				Member me = guild.getMember(u);

				Role mem = guild.getRoleById(Config.i.MEMBER_ROLE);
				Role vet = guild.getRoleById(Config.i.VETERAN_ROLE);
				Role com = guild.getRoleById(Config.i.COMMUNITY_ROLE);

				if(mem != null && sent >= Config.i.EARN_MEMBER_MSG && since >= Config.i.EARN_MEMBER_MS && !me.getRoles().contains(mem))
				{
					guild.getController().addRolesToMember(me, mem).queue();
				}

				if(com != null && sent >= Config.i.EARN_COMMUNITY_MSG && since >= Config.i.EARN_COMMUNITY_MS && !me.getRoles().contains(com))
				{
					guild.getController().addRolesToMember(me, com).queue();
				}

				if(vet != null && sent >= Config.i.EARN_VETERAN_MSG && since >= Config.i.EARN_VETERAN_MS && !me.getRoles().contains(vet))
				{
					guild.getController().addRolesToMember(me, vet).queue();
				}

				try
				{
					write();
				}

				catch(FileNotFoundException e)
				{
					e.printStackTrace();
				}
			}
		});
	}

	public void announce(Message msg)
	{
		TextChannel c = guild.getTextChannelById(Config.i.STAFF_CHANNEL);

		if(c != null)
		{
			c.sendMessage(msg).queue();
		}
	}

	public void announce(MessageEmbed msg)
	{
		TextChannel c = guild.getTextChannelById(Config.i.STAFF_CHANNEL);

		if(c != null)
		{
			c.sendMessage(msg).queue();
		}
	}

	public Guild getGuild()
	{
		return guild;
	}

	public GList<TempMessage> getMsgs()
	{
		return msgs;
	}
}
