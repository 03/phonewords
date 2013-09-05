import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.wind.phonewords.PhonewordFinder;
import com.wind.phonewords.WordReader;


public class MainEntry {

	public static void main(String[] args) {

		WordReader dictionary = new WordReader("/dict.txt");
		PhonewordFinder finder = new PhonewordFinder(dictionary);
		// List<String> words = finder.num2words("2255.63");
		// finder.num2words("2255.63");
		
		List<String> optsList = new ArrayList<String>();
		List<String> phoneNumberList = new ArrayList<String>();
		
		// Check how many arguments were passed in
	    if(args.length == 0)
	    {
	        System.out.println("Phoneword usage: ");
	        System.out.println("java -cp phonewords-0.1.jar MainEntry [-d dictionaryFile] [-f phonelistFile] [phonenum1] [phonenum2] ...");
	        System.out.println("---------------------------------------------------------------------------------------------------------");
	        System.out.println("Example 1: java -cp phonewords-0.1.jar MainEntry 2255.63 356937");
	        System.out.println("Example 2: java -cp phonewords-0.1.jar MainEntry -d \"C:/test/dict4test-3words.txt\" 2255.63 356937");
	        System.out.println("Example 3: java -cp phonewords-0.1.jar MainEntry -f \"C:/test/phonelist.txt\"");
	        
	        System.exit(0);
	    }
		
		for (int i=0; i < args.length; i++) {
	        switch (args[i].charAt(0)) {
	        case '-':
	            if (args[i].length() != 2)
	                throw new IllegalArgumentException("Not a valid option: "+args[i]);
	            
	            if (args.length-1 == i)
                    throw new IllegalArgumentException("Expected arg after: "+args[i]);
	            
				char opt = args[i].charAt(1);
				if ('d' == opt) {
					// dictionary
					System.out.println("dictionary -> " + args[i + 1]);
					dictionary.loadDict(args[i + 1]);

				} else if ('f' == opt) {
					// phone list
					System.out.println("phone list-> " + args[i + 1]);
					
					String line;
					try {
						@SuppressWarnings("resource")
						BufferedReader br = new BufferedReader(new FileReader(args[i + 1]));
						
						while ((line = br.readLine()) != null) {
							finder.num2words(line);
						}
						// System.out.println("Dictionary ["+ fileName + "] loaded successfully!");

					} catch (IOException e) {
						System.out.println("Phone List loading failed! " + e.getMessage());
						e.printStackTrace();
					}

				} else {
					throw new IllegalArgumentException("Unsupport option: " + args[i]);
				}
				
	            // -opt
                optsList.add(args[i]);
                i++;
	            break;
	            
	        default:
	        	phoneNumberList.add(args[i]);
	            break;
	        }
	    }
		
		// convert number list
		for(int i=0; i<phoneNumberList.size(); i++) {
			
			finder.num2words(phoneNumberList.get(i));
		}
		
		// System.out.println("optsList-> "+optsList);
	}

}
