package com.alex;


import java.util.Date;

public class Ticket {

    private int priority;
    private String reporter;
    private String description;
    private Date dateReported;
    private Date resolutionDate;
    private String resolution;

    private static int staticTicketIDCounter = 1;
    protected int ticketID;

    public Ticket(){}
    //constructor for new ticket
    public Ticket(String desc, int p, String rep, Date date){
        this.description = desc;
        this.priority = p;
        this.reporter = rep;
        this.dateReported = date;
        this.ticketID = staticTicketIDCounter;
        staticTicketIDCounter++;
    }
    //constructor for open ticket restored from file
    public Ticket(int ticketID, String desc, int p, String rep, Date date){
        this.description = desc;
        this.priority = p;
        this.reporter = rep;
        this.dateReported = date;
        this.ticketID = ticketID;
        //when you restore a Ticket, if the id is equal to or higher than the ticket counter, make the static counter that number +1.
        if (ticketID >= staticTicketIDCounter){
            staticTicketIDCounter = ticketID + 1;
        }
    }

    public void setResolution(Date resolutionDate, String resolution){
        this.resolutionDate = resolutionDate;
        this.resolution = resolution;
    }

    protected int getPriority() {
        return priority;
    }

    public String getReporter() {
        return reporter;
    }

    public String getDescription() {
        return description;
    }

    public String getResolution() {
        return resolution;
    }

    public Date getDateReported() {
        return dateReported;
    }

    public static int getStaticTicketIDCounter() {
        return staticTicketIDCounter;
    }

    public int getTicketID() {
        return ticketID;
    }



    @Override
    public String toString(){
        return("ID = " + this.ticketID + " Issue: " + this.description + " Priority: " + this.priority + " Reported by: "
                + this.reporter + " Reported on: " + this.dateReported);
    }
    //string to write to file for easy splitting
    public String toStringToFile(){
        String ticketString = this.ticketID + ";" + this.description + ";" + this.priority + ";"
                + this.reporter + ";" + this.dateReported;

        if (resolution != null){
            ticketString += ";" + this.resolution + ";" + this.resolutionDate;
        }

        return ticketString;
    }

}
