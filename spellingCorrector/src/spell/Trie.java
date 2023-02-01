package spell;


import java.io.OptionalDataException;

public class Trie implements ITrie{
    private final Node root;//top of the map
    private int wordCount; //number of words
    private int nodeCount; //number of letters + root
    private int indexHash; //position

    //constructor
    public Trie(){
        root = new Node();
        wordCount = 0;
        nodeCount = 1; //even when there is an emptry tree, there is 1 node, the *
        indexHash = 1; //^ is 0
    }

    @Override
    public void add(String word) {
        //tools
        char letter; //to keep in track of each character from the inputted word
        int index; //ASCII
        //start at the root
        Node currNode = root; //remember, a node is a letter in the tree!

        //convert to lower case bc of case insensitivity
        word = word.toLowerCase();

        //iterate through each CHARACTER of the word
        for (int i = 0; i < word.length(); i++) {
            //Get the index of the character in the children array of the current node.
            letter = word.charAt(i);
            index = letter - 'a';

            // If the child at that index is null, create a new node
            // and increment the "nodeCount" variable.
            if (currNode.getChildren()[index] == null) {
                currNode.getChildren()[index] = new Node();
                nodeCount++;
            }

            // If this is the last character of the word,
            // increment the value of the child node
            // and if the value was previously 0,
            // increment the "wordCount" variable.
            if (i == word.length() - 1) {
                if (currNode.getChildren()[index].getValue() == 0) { wordCount++; }
                currNode.getChildren()[index].incrementValue();
            }

            //Set the current node to the child node.
            currNode = (Node) currNode.getChildren()[index];
        }
    }

    @Override
    public INode find(String word) {
        char letter;
        int index = 0;
        Node currNode = root;

        //convert to lower case for case insensitivity
        word = word.toLowerCase();

        //search the tree
        //For each character, it checks if the character exists in the Trie as a child of the current node.
        // If it does, it sets the current node to that child and continues to the next character.
        // If the character does not exist as a child, it returns null, indicating that the word is not present in the trie.

        //Iterate through each CHARACTER of the word
        for (int i = 0; i < word.length(); i++) {
            letter = word.charAt(i);
            //Get the index of the character in the children array of the current node.
            index = letter - 'a';
            Node child = (Node) currNode.getChildren()[index];

            //If the child at that index is null, return null as the word is not present in the trie.
            if (child == null) return null;

            // If this is not the last character of the word, set the current node to the child node.
            if (i < word.length() - 1) currNode = child;
        }

        //check the last child
        //Check if the last child node has a value greater than 0,
        // if so return that child node, present in trie.
        // If not return null as the word is NOT present in the trie.
        if (currNode.getChildren()[index].getValue() > 0) {
            return currNode.getChildren()[index];
        }

        return null;
    }

    @Override
    public int getWordCount() {
        //TODO: Returns the number of unique words in the trie.
        return wordCount;
    }

    @Override
    public int getNodeCount() {
        //TODO: Returns the number of nodes in the trie.
        return nodeCount;
    }

    @Override
    public String toString() {
        //Remember, a StringBuilder is a class that is like String but is mutable! (has append, delete, etc!)
        StringBuilder word = new StringBuilder();
        StringBuilder wordList = new StringBuilder();

        return toStringHelper(root, word, wordList);
    }

    public String toStringHelper(INode currNode, StringBuilder word, StringBuilder wordList) {
        //currNode is the current node in the trie
        //word: a StringBuilder object that stores the current word
        //wordList represents a StringBuilder object that stores the list of words found in the trie.

        char letter;
        //iterate through the currNode and its children
        for (int i = 0; i < currNode.getChildren().length; i++) {
            INode child = currNode.getChildren()[i];
            //if the first character is not valid, go to the next character
            if (child != null) {
                letter = 'a';
                letter += i; //we want the next corresponding letter if it is a null
                word.append(letter);
                //if its a valid word!
                if (currNode.getChildren()[i].getValue() > 0) {
                    wordList.append(word.toString());
                    wordList.append("\n");
                }
                //called recursively!
                toStringHelper(child, word, wordList);
            }
        }
        if (word.length() > 1) { word.deleteCharAt(word.length() - 1); }
        else { word.setLength(0);}

        return wordList.toString(); //returns the word list in string form
    }


    @Override
    public int hashCode(){
        //TODO: Returns the hashcode of this trie.
        //REMEMBER: this has to be made here, not in any other function
        // write for loop
        for(int i=0;i<26;i++) {
            //assign the child as root
            //if the child[i] != null
            if (root.children[i]!=null){
                return (nodeCount * wordCount +  i);
            }
        }
        return 0;
    }

    @Override
    //we need to override because by default it will compare the addresses if (this == obj) {...}
    public boolean equals(Object o){
        //TODO: Checks if an object is equal to this trie
        //case 1: check if incoming object is null
        if (o == null) {
            return false; //"if hes null and im not", FALSE
        }
        //case 2: if equal to myself
        if (o == this){
            return true; //we are all equal to ourselves
        }
        //case 3: is it the same class/type as o?
        if (o.getClass() != this.getClass()) { //we can use .equals or ==
            return false; //they cannot be equal if they have different classes
        }
        if (!(o instanceof Trie)) return false;

        //now we know it is a valid object!
        //the objects are only equal if they have the same root, wordCount and nodeCount
        Trie temp = (Trie) o; //cast to fix error below

        if (temp.getWordCount() != this.wordCount) {
            return false;
        }
        if (temp.getNodeCount() != this.nodeCount) {
            return false;
        }

        return equalsHelper(this.root, temp.root);

    }
    public boolean equalsHelper(INode node1, INode node2) {

        //null conditions
        //if both are null, return true
        if(node1 == null && node2 == null) return true;
        //if 1 is null and the other isnt, return false
        if(node1 == null && node2  != null) return false;
        if(node1 != null && node2  == null) return false;

        //if the values (frequencies) are not equal, return false
        if(node1.getValue() != node2.getValue()) return false;

        //comparing each individually node (basically the structure of node1 and node2)
        for (int i = 0; i < node1.getChildren().length; i++) {
            INode child1 = node1.getChildren()[i];
            INode child2 = node2.getChildren()[i];
            //if they arent null, recurse the function to see if it is equal! (will skip over the null coniditions stated above^^)
            if (child1 != null && child2 != null) {
                //if it isnt equal, return false
                if (!equalsHelper(child1, child2)) return false; //recursive
                //double check, if either 1 or 2 are null, and the other isnt, return false
                //this is needed for: assertNotEquals(studentTrie, studentTrie2, "Unequal tries with equal counts found equal.");
            } else if (child1 != null || child2 != null) return false;
        }
        return true;
    }
}
