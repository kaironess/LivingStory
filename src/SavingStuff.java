import java.io.*;

public class SavingStuff {
	
	public static void saveObj(MyClass obj) {
		try {
			FileOutputStream fos = new FileOutputStream("save.save");
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			
			oos.writeObject(obj);
			oos.close();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static MyClass loadObj() {
		MyClass obj = null;
		
		try {
			FileInputStream fis = new FileInputStream("save.save");
			ObjectInputStream ois = new ObjectInputStream(fis);
			
			obj = (MyClass)ois.readObject();
			
			ois.close();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		return obj;
	}
	
	public static void main(String[] args) {
		
		//MyClass obj = new MyClass("Hello");
		
		//saveObj(obj);
		
		MyClass obj = loadObj();
		//obj.addStr(" World!");
		
		//saveObj(obj);
		
		System.out.print(obj.getMyStrings().get(0));
		System.out.println(obj.getMyStrings().get(1));
	}
}
