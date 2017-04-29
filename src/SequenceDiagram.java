 
 public class SequenceDiagram{

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
 