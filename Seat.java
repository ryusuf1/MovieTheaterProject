//Rikhab Yusuf	rty200000

public class Seat {

    private int row;    //var for row
    private char seat;  //var for col
    private char ticketType;    //var for ticket type

    public Seat() {}    //constructor

    public Seat(int r, char s, char t) {    //overloaded constructor
        row = r;
        seat = s;
        ticketType = t;
    }

    //setters
    public void setRow(int r) { //mutates row
        row = r;
    }
    public void setSeat(char s) {   //mutates row
        seat = s;
    }
    public void setTicketType(char t) { //mutates row
        ticketType = t;
    }

    //getters
    public int getRow() {   //gets row
        return row;
    }
    public char getSeat() { //gets column
        return seat;
    }
    public char getTicketType() {   //gets ticket type
        return ticketType;
    }
}
