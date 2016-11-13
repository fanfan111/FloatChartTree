package com.wxl;

import java.util.ArrayList;

public class SequentialBlock extends Sentence{
  private ArrayList<String> sentence;
 // private int nodeNumber;
  //public int nextNodeNumber;
	SequentialBlock(){
		this.sentence = new  ArrayList<String>();
		this.nodeNumber = -1;
		this.flag = true;
	}
	public void setNodeNum(int num){
		this.nodeNumber = num;
	}
	public void insert(String inString){
		this.sentence.add(inString);
	}
	public ArrayList<String> getSentence(){
		return sentence;
	}
	public int getNodeNumber(){
		return this.nodeNumber;
	}
	public String printSequentialBlock(){
		String reString = new String(); 
		reString = "";
		for(String i:sentence){
			reString +=i+"\n";
		}
		return reString;
		
	}
	
}
