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
            TTransport  transport = new TSocket("localhost", 9090);
            TProtocol protocol = new TBinaryProtocol(new TFramedTransport(transport));
            KeyValueStorage.Client client = new KeyValueStorage.Client(protocol);

            //Try to connect
            transport.open();

            //What you need to do.
			client.put("test", "heiheihei");
            String str = client.get("test");
            System.out.printf("I got %s from the server\n", str);
        } catch(TException e) {

        }

    }
}
