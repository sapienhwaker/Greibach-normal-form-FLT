import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Scanner;
import java.util.Stack;
import java.util.TreeMap;

/**
* This program simplifies the given context free grammar.
* It removes unproductive variables, unreachable variables, nullables symbols,
* unit productions and left recursion
*
* @author  Prasad Hajare
* @course  CS5313
* @since   2020-01-11
* 
*  List of functions:
*  1. Main
*  2. removeUnproductive
*  3. removeUnreachable
*  4. removeNullabels
*  5. removeUnitProductions
*  6. removeLeftRecursion
*  7. WriteToFile
*/
public class GNF {
	static TreeMap<String, List<List>> map = new TreeMap();
	static List<String> nonterminals = new LinkedList();
	
	
	/**
	   * This is the main method which provides the access to menu items.
	   * By selecting one of the choices from the menu you can perform
	   * simplification on the grammar
	   * @param args Unused.
	   * @return Nothing.
	   * @exception InterruptedException On keyboard interrupt at the wait time.
	   * @see InterruptException
	   */
    @SuppressWarnings("resource")
	public static void main(String[] args) throws InterruptedException {
    	String start_symbol = null;
    	String output_file_name = null;
    	boolean flag = true;
    	
        try {
        	System.out.println("Please give (space seperated) input and output file name");
        	Scanner input = new Scanner(System.in);
            String[] input_files = input.nextLine().split("\\s+");
            if(input_files.length < 2) {
            	System.out.println("Only 2 file names are required");
            	System.out.println("Exiting the program...");
            	System.exit(0);
            }else if(input_files.length > 2) {
            	System.out.println("Only 2 file names will be considered for input and output");
            }
            String input_file_name = input_files[0];
            File file = new File(input_file_name);
            output_file_name = input_files[1];
            //File file = new File("/home/sapienhwaker/eclipse-workspace/CFG/test");
            input = new Scanner(file);
            
            while (input.hasNextLine()) {
                String line = input.nextLine();
                String[] array = line.split("::=");
                String key = array[0].trim();
                if(!key.contains("<")) {
                	System.out.println("Non_terminals on the left hand side of the production rule must be surrounded by < >");
                	System.out.println("Exiting code...");
                	System.exit(0);
                }
                nonterminals.add(key);
                if (flag == true) {
                	start_symbol = key;
                	flag = false;
            	}
                
                String[] array1 = array[1].trim().split("\\s+");
                
                List<String> list = new LinkedList<>();
                for(String s: array1) {
                	list.add(s.trim());
                }
                
                if(map.containsKey(key)) {
                	List<List> value = map.get(key);
                	value.add(list);
                	map.put(key, value);
                }else {
                	List<List> value = new LinkedList<>();
                	value.add(list);
                	map.put(key, value);
                }
                //System.out.println(line);
            }
            input.close();

        } catch (Exception ex) {
            //ex.printStackTrace();
        	System.out.println("Expected input file format is not provided");
        	System.out.println("# Please provide the file with below format");
        	System.out.println("-------------------------------------------");
        	System.out.println("1. Left and right side of the production rule should be seperated by ::=");
        	System.out.println("2. All non_terminals must be surrounded by < >");
        	System.out.println("3. Each line should have only one production rule");
        	System.out.println("4. *Please enter at least two spaces after ::= for null production rules");
        	System.out.println("Exiting code.....");
        	System.exit(0);
        }
        
        for(Map.Entry<String, List<List>> entry: map.entrySet()) {
        	String key = entry.getKey();
        	List<List> value = entry.getValue();
        	System.out.print(key + " -> ");
        	for(List li: value) {
        		System.out.print(li);
        	}
        	System.out.println();
        }
        
        
        while(true) {
        	System.out.println();
            System.out.println("********************************************");
            System.out.println();
            
            System.out.println("You can perform following operations to simplify the grammar :");
            System.out.println("1. Remove Unproductive Symbols");
            System.out.println("2. Remove Unreachable Symbols");
            System.out.println("3. Remove Null Productions");
            System.out.println("4. Remove Unit Productions");
            System.out.println("5. Eliminate the immediate left recursion");
            System.out.println("6. Dubug Log: Print current state of the datastructure");
            System.out.println("7. Write output to the file");
            System.out.println("8. EXIT");
            
            System.out.println("Select one of the above options:");
            Scanner scan = new Scanner(System.in);
            int choice = scan.nextInt();
            
            switch(choice) {
            case 1:
            	System.out.println("Removing unproductive symbols");
            	Thread.sleep(1000);
            	removeUnproductive(nonterminals.get(0), true);
            	break;
            case 2:
            	System.out.println("Removing unreachable symbols");
            	Thread.sleep(1000);
            	String pass = nonterminals.get(0);
            	for(String s: nonterminals) {
            		if(map.containsKey(s)) {
            			pass = s;
            			break;
            		}
            	}
            	removeUnreachable(pass);
            	break;
            case 3:
            	System.out.println("Removing nullable symbols");
            	Thread.sleep(1000);
            	removeNullables();
            	break;
            case 4:
            	System.out.println("Removing unit productions");
            	Thread.sleep(1000);
            	removeUnitProductions();
            	break;
            case 5:
            	System.out.println("Removing left recursions");
            	Thread.sleep(1000);
            	removeLeftRecursion();
            	break;
            case 6:
            	System.out.println("Printing current state of HashMap");
            	Thread.sleep(1000);
            	for(Map.Entry<String, List<List>> entry: map.entrySet()) {
                	String key = entry.getKey();
                	List<List> value = entry.getValue();
                	System.out.print(key + " -> ");
                	for(List li: value) {
                		System.out.print(li);
                	}
                	System.out.println();
                }
            	break;
            case 7:
            	System.out.println("Write ouptut to the file");
            	Thread.sleep(1000);
            	writeToFile(output_file_name);
            	break;
            case 8:
            	System.exit(0);
            default:
            	System.out.println("INVALID INPUT... Please re-enter again");
            	break;
            }
        }
    }
    
