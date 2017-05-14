import com.github.javaparser.ast.body.BodyDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;

import com.github.javaparser.*;
import com.github.javaparser.ast.*;
import com.github.javaparser.ast.body.*;
import com.github.javaparser.ast.type.ClassOrInterfaceType;


public class FieldsParser {

    public String fieldParser(BodyDeclaration bodyDec, boolean nextField, ArrayList<String> setterAndGetterHolder, String cName, HashMap<String, Boolean> typeMap,HashMap<String, String> classConnections)
    {
        String fieldsOutput = "";
        FieldDeclaration fDec = ((FieldDeclaration)bodyDec);
        String scopeVal = bodyDec.toStringWithoutComments().substring(0, bodyDec.toStringWithoutComments().indexOf(" "));
        String fScope =  modifierTransform(scopeVal);


        String fClass = bracketsTransform(fDec.getType().toString());
        List<Node> fieldList = fDec.getChildrenNodes();
        String fName = fieldList.get(1).toString();

        if (fName.contains("="))
        {
            List<Node> tempNodes = fDec.getChildrenNodes();
            String tempName = tempNodes.get(1).toString();
            int tempIndex= tempName.indexOf("=");
            fName = tempName.substring(0,tempIndex-1);
        }



        if(fScope.equals("-"))
        {
            String classNameLower = fName.toLowerCase();
            if(setterAndGetterHolder.contains(classNameLower))
            {
                fScope = "+";
            }
        }

        dependencyDetector(fClass, cName,typeMap ,classConnections);


        if(fScope == "+" || fScope =="-")
        {
            if(nextField)
                fieldsOutput +="; ";
            fieldsOutput += fScope +" "+fName+" : "+ fClass;

        }
        return fieldsOutput;
    }


    private void dependencyDetector(String fClassName,String className,HashMap<String, Boolean> typeMap, HashMap<String, String> classConnections)
    {
        boolean hasParam = false;

        String referenceClass = "";

        if (fClassName.contains("(")) {
            hasParam = true;
        }

        if (hasParam) {
            referenceClass = getParam(fClassName);

        } else if (typeMap.containsKey(fClassName)) {
            referenceClass = fClassName;
        }
        if (referenceClass.length() > 0)
        {
            if (typeMap.containsKey(referenceClass)) {
                String buildDependancy = "-";
                if (classConnections.containsKey(referenceClass + "-" + className)) {
                    buildDependancy = classConnections.get(referenceClass + "-" + className);
                    if (hasParam)
                        buildDependancy = "*" + buildDependancy;
                    classConnections.put(referenceClass + "-" + className, buildDependancy);

                } else {
                    if (hasParam)
                        buildDependancy += "*";
                    classConnections.put(className + "-" + referenceClass, buildDependancy);
                }
            }
        }
    }
        private String modifierTransform(String modifier) {

            if(modifier.equalsIgnoreCase("Public"))
            {
                return "+";
            }
            else
            if(modifier.equalsIgnoreCase("Private"))
            {
                return "-";
            }
            else
            {
                return "";
            }

    }


    private String bracketsTransform(String bracket) {
        bracket = bracket.replace("[", "(");
        bracket = bracket.replace("]", ")");
        bracket = bracket.replace("<", "(");
        bracket = bracket.replace(">", ")");
        return bracket;
    }
    private String getParam(String cName)
    {
        String parameter = "";
        int startIndex = cName.indexOf('(') + 1;
        int endIndex = cName.indexOf(')');
        parameter = cName.substring(startIndex, endIndex);
        return parameter;
    }

}
