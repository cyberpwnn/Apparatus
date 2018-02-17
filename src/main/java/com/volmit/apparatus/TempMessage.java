package com.volmit.apparatus;

import org.cyberpwn.glang.GList;
import org.cyberpwn.gmath.M;
import org.cyberpwn.json.JSONArray;
import org.cyberpwn.json.JSONObject;

import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.Message.Attachment;

public class TempMessage
{
	private String messageContent;
	private GList<String> attachments;
	private long id;
	private long member;
	private long at;
	private long ch;

	public TempMessage(Message msg)
	{
		messageContent = msg.getContent();
		attachments = new GList<String>();
		id = msg.getIdLong();
		member = msg.getAuthor().getIdLong();
		at = M.ms();
		ch = msg.getTextChannel().getIdLong();

		for(Attachment i : msg.getAttachments())
		{
			attachments.add(i.getUrl());
		}
	}

	public TempMessage(JSONObject msg)
	{
		messageContent = msg.getString("content");
		attachments = new GList<String>();
		id = msg.getLong("id");
		member = msg.getLong("author");
		at = msg.getLong("at");
		ch = msg.getLong("ch");

		for(Object i : msg.getJSONArray("attachments"))
		{
			attachments.add(i.toString());
		}
	}

	public String getMessageContent()
	{
		return messageContent;
	}

	public GList<String> getAttachments()
	{
		return attachments;
	}

	public long getId()
	{
		return id;
	}

	public long getMember()
	{
		return member;
	}

	public long getAt()
	{
		return at;
	}

	public long getCh()
	{
		return ch;
	}

	public JSONObject toJSON()
	{
		JSONObject j = new JSONObject();
		j.put("content", getMessageContent());
		j.put("at", getAt());
		j.put("id", getId());
		j.put("ch", getCh());
		j.put("author", getMember());

		JSONArray a = new JSONArray();

		for(String i : getAttachments())
		{
			a.put(i);
		}

		j.put("attachments", a);

		return j;
	}
}