    /**
	   * This method removes unproductive symbols from the grammar.
	   * By selecting one of the choices from the menu you can perform
	   * simplification on the grammar
	   * @param String starting non_terminal, boolean isNonterminal
	   * @return Nothing.
	   * 
	   * Implementation details:
	   * It calls isProductiveList it returns
	   * if the given production rule is productive or not
	   * 
	   * If it is not productive then it calls deleteUnproductiveSymbol
	   * by passing the argument as the string or non-terminal is not productive
	   */
    public static void removeUnproductive(String start, boolean check){
    	Stack<String> stack = new Stack<>();
    	stack.push(start);
    	while(!stack.isEmpty()) {
    		
    		String target = stack.pop();
    		List<List> values = map.get(target);
    		int size = values.size();
    		
    		Iterator it = values.iterator();
    		
    		while(it.hasNext()) {
    			List<String> checkList = (List<String>) it.next();
    			if (checkList.contains(target) && size == 1) {
    				check = false;
    				break;
    			}else if(checkList.contains(target) && size > 1) {
    				checkList = (List<String>) it.next();
    			}
    			check &= isProductiveList(checkList, check);
    		}
    		
    		if(!check) {
    			deleteUnproductiveSymbol(target);
        	}
    	}    	
    }
    
    /**
	   * This is supportive method for removeUnproductive symbols
	   * This method returns a boolean value for any production rule on right hand side
	   * It returns true if given rule lead to some terminal symbol else returns false
	   * @param List, boolean check
	   * @return boolean
	   * 
	   * Implementation details:
	   * It calls back removeUnproductive method again with new symbol
	   * and check if that is a productive or not.
	   * It calls isTerminal method to check any symbol if it is terminal or not
	   */
    public static boolean isProductiveList(List<String> list, boolean check) {
    	Queue<String> q = new LinkedList<>(list);
    	int index = 0;
    	
    	while(!q.isEmpty()) {
    		String var = (String)q.poll();
    		if(isTerminal(var)) {
    			return true;
    		}else {
    			removeUnproductive(var, check);
    		}
    		index++;
    	}
    	return check;
    }
    
