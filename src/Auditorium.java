//Rikhab Yusuf	rty200000

public class Auditorium {   //class that creates theater

    private Node first; //holds first node

    public Auditorium() {}  //constructor

    public Auditorium(Node first, char[][] aud, int row, int col) { //overloaded constructor
        Node rowHead = first;   //starts at first
        Node currNode = rowHead;    //starts at first
        for(int r = 0; r < row; r++) {  //loops until rows reached limit
            for(int c = 0; c < col; c++) {  //loops until columns reached limit
                char colInCharForm = (char)(c + 65);    //converts column to int form
                Seat currSeat = new Seat(r, colInCharForm, aud[r][c]);  //creates current seat from data in seat
                currNode.setPayload(currSeat);  //sets data in seat
                if(c+1 != col) {    //if node is not the last in the row, create seat and go to next seat
                    currNode.setNext(new Node());
                    currNode = currNode.getNext();
                }
            }
            if(r+1 != row) {    //if row is not the last row in list, create new row
                rowHead.setDown(new Node());
                rowHead = rowHead.getDown();
                currNode = rowHead;
            }
        }
    }

    //setter
    public void setFirst(Node f) {  //mutates first
        first = f;
    }

    //getter
    public Node getFirst() {    //accesses first
        return first;
    }
}
