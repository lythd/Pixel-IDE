package lang.core.executor;

import java.io.File;

import lang.core.structures.InstructionObject;

public interface Executor {
	
		/*	OVERVIEW:
		 * 	-This is an interface that should be followed by any executor. This class does not have to be physically present, but should be followed. Even in
		 *   other languages.
		 *  -All executors should never crash, they should use abundant "try catches" or equivalent in order to prevent any crash on the executor. Any crash should
		 *   be reported to the maker of the executor. The executor should catch any errors, exit the program, and output to a console window after with an
		 *   error message, and traceback if available.
		 *  -This is the interface as of Pixel vINDEV. The version of this interface should be used as the version for any modded pixel interface based on this
		 *   version. The version will either follow a stage, INDEV, INFDEV, ALPHA, BETA, or be a number. If its a number it will always be x.0 for the interface,
		 *   since the second number refers to IDE and Executor changes. So on your own modded version, you can change the second number on your own, and add a
		 *   third if you really want. For stages, you can just add a number after.
		 *  -The IDE will need to know what executor is being used, this can be set in a link to the folder containing the executor, and all of its method files.
		 *  -The executor should be contained in a folder, that will contain directly the executor, (this interface, though not needed), and its method files. No
		 *   folders should be contained in the executor folder.
		 *  -This interface contains everything required to be compatible with a Pixel compiler. Additional components, executor-side methods, load functions,
		 *   or instructions can be added provided it follows as stated below.
		 *  -Additional components must not take any features from existing components. Additional components should be there for additional features, not for
		 *   feature management.
		 *  -Additional executor-side methods can only be added for specific IDEs that account for them, this should be avoided as it breaks Pixel's natural
		 *   compatibility. Any additional executor-side methods would be seen as modding the Pixel version, additional methods should be added via a library
		 *   in the IDE to keep compatibility.
		 *  -Additional load functions can be added provided they follow {deexecute} which is the load function procedure.
		 *  -Additional instructions can be added provided that they are only used in executor-side methods.
		 */
	
	/*	Executor-Side Methods:
	 * 	(file [arguments] [output] - intended purpose)
	 * 	exit.pxl [1] [null] - method that will exit with a message that should be displayed in a console, follows {exit}
	 * 	print.pxl [1] [null] - method that will print a message onto the main screen view of the user
	 */
	
	/*	Components:
	 * 	-internal main memory to store values [other 4 components can be stored in here, or can be seperate, up to executor]
	 * 	-internal memory to store all the instructions [in an interpreter is normally not stored in the main memory as ]
	 * 	-internal memory to store accompanying values to main memory
	 * 	-references for variables to the internal memory
	 * 	-references for tmp variables to the internal memory
	 */
	
	/* 	{deexecute}
	 * 	-Special procedure that outlines the load functions, (the load functions need to follow this). It handles updates to the executor, and also 
	 * 		1) Detect if any execute instructions are present.
	 * 			1.1) If not then can set the internal memory of instructions based on the given data, and skip the rest of this procedure.
	 * 		2) Initialise a new list of instructions.
	 * 		3) Loop through the given instructions:
	 * 			3.1) If it is a execute instruction:
	 * 				3.1.1) Then expand it based upon the executor-side method files and add each new instruction to the new list.
	 * 				3.1.2) Otherwise, just add the instruction to the new list.
	 * 		4) Turn the new list into an array.
	 * 		5) Use this array in internal memory instead of the other one.
	 * 		6) (loadFromFile only) rewrite the file to contain these new instructions.
	 */
	
	/* {set}
	 * -Special procedure that should be used anytime something is being set, (example instructions: set, multiply, etc)
	 * 		1) Check if null, any number value, or any string value is being set.
	 * 			1.1) Then, simply skip the procedure. Recommended to, but not required to, exit the program with message "An attempt to set an internal value of [whatever contained in the string]."
	 * 		2) Check if the two are the same type.
	 * 			2.1) Otherwise, exit the program with message "Variable being set to a value of a different type."
	 * 		3) Set the variable to the value.
	 */
	
	/* {exit}
	 * -Outline of the implementation of the exit method.
	 * 		1) Output the given message into a console.
	 * 		2) Output a traceback into the console. This should include instruction number, and (for interpreters) line number.
	 * 			[the actual traceback through the code blocks will be here when they are actually functional]
	 * 		3) Exit the run function.
	 */
	
	public void loadFromInstructions(InstructionObject[] instructions);//set internal memory of instructions based on an instruction array that follows {deexecute}
	public void loadFromFile(File file);//set internal memory of instructions based on a file, also rewrites the file following {deexecute}
	public void run();//executes instructions
	/* Instruction Types:
	 * [tmp] variable means it could be tmp or not, whereas tmp variable would be it has to be tmp, and variable would be a non-tmp variable
	 * set [2] - sets a [tmp] variable to a value, the value can be from a [tmp] variable or given direct, follows {set}
	 * iint [1] - initialises an integer variable with that name
	 * multiply [3] - takes in two number values, which can each be [tmp] variables or given direct, multiplies them and stores into a [tmp] variable following {set}
	 */
	
}