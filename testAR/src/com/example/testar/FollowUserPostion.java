package com.example.testar;

import java.util.ArrayList;

public class FollowUserPostion {
	private ArrayList<Tree> surroundingTrees = new ArrayList<Tree>();;
	
	private FollowUserPostion(){

	}
	
	public static void startFollowing(DisplayTreeListener listener){
		//TODO get user position
		//TODO get trees corresponding to user position
		//TODO update trees when user moves
		
		//TEST
		Tree tree1 = new Tree(45.778339,4.874631, 0, "Rue de la doua");
		Tree tree2 = new Tree(45.781573, 4.872156, 1, "Gaston Berger");
		Tree tree3 = new Tree(45.781856, 4.870511, 2, "BU");
		Tree tree4 = new Tree(45.781035, 4.873657, 3, "Beurk");
		
		listener.addTree(tree1);
		listener.addTree(tree2);
		listener.addTree(tree3);
		listener.addTree(tree4);
	}
	
	public static void stopFollowing(){
		
	}
	
	
}
