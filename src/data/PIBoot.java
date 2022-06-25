package data;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.text.BadLocationException;

import lang.core.structures.InstructionObject;
import lang.core.structures.StructureObject;

public class PIBoot {

	/*
	 * EXECUTING PUBLICIFYING THE LANGUAGE:
	 * Website with documentation on everything in the library and primitives, includes also a basic tutorial to get used to the syntax 
	 * utils and pretty much everything that you need to know to code but isn't a library or primitive.
	 * Ads saying why you should use it, and also link to youtube channel.
	 * Youtube channel with tons of prerecorded series on how to do a bunch of stuff, like server, website, game, calculator, etc. Little
	 * details like exact speed tests, and what happens when you type things might be kept to the end to be edited in.
	 * Pay ide's to include it in their ide.
	 * Donations page on website.
	 * Website has page detailing how to add it to your ide.
	 * After this java version is complete, do it in c, (the ide can be done in c# unity), then (for the vm and re) in exe or assembly or
	 * as low level as you can get. Maybe look up how other languages do it.
	 * 
	 * FOCUS:
	 * Executing
	 * 
	 * OPTIMISING:
	 * 
	 * FEATURES:
	 * allow stacks to contain other stacks (however compute can't directly hold another compute)
	 * allow to have brackets normally (without a function in front)
	 * 
	 * UTILS (future feature for when getting to compiling, mainly for functions) (its stupid doesnt work remove it)
	 * 		alternates, uses $, used mainly for when you want to optimise something but it comes at the cost of something else,
	 * by default the complier wont take something for the cost of something else, ie speed for memory, but with #speed it knows ok ill be
	 * speed and i can make it take a bit more memory, it can also be used for multiple variants. You can put it before a method if you want
	 * to make your own. The features should be identical, there should be a default aswell. Acceptable alternatives are $speed, $memory
	 * maybe some other ones in the future. For certain application the vmre might decide to use them automatically, like if its running
	 * low on spare memory it might optimise memory, or if its running low fps (detecting from the low level libraries) it might optimise
	 * speed. Example of using it specifically (otherwise it will do default, or whatever the compiler says): draw#speed()
	 * Total ones for now are: $default $speed $memory $emergencyspeed $emergencymemory $predictive $custom
	 * Default is not needed, but if you have ocd you can put it in. Speed and memory are generic improvement. Emergency speed and memory are 
	 * for improvement under stressful locations only due to the expense of the other. Predictive is when the total "badness" of the speed and 
	 * memory situation exceeds a threshold (which could be memory is fine but its really slow, or vice versa, or both are pretty bad), it 
	 * improves both by cutting out parts of the code. Ie a for loop the increments a variable each time will be reduced to variable += times 
	 * for loop runs. It might come at the cost of errors in the code which is why it only happens generally in really bad situations where 
	 * the program is about to crash. If the wanted alternate is not available it will go to default, however you can also provide backups,
	 * ie using draw#emergencyspeed#speed(); this would say use emergency speed if you don't have that use speed if you dont have that use 
	 * default. Alternates.predictive_deny=true; this will block predictive, ie if youre running a bank and you dont want any glitches to happen
	 * if the bank servers gets laggy this would be important as banks need to be fully accurate. You can also tweak thresholds, like
	 * Alternates.predictive_threshold=300; This isn't recommended if you don't know what you are doing, however if you feel its happening
	 * too easily, or its happening too late you can tweak the threshold. The render predictive will skip over textures (instead just using
	 * the first colour of the texture for the whole shape). Due to the predictive stopping a loop the behavior should be similar in other
	 * libraries even if not implemented. If predictive is the first choice (or the choice that runs), then it will employ techniques to
	 * make it faster and use less memory. Like loops especially, it can do what was mentioned above, or if it can't it will only run once.
	 * If the loop is marked essential then it wont do it. However essential is not something most people should use if they want predictive
	 * to be effective. Predictive is meant to cut back memory usage and increase speed quickly in order to get to a lower state.
	 * 		types, uses ~, works best with alternates it gives the function a type. Certain types can be given alternates. Especially at the 
	 * vmre level. For example ~render might then be called with $speed if the vmre notices the program is running slow. It is useful as it can
	 * let the vmre automatically decide to speed certain things up. The types can be whatever you want, however typical ones are used by
	 * the vmre (the rest you can use by yourself, like ~mytype$memory [well you can do all of them by yourself if you wanted to]).
	 * Total ones used in vmre now are: ~render ~internet ~database ~input ~update
	 */
	
