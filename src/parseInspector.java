import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import japa.parser.ast.Node;
import japa.parser.ast.body.ClassOrInterfaceDeclaration;
import japa.parser.ast.body.TypeDeclaration;
import japa.parser.ast.body.ClassOrInterfaceDeclaration;
import japa.parser.JavaParser;
import japa.parser.ParseException;
import japa.parser.ast.CompilationUnit;

import japa.parser.JavaParser;

//import com.github.javaparser.JavaParser;

import japa.parser.ast.CompilationUnit;

//import com.github.javaparser.ast.CompilationUnit;

public class parseInspector {
	String input_path;
	String output_path;
	List<CompilationUnit> comp_unit_holder;
	  HashMap<String, Boolean> classOrInterface_map;
	public parseInspector(String input_path, String output_file)
	{
		this.input_path = input_path;
		this.output_path = input_path + "\\" + output_file + ".png";
		
	}
	
	public void parser_agent() throws Exception
	{
		comp_unit_holder = returned_compiler(input_path);
		for(CompilationUnit comp_unit: comp_unit_holder)
    	{
    		System.out.println(comp_unit);
    		intermediate_code_generator(comp_unit);
    	}
		classOrInterface(comp_unit_holder);
		
	}
	
	
	//String UML_code_holder = null;
	
	private void intermediate_code_generator(CompilationUnit comp_parse_unit)
	{
		List<TypeDeclaration> TypeHolder = comp_parse_unit.getTypes();
		
		for(TypeDeclaration T : TypeHolder)
		{
			System.out.print(T.toString());
		}
	}
	
	private List<CompilationUnit> returned_compiler(String input_path)
            throws Exception {
        File files = new File(input_path);
        List<CompilationUnit> comp_unit_collection = new ArrayList<CompilationUnit>();
        for ( File file_unit : files.listFiles()) {
            if (!file_unit.isFile()) {
            	System.out.println("File is missing");
            	     
            }
            else
            {
            	if(file_unit.getName().endsWith(".java"))
            	{
            		 FileInputStream in = new FileInputStream(file_unit);
                     CompilationUnit comp_unit;
                     try {
                         comp_unit = JavaParser.parse(in);
                         comp_unit_collection.add(comp_unit);
                     } finally {
                         in.close();
                     }
            	}
            }
        }
        return comp_unit_collection;
    }
	
	
	private void classOrInterface(List<CompilationUnit> comp_unit_array)
    {
    	for(CompilationUnit comp_unit : comp_unit_array)
    	{
    		   List<japa.parser.ast.body.TypeDeclaration> typeHolder = comp_unit.getTypes();
               for (Node n : typeHolder) {
                   ClassOrInterfaceDeclaration coi_holder = (ClassOrInterfaceDeclaration) n;
                   
                   if(coi_holder.isInterface())
                   {
                	   classOrInterface_map.put(coi_holder.getName(), true);
                   }
                   else
                   {
                	   classOrInterface_map.put(coi_holder.getName(), false);
                   }
                   
                 
               }
    	}
    }
	
	
	
	
}

	
