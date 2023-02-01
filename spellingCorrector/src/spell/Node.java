package spell;

public class Node implements INode{
    Node [] children;
    int count;

    public Node(){
        children = new Node[26];
        count = 0;
    }
    @Override
    public int getValue() {
        //Returns the frequency count for the word represented by the node.
        return count;
    }

    @Override
    public void incrementValue() {
        //Increments the frequency count for the word represented by the node.
        count++;
    }

    @Override
    public INode[] getChildren() {
        //Returns the child nodes of this node.
        //return new INode[0];
        return children;
    }

}
