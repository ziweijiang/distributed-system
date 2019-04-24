import org.apache.thrift.TException;
import org.apache.thrift.transport.TTransportFactory;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TFramedTransport;
import org.apache.thrift.transport.TSSLTransportFactory.TSSLTransportParameters;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import java.util.ArrayList;

public class NodeServiceHandler implements NodeService.Iface {
	// NodeSet
    int ftSize = 5;
    private ArrayList<NodeSet> ftNode = new ArrayList<>();
    private ArrayList<Integer> ftStart = new ArrayList<>();
    private NodeInfo pred;
    private NodeInfo succ;
    private NodeInfo curr;
    private NodeSet curSet;
    public void init(String nip, String nport, NodeInfo initInfo) {
    	//Set current node info
		curr.nodeId = initInfo.nodeKey;
		curr.nodeIp = nip;
		curr.nodePort = nport;
		//Calculate the start of finger table
		for (int i = 1; i <= ftSize; i ++) {
    			ftStart.add((parseInt(curr.nodeId) + Math.pow(2, i - 1) mod Math.pow(2, ftSize)));
    		}
    	//If there is no node in DHT
    	if (initInfo.nodeIp == "") {
    		//Set predecessor and successor to itself
			pred.nodeId = initInfo.nodeKey;
			pred.nodeIp = nip;
			pred.nodePort = nport;
			succ.nodeId = initInfo.nodeKey;
			succ.nodeIp = nip;
			succ.nodePort = nport;
			curSet.pred = pred;
			curSet.curr = curr;
			curSet.succ = succ;
			//Set the node of finger table
    		for (int i = 1; i <= ftSize; i ++) {
    			ftNode.add(curSet);
    		}
    	} else {
    		initFT(initInfo.nodeIp, initInfo.nodePort);
    		updateOthers();
    	}

    }
    private void initFT(String nid, String nport) {
    	TTransport  transport = new TSocket(nid, parseInt(nport));
        TProtocol protocol = new TBinaryProtocol(transport);
        AssignService.Client client = new AssignService.Client(protocol);
        NodeSet mySucc = client.findSucc(curr.nodeId);
		transport.open();
        pred = mySucc.pred;
        succ = mySucc.curr;
		curSet.pred = pred;
		curSet.curr = curr;
		curSet.succ = succ;
		client.setPred(pred);
		ftNode.add(mySucc);
		for (int i = 1; i <= ftSize; i ++) {
			if (ftStart[i] > parseInt(curr.nodeId) && ftStart[i] < parseInt(ftNode[i - 1].curr.nodeId)) {
				ftNode[i] = ftNode[i - 1];
			} else {
				NodeSet tmp = client.findSucc(ftStart[i]);
				ftNode[i] = tmp;
			}
		}
    }
    private void updateOthers() {
    	for (int i = 1; i <= ftSize; i ++) {
    		NodeSet p = findPred(parseInt(curr.nodeId - Math.pow(2, i - 1)));
	    	TTransport  transport = new TSocket(p.curr.nodeId, parseInt(p.curr.nodePort));
	        TProtocol protocol = new TBinaryProtocol(transport);
	        AssignService.Client client = new AssignService.Client(protocol);
			transport.open();
	        client.updateDHT(curSet, i);
    	}
    }
    private NodeSet findSucc(String nid) {
    	NodeSet tmp = findPred(id);
    	String tmpSuccIp = tmp.succ.nodeIp;
		String tmpSuccPort = tmp.succ.nodePort;
    	TTransport  transport = new TSocket(tmpSuccIp, parseInt(tmpSuccPort));
        TProtocol protocol = new TBinaryProtocol(transport);
        AssignService.Client client = new AssignService.Client(protocol);
		transport.open();
		tmp = client.getNodeSet;
    }
    private NodeSet findPred(String nid) {
    	NodeSet tmp = closest_preceding_finger(id);
    	while (!(id > parseInt(curSet.curr.nodeId) && id < parseInt(curSet.succ.nodeId))) {
    		String tmpIp = tmp.curr.nodeIp;
    		String tmpPort = tmp.curr.nodePort;
	    	TTransport  transport = new TSocket(tmpIp, parseInt(tmpPort));
	        TProtocol protocol = new TBinaryProtocol(transport);
	        AssignService.Client client = new AssignService.Client(protocol);
			transport.open();
			tmp = client.closest_preceding_finger(id);
    	}
    	return tmp;
    }
    private NodeSet closest_preceding_finger(String nid) {
    	for (int i = ftSize; i >= 0; i --) {
    		if (parseInt(ftNode[i].curr.nodeId) < parseInt(nid) && parseInt(ftNode[i].curr.nodeId) > parseInt(curr.nodeId)) {
    			return ftNode[i];
    		}
    	}
    	return curSet;
    }

    @Override
    public getNodeSet () throws TException { //add this to interface
    	return curSet;
    }

    @Override
    public setPred (NodeInfo n) throws TException { //add this to interface
    	pred = n;
    	curSet.pred = pred;
    }

	@Override
	public void setItem(String bookTitle, String genre) throws TException {

	}
	@Override
	public void getItem(String bookTitle) throws TException {
		
	}
	@Override
	public void updateDHT(NodeSet n, int i) throws TException { // change params
		if (parseInt(n.curr.nodeId) > parseInt(curr.nodeId) && parseInt(n.curr.nodeId) < parseInt(ftNode[i - 1].curr.nodeId)) {
			ftNode[i - 1] = n;
	    	TTransport  transport = new TSocket(pred.nodeIp, parseInt(pred.nodePort));
	        TProtocol protocol = new TBinaryProtocol(transport);
	        AssignService.Client client = new AssignService.Client(protocol);
			transport.open();
	        client.updateDHT(n, i);
		}
	}
}