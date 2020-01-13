package trie;

import java.util.ArrayList;

/**
 * 
 *
 */
public class Trie {
	
	// prevent instantiation
	private Trie() { }
	
	/**
	 * Builds a trie by inserting all words in the input array, one at a time,
	 * in sequence FROM FIRST TO LAST. (The sequence is IMPORTANT!)
	 * The words in the input array are all lower case.
	 * 
	 * @param allWords Input array of words (lowercase) to be inserted.
	 * @return Root of trie with all words inserted from the input array
	 */
	public static TrieNode buildTrie(String[] allWords) {
		TrieNode root=new TrieNode(null,null,null);
		
		if(allWords.length==0) {
			return root;
		}
		
		short start=0; 
		
		short end=(short)(allWords[0].length()-1); 
		
		Indexes firstword=new Indexes(0,start,end);
		root.firstChild=new TrieNode(firstword,null,null);
		
		TrieNode ptr=root.firstChild;
		
	
		TrieNode prev = ptr.firstChild;
		
		
		
		for(int i=1; i<allWords.length; i++) {
			String trieword=allWords[i];
			
			
			int sumupto=0;
			while(ptr!=null) {
			
				
				sumupto=simcheck(trieword.substring((int)ptr.substr.startIndex),allWords[ptr.substr.wordIndex].substring((int)ptr.substr.startIndex,ptr.substr.endIndex+1));
				
				
				if( sumupto==0 && trieword.charAt(ptr.substr.startIndex)!=allWords[ptr.substr.wordIndex].charAt(ptr.substr.startIndex)){
					prev=ptr;
					ptr=ptr.sibling;
				}else {
					
					String r2=trieword.substring((int)ptr.substr.startIndex);
							
					String d2=allWords[ptr.substr.wordIndex].substring((int)ptr.substr.startIndex,(int)ptr.substr.endIndex+1);
					if((int)ptr.substr.endIndex+1>r2.length()) {
						d2 = d2.substring(0,sumupto);
						r2 = r2.substring(0,sumupto);
					}
					else {
						r2 = trieword.substring((int)ptr.substr.startIndex,(int)ptr.substr.endIndex+1);
					}
					if(r2.equals(d2)) {
						if(ptr.firstChild==null) {
							prev = ptr;
							break;
						}
						prev=ptr.firstChild;
						ptr=ptr.firstChild;
					
						
					}else {
						prev=ptr;
						break;
					}
				}
				
				
			}
			
			if(ptr==null) {
				Indexes insert=new Indexes(i,prev.substr.startIndex,(short)(trieword.length()-1));
				prev.sibling=new TrieNode(insert,null,null);
			}else if(ptr!=null){
				TrieNode temp=prev.firstChild;
				
			
				
				Indexes tik=new Indexes(prev.substr.wordIndex,(short)(sumupto+ptr.substr.startIndex),prev.substr.endIndex);
				prev.substr.endIndex = (short)(sumupto+ptr.substr.startIndex-1);

				
				TrieNode ins1=new TrieNode(tik,null,null);
				
				prev.firstChild=ins1;
				
				prev.firstChild.firstChild=temp;
				
				Indexes ins2index= new Indexes((short)i,(short)(sumupto+ptr.substr.startIndex),(short)(trieword.length()-1));
				TrieNode ins2=new TrieNode(ins2index,null,null);
				prev.firstChild.sibling=ins2;
				
				
			}
			
		
			ptr=root.firstChild;
			prev=root.firstChild;
		
			
		}
	
		return root;
	}
	
	private static int simcheck(String one, String two) {
		int res=0;
		int n=0;
		if(one.length()>=two.length()) {
			n=two.length();
		}else {
			n=one.length();
		}
		
		
		for(int a=0; a<n; a++) {
			if(one.charAt(a)==two.charAt(a)) {
				res++;
			}else if(one.charAt(a)!=two.charAt(a)){
				break;
			}
		}
		
		return res;
		
	}
	
		
	
	
	
