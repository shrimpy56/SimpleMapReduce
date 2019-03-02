import org.apache.thrift.TException;
import java.util.*;
import java.lang.*;

public class ComputeNodeHandler implements ComputeNode.Iface
{
    //private Map<String, String> storage = new HashMap<>();
    //todo
    private String serverIP;
    private int serverPort;
    private String resultFilename;

    @Override
    public String mapTask(String filename) throws org.apache.thrift.TException;
    {
        MapTask mapTask = new MapTask(serverIP, serverPort, filename, resultFilename);
        mapTask.start();
        return resultFilename;
    }

    @Override
    public String sortTask(List<String> filenames) throws org.apache.thrift.TException;
    {
        SortTask sortTask = new MapTask(serverIP, serverPort, filename, resultFilename);
        sortTask.start();
        return resultFilename;
    }
}

