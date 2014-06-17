#ifndef NETWORKPACKET_H
#define NETWORKPACKET_H

#include "Process.h"

namespace MaloW
{
	class NetworkPacket : public MaloW::ProcessEvent
	{
	private:
		string message;
		int id;

	public:
		NetworkPacket(string message, int SenderID) { this->message = message; this->id = SenderID; }
		virtual ~NetworkPacket() { }
		string GetMessage() { return this->message; }
		int GetSenderID() { return this->id; }
	};
}

#endif