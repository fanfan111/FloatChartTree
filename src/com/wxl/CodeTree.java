package com.wxl;

//还不能处理 switch case default return break continue 语句 其他语句看起来正常
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CodeTree extends Sentence {
	public String name;// if
	public String sentence;
	// public ConditionTree conditions; Temporarily not achieved
	private ArrayList<String> allSentence;// 应删除
	public ArrayList<Sentence> sons;
	public int nodeNumT;
	private int judgmentNodeNum;
	public ArrayList<String> basePath;
	public ArrayList<String> testCases;


	public int getJudgmentNodeNum() {
		return this.judgmentNodeNum;
	}

	CodeTree(String name, String sentence, ArrayList<String> allInputString) {
		this.name = name;
		this.sentence = sentence;
		if ((this.name.equals("else") && this.sentence.equals("")) || this.name.equals("default")
				|| this.name.equals("switch")) {
			this.judgmentNodeNum = 0;
		} else {
			this.judgmentNodeNum = 1;
		}
		this.basePath = new ArrayList<String>();
		this.testCases = new ArrayList<String>();
		this.allSentence = new ArrayList<String>();
		this.sons = new ArrayList<Sentence>();
		this.flag = false;
		this.allSentence = new ArrayList<String>();
		for (String str : allInputString) {
			this.allSentence.add(str);
		}
	}

	private String isSequential(String OneLineOfCode) {
		Pattern keyWord = Pattern.compile("case|default|switch|do|else|for|if|while");// break
																						// continue
		Matcher resultKey = keyWord.matcher(OneLineOfCode);
		String reString = "";
		if (resultKey.find()) {
			if (resultKey.start() != 0) {//处理dobule类型
				if ((OneLineOfCode.charAt(resultKey.start() - 1) > 'a'
						&& OneLineOfCode.charAt(resultKey.start() - 1) < 'z')
						|| (OneLineOfCode.charAt(resultKey.start() - 1) > 'A'
								&& OneLineOfCode.charAt(resultKey.start() - 1) < 'Z')) {
					return reString;
				}
			}
			if (resultKey.end() + 1 < (OneLineOfCode.length())) {
				if ((OneLineOfCode.charAt(resultKey.end() + 1) > 'a' && OneLineOfCode.charAt(resultKey.end() + 1) < 'z')
						|| (OneLineOfCode.charAt(resultKey.end() + 1) > 'A'
								&& OneLineOfCode.charAt(resultKey.end() + 1) < 'Z')) {
					return reString;
				}
			}
			reString = resultKey.group();
		}
		return reString;
	}

	private String findTheParentheses(String OneLineOfCode) throws Exception {
		Pattern left = Pattern.compile("\\(");
		Pattern right = Pattern.compile("\\)");
		Matcher resultleft = left.matcher(OneLineOfCode);
		Matcher resultright = right.matcher(OneLineOfCode);
		if (!resultleft.find() || !resultright.find()) {
			return "";
		}

		return OneLineOfCode.substring(resultleft.end(), resultright.start());
	}

	private boolean findElse(String OneLineOfCode) {
		Pattern left = Pattern.compile("else");
		Matcher resultleft = left.matcher(OneLineOfCode);
		return resultleft.find();
	}

	private int scanning(ArrayList<String> bigParantheses, int scanningNum) {
		int num = 0;
		Pattern left = Pattern.compile("\\{");
		Pattern right = Pattern.compile("\\}");
		for (int i = scanningNum; i < allSentence.size(); i++) {
			String OneLineOfCode = allSentence.get(i);
			Matcher resultright = right.matcher(OneLineOfCode);
			Matcher resultleft = left.matcher(OneLineOfCode);

			if (resultleft.find()) {
				num++;
				if (num > 1) {
					bigParantheses.add(OneLineOfCode);
				}
				continue;
			}
			boolean flag = resultright.find();
			if (!flag && num >= 1) {
				bigParantheses.add(OneLineOfCode);
			} else if (flag) {
				num--;
				if (num > 0) {
					bigParantheses.add(OneLineOfCode);
				}
				if (num == 0) {
					scanningNum = i + 1;
					break;
				}
			}
		}
		return scanningNum;
	}

	private int saveIf(int scanningNum) throws Exception {
		String OneLineOfCode = allSentence.get(scanningNum);

		ArrayList<String> bigParantheses = new ArrayList<String>();
		scanningNum = scanning(bigParantheses, scanningNum);
		CodeTree newCodeTree = new CodeTree("if", findTheParentheses(OneLineOfCode), bigParantheses);
		sons.add(newCodeTree);
		newCodeTree.Create();
		if (scanningNum < (allSentence.size() - 1)) {
			while (findElse(allSentence.get(scanningNum))) {
				OneLineOfCode = allSentence.get(scanningNum);
				ArrayList<String> bigParanthesesElse = new ArrayList<String>();
				String isHave = findTheParentheses(OneLineOfCode);
				scanningNum = scanning(bigParanthesesElse, scanningNum);
				CodeTree newCodeTreeElse = new CodeTree("else", isHave, bigParanthesesElse);
				sons.add(newCodeTreeElse);
				newCodeTreeElse.Create();
				if (scanningNum > (allSentence.size() - 1)) {
					break;
				}
			}
		}
		return scanningNum;
	}

	private int saveSwitch(int scanningNum) throws Exception {

		CodeTree newCodeSwitch = new CodeTree("switch", findTheParentheses(allSentence.get(scanningNum)),
				new ArrayList<String>());
		ArrayList<Sentence> yourFatherSon = newCodeSwitch.sons;
		sons.add(newCodeSwitch);
		Pattern searchCaseD = Pattern.compile("case|default");
		Pattern right = Pattern.compile("\\}");
		Pattern left = Pattern.compile("\\{");

		for (int i = scanningNum; i < allSentence.size(); i++) {
			String OneLineOfCode = allSentence.get(i);
			Matcher resultleft = left.matcher(OneLineOfCode);
			if (resultleft.find()) {
				scanningNum = i + 1;
				break;
			}
		}

		for (int i = scanningNum; i < allSentence.size(); i++) {
			scanningNum = i + 1;
			String OneLineOfCode = allSentence.get(i);
			Matcher resultright = right.matcher(OneLineOfCode);
			Matcher resultSearchCaseD = searchCaseD.matcher(OneLineOfCode);
			if (resultright.find()) {
				break;
			}
			if (resultSearchCaseD.find()) {
				String find = resultSearchCaseD.group();

				Pattern colon = Pattern.compile("\\:");
				Matcher resultColon = colon.matcher(OneLineOfCode);
				if (!resultColon.find()) {
					throw new Exception("Error");
				}
				String storage = "";
				if (find.equals("case")) {
					storage = OneLineOfCode.substring(resultSearchCaseD.end(), resultColon.start());
				}
				ArrayList<String> caseStorage = new ArrayList<String>();
				OneLineOfCode = allSentence.get(i + 1);
				for (int j = i + 1; !searchCaseD.matcher(OneLineOfCode).find(); j++) {
					caseStorage.add(OneLineOfCode);
					if (j > allSentence.size() - 2) {
						break;
					}
					OneLineOfCode = allSentence.get(j + 1);
					Matcher resultright1 = right.matcher(OneLineOfCode);
					if (resultright1.find()) {
						break;
					}
					i = j;
				}
				CodeTree newCodeTree = new CodeTree(find, storage, caseStorage);
				newCodeTree.Create();
				yourFatherSon.add(newCodeTree);
			}
		}
		return scanningNum;

	}

	private int saveNull(String OneLineOfCode, int scanningNum) {
		Pattern left = Pattern.compile("[^\\s\\n]");
		Matcher resultleft = left.matcher(OneLineOfCode);
		if (resultleft.find()) {
			if (sons.size() > 0) {
				Sentence sonbf = sons.get(sons.size() - 1);
				if (sonbf.flag == true) {
					SequentialBlock q = (SequentialBlock) sonbf;
					q.insert(OneLineOfCode);
				} else {
					SequentialBlock q = new SequentialBlock();
					q.insert(OneLineOfCode);
					sons.add(q);
				}
			} else {
				SequentialBlock q = new SequentialBlock();
				q.insert(OneLineOfCode);
				sons.add(q);
			}
		}
		scanningNum++;
		return scanningNum;
	}

	public void Create() throws Exception {
		// |switch| do| for|if| while
		for (int scanningNum = 0; scanningNum < allSentence.size();) {
			String OneLineOfCode = allSentence.get(scanningNum);
			String reString = isSequential(OneLineOfCode);
			if (reString.equals("")) {
				scanningNum = saveNull(OneLineOfCode, scanningNum);
			}
//			else if(reString.equals("return")){
//				
//			} 
			else {
				if (reString.equals("if")) {
					scanningNum = saveIf(scanningNum);
				} else if (reString.equals("while") || reString.equals("for")) {
					ArrayList<String> bigParantheses = new ArrayList<String>();
					scanningNum = scanning(bigParantheses, scanningNum);
					CodeTree newCodeTree = new CodeTree(reString, findTheParentheses(OneLineOfCode), bigParantheses);
					sons.add(newCodeTree);
					newCodeTree.Create();
				} else if (reString.equals("do")) {
					ArrayList<String> bigParantheses = new ArrayList<String>();
					scanningNum = scanning(bigParantheses, scanningNum);
					OneLineOfCode = allSentence.get(scanningNum - 1);
					CodeTree newCodeTree = new CodeTree(reString, findTheParentheses(OneLineOfCode), bigParantheses);
					sons.add(newCodeTree);
					newCodeTree.Create();
				} else if (reString.equals("switch")) {
					scanningNum = saveSwitch(scanningNum);
				} else {
					throw new Exception("Error");
				}
			}
		}
	}

	public int initializationNodeNum(int parameter) {
		// Only can add your own node number

		for (int i = 0; i < sons.size(); i++) {
			Sentence son = sons.get(i);
			son.nodeNumber = parameter;
			parameter++;
			if (son.flag == false) {

				CodeTree cSon = (CodeTree) son;
				parameter = cSon.initializationNodeNum(parameter);
			}
		}
		return parameter;

	}

	private int nextAuxiliary(CodeTree root, int myNum) throws Exception {
		CodeTree scanning = root;
		int i = 0;

		Sentence son = scanning.sons.get(i);
		while (true) {
			if (i == scanning.sons.size()) {
				System.out.println(myNum);
				throw new Exception("Error");
			}
			son = scanning.sons.get(i);
			if (son.nodeNumber == myNum) {
				int reNum = -1;
				if (this.name.equals("case") || this.name.equals("default")) {
					reNum = scanning.nextNode;
				} else {
					while (true) {
						if (i == scanning.sons.size() - 1) {
							reNum = nextNode;
							break;
						}
						Sentence uncle = scanning.sons.get(i + 1);
						if (uncle.flag == true) {
							reNum = uncle.nodeNumber;
							break;
						} else {
							CodeTree q = (CodeTree) uncle;
							if (q.name.equals("else")) {
								i++;
								continue;
							} else {
								reNum = uncle.nodeNumber;
								break;
							}
						}
					}
				}
				return reNum;
			} else if (son.nodeNumber > myNum || i == (scanning.sons.size() - 1)) {
				if (i == 0) {
					throw new Exception("Error");
				}
				if (i == (scanning.sons.size() - 1))
					i++;
				scanning = (CodeTree) scanning.sons.get(i - 1);
				i = 0;
				continue;
			}
			i++;
		}

	}

	public void initializationNextNum(CodeTree root) throws Exception {
		// T or F 处理 过程
		for (int i = 0; i < sons.size() - 1; i++) {
			Sentence son = sons.get(i);
			son.nextNode = sons.get(i + 1).nodeNumber;
			if (son.flag == false) {
				CodeTree cSon = (CodeTree) son;
				if (cSon.sons.size() == 0) {
					throw new Exception("Error");
				}
				cSon.nodeNumT = cSon.sons.get(0).nodeNumber;
				cSon.initializationNextNum(root);
			}
		}
		if (sons.size() > 0) {
			Sentence son = sons.get(sons.size() - 1);
			if (this.name.equals("if") || this.name.equals("else") || this.name.equals("case")
					|| this.name.equals("default")) {
				son.nextNode = nextAuxiliary(root, nodeNumber);
			} else if (this.name.equals("root")) {
				son.nextNode = -1;
			} else if (this.name.equals("while") || this.name.equals("do") || this.name.equals("for")) {
				son.nextNode = nodeNumber;

			} else if (this.name.equals("switch")) {
				son.nextNode = nextNode;

			} else {
				throw new Exception("Error");
			}

			if (son.flag == false) {
				CodeTree cSon = (CodeTree) son;
				if (cSon.sons.size() == 0) {
					throw new Exception("Error");
				}
				cSon.nodeNumT = cSon.sons.get(0).nodeNumber;
				cSon.initializationNextNum(root);
			}
		}
	}

	public String printCodeTree() {
		if (!this.name.equals("root")) {
			System.out.println(name + " (" + sentence + "){\n" + "NodeNumber:" + this.nodeNumber + "    nextNode:"
					+ this.nextNode + "   nodeNumT:" + this.nodeNumT + "\n-*-*-*-*-");
		}
		for (Sentence son : sons) {
			if (son.flag) {
				SequentialBlock q = (SequentialBlock) son;//
				System.out.print(q.printSequentialBlock() + "NodeNumber:" + q.getNodeNumber() + "    nextNode:"
						+ q.nextNode + "\n-*-*-*-*-\n");
			} else {
				CodeTree q = (CodeTree) son;
				q.printCodeTree();
			}
		}
		if (!this.name.equals("root")) {
			System.out.println("}");
		}
		return "";
	}

	public ArrayList<ControlFlowChart> printControlFlowChart() {
		ArrayList<ControlFlowChart> resultPrint = new ArrayList<ControlFlowChart>();
		for (Sentence son : sons) {
			if (son.flag) {
				SequentialBlock q = (SequentialBlock) son;//
				resultPrint.add(new ControlFlowChart(q.nodeNumber, q.nextNode, 'N'));
			} else {
				CodeTree q = (CodeTree) son;
				if ((q.name.equals("else") && q.sentence.equals("")) || q.name.equals("default")
						|| q.name.equals("switch")) {

					resultPrint.add(new ControlFlowChart(q.nodeNumber, q.nodeNumT, 'N'));
					for (ControlFlowChart i : q.printControlFlowChart()) {
						resultPrint.add(i);
					}
					judgmentNodeNum += q.getJudgmentNodeNum();
					continue;

				}

				resultPrint.add(new ControlFlowChart(q.nodeNumber, q.nextNode, 'F'));
				resultPrint.add(new ControlFlowChart(q.nodeNumber, q.nodeNumT, 'T'));
				for (ControlFlowChart i : q.printControlFlowChart()) {
					resultPrint.add(i);
				}
				judgmentNodeNum += q.getJudgmentNodeNum();
			}

		}
		
		return resultPrint;
	}
	private void howToDo(int allNum, ArrayList<ControlFlowChart> listControlFlowChart){
		Boolean[][] flag  = new Boolean[allNum][allNum];
		for(int i = 0 ; i < allNum;i++){
			for(int j = 0 ; j < allNum; j++){
				flag[i][j]= false;
			}
		}
		for(int i = 0 ; i <listControlFlowChart.size();i++ ){
			ControlFlowChart a = listControlFlowChart.get(i);
			if(a.end == -1){
				continue;
			}
			flag[a.start][a.end] = true;
		}
		for(int i = 0 ;i < this.basePath.size();){
			String[] p = this.basePath.get(i).split("->");
			if(p.length == 1){
				break;
			}
			boolean continueflag = true;
			for(int j = 0 ; j < p.length-1;j++){
				int numStart = Integer.parseInt(p[j]);
				int numEnd = Integer.parseInt(p[j+1]);
				if(flag[numStart][numEnd]==true){
					flag[numStart][numEnd] = false;
					continueflag = false;
				}
			}
			if(continueflag){
				this.basePath.remove(i);
				this.testCases.remove(i);
				continue;
			}
			i++;
		}
		
		
	}
	public void bulidBasePathAndTestCases(int allNum, ArrayList<ControlFlowChart> listControlFlowChart) throws Exception {

		for (int k = 0; k < sons.size(); k++) {
			Sentence son = sons.get(k);
			if (son.flag) {
				if (basePath.isEmpty()) {
					basePath.add(String.valueOf(son.nodeNumber));
					testCases.add("");
				} else {
					for (int i = 0; i < basePath.size(); i++) {
						String a = basePath.get(i) + "->" + String.valueOf(son.nodeNumber);
						basePath.set(i, a);
					}
				}
			} else {
				CodeTree q = (CodeTree) son;
				q.bulidBasePathAndTestCases(allNum, listControlFlowChart);
				ArrayList<String> tempBasePath = new ArrayList<String>();

				ArrayList<String> tempTestCase = new ArrayList<String>();
				if (basePath.size() != testCases.size()) {
					throw new Exception("Error!");
				}
				int n = basePath.size();
				if (q.name.equals("do")) {
					for (int i = 0; i < n; i++) {
						String temptBase = basePath.remove(0);
						String temptTest = testCases.remove(0);
						for (int j = 0; j < q.basePath.size(); j++) {
							tempBasePath.add(temptBase + "->" + q.basePath.get(j));
							tempTestCase.add(temptTest + "##" + q.testCases.get(j));
						}
					}
				} else {
					for (int i = 0; i < n; i++) {
						tempBasePath.add(basePath.remove(0));
						tempTestCase.add(testCases.remove(0));
					}
				}
				if (basePath.size() != 0) {
					throw new Exception("Error");
				}
				if (tempBasePath.size() == 0) {
					tempBasePath.add("");
					tempTestCase.add("");
				}
				if (q.name.equals("if")) {
					for (int i = 0; i < tempBasePath.size(); i++) {
						for (int m = 0; m < q.basePath.size(); m++) {
							basePath.add(tempBasePath.get(i) + "->" + son.nodeNumber + "->" + q.basePath.get(m));
							testCases.add(tempTestCase.get(i) + "##" + q.sentence + " T" + "##" + q.testCases.get(m));
						}
						tempBasePath.set(i, tempBasePath.get(i) + "->" + son.nodeNumber);
						tempTestCase.set(i, tempTestCase.get(i) + "##" + q.sentence + " F");
					}
					boolean flagScanning = false;
					if (k + 1 <= sons.size() - 1) {
						while (sons.get(k + 1).flag == false) {
							CodeTree p = (CodeTree) sons.get(k + 1);
							p.bulidBasePathAndTestCases(allNum, listControlFlowChart);
							if (p.name.equals("else")) {
								k++;
								flagScanning = true;
								for (int i = 0; i < tempBasePath.size(); i++) {
									for (int m = 0; m < p.basePath.size(); m++) {
										basePath.add(
												tempBasePath.get(i) + "->" + p.nodeNumber + "->" + p.basePath.get(m));
										testCases.add(tempTestCase.get(i) + "##" + p.sentence + " T" + "##"
												+ p.testCases.get(m));
									}
									tempBasePath.set(i, tempBasePath.get(i) + "->" + p.nodeNumber);
									tempTestCase.set(i, tempTestCase.get(i) + "##" + p.sentence + " F");
								}
								if (k + 1 <= sons.size() - 1)
									continue;
							}
							break;
						}
					}
					if (flagScanning == false) {
						for (int i = 0; i < tempBasePath.size(); i++) {
							basePath.add(tempBasePath.get(i));
							testCases.add(tempTestCase.get(i));

						}
					}
					continue;
				} else if (q.name.equals("while") || q.name.equals("for") || q.name.equals("do")) {
					for (int i = 0; i < tempBasePath.size(); i++) {
						basePath.add(tempBasePath.get(i) + "->" + q.nodeNumber);
						testCases.add(tempTestCase.get(i) + "##" + q.sentence + " F");
					}
					for (int i = 0; i < tempBasePath.size(); i++) {
						for (int m = 0; m < q.basePath.size(); m++) {
							basePath.add(tempBasePath.get(i) + "->" + son.nodeNumber + "->" + q.basePath.get(m) + "->"
									+ q.nodeNumber);
							testCases.add(tempTestCase.get(i) + "##" + q.sentence + " T" + "##" + q.testCases.get(m)
									+ "##" + q.sentence + " F");
						}
					}
					continue;
				}
				for (int i = 0; i < tempBasePath.size(); i++) {
					for (String instring : q.basePath) {
						basePath.add(tempBasePath.get(i) + "->" + son.nodeNumber + "->" + instring);
						testCases.add(tempTestCase.get(i) + "##" + q.sentence + " T");
					}
				}
			}

		}
		if (sons.get(0).flag == false) {
			for (int j = 0; j < basePath.size(); j++) {
				basePath.set(j, basePath.get(j).substring(2));
			}
		}
		//应添加一个扫描函数 将多余的路径过滤
		howToDo(allNum, listControlFlowChart);
	}
}
