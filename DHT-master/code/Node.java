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
import java.util.concurrent.TimeUnit;
import java.lang.Thread;
public class Node {
    public static NodeServiceHandler handler;
    public static NodeService.Processor processor;

    public static void main(String [] args) {
        try {
            handler = new NodeServiceHandler();
            processor = new NodeService.Processor(handler);

            Runnable simple = new Runnable() {
                public void run() {
                    simple(processor);
                }
            };

            new Thread(simple).start();
        } catch (Exception x) {
            x.printStackTrace();
        }
    }


    public static void simple(NodeService.Processor processor) {
    	//Create client connect.
    	String snip = "localhost";
    	String snport = "9990";
    	String nip = "localhost";
    	String nport = "9998"; 
        try {
        } catch(TException e) {
        	e.printStackTrace();
        }

        try {
            //Create Thrift server socket
            TServerTransport serverTransport = new TServerSocket(parseInt(nport));
            TTransportFactory factory = new TFramedTransport.Factory();

            //Create service request handler
            NodeServiceHandler handler = new NodeServiceHandler();
            processor = new NodeService.Processor(handler);


            TTransport  transport = new TSocket(snip, snport);
            TProtocol protocol = new TBinaryProtocol(transport);
            SNodeService.Client client = new SNodeService.Client(protocol);

            //Try to connect
            transport.open();

            //Try to join DHT
            NodeInfo initInfo = client.join(nip, nport);
            //If receive NACK, wait 1s and try again
            while (initInfo.nodeIp == "0") {
            	Thread.sleep(1000);
            	initInfo = client.join(nip, nport);
            }
            //Once receive identifier and a node info from super node, start initiate its finger table
            handler.init(nip, nport, ninitInfo);

            //Set server arguments
            TServer.Args args = new TServer.Args(serverTransport);
            args.processor(processor);  //Set handler
            args.transportFactory(factory);  //Set FramedTransport (for performance)

            //Run server as a multithread server
            TServer server = new TThreadPoolServer(new TThreadPoolServer.Args(serverTransport).processor(processor));
            server.serve();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}