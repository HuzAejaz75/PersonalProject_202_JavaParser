import java.io.*;
import java.util.*;
import java.lang.*;

import japa.parser.ast.Node;
import japa.parser.ast.body.ClassOrInterfaceDeclaration;
import japa.parser.ast.body.TypeDeclaration;
import japa.parser.ast.body.ClassOrInterfaceDeclaration;
import japa.parser.JavaParser;
import japa.parser.ParseException;
import japa.parser.ast.CompilationUnit;


public class parseInspector {
	final String input_path;
    final String output_file;
    List<CompilationUnit> comp_unit_holder;
    HashMap<String, Boolean> classOrInterface_map;

    parseInspector(String input_path, String output_file) {
        this.input_path = input_path;
        this.output_file = input_path + "\\" + output_file + ".png";
    }
    public void parseComputer() throws Exception {
    	comp_unit_holder = getCuArray(input_path);
    	System.out.println("the initial code trasnforms to");
    	for(CompilationUnit comp_unit: comp_unit_holder)
    	{
    		System.out.println(comp_unit);
    	}
    }
    
    private void classOrInterface(ArrayList<CompilationUnit> comp_unit_array)
    {
    	for(CompilationUnit comp_unit : comp_unit_array)
    	{
    		   List<TypeDeclaration> typeHolder = comp_unit.getTypes();
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
                   
                  
                   // false is class,
                                                              // true is interface
               }
    	}
    }
    private List<CompilationUnit> getCuArray(String input_path)
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
	
}
