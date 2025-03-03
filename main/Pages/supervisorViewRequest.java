package main.Pages;

import java.util.Scanner;

import main.Models.Request;
import main.Models.Supervisor;
import main.Models.requestStatus_ENUM;
import main.Utils.ConsoleColors;
import main.Utils.ConsoleUtils;
/**
 * supervisorViewRequest is a subclass of Page that allows supervisor to view request
 *
 * @author Dr. Heinz Doofenshmirtz
 * @version 1.0
 * @since 13-4-2023
 */
public class supervisorViewRequest extends Page{
    private Supervisor staff;
    private Scanner sc = new Scanner(System.in);

    /**
     * supervisorViewRequest constructor
     *
     */
    public supervisorViewRequest(Page previousPage, Supervisor staff) {
        super(previousPage);
        this.staff = staff;
    }

    /**
     * Main executable for supervisorViewRequest page
     * @return next page {@link main.Pages.StaffMain)
     */
    @Override
    public Page executable() {
        ConsoleUtils.clearScreen();

        // print requests
        System.out.println("╔══════════════════════════════════════════════════════════════════════════════════════════════════════╗");
        System.out.println("║                  ██████╗ ███████╗ ██████╗ ██╗   ██╗███████╗███████╗████████╗███████╗                 ║");
        System.out.println("║                  ██╔══██╗██╔════╝██╔═══██╗██║   ██║██╔════╝██╔════╝╚══██╔══╝██╔════╝                 ║");
        System.out.println("║                  ██████╔╝█████╗  ██║   ██║██║   ██║█████╗  ███████╗   ██║   ███████╗                 ║");
        System.out.println("║                  ██╔══██╗██╔══╝  ██║▄▄ ██║██║   ██║██╔══╝  ╚════██║   ██║   ╚════██║                 ║");
        System.out.println("║                  ██║  ██║███████╗╚██████╔╝╚██████╔╝███████╗███████║   ██║   ███████║                 ║");
        System.out.println("║                  ╚═╝  ╚═╝╚══════╝ ╚══▀▀═╝  ╚═════╝ ╚══════╝╚══════╝   ╚═╝   ╚══════╝                 ║");
        System.out.println("╠══════════════════╦══════════════════════════════════════════════════════════════════════╦════════════╣");
        System.out.println("║ID                ║Request Type                                                          ║Status      ║");

        for(Request r : this.staff.getRequests()){
            if(r.getRequestType().equals("3") && r.getRequestStatus().equals(requestStatus_ENUM.PENDING)){
                System.out.println("╠══════════════════╬══════════════════════════════════════════════════════════════════════╬════════════╣");
                System.out.printf("║%-18.18s║%-70.70s║%-12.12s║ %-10s\n", r.getRequestID(), "Request for Project Title Change", r.getRequestStatus().toString(), r.getRequestStatus().equals(requestStatus_ENUM.PENDING)?ConsoleColors.BLUE_BOLD_BRIGHT+"NEW!"+ConsoleColors.RESET:"");
            }
        }
        System.out.println("╚══════════════════╩══════════════════════════════════════════════════════════════════════╩════════════╝");
        
        // ask for requestID to view
        String requestID;
        Request r;
        while(true){
            System.out.print("Enter requestID to process(empty to return): ");
            requestID = sc.nextLine().trim();

            // return if blank
            if(requestID.isBlank()){
                return this.getPreviousPage();
            }

            // check if request exist
            r = staff.getRequestbyID(requestID);
            if(r == null){
                System.out.println("Invalid requestID");
                continue;
            }

            // check if is type 3 request
            if(!r.getRequestType().equals("3")){
                System.out.println("Cannot access this request");
                continue;
            }
            
            // check if is PENDING
            if(!r.getRequestStatus().equals(requestStatus_ENUM.PENDING)){
                System.out.println("Cannot access this request");
                continue;
            }
            break;
        }
        
        // print request info
        ConsoleUtils.clearScreen();
        String currentTitle = staff.getProjectbyID(r.getProjectID()).getTitle();
        r.printInfo(currentTitle, null, null);

        // process request
        String action;
        while(true) {
            System.out.print("Choose \u001B[32mAPPROVE\u001B[0m or \u001B[31mREJECT\u001B[0m for the request(blank for return): ");
            action = sc.nextLine();

            if (action.isBlank()) {
                return this.getPreviousPage();
            }
            else if(action.equals("APPROVE") || action.equals("REJECT")){break;}
        }

        r.processRequest(action);

        System.out.print("Enter any input to return: ");
        String hold = sc.nextLine();
        return this.getPreviousPage();


    }
    

}
