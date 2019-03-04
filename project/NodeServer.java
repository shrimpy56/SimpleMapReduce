import org.apache.thrift.TException;
import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TServer.Args;
import org.apache.thrift.server.TSimpleServer;
import org.apache.thrift.server.TThreadPoolServer;
import org.apache.thrift.transport.TTransportFactory;
import org.apache.thrift.transport.TFramedTransport;
import org.apache.thrift.transport.TServerSocket;
import org.apache.thrift.transport.TServerTransport;
import org.apache.thrift.transport.TSSLTransportFactory.TSSLTransportParameters;
import java.net.InetAddress;

public class NodeServer {
//    public static ComputeNodeHandler handler;
//    public static ComputeNode.Processor processor;
    private float loadProbability = 0;
    private int port = 9099;
    private ServerData serverData;
    String serverIP;
    int serverPort;

    public static void main(String [] args) {
        try {
//            handler = new ComputeNodeHandler();
//            processor = new ComputeNode.Processor(handler);

            // pass master server ip, server port, node port and loadprobability in.
            serverIP = String.parseString(args[0]);
            serverPort = Integer.parseInt(args[1]);
            port = Integer.parseInt(args[2]);
            loadProbability = Float.parseFloat(args[3]);

            // register node
            TTransport transport = new TSocket(serverIP, serverPort);
            TProtocol protocol = new TBinaryProtocol(new TFramedTransport(transport));
            MasterServer.Client server = new MasterServer.Client(protocol);
            transport.open();
            serverData = server.registerNode(InetAddress.getLocalHost().getHostAddress(), port);
            transport.close();

            Runnable simple = new Runnable() {
                public void run() {
                    simple();
                }
            };

            new Thread(simple).start();
        } catch (Exception x) {
            x.printStackTrace();
        }
    }

    public static void simple() {
        try {
            //Create Thrift server socket
            TServerTransport serverTransport = new TServerSocket(port);
            TTransportFactory factory = new TFramedTransport.Factory();

            //Create service request handler
            ComputeNodeHandler handler = new ComputeNodeHandler();
            handler.setData(serverIP, serverPort, serverData, loadProbability);
            ComputeNode.Processor processor = new ComputeNode.Processor(handler);

            //Set server arguments
            TServer.Args args = new TServer.Args(serverTransport);
            args.processor(processor);  //Set handler
            args.transportFactory(factory);  //Set FramedTransport (for performance)

            //Run server as a single thread
            TServer server = new TThreadPoolServer(args);
            server.serve();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

