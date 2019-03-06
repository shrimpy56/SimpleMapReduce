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

public class Server {
    public static ServerHandler handler;
    public static MasterServer.Processor processor;

    public static void main(String [] args) {
        try {
            //pass params in
            int port = Integer.parseInt(args[0]);
            //mode: 1: Random, 2: LoadBalancing
            int mode = Integer.parseInt(args[1]);

            //Create Thrift server socket
            TServerTransport serverTransport = new TServerSocket(port);
            TTransportFactory factory = new TFramedTransport.Factory();

            //Create service request handler
            handler = new ServerHandler();
            handler.setData(port, mode);
            processor = new MasterServer.Processor(handler);

            //Set server arguments
            TThreadPoolServer.Args arguments = new TThreadPoolServer.Args(serverTransport);
            arguments.processor(processor);  //Set handler
            arguments.transportFactory(factory);  //Set FramedTransport (for performance)

            System.out.println("Server IP address:" + InetAddress.getLocalHost().getHostAddress() + ":" + port);
            System.out.println("Mode:" + ((mode == 1)?"Random":"LoadBalancing"));

            //Run server as a single thread
            TServer server = new TThreadPoolServer(arguments);
            server.serve();
        } catch (Exception x) {
            x.printStackTrace();
        }
    }
}

