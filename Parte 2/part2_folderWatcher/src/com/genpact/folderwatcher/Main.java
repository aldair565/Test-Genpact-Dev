package com.genpact.folderwatcher;

import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchEvent.Kind;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.List;

import javax.swing.JOptionPane;

public class Main {

	public void watch() throws Exception{
		
		//the folder will be searched inside current directory or you can insert a path, ie. C:/User/MyFolder
		String folderName = JOptionPane.showInputDialog(null, "Enter name of folder or path");
		
		//Create watcher object
		WatchService watcher = FileSystems.getDefault().newWatchService();
		Path dir = Paths.get(folderName); //path of folder to monitor
		
		//events to monitor, ie. new file, modify file or delete file
		dir.register(watcher, StandardWatchEventKinds.ENTRY_CREATE, StandardWatchEventKinds.ENTRY_DELETE, StandardWatchEventKinds.ENTRY_MODIFY);
		System.out.println("Monitoring folder: " + dir.getFileName());
		
		//loop to continous monitoring
		while (true) {
			WatchKey key;
			key = watcher.take();
			
			List<WatchEvent<?>> eventList = key.pollEvents();
			
			for (WatchEvent<?> event : eventList) {
				//get type of event
				Kind<?> eventType = event.kind();
				
				//get file name
				Path fileName = (Path)event.context();
				
				//testing event types
				//System.out.println(eventType.name() + ": " + fileName);
				
				//continue to avoid an unwanted event
				if (eventType == StandardWatchEventKinds.OVERFLOW) {
					continue;
				} else if(eventType == StandardWatchEventKinds.ENTRY_CREATE) {
					System.out.println("[New]" + fileName.toString() + " has been created");
				} else if (eventType == StandardWatchEventKinds.ENTRY_DELETE) {
					System.out.println("[Delete]" + fileName.toString() + " has been deleted");
				} else if (eventType == StandardWatchEventKinds.ENTRY_MODIFY) {
					System.out.println("[Modify]" + fileName.toString() + " has been modified");
				}
				
			}
			
			//reset key to avoid loop
			boolean valid = key.reset();
			if (!valid) {
				break;
			}
			
		}
	}
	
	public static void main(String[] args) throws Exception {
		
		Main app = new Main();
		app.watch();

	}

}
