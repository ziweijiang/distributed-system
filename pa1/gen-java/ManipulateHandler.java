import org.apache.thrift.TException;
import java.util.*;

public class MapReduce implements MapReduce.Iface
{
        @Override
        public Result cal(String address) throws TException {
		System.out.println("I got cal()");
                Result res = new Result(new List<string>, 0);
                return res;
        }
        
        @Override
        public boolean compute(String res) throws TException {
		System.out.printf("I got compute");
                return true;
        }
}