import org.apache.thrift.TException;
import java.util.*;
import java.lang.*;

public class MapTask extends Thread {

    public static String NegativeFile = "negative.txt";
    public static String PositiveFile = "positive.txt";

    public MapTask(String serverIP, int serverPort, String inputFilename, String resultFilename)
    {
        this.serverIP = serverIP;
        this.serverPort = serverPort;
        this.inputFilename = inputFilename;
        this.resultFilename = resultFilename;
    }

    public void run()
    {
        try{
            //count
            int negativeCounter = countWords(NegativeFile);
            int positiveCounter = countWords(PositiveFile);
            //calculate sentiment
            float sentiment = 1.0 * (positiveCounter - negativeCounter) / (positiveCounter + negativeCounter);
            //write to result file
            // @todo
            //notice server
            TTransport transport = new TSocket(serverIP, serverPort);
            TProtocol protocol = new TBinaryProtocol(new TFramedTransport(transport));
            //todo
            MasterServer.Client server = new MasterServer.Client(protocol);
            //Try to connect
            transport.open();
            server.noticeFinishedMap(resultFilename);
            transport.close();
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private int countWords(String counterFile)
    {
        // @todo
    }

    private String serverIP;
    private int serverPort;
    private String inputFilename;
    private String resultFilename;
}