package com.alex;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.Locale;

public class TicketManagerGUI extends JFrame {
    private JButton addTicketButton;
    private JTextField descriptionTextField;
    private JTextField reporterTextField;
    private JPanel rootPanel;
    private JList<Ticket> activeTicketList;
    private JButton deleteTicketButton;
    private JTextField resolutionTextField;
    private JComboBox<Integer> priorityComboBox;

    DefaultListModel<Ticket> ticketListModel;






    public TicketManagerGUI(){
        super("Ticket Manager");
        setContentPane(rootPanel);
        pack();
        setSize(new Dimension(800, 500));
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setVisible(true);

        priorityComboBox.addItem(1);
        priorityComboBox.addItem(2);
        priorityComboBox.addItem(3);
        priorityComboBox.addItem(4);
        priorityComboBox.addItem(5);

        ticketListModel = new DefaultListModel<Ticket>();
        activeTicketList.setModel(ticketListModel);
        activeTicketList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);


        LinkedList<Ticket> resolvedTickets = new LinkedList<>();



        addTicketButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String description = descriptionTextField.getText();
                String reporter = reporterTextField.getText();
                Date dateReported = new Date(); //Default constructor creates date with current date/time
                int priority = (int)priorityComboBox.getSelectedItem();

                    Ticket t = new Ticket(description, priority, reporter, dateReported);

                    addTicketInPriorityOrder(ticketListModel, t);
            }

        });
        deleteTicketButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Ticket toDelete = activeTicketList.getSelectedValue();
                toDelete.setResolution(new Date(), resolutionTextField.getText());
                resolvedTickets.add(toDelete);
                ticketListModel.removeElement(toDelete);

            }
        });
    }


    protected static void addTicketInPriorityOrder(DefaultListModel<Ticket> tickets, Ticket newTicket){
        //Logic: assume the list is either empty or sorted
        if (tickets.size() == 0 ) {//Special case - if list is empty, add ticket and return
            tickets.add(0, newTicket);
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
        tickets.addElement(newTicket);
    }
}
