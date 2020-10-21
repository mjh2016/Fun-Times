import java.time.LocalDate;

public class funtimes
{
    public static void main(String[] args)
    {
        String[] StockItem_Product = {"soup", "bread", "milk", "apples"};
        String[] StockItem_Product_Single = {"soup", "bread", "milk", "apple"};
        String[] StockItem_Unit = {"tin", "loaf", "bottle", "single"};
        String[] StockItem_Unit_Plural = {"tins", "loaves", "bottles", ""};
        double[] StockItem_Cost = {0.65, 0.80, 1.30, 0.10};

        Integer[] OrderList = {0, 0, 0, 0};

        String[] PreambleString = {"Price", "a", "basket", "containing:"};
        int PreambleString_Pass = 0;

        // System.out.println("Test Shopping List\n");

        LocalDate d = LocalDate.now();
        LocalDate endOfMonth = d.plusMonths(1).withDayOfMonth(1).minusDays(1);
        LocalDate endOfNextMonth = d.plusMonths(2).withDayOfMonth(1).minusDays(1);

        // System.out.println(d.getMonth());
        // System.out.println("The last day of this month is: " + endOfMonth.getDayOfMonth());
        // System.out.println("The last day of the next month is: " + endOfNextMonth.getDayOfMonth());

        int pdom = d.getDayOfMonth();
        int eom = endOfMonth.getDayOfMonth();
        int eonm = endOfNextMonth.getDayOfMonth();

        int runofdays =  (eom - pdom) + eonm;

        // System.out.println("INT The last day of this month is: " + Integer.toString(eom));
        // System.out.println("INT The last day of the next month is: " + Integer.toString(eonm));
        // System.out.println("INT run of days is: " + Integer.toString(runofdays));

        int i =0;
        while(i < args.length)
        {
            String s = args[i];
            // System.out.println(Integer.toString(i) + ") " + s);
            i++;
        }

        i = 0;
        if (args.length >= 4)
        {
            PreambleString_Pass = 1;
            while(i < 4)
            {
                String s = args[i];
                if (s.toLowerCase().equals(PreambleString[i].toLowerCase()) == false)
                    PreambleString_Pass = 0;
                // System.out.println(Integer.toString(i) + ") " + Integer.toString(PreambleString_Pass) + " " + s);
                i++;
            }


            // an apple + ["," or "and"]
            // 3 apples
            // a {"tin", "loaf", "bottle"} of {"soup", "bread", "milk"}
            // 3 {"tins", "loaves", "bottles"} of {"soup", "bread", "milk"}

            int state_var = 0; // 0 -> Quantity; 
                            // 1 -> Single Not Apple; 
                            // 2 -> Single Apple; 
                            // 3 -> Plural; 
                            // 4 -> Single Not Apple "of" detection state; 
                            // 5 -> Single Not Apple Product Single detection state;
                            // 6 -> Comma Coupling state
                            // 7 -> "And" state
                            // 8 -> Post "Bought" state 
                            // 9 -> Post "in" state
                            // 10 -> "Days" state
                            // 11 -> "Time" state 
                            // 12 -> Plural Not Apple "of" detection state; 
                            // 13 -> Plural Not Apple Product Plural detection state;
                            // 101 -> Error
            int quantity = 0;
            int product = 0;
            int days = 0;

            if ((PreambleString_Pass == 1) && (args.length >= 5))
            {
                String s = "";
                String s_minus_comma = "";
                String Test_a = "a";
                String Test_an = "an";
                String Test_of = "of";
                String Test_bought = "bought";
                String Test_today = "today";
                String Test_in = "in";
                String Test_days = "days";
                String Test_time = "time";
                String Test_and = "and";

                int endingComma = 0;
                
                while(i < args.length)
                {
                    s = args[i];
                    endingComma = 0;
                    if (s.length() > 1)
                    {
                        if (s.substring(s.length() - 1).equals(","))
                        {
                            endingComma = 1;
                            s_minus_comma = s.substring(0, s.length() - 1);
                        }
                        else
                        {
                            s_minus_comma = s;
                        }
                    }
                    else
                    {
                        s_minus_comma = s;
                    }

                    // System.out.println(Integer.toString(i) + ") state_var = " + Integer.toString(state_var) + " " + s);

                    // 0 -> Quantity; 
                    if (state_var == 0)
                    {
                        if (s.toLowerCase().equals(Test_a.toLowerCase()) == true)
                        {
                            state_var = 1;
                            quantity = 1;
                        }
                        else if (s.toLowerCase().equals(Test_an.toLowerCase()) == true)
                        {
                            state_var = 2;
                            quantity = 1;
                        }
                        else if (isInteger(s) == true)
                        {
                            state_var = 3;
                            quantity = Integer.parseInt(s);
                        }
                        else
                        {
                            state_var = 101;
                            quantity = 0;
                        }
                    }
                    // 1 -> Single Not Apple;
                    else if (state_var == 1)
                    {
                        int StockItem_Unit_Successful = 0;
                        for (int siu = 0; siu < 4; siu++)
                        {
                            if (s.toLowerCase().equals(StockItem_Unit[siu].toLowerCase()) == true)
                            {
                                StockItem_Unit_Successful = 1;
                                product = siu;
                            }
                            ;
                        }

                        if (StockItem_Unit_Successful == 1)
                            state_var = 4;
                        else
                            state_var = 101;
                    }
                    // 2 -> Single Apple;
                    else if (state_var == 2)
                    {
                        // System.out.println("Test Point for state_var = " + Integer.toString(state_var) + "\n");
                        // System.out.println("s_minus_comma:\n" + s_minus_comma + "\n\n");
                        
                        if (s_minus_comma.toLowerCase().equals(StockItem_Product_Single[3].toLowerCase()) == true)
                        {
                            // System.out.println("Single Apple Case\n");
                            if (endingComma == 1)
                                state_var = 6;
                            else
                                state_var = 7;
                            product = 3;
                            quantity = 1;
                            OrderList[product] += quantity;
                        }
                        ;
                    }
                    // 3 -> Plural;
                    else if (state_var == 3)
                    {
                        if (s_minus_comma.toLowerCase().equals(StockItem_Product[3].toLowerCase()) == true)
                        {
                            if (endingComma == 1)
                                state_var = 6;
                            else
                                state_var = 7;
                            product = 3;
                            OrderList[product] += quantity;
                        }
                        else
                        {
                            int StockItem_Unit_Plural_Successful = 0;
                            for (int siup = 0; siup < 4; siup++)
                            {
                                if (s.toLowerCase().equals(StockItem_Unit_Plural[siup].toLowerCase()) == true)
                                {
                                    StockItem_Unit_Plural_Successful = 1;
                                    product = siup;
                                }
                                ;
                            }
                            
                            if (StockItem_Unit_Plural_Successful== 1)
                                state_var = 12;
                            else
                                state_var = 101;
                        }

                        
                        ;
                    }
                    // 4 -> Single Not Apple "of" detection state;
                    else if (state_var == 4)
                    {
                        if (s.toLowerCase().equals(Test_of.toLowerCase()) == true)
                            state_var = 5;
                        else
                            state_var = 101;
                    }
                    // 5 -> Single Not Apple Product Single detection state;
                    else if (state_var == 5)
                    {
                        int StockItem_Product_Single_Successful = 0;
                        int StockItem_Product_Single_Index = 0;
                        
                        for (int sip = 0; sip < 4; sip++)
                        {
                            if (s_minus_comma.toLowerCase().equals(StockItem_Product_Single[sip].toLowerCase()) == true)
                            {
                                StockItem_Product_Single_Successful = 1;
                                StockItem_Product_Single_Index = sip;
                            }
                            ;
                        }

                        if ((StockItem_Product_Single_Successful == 1) && (StockItem_Product_Single_Index == product))
                        {
                            if (endingComma == 1)
                                state_var = 6;
                            else
                                state_var = 7;
                            OrderList[product] += quantity;
                        }
                        else
                            state_var = 101;
                        ;
                    }
                    // 6 -> Comma Coupling state
                    else if (state_var == 6) // This is based on the state_var <- 0 case with appendment covering the
                                        // bought exit case condition
                    {
                        if (s.toLowerCase().equals(Test_a.toLowerCase()) == true)
                        {
                            state_var = 1;
                            quantity = 1;
                        }
                        else if (s.toLowerCase().equals(Test_an.toLowerCase()) == true)
                        {
                            state_var = 2;
                            quantity = 1;
                        }
                        else if (s.toLowerCase().equals(Test_bought.toLowerCase()) == true)
                        {
                            state_var = 8;
                        }
                        else if (isInteger(s) == true)
                        {
                            state_var = 3;
                            quantity = Integer.parseInt(s);
                        }
                        else
                        {
                            state_var = 101;
                            quantity = 0;
                        }
                    }
                    else if (state_var == 7)
                    {
                        if (s.toLowerCase().equals(Test_and.toLowerCase()) == true)
                            state_var = 0; // PITA (given that this structure is being trialled) to replicate 
                                            // entire state pool and modify to remove multiple item addition capability
                                            // -> therefore cycle back to state 0 (Quantity)
                        else
                            state_var = 101;
                    }
                    // 8 -> Post "Bought" state
                    else if (state_var == 8)
                    {
                        if (s_minus_comma.toLowerCase().equals(Test_today.toLowerCase()) == true)
                        {
                            state_var = 0;
                            // today calculation is executed here
                            
                            // {0: "soup", 1: "bread", 2: "milk", 3: "apples"};

                            double cash_register = 0.0;
                            
                            if ((OrderList[0] >= 2) && (OrderList[1] >= 1))
                            {
                                OrderList[1]--;
                                cash_register += 0.5*StockItem_Cost[1];
                            }

                            // Accumulate cash_register
                            for (int p_index = 0; p_index < 4; p_index++)
                                cash_register += ((double)OrderList[p_index])*StockItem_Cost[p_index];

                            System.out.println("Total cost = " + Double.toString(cash_register) + "\n");

                            // Reset OrderList
                            for (int p_index = 0; p_index < 4; p_index++)
                                OrderList[p_index] = 0;
                            ;
                        }
                        else if (s_minus_comma.toLowerCase().equals(Test_in.toLowerCase()) == true)
                        {
                            state_var = 9;
                            ;
                        }
                        else
                        {
                            state_var = 101;
                        }
                        ;
                    }
                    // 9 -> Post "in" state
                    else if (state_var == 9)
                    {
                        if (isInteger(s) == true)
                        {
                            state_var = 10;
                            days = Integer.parseInt(s);
                        }
                        else
                        {
                            state_var = 101;
                        }
                        ;
                    }
                    // 10 -> "Days" state
                    else if (state_var == 10)
                    {
                        if (s.toLowerCase().equals(Test_days.toLowerCase()) == true)
                        {
                            state_var = 11;
                        }
                        else
                        {
                            state_var = 101;
                        }
                        ;
                    }
                    // 11 -> "Time" state
                    else if (state_var == 11)
                    {
                        if (s_minus_comma.toLowerCase().equals(Test_time.toLowerCase()) == true)
                        {
                            state_var = 0;
                            // x days time calculation is executed here

                            // {0: "soup", 1: "bread", 2: "milk", 3: "apples"};

                            double cash_register = 0.0;
                            
                            if ((OrderList[0] >= 2) && (OrderList[1] >= 1) && (days < 7))
                            {
                                OrderList[1]--;
                                cash_register += 0.5*StockItem_Cost[1];
                            }

                            if ((days >= 3) && (days <= runofdays))
                            {
                                cash_register += ((double)OrderList[3])*0.9*StockItem_Cost[3];
                                OrderList[3] = 0;
                            }

                            // Accumulate cash_register
                            for (int p_index = 0; p_index < 4; p_index++)
                                cash_register += ((double)OrderList[p_index])*StockItem_Cost[p_index];

                            System.out.println("Total cost = " + Double.toString(cash_register) + "\n");
                            
                            // Reset OrderList
                            for (int p_index = 0; p_index < 4; p_index++)
                                OrderList[p_index] = 0;
                        }
                        else
                        {
                            state_var = 101;
                        }
                        ;
                    }
                    // 12 -> Plural Not Apple "of" detection state;
                    else if (state_var == 12)
                    {
                        if (s.toLowerCase().equals(Test_of.toLowerCase()) == true)
                            state_var = 13;
                        else
                            state_var = 101;
                    }
                    // 13 -> Plural Not Apple Product Plural detection state;
                    else if (state_var == 13)
                    {
                        int StockItem_Product_Successful = 0;
                        int StockItem_Product_Index = 0;
                        
                        for (int sip = 0; sip < 4; sip++)
                        {
                            if (s_minus_comma.toLowerCase().equals(StockItem_Product[sip].toLowerCase()) == true)
                            {
                                StockItem_Product_Successful = 1;
                                StockItem_Product_Index = sip;
                            }
                            ;
                        }

                        if ((StockItem_Product_Successful == 1) && (StockItem_Product_Index == product))
                        {
                            if (endingComma == 1)
                                state_var = 6;
                            else
                                state_var = 7;
                            OrderList[product] += quantity;
                        }
                        else
                            state_var = 101;
                        ;
                    }
                    i++;
                }
                ;
            }
            ;
        }


    }

    public static boolean isInteger(String s) {
        boolean isValidInteger = false;
        try
        {
           Integer.parseInt(s);
   
           // s is a valid integer
   
           isValidInteger = true;
        }
        catch (NumberFormatException ex)
        {
           // s is not an integer
        }
   
        return isValidInteger;
     }
}