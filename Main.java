//Rikhab Yusuf  rty200000

import java.io.*;   //imports java file class
import java.util.*; //imports scanner
public class Main {

    public static String splitUsername(String line) {   //method that splits user's username
        String[] arr = line.split(" ");
        return arr[0];
    }

    public static String splitPassword(String line) {   //method that splits user's password
        String[] arr = line.split(" ");
        return arr[1];
    }

    public static int displayMenuToCustomer(Scanner scan) { //method that displays menu to customer
        System.out.println("1. Reserve Seats");
        System.out.println("2. View Orders");
        System.out.println("3. Update Order");
        System.out.println("4. Display Receipt");
        System.out.println("5. Log Out");

        boolean isChoiceValid = false;
        int choice = 0;
        while (isChoiceValid == false) {    //verifies that user input is valid and in range
            try {
                choice = scan.nextInt();
                if (choice < 1 || choice > 5) {
                    throw new IllegalArgumentException();
                }
                isChoiceValid = true;
            } catch (InputMismatchException e) {
                System.out.println("Invalid choice, try again.");
                scan.nextLine();
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid choice, try again.");
            }
        }

        return choice;
    }

    public static void printAudToUser(Node first, int col) {    //method that prints theater to user
        if (first == null) { //checks if list is empty
            System.out.println("empty list");
        }
        Node rowHead = first;   //assigns the first node as the start of the row
        Node curr = rowHead;    //assigns the first node as current node

        System.out.print("  "); //prints space between each elements
        char colNum = 'A';  //holds the name of the column
        for (int c = 0; c < col; c++) {  //prints out the column name based on amount of columns
            System.out.print(colNum);
            colNum++;
        }

        int rowNum = 1; //holds row name
        System.out.println();
        System.out.print(rowNum + " "); //prints row name
        while (curr != null && rowHead != null) {    //loops until there are no more nodes
            char ticketType;    //holds the ticket type
            if (curr.getPayload().getTicketType() != '.') {  //if seat is not empty, print occupied
                ticketType = '#';
            } else {  //if seat is empty, print empty
                ticketType = '.';
            }
            if (curr.getNext() == null) {    //if there are no more nodes on that row, print node and change rows
                System.out.print(ticketType);
                System.out.println();
            } else {  //print node
                System.out.print(ticketType);
            }
            if (curr.getNext() == null && rowHead.getDown() != null) {    //if row has reached end and there are rows under start a new row
                rowNum++;
                System.out.print(rowNum + " "); //print new row name
            }
            if (curr.getNext() == null) {    //if row has reached end and there are rows under start a new row
                rowHead = rowHead.getDown();
                curr = rowHead; //sets current node as row head
            } else {
                curr = curr.getNext();  //moves to next node
            }
        }
    }

    public static boolean checkIfSeatsAvailable(Node first, int totalTiks, int row, int col) {  //method that checks if seats are available
        Node curr = first;  //assigns curr to first

        for (int r = 1; r < row; r++) {  //traverses to the row that customer has chosen
            curr = curr.getDown();
        }
        for (int c = 0; c < col; c++) {  //traverses to the column that customer has chosen
            curr = curr.getNext();
        }

        int counter = 0;    //counter that verifies if seats are available
        while (totalTiks != 0) { //loops until the total seats requested is 0
            if (curr.getPayload().getTicketType() != '.') {  //if seat is not available, counter increments
                counter++;
            }
            curr = curr.getNext();
            totalTiks--;
        }
        if (counter == 0) {  //if counter is 0, seats are available
            return true;
        } else {  //if counter is not 0, seats are not available
            return false;
        }
    }

    public static boolean checkIfSeatsPossible(Node first, int totalTiks, int row, int col) {   //method that determines whether amount of seats requested is possible
        Node curr = first;  //assigns current node to first

        for (int r = 1; r < row; r++) {  //traverses to the row that customer has chosen
            curr = curr.getDown();
        }
        for (int c = 0; c < col; c++) {  //traverses to the column that customer has chosen
            curr = curr.getNext();
        }

        while (totalTiks != 0) { //loops until the total seats requested is 0
            try {   //attempts to move through nodes
                curr = curr.getNext();
                totalTiks--;
            } catch (NullPointerException e) {    //if row has ended and tickets are not 0, catches exception
                return false;   //if exception is thrown, returns false
            }
        }
        return true;    //if exception is not thrown, requested seats are possible
    }

    public static Node reserveSeats(Node first, int totalTiks, int adults, int children, int seniors, int row, int col, HashMap<String, List<Object>> map, int audChoice, String user) {   //method that reserves seats for customer
        Node curr = first;  //assigns current node to first

        for (int r = 1; r < row; r++) {  //traverses to the row that customer has chosen
            curr = curr.getDown();
        }
        for (int c = 0; c < col; c++) {  //traverses to the column that customer has chosen
            curr = curr.getNext();
        }
        int r = curr.getPayload().getRow()+1;
        char c = curr.getPayload().getSeat();
        String order = "Auditorium " + audChoice + ";"; //creates order to put in hashmap
        int tempA = adults; //holds num of adults
        int tempC = children;   //holds num of children
        int tempS = seniors;    //holds num of seniors

        while (totalTiks != 0) { //loops until total tickets requested reaches 0
            while (adults != 0 && curr != null) {    //loops until total adult tickets requested reaches 0
                r = curr.getPayload().getRow()+1;
                c = curr.getPayload().getSeat();
                curr.getPayload().setTicketType('A');   //assigns adult tickets
                curr = curr.getNext();  //moves to next node
                totalTiks--;    //decrements total tickets after they've been assigned
                adults--;   //decrements adult tickets after they've been assigned
                order = order + r + "" + c + ";";   //adds to order
            }
            while (children != 0) {  //loops until total child tickets requested reaches 0
                r = curr.getPayload().getRow()+1;
                c = curr.getPayload().getSeat();
                curr.getPayload().setTicketType('C');   //assigns child tickets
                curr = curr.getNext();  //moves to next node
                totalTiks--;    //decrements total tickets after they've been assigned
                children--; //decrements children tickets after they've been assigned
                order = order + r + "" + c + ";";   //adds to order
            }
            while (seniors != 0) {   //loops until total senior tickets requested reaches 0
                r = curr.getPayload().getRow()+1;
                c = curr.getPayload().getSeat();
                curr.getPayload().setTicketType('S');   //assigns senior tickets
                curr = curr.getNext();  //moves to next node
                totalTiks--;    //decrements total tickets after they've been assigned
                seniors--;  //decrements senior tickets after they've been assigned
                order = order + r + "" + c + ";";   //adds to order
            }
        }
        order = order + tempA + " adult;" + tempC + " child;" + tempS + " senior";
        map.get(user).add(order);   //adds order to hashmap
        return first;   //returns updated auditorium
    }

