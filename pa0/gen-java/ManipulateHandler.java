import org.apache.thrift.TException;
import java.util.*;

public class ManipulateHandler implements Manipulate.Iface
{
	public Map<String, String> map = new HashMap<>();
	@Override
        public boolean ping() throws TException {
			System.out.println("I got ping()");
			return true;
		}

        @Override
        public String get(String k) throws TException {
				System.out.println("I got get()");
                return map.get(k);
        }
        
        @Override
        public boolean put(Pair p) throws TException {
				System.out.printf("I got put");
				map.put(p.key, p.value);
                return true;
        }
}