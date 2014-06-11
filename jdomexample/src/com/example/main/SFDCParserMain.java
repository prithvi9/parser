package com.example.main;

import java.io.IOException;

import com.example.file.traversal.FileListingVisitor;
import com.example.file.traversal.FileStats;
import com.example.main.apex.ApexParserEngine;

public class SFDCParserMain {

	public static void main(String[] args) {
		FileListingVisitor flv = new FileListingVisitor();
		FileStats fs = null;
		try {
			fs = flv.processDirectory("C:\\force.comproject\\DevOrgProject1\\src");
		} catch (IOException e) {
			e.printStackTrace();
		}
		ApexParserEngine ape = new ApexParserEngine(fs);
		ape.processVisualForcePage();
	}
	
}
