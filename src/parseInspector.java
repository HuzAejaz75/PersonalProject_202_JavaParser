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
	
	
	
private void TypeHolder(ArrayList<CompilationUnit> cuHolder) {
        for (CompilationUnit compUnit : cuHolder) {
            List<TypeDeclaration> typeHold = compUnit.getTypes();
            for (Node node : typeHold) {
                ClassOrInterfaceDeclaration elem_type = (ClassOrInterfaceDeclaration) node;
                if(!(elem_type.isInterface()))//if it is not an interface
                {
                	typeMap.put(elem_type.getName(),false); // false if it is a class
                }
                else//if it is an interface
                {
                	typeMap.put(elem_type.getName(), true);//true if it is an interface
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
	
private String parseEngine(CompilationUnit compUnit) {
	 String className = "";
	 List<TypeDeclaration> typeList= compUnit.getTypes();
         Node node = typeList.get(0); 
	 ClassOrInterfaceDeclaration typeDetect = (ClassOrInterfaceDeclaration) node;
        if (!(typeDetect.isInterface()))
	{
        	className = "[";
        } else 
	{
            className = "[" + "<<interface>>;";
        }
}
	private ArrayList<CompilationUnit> getCuHolder(String inputPath)
            throws Exception {
        File file = new File(inputPath);
        ArrayList<CompilationUnit> cuHolder = new ArrayList<CompilationUnit>();
        //ArrayList<CompilationUnit> cuHold = new ArrayList<CompilationUnit>();
        File[] fileHolder = file.listFiles();
        for (final File fileUnit : fileHolder) {
            
            if(fileUnit.isFile())
            {
            	String fileName = fileUnit.getName();
            	if(fileName.endsWith(".java"))
            	{
            		CompilationUnit compUnit = null;
            		 FileInputStream iStream = new FileInputStream(fileUnit);
                      
                      try {
                    	  compUnit = setCuHolder(iStream,compUnit);
                    	  cuHolder.add(compUnit);
                      } finally {
                          iStream.close();
                      }
            		
            	}
            }
        }
        return cuHolder;
    }
    private CompilationUnit setCuHolder(FileInputStream fileStream, CompilationUnit c) throws ParseException, IOException
    {
    		c = JavaParser.parse(fileStream);
        	 return c;
    }
		
public String method_parsing(BodyDeclaration body_dec, String class_details)
    {
    	String method_parse = "";
    	MethodDeclaration m_dec = (MethodDeclaration)body_dec;
    	String method_name = m_dec.getName();
    	System.out.println(constructor_name);
    	if(method_name.startsWith("set") || method_name.startsWith("get"))
    	{
    		String methodName = m_dec.getName();
    	}
    	else
    	{
    		method_parse += "+ " + m_dec.getName() + "(";
    		for(Object method_child : m_dec.getChildrenNodes())
    		{
    			if(method_child instanceof Parameter)
    			{
    				Parameter param_method = (Parameter) method_child;
    				String param_classname = param_method.getType().toString();
    				String param_name = param_classname.getChildrenNodes.get(0).toString();
    				System.out.println("the method name :"+ param_name);
    			}
    			else
    			{
    				
    			}
    		}
    	}
    }
	public String constructor_parsing(BodyDeclaration body_dec,String class_details,ClassOrInterfaceDeclaration elem_type)
    {
    	String method_parse = "";
    	ConstructorDeclaration c_dec = (ConstructorDeclaration)body_dec;
    	String constructor_name = c_dec.getName();
    	System.out.println(constructor_name);
    	if(constructor_name.startsWith("public"))
    	{
    		if(elem_type.isInterface())
    		{
    			method_parse+="+"+c_dec.getName();
    			method_parse+="(";
    			for(Object child_nodes : c_dec.getChildrenNodes())
    			{
    				if(child_nodes instanceof Parameter)
    				{
    					Parameter param = (Parameter) child_nodes;
    					String param_type = param.getType().toString();
    					System.out.println(param_type);
    				}
    			}
    		}
    	}
    }
    			
    public String fieldParser(BodyDeclaration bDec, boolean nextElem)
    {
       String fieldsOutput = "";
       FieldDeclaration fDec = ((FieldDeclaration)bDec);
       String scopeVal = bDec.toStringWithoutComments().substring(0, bDec.toStringWithoutComments().indexOf(" "));
       String fScope =  aToSymScope(scopeVal);
       //String fScope = null;
       
       String fClass = changeBrackets(fDec.getType().toString());//bracketTransform
       String fName = fDec.getChildrenNodes().get(1).toString();
      // fScope = scopeDetect(scopeVal)
       if(fScope == "+" || fScope =="-")
       {
           fieldsOutput += fScope +" "+fName+" : "+ fClass;
       }
        if(nextElem)
            fieldsOutput +="; ";
       return fieldsOutput;

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
	


	
