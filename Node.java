//Rikhab Yusuf	rty200000

public class Node { //class that creates node

    private Node next;  //goes to next node
    private Node down;  //goes to down node
    private Seat payload;   //goes to data in node

    public Node() {}    //constructor

    public Node(Node n, Node d, Seat p) {   //overloaded constructor
        next = n;
        down = d;
        payload = p;
    }

    //setters
    public void setNext(Node n) {   //mutates next
        next = n;
    }
    public void setDown(Node d) {   //mutates down
        down = d;
    }
    public void setPayload(Seat p) {    //mutates data in node
        payload = p;
    }

    //getters
    public Node getNext() { //gets next node
        return next;
    }
    public Node getDown() { //gets down node
        return down;
    }
    public Seat getPayload() {  //gets data in node
        return payload;
    }


}
