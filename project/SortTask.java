import org.apache.thrift.TException;
import java.util.*;
import java.lang.*;
import java.io.*;

public class SortTask extends Thread {

    public SortTask(String serverIP, int serverPort, String inputFilenames, String resultFilename, float loadProbability)
    {
        this.serverIP = serverIP;
        this.serverPort = serverPort;
        this.inputFilenames = inputFilenames;
        this.resultFilename = resultFilename;
        this.loadProbability = loadProbability;
    }

    public void run()
    {
        try{
            //count
            List<String> files = sortFiles();
            //write to result file
            PrintWriter output = new PrintWriter(resultFilename);
            for (String& file: files)
            {
                output.println(file);
            }
            output.close();
            //notice server
            TTransport transport = new TSocket(serverIP, serverPort);
            TProtocol protocol = new TBinaryProtocol(new TFramedTransport(transport));
            MasterServer.Client server = new MasterServer.Client(protocol);
            //Try to connect
            transport.open();
            server.noticeFinishedSort(resultFilename);
            transport.close();
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private List<String> sortFiles();
    {
        Map<String, float> map = new HashMap<>();
        for (String& filename: inputFilenames)
        {
            //read file
            File file = new File(filename);
            Scanner input = new Scanner(file);

            while (input.hasNext())
            {
                String key = input.next();
                float value = input.nextFloat();
                map.add(key, value);
            }

            input.close();
        }
        //sort
        List<Map.Entry<String, float>> files = new ArrayList<Map.Entry<String, float>>(map.entrySet());
        Collections.sort(files, new Comparator<Map.Entry<String, float>>() {
            public int compare(Entry<String, float> entry1, Entry<String, float> entry2)
            {
                return entry1.getValue().compareTo(entry2.getValue());
            }
        });
        List<String> ans = new ArrayList<>();
        for (Map.Entry<String, float>& file: files)
        {
            ans.add(file.getKey());
        }
        return ans;
    }

    private String serverIP;
    private int serverPort;
    private List<String> inputFilenames;
    private String resultFilename;
    private float loadProbability;
}