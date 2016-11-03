package com.alex;


import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.Locale;

public class TicketManagerGUI extends JFrame {
    //region all variables
    private JButton addTicketButton;
    private JTextField descriptionTextField;
    private JTextField reporterTextField;
    private JPanel rootPanel;
    private JList<Ticket> activeTicketList;
    private JButton deleteTicketButton;
    private JTextField resolutionTextField;
    private JComboBox<Integer> priorityComboBox;
    private DefaultListModel<Ticket> ticketListModel;
    private LinkedList<Ticket> resolvedTickets;
    //endregion


    public TicketManagerGUI() {
        //region gui setup
        super("Ticket Manager");
        setContentPane(rootPanel);
        pack();
        setSize(new Dimension(800, 500));
        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        setVisible(true);

        //endregion


        //region adding numbers to combobox
        priorityComboBox.addItem(1);
        priorityComboBox.addItem(2);
        priorityComboBox.addItem(3);
        priorityComboBox.addItem(4);
        priorityComboBox.addItem(5);
        //endregion


        ticketListModel = new DefaultListModel<Ticket>();
        resolvedTickets = new LinkedList<>();
        activeTicketList.setModel(ticketListModel);
        activeTicketList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        ArrayList<String> ticketLine = new ArrayList<>();
        //if there are open tickets, restore the ticket info strings back into the program
        try (BufferedReader bufReader = new BufferedReader(new FileReader("open_tickets.txt"))) {
            String line = bufReader.readLine();
            while (line != null) {
                ticketLine.add(line);
                line = bufReader.readLine();
            }
            bufReader.close();

        } catch (IOException ioe) {
            System.out.println("Unable to find previous open tickets");
            System.out.println(ioe.toString());
        }

        //parse the strings and restore each ticket object
        for (String s : ticketLine) {
            String[] ticketInfo = s.split(";");
            //format the date info back to a date. method taken from http://stackoverflow.com/questions/4496359/how-to-parse-date-string-to-date
            try {
                String target = ticketInfo[4];
                DateFormat df = new SimpleDateFormat("EEE MMM dd kk:mm:ss zzz yyyy", Locale.ENGLISH);
                Date ticketDate = df.parse(target);

                addTicketInPriorityOrder(ticketListModel, new Ticket(Integer.parseInt(ticketInfo[0]), ticketInfo[1], Integer.parseInt(ticketInfo[2]), ticketInfo[3], ticketDate));

            } catch (ParseException pe) {
                System.out.println(pe.toString());
            }
        }


            addTicketButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String description = descriptionTextField.getText();
                    String reporter = reporterTextField.getText();
                    Date dateReported = new Date(); //Default constructor creates date with current date/time
                    int priority = (int) priorityComboBox.getSelectedItem();

                    Ticket t = new Ticket(description, priority, reporter, dateReported);

                    addTicketInPriorityOrder(ticketListModel, t);
                }
            });

            deleteTicketButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if(!activeTicketList.isSelectionEmpty()) {
                        Ticket toDelete = activeTicketList.getSelectedValue();
                        toDelete.setResolution(new Date(), resolutionTextField.getText());

                        int confirm = JOptionPane.showConfirmDialog(TicketManagerGUI.this, "You are going to delete" + toDelete.getDescription() +
                                        "\n with a resolution of " + toDelete.getResolution()
                                , "Confirm Deletion", JOptionPane.OK_CANCEL_OPTION);

                        if (confirm == JOptionPane.OK_OPTION) {
                            resolvedTickets.add(toDelete);
                            ticketListModel.removeElement(toDelete);
                        }
                    }

                }
            });
            //thanks to this link for explaining how to run code before a program closes: http://stackoverflow.com/questions/16372241/run-function-on-jframe-close
            addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    //save all open tickets not dealt with in this session
                    try (BufferedWriter bufWriter = new BufferedWriter(new FileWriter("open_tickets.txt"))) {
                        for (int i = 0; i < ticketListModel.size(); i++) {
                            bufWriter.write(ticketListModel.get(i).toStringToFile() + "\n");
                        }
                        bufWriter.close();
                    } catch (IOException ioe) {
                        System.out.println(ioe.toString());
                    }

                    Date today = new Date();
                    String todayString = today.toString().replace(" ", "_").replace(":", "_");

                    //save all resolved tickets from this session
                    if (resolvedTickets.size() > 0) {
                        try (BufferedWriter bufWriter = new BufferedWriter(new FileWriter("resolved_tickets_as_of_" + todayString + ".txt"))) {
                            for (Ticket t : resolvedTickets) {
                                bufWriter.write(t.toStringToFile() + "\n");
                            }
                            bufWriter.close();
                        } catch (IOException ioe) {
                            System.out.println(ioe.toString());
                        }
                    }
                    dispose();
                    System.exit(0);
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
