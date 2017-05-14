/**
 * Created by huzaifa.aejaz on 2/18/17.
 */
import java.io.*;

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


    ParseHub(String inputPath) {
        this.inputPath = inputPath;
        this.outputPath = inputPath + "\\Classdiagram" +  ".jpeg";
        typeMap = new HashMap<String, Boolean>();
        classConnections = new HashMap<String, String>();
        readCode = "";
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
        rCode = removeDuplicates(rCode);

        DiagramGenerator.imageGenerator( oPath,rCode);

    }

    private String removeDuplicates(String UMLcode) {
        String[] umlText = UMLcode.split(",");
        LinkedHashSet<String> LH = new LinkedHashSet<String>(Arrays.asList(umlText));
        String[] umlUnique = LH.toArray(new String[0]);
        String output = String.join(",", umlUnique);
        return output;
    }

    private ArrayList<CompilationUnit> getCuHolder(String inputPath)
            throws Exception {
        File file = new File(inputPath);
        ArrayList<CompilationUnit> cuHolder = new ArrayList<CompilationUnit>();
        File[] fileHolder = file.listFiles();
        for (final File fileUnit : fileHolder)
        {

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
                    } finally
                    {
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

    private String addConnections()
    {
        String output = "";
        Set<String> keyHolder= classConnections.keySet();
        for (String key : keyHolder)
        {
            String[] classHolder = key.split("-");
           output += arrayConnection(classHolder,output, key);
        }
        return output;
    }

    private String arrayConnection(String[] classes, String output, String key)
    {
        ArrayList<String> arrayConnectionList = new ArrayList<String>();
        if (typeMap.get(classes[0]))
        {

            arrayConnectionList.add("[<<interface>>;");
            arrayConnectionList.add(classes[0]);
            arrayConnectionList.add("]");

        }
       else
        {

            arrayConnectionList.add("[");
            arrayConnectionList.add(classes[0]);
            arrayConnectionList.add("]");

        }

        arrayConnectionList.add(classConnections.get(key));

        if (typeMap.get(classes[1]))
        {

            arrayConnectionList.add("[<<interface>>;");
            arrayConnectionList.add(classes[1]);
            arrayConnectionList.add("]");

        }
        else
        {

            arrayConnectionList.add("[");
            arrayConnectionList.add(classes[1]);
            arrayConnectionList.add("]");

        }
         for(String s : arrayConnectionList)
         {
             output += s;
         }

        output += ",";

        return output;

    }

    private String parseEngine(CompilationUnit compUnit)
    {
        String dependencyText = ",";
        String methodString = "";
        String parseOutput = "";
        String cName = "";
        String cleanName = "";
        String fieldString = "";



        ArrayList<String> setterAndGetterHolder = new ArrayList<String>();
        List<TypeDeclaration> typeList= compUnit.getTypes();
        Node typeNode = typeList.get(0);


        ClassOrInterfaceDeclaration typeDetect = (ClassOrInterfaceDeclaration) typeNode;
        if (!(typeDetect.isInterface()))
        {
            cName = "[";
        }
        else
        {
            cName = "[" + "<<interface>>;";
        }
        cName += typeDetect.getName();
        cleanName = typeDetect.getName();


        boolean nextElems = false;
        TypeDeclaration tdCollection = (TypeDeclaration) typeNode;
        for (BodyDeclaration codeBlock : tdCollection.getMembers())
        {

            if (codeBlock instanceof ConstructorDeclaration)
            {
                ConstructorDeclaration constrDec = ((ConstructorDeclaration) codeBlock);
                if (constrDec.getDeclarationAsString().startsWith("public") && !typeDetect.isInterface())
                {
                    if (nextElems)
                        methodString += ";";
                    methodString += "+ " + constrDec.getName() + "(";
                    List<Node> constrNodes = constrDec.getChildrenNodes();
                    for (Object childNode : constrNodes)
                    {
                        if (childNode instanceof Parameter)
                        {
                            Parameter paramNode = (Parameter) childNode;
                            String pClassNode = paramNode.getType().toString();
                            List<Node> PNodeHolder = paramNode.getChildrenNodes();
                            String pClassName = PNodeHolder.get(0).toString();
                            methodString += pClassName ;
                            methodString += " : ";
                            methodString += pClassNode;
                            if (typeMap.containsKey(pClassNode) && !typeMap.get(cleanName))
                            {
                                dependencyText += dependencyParser(cleanName,pClassNode);
                            }
                            dependencyText += ",";
                        }
                    }
                    methodString += ")";
                    nextElems = true;
                }
            }
        }
        TypeDeclaration MethodNodes = (TypeDeclaration) typeNode;
        for (BodyDeclaration  codeBlock : MethodNodes.getMembers())
        {
            if (codeBlock instanceof MethodDeclaration)
            {
                MethodDeclaration methDec = ((MethodDeclaration)  codeBlock);

                if (methDec.getDeclarationAsString().startsWith("public") && !typeDetect.isInterface())
                {

                    if (methDec.getName().startsWith("set") || methDec.getName().startsWith("get"))
                    {
                        String setNGet = methDec.getName().substring(3);
                        setterAndGetterHolder.add(setNGet.toLowerCase());
                    }
                    else
                        {

                        if (nextElems)
                            methodString += ";";
                            methodString += "+ ";
                            methodString += methDec.getName() + "(";
                            List<Node> methodChildrenList = methDec.getChildrenNodes();
                        for (Object childNodes : methodChildrenList)
                        {
                            if (childNodes instanceof Parameter)
                            {
                                Parameter paramNode = (Parameter) childNodes;
                                String pClassNode = paramNode.getType().toString();
                                List<Node> paramList = paramNode.getChildrenNodes();
                                String pClassName = paramList.get(0).toString();
                                methodString += pClassName;
                                methodString += " : ";
                                methodString += pClassNode;
                                if ( !typeMap.get(cleanName) && typeMap.containsKey(pClassNode))
                                {
                                    dependencyText += dependencyParser(cleanName,pClassNode);
                                }
                                dependencyText += ",";
                            }
                            else
                                {
                                String methodBody[] = childNodes.toString().split(" ");
                                for (String codeBlck : methodBody)
                                {

                                    if (!typeMap.get(cleanName) && typeMap.containsKey(codeBlck))
                                    {
                                        ArrayList<String> dependencyHolder = new ArrayList<String>();
                                        dependencyHolder.add("[");
                                        dependencyHolder.add(cleanName);
                                        dependencyHolder.add("] uses -.->");

                                        if (typeMap.get(codeBlck))
                                        {
                                            dependencyHolder.add("[<<interface>>;");
                                            dependencyHolder.add(codeBlck);
                                            dependencyHolder.add("]");

                                        }
                                        else
                                        {
                                            dependencyHolder.add("[");
                                            dependencyHolder.add(codeBlck);
                                            dependencyHolder.add("]");

                                        }
                                        for(String d : dependencyHolder)
                                        {
                                            dependencyText += d;
                                        }
                                        dependencyText += ",";
                                    }
                                }
                            }
                        }
                        methodString += ") : " + methDec.getType();
                        nextElems = true;
                    }
                }
            }
        }

        boolean nextItem= false;
        TypeDeclaration fieldNode = (TypeDeclaration) typeNode;
        for (BodyDeclaration codeBlock : fieldNode.getMembers())
        {
            if (codeBlock instanceof FieldDeclaration)
            {
                FieldsParser Fp = new FieldsParser();
                fieldString += Fp.fieldParser(codeBlock,nextItem,setterAndGetterHolder,cleanName,typeMap,classConnections);
                nextItem = true;

            }
        }
        boolean extending = false;
        extending = implementationDetector(typeDetect,"extension");
        if (extending && !typeDetect.isInterface()){
            dependencyText += "[";
            dependencyText += cleanName;
            dependencyText  += "] ";
            dependencyText +=  "-^ " + typeDetect.getExtends();
            dependencyText += ",";
        }

        boolean implementing = false;
        implementing = implementationDetector(typeDetect,"implementation");
        if (implementing)
        {
            ArrayList<String> implementationHolder = new ArrayList<String>();
            List<ClassOrInterfaceType> interfaceList = (List<ClassOrInterfaceType>) typeDetect.getImplements();
            for (ClassOrInterfaceType intface : interfaceList)
            {
                implementationHolder.add("[");
                implementationHolder.add(cleanName);
                implementationHolder.add("] ");
                implementationHolder.add("-.-^ ");
                implementationHolder.add("[");
                implementationHolder.add("<<interface>>;");
                implementationHolder.add(intface+"]");
                implementationHolder.add(",");

            }

            for(String Imple: implementationHolder)
            {
                dependencyText += Imple;
            }
        }
        ArrayList<String> parserHolder = new ArrayList<String>();

        parserHolder.add(cName);

        if (!fieldString.isEmpty()) {
            parserHolder.add("|");
            parserHolder.add(bracketsTransform(fieldString));

        }
        if (!methodString.isEmpty()) {
            parserHolder.add("|");
            parserHolder.add(bracketsTransform(methodString));

        }
        for(String parseText : parserHolder)
        {
            parseOutput += parseText;
        }

        parseOutput += "]";
        parseOutput += dependencyText;
        return parseOutput;
    }

    private boolean implementationDetector(ClassOrInterfaceDeclaration cid, String type)
    {
        switch(type)
        {
            case"extension":
                if(cid.getExtends() != null)
            {
                return true;
            }
            break;
            case "implementation":
                if(cid.getImplements() != null)
            {
                return true;
            }
            break;
        }
        return false;
    }



    private String bracketsTransform(String bracket)
    {
        bracket = bracket.replace(">", ")");
        bracket = bracket.replace("[", "(");
        bracket = bracket.replace("]", ")");
        bracket = bracket.replace("<", "(");
        return bracket;
    }

    public String dependencyParser(String justClassName, String pClass)
    {
        String addString = "";
        ArrayList<String> addStringHolder = new ArrayList<String>();

        addStringHolder.add("[");
        addStringHolder.add(justClassName);
        addStringHolder.add("] uses -.->");


        if (typeMap.get(pClass))
        {
            addStringHolder.add("[<<interface>>;");
            addStringHolder.add(pClass);
            addStringHolder.add("]");
        }
        else
        {
            addStringHolder.add("[");
            addStringHolder.add(pClass);
            addStringHolder.add("]");

        }
        for(String aString : addStringHolder)
        {
            addString += aString;
        }

        return addString;

    }






    private String getParam(String cName)
    {
        String parameter = "";
        int startIndex = cName.indexOf('(') + 1;
        int endIndex = cName.indexOf(')');
        parameter = cName.substring(startIndex, endIndex);
        return parameter;
    }

    private void TypeHolder(ArrayList<CompilationUnit> cuHolder) {
        for (CompilationUnit compUnit : cuHolder) {
            List<TypeDeclaration> typeHold = compUnit.getTypes();
            for (Node node : typeHold) {
                ClassOrInterfaceDeclaration elem_type = (ClassOrInterfaceDeclaration) node;
                if(!(elem_type.isInterface()))
                {
                    typeMap.put(elem_type.getName(),false);
                }
                else
                {
                    typeMap.put(elem_type.getName(), true);
                }
            }
        }
    }







}

