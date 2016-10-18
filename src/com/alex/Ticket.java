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

    public Ticket(String desc, int p, String rep, Date date){
        this.description = desc;
        this.priority = p;
        this.reporter = rep;
        this.dateReported = date;
        this.ticketID = staticTicketIDCounter;
        staticTicketIDCounter++;
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
        return("ID = " + this.ticketID + " Issued: " + this.description + " Priority: " + this.priority + " Reported by: "
                + this.reporter + " Reported on: " + this.dateReported);
    }

}
