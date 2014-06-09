package com.example.file.traversal;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

/** Recursive listing with SimpleFileVisitor in JDK 7. */
public class FileListingVisitor {
	FileStats fs = null;
	

	public FileStats processDirectory(String path)throws IOException{
		fs = new FileStats();
		FileVisitor<Path> fileProcessor = new ProcessFile();
		Files.walkFileTree(Paths.get(path), fileProcessor);
		return fs;
	}
	private  final class ProcessFile extends SimpleFileVisitor<Path> {
		
		@Override
		public FileVisitResult visitFile(Path aFile, BasicFileAttributes aAttrs)
				throws IOException {
//			System.out.println("Processing file:" + aFile);
			if(aFile.getFileName().toString().endsWith(".cls")){
				fs.getControllerLocations().put(aFile.getFileName().toString(), aFile.toString());
			}else if (aFile.getFileName().toString().endsWith(".page")){
				fs.getPageLocations().put(aFile.getFileName().toString(), aFile.toString());
			}
			return FileVisitResult.CONTINUE;
		}

		@Override
		public FileVisitResult preVisitDirectory(Path aDir,
				BasicFileAttributes aAttrs) throws IOException {
//			System.out.println("Processing directory:" + aDir);
			return FileVisitResult.CONTINUE;
		}
	}
}