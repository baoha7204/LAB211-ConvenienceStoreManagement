package utils;

import business_objects.ProductType;
import business_objects.Trade;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

public class Validation implements IValidation {

    private Scanner sc = new Scanner(System.in);
    
    @Override
    public String checkString(String msg, Status status) {
        while (true) {
            System.out.println(msg);
            String rawInput = sc.nextLine().trim();
            if(status == Status.UPDATE && rawInput.isEmpty()){
                return rawInput;
            }
            // input = null || length = 0 => Error
            if (rawInput == null || rawInput.isEmpty()) {
                System.err.println("Must input a string not empty! Please enter again.");
                continue;
            }
            return rawInput;
        }
    }

    @Override
    public int checkInt(String msg, int min, int max, Status status) {
        while (true) {
            String rawInput = checkString(msg, status);
            if (rawInput.isEmpty() && status == Status.UPDATE) {
                return -1;
            }
            try {
                // wrong format of Integer Error
                int input = Integer.parseInt(rawInput);
                // out of range Error
                if (input < min || input > max) {
                    System.err.println("Must input a number from " + min + " to " + max);
                    continue;
                }
                return input;
            } catch (NumberFormatException e) {
                System.err.println("Must enter a number");
            }
        }
    }

    @Override
    public double checkDouble(String msg, double min, double max, Status status) {
        while (true) {
            String rawInput = checkString(msg, status);
            if (rawInput.isEmpty() && status == Status.UPDATE) {
                return -1;
            }
            try {
                // wrong format of Double Error
                double input = Double.parseDouble(rawInput);
                // out of range Error
                if (input < min || input > max) {
                    System.err.println("Must input a number from " + min + "to " + max);
                    continue;
                }
                return input;
            } catch (NumberFormatException e) {
                System.err.println("Must enter a number");
            }

        }
    }

    @Override
    public boolean checkYesOrNo(String msg) {
        while (true) {
            String input = checkString(msg, Status.UPDATE);
            if (input.equalsIgnoreCase("Y")) {
                return true;
            } else if (input.equalsIgnoreCase("N")) {
                return false;
            } else {
                System.err.println("Must input Y or N to select option");
            }
        }
    }

    @Override
    public Date checkBeforeDate(String msg, Status status) {
        String pattern = "dd/MM/yyyy";
        DateFormat sdf = new SimpleDateFormat(pattern);
        sdf.setLenient(false);
        while (true) {
            String dateStr = checkString(msg, status);
            if(status == Status.UPDATE && dateStr.isEmpty()){
                return null;
            }
            try {
                Date date = sdf.parse(dateStr);
                return date;
            } catch (ParseException e) {
                System.err.println("Incorrect date! Please enter again.");
            }
        }

    }

    @Override
    public Date checkAfterDate(String msg, Date productionDate, Status status) {
        while (true) {
            Date expiredDate = checkBeforeDate(msg, status);
            if(status == Status.UPDATE && expiredDate == null){
                return null;
            }
            if (expiredDate.before(productionDate)) {
                System.err.println("Expiration date must after production date! Please enter again.");
                continue;
            }
            return expiredDate;
        }
    }

    @Override
    public ProductType checkProductType(String msg, Status status) {
        while(true){
            String type = checkString(msg, status);
            if(status == Status.UPDATE && type.isEmpty()){
                return null;
            }
            if(type.equalsIgnoreCase("DAILY")){
                return ProductType.DAILY;
            } else if(type.equalsIgnoreCase("LONG")){
                return ProductType.LONG;
            } else{
                System.err.println("Must input 1 in 2 type product is 'Daily' or 'Long'! Please input again.");
            }
        }
    }

    @Override
    public Trade checkTradeType(String msg, Status status) {
        while(true){
            String type = checkString(msg, status);
            if(status == Status.UPDATE && type.isEmpty()){
                return null;
            }
            if(type.equalsIgnoreCase("IMPORT")){
                return Trade.IMPORT;
            } else if(type.equalsIgnoreCase("EXPORT")){
                return Trade.EXPORT;
            } else{
                System.err.println("Must input 1 in 2 trade type is 'Import' or 'Export'! Please input again.");
            }
        }
    }
}