    public static Node bestAvailable(Node first, int totalTiks, int row, int col) {  //calculates best available seats
        Node curr = first;  //holds the current node
        Node rowHead = first;   //holds the row head
        Node bestSeat = first;  //holds the best seat
        int counter = 0;    //counts amount of times best seats are compared

        //calculate center of auditorium
        double rowCenter = (row / 2.0); //calculates row center of theater
        double colCenter = (col / 2.0); //calculates column center of theater
        int column = (int) Math.ceil(colCenter); //rounds column center up
        double AudRowCenter = (int) Math.ceil(rowCenter);    //rounds row center up

        double smallestDistance = Integer.MAX_VALUE;    //holds the smallest distance from center
        double smallestxDistance = Integer.MAX_VALUE;
        while (curr != null && rowHead != null) {    //loops until list is empty
            boolean areSeatsAvailable = true;   //checks if seats are available
            Node seat = curr;   //assigns current node to seat
            Node forCurr = curr;    //current node used in for loop
            for (int i = 0; i < totalTiks; i++) {    //loops until total tickets have been accounted for
                if (forCurr.getPayload().getTicketType() != '.') {    //if seats requested are occupied return false
                    areSeatsAvailable = false;
                }
                if ((forCurr.getNext() == null) && (i + 1 != totalTiks)) {  //if row has finished and total tickets is not done, seats are not available
                    areSeatsAvailable = false;
                    break;
                } else {  //moves to next node
                    forCurr = forCurr.getNext();
                }
            }

            if (areSeatsAvailable == true) {
                counter++;
                double tiksCenter = (totalTiks / 2.0);  //calculates center of requested seats
                int selectionCenter = (int) Math.ceil(tiksCenter);   //changes center to int
                int startingPosition = (seat.getPayload().getSeat() - 65);    //calculates int form of seat
                double totalSelectionCenter = startingPosition + selectionCenter;   //adds center of selection and starting position

                double xDistance = Math.pow(Math.abs(totalSelectionCenter - colCenter), 2); //calculates x distance
                double yDistance = Math.pow(Math.abs((seat.getPayload().getRow() + 1) - AudRowCenter), 2);    //calculates y distance
                double distance = Math.sqrt(xDistance + yDistance); //calculates distance from center of auditorium to selection center

                double seatRow = seat.getPayload().getRow() + 1.0;    //gets the row of the current seat
                double rowDistance = Math.abs(seatRow - AudRowCenter + 0.0);    //calculates distance from current seat row and theater row center
                double bestSeatRow = bestSeat.getPayload().getRow() + 1.0;    //gets the best seat row
                double bestSeatDistance = Math.abs(bestSeatRow - AudRowCenter + 0.0); //calculates best seat distance from theater row center

                if (((distance < smallestDistance) || ((distance == smallestDistance) && (rowDistance < bestSeatDistance)) || ((distance == smallestDistance) && (xDistance == smallestxDistance)))) {    //if distance is smaller than smallest distance or if distances are equal and row is closer to center, that is the new best seat
                    smallestDistance = distance;
                    bestSeat = seat;
                    smallestxDistance = xDistance;
                }
            }

            if (curr.getNext() == null) {   //goes to the next node
                rowHead = rowHead.getDown();
                curr = rowHead;
            } else {    //goes to the next node
                curr = curr.getNext();
            }
        }

        if(counter == 0){   //if no seats have been compared, there is no best seat
            return null;
        }
        else {
            return bestSeat;    //returns the starting position of best seat
        }
    }

    public static int splitAudForUpdate(String line){   //splits auditorium from rest of line
        String[] firstSplit = line.split(" ");
        String[] secondSplit = firstSplit[2].split(",");
        int aud = Integer.valueOf(secondSplit[0]);
        return aud;
    }

    public static Node addSeat(Node first, int row, int col, char seat, int adult, int child, int senior, int audChoice, int totalTiks, HashMap<String, List<Object>> map, int mapIndex, String user){  //adds seat to existing order
        Node curr = first;

        for(int i = 1; i < row; i++){   //goes to the selected row
            curr = curr.getDown();
        }
        for(int j = 0; j < col; j++){   //goes to the selected column
            curr = curr.getNext();
        }

        String[] prevOrder = String.valueOf(map.get(user).get(mapIndex)).split(";");    //splits up order
        String order = "";  //variable to hold new order
        for(int t = 0; t < (prevOrder.length-3); t++){  //adds existing order seats to new order
            if(t == (prevOrder.length-4)){
                order = order + prevOrder[t];
            }else {
                order = order + prevOrder[t] + ";";
            }
        }

        int tempA = adult;  //holds number of adults
        int tempC = child;  //holds number of children
        int tempS = senior; //holds number of seniors
        String newAdult = incrementPerson(prevOrder[prevOrder.length-3], tempA);    //assigns new number to adults
        String newChild = incrementPerson(prevOrder[prevOrder.length-2], tempC);     //assigns new number to children
        String newSenior = incrementPerson(prevOrder[prevOrder.length-1], tempS);    //assigns new number to seniors

        while(totalTiks != 0){
            while(adult != 0){
                order = order + ";" + (curr.getPayload().getRow()+1) + curr.getPayload().getSeat();
                curr.getPayload().setTicketType('A');   //assigns adult tickets
                curr = curr.getNext();  //moves to next node
                totalTiks--;    //decrements total tickets after they've been assigned
                adult--;
            }
            while(child != 0){
                order = order + ";" + (curr.getPayload().getRow()+1) + curr.getPayload().getSeat();
                curr.getPayload().setTicketType('C');   //assigns adult tickets
                curr = curr.getNext();  //moves to next node
                totalTiks--;    //decrements total tickets after they've been assigned
                child--;
            }
            while(senior != 0){
                order = order + ";" + (curr.getPayload().getRow()+1) + curr.getPayload().getSeat();
                curr.getPayload().setTicketType('S');   //assigns adult tickets
                curr = curr.getNext();  //moves to next node
                totalTiks--;    //decrements total tickets after they've been assigned
                senior--;
            }
        }
        String[] seats = order.split(";");  //splits order apart
        order = sortSeats(seats);   //sorts seats in order
        order = order + ";" + newAdult + ";" + newChild + ";" + newSenior;  //assembles new order
        map.get(user).set(mapIndex, order); //updates order in hashmap
        return first;
    }

    public static String sortSeats (String[] seats){    //method that sorts seats in ascending order
        for(int i = 1; i < seats.length-1; i++){    //loops until seats are sorted
            String curr = seats[i];
            String next = seats[i+1];
            if(curr.charAt(0) > next.charAt(0)){    //if row is greater push behind current
                seats[i] = next;
                seats[i+1] = curr;
                i = 1;
            } else if(curr.charAt(0) == next.charAt(0)){    //if rows are equal and col is greater push behind current
                if(curr.charAt(1) > next.charAt(1)){
                    seats[i] = next;
                    seats[i+1] = curr;
                    i = 1;
                }
            }
        }
        String order = seats[0] + ";";  //new order
        for(int i = 1; i < seats.length; i++){  //loops until new order is assembled
            if(i+1 == seats.length){    //if last order, dont add semicolon
                order = order + seats[i];
            }
            else{
                order = order + seats[i] + ";";
            }
        }
        return order;
    }

    public static String incrementPerson(String person, int amount){    //method that increases amount in ticket
        String[] firstSplit = person.split(" ");    //splits amount of people from line
        int val = Integer.valueOf(firstSplit[0]);
        val = val + amount; //updates amount of tickets
        String str = val + " " + firstSplit[1];

        return str;
    }

    public static boolean isOrderEmpty(String order){   //method that checks if there are any orders
        String[] firstSplit = order.split(";");
        if(firstSplit[1].contains("adult")){    //if there are no seats there are no orders
            return true;
        }
        else{
            return false;
        }
    }

