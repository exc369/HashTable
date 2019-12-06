import java.io.*;
import java.util.Scanner;

public class WordCounter {

  //representation of the hashtable
  private LLNodeHash[] hashTable;
  //number of non empty hash slots
  private int usedIndex;
  //The total number of spaces in the array
  private int tableSize;
  //the ratio of the used hash slots to the total slots
  private double loadFactor;
  
  //helper method to instantiate the table
  public void instantiateTable(){
    hashTable = new LLNodeHash[16];
  }
  
  //helper method to calculate the open space in the hashtable
  public int space(){
    return (tableSize - usedIndex);
  }
  
  //helper method to add a Hash Node to the hashtable that contains the String word and int frequency
  public void add(LLNodeHash word){
    if (!checkWord(word)){
      put(word);
    }
  }
  
  //helper method to calculate and return the hash of the word
  public int hash(String w){
    return (Math.abs(w.hashCode()) % hashTable.length);
  }
  
  //helper method to check the load factor of the table
  public void checkLoadFactor(){
    usedIndex = 0;
    tableSize = hashTable.length;
    //for loop to check how many indices of the hashtable are being used
    for (int i = 0; i < tableSize; i++){
      if (hashTable[i] != null){
        usedIndex++; 
      }
    }
    loadFactor = ((double) usedIndex / (double) hashTable.length);
    //rehashes if the load factor is more than 0.75
    if (loadFactor > 0.75){
      rehash();
    }
  }
  
  //helper method to calculate the average length of collisions
  public double collisionLength(){
    //count to keep track of how many words there are the chain
    int count = 0;
    for (int i = 0;i< tableSize;i++){
      if (hashTable[i] != null){
        LLNodeHash ptr = hashTable[i];
        //increments if there is an element
        if (ptr != null){
          count++;
        }
        //while loop to traverse the linked list
        while (ptr.getNext() != null){
          //increments for every word
          count++;
          ptr = ptr.getNext();
        }
      }
    }
    return ((double) count / (double) usedIndex);
  }
  
  //helper method to see if the word is already in the hashtable
  public boolean checkWord(LLNodeHash word){
    //finds index of the word in the array
    int h = hash(word.getKey());
    LLNodeHash ptr = hashTable[h];
    //looks through linked list at that index and changes frequency of word if found
    while (ptr != null){
      if (ptr.getKey().equals(word.getKey())){
        ptr.setFreq(ptr.getFreq() + word.getFreq());
        return true;
      }
      ptr = ptr.getNext();
    }
    return false;
  }
  
  //adds a word to the Hashtable
  public void put(LLNodeHash word){
    int h = hash(word.getKey());
    if (hashTable[h] == null){
      hashTable[h] = new LLNodeHash(word.getKey(), word.getFreq(), null);
    }
    else{
      LLNodeHash ptr = hashTable[h];
      //while loop to traverse the linked list till the last node
      while (ptr.getNext() != null){
        ptr = ptr.getNext();
      }
      //sets the next node to the new inserted word
      ptr.setNext(new LLNodeHash(word.getKey(), 1, null));
    }
    //checks the load factor after each addition to the hashtable to see
    //if there is a need to rehash based on the load factor
    checkLoadFactor();
  }
  
  //helper method to rehash the hashtable
  public void rehash(){
    //creates a new temporary hashtable since arrays are immutable
    LLNodeHash[] temp = hashTable;
    //doubles the size of the current hashtable
    hashTable = new LLNodeHash[hashTable.length * 2];
    for (int i = 0; i < temp.length; i++){
      //if the index of the array has an element
      if (temp[i] != null){
        LLNodeHash ptr = temp[i];
        //traverse the linked list and adds each word back to the new hashtable incase they have a new position
        while (ptr.getNext() != null){
          LLNodeHash word = new LLNodeHash(ptr.getKey(), ptr.getFreq(), null);
          add(word);
          ptr = ptr.getNext();
        }
        //adds the last element to the new hashtable
        LLNodeHash word = new LLNodeHash(ptr.getKey(), ptr.getFreq(), null);
        add(word);
      }
    }
  }
  
  //helper method to print out the hashtable for testing purposes
  public void print(){
    for (int i = 0; i < hashTable.length; i++){
      if (hashTable[i] != null){
        LLNodeHash ptr = hashTable[i];
        while (ptr.getNext() != null){
          System.out.print("(" + ptr.getKey() + ", " + ptr.getFreq() + ")");
          ptr = ptr.getNext();
        }
        System.out.print("(" + ptr.getKey() + ", " + ptr.getFreq() + ")");
      }
    }
  }
  
  //method that reads the inputted file and inserts each word into the hashtable
  public void input(File inputFile){
    instantiateTable();
    try{
      String line = "";
      //method to scan certain words between 2 delimiting characters, usually blank lines or white spaces or tabs.
      Scanner read =  new Scanner(inputFile).useDelimiter("\\W+|\\n|\\r|\\t|, ");
      //while there is a next word, keeps reading the file 
      while (read.hasNext()){
        line = read.next().toLowerCase();
        LLNodeHash words = new LLNodeHash(line, 1, null);
        add(words);
      }
      read.close();
    }
    catch (FileNotFoundException e){
      System.out.print("Not found");
      e.printStackTrace();
    }
    System.out.println(hashTable.length);
    System.out.println(space());
    System.out.println(loadFactor);
    System.out.println(collisionLength());
    print();
  }
  
  //method to write a new output file that contains the words, frequencies and other information like the
  //load factor and average collision length
  public void output(File outputFile){
    try {
      BufferedWriter file = new BufferedWriter(new FileWriter(outputFile));
      file.write(hashTable.length + "\n");
      file.write(space() + "\n");
      file.write(loadFactor + "\n");
      file.write(collisionLength() + "\n");
      for (int i = 0; i < hashTable.length; i++){
        if (hashTable[i] != null){
          LLNodeHash ptr = hashTable[i];
          while (ptr.getNext() != null){
            file.write("(" + ptr.getKey() + "," + ptr.getFreq() + ") ");
            ptr = ptr.getNext();
          }
          file.write("(" + ptr.getKey() + "," + ptr.getFreq() + ") ");
        }
      }
      file.close();
    }
    catch (IOException e){
      System.out.println("Cannot create new file");
      e.printStackTrace();
    }
  }
  
  //method that starts the reading and writing file process
  public static void createHashTable(String inputFileName, String outputFileName){
    File inputFile = new File(inputFileName);
    File outputFile = new File(outputFileName);
    WordCounter h = new WordCounter();
    h.input(inputFile);
    h.output(outputFile);
  }
  
  public static void main(String[] args){
    createHashTable(args[0],args[1]);
    //createHashTable("toyInput.txt", "toyOutput.txt");
  }
}