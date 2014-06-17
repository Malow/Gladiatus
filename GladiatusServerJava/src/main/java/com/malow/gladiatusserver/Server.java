package com.malow.gladiatusserver;

import java.util.ArrayList;
import java.util.List;

import malow.gladiatus.common.models.requests.CharacterInfoRequest;
import malow.gladiatus.common.models.requests.LoginRequest;
import malow.gladiatus.common.models.requests.RegisterRequest;
import malow.gladiatus.common.models.*;

import malow.malowlib.NetworkChannel;
import malow.malowlib.NetworkPacket;
import malow.malowlib.Process;
import malow.malowlib.ProcessEvent;

public class Server extends Process
{
	private List<NetworkChannel> ccs = new ArrayList<NetworkChannel>();

	public void dispose()
	{
		for(int i = 0; i < this.ccs.size(); i++)
		{
			this.ccs.get(i).Close();
		}
		for(int i = 0; i < this.ccs.size(); i++)
		{
			this.ccs.get(i).WaitUntillDone();
		}
		this.ccs = null;
	}
	
	public void clientConnected(NetworkChannel cc)
	{
		this.ccs.add(cc);
		cc.SetNotifier(this);
		cc.Start();
		System.out.println("Client " + this.ccs.size() + " connected.");
	}
	
	public NetworkChannel getClientWithId(long id)
	{
		NetworkChannel nc = null;
		for(NetworkChannel c: this.ccs)
		{
			if(c.GetChannelID() == id)
			{
				nc = c;
			}
		}
		return nc;
	}

	@Override
	public void Life() 
	{
		while(this.stayAlive)
		{
			ProcessEvent ev = this.WaitEvent();
			if(ev instanceof NetworkPacket)
			{
				String msg = ((NetworkPacket) ev).GetMessage();
				NetworkChannel sender = this.getClientWithId(((NetworkPacket) ev).GetSenderID());
				System.out.println("Received from Client " + (sender.GetChannelID() + 1) + ": " + msg);
				ModelInterface request = ConvertStringToModel.toModel(msg);
				if(request instanceof LoginRequest)
				{
					RequestHandler.handleLoginRequest((LoginRequest) request, sender);
				}
				else if(request instanceof RegisterRequest)
				{
					RequestHandler.handleRegisterRequest((RegisterRequest) request, sender);
				}
				else if(request instanceof CharacterInfoRequest)
				{
					RequestHandler.handleCharacterInfoRequest((CharacterInfoRequest) request, sender);
				}
				else
				{
					System.out.println("Unexpected Request from client " + (sender.GetChannelID() + 1) + ": " + request);
				}
			}
		}
	}
}
