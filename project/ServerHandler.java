import org.apache.thrift.TException;
import java.util.*;
import java.lang.*;

public class ServerHandler implements MasterServer.Iface
{
//    private List of nodes
    private int mode;

    void setData()
    {
    }

    @Override
    public String job(List<String> filenames) throws org.apache.thrift.TException
    {
        //todo
    }

    @Override
    public ServerData registerNode(String ip, int port) throws org.apache.thrift.TException
    {
        //todo
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