    /**
	   * This is supportive method for removeUnproductive symbols
	   * After identifying the unproductive symbols this methods eliminates
	   * those symbols and their corresponding associations from the grammar
	   * @param String non_terminal
	   * @return Nothing
	   */
    public static void deleteUnproductiveSymbol(String symbol) {
    	map.remove(symbol);
    	
    	for(Map.Entry<String, List<List>> entry: map.entrySet()) {
    		String key = entry.getKey();
    		List<List> value = entry.getValue();
    		int index = 0;
    		List<Integer> tobeDeleted = new LinkedList(); 
    		for(List<String> list: value) {
    			if(list.contains(symbol)) {
    				value.removeAll(list);
    				tobeDeleted.add(index);
    			}
    			index++;
    		}
    		for(int i: tobeDeleted) {
    			value.remove(i);
    		}
    	}
    }
    
    //Implementation of unproductive symbols end here
    
    //Function to identify if the given string/variable is terminal or not
    
    /**
	   * This method returns if the given symbol is terminal or not
	   * @param String symbol
	   * @return boolean
	   */
    public static boolean isTerminal(String s) {
    	return !(s.contains("<") && s.contains(">"));
    }
    
    //Implementation of unreachable symbols here
    
    /**
	   * This method removes the unreachable symbols
	   * @param String start_symbol
	   * @return Nothing
	   * 
	   * Implementation details:
	   * It calls back dfs function to find the variables which are reachable
	   * Then those are which non_terminals are un
	   */
    public static void removeUnreachable(String start) {
    	HashSet<String> set = new HashSet<>();
    	
    	HashSet<String> master = new HashSet<>();
    	
    	for(Map.Entry<String, List<List>> entry: map.entrySet()) {
    		String k = entry.getKey();
    		master.add(k);
    	}
    	HashSet<String> output = dfs(start, map, set);
    	Iterator it = output.iterator();
    	
    	while(it.hasNext()) {
    		String s = (String) it.next();
    		master.remove(s);
    	}
    	
    	Iterator it1 = master.iterator();
    	
    	while(it1.hasNext()) {
    		String deleteThis = (String)it1.next();
    		map.remove(deleteThis);
    	}
    }
    
    /**
	   * This is supportive method for remove unreachable symbols
	   * This method returns a set of reachable symbols
	   * @param Starting symbol, TreeMap, set
	   * @return set
	   * 
	   * Implementation details:
	   * It recursively calls itself and identifies all reachable variables
	   */
    public static HashSet<String> dfs(String start, TreeMap<String, List<List>> map, HashSet<String> set){
    	if(set.contains(start))
    		return set;
    	set.add(start);
    	
    	List<List> values = map.get(start);
    	
    	for(List<String> list: values) {
    		for(String s: list) {
    			if(!isTerminal(s)) {
    				dfs(s, map, set);
    			}
    		}
    	}
    	return set;
    }
    //Implementation for unreachable symbols ends here
    
