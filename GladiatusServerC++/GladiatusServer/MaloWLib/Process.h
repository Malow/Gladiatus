#ifndef PROCESS_H
#define PROCESS_H

#include <iostream>
#include "WindowsWrapper.h"
#include "Queue.h"
#include "MaloWFileDebug.h"
#include "MaloW.h"

using namespace std;

/*
INHERIT THIS CLASS!
Inheriting this class will get your class its own thread that runs the function Life(); 
Implement the code you want your class to execute there.
You must .start the object for it to start running the Life();
*/

// Doubles every time the threshold is reached to avoid exessive error reporting.
#define DEFAULT_WARNING_THRESHOLD_EVENTQUEUE_FULL 250

namespace MaloW
{
	enum ProcessState
	{
		NOT_STARTED		= 0,
		WAITING			= 1,
		RUNNING			= 2,
		FINISHED		= 3
	};

	class ProcessEvent
	{
	public:
		virtual ~ProcessEvent() { }
	};

	class Process
	{
	private:
		HANDLE hThread;
		DWORD threadID;
		ProcessState state;
		Queue<ProcessEvent*>* EvQueue;
		HANDLE ProcMtx;
		
		static long nextPID;
		int WarningThresholdEventQueue;
		long id;

		bool debug;

	protected:
		bool stayAlive;
			

	public:
		Process();
		virtual ~Process();
		
		virtual void Life() = 0;

		void Start();
		void Suspend();
		void Resume();
		void Close();
		virtual void CloseSpecific() { };

		void WaitUntillDone();

		ProcessEvent* WaitEvent();
		ProcessEvent* PeekEvent();
		void PutEvent(ProcessEvent* ev, bool important = true);

		ProcessState getState() { return this->state; }
		void setState(ProcessState state) { this->state = state; }

		long getID() { return this->id; }
		long getNrOfProcs() { return this->nextPID; }
		int GetEventQueueSize() const { return this->EvQueue->size(); }

		void FlushQueue();

		static unsigned long __stdcall threadProc(void* p)
		{
			((Process*)p)->Life();
			((Process*)p)->setState(FINISHED);
			return 0;
		}
	};
}



#endif
