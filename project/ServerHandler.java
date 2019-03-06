import org.apache.thrift.TException;
import org.apache.thrift.server.*;
import org.apache.thrift.protocol.*;
import org.apache.thrift.transport.*;
import org.apache.thrift.transport.TSSLTransportFactory.TSSLTransportParameters;
import java.io.*;
import java.util.*;
import java.lang.*;

class NodeInfo
{
    String IP;
    int port;
}

public class ServerHandler implements MasterServer.Iface
{
    private List<NodeInfo> nodeList = new ArrayList<>();
    private int port;
    //mode: 1: Random, 2: LoadBalancing
    private int mode;
    private boolean sortFinished;
    private String resultFilename;
    private List<String> mapResults = new ArrayList<>();
    private List<String> mapFilenames;

    void setData(int port, int mode)
    {
        this.port = port;
        this.mode = mode;
    }

    @Override
    public Result sendTask(List<String> filenames) throws org.apache.thrift.TException
    {
        mapResults.clear();
        mapFilenames = filenames;
        sortFinished = false;

        // timer
        long startTime = System.currentTimeMillis();

        for (int i = 0; i < filenames.size(); ++i)
        {
            //if does not exist, exit
            File file = new File(filenames.get(i));
            if (!file.exists())
            {
                return null;
            }

            boolean flag = true;
            while (flag) {
                int idx = (int) (Math.random() * nodeList.size());

                TTransport transport = new TSocket(nodeList.get(idx).IP, nodeList.get(idx).port);
                TProtocol protocol = new TBinaryProtocol(new TFramedTransport(transport));
                ComputeNode.Client nodeClient = new ComputeNode.Client(protocol);
                transport.open();
                if (nodeClient.mapTask(filenames.get(i))) {
                    System.out.println("Map task successfully assigned on node "+ idx + ", address: " + nodeList.get(idx).IP + nodeList.get(idx).port);
                    flag = false;
                }
                transport.close();
            }
        }

        try {
            // waiting
            while (!sortFinished) {
                Thread.sleep(1);
            }
        } catch (Exception x) {
            x.printStackTrace();
        }

        long timeUsed = System.currentTimeMillis() - startTime;

        System.out.println("===============================================");
        System.out.println("Job finished, map task number: "+ mapFilenames.size() +", time used: " + timeUsed + "ms");
        System.out.println("===============================================");

        Result res = new Result();
        res.filename = resultFilename;
        res.timeUsed = timeUsed;
        return res;
    }

    @Override
    public ServerData registerNode(String ip, int port) throws org.apache.thrift.TException
    {
        NodeInfo info = new NodeInfo();
        info.IP = ip;
        info.port = port;
        nodeList.add(info);
        ServerData data = new ServerData();
        data.mode = mode;
        data.nodeID = nodeList.size();
        return data;
    }

    @Override
    public void noticeFinishedMap(String resultFilename) throws org.apache.thrift.TException
    {
        System.out.println("Map job finish: " + resultFilename);

        mapResults.add(resultFilename);

        if (mapResults.size() == mapFilenames.size())
        {
            //sort
            int idx = (int) (Math.random() * nodeList.size());

            TTransport transport = new TSocket(nodeList.get(idx).IP, nodeList.get(idx).port);
            TProtocol protocol = new TBinaryProtocol(new TFramedTransport(transport));
            ComputeNode.Client nodeClient = new ComputeNode.Client(protocol);
            transport.open();
            nodeClient.sortTask(mapResults);
            transport.close();
        }
    }

    @Override
    public void noticeFinishedSort(String resultFilename) throws org.apache.thrift.TException
    {
        System.out.println("Sort job finish: " + resultFilename);

        this.resultFilename = resultFilename;
        sortFinished = true;
    }
}

