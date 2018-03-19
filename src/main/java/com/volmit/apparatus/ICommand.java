package com.volmit.apparatus;

import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.TextChannel;

public interface ICommand
{
	public String[] getRoot();

	public void onCommand(String[] a, Guild g, TextChannel c, Message m);
}
