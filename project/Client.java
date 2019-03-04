import org.apache.thrift.TException;
import org.apache.thrift.transport.TTransportFactory;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TFramedTransport;
import org.apache.thrift.transport.TSSLTransportFactory.TSSLTransportParameters;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;

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
			//todo: client.put("test", "heiheihei");
        } catch(TException e) {
            e.printStackTrace();
        }

    }
}
