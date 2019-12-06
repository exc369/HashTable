/**
 * The node of a linked list
 * Some methods were given by Professor Connamacher in EECS132, others were written for the HashTable project
 * @author Eric Chang
 */
public class LLNodeHash {
  /** the element stored in the node */
  private String word;
  private int frequency;
  
  /** a reference to the next node of the list */
  private LLNodeHash next;
  
  /**
   * the constructor
   * @param w the string to store in the node
   * @param f the int frequency to store in the node
   * @param next  a reference to the next node of the list 
   */
  public LLNodeHash(String w, int f, LLNodeHash next) {
    this.word = w;
    this.frequency = f;
    this.next = next;
  }
  
  /**
   * Returns the word stored in the node
   * @return the word stored in the node
   */
  public String getKey() {
    return this.word;
  }
  
  //returns the frquency of the word
  public int getFreq() {
    return this.frequency;
  }
  
  //sets the frequency to an inputted value
  public void setFreq(int f) {
    this.frequency = f;
  }
  
  /**
   * Returns the next node of the list
   * @return the next node of the list
   */
  public LLNodeHash getNext() {
    return this.next;
  }
  
  /**
   * Changes the node that comes after this node in the list
   * @param next  the node that should come after this node in the list.  It can be null.
   */
  public void setNext(LLNodeHash next) {
    this.next = next;
  }
}
