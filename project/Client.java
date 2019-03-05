import org.apache.thrift.TException;
import org.apache.thrift.transport.TTransportFactory;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TFramedTransport;
import org.apache.thrift.transport.TSSLTransportFactory.TSSLTransportParameters;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import java.io.File；

public class Client {
    public static void main(String [] args) {
        //Create client connect.
        try {
            //todo: pass params in



            TTransport  transport = new TSocket("localhost", 9090);//todo: port
            TProtocol protocol = new TBinaryProtocol(new TFramedTransport(transport));
            MasterServer.Client client = new MasterServer.Client(protocol);
            //Try to connect
            transport.open();
            //What you need to do.
            String input_dir = args[0];

            List<String> inputFiles = new ArrayList<String>();
            File fileDir = new File(input_dir);
            File[] tempList = fileDir.listFiles();
            for (File file : tempList) {
                if (file.isFile()) {
                    inputFiles.add(file.toString());
                }
            }
            // String[] inputStrings = inputFiles.toArray(String[inputFiles.size()]);
            Result res = client.sendTask(inputFiles);
			// client.put("test", "heiheihei");
            // String str = client.get("test");
            for (String name : res.resultList) {
                System.out.println(name);
            }
            System.out.println(res.timeUsed);
            System.out.println("finish job from service!");
        } catch(TException e) {
            System.out.println("Error occurs!!!");
        }

    }
}
