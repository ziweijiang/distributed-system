import org.apache.thrift.TException;

public class MultiplyHandler implements Multiply.Iface
{
        @Override
        public boolean ping() throws TException {
			System.out.println("I got ping()");
			return true;
		}

        @Override
        public int multiply_1(Numbers values) throws TException {
				System.out.println("I got multiply_1");
                return values.x * values.y;
        }
        
        @Override
        public int multiply_2(int x, int y) throws TException {
				System.out.printf("I got multiply_2  -> x: %d  y: %d\n", x, y);
                return x * y;
        }
}

