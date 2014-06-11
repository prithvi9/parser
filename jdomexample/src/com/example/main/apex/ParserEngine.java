package com.example.main.apex;

import java.util.List;
import java.util.Map;

public interface ParserEngine {
	public Map<String, Map<String,List<String>>>  processVisualForcePage();
	public void processStandardLayout();
}
