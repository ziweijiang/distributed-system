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
            TTransport  transport = new TSocket("localhost", 9091);
            TProtocol protocol = new TBinaryProtocol(new TFramedTransport(transport));
            Manipulate.Client client = new Manipulate.Client(protocol);

            //Try to connect
            transport.open();

            //What you need to do.
			client.ping();
            Pair p = new Pair("Alice", "CS");
            boolean ret1 = client.put(p);
            if(ret1) System.out.printf("I put to the server successfully\n");
            else System.out.printf("Failure!");

			String ret2 = client.get("Alice");
            System.out.printf("I got %s from the server\n", ret2);
        } catch(TException e) {

        }

    }
}
