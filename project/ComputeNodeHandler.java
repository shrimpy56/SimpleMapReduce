import org.apache.thrift.TException;
import java.util.*;
import java.lang.*;

public class ComputeNodeHandler implements ComputeNode.Iface
{
    private String serverIP;
    private int serverPort;
    private String resultFilename;
    private bool balancingMode;
    private float loadProbability;

    void setData(String serverIP, int serverPort, ServerData serverData, float loadProbability)
    {
        this.serverIP = serverIP;
        this.serverPort = serverPort;
        resultFilename = "node" + serverData.nodeID;
        balancingMode = (serverData.mode == 2);
        this.loadProbability = loadProbability;
    }

    @Override
    public String mapTask(String filename) throws org.apache.thrift.TException;
    {
        if (balancingMode)
        {
            double reject = Math.random();
            if (reject < loadProbability){
                return "";
            }
        }

        MapTask mapTask = new MapTask(serverIP, serverPort, filename, resultFilename, loadProbability);
        mapTask.start();
        return resultFilename;
    }

    @Override
    public String sortTask(List<String> filenames) throws org.apache.thrift.TException;
    {
        SortTask sortTask = new SortTask(serverIP, serverPort, filename, resultFilename, loadProbability);
        sortTask.start();
        return resultFilename;
    }
}

