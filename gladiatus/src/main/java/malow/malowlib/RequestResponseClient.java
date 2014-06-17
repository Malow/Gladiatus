package malow.malowlib;

import java.lang.*;

public class RequestResponseClient extends Process
{
    private String ip;
    private int port;
    private NetworkChannel nc;
    private Process notifier;

    private String response = null;

    public RequestResponseClient(String ip, int port)
    {
        this.ip = ip;
        this.port = port;
        this.Start();
    }

    public String sendAndReceive(String msg)
    {
        this.nc.SendData(msg);

        while(this.response == null)
        {
            try
            {
                Thread.sleep(10);
            } catch (InterruptedException e)
            {
                e.printStackTrace();
            }
        }
        String resp = this.response;
        this.response = null;
        return resp;
    }

    public boolean isAlive()
    {
        return this.nc.GetState() == Process.RUNNING;
    }

    @Override
    public void Life()
    {
        nc = new NetworkChannel(ip, port);
        nc.SetNotifier(this);
        nc.Start();

        while(this.stayAlive)
        {
            ProcessEvent ev = this.WaitEvent();
            if(ev instanceof NetworkPacket)
            {
                this.response = ((NetworkPacket) ev).GetMessage();
            }
        }

        this.nc.Close();
        this.nc.WaitUntillDone();
        this.nc = null;
    }
}
