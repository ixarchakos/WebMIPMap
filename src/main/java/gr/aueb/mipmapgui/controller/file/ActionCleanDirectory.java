package gr.aueb.mipmapgui.controller.file;

//giannisk
import gr.aueb.mipmapgui.Costanti;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.io.FileUtils;


public class ActionCleanDirectory {
    
    public ActionCleanDirectory() {}
    
    public void performAction(String user){
        try {
            String sourceTempDirectory = MipmapDirectories.getUserTempPath(user) + Costanti.SERVER_SOURCE_FOLDER;
            File directory = new File(sourceTempDirectory);
            FileUtils.cleanDirectory(directory);            
            String targetTempDirectory = MipmapDirectories.getUserTempPath(user) + Costanti.SERVER_TARGET_FOLDER;
            File directory2 = new File(targetTempDirectory);
            FileUtils.cleanDirectory(directory2);
        } catch (IOException ex) {
            Logger.getLogger(ActionDeleteMappingTask.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}