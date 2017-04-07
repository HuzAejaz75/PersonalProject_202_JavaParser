import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.TypeDeclaration;

/*import japa.parser.ast.Node;
import japa.parser.ast.body.ClassOrInterfaceDeclaration;
import japa.parser.ast.body.TypeDeclaration;

import japa.parser.JavaParser;
import japa.parser.ParseException;
import japa.parser.ast.CompilationUnit;

import japa.parser.JavaParser;*/

//import com.github.javaparser.JavaParser;

//import japa.parser.ast.CompilationUnit;

//import com.github.javaparser.ast.CompilationUnit;

public class parseInspector {
	String input_path;
	String output_path;
	//List<CompilationUnit> comp_unit_holder;
	 ArrayList<CompilationUnit> comp_unit_holder;
	  HashMap<String, Boolean> classOrInterface_map;
	public parseInspector(String input_path, String output_file)
	{
		this.input_path = input_path;
		this.output_path = input_path + "\\" + output_file + ".png";
		
	}
	
	public void parser_agent() throws Exception
	{
		comp_unit_holder = returned_compiler(input_path);
	//	for(CompilationUnit comp_unit: comp_unit_holder)
    	//{
    	//	System.out.println(comp_unit);
    		//intermediate_code_generator(comp_unit);
    	//}
		//classOrInterface(comp_unit_holder);
		
	}
	
	
	//String UML_code_holder = null;
	
	
	
private void PopulateMap(ArrayList<CompilationUnit> comp_parse_array)
    {
    	
        for (CompilationUnit comp_unit : comp_parse_array) {
            List<TypeDeclaration> TypeHolder = comp_unit.getTypes();
            for (Node n : TypeHolder) {
                ClassOrInterfaceDeclaration Type_Unit = (ClassOrInterfaceDeclaration) n;
                if(Type_Unit.isInterface())
                {
                	  classOrInterface_map.put(Type_Unit.getName(), true); 
                }
                else
                {
                	classOrInterface_map.put(Type_Unit.getName(), false); 
                }
              
                                      
            }
        }
    }
	private void parsing(CompilationUnit comp_parsed_unit) {
    	String ClassValue;
    	List<TypeDeclaration> class_types =  comp_parsed_unit.getTypes();
    	Node parse_node = class_types.get(0);
    	String class_details;
    	 
        
    	 ClassOrInterfaceDeclaration Class_Or_Interface = null;
    			 Class_Or_Interface = (ClassOrInterfaceDeclaration) parse_node;
    	 
         if (!(Class_Or_Interface.isInterface())) {
        	 class_details = "[";
         }
         else
         {
        	 class_details = "[<<interface>>;";
         }
         
         class_details += Class_Or_Interface.getName();
         
         for(BodyDeclaration body_dec : ((TypeDeclaration)parse_node).getMembers())
         {
        	 if(body_dec instanceof ConstructorDeclaration)
        	 {
        		class_details = constructor_parsing(body_dec, class_details);// this method parses constructors
        	 }
        	 else
        		 if(body_dec instanceof FieldDeclaration)
        		 {
        			 field_parsing(body_dec,class_details); // this method parses fields
        		 }
        		 
         else
        	 if(body_dec instanceof MethodDeclaration)
        	 {
        		 method_parsing(body_dec,class_details);//this method parses methods
        	 }
    }
	
	
	private ArrayList<CompilationUnit> returned_compiler(String input_path)
            throws Exception {
        File files = new File(input_path);
        ArrayList<CompilationUnit> comp_unit_collection = new ArrayList<CompilationUnit>();
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
	
	
    }
	/*private void classOrInterface(List<CompilationUnit> comp_unit_array)
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
    }*/
	


	