    /**
	   * This method removes all nullables from the given grammar
	   * @param 
	   * @return
	   * 
	   * Implementation details:
	   * It calls getNulls and collects all initial nullable variables
	   * and check if that is a productive or not.
	   * It calls isTerminal method to check any symbol if it is terminal or not
	   */
    public static void removeNullables() {
    	boolean presentE = true;
    	while(presentE) {
    		HashSet<String> set = getNulls();
    		if(set.isEmpty() || set.size() == 0) {
    			presentE = false;
    		}
    		
    		Iterator it = set.iterator();
    		while(it.hasNext()) {
    			String target = (String)it.next();
    			
    			for(Map.Entry<String, List<List>> entry: map.entrySet()) {
    				String key = entry.getKey();
    				List<List> values = entry.getValue();
    				
    				List<String> tempList = new LinkedList();
    				for(List<String> list: values) {
    					
    					if(list.contains(target)) {
    						for(String s: list) {
    							if(s.equals(target))
    								continue;
    							tempList.add(s);
    						}
    						
    					}
    					
    				}
    				
    				if(!tempList.isEmpty() || tempList.size() > 1)
    					values.add(tempList);
    				
    				int index = 0;
    				List<Integer> removeEmptyLists = new LinkedList<>();
    				for(List<String> list: values) {
    					if(list.size() == 1) {
    						if(list.get(0).equals("") || list.get(0).equals(" "))
    							removeEmptyLists.add(index);
    					}
    					index++;
    				}
    				
    				for(int i: removeEmptyLists) {
    					values.remove(i);
    				}
    			}
    			
    			List<String> removeEntry = new LinkedList<>();
    			for(Map.Entry<String, List<List>> entry: map.entrySet()) {
    				String key = entry.getKey();
    				List<List> values = entry.getValue();
    				
    				if(values.size() == 0) {
    					removeEntry.add(key);
    				}else if(values.size() == 1) {
    					if(values.get(0).size() == 0) {
    						removeEntry.add(key);
    					}else if(values.get(0).size() == 1 && (values.get(0).get(0).equals("") || values.get(0).get(0).equals("")))
    						removeEntry.add(key);
    				}
    			}
    			
    			for(String non_terminal: removeEntry) {
    				map.remove(non_terminal);
    			}
    		}
    	}
    }
    
    /**
	   * This is supportive method for removeNullable symbols
	   * This method returns a set of all non-terminals which are nullables
	   * It returns true if given rule lead to some terminal symbol else returns false
	   * @param
	   * @return Set
	   */
    public static HashSet<String> getNulls(){
    	HashSet<String> nullables = new HashSet<>();
    	
    	for(Map.Entry<String, List<List>> entry: map.entrySet()) {
			String key = entry.getKey();
			List<List> values = entry.getValue();
			
			for(List<String> list:values) {
				for(String s: list) {
					if(list.size() <= 1 && (s.equals(" ") || s.equals(""))) {
						nullables.add(key);
					}
				}
			}
		}
    	
    	return nullables;
    }
    
    /**
	   * This is supportive method for removeNullable symbols
	   * This method removes all the null productions from the all the non-terminals
	   * @param
	   * @return Set
	   */
    public static void removeEmptyList() {
    	for(Map.Entry<String, List<List>> entry: map.entrySet()) {
			String key = entry.getKey();
			List<List> values = entry.getValue();
			
			LinkedList<Integer> emptyListIndices = new LinkedList<>();
			int index = 0;
			for(List<String> list: values) {
				if(list.contains(" ") || list.contains("")) {
					emptyListIndices.add(index);
				}
				index++;
			}
			
			for(int i: emptyListIndices) {
				values.remove(i);
			}
			
			map.put(key, values);
		}
    	
    	removeEmptyProductions();
    }
    
    /**
	   * This is supportive method for removeNullable symbols
	   * This method removes all the non terminals which has only null production
	   * in their production rule
	   * @param
	   * @return Set
	   */
    public static void removeEmptyProductions() {
    	LinkedList<String> removeProductions = new LinkedList<>();
    	for(Map.Entry<String, List<List>> entry: map.entrySet()) {
			String key = entry.getKey();
			List<List> values = entry.getValue();
			
			if(values.size() == 0) {
				removeProductions.add(key);
			}
		}
    	
    	Iterator it = removeProductions.iterator();
    	
    	while(it.hasNext()) {
    		String s = (String)it.next();
    		map.remove(s);
    	}
    }
    //Implementation of nullables ends here
    
