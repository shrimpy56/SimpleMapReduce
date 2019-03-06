import org.apache.thrift.TException;
import org.apache.thrift.transport.TTransportFactory;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TFramedTransport;
import org.apache.thrift.transport.TSSLTransportFactory.TSSLTransportParameters;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import java.io.*;
import java.util.*;
import java.lang.*;

public class Client {
    public static void main(String [] args) {
        //Create client connect.
        try {
            // pass params in
            String serverIP = args[0];
            int serverPort = Integer.parseInt(args[1]);
            String input_dir = args[2];

            List<String> inputFiles = new ArrayList<String>();
            File fileDir = new File(input_dir);
            File[] tempList = fileDir.listFiles();
            for (File file : tempList) {
                if (file.isFile()) {
                    inputFiles.add(file.toString());
                    System.out.println(file.toString());
                }
            }
            // String[] inputStrings = inputFiles.toArray(String[inputFiles.size()]);

            TTransport transport = new TSocket(serverIP, serverPort);
            TProtocol protocol = new TBinaryProtocol(new TFramedTransport(transport));
            MasterServer.Client client = new MasterServer.Client(protocol);
            //Try to connect
            transport.open();
            Result res = client.sendTask(inputFiles);
            System.out.println("===============================================");
            System.out.println("Job finished, result is in file: " + res.filename);
            System.out.println("Time used: " + res.timeUsed + "ms");
            System.out.println("===============================================");
            transport.close();
        } catch(TException e) {
            e.printStackTrace();
        }

    }
}
