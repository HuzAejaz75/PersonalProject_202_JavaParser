 
 public class SequenceDiagram{

   private ArrayList<CompilationUnit> getCuHolder(String inPath)
            throws Exception {
        File folder = new File(inPath);
        ArrayList<CompilationUnit> cuHolder = new ArrayList<CompilationUnit>();
        ArrayList<File> jFile = readFiles(folder);
        for(final File f : jFile){

                FileInputStream in = new FileInputStream(f);
                CompilationUnit compUnit = null;
                try {
                    compUnit = setCuHolder(in,compUnit);
                    cuHolder.add(compUnit);
                } finally {
                    in.close();
                }

        }
        return cuHolder;
    }
  private CompilationUnit setCuHolder(FileInputStream fileStream, CompilationUnit c) throws ParseException, IOException
    {
        c = JavaParser.parse(fileStream);
        return c;
    }
  
private ArrayList<File> readFiles(final File folderPath)
    {
        ArrayList<File> FileCollection = new ArrayList<File>();
        File[] fileHolder = folderPath.listFiles();
        for(final File fileUnit : fileHolder )
        {
            String fAddress = fileUnit.getPath();
            if(fAddress.toLowerCase().endsWith(".java"))
            {
                FileCollection.add(fileUnit);
            }
        }
        return FileCollection;

    }
 }
 
