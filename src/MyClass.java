import java.io.Serializable;
import java.util.ArrayList;

public class MyClass implements Serializable {
	private ArrayList<String> myStrings;
	
	public MyClass() {
		myStrings = new ArrayList<>();
	}
	
	public MyClass(String firstStr) {
		myStrings = new ArrayList<>();
		myStrings.add(firstStr);
	}
	
	public void addStr(String newStr) {
		myStrings.add(newStr);
	}
	
	public ArrayList<String> getMyStrings() {
		return myStrings;
	}
}
