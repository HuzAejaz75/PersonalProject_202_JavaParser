import java.io.*;
import java.util.*;
public class parseInspector {

	String input_path;
	String output_path;
	List<CompilationUnit> comp_unit_holder;
	public parseInspector()
	{
		this.input_path = input_path;
		this.output_path = output_path;
		
	}
	
	
	public List<CompilationUnit> returned_compiler(String input_path)
	{
		File file = new File(input_path);
		List<CompilationUnit> Compiled_unit = new List<CompilationUnit>();
		List<File> file_holder = Arrays.asList(file.listFiles());
		for(File file_unit : file_holder)
		{
			if(!file_unit.isFile())
			{
				System.out.println("The file doesn't exist");
			}
			
			else
			{
				if(file.getName().endsWith(".java"));
				{
					FileInputStream input_stream = new FileInputStream(file_unit);
					CompilationUnit Compiled_units;
					Compiled_units = JavaParser.parse(input_stream);
					Compiled_unit.add(Compiled_units);
				}
			}
		}
		
	}
}