    /**
	   * This method removes all unit productions
	   * @param
	   * @return
	   * 
	   * Implementation details:
	   * It first identifies and collects all unit productions
	   * Then identifies appropriate terminal for that particular non_terminal
	   * and replaces that non_terminal with it's terminal value*/
    public static void removeUnitProductions() {
    	HashMap<String, List<List>> dummy = new HashMap<>();
    	List<List> value_list;
    	
    	for(String non_terminal: nonterminals) {//[S, X, Y, Z, M, N]
    		List<List> values = map.get(non_terminal);
    		value_list = new LinkedList<>();
    		for(List<String> list: values) {
    			if(list.size() == 1) {
    				List<String> li = new LinkedList<>();
    				li.add(list.get(0));
    				value_list.add(li);
    			}
    		}
    		if(value_list.size() != 0)
    			dummy.put(non_terminal, value_list);
    	}
    	
    	List<String> terminals_output = new LinkedList<>();
    	for(String non_terminal: nonterminals) {
    		if(dummy.containsKey(non_terminal)) {
    			System.out.println("Finding terminal for: " + non_terminal);
    			String terminal = getDfsTerminal(dummy, non_terminal);
    			System.out.println("Found Terminal: " + terminal);
    			terminals_output.add(non_terminal + " " + terminal);
    		}
    	}
    	List<Integer> removeUnitList;
    	for(String s: terminals_output) {
    		String array[] = s.split(" ");
    		removeUnitList = new LinkedList<>();
    		List<List> values = map.get(array[0]);
    		int index = 0;
    		for(List<String> list: values) {
    			if(list.size() == 1) {
    				removeUnitList.add(index);
    			}
    			index++;
    		}
    		for(int i: removeUnitList) {
    			values.remove(i);
    		}
    		
    		List<String> add_terminal_list = new LinkedList<>();
    		add_terminal_list.add(array[1]);
    		values.add(add_terminal_list);
    		map.put(array[0], values);
    	}
    }
    
    /**
	   * This is supportive method for removeUnitProductions
	   * It returns a list of non-terminal and its corresponding terminal symbol
	   * by computing it recursively
	   * @param HashMap, String
	   * @return String
	   * 
	   * Implementation details:
	   * It computes the terminal value for non-terminal by using dfs approach
	   */
    public static String getDfsTerminal(HashMap<String, List<List>> dummy, String non_terminal) {
		if(isTerminal(non_terminal))
			return non_terminal;
		List<List> list = dummy.get(non_terminal);
		
		for(List<String> li: list) {
			for(String s: li) {
				non_terminal = getDfsTerminal(dummy, s);
			}
		}
    	return non_terminal;
    }
    
    
    /**
	   * This method removes the left recursion from the given grammar
	   * @param
	   * @return
	   * 
	   * Implementation details:
	   * It calls getEligibleValues method to get all non_terminals
	   * which has left recursion present in their production
	   * Then it computes all alpha and beta values.
	   * Finally it creates a new symbol alpha1 and associate alpha values with that
	   * and inserts into the table 
	   */
    public static void removeLeftRecursion() {
    	List<String> eligible_values = getEligibleValues();
    	Iterator it = eligible_values.iterator();
    	
    	List<String> alphas;
    	List<String> betas;
    	
    	while(it.hasNext()) {
    		String s = (String) it.next();
    		alphas = getAlphas(s);
    		betas = getBetas(s);
    		
    		map.remove(s);
    		List<List> listfora = new LinkedList<>();
    		List<List> listfora1 = new LinkedList<>();
    		
    		listfora.add(betas);
    		listfora1.add(alphas);
    		String a1temp = s.substring(1, s.length()-1)+1;
    		String a1 = "<"+a1temp+">";
    		List<String> appendalphas = new LinkedList<>();
    		
    		for(String al: alphas) {
    			List<String> templist = new LinkedList<>();
    			templist.add(al);
    			templist.add(a1);
    			listfora1.add(templist);
    		}
    		
    		for(String b: betas) {
    			List<String> templist = new LinkedList<>();
    			templist.add(b);
    			templist.add(a1);
    			listfora.add(templist);
    		}
    		
    		map.put(s, listfora);
    		map.put(a1, listfora1);
    	}
    }
    
