package com.wxl;

import java.io.*;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
	public static ArrayList<ArrayList<ControlFlowChart>> basePath;
	public static ArrayList<ControlFlowChart> listControlFlowChart;
	public static int allNum;
	public static ArrayList<String> allSentence;
	public static Boolean[] flagControl;

	private static void fileProcessing(String fileName) throws Exception {
		allSentence = new ArrayList<String>();
		File file = new File(fileName);
		BufferedReader reader = null;
		reader = new BufferedReader(new FileReader(file));
		String tempString = null;
		while ((tempString = reader.readLine()) != null) {

			allSentence.add(tempString);
		}
		reader.close();
	}

	private static void printTestCasesBeautiful(ArrayList<String> testCases) {
		int num = 0;
		for (int i = 0; i < testCases.size(); i++) {
			
				num++;
				System.out.println("测试用例" + num);
				String aCase = testCases.get(i);
				String[] p = aCase.split("##");
				for (String p1 : p) {
					if (p1.equals("")) {
						continue;
					}
					System.out.println(p1);
				}
			}
		
	}

	public static void main(String[] args) {
		String fileName = new String();
		fileName = "ceshi.txt";
		try {
			fileProcessing(fileName);
			CodeTree root = new CodeTree("root", "", allSentence);
			root.Create();
			allNum = root.initializationNodeNum(0);
			root.nextNode = -1;
			root.initializationNextNum(root);
			root.printCodeTree();
			listControlFlowChart = root.printControlFlowChart();	
			
			System.out.println("Cycle complexity : " + (root.getJudgmentNodeNum()));
//			for(ControlFlowChart a:listControlFlowChart){
//				System.out.println(a.start+"->"+a.end+" "+a.flag);
//			}
			root.bulidBasePathAndTestCases(allNum, listControlFlowChart);
			
			for(String a:root.basePath){
				System.out.println(a);
			}
			printTestCasesBeautiful(root.testCases);
			
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
}
