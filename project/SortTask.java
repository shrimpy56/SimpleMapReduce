import org.apache.thrift.TException;
import org.apache.thrift.server.*;
import org.apache.thrift.protocol.*;
import org.apache.thrift.transport.*;
import org.apache.thrift.transport.TSSLTransportFactory.TSSLTransportParameters;

import java.util.*;
import java.util.Map.*;
import java.lang.*;
import java.io.*;

public class SortTask extends Thread {

    private Map<String, Double> map = new HashMap<>();

    public SortTask(String serverIP, int serverPort, List<String> inputFilenames, String resultFilename, float loadProbability)
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
            File outputFile = new File(resultFilename);
            outputFile.getParentFile().mkdirs();
            PrintWriter output = new PrintWriter(resultFilename);
            for (int i = files.size()-1; i >= 0; --i)
            {
                String file = files.get(i);
                output.println(file + " " + map.get(file));
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

    private List<String> sortFiles()
    {
        map.clear();
        try {
            for (String filename : inputFilenames) {
                //read file
                File file = new File(filename);
                Scanner input = new Scanner(file);

                while (input.hasNext()) {
                    String key = input.next();
                    double value = input.nextDouble();
                    map.put(key, value);
                }

                input.close();
            }
        } catch (Exception x) {
            x.printStackTrace();
        }
        //sort
        List<Map.Entry<String, Double>> files = new ArrayList<Map.Entry<String, Double>>(map.entrySet());
        Collections.sort(files, new Comparator<Map.Entry<String, Double>>() {
            public int compare(Entry<String, Double> entry1, Entry<String, Double> entry2)
            {
                return entry1.getValue().compareTo(entry2.getValue());
            }
        });
        List<String> ans = new ArrayList<>();
        for (Map.Entry<String, Double> file: files)
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