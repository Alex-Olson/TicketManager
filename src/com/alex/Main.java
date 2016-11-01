package com.alex;

import java.io.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;

public class Main {


    static Scanner numberScanner = new Scanner(System.in);

    public static void main(String[] args) {
        TicketManagerGUI gui = new TicketManagerGUI();
    }
/*

        LinkedList<Ticket> ticketQueue = new LinkedList<Ticket>();
        LinkedList<Ticket> resolvedTickets = new LinkedList<>();
        ArrayList<String> ticketLine = new ArrayList<>();
        //if there are open tickets, restore the ticket info strings back into the program
        try (BufferedReader bufReader = new BufferedReader(new FileReader("open_tickets.txt"))){
            String line = bufReader.readLine();
            while (line != null) {
                ticketLine.add(line);
                line = bufReader.readLine();
            }
            bufReader.close();

        } catch (IOException ioe){
            System.out.println("Unable to find previous open tickets");
            System.out.println(ioe.toString());
        }
        //parse the strings and restore each ticket object
        for (String s : ticketLine){
            String[] ticketInfo = s.split(";");
            //format the date info back to a date. method taken from http://stackoverflow.com/questions/4496359/how-to-parse-date-string-to-date
            try {
                String target = ticketInfo[4];
                DateFormat df = new SimpleDateFormat("EEE MMM dd kk:mm:ss zzz yyyy", Locale.ENGLISH);
                Date ticketDate =  df.parse(target);

                addTicketInPriorityOrder(ticketQueue, new Ticket(Integer.parseInt(ticketInfo[0]), ticketInfo[1], Integer.parseInt(ticketInfo[2]), ticketInfo[3], ticketDate));

            } catch (ParseException pe) {
                System.out.println(pe.toString());
            }




        }

        Scanner scan = new Scanner(System.in);
        while(true){
            System.out.println("1. Enter Ticket\n" +
                    "2. Delete Ticket By Id\n" +
                    "3. Delete Ticket By Issue\n" +
                    "4. Search By Name\n" +
                    "5. Display All Tickets\n" +
                    "6. Quit");
            int task = intInput();
            if (task == 1) {
//Call addTickets, which will let us enter any number of new tickets
                addTickets(ticketQueue);
            } else if (task == 2) {
//delete a ticket by id
                printAllTickets(ticketQueue); //display list for user
                System.out.println("Enter ID of ticket to delete");
                int deleteID = intInput();
                resolvedTickets.add(deleteTicketByID(ticketQueue, deleteID));
            } else if (task == 3){
                //delete ticket by issue
                resolvedTickets.add(deleteTicketByIssue(ticketQueue));
            } else if (task == 4){
                //search by name
                searchTicketByName(ticketQueue);

            }
            else if ( task == 6 ) {
//Quit. Future prototype may want to save all tickets to a file
                System.out.println("Quitting program");
                //save all open tickets to a file
                try (BufferedWriter bufWriter = new BufferedWriter(new FileWriter("open_tickets.txt"))){
                    for(Ticket t : ticketQueue){
                        bufWriter.write(t.toStringToFile() + "\n");
                    }
                    bufWriter.close();
                } catch (IOException ioe){
                    System.out.println(ioe.toString());
                }


                Date today = new Date();
                String todayString = today.toString().replace(" ", "_").replace(":", "_");

                //save all resolved tickets from this session
                try (BufferedWriter bufWriter = new BufferedWriter(new FileWriter("resolved_tickets_as_of_" + todayString + ".txt"))){
                    for(Ticket t : resolvedTickets){
                        bufWriter.write(t.toStringToFile() + "\n");
                    }
                    bufWriter.close();
                } catch (IOException ioe){
                    System.out.println(ioe.toString());
                }
                break;
            }
            else {
//this will happen for 5 or any other selection that is a valid int

//Default will be print all tickets
                printAllTickets(ticketQueue);
            }
        }
        scan.close();
        numberScanner.close();
    }

    protected static Ticket deleteTicketByID(LinkedList<Ticket> ticketQueue, int ticketID) {

        if (ticketQueue.size() == 0) { //no tickets!
            System.out.println("No tickets to delete!\n");
            return null;
        }
        String resolution;
        Ticket resolvedTicket = new Ticket();

        boolean found = false;
        //Loop over all tickets. Delete the one with this ticket ID
        while (found == false) {

            for (Ticket ticket : ticketQueue) {
                if (ticket.getTicketID() == ticketID) {
                    found = true;
                    resolvedTicket = ticket;
                    Scanner sc = new Scanner(System.in);
                    System.out.println("Please enter the resolution for this ticket.");
                    resolution = sc.nextLine();

                    resolvedTicket.setResolution(new Date(), resolution);
                    ticketQueue.remove(ticket);

                    System.out.println(String.format("Ticket %d deleted", ticketID));

                    break; //don't need loop any more.
                }
            }
            //if the ticket id isn't found, the user will be asked to redo the deletion
            if (!found) {
                System.out.println("Ticket ID not found, no ticket deleted");
            }
        }


        printAllTickets(ticketQueue); //print updated list
        return resolvedTicket;

    }

    protected static Ticket deleteTicketByIssue(LinkedList<Ticket> ticketQueue){

        LinkedList<Ticket> searchedTickets = searchTicketByName(ticketQueue);
        Ticket resolvedTicket = new Ticket();

        System.out.println("Enter the ID of the ticket you wanted to delete.");
        int deleteID = intInput();

        for (Ticket t : searchedTickets){
            if (deleteID == t.getTicketID()){
                resolvedTicket = deleteTicketByID(ticketQueue, deleteID);
                break;
            }
        }
        return resolvedTicket;
    }

    protected static LinkedList<Ticket> searchTicketByName(LinkedList<Ticket> ticketQueue){
        LinkedList<Ticket> foundTickets = new LinkedList<>();
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter search term");
        String searchTerm = sc.nextLine();

        for (Ticket t : ticketQueue){
            if (t.getDescription().contains(searchTerm)){
                foundTickets.add(t);
            }
        }

        System.out.println("List of found tickets:");
        for (Ticket t : foundTickets){
            System.out.println(t.toString());
        }

        return foundTickets;
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
            System.out.println("Enter priority of " + description + " (1 (meh)- 5 (critical)");
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
    */

}
