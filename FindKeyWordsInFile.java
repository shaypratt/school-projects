// Shaylan Pratt 2210 Assignment 3
// this program is a simple java keyword extraction program from a text file

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.PriorityQueue;
import java.util.*;


public class FindKeyWordsInFile {

    //this function reads words from a file and stores the word are the frequency of the word in an AVL Tree
    public static AVLTree<String, Integer> computeWordFrequencies(String file, AVLTree<String, Integer> tree) throws IOException{
        BufferedReader reader;
        reader = new BufferedReader(new FileReader(file));
        String line;
        while((line=reader.readLine())!=null){    // reads each line in the file 
            line = line.replaceAll("\\p{Punct}", "").toLowerCase();  // take out the punctuation and convert to lowercase 
            String[] words = line.split(" ");  // split the line into the individual words and put into an array 
            for(int i=0; i<words.length; i++){
                if(tree.get(words[i])==null){      // if the word isn't already in the tree, insert it and set the value to 1
                    tree.put(words[i], 1);                                   
                }
                else{
                    int value = tree.get(words[i]);  // if the word is already in the tree, increase the value by 1
                    tree.change(words[i], value+1);

                }
            }
        }  
        return tree;
    }

    //this function puts k most frequent words into a priority queue
    public static PriorityQueue<AVLTree<String,Integer>.Node> findMostFrequentWords(AVLTree<String, Integer> tree, int k){
        PriorityQueue<AVLTree<String,Integer>.Node> pq = new PriorityQueue<AVLTree<String,Integer>.Node>(new NodeCompare());  // a priority queue with custom comparator that sorts the nodes based on their values 
        tree.traverse_insert(pq); // insert the nodes into the priority queue
        PriorityQueue<AVLTree<String,Integer>.Node> pq2 = new PriorityQueue<AVLTree<String,Integer>.Node>(new NodeCompare());
        for(int i=0; i<k; i++){    // make the priority queue k size
            AVLTree<String,Integer>.Node node = pq.poll();
            pq2.offer(node);
        }

        return pq2;
    }

    // this function filters the common english words out 
    public static void filterCommonEnglishWords(String file, PriorityQueue<AVLTree<String,Integer>.Node> pq, AVLTree<String, Void> tree2, AVLTree<String, Integer> tree3) throws IOException{
        BufferedReader reader2;
        reader2 = new BufferedReader(new FileReader(file));  // read through the common english words and add them to a tree
        String line2;
        while((line2=reader2.readLine())!=null){
            line2=line2.toLowerCase();
            tree2.put(line2,null);
        }  

        while (!pq.isEmpty()) {      // if a word in the priority queue is not a common english word, it is a key word
            AVLTree<String,Integer>.Node node = pq.poll();  // the words in the priority queue 
            if(tree2.find(node.getKey())==null){            // compare to the common english words
                tree3.put(node.getKey(), node.getValue());  // put the keyword in a new tree
            }
        }

        tree3.inOrderTraversal();
    }

    
    public static void main(String[] args) {
        if (args.length != 3) {
            System.err.println("Usage: java FindKeyWordsInFile k file.txt MostFrequentEnglishWords.txt");
            System.exit(1);
        }

        int k = Integer.parseInt(args[0]);
        String inputFileName = args[1];
        String englishWordsFileName = args[2];
        
        AVLTree<String, Integer> wordFrequencies = new AVLTree<>();
        AVLTree<String, Void> englishWords = new AVLTree<>();
        AVLTree<String, Integer> keywordFrequencies = new AVLTree<>();
        
        try {
            //Part 1
            // function name => computeWordFrequencies
           computeWordFrequencies(inputFileName, wordFrequencies);


            //Part 2
            // function name => findKMostFrequentWords
            PriorityQueue<AVLTree<String,Integer>.Node> pq = findMostFrequentWords(wordFrequencies,k);
        
            
            //Part 3
            // function name => filterCommonEnglishWords
            filterCommonEnglishWords(englishWordsFileName, pq, englishWords, keywordFrequencies);


            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}