	private static JFrame frame, extended;
	
	public static void main(String[] args) {
		
		//lang.core.general testing
		String parse = "";
		//working
//		parse = "#test comment\nString s = \"yes\";\nint i = 3;\nint j = i;\ni = 4;\n#test comment\ni += 2;";
		
		//working
//		parse = "int i = 4;\nint j =3;\nint k= 2;\nint l=1;";
		
		//working
//		parse = "String s = \"yes hello\";\nint i = 3;\n#wowcoolcomment + ; \" wowness \t\nint j = i;\ni = 4;\n\nj += i;";
		
		//working
//		parse = "bool test = true;\nbool other = false;";

		//working
//		parse = "int i = 4;\nint j = 3;\ni = j + 3;\nint k = 4 * 3 + 7 ^ 3 % 4 / 2 - 3;\nk += i * j + 2;";
		
		//working
//		parse = "int i = 4;\nprint(i);";
		
		//working
//		parse = "int x = 4;\nint y = 3;\nrect(x,y,2,3);";

		//working
//		parse = "int i = 4;\nprint(i + 2);\nrect(i + 3, i + 4, 5, 6);";

		//working
//		parse = "int i = 4;\nint j = 4 * (i + 1);";

		//working
//		parse = "int i = 4;\nint j = 4 ^ (2*(i + 1));";
		
		//working
//		parse = "int i = 4;\nint j = 4 ^ (j + 2 * (j - 2)) - (j-4*(j+4));";
		
		//working
//		parse = "int i = 4;\nint j = 3 * 4 - (j + 2 * (j - 2)) - 4 + 5 * 8 ^ (j-4*(j+4)) + 7*2;";
		
		//working
//		parse = "int i = 4;\nint j = 4 * (i + 1);\nprint(i);\nrect(i + 3, i + 4, 5, 6);\nj = i +1;\nj = root(i,3);";

		//working
//		parse = "float f = 4;\nfloat g = f*3;\nprint(g);\nf=g/3;\nprint(f);";
		
		//working
		//parse = "import lang.core.math;\nint testroot = root(64,3);\nprint(testroot);\n";
		
		//not working
//		parse = "int i = 4;\nint j = 4 * (i + 1);\nj = 2+root(i+4,3)*3;";
		
		//test
//		parse = "int i = 4;\nint j = 3*2+i;\nint blank=?;\nObject nothing=null;\nint k = multiply(j,i);\nprint(3);\nprint(k);";
		
//		ArrayList<String> tokens = lang.core.general.TokenParser.parse(parse);
//		System.out.println("tokens " + tokens);
//		System.out.println("");
//		ArrayList<StructureObject> structures = lang.core.general.StructureParser.parse(tokens);
//		for(StructureObject o : structures) System.out.println(o.type + " " + o.data);
//		System.out.println("");
//		ArrayList<InstructionObject> instructions = lang.core.general.InstructionParser.parse(structures);
//		for(InstructionObject o : instructions) System.out.println(o.type + " " + o.data);
//		System.out.println("");
//
//		InstructionObject[] instructionsA = lang.core.general.InstructionParser.fullparse(parse);
//		for(InstructionObject o : instructionsA) System.out.println(o.type + " " + o.data);
		
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				try {
					Dimension size = Toolkit.getDefaultToolkit().getScreenSize();
					frame = new MainFrame("Pixel IDE");
					frame.setSize((int)(size.getWidth()*3/4),(int)(size.getHeight()*3/4));
					frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
					frame.setVisible(true);
				} catch (BadLocationException e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	public static void relaunch() {
		try {
			if(extended != null) extended.dispose();
			if(frame != null) frame.dispose();
			Dimension size = Toolkit.getDefaultToolkit().getScreenSize();
			frame = new MainFrame("Pixel IDE");
			frame.setSize((int)(size.getWidth()*3/4),(int)(size.getHeight()*3/4));
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame.setVisible(true);		
		} catch (BadLocationException e) {
			e.printStackTrace();
		}		
	}
	
	public static void launch(String extended_) {
		try {
			if(frame != null) frame.dispose();
			if(extended != null) extended.dispose();
			Dimension size = Toolkit.getDefaultToolkit().getScreenSize();
			frame = new MainFrame("Pixel IDE",extended_);
			frame.setSize((int)(size.getWidth()*3/4),(int)(size.getHeight()*3/4));
			extended.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			extended.setVisible(true);		
		} catch (BadLocationException e) {
			e.printStackTrace();
		}		
	}
}
