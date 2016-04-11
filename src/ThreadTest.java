import com.cycling74.max.MaxObject;

public class ThreadTest extends MaxObject {
	private double[] darray;
	public ThreadTest()
	{
	declareIO(1,1);
	darray = new double[10000000];
	}
	public void bang()
	{
	//create a new thread to fill out darray
	Thread t = new Thread(){
	public void run()
	{
	for(int i = 0; i < darray.length;i++)
	{
	darray[i] = Math.sin(System.currentTimeMillis());
	}
	outletBang(getInfoIdx()); //outlet a bang out
	//of the info outlet
	//when we are through
	//filling the array.
	}
	};
	t.start(); //start the thread executing
	}

}
