import org.apache.thrift.TException;
import java.util.*;
import java.lang.*;

public class SortTask extends Thread {

    public static String NegativeFile = "negative.txt";
    public static String PositiveFile = "positive.txt";

    public SortTask(String serverIP, int serverPort, String inputFilenames, String resultFilename)
    {
        this.serverIP = serverIP;
        this.serverPort = serverPort;
        this.inputFilenames = inputFilenames;
        this.resultFilename = resultFilename;
    }

    public void run()
    {
        try{
//            //count
//            int negativeCounter = countWords(NegativeFile);
//            int positiveCounter = countWords(PositiveFile);
//            //calculate sentiment
//            float sentiment = 1.0 * (positiveCounter - negativeCounter) / (positiveCounter + negativeCounter);
//            //write to result file
//            // @todo
//            //notice server
//            TTransport transport = new TSocket(serverIP, serverPort);
//            TProtocol protocol = new TBinaryProtocol(new TFramedTransport(transport));
//            MasterServer.Client server = new MasterServer.Client(protocol);
//            //Try to connect
//            transport.open();
//            server.noticeFinishedMap(resultFilename);
//            transport.close();
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private String serverIP;
    private int serverPort;
    private List<String> inputFilenames;
    private String resultFilename;
}