package negate;

import java.util.ArrayList;
import java.util.HashMap;

public class Sentence {
	
	private ArrayList<String> rawSent;
	
	Sentence (ArrayList<String> inSent){
		this.rawSent =inSent;
	}
	
	public ArrayList<String> morphNegate(HashMap<String,String>  acceptMap,HashMap<String,String> discardMap) { 
		
		String token = null;
		ArrayList<String> annotatedSent = new ArrayList<String>();

		for (String candidate : this.rawSent){
			token = candidate.split("\t")[0];
		    if (acceptMap.containsKey(token)) {
		    	
		    	if (discardMap.containsKey(token)) {
		    		
		    		annotatedSent.add(candidate);
		    		continue;
		    		
		    	} else { 
		    		
		    		candidate += "\t";
		    		candidate += "morphneg";
		    		annotatedSent.add(candidate);
		    	}
		    	
		    } else {
		    	annotatedSent.add(candidate);
		    	continue;
		    
		    }
		    
		}

		return annotatedSent;
		
	}
	
	public ArrayList<String> sentNegate(ArrayList<String> rSentence) { 
		
		ArrayList<String> annotatedSent = new ArrayList<String>();
		String tag = null;
		
		for (String candidate : rSentence){

			tag = candidate.split("\t")[1];
			
			if (tag.equals("RB")){
				
				// I did this above in the Stanford Dependencies
				// How can I do it here?
				//candidate += "\tSNeg";
				
		    	annotatedSent.add(candidate);
		    	continue;
				
			} else {
				
		    	annotatedSent.add(candidate);
		    	continue;
			}
		}
		
		return annotatedSent;
	}
	
	public ArrayList<String> doubleNegate(ArrayList<String> rSentence) {
		
		ArrayList<String> annotatedSent = new ArrayList<String>();
		ArrayList<Integer> matchCandidate = new ArrayList<Integer>();
		ArrayList<Integer> matchWindow = new ArrayList<Integer>();

		int Negcount = 0;
		
		for (String candidate : rSentence){
			
			// get candidates who were tagged negative
			String[] mainList = candidate.split("\t");
			
			// if they were tagged negative
			if (mainList.length == 3){
				
				// get thier indeces
				matchCandidate.add(rSentence.indexOf(candidate));
				Negcount += 1;
			}
		}
		
		// go through the matched indeces, and measure their distance
		if (matchCandidate.size() > 1) {
			for (int i = 1; i < matchCandidate.size(); i++) {
				
				int left = matchCandidate.get(i);
				int right = matchCandidate.get(i-1);
				
				// if there are two matches in a 6 word window, add to window array
				if (right-left < 7){
					
					matchWindow.add(right);
					matchWindow.add(left);				
				}
			}
		} 
		
		// go through all tokens in sentence
		for (String candidate : rSentence){
				
			int index = rSentence.indexOf(candidate);
			
			// get candidates who were tagged negative
			String[] mainList = candidate.split("\t");
			
			// if they were tagged negative, and not yet tagged as Double
			if (mainList.length == 3){
				
				// if they are real matches, tag them as double
				if (matchWindow.contains(index)){
					
					candidate += "\tDoubleNeg";
					annotatedSent.add(candidate);
					
				} else {
					annotatedSent.add(candidate);
					continue;
				}
					
			} else {
				
				annotatedSent.add(candidate);
				
			}
			
		}
		
		return annotatedSent;
	}
	
}