    public static Node removeSeat(Node first, int row, int col, char seat, HashMap<String, List<Object>> map, int mapIndex, String user){   //method that removes seat from order
        Node curr = first;
        for(int i = 1; i < row; i ++){  //goes to proper row
            curr = curr.getDown();
        }
        for(int j = 0; j < col; j++){   //goes to proper column
            curr = curr.getNext();
        }

        char tikType = curr.getPayload().getTicketType();
        curr.getPayload().setTicketType('.');   //sets ticket to empty

        String removeSeat = row + "" + seat;
        String[] order = String.valueOf(map.get(user).get(mapIndex)).split(";");    //splits order apart
        String newOrder = "";

        if(tikType == 'A'){ //if adult is sitting there, decrement adult
            order[order.length-3]  = decrementSeatHolder(order[order.length-3]);
        } else if(tikType == 'C'){  //if child is sitting there, decrement child
            order[order.length-2]  = decrementSeatHolder(order[order.length-2]);
        } else{ //if senior is sitting there, decrement senior
            order[order.length-1]  = decrementSeatHolder(order[order.length-1]);
        }
        for(int i = 0; i < order.length; i++){  //locates and removes seat from order
            if(!order[i].contains(removeSeat)){
                if(i == (order.length-1)){
                    newOrder = newOrder + order[i];
                }
                else {
                    newOrder = newOrder + order[i] + ";";
                }
            }
        }

        boolean isEmpty = isOrderEmpty(newOrder);
        if(isEmpty == true){    //if no orders, cancel order
            map.get(user).remove(mapIndex);
        }else { //else, update hashmap
            map.get(user).set(mapIndex, newOrder);
        }
        return first;
    }

    public static String decrementSeatHolder(String person){    //method that decrements seat holders
        String[] firstSplit = person.split(" ");    //splits line apart
        int val = Integer.valueOf(firstSplit[0]);
        String str = firstSplit[1];
        val = val-1;    //decrements value
        String newPerson = val + " " + str;

        return newPerson;
    }

    public static Node cancelOrder(Node first, HashMap<String, List<Object>> map, int mapIndex, String user){   //method that cancels order
        String[] prevOrder = String.valueOf(map.get(user).get(mapIndex)).split(";");
        for(int i = 1; i < (prevOrder.length-3); i++){  //locates and empties seat
            String seat = prevOrder[i];
            int row = Integer.valueOf(String.valueOf(seat.charAt(0)));
            char col = seat.charAt(1);
            int colIntForm = col - 65;
            first = cancelOrderHelper(first, row, colIntForm);
        }

        map.get(user).remove(mapIndex); //updates hashmap


        return first;
    }

    public static Node cancelOrderHelper(Node first, int row, int col){ //helper function for canceling orders
        Node curr = first;
        for(int i = 1; i < row; i++){   //locates proper row
            curr = curr.getDown();
        }
        for(int j = 0; j < col; j++){   //locates proper column
            curr = curr.getNext();
        }
        curr.getPayload().setTicketType('.');   //sets seat to empty
        return first;
    }

    public static double receiptTotal(HashMap<String, List<Object>> map, int mapIndex, String user){    //method that calculates total for order
        String[] firstSplit = String.valueOf(map.get(user).get(mapIndex)).split(";");   //splits order
        double total = 0.0;
        String adult = firstSplit[firstSplit.length-3]; //variable for adult tickets
        String child = firstSplit[firstSplit.length-2]; //variable for child tickets
        String senior = firstSplit[firstSplit.length-1];    //variable for senior tickets

        total = total + receiptTotalHelper(adult, 10.0);    //gets new total for adults
        total = total + receiptTotalHelper(child, 5.0); //gets new total for children
        total = total + receiptTotalHelper(senior, 7.50);   //gets new total for seniors

        return total;
    }

    public static double receiptTotalHelper(String person, double x){   //helper method for calculating receipt
        String[] firstSplit = person.split(" ");    //splits number of tickets
        double val = Integer.valueOf(firstSplit[0]);
        return val * x; //finds new value
    }

    public static int calculateOpenSeats(Node first){   //method that calculates open seats in auditorium
        int open = 0;   //holds number of open seats
        Node curr = first;
        Node rowHead = curr;

        while(rowHead != null){ //loops until aud is complete
            while(curr != null){
                if(curr.getPayload().getTicketType() == '.'){   //increments counter if seat is open
                    open++;
                }
                curr = curr.getNext();
            }
            rowHead = rowHead.getDown();
            curr = rowHead;
        }
        return open;    //returns number of open seats
    }

    public static int calculateReservedSeats(Node first){   //method that calculates number of reserved seats in auditorium
        int reserved = 0;
        Node curr = first;
        Node rowHead = curr;

        while(rowHead != null){ //loops until auditorium is finished
            while(curr != null){
                if(curr.getPayload().getTicketType() != '.'){   //increments counter if seat is occupied
                    reserved++;
                }
                curr = curr.getNext();
            }
            rowHead = rowHead.getDown();
            curr = rowHead;
        }
        return reserved;    //returns number of reserved seats
    }

    public static int calculateAdults(Node first){  //method that calculates number of adults in auditorium
        int a = 0;
        Node curr = first;
        Node rowHead = curr;

        while(rowHead != null){ //loops until auditorium is complete
            while(curr != null){
                if(curr.getPayload().getTicketType() == 'A'){
                    a++;
                }
                curr = curr.getNext();
            }
            rowHead = rowHead.getDown();
            curr = rowHead;
        }
        return a;   //returns number of adults
    }

    public static int calculateSeniors(Node first){ //method that calculates number of seniors in auditorium
        int s = 0;
        Node curr = first;
        Node rowHead = curr;

        while(rowHead != null){
            while(curr != null){
                if(curr.getPayload().getTicketType() == 'S'){   //loops until auditorium is complete
                    s++;
                }
                curr = curr.getNext();
            }
            rowHead = rowHead.getDown();
            curr = rowHead;
        }
        return s;   //returns number of seniors
    }

    public static int calculateChildren(Node first){    //method that calculates number of children in auditorium
        int c = 0;
        Node curr = first;
        Node rowHead = curr;

        while(rowHead != null){ //loops until auditorium is complete
            while(curr != null){
                if(curr.getPayload().getTicketType() == 'C'){
                    c++;
                }
                curr = curr.getNext();
            }
            rowHead = rowHead.getDown();
            curr = rowHead;
        }
        return c;   //returns number of children
    }

    public static double calculateTotal(Node first){    //method that calculates total profit from auditorium
        Node curr = first;
        Node rowHead = curr;
        double total = 0.0;

        while(rowHead != null){ //loops until auditorium is complete
            while(curr != null){
                if(curr.getPayload().getTicketType() == 'A'){   //if seat is adult add 10 dollars
                    total = total + 10.0;
                } else if(curr.getPayload().getTicketType() == 'C'){    //if seat is child add 5 dollard
                    total = total + 5.0;
                } else if(curr.getPayload().getTicketType() == 'S'){    //if seat is senior add 7.50 dollars
                    total = total + 7.50;
                }
                curr = curr.getNext();
            }
            rowHead = rowHead.getDown();
            curr = rowHead;
        }
        return total;   //return total profit
    }

