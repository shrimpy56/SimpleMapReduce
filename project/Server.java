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

public class Server {
    public static ServerHandler handler;
    public static MasterServer.Processor processor;

    public static void main(String [] args) {
        try {
            //todo: pass params in

            //Create Thrift server socket
            TServerTransport serverTransport = new TServerSocket(9090);//todo: port
            TTransportFactory factory = new TFramedTransport.Factory();

            //Create service request handler
            handler = new ServerHandler();
            processor = new MasterServer.Processor(handler);

            //Set server arguments
            TServer.Args arguments = new TServer.Args(serverTransport);
            arguments.processor(processor);  //Set handler
            arguments.transportFactory(factory);  //Set FramedTransport (for performance)

            //Run server as a single thread
            TServer server = new TSimpleServer(arguments);
            server.serve();
        } catch (Exception x) {
            x.printStackTrace();
        }
    }
}

