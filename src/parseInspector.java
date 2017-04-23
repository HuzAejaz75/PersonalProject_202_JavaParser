/**
 * Created by huzaifa.aejaz on 4/16/17.
 */
import java.io.*;
import java.lang.reflect.Array;
import java.util.*;
import java.lang.*;

import com.github.javaparser.*;
import com.github.javaparser.ast.*;
import com.github.javaparser.ast.body.*;
import com.github.javaparser.ast.type.ClassOrInterfaceType;

public class ParseHub {
    final String inputPath;
    final String outputPath;
    HashMap<String, String> classConnections;
    String readCode;
    ArrayList<CompilationUnit> cuHolder;
    HashMap<String, Boolean> typeMap;


    ParseHub(String inputPath, String outputPath) {
        this.inputPath = inputPath;
        this.outputPath = inputPath + "\\" + outputPath + ".png";
        typeMap = new HashMap<String, Boolean>();
        classConnections = new HashMap<String, String>();
        readCode = "";
    }
	
    private ArrayList<CompilationUnit> getCuHolder(String inputPath)
            throws Exception {
        File file = new File(inputPath);
        ArrayList<CompilationUnit> cuHolder = new ArrayList<CompilationUnit>();
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
	

    public void controller() throws Exception {
        cuHolder = getCuHolder(inputPath);
        TypeHolder(cuHolder);
        for (CompilationUnit cu : cuHolder)
            readCode += parseEngine(cu);
        splitController(readCode,outputPath);

    }
    public void splitController(String rCode,String oPath)
    {
        rCode += addConnections();
        rCode = codeUnique(rCode);
        System.out.println("Unique Code: " + rCode);
        DiagramGenerator.imageGenerator( oPath,rCode);

    }



		
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
	
	 private String codeUnique(String UMLcode) {
        String[] umlText = UMLcode.split(",");
        LinkedHashSet<String> LH = new LinkedHashSet<String>(Arrays.asList(umlText));
        String[] umlUnique = LH.toArray(new String[0]);
        String output = String.join(",", umlUnique);
        return output;
    }

	private String addConnections() {
        String output = "";
        Set<String> keyHolder= classConnections.keySet(); // get all keys
        for (String key : keyHolder)
        {
            String[] classHolder = key.split("-");
           output += arrayConnection(classHolder,output, key);
        }
        return output;
    }
	
	private String arrayConnection(String[] classes, String output, String key)
    {
        if (typeMap.get(classes[0])) {
            output += "[<<interface>>;" + classes[0];
            output += "]";
        }
        else
            output += "[" + classes[0] + "]";
        output += classConnections.get(key); // Add connection
        if (typeMap.get(classes[1]))
            output += "[<<interface>>;" + classes[1] + "]";
        else
            output += "[" + classes[1] + "]";
        output += ",";

        return output;

    }

	private String bracketsTransform(String foo) {
        foo = foo.replace("[", "(");
        foo = foo.replace("]", ")");
        foo = foo.replace("<", "(");
        foo = foo.replace(">", ")");
        return foo;
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
		

		
private void dependancyDetector(String className)//we need class original name
    {
    	boolean hasParam = false;
    	//boolean hasDepen = false;
    	String referenceClass = "";
    	
    	if(className.contains("("))
    	{
    		hasParam = true;
    	}
    	
    	if(hasParam)
    	{
    		referenceClass = getParam(className);
    		//hasDepen = true;
    	}
	else
    		if(typeMap.containsKey(className))
    		{
    			referenceClass = className;
    		}
    	if(!(referenceClass.equals("")))
    	{
    		if(typeMap.containsKey(referenceClass))
    		{
    			String buildDependancy = "-";
    			if(classConnections.containsKey(referenceClass+"-"+className))
    			{
    				buildDependancy = classConnections.get(referenceClass+"-"+className);
    				if(hasParam)
    					buildDependancy = "*" +buildDependancy;
    				classConnections.put(referenceClass+"-"+className, buildDependancy);
    					
    			}
    			else
    			{
    				if(hasParam)
    					buildDependancy += "*";
    				classConnections.put(referenceClass+"-"+className, buildDependancy);
    			}
    		}
    	}
    
}

 public String getParam(String classVal)
 
 {
    String parameter = "";
    int startIndex = classVal.indexOf("(");
    int endIndex = classVal.indexOf(")");
    parameter = classVal.substring(startIndex,endIndex);
    return parameter;

 }
/************************************ End *************************************/	
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
private void printMaps() {
        System.out.println("Map:");
        Set<String> keys = classConnections.keySet(); // get all keys
        for (String i : keys) {
            System.out.println(i + "->" + classConnections.get(i));
        }
        System.out.println("---");
    }
		
private ArrayList<CompilationUnit> getCuHolder(String inputPath)
            throws Exception {
        File file = new File(inputPath);
        ArrayList<CompilationUnit> cuHolder = new ArrayList<CompilationUnit>();
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
  

	


	
