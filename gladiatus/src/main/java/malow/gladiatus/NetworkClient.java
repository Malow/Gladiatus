package malow.gladiatus;


import android.util.Log;

import malow.gladiatus.common.models.ConvertStringToModel;
import malow.gladiatus.common.models.ModelInterface;
import malow.malowlib.RequestResponseClient;

public class NetworkClient
{
    private static RequestResponseClient nc = null;
    private static final String SERVER_IP = "83.233.58.215";
    private static final int SERVER_PORT = 7000;

    private static void createNetworkClientIfNeeded()
    {
        if(nc == null)
        {
            nc = new RequestResponseClient(SERVER_IP, SERVER_PORT);
        }
        else if(nc.isAlive() == false)
        {
            nc.Close();
            nc = new RequestResponseClient(SERVER_IP, SERVER_PORT);
        }
    }

    public static ModelInterface sendAndReceive(ModelInterface model) throws RequestResponseClient.ConnectionBrokenException
    {
        createNetworkClientIfNeeded();
        String msg = model.toNetworkString();
        String response = nc.sendAndReceive(msg);
        return ConvertStringToModel.toModel(response);
    }
}
