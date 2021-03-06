package gr.aueb.controllers;

import static gr.aueb.controllers.MappingController.user;
import gr.aueb.file.DiscardBOM;
import gr.aueb.mipmapgui.Costanti;
import gr.aueb.mipmapgui.controller.file.MipmapDirectories;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.security.Principal;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;
import org.json.simple.JSONObject;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FileUploadController {    
       
    @RequestMapping(value = "/UploadToSource", method = RequestMethod.POST)
    public String handleFileSourceUpload(HttpServletRequest request, Principal principal) throws IOException, ServletException {
        JSONObject outputObject = new JSONObject();
        String user = principal.getName();
        String path = MipmapDirectories.getUserTempPath(user) + Costanti.SERVER_SOURCE_FOLDER;        
        String type = request.getParameter("inputTypeSource");
        Part sourceFilePart = request.getPart(type+"SchemaSource"); 
        uploadfile(path, sourceFilePart);            
        return outputObject.toJSONString();
    }
    
    @RequestMapping(value = "/UploadToTarget", method = RequestMethod.POST)
    public String handleFileTargetUpload(HttpServletRequest request, Principal principal) throws IOException, ServletException {
        JSONObject outputObject = new JSONObject();
        String user = principal.getName();
        String path = MipmapDirectories.getUserTempPath(user) + Costanti.SERVER_TARGET_FOLDER;                   
        String type = request.getParameter("inputTypeTarget");
        Part targetFilePart = request.getPart(type+"SchemaTarget"); 
        uploadfile(path, targetFilePart);              
        return outputObject.toJSONString();
    }
    
    @RequestMapping(value = "/AddToSource", method = RequestMethod.POST, produces="text/plain")
    public String handleFileSourceAdd(@RequestParam("fileName") String fileName, @RequestParam("firstLine") String firstLine, Principal principal) throws IOException  {
        JSONObject outputObject = new JSONObject();
        String user = principal.getName();
        String path = MipmapDirectories.getUserTempPath(user) + Costanti.SERVER_SOURCE_FOLDER;   
        addFile(path, fileName, firstLine);      
        return outputObject.toJSONString();
    }
    
    @RequestMapping(value = "/AddToTarget", method = RequestMethod.POST, produces="text/plain")
    public String handleFileTargetAdd(@RequestParam("fileName") String fileName, @RequestParam("firstLine") String firstLine, Principal principal) throws IOException  {
        JSONObject outputObject = new JSONObject();
        String user = principal.getName();
        String path = MipmapDirectories.getUserTempPath(user) + Costanti.SERVER_TARGET_FOLDER;   
        addFile(path, fileName, firstLine);                      
        return outputObject.toJSONString();
    }
    
    @RequestMapping(value = "/RemoveFromSource", method = RequestMethod.POST, produces="text/plain")
    public String handleFileSourceRemove(@RequestParam("fileToDelete") String fileNameToDelete, Principal principal)  {
        JSONObject outputObject = new JSONObject();
        String user = principal.getName();
        String path = MipmapDirectories.getUserTempPath(user) + Costanti.SERVER_SOURCE_FOLDER;   
        removeFile(path, fileNameToDelete);            
        return outputObject.toJSONString();
    }
    
    @RequestMapping(value = "/RemoveFromTarget", method = RequestMethod.POST, produces="text/plain")
    public String handleFileTargetRemove(@RequestParam("fileToDelete") String fileNameToDelete, Principal principal)  {
        JSONObject outputObject = new JSONObject();
        String user = principal.getName();
        String path = MipmapDirectories.getUserTempPath(user) + Costanti.SERVER_TARGET_FOLDER;   
        removeFile(path, fileNameToDelete);                      
        return outputObject.toJSONString();
    }
    
    @ExceptionHandler(Exception.class)
    public String handleException(HttpServletRequest request, Exception ex){ 
        JSONObject outputObject = new JSONObject();
        outputObject.put("exception","Server exception: file could not be uploaded to the server. Please, try again.");
        return outputObject.toJSONString();
    }  

    private void uploadfile(String path, Part filePart) throws IOException{
        String [] fileNameArray = filePart.getSubmittedFileName().split("\\\\");               
        String fileName = fileNameArray[fileNameArray.length-1];

        File file = new File(path, fileName);
        InputStream input = filePart.getInputStream();
        Files.copy(DiscardBOM.checkForUtf8BOMAndDiscardIfAny(input), file.toPath());                        
    }
    
    private void addFile(String path, String fileName, String firstLine) throws IOException{
        File file = new File(path + fileName);
        try (FileWriter writer = new FileWriter(file)) {
            writer.write(firstLine);
        }
    }
    
    private void removeFile(String path, String fileNameToDelete){
        File deleteFile = new File(path + fileNameToDelete);
        // check if the file  present or not
        if( deleteFile.exists() ){
            deleteFile.delete();
        } 
    }
    
}
