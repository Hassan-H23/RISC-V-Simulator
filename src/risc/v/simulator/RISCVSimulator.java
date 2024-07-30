/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package risc.v.simulator;

import java.util.*;

/**
 *
 * @author lenovo
 */

public class RISCVSimulator {


 
    
    public static void main(String[] args) {
        
        // creating a string array for the registers 
        // all elements are automatically initialized to zero

        // need to find a way to make x0 constant at zero 
        
        
        // getting initial data from the user
        // getting the program starting address (program counter)
        int program_counter;
        System.out.println("Enter starting address of the program: ");
        Scanner s = new Scanner(System.in);
        program_counter = s.nextInt();
        System.out.println("Program counter is at " + program_counter+ "\n");// should be printed after each line of assembly code
        
        
        // getting assembly code from the user
        String asembly_line;
        System.out.println("Enter assembly code (1 line only): ");
        s = new Scanner(System.in);
        asembly_line = s.nextLine();
        
        // spliting the line of assembly code into words using spaces or commas
        String[] assembly_line_split = asembly_line.split("[\\s,]+");
        
     
        //giant swich-case statement to know what to do
        switch(assembly_line_split[0]) {
            // 19- addi
            case "addi" :
                
                // getting the destination register number
                String rd_num_string = assembly_line_split[1].substring(1);
                int rd_num_int = Integer.parseInt(rd_num_string);
                
                // getting the source register number
                String rs_num_string = assembly_line_split[2].substring(1);
                int rs_num_int = Integer.parseInt(rs_num_string);
                
                //getting the immidate value 
                int imm = Integer.parseInt(assembly_line_split[3]);
                
                // doing the operation and saving in the reg 
               // reg[rd_num_int] = reg[rd_num_int] + imm;
                
                
                break;
                
            default:
  
        }
        
        
        
        // function that prints all the registers 
    //   for (int i = 0; i < reg.length; i++) {
  //           System.out.println("x" + i+ " : " + reg[i]);
   //     }
        
   
           
    }
    
    
    
}
