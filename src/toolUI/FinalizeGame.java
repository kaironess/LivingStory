package toolUI;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.Attributes;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.JarOutputStream;
import java.util.jar.Manifest;

import org.apache.tools.ant.Project;
import org.apache.tools.ant.ProjectHelper;
import org.apache.tools.ant.PropertyHelper;

import createdGameClasses.GameController;
import creationToolClasses.WIP;

public class FinalizeGame {
    
    public static void finalizeGame(GameController gc) {
        WIP wip = WIP.getWIP();
        String gameGC = makeGC(gc);
        
        // Run the xml file and make the game jar
        File buildFile = null;
        String workspace = "";
        
        if (wip.classLoader != null) {
            Path tempJar = createJAR();
        }
        else {
            buildFile = new File("src" + File.separator + "createGame.xml");
            workspace = System.getProperty("user.dir") + File.separator + "bin";
            runAntFile(workspace, buildFile);
        }
    }
    
    private static String makeGC(GameController gc) {
        // Create and save the GameController for the game
        try {
            FileOutputStream fos = new FileOutputStream("game.gc");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            
            oos.writeObject(gc);
            oos.close();
            
            return System.getProperty("user.dir") + File.separator + "game.gc";
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        
        return "";
    }
    
    private static Path createJAR() {
        try {
           JarOutputStream os = createJarOS();
           
           String workspace = "";
           WIP wip = WIP.getWIP();
           URL buildPath = wip.classLoader.getResource("createGame.xml");
           workspace = buildPath.toString().substring(0, buildPath.toString().lastIndexOf("/"));
           workspace = workspace.substring(workspace.indexOf("C"), workspace.length() - 1);
            
           Path tempJar = Files.createTempDirectory("tempJar");
           JarFile jf = new JarFile(workspace);
           Enumeration<JarEntry> allFiles = jf.entries();
           List<JarEntry> jarFiles = Collections.list(allFiles);
           jarFiles.sort(new Comparator<JarEntry>() {
              @Override
              public int compare(JarEntry arg0, JarEntry arg1) {
                  if ((arg0.isDirectory() && arg1.isDirectory()) || 
                      (!arg0.isDirectory() && !arg1.isDirectory()))
                      return 0;
                  else 
                      return arg0.isDirectory() ? -1 : 1;
              }
           });
          
           for (JarEntry file : jarFiles) {
              File f = new File(tempJar.toAbsolutePath() + java.io.File.separator + file.getName());
              if (file.isDirectory()) { // if its a directory, create it
                  f.mkdir();
                  continue;
              }
              InputStream is = jf.getInputStream(file); // get the input stream
              FileOutputStream fos = new FileOutputStream(f);
              while (is.available() > 0) {  // write contents of 'is' to 'fos'
                  fos.write(is.read());
              }
              fos.close();
              is.close();
              
           }
           jf.close();
           
           add(new File(tempJar.toUri()), os, tempJar.toString());
           add(new File(System.getProperty("user.dir") + File.separator + "game.gc"), os, tempJar.toString());
           
           File oldGC = new File(System.getProperty("user.dir") + File.separator + "game.gc");
           oldGC.delete();
           
           os.close();
           return tempJar;
        }
        catch (Exception e) { e.printStackTrace(); }
        
        return null;
    }
    
    private static JarOutputStream createJarOS() {
        JarOutputStream os = null;
        try {
            Manifest manifest = new Manifest();
            manifest.getMainAttributes().put(Attributes.Name.MANIFEST_VERSION, "1.0");
            manifest.getMainAttributes().put(Attributes.Name.MAIN_CLASS, "gameUI.Main");
            os = new JarOutputStream(new FileOutputStream("Game.jar"), manifest);
        }
        catch (Exception e) { e.printStackTrace(); }
        
        return os;
    }
    
    private static void add(File source, JarOutputStream target, String tempDir) throws IOException
    {
      BufferedInputStream in = null;
      tempDir = tempDir.replace("\\", "/");
      String workDir = System.getProperty("user.dir") + "/";
      workDir = workDir.replace(File.separator, "/");
      String path = source.getAbsolutePath().replace("\\", "/");
      path = path.replaceAll(tempDir + "/", "");
      path = path.replaceAll(tempDir, "");
      path = path.replaceAll(workDir, "");
      System.out.println(path);
      
      try
      {
        if (source.isDirectory())
        {
          String name = path;
          if (!name.isEmpty())
          {
            if (!name.endsWith("/"))
              name += "/";
            JarEntry entry = new JarEntry(name);
            entry.setTime(source.lastModified());
            target.putNextEntry(entry);
            target.closeEntry();
          }
          for (File nestedFile: source.listFiles()) {
            add(nestedFile, target, tempDir);
          }
          return;
        }

        if (path.contains("MANIFEST"))
            return; 
        
        JarEntry entry = new JarEntry(path);
        entry.setTime(source.lastModified());
        target.putNextEntry(entry);
        in = new BufferedInputStream(new FileInputStream(source));

        byte[] buffer = new byte[1024];
        while (true)
        {
          int count = in.read(buffer);
          if (count == -1)
            break;
          target.write(buffer, 0, count);
        }
        target.closeEntry();
      }
      finally
      {
        if (in != null)
          in.close();
      }
    }
    
    private static void runAntFile(String workspace, File buildFile) {
        workspace = workspace.replace(File.separator, "/");
        Project p = new Project();
        p.setUserProperty("ant.file", buildFile.getAbsolutePath());
        p.init();
        ProjectHelper helper = ProjectHelper.getProjectHelper();
        PropertyHelper propHelp = PropertyHelper.getPropertyHelper(p);
        propHelp.setNewProperty("dir.workspace", "dir.workspace", workspace);
        p.addReference("ant.projectHelper", helper);
        helper.parse(p, buildFile);
        p.executeTarget(p.getDefaultTarget());
    }

}
