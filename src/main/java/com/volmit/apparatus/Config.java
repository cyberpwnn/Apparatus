package com.volmit.apparatus;

import java.awt.Color;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import net.dv8tion.jda.core.EmbedBuilder;

public class Config implements Serializable
{
	private static final long serialVersionUID = 1234L;
	public static Config i = new Config();

	public int STORE_MESSAGES_MINUTES = 30;
	public long STAFF_ROLE = -1337;
	public long MEMBER_ROLE = -1337;
	public long BANISHED_ROLE = -1337;
	public long VETERAN_ROLE = -1337;
	public long COMMUNITY_ROLE = -1337;
	public long STAFF_CHANNEL = -6969;
	public int EARN_MEMBER_MSG = 3;
	public int EARN_COMMUNITY_MSG = 120;
	public int EARN_VETERAN_MSG = 345;
	public long EARN_MEMBER_MS = 60000;
	public long EARN_COMMUNITY_MS = 2592000000L;
	public long EARN_VETERAN_MS = 15778800000L;

	public Config()
	{

	}

	public static void load() throws ClassNotFoundException, IOException
	{
		File f = new File("config.gg");

		if(f.exists())
		{
			ObjectInputStream ii = new ObjectInputStream(new FileInputStream(f));
			i = (Config) ii.readObject();
			ii.close();
		}
	}

	public static void save() throws IOException
	{
		File f = new File("config.gg");
		ObjectOutputStream oo = new ObjectOutputStream(new FileOutputStream(f));
		oo.writeObject(i);
		oo.close();
	}

	public static EmbedBuilder set(String field, String value) throws IOException
	{
		EmbedBuilder b = new EmbedBuilder();
		b.setColor(Color.white);
		b.setDescription("???");

		for(Field i : Config.class.getDeclaredFields())
		{
			if(Modifier.isStatic(i.getModifiers()))
			{
				continue;
			}

			String ff = field.toUpperCase().replaceAll("-", "_");
			String vv = value.replaceAll("_", " ");

			if(i.getName().equals(ff))
			{
				if(i.getType().equals(int.class) || i.getType().equals(Integer.class))
				{
					try
					{
						i.set(Config.i, Integer.valueOf(vv));
						b.setColor(Color.WHITE);
						b.setDescription(ff + " updated to " + vv);
					}

					catch(Exception e)
					{
						b.setColor(Color.red);
						b.setDescription("Invalid value for type: " + vv + " (" + i.getClass().getSimpleName() + ")");
					}
				}

				if(i.getType().equals(long.class) || i.getType().equals(Long.class))
				{
					try
					{
						i.set(Config.i, Long.valueOf(vv));
						b.setColor(Color.WHITE);
						b.setDescription(ff + " updated to " + vv);
					}

					catch(Exception e)
					{
						b.setColor(Color.red);
						b.setDescription("Invalid value for type: " + vv + " (" + i.getClass().getSimpleName() + ")");
					}
				}

				if(i.getType().equals(boolean.class) || i.getType().equals(Boolean.class))
				{
					try
					{
						i.set(Config.i, Boolean.valueOf(vv));
						b.setColor(Color.WHITE);
						b.setDescription(ff + " updated to " + vv);
					}

					catch(Exception e)
					{
						b.setColor(Color.red);
						b.setDescription("Invalid value for type: " + vv + " (" + i.getClass().getSimpleName() + ")");
					}
				}

				if(i.getType().equals(double.class) || i.getType().equals(Double.class))
				{
					try
					{
						i.set(Config.i, Double.valueOf(vv));
						b.setColor(Color.WHITE);
						b.setDescription(ff + " updated to " + vv);
					}

					catch(Exception e)
					{
						b.setColor(Color.red);
						b.setDescription("Invalid value for type: " + vv + " (" + i.getClass().getSimpleName() + ")");
					}
				}

				if(i.getType().equals(String.class))
				{
					try
					{
						i.set(Config.i, vv);
						b.setColor(Color.WHITE);
						b.setDescription(ff + " updated to " + vv);
					}

					catch(Exception e)
					{
						b.setColor(Color.red);
						b.setDescription("Invalid value for type: " + vv + " (" + i.getClass().getSimpleName() + ")");
					}
				}

				save();
				return b;
			}
		}

		return b;
	}

	public static EmbedBuilder getConfig() throws IllegalArgumentException, IllegalAccessException
	{
		EmbedBuilder b = new EmbedBuilder();
		b.setColor(Color.YELLOW);
		b.setDescription("The current apparatus configuration");

		for(Field i : Config.class.getDeclaredFields())
		{
			if(Modifier.isStatic(i.getModifiers()))
			{
				continue;
			}

			b.addField(i.getName(), "(" + i.getType().getSimpleName() + ") " + i.get(Config.i), true);
		}

		return b;
	}
}
