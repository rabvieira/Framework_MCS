package com.ui.mvc;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.ui.Message;
import com.ui.mcs.Experimenting;
import com.ui.mcs.Experimento;
import com.ui.mcs.Experiments;
import com.ui.storage.StorageFileNotFoundException;
import com.ui.storage.StorageService;
import com.ui.util.UnZip;

import java.io.File;
import java.io.IOException;
import java.util.stream.Collectors;

@Controller
//@RequestMapping("/")
public class MessageController {
	
	//private Experiments ex = new Experiments();
	private Experimenting ex = new Experimenting();
	String buffer = "testing";
	ArrayList<Boolean> checkBoxClassifiers = new ArrayList<Boolean>();
    private final StorageService storageService;
    //private static final String INPUT_ZIP_FILE =   "C:\\Users\\rvieira\\Documents\\workspace-sts-3.8.4.RELEASE\\spring-boot-master\\spring-boot-samples\\spring-boot-sample-web-ui\\upload-dir\\new_feat_tes.zip";
    //private static final String OUTPUT_FOLDER   =  "C:\\Users\\rvieira\\Documents\\workspace-sts-3.8.4.RELEASE\\spring-boot-master\\spring-boot-samples\\spring-boot-sample-web-ui\\upload-dir";
    //private static final String PATH_ZIP_FILE =    "C:\\Users\\rvieira\\Documents\\workspace-sts-3.8.4.RELEASE\\FrameworkMCS\\upload-dir\\";
    private static final String PATH_ZIP_FILE =     "upload-dir/";
    
    
    @Autowired
    public MessageController(StorageService storageService) {
        this.storageService = storageService;
    }

    @GetMapping("/")
    public String listUploadedFiles(Model model) throws IOException {

        model.addAttribute("files", storageService
                .loadAll()
                .map(path ->
                        MvcUriComponentsBuilder
                                .fromMethodName(MessageController.class, "serveFile", path.getFileName().toString())
                                .build().toString())
                .collect(Collectors.toList()));

        return "uploadForm";
    }