    public static void printToFile(Node first, int col, PrintWriter outfile){   //print updated auditorium to file
        if(first != null){  //if list is not empty
            Node rowHead = first;   //starts list at beginning
            Node curr = rowHead;    //starts list at beginning

            while(curr != null && rowHead != null) {    //loops until list is null
                if(curr.getNext() == null) {    //if row is finished, print node and go to next node
                    outfile.print(curr.getPayload().getTicketType());
                    rowHead = rowHead.getDown();
                    curr = rowHead;
                    outfile.println();
                }
                else {  //print node
                    outfile.print(curr.getPayload().getTicketType());
                    curr = curr.getNext();
                }
            }
        }
    }
    public static void main(String[] args){
        Scanner scan = new Scanner(System.in);  //scanner for user input
        HashMap<String, List<Object>> input = new HashMap<>();  //hashmap for users

        File users = new File("userdb.dat");    //creates file that holds user information
        Scanner scanUsers = new Scanner(System.in); //creates scanner for file
        boolean doesFileExist = false;  //checks if file exists
        while(doesFileExist == false) { //loops until file exists
            try {
                scanUsers = new Scanner(users);
                doesFileExist = true;
            } catch (FileNotFoundException e) { //catches exception if file is not found
                System.out.println("File not found, try again");
            }
        }
        doesFileExist = false;
        while (scanUsers.hasNextLine()) {   //loops until file is over
            String line = scanUsers.nextLine();
            String inputUsername = splitUsername(line); //gets username from file
            String inputPassword = splitPassword(line); //gets password from file
            List<Object> list = new ArrayList<>();  //creates array list to hold user info
            list.add(inputUsername);    //adds username to list
            list.add(inputPassword);    //adds password to list
            input.put(inputUsername, list); //adds list to user
        }

        File file1 = new File("A1.txt");    //creates file for auditorium 1
        Scanner fromAud1 = new Scanner(System.in);  //scanner for auditorium 1
        while(doesFileExist == false) { //loops until file exists
            try {
                fromAud1 = new Scanner(file1);
                doesFileExist = true;
            } catch (FileNotFoundException e) { //catches exception if file is not found
                System.out.println("File not found, try again");
            }
        }
        doesFileExist = false;
        char[][] inputAud1 = new char[100][26];  //holds theater
        int inputAudRowIndex1 = 0;   //holds theater rows
        int inputAudColIndex1 = 0;   //holds column rows
        int tempAudColIndex1 = 0;    //holds column rows
        while (fromAud1.hasNextLine() == true) { //loops until file reaches its end
            String rowFromFile1 = fromAud1.nextLine();   //holds row from file

            for (int i = 0; i < rowFromFile1.length(); i++) { //loops until row is finished
                inputAud1[inputAudRowIndex1][tempAudColIndex1] = rowFromFile1.charAt(i);
                tempAudColIndex1++;  //increments column
            }
            inputAudColIndex1 = tempAudColIndex1; //assigns column number
            tempAudColIndex1 = 0;    //sets column back to 0
            inputAudRowIndex1++; //increments row number
        }
        fromAud1.close();   //closes scanner
        Node first1 = new Node();   //creates new node
        Auditorium aud1 = new Auditorium(first1, inputAud1, inputAudRowIndex1, inputAudColIndex1);  //creates auditorium from node

        File file2 = new File("A2.txt");    //creates file for auditorium 2
        Scanner fromAud2 = new Scanner(System.in);  //scanner for auditorium 2
        while(doesFileExist == false) { //loops until file exists
            try {
                fromAud2 = new Scanner(file2);
                doesFileExist = true;
            } catch (FileNotFoundException e) { //catches exception if file is not found
                System.out.println("File not found, try again");
            }
        }
        doesFileExist = false;
        char[][] inputAud2 = new char[100][26];  //holds theater
        int inputAudRowIndex2 = 0;   //holds theater rows
        int inputAudColIndex2 = 0;   //holds column rows
        int tempAudColIndex2 = 0;    //holds column rows
        while (fromAud2.hasNextLine() == true) { //loops until file reaches its end
            String rowFromFile2 = fromAud2.nextLine();   //holds row from file

            for (int i = 0; i < rowFromFile2.length(); i++) { //loops until row is finished
                inputAud2[inputAudRowIndex2][tempAudColIndex2] = rowFromFile2.charAt(i);
                tempAudColIndex2++;  //increments column
            }
            inputAudColIndex2 = tempAudColIndex2; //assigns column number
            tempAudColIndex2 = 0;    //sets column back to 0
            inputAudRowIndex2++; //increments row number
        }
        fromAud2.close();   //closes scanner
        Node first2 = new Node();   //creates new node
        Auditorium aud2 = new Auditorium(first2, inputAud2, inputAudRowIndex2, inputAudColIndex2);  //creates auditorium from node

        File file3 = new File("A3.txt");    //creates file for auditorium 3
        Scanner fromAud3 = new Scanner(System.in);  //scanner for auditorium 2
        while (doesFileExist == false) {    //loops until file reaches end
            try {
                fromAud3 = new Scanner(file3);
                doesFileExist = true;
            } catch (FileNotFoundException e) {     //catches exception if file is not found
                System.out.println("File not found, try again");
            }
        }
        doesFileExist = false;
        char[][] inputAud3 = new char[100][26];  //holds theater
        int inputAudRowIndex3 = 0;   //holds theater rows
        int inputAudColIndex3 = 0;   //holds column rows
        int tempAudColIndex3 = 0;    //holds column rows
        while (fromAud3.hasNextLine() == true) { //loops until file reaches its end
            String rowFromFile3 = fromAud3.nextLine();   //holds row from file

            for (int i = 0; i < rowFromFile3.length(); i++) { //loops until row is finished
                inputAud3[inputAudRowIndex3][tempAudColIndex3] = rowFromFile3.charAt(i);
                tempAudColIndex3++;  //increments column
            }
            inputAudColIndex3 = tempAudColIndex3; //assigns column number
            tempAudColIndex3 = 0;    //sets column back to 0
            inputAudRowIndex3++; //increments row number
        }
        fromAud3.close();   //closes scanner
        Node first3 = new Node();   //creates node
        Auditorium aud3 = new Auditorium(first3, inputAud3, inputAudRowIndex3, inputAudColIndex3);  //creates auditorium from node

        String username = "";
        String password = "";
        boolean canCustomerLogin = false;
        while(canCustomerLogin == false) {  //loops until customer can login
            System.out.println("Enter username");
            boolean isUsernameRight = false;
            while (isUsernameRight == false) {  //loops until input is correct
                try {
                    username = scan.nextLine();
                    if (!input.containsKey(username)) { //if username is not in hashmap, it is invalid
                        throw new IllegalArgumentException();
                    }
                    isUsernameRight = true;
                } catch (IllegalArgumentException e) {
                    System.out.println("Username does not exist, try again");
                }
            }

            System.out.println("Enter password");
            int wrongPass = 0;
            password = scan.nextLine();
            boolean isPasswordRight = false;
            while (isPasswordRight == false) {  //loops until password is correct
                if (wrongPass == 2) {   //if password is wrong three times begin process again
                    wrongPass = 0;
                    System.out.println("Enter username");
                    isUsernameRight = false;
                    while (isUsernameRight == false) {
                        try {
                            username = scan.nextLine();
                            if (!input.containsKey(username)) {
                                throw new IllegalArgumentException();
                            }
                            isUsernameRight = true;
                        } catch (IllegalArgumentException e) {
                            System.out.println("Username does not exist, try again");
                        }
                    }

                    System.out.println("Enter password");
                    password = scan.nextLine();
                }
                if (input.get(username).get(1).equals(password) == false) { //checks hashmap for password that matches username
                    wrongPass++;
                    System.out.println("Invalid password, try again");
                    password = scan.nextLine();
                } else {
                    isPasswordRight = true;
                }
            }
            canCustomerLogin = true;
        }

        boolean isProgramBeingUsed = true;
        while(isProgramBeingUsed == true){  //loops until program is exited by admin
            boolean doesProgramNeedToExit = false;
            boolean isCustomerLoggedIn = true;
            while (isCustomerLoggedIn == true) {    //loops until customer is logged out
                if (username.equals("admin") == false) {    //if user is not admin
                    int menuChoice = displayMenuToCustomer(scan);   //gets customers menu choice

                    if (menuChoice == 1) {  //if choice is 1, reserves seat for customer
                        System.out.println("1. Auditorium 1\n2. Auditorium 2\n3. Auditorium 3");
                        int audChoice = -1;
                        boolean isAudChoiceValid = false;
                        while (isAudChoiceValid == false) { //loops until auditorium choice is valid
                            try {
                                audChoice = scan.nextInt();
                                if (audChoice > 3 || audChoice < 1) {
                                    throw new IllegalArgumentException();
                                }
                                isAudChoiceValid = true;
                            } catch (InputMismatchException e) {
                                System.out.println("Choice is invalid, try again");
                                scan.nextLine();
                            } catch (IllegalArgumentException e) {
                                System.out.println("Choice out of range, try again");
                            }
                        }

                        Node tempFirst = null;
                        int tempCol = 0;
                        int tempRow = 0;
                        if (audChoice == 1) {   //if auditorium choice is 1, uses proper auditorium
                            tempFirst = first1;
                            tempCol = inputAudColIndex1;
                            tempRow = inputAudRowIndex1;
                        } else if (audChoice == 2) {//if auditorium choice is 2, uses proper auditorium
                            tempFirst = first2;
                            tempCol = inputAudColIndex2;
                            tempRow = inputAudRowIndex2;
                        } else {    //if auditorium choice is 3, uses proper auditorium
                            tempFirst = first3;
                            tempCol = inputAudColIndex3;
                            tempRow = inputAudRowIndex3;
                        }

                        printAudToUser(tempFirst, tempCol); //prints auditorium to customer

                        System.out.println("Enter your row choice");
                        int rowChoice = 0;
                        boolean isRowChoiceIsValid = false;
                        while (isRowChoiceIsValid == false) {   //loops until row choice is valid
                            try {
                                rowChoice = scan.nextInt();
                                if (rowChoice < 1 || rowChoice > tempRow) { //checks if row choice is in range
                                    throw new IllegalArgumentException();
                                }
                                isRowChoiceIsValid = true;
                            } catch (InputMismatchException e) {
                                System.out.println("Invalid input, try again");
                                scan.nextLine();
                            } catch (IllegalArgumentException e) {
                                System.out.println("Choice is out of range, try again");
                            }
                        }

                        System.out.println("Enter your seat choice");
                        char seatChoice = '!';
                        char colInCharForm = (char) (tempCol + 64);
                        boolean isSeatChoiceValid = false;
                        while (isSeatChoiceValid == false) {    //loops until seat choice is valid
                            try {
                                seatChoice = scan.next().charAt(0);
                                if (seatChoice < 'A' || seatChoice > colInCharForm) {   //checks if seat choice is in range
                                    throw new IllegalArgumentException();
                                }
                                isSeatChoiceValid = true;
                            } catch (InputMismatchException e) {
                                System.out.println("Invalid input, try again");
                            } catch (IllegalArgumentException e) {
                                System.out.println("Choice is out of range, try again");
                            }
                        }
                        int seatChoiceIntForm = seatChoice - 65;    //gets int form of seat

                        System.out.println("How many adult tickets would you like");
                        int adultTiks = 0;
                        boolean isAdultTiksValid = false;
                        while (isAdultTiksValid == false) { //loops until adult tickets are valid
                            try {
                                adultTiks = scan.nextInt();
                                if (adultTiks < 0) {    //checks if tickets are in range
                                    throw new IllegalArgumentException();
                                }
                                isAdultTiksValid = true;
                            } catch (InputMismatchException e) {
                                System.out.println("Invalid input, try again");
                            } catch (IllegalArgumentException e) {
                                System.out.println("Invalid input, try again");
                            }
                        }

                        System.out.println("How many child tickets would you like");
                        int childTiks = 0;
                        boolean isChildTiksValid = false;
                        while (isChildTiksValid == false) { //loops until children tickets are valid
                            try {
                                childTiks = scan.nextInt();
                                if (childTiks < 0) {    //checks if tickets are in range
                                    throw new IllegalArgumentException();
                                }
                                isChildTiksValid = true;
                            } catch (InputMismatchException e) {
                                System.out.println("Invalid input, try again");
                            } catch (IllegalArgumentException e) {
                                System.out.println("Invalid input, try again");
                            }
                        }

                        System.out.println("How many senior tickets would you like");
                        int seniorTiks = 0;
                        boolean isSeniorTiksValid = false;
                        while (isSeniorTiksValid == false) {    //checks if senior tickets are valid
                            try {
                                seniorTiks = scan.nextInt();
                                if (seniorTiks < 0) {   //checks if tickets are in range
                                    throw new IllegalArgumentException();
                                }
                                isSeniorTiksValid = true;
                            } catch (InputMismatchException e) {
                                System.out.println("Invalid input, try again");
                            } catch (IllegalArgumentException e) {
                                System.out.println("Invalid input, try again");
                            }
                        }

                        int totalTiks = adultTiks + childTiks + seniorTiks; //calculates total tickets
                        boolean areSeatsPossible = checkIfSeatsPossible(tempFirst, totalTiks, rowChoice, seatChoiceIntForm);    //checks if seats are possible
                        if (areSeatsPossible == false) {    //if seats are not possible, let customer know
                            System.out.println("no seats available");
                        } else {
                            boolean areSeatsAvailable = checkIfSeatsAvailable(tempFirst, totalTiks, rowChoice, seatChoiceIntForm);  //check if seats are available
                            if (areSeatsAvailable == true) {    //if seats are available, reserve seats
                                tempFirst = reserveSeats(tempFirst, totalTiks, adultTiks, childTiks, seniorTiks, rowChoice, seatChoiceIntForm, input, audChoice, username);

                                if (audChoice == 1) {   //update auditorium accordingly
                                    first1 = tempFirst;
                                } else if (audChoice == 2) {    //update auditorium accordingly
                                    first2 = tempFirst;
                                } else {    //update auditorium accordingly
                                    first3 = tempFirst;
                                }

                            } else {    //find best available seats
                                Node best = bestAvailable(tempFirst, totalTiks, tempRow, tempCol);
                                if (best == null) { //if best available doesnt exist, let customer know
                                    System.out.println("no seats available");
                                } else {
                                    int bestRowInt = (best.getPayload().getRow() + 1);  //gets best seat row
                                    int bestColInt = (best.getPayload().getSeat() - 65);    //gets best seat column
                                    Node endBest = best;
                                    for (int i = 1; i < totalTiks; i++) {   //locates the last best seat
                                        endBest = endBest.getNext();
                                    }
                                    if (totalTiks == 1) {   //if only one seat display one
                                        System.out.println("Best available " + (best.getPayload().getRow() + 1) + "" + best.getPayload().getSeat());
                                    } else {    //display all best seats
                                        System.out.println("Best available " + (best.getPayload().getRow() + 1) + "" + best.getPayload().getSeat() + "-" + (endBest.getPayload().getRow() + 1) + "" + endBest.getPayload().getSeat());
                                    }
                                    System.out.println("Would you like to reserve best available? Y/N");
                                    char bestAvailableChoice = '!';
                                    boolean isBestAvailableChoiceValid = false;
                                    while (isBestAvailableChoiceValid == false) {   //loops until choice is valid
                                        try {
                                            bestAvailableChoice = scan.next().charAt(0);
                                            if (bestAvailableChoice == 'Y' || bestAvailableChoice == 'N') { //checks if choice is valid
                                                isBestAvailableChoiceValid = true;
                                            } else {
                                                throw new IllegalArgumentException();
                                            }
                                        } catch (InputMismatchException e) {
                                            System.out.println("Invalid input, try again");
                                        } catch (IllegalArgumentException e) {
                                            System.out.println("Invalid choice, try again");
                                        }
                                    }

                                    if (bestAvailableChoice == 'Y') {   //if choice is yes, reserve seats
                                        reserveSeats(tempFirst, totalTiks, adultTiks, childTiks, seniorTiks, bestRowInt, bestColInt, input, audChoice, username);

                                        if (audChoice == 1) {   //update auditorium accordingly
                                            first1 = tempFirst;
                                        } else if (audChoice == 2) {    //update auditorium accordingly
                                            first2 = tempFirst;
                                        } else {    //update auditorium accordingly
                                            first3 = tempFirst;
                                        }
                                    } else {
                                        System.out.println("no seats available");
                                    }
                                }
                            }
                        }

                    } else if (menuChoice == 2) {   //if menu choice is 2, show customer their orders
                        if(input.get(username).size() < 3){ //if there's no seats, there are no orders
                            System.out.println("No orders");
                        }
                        else{
                            for(int i = 2; i < input.get(username).size(); i++){    //loop until no more orders
                                String line = String.valueOf(input.get(username).get(i));
                                String[] order = line.split(";");   //split order information

                                System.out.print(order[0] + ", ");
                                for(int j = 1; j < order.length; j++) { //loop until order is finished
                                    try{
                                        if(order[j+1].contains("adult")){  //print new line if next info contains adult
                                            System.out.println(order[j]);
                                        }
                                        else if(order[j].contains("adult") || order[j].contains("child") || order[j].contains("senior")){
                                            if(!order[j].contains("senior")){   //print new line if info contains senior
                                                System.out.print(order[j] + ", ");
                                            }
                                            else{
                                                System.out.println(order[j]);
                                            }
                                        }
                                        else{
                                            System.out.print(order[j] + ",");
                                        }
                                    }catch (ArrayIndexOutOfBoundsException e){
                                        System.out.println(order[j]);
                                    }
                                }
                                System.out.println();   //seperate orders
                            }
                        }
                    } else if(menuChoice == 3) {    //if choice is 3, update orders
                        if (input.get(username).size() < 3) {   //if no seats, no orders
                            System.out.println("No orders");
                        } else{
                            List<Object> orderList = new ArrayList<>(); //create list to hold orders
                            int index = 0;
                            for (int i = 2; i < input.get(username).size(); i++) {  //loops until all orders are placed into list
                                String line = String.valueOf(input.get(username).get(i));
                                String[] order = line.split(";");   //splits order information
                                String listInput = "";
                                index++;


                                listInput = index + ". " + order[0] + ", ";
                                for (int j = 1; j < order.length; j++) {
                                    if (j == order.length - 1) {
                                        listInput = listInput + order[j];
                                    } else {
                                        listInput = listInput + order[j] + ", ";
                                    }
                                }
                                orderList.add(listInput);
                            }

                            System.out.println("Which order would you like to update?");
                            for (int i = 0; i < orderList.size(); i++) {
                                System.out.println(orderList.get(i));
                            }
                            int updateOrderChoice = 0;
                            boolean isUpdateChoiceValid = false;
                            while (isUpdateChoiceValid == false) {  //loops until choice is valid
                                try {
                                    updateOrderChoice = scan.nextInt();
                                    if (updateOrderChoice < 1 || updateOrderChoice > index) {   //if choice is out of range, throw exception
                                        throw new IllegalArgumentException();
                                    }
                                    isUpdateChoiceValid = true;
                                } catch (InputMismatchException e) {
                                    System.out.println("Invalid input, try again");
                                    scan.nextLine();
                                } catch (IllegalArgumentException e) {
                                    System.out.println("Choice not available, try again");
                                }
                            }
                            boolean isUserUpdatingOrder = true;
                            while (isUserUpdatingOrder == true) {   //loops until user is done updating order
                                System.out.println("1. Add tickets to order\n2. Delete tickets from order\n3. Cancel Order");
                                int updateMenuChoice = 0;
                                boolean isUpdateMenuChoiceValid = false;
                                while (isUpdateMenuChoiceValid == false) {  //loops until choice is valid
                                    try {
                                        updateMenuChoice = scan.nextInt();
                                        if (updateMenuChoice < 1 || updateMenuChoice > 3) { //if choice is out of range, throw exception
                                            throw new IllegalArgumentException();
                                        }
                                        isUpdateMenuChoiceValid = true;
                                    } catch (InputMismatchException e) {
                                        System.out.println("Invalid input, try again");
                                        scan.nextLine();
                                    } catch (IllegalArgumentException e) {
                                        System.out.println("Choice not available, try again");
                                    }
                                }

                                int audForUpdate = splitAudForUpdate(String.valueOf(orderList.get(updateOrderChoice - 1))); //find auditorium that needs to be updated
                                int mapIndex = updateOrderChoice + 1;   //find order on hashmap
                                String currOrder = String.valueOf(orderList.get(updateOrderChoice - 1));

                                Node updateFirst = null;
                                int updateCol = 0;
                                int updateRow = 0;
                                if (audForUpdate == 1) {    //find correct auditorium to update
                                    updateFirst = first1;
                                    updateCol = inputAudColIndex1;
                                    updateRow = inputAudRowIndex1;
                                } else if (audForUpdate == 2) { //find correct auditorium to update
                                    updateFirst = first2;
                                    updateCol = inputAudColIndex2;
                                    updateRow = inputAudRowIndex2;
                                } else if (audForUpdate == 3) { //find correct auditorium to update
                                    updateFirst = first3;
                                    updateCol = inputAudColIndex3;
                                    updateRow = inputAudRowIndex3;
                                }

                                if (updateMenuChoice == 1) {    //if update menu choice is 1, add tickets to order
                                    printAudToUser(updateFirst, updateCol); //print auditorium to user

                                    System.out.println("Enter your row choice");
                                    int rowChoice = 0;
                                    boolean isRowChoiceIsValid = false;
                                    while (isRowChoiceIsValid == false) {   //loops until row choice is valid
                                        try {
                                            rowChoice = scan.nextInt();
                                            if (rowChoice < 1 || rowChoice > updateRow) {   //if row choice is out of range, throw exception
                                                throw new IllegalArgumentException();
                                            }
                                            isRowChoiceIsValid = true;
                                        } catch (InputMismatchException e) {
                                            System.out.println("Invalid input, try again");
                                            scan.nextLine();
                                        } catch (IllegalArgumentException e) {
                                            System.out.println("Choice is out of range, try again");
                                        }
                                    }

                                    System.out.println("Enter your seat choice");
                                    char seatChoice = '!';
                                    char colInCharForm = (char) (updateCol + 64);
                                    boolean isSeatChoiceValid = false;
                                    while (isSeatChoiceValid == false) {    //loops until seat choice is valid
                                        try {
                                            seatChoice = scan.next().charAt(0);
                                            if (seatChoice < 'A' || seatChoice > colInCharForm) {   //if seat choice is not valid, throw exception
                                                throw new IllegalArgumentException();
                                            }
                                            isSeatChoiceValid = true;
                                        } catch (InputMismatchException e) {
                                            System.out.println("Invalid input, try again");
                                        } catch (IllegalArgumentException e) {
                                            System.out.println("Choice is out of range, try again");
                                        }
                                    }
                                    int seatChoiceIntForm = seatChoice - 65;    //calculate int form of seat choice

                                    System.out.println("How many adult tickets would you like");
                                    int adultTiks = 0;
                                    boolean isAdultTiksValid = false;
                                    while (isAdultTiksValid == false) { //loops until adult tickets are valid
                                        try {
                                            adultTiks = scan.nextInt();
                                            if (adultTiks < 0) {    //if tickets are out of range, throw exception
                                                throw new IllegalArgumentException();
                                            }
                                            isAdultTiksValid = true;
                                        } catch (InputMismatchException e) {
                                            System.out.println("Invalid input, try again");
                                        } catch (IllegalArgumentException e) {
                                            System.out.println("Invalid input, try again");
                                        }
                                    }

                                    System.out.println("How many child tickets would you like");
                                    int childTiks = 0;
                                    boolean isChildTiksValid = false;
                                    while (isChildTiksValid == false) { //loops until child tickets are valid
                                        try {
                                            childTiks = scan.nextInt();
                                            if (childTiks < 0) {    //if tickets are out of range, throw exception
                                                throw new IllegalArgumentException();
                                            }
                                            isChildTiksValid = true;
                                        } catch (InputMismatchException e) {
                                            System.out.println("Invalid input, try again");
                                        } catch (IllegalArgumentException e) {
                                            System.out.println("Invalid input, try again");
                                        }
                                    }

                                    System.out.println("How many senior tickets would you like");
                                    int seniorTiks = 0;
                                    boolean isSeniorTiksValid = false;
                                    while (isSeniorTiksValid == false) {    //loops until senior tickets are valid
                                        try {
                                            seniorTiks = scan.nextInt();
                                            if (seniorTiks < 0) {   //if tickets are ouf of range, throw exception
                                                throw new IllegalArgumentException();
                                            }
                                            isSeniorTiksValid = true;
                                        } catch (InputMismatchException e) {
                                            System.out.println("Invalid input, try again");
                                        } catch (IllegalArgumentException e) {
                                            System.out.println("Invalid input, try again");
                                        }
                                    }

                                    int totalTiks = adultTiks + childTiks + seniorTiks; //calculate total tickets
                                    boolean areSeatsPossible = checkIfSeatsPossible(updateFirst, totalTiks, rowChoice, seatChoiceIntForm);  //check if seats are possible
                                    if (areSeatsPossible == false) {
                                        System.out.println("no seats available");
                                    } else {
                                        boolean areSeatsAvailable = checkIfSeatsAvailable(updateFirst, totalTiks, rowChoice, seatChoiceIntForm);    //check if seats are available
                                        if (areSeatsAvailable == true) {    //if seats are available, reserve them
                                            updateFirst = addSeat(updateFirst, rowChoice, seatChoiceIntForm, seatChoice, adultTiks, childTiks, seniorTiks, audForUpdate, totalTiks, input, mapIndex, username);

                                            if (audForUpdate == 1) {    //update auditorium accordingly
                                                first1 = updateFirst;
                                            } else if (audForUpdate == 2) { //update auditorium accordingly
                                                first2 = updateFirst;
                                            } else {    //update auditorium accordingly
                                                first3 = updateFirst;
                                            }
                                            isUserUpdatingOrder = false;    //exit to the menu
                                        } else {    //if seats cant be reserved, return to update menu
                                            System.out.println("no seats available");
                                        }
                                    }
                                } else if (updateMenuChoice == 2) { //if update menu choice is 2, delete seat
                                    System.out.println("Which row is the seat to be removed in?");
                                    int removeRow = 0;
                                    boolean isRemoveRowValid = false;
                                    while (isRemoveRowValid == false) { //loops until row choice is valid
                                        try {
                                            removeRow = scan.nextInt();
                                            if (removeRow < 1 || removeRow > updateRow) {   //if row choice is out of range, throw exception
                                                throw new IllegalArgumentException();
                                            }
                                            isRemoveRowValid = true;
                                        } catch (InputMismatchException e) {
                                            System.out.println("Invalid input, try again");
                                            scan.nextLine();
                                        } catch (IllegalArgumentException e) {
                                            System.out.println("Choice is out of range, try again");
                                        }
                                    }

                                    System.out.println("Which column is the seat to be removed in?");
                                    char removeCol = '!';
                                    boolean isRemoveColValid = false;
                                    char colInCharForm = (char) (removeCol + 64);
                                    while (isRemoveColValid == false) { //loops until seat choice is valid
                                        try {
                                            removeCol = scan.next().charAt(0);
                                            if (removeCol < 'A' || removeCol > colInCharForm) { //if seat choice is out of range, throw exception
                                                throw new IllegalArgumentException();
                                            }
                                            isRemoveColValid = true;
                                        } catch (InputMismatchException e) {
                                            System.out.println("Invalid input, try again");
                                        } catch (IllegalArgumentException e) {
                                            System.out.println("Choice is out of range, try again");
                                        }
                                    }
                                    int removeSeatIntForm = removeCol - 65; //calculate int form of seat choice

                                    String removing = removeRow + "" + removeCol;   //create string for seat to be removed

                                    if (currOrder.contains(removing)) { //if order contains seat, remove it
                                        updateFirst = removeSeat(updateFirst, removeRow, removeSeatIntForm, removeCol, input, mapIndex, username);
                                        isUserUpdatingOrder = false;
                                    } else {    //if order does not contain seat, return to update menu
                                        System.out.println("Choice is not valid");
                                    }
                                } else {    //if update menu choice is 3, cancel order
                                    updateFirst = cancelOrder(updateFirst, input, mapIndex, username);
                                    isUserUpdatingOrder = false;
                                }
                            }
                        }
                    } else if(menuChoice == 4){ //if menu choice is 4, display receipt to customer
                        if(input.get(username).size() < 3){ //if no orders, display proper message and total
                            System.out.println("No orders");
                            System.out.println();
                            System.out.println("Customer Total: $0.00");
                        }
                        else{
                            double customerTotal = 0.0;
                            double orderTotal = 0.0;
                            for(int i = 2; i < input.get(username).size(); i++){    //loops until orders are done
                                String line = String.valueOf(input.get(username).get(i));
                                String[] order = line.split(";");   //splits order information

                                System.out.print(order[0] + ", ");
                                for(int j = 1; j < order.length; j++) { //loops until order is finished
                                    try{
                                        if(order[j+1].contains("adult")){   //if next info contains adult, print new line
                                            System.out.println(order[j]);
                                        }
                                        else if(order[j].contains("adult") || order[j].contains("child") || order[j].contains("senior")){
                                            if(!order[j].contains("senior")){
                                                System.out.print(order[j] + ", ");
                                            }
                                            else{   //if info contains senior, print new line
                                                System.out.println(order[j]);
                                            }
                                        }
                                        else{
                                            System.out.print(order[j] + ",");
                                        }
                                    }catch (ArrayIndexOutOfBoundsException e){
                                        System.out.println(order[j]);
                                    }
                                }
                                orderTotal = receiptTotal(input, i, username);  //calculate order total
                                customerTotal = customerTotal + orderTotal; //add order total to customer total
                                System.out.format("Order Total: $%.2f", orderTotal);
                                System.out.println();
                                System.out.println();
                            }
                            System.out.format("Customer Total: $%.2f", customerTotal);
                            System.out.println();
                        }
                    } else if(menuChoice == 5){ //if menu choice is 5, log out of system
                        isCustomerLoggedIn = false;
                    }
                } else{ //user is admin
                    System.out.println("1. Print Report\n2. Logout\n3. Exit");  //print menu to admin
                    int menuChoice = 0;
                    boolean isMenuChoiceValid = false;
                    while(isMenuChoiceValid == false){  //loops until menu choice is valid
                        try{
                            menuChoice = scan.nextInt();
                            if(menuChoice < 1 || menuChoice > 3){   //if menu choice is out of range, throw exception
                                throw new IllegalArgumentException();
                            }
                            isMenuChoiceValid = true;
                        } catch(InputMismatchException e){
                            System.out.println("Choice is not valid, try again");
                            scan.nextLine();
                        } catch(IllegalArgumentException e){
                            System.out.println("Choice is out of range, try again");
                        }
                    }

                    if(menuChoice == 1){    //if menu choice is 1, print report to admin
                        System.out.print("Auditorium 1\t");
                        int open1 = calculateOpenSeats(first1); //find open seats for auditorium 1
                        int reserved1 = calculateReservedSeats(first1); //find reserved seats for auditorium 1
                        int adult1 = calculateAdults(first1);   //find adults in auditorium 1
                        int child1 = calculateChildren(first1); //find children in auditorium 1
                        int senior1 = calculateSeniors(first1); //find seniors in auditorium 1
                        double total1 = calculateTotal(first1); //find total profit seats in auditorium 1
                        System.out.print(open1 + "\t" + reserved1 + "\t" + adult1 + "\t" + child1 + "\t" + senior1 + "\t"); //print info separated by tabs
                        System.out.format("$%.2f", total1); //format total profit
                        System.out.println();
                        System.out.print("Auditorium 2\t");
                        int open2 = calculateOpenSeats(first2); //find open seats for auditorium 2
                        int reserved2 = calculateReservedSeats(first2); //find reserved seats for auditorium 2
                        int adult2 = calculateAdults(first2);   //find adults in auditorium 2
                        int child2 = calculateChildren(first2); //find children in auditorium 2
                        int senior2 = calculateSeniors(first2); //find seniors in auditorium 2
                        double total2 = calculateTotal(first2); //find total profit seats in auditorium 2
                        System.out.print(open2 + "\t" + reserved2 + "\t" + adult2 + "\t" + child2 + "\t" + senior2 + "\t"); //print info separated by tabs
                        System.out.format("$%.2f", total2); //format total profit
                        System.out.println();
                        System.out.print("Auditorium 3\t");
                        int open3 = calculateOpenSeats(first3); //find open seats for auditorium 3
                        int reserved3 = calculateReservedSeats(first3); //find reserved seats for auditorium 3
                        int adult3 = calculateAdults(first3);   //find adults in auditorium 3
                        int child3 = calculateChildren(first3); //find children in auditorium 3
                        int senior3 = calculateSeniors(first3); //find seniors in auditorium 3
                        double total3 = calculateTotal(first3); //find total profit seats in auditorium 3
                        System.out.print(open3 + "\t" + reserved3 + "\t" + adult3 + "\t" + child3 + "\t" + senior3 + "\t"); //print info separated by tabs
                        System.out.format("$%.2f", total3); //format total profit
                        System.out.println();

                        int openTotal = open1 + open2 + open3;  //calculate total open seats
                        int reservedTotal = reserved1 + reserved2 + reserved3;  //calculate total reserved seats
                        int adultTotal = adult1 + adult2 + adult3;  //calculate total adult seats
                        int childTotal = child1 + child2 + child3;  //calculate total child seats
                        int seniorTotal = senior1 + senior2 + senior3;  //calculate total senior seats
                        double totalTotal = total1 + total2 + total3;   //calculate total profit
                        System.out.print("Total\t" + openTotal + "\t" + reservedTotal + "\t" + adultTotal + "\t" + childTotal + "\t" + seniorTotal + "\t"); //print info separated by tabe
                        System.out.format("$%.2f", totalTotal); //format total profit
                        System.out.println();
                    } else if(menuChoice == 2){ //if menu choice is 2, log out
                        isCustomerLoggedIn = false;
                    } else if(menuChoice == 3){ //if menu choice is 3, exit program fully
                        isProgramBeingUsed = false;
                        doesProgramNeedToExit = true;
                        break;
                    }
                }
            }
            if(doesProgramNeedToExit == true){  //if program needs to exit, break out of loop
                break;
            }
            scan.nextLine();
            canCustomerLogin = false;
            while(canCustomerLogin == false) {  //loops until customer can login
                System.out.println("Enter username");
                boolean isUsernameRight = false;
                while (isUsernameRight == false) {  //loops until username is valid
                    try {
                        username = scan.nextLine();
                        if (!input.containsKey(username)) { //if username is not in hashmap, throw exception
                            throw new IllegalArgumentException();
                        }
                        isUsernameRight = true;
                    } catch (IllegalArgumentException e) {
                        System.out.println("Username does not exist, try again");
                    }
                }

                System.out.println("Enter password");
                int wrongPass = 0;
                password = scan.nextLine();
                boolean isPasswordRight = false;
                while (isPasswordRight == false) {  //loops until password is valid
                    if (wrongPass == 2) {   //if password is wrong 3 times, start process over
                        wrongPass = 0;
                        System.out.println("Enter username");
                        isUsernameRight = false;
                        while (isUsernameRight == false) {
                            try {
                                username = scan.nextLine();
                                if (!input.containsKey(username)) {
                                    throw new IllegalArgumentException();
                                }
                                isUsernameRight = true;
                            } catch (IllegalArgumentException e) {
                                System.out.println("Username does not exist, try again");
                            }
                        }

                        System.out.println("Enter password");
                        password = scan.nextLine();
                    }
                    if (input.get(username).get(1).equals(password) == false) { //check if password can be found in hashmap
                        wrongPass++;
                        System.out.println("Invalid password, try again");
                        password = scan.nextLine();
                    } else {
                        isPasswordRight = true;
                    }
                }
                canCustomerLogin = true;
            }
        }
        while(doesFileExist == false) { //loops until file can be found
            try {
                FileOutputStream fileStream1 = new FileOutputStream("A1Final.txt");   //opens new output file
                PrintWriter outfile1 = new PrintWriter(fileStream1);  //prints in output file
                printToFile(first1, inputAudColIndex1, outfile1);   //prints new auditorium to proper file
                outfile1.close();
                doesFileExist = true;
            } catch (FileNotFoundException e){
                System.out.println("File not found");
            }
        }
        doesFileExist = false;

        while (doesFileExist == false) {
            try {
                FileOutputStream fileStream2 = new FileOutputStream("A2Final.txt");   //opens new output file
                PrintWriter outfile2 = new PrintWriter(fileStream2);  //prints in output file
                printToFile(first2, inputAudColIndex2, outfile2);   //prints new auditorium to proper file
                outfile2.close();
                doesFileExist = true;
            } catch (FileNotFoundException e){
                System.out.println("File not found");
            }
        }
        doesFileExist = false;

        while (doesFileExist == false) {
            try {
                FileOutputStream fileStream3 = new FileOutputStream("A3Final.txt");   //opens new output file
                PrintWriter outfile3 = new PrintWriter(fileStream3);  //prints in output file
                printToFile(first3, inputAudColIndex3, outfile3);   //prints new auditorium to proper file
                outfile3.close();
                doesFileExist = true;
            } catch (FileNotFoundException e){
                System.out.println("File not found");
            }
        }
    }
}
