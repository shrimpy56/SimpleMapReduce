import org.apache.thrift.TException;
import org.apache.thrift.server.*;
import org.apache.thrift.protocol.*;
import org.apache.thrift.transport.*;
import org.apache.thrift.transport.TSSLTransportFactory.TSSLTransportParameters;

import java.io.*;
import java.util.*;
import java.lang.*;

public class ComputeNodeHandler implements ComputeNode.Iface
{
    private String mapOutputDir = "./data/intermediate/";
    private String sortOutputDir = "./data/result/";

    private String serverIP;
    private int serverPort;
    private boolean balancingMode;
    private float loadProbability;
    private int nodeID;
    private long delayTime;

    void setData(String serverIP, int serverPort, ServerData serverData, float loadProbability, long delay)
    {
        this.serverIP = serverIP;
        this.serverPort = serverPort;
        nodeID = serverData.nodeID;
        balancingMode = (serverData.mode == 2);
        this.loadProbability = loadProbability;
        delayTime = delay;
    }

    @Override
    public boolean mapTask(String filename) throws org.apache.thrift.TException
    {
        //System.out.println("server:"+serverIP+":"+serverPort);

        if (balancingMode)
        {
            double reject = Math.random();
            if (reject < loadProbability)
            {
                System.out.println("map task rejected, file: " + filename);
                return false;
            }
        }
        System.out.println("map task accepted, file: " + filename);

        File tempFile = new File(filename);
        MapTask mapTask = new MapTask(serverIP, serverPort, filename, mapOutputDir+"map_"+tempFile.getName(), loadProbability, delayTime);
        mapTask.start();
        return true;
    }

    @Override
    public void sortTask(List<String> filenames) throws org.apache.thrift.TException
    {
        System.out.println("sort task received.");
        //System.out.println("sort input file [0]:" + filenames.get(0));

        SortTask sortTask = new SortTask(serverIP, serverPort, filenames, sortOutputDir+"Sort", loadProbability);
        sortTask.start();
    }
}

