import org.apache.thrift.TException;
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
    //
    private int mode;

    void setData()
    {
        //todo
    }

    @Override
    public String job(List<String> filenames) throws org.apache.thrift.TException
    {
        //todo
        return null;
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
        //todo
    }

    @Override
    public void noticeFinishedSort(String resultFilename) throws org.apache.thrift.TException
    {
        //todo
    }
}

