package com.alex;

import java.util.Date;
import java.util.InputMismatchException;
import java.util.LinkedList;
import java.util.Scanner;

public class Main {
    static Scanner numberScanner = new Scanner(System.in);

    public static void main(String[] args) {


        LinkedList<Ticket> ticketQueue = new LinkedList<Ticket>();
        Scanner scan = new Scanner(System.in);
        while(true){
            System.out.println("1. Enter Ticket\n2. Delete Ticket\n3. Display All Tickets\n4. Quit");
            int task = intInput();
            if (task == 1) {
//Call addTickets, which will let us enter any number of new tickets
                addTickets(ticketQueue);
            } else if (task == 2) {
//delete a ticket
                deleteTicket(ticketQueue);
            } else if ( task == 4 ) {
//Quit. Future prototype may want to save all tickets to a file
                System.out.println("Quitting program");
                break;
            }
            else {
//this will happen for 3 or any other selection that is a valid int
//TODO Program crashes if you enter anything else - please fix
//Default will be print all tickets
                printAllTickets(ticketQueue);
            }
        }
        scan.close();
    }

    protected static void deleteTicket(LinkedList<Ticket> ticketQueue) {
        printAllTickets(ticketQueue); //display list for user
        if (ticketQueue.size() == 0) { //no tickets!
            System.out.println("No tickets to delete!\n");
            return;
        }

        boolean found = false;
        //Loop over all tickets. Delete the one with this ticket ID
        while (found == false) {
            System.out.println("Enter ID of ticket to delete");
            int deleteID = intInput();

        for (Ticket ticket : ticketQueue) {
            if (ticket.getTicketID() == deleteID) {
                found = true;
                ticketQueue.remove(ticket);
                System.out.println(String.format("Ticket %d deleted", deleteID));
                break; //don't need loop any more.
            }
        }
            //if the ticket id isn't found, the user will be asked to redo the deletion
            if (!found) {
                System.out.println("Ticket ID not found, no ticket deleted");
            }
        }
        printAllTickets(ticketQueue); //print updated list
    }

    protected static void addTickets(LinkedList<Ticket> ticketQueue) {
        Scanner sc = new Scanner(System.in);
        boolean moreProblems = true;
        String description, reporter;
        Date dateReported = new Date(); //Default constructor creates date with current date/time
        int priority;
        while (moreProblems){
            System.out.println("Enter problem");
            description = sc.nextLine();
            System.out.println("Who reported this issue?");
            reporter = sc.nextLine();
            System.out.println("Enter priority of " + description);
            priority = intInput();
            Ticket t = new Ticket(description, priority, reporter, dateReported);
            //ticketQueue.add(t);
            addTicketInPriorityOrder(ticketQueue, t);
            printAllTickets(ticketQueue);
            System.out.println("More tickets to add?");
            String more = sc.nextLine();
            if (more.equalsIgnoreCase("N")) {
                moreProblems = false;
            }
        }
    }
    protected static void addTicketInPriorityOrder(LinkedList<Ticket> tickets, Ticket newTicket){
        //Logic: assume the list is either empty or sorted
        if (tickets.size() == 0 ) {//Special case - if list is empty, add ticket and return
            tickets.add(newTicket);
            return;
        }
        //Tickets with the HIGHEST priority number go at the front of the list. (e.g. 5=server on fire)
        //Tickets with the LOWEST value of their priority number (so the lowest priority) go at the end
        int newTicketPriority = newTicket.getPriority();
        for (int x = 0; x < tickets.size() ; x++) { //use a regular for loop so we know which element we are looking at
            //if newTicket is higher or equal priority than the this element, add it in front of this one, and return
            if (newTicketPriority >= tickets.get(x).getPriority()) {
                tickets.add(x, newTicket);
                return;
            }
        }
        //Will only get here if the ticket is not added in the loop
        //If that happens, it must be lower priority than all other tickets. So, add to the end.
        tickets.addLast(newTicket);
    }
    protected static void printAllTickets(LinkedList<Ticket> tickets) {
        System.out.println(" ------- All open tickets ----------");
        for (Ticket t : tickets ) {
            System.out.println(t); //Write a toString method in Ticket class
//println will try to call toString on its argument
        }
        System.out.println(" ------- End of ticket list ----------");
    }

    //everything past this line was copy/pasted from https://github.com/minneapolis-edu/Java2545examples/blob/master/src/week3_methods/Validation.java

    // A variant of the method below - notice it calls intInput with null as the argument
    public static int intInput() {
        return intInput(null);
    }

    //Takes a question, asks user the question, checks to make sure user enters an int, and
    //then returns that int to the calling method.
    public static int intInput(String question) {

        while (true) {
            // If user has provided a question, then print it for the user
            if (question != null) {
                System.out.println(question);
            }

            //Try to read what the user typed as an int.
            try {
                // If the input can be read as a int, that int will be returned
                // This ends the loop, and this method, and control returns to the calling method.
                return numberScanner.nextInt();

            } // if the input can't be read as an int, then an error will be raised.
            // For example, if the user enters 'ten' or 1.4 or 123456543454343434, these are not ints, so will cause an error.
            // That error can be 'caught' by this code, and we can print an error message.
            // Since we are inside a while loop, then the loop can repeat and ask the user for input again.
            catch (InputMismatchException ime) {
                System.out.println("Error - please enter an integer number");
                numberScanner.next();   //Clear any other characters from the Scanner
            }
        }

    }
}