	/**
	 * Given a trie, returns the "completion list" for a prefix, i.e. all the leaf nodes in the 
	 * trie whose words start with this prefix. 
	 * For instance, if the trie had the words "bear", "bull", "stock", and "bell",
	 * the completion list for prefix "b" would be the leaf nodes that hold "bear", "bull", and "bell"; 
	 * for prefix "be", the completion would be the leaf nodes that hold "bear" and "bell", 
	 * and for prefix "bell", completion would be the leaf node that holds "bell". 
	 * (The last example shows that an input prefix can be an entire word.) 
	 * The order of returned leaf nodes DOES NOT MATTER. So, for prefix "be",
	 * the returned list of leaf nodes can be either hold [bear,bell] or [bell,bear].
	 *
	 * @param root Root of Trie that stores all words to search on for completion lists
	 * @param allWords Array of words that have been inserted into the trie
	 * @param prefix Prefix to be completed with words in trie
	 * @return List of all leaf nodes in trie that hold words that start with the prefix, 
	 * 			order of leaf nodes does not matter.
	 *         If there is no word in the tree that has this prefix, null is returned.
	 */
	public static ArrayList<TrieNode> completionList(TrieNode root,
										String[] allWords, String prefix) {
		
         ArrayList<TrieNode> targets=new ArrayList<>();
         
         TrieNode wordsearch=root;// begin at the root of the prefix tree
	
         while(wordsearch!=null) { //traverse the tree until you hit a leaf node
        	
        	 if(wordsearch.substr!=null) {
        	 if(allWords[wordsearch.substr.wordIndex].substring(0, (int)(wordsearch.substr.endIndex)+1).contains(prefix) || prefix.contains(allWords[wordsearch.substr.wordIndex].substring(0, (int)(wordsearch.substr.endIndex)+1))) { //check if the word at the place in tree starts with the prefix
        		 if(wordsearch.firstChild==null) { //you have found a full word can be a singel word or it can share a prefix with other words in ther tree
        		 targets.add(wordsearch); 
        		 wordsearch=wordsearch.sibling; //move to its sibling add it if there are common prefix's if there are not any then only add the word
        		 }else {
        			 TrieNode newtree=wordsearch.firstChild; //place holder tree;
        			ArrayList<TrieNode> pos2=completionList(newtree,allWords,prefix); //run the program again but this tim start with the childnode and repeat the same process and add the results into a temp array list
        			
        			for(int i=0; i<pos2.size(); i++) { 
        				targets.add(pos2.get(i));
        			}
        			
        			wordsearch=wordsearch.sibling; 
        		 }
       	 
        	 }else {
        		 wordsearch=wordsearch.sibling;
        	 }
         }else{
         	wordsearch=wordsearch.firstChild;
          }
         }
		
		
		return targets;
	}

	
	public static void print(TrieNode root, String[] allWords) {
		System.out.println("\nTRIE\n");
		print(root, 1, allWords);
	}
	
	private static void print(TrieNode root, int indent, String[] words) {
		if (root == null) {
			return;
		}
		for (int i=0; i < indent-1; i++) {
			System.out.print("    ");
		}
		
		if (root.substr != null) {
			String pre = words[root.substr.wordIndex]
							.substring(0, root.substr.endIndex+1);
			System.out.println("      " + pre);
		}
		
		for (int i=0; i < indent-1; i++) {
			System.out.print("    ");
		}
		System.out.print(" ---");
		if (root.substr == null) {
			System.out.println("root");
		} else {
			System.out.println(root.substr);
		}
		
		for (TrieNode ptr=root.firstChild; ptr != null; ptr=ptr.sibling) {
			for (int i=0; i < indent-1; i++) {
				System.out.print("    ");
			}
			System.out.println("     |");
			print(ptr, indent+1, words);
		}
	}
 }