    /**
	   * This is supportive method for removeleftrecursion symbols
	   * This method returns all possible alpha values from the given production
	   * provided it is eligible for left recursion
	   * @param String non_terminal
	   * @return List
	   * 
	   * Implementation details:
	   * It considers right side of the production as alpha
	   * if the leftmost symbol is same as that of the left side non terminal
	   */
    public static List<String> getAlphas(String key){
    	List<List> values = map.get(key);
    	List<String> alphas = new LinkedList<>();
    	
    	for(List<String> list: values) {
    		if(list.get(0).equals(key)) {
    			StringBuilder sb = new StringBuilder();
    			for(int i = 1; i < list.size(); i++) {
    				sb.append(list.get(i));
    			}
    			alphas.add(sb.toString());
    		}
    	}
    	return alphas;
    }
    
    /**
	   * This is supportive method for removeleftrecursion symbols
	   * This method returns all possible beta values from the given production
	   * provided it is eligible for left recursion
	   * @param String non_terminal
	   * @return List
	   * 
	   * Implementation details:
	   * It considers right side of the production as beta
	   * if the leftmost symbol is not the left side non terminal
	   */
    public static List<String> getBetas(String key){
    	List<List> values = map.get(key);
    	List<String> alphas = new LinkedList<>();
    	
    	for(List<String> list: values) {
    		if(!list.get(0).equals(key)) {
    			StringBuilder sb = new StringBuilder();
    			for(int i = 0; i < list.size(); i++) {
    				sb.append(list.get(i));
    			}
    			alphas.add(sb.toString());
    		}
    	}
    	return alphas;
    }
    
    /**
	   * This is supportive method for removeleftrecursion symbols
	   * This method returns a list of all non_terminals which has left recursion present in their production
	   * 
	   * @param
	   * @return List
	   * 
	   * Implementation details:
	   * If it finds the left most symbol in right hand side as the left side non_terminal it adds
	   * that symbol to the list
	   */
    public static List<String> getEligibleValues(){
    	List<String> eligibleList = new LinkedList<>();
    	
    	for(Map.Entry<String, List<List>> entry: map.entrySet()) {
			String key = entry.getKey();
			List<List> values = entry.getValue();
			
			for(List<String> list: values) {
				if(list.get(0).equals(key)) {
					eligibleList.add(key);
					break;
				}
			}
		}
    	
    	return eligibleList;
    }
    //Implementation of left recursion ends here
    
    /**
	   * This method writes the current state of the datastructure(Hashtable) into the text file
	   * @param 
	   * @return 
	   */
    public static void writeToFile(String fileName) {
    	try {
    	      FileWriter myWriter = new FileWriter(fileName);
    	      for(Map.Entry<String, List<List>> entry: map.entrySet()) {
    	    	  String key = entry.getKey();
    	    	  List<List> values = entry.getValue();
    	    	  
    	    	  for(List<String> list: values) {
    	    		  StringBuilder line = new StringBuilder();
    	    		  line.append(key + " " + "::=" + " ");
    	    		  for(String s: list) {
    	    			  line.append(s + " ");
    	    		  }
    	    		  myWriter.write(line.toString() + "\n");  
    	    	  }
    	    	  
    	      }
    	      myWriter.close();
    	      System.out.println("Successfully wrote to the file.");
    	    } catch (IOException e) {
    	      System.out.println("An error occurred.");
    	      e.printStackTrace();
    	    }
    }
}