    @GetMapping("/files/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> serveFile(@PathVariable String filename) {

        Resource file = storageService.loadAsResource(filename);
        return ResponseEntity
                .ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\""+file.getFilename()+"\"")
                .body(file);
    }

    @PostMapping("/")
    public String handleFileUpload(@RequestParam("file") MultipartFile file,
                                   RedirectAttributes redirectAttributes) {

        storageService.store(file);
        redirectAttributes.addFlashAttribute("message",
                "You successfully uploaded " + file.getOriginalFilename() + "!");

        return "redirect:/running";
    }

    @ExceptionHandler(StorageFileNotFoundException.class)
    public ResponseEntity handleStorageFileNotFound(StorageFileNotFoundException exc) {
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/index")
    public String createIndex(Model model) {
        return "index";
    }
    
    @GetMapping("/experiment")
    public String messageForm(Model model) {
    	model.addAttribute("message", new Message());
        return "experiment";
    }

    @GetMapping("/examples")
    public String createExamples(Model model) {
    	model.addAttribute("message", new Message());
        return "examples";
    }

    @GetMapping("/documentation")
    public String createDocumentation(Model model) {
        return "documentation";
    }
	
    @GetMapping("/running")
    public String createRunning(Model model) throws IOException {
    	model.addAttribute("message", new Message());
        model.addAttribute("files", storageService
                .loadAll()
                .map(path ->
                        MvcUriComponentsBuilder
                                .fromMethodName(MessageController.class, "serveFile", path.getFileName().toString())
                                .build().toString())
                .collect(Collectors.toList()));
        return "running";
    }

    @PostMapping("/experiment")
    public String experimentSubmit(@ModelAttribute Message message,
    		@RequestParam("file") MultipartFile file, 
    		//@RequestParam("file2") MultipartFile file2,
            RedirectAttributes redirectAttributes) throws Exception {
    	
		String pathname = new String("upload-dir/");
		File path = new File(pathname);
		File[] feat = path.listFiles();
		for(File i : feat){
	    	if(i.delete()){
	    	System.out.println(file.getName() + " is deleted!");
	    	}else{
	    		System.out.println("Delete operation is failed.");
	    	}
		}
    	
    	
        storageService.store(file);
        redirectAttributes.addFlashAttribute("message", "You successfully uploaded " + file.getOriginalFilename() + "!");

        //storageService.store(file2);
        //redirectAttributes.addFlashAttribute("message", "You successfully uploaded " + file2.getOriginalFilename() + "!");
        //return "redirect:/";

//    	if(message.getCheckbox_1() == true)
//    		checkBoxClassifiers.add(true);
//    	else
//    		checkBoxClassifiers.add(false);
//    	
//    	if(message.getCheckbox_2() == true)
//    		checkBoxClassifiers.add(true);
//    	else
//    		checkBoxClassifiers.add(false);
//    	
//    	if(message.getCheckbox_3() == true)
//    		checkBoxClassifiers.add(true);
//    	else
//    		checkBoxClassifiers.add(false);
//    	
//    	if(message.getCheckbox_4() == true)
//    		checkBoxClassifiers.add(true);
//    	else
//    		checkBoxClassifiers.add(false);
    	
    	String INPUT_ZIP_FILE = PATH_ZIP_FILE + file.getOriginalFilename();
    	//System.out.println("hey:   " + file.getOriginalFilename()); 
    	//System.out.println(INPUT_ZIP_FILE);
    	String OUTPUT_FOLDER = PATH_ZIP_FILE;
    	//System.out.println(OUTPUT_FOLDER);
    	UnZip unZip = new UnZip();
    	unZip.unZipIt(INPUT_ZIP_FILE, OUTPUT_FOLDER);
    	//INPUT_ZIP_FILE = PATH_ZIP_FILE + file2.getOriginalFilename();
    	//unZip.unZipIt(INPUT_ZIP_FILE, OUTPUT_FOLDER);
    	
    	//buffer = ex.run_experiment(message.getFolds(), message.getcheckBoxClassifiers);
    	//buffer = ex.run_experiment(message, file.getOriginalFilename());
    	buffer = ex.run_experiment(message, file.getOriginalFilename());
    	
    	//System.out.println(buffer);
    	message.setReport(buffer);

    	//try{
    		//File filex = new File(PATH_ZIP_FILE + file.getOriginalFilename());
    		pathname = new String("upload-dir/");
    		path = new File(pathname);
    		feat = path.listFiles();
    		for(File i : feat){
    		
	    		//System.out.println(PATH_ZIP_FILE + file.getOriginalFilename());
    			if(i.getName().endsWith("result.arff") == false)
		    		if((i.getName().endsWith("zip") || i.getName().endsWith("arff")) && i.delete()){
		    			System.out.println(file.getName() + " is deleted!");
		    		}else{
		    			System.out.println("Delete operation is failed.");
		    		}
    		}
    	//}catch(Exception e){

    	//	e.printStackTrace();
    	//}
    	
    	return "redirect:/running";
    }
   
    @PostMapping("/examples")
    public String examplesSubmit(@ModelAttribute Message message) throws Exception {
    	Experimento ex1 = new Experimento();
    	System.err.println(message.getFolds());
    	System.err.println(message.getCheckbox_1());
    	System.err.println(message.getCheckbox_2());
    	System.err.println(message.getCheckbox_3());
    	System.err.println(message.getCheckbox_4());
    	if(message.getCheckbox_1() == true)
    		checkBoxClassifiers.add(true);
    	else
    		checkBoxClassifiers.add(false);
    	if(message.getCheckbox_2() == true)
    		checkBoxClassifiers.add(true);
    	else
    		checkBoxClassifiers.add(false);
    	if(message.getCheckbox_3() == true)
    		checkBoxClassifiers.add(true);
    	else
    		checkBoxClassifiers.add(false);
    	if(message.getCheckbox_4() == true)
    		checkBoxClassifiers.add(true);
    	else
    		checkBoxClassifiers.add(false);
    	buffer = ex1.run_experiment(message.getFolds(), checkBoxClassifiers);
    	System.out.println(buffer);
    	message.setReport(buffer);
        return "running";
    }
    
}
