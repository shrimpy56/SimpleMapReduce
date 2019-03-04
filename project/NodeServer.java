import org.apache.thrift.TException;
import org.apache.thrift.server.*;
import org.apache.thrift.protocol.*;
import org.apache.thrift.transport.*;
import org.apache.thrift.transport.TSSLTransportFactory.TSSLTransportParameters;
import java.net.InetAddress;

import java.io.*;
import java.util.*;
import java.lang.*;

public class NodeServer {

    public static void main(String [] args) {
        try {
            // pass master server ip, server port, node port and loadprobability in.
            String serverIP = args[0];
            int serverPort = Integer.parseInt(args[1]);
            int port = Integer.parseInt(args[2]);
            float loadProbability = Float.parseFloat(args[3]);

            // register node
            TTransport transport = new TSocket(serverIP, serverPort);
            TProtocol protocol = new TBinaryProtocol(new TFramedTransport(transport));
            MasterServer.Client serverClient = new MasterServer.Client(protocol);
            transport.open();
            ServerData serverData = serverClient.registerNode(InetAddress.getLocalHost().getHostAddress(), port);
            transport.close();

            //Create Thrift server socket
            TServerTransport serverTransport = new TServerSocket(port);
            TTransportFactory factory = new TFramedTransport.Factory();

            //Create service request handler
            ComputeNodeHandler handler = new ComputeNodeHandler();
            handler.setData(serverIP, serverPort, serverData, loadProbability);
            ComputeNode.Processor processor = new ComputeNode.Processor(handler);

            //Set server arguments
            TThreadPoolServer.Args arguments = new TThreadPoolServer.Args(serverTransport);
            arguments.processor(processor);  //Set handler
            arguments.transportFactory(factory);  //Set FramedTransport (for performance)

            //Run server as a single thread
            TServer server = new TThreadPoolServer(arguments);
            server.serve();
        } catch (Exception x) {
            x.printStackTrace();
        }
    }
}

