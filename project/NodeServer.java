import org.apache.thrift.TException;
import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TServer.Args;
import org.apache.thrift.server.TSimpleServer;
import org.apache.thrift.server.TThreadedServer;
import org.apache.thrift.server.TThreadPoolServer;
import org.apache.thrift.transport.TTransportFactory;
import org.apache.thrift.transport.TFramedTransport;
import org.apache.thrift.transport.TServerSocket;
import org.apache.thrift.transport.TServerTransport;
import org.apache.thrift.transport.TSSLTransportFactory.TSSLTransportParameters;

public class NodeServer {
//    public static ComputeNodeHandler handler;
//    public static ComputeNode.Processor processor;

    public static void main(String [] args) {
        try {
//            handler = new ComputeNodeHandler();
//            processor = new ComputeNode.Processor(handler);
            //todo: pass ip and port in.
            String ip = String.parseString(args[0]);
            int port = Integer.parseInt(args[1]);

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
            TServerTransport serverTransport = new TServerSocket(9099);//todo:port?
            TTransportFactory factory = new TFramedTransport.Factory();

            //Create service request handler
            ComputeNodeHandler handler = new ComputeNodeHandler();
            ComputeNode.Processor processor = new ComputeNode.Processor(handler);

            //Set server arguments
            TServer.Args args = new TServer.Args(serverTransport);
            args.processor(processor);  //Set handler
            args.transportFactory(factory);  //Set FramedTransport (for performance)

            //Run server as a single thread
            TServer server = new TThreadedServer(args);
            server.serve();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

