package com.volmit.apparatus;

import java.util.concurrent.TimeUnit;

import org.cyberpwn.glang.GList;
import org.cyberpwn.gmath.M;
import org.cyberpwn.json.JSONArray;
import org.cyberpwn.json.JSONObject;

import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Message;

public class ServerData
{
	private Guild guild;
	private GList<TempMessage> msgs;

	public ServerData(Guild guild)
	{
		this.guild = guild;
		msgs = new GList<TempMessage>();
	}

	public void update()
	{
		for(TempMessage i : msgs)
		{
			if(M.ms() - i.getAt() > TimeUnit.MINUTES.toMillis(Config.STORE_MESSAGES_MINUTES))
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

		js.put("messages", a);

		return js;
	}

	public void write()
	{

	}

	public void read()
	{

	}

	public void load(JSONObject o)
	{
		msgs.clear();

		for(int i = 0; i < o.getJSONArray("messages").length(); i++)
		{
			TempMessage tm = new TempMessage(o.getJSONArray("messages").getJSONObject(i));
			msgs.add(tm);
		}
	}

	public void saveMessage(Message m)
	{
		msgs.add(new TempMessage(m));
		update();
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
