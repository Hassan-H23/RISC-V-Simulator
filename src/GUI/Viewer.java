/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package GUI;


import java.awt.Color;
import java.util.*;
import javax.swing.JOptionPane;


/**
 *
 * @author Hassan
 */





public class Viewer extends javax.swing.JFrame {
//Global Varibels
public  Map <Integer, Integer> Data_Memory; // Memory
public  Map <Integer, String> Instruction_Memory = new LinkedHashMap<>(); //sets the instructions in order
public  Map <Integer, Integer> intialDataMem; // Used to restart
public  ArrayList<Integer> reg = new ArrayList<>(Collections.nCopies(32, 0)); // Registers
String[] Instruction; // to be able to store instruction sequentially 
String Assembly_CodeCpy; int Program_CounterCpy, MaxPR, intialProgramCounter , counter; //Misall


//Misc
public void EndSimulation(){
  NextBttn.setVisible(false);
  Complete.setVisible(true);
}
//Parsing Functions
   public  String extractOpcode(String instruction) {
        // Splitting the instruction by whitespace or comma
        String[] parts = instruction.split("[,\\s]+");
        // Returning the first part, which is the opcode
        return parts[0];
    }
//Instruction Functions 
   // 1
   public void LUI (String instr){
        String[] parts = instr.split("[,\\s]+");
            int regd = 0, imm = 0;
        
        for (int i = 1; i < parts.length; i++) {
            String part = parts[i];
            if (part.startsWith("x")) {regd = Integer.parseInt(part.substring(1));} 
            else {imm = Integer.parseInt(part);}
            }
          int immShifted = (imm << 12);
          if(regd == 0){
          
          JOptionPane.showMessageDialog(this, "ERROR: Cannot Use x0 as it is a Constant Register!", "Constant Register",  JOptionPane.ERROR_MESSAGE);
          return;
          }
        reg.set(regd, immShifted);
        Program_CounterCpy += 4;
   
   }
   // 2
   public void AUIPC (String instr){
       String[] parts = instr.split("[,\\s]+");
            int regd = 0, imm = 0;
        for (int i = 1; i < parts.length; i++) {
            String part = parts[i];
            if (part.startsWith("x")) {regd = Integer.parseInt(part.substring(1));} 
            else {imm = Integer.parseInt(part);}
            }
         int immShifted = (imm << 12)+Program_CounterCpy;
             if(regd == 0){
          
          JOptionPane.showMessageDialog(this, "ERROR: Cannot Use x0 as it is a Constant Register!", "Constant Register",  JOptionPane.ERROR_MESSAGE);
          return;
          }
         reg.set(regd, immShifted);
        Program_CounterCpy += 4;
   }
   //3
   public void JAL (String instr) {
   // jal rd, label
    String[] assembly_line_split = instr.split("[\\s,]+");
    String rsd_string = assembly_line_split[1].substring(1);
    int rsd= Integer.parseInt(rsd_string);
    int label_addr = Integer.parseInt(assembly_line_split[2]);
        if(rsd == 0){
          JOptionPane.showMessageDialog(this, "ERROR: Cannot Use x0 as it is a Constant Register!", "Constant Register",  JOptionPane.ERROR_MESSAGE);
          return;
          }
    reg.set(rsd, Program_CounterCpy+4);
    Program_CounterCpy = label_addr;
    //Program_CounterCpy -= 4;
    counter--;
   }
   // 4
   public void JALR(String instr) { 
   String[] assembly_line_split = instr.split("[\\s,]+");
   int rd1 = Integer.parseInt(assembly_line_split[1].substring(1));
    int length_temp = assembly_line_split[3].length();
    String base_reg_string = assembly_line_split[3].substring(2,length_temp-1);
    int base_addr_reg = Integer.parseInt(base_reg_string);
    int base_addr = reg.get(base_addr_reg);
        
    String off_set_string = assembly_line_split[2];
    int off_set = Integer.parseInt(off_set_string);
   
   reg.set(rd1, Program_CounterCpy+4);
   Program_CounterCpy =  base_addr + off_set; 
   counter--;
   }
   
   
   
   
   //5
   public void BEQ(String instr){  
   String[] assembly_line_split = instr.split("[\\s,]+");
   int rd1 = Integer.parseInt(assembly_line_split[1].substring(1));
   int rd2 = Integer.parseInt(assembly_line_split[2].substring(1));
   int label_addr = Integer.parseInt(assembly_line_split[3]);
   if(reg.get(rd1) == reg.get(rd2)){Program_CounterCpy = label_addr; return;}  
   Program_CounterCpy += 4;
   counter--;
  
   }
   //6
     public void BNE (String instr){
   String[] assembly_line_split = instr.split("[\\s,]+");
   int rd1 = Integer.parseInt(assembly_line_split[1].substring(1));
   int rd2 = Integer.parseInt(assembly_line_split[2].substring(1));
   int label_addr = Integer.parseInt(assembly_line_split[3]);
   if(reg.get(rd1) != reg.get(rd2)){Program_CounterCpy = label_addr; return;}   
   Program_CounterCpy += 4;
   counter--;
   }
   //7
   public void BLT (String instr){
   String[] assembly_line_split = instr.split("[\\s,]+");
   int rd1 = Integer.parseInt(assembly_line_split[1].substring(1));
   int rd2 = Integer.parseInt(assembly_line_split[2].substring(1));
   int label_addr = Integer.parseInt(assembly_line_split[3]);
   if(reg.get(rd1) < reg.get(rd2)){Program_CounterCpy = label_addr;return;} 
   Program_CounterCpy += 4;
   counter--;
   }  
   //8
     public void BGE (String instr){
   String[] assembly_line_split = instr.split("[\\s,]+");
   int rd1 = Integer.parseInt(assembly_line_split[1].substring(1));
   int rd2 = Integer.parseInt(assembly_line_split[2].substring(1));
   int label_addr = Integer.parseInt(assembly_line_split[3]);
   if(reg.get(rd1) >= reg.get(rd2)){Program_CounterCpy = label_addr;return;}  
   Program_CounterCpy += 4;
   counter--;
   }  
   //9
     public void BLTU (String instr){
       String[] assembly_line_split = instr.split("[\\s,]+");    
       int rd1 = Integer.parseInt(assembly_line_split[1].substring(1));
       int rd2 = Integer.parseInt(assembly_line_split[2].substring(1));
       int label_addr = Integer.parseInt(assembly_line_split[3]);
        int value_rs1_absolute = Math.abs(reg.get(rd1));
        int value_rs2_absolute = Math.abs(reg.get(rd2));
        if ((value_rs1_absolute < value_rs2_absolute)){Program_CounterCpy = label_addr;return;}
        Program_CounterCpy += 4;
        counter--;
       
     }
   // 10
   public  void BGEU (String instr){
   String[] assembly_line_split = instr.split("[\\s,]+");
   int rd1 = Integer.parseInt(assembly_line_split[1].substring(1));
   int rd2 = Integer.parseInt(assembly_line_split[2].substring(1));
   int label_addr = Integer.parseInt(assembly_line_split[3]);
   if(Math.abs(reg.get(rd1)) >= Math.abs(reg.get(rd2))){Program_CounterCpy = label_addr;return;} 
   Program_CounterCpy += 4;
   counter--;
    }
   
   // 11
   public  void LB (String instr){
   String[] parts = instr.split("[,\\s]+");
        
        String rd_string = parts[1].substring(1);
        int rd_int = Integer.parseInt(rd_string);
        
        // condition to keep x0 zero
        if (rd_int == 0){
            JOptionPane.showMessageDialog(this, "ERROR: Cannot Use x0 as it is a Constant Register!", "Constant Register",  JOptionPane.ERROR_MESSAGE);
            return;
        }
        String off_set_string = parts[2];
        int off_set = Integer.parseInt(off_set_string);
        
        int length_temp = parts[3].length();
        String base_reg_string = parts[3].substring(2,length_temp-1);
        int base_addr_reg = Integer.parseInt(base_reg_string);
        int base_addr = reg.get(base_addr_reg);
        
        int value = Data_Memory.get(base_addr + off_set);
        int firstByte = (byte) value;
        reg.set(rd_int,firstByte);
        Program_CounterCpy += 4;
    }
   
   // 12
   public  void LH (String instr){
   String[] parts = instr.split("[,\\s]+");
        
        String rd_string = parts[1].substring(1);
        int rd_int = Integer.parseInt(rd_string);
        
        // condition to keep x0 zero
        if (rd_int == 0){
            JOptionPane.showMessageDialog(this, "ERROR: Cannot Use x0 as it is a Constant Register!", "Constant Register",  JOptionPane.ERROR_MESSAGE);
            return;
        }
        String off_set_string = parts[2];
        int off_set = Integer.parseInt(off_set_string);
        int length_temp = parts[3].length();
        String base_reg_string = parts[3].substring(2,length_temp-1);
        int base_addr_reg = Integer.parseInt(base_reg_string);
        int base_addr = reg.get(base_addr_reg);
        int value = Data_Memory.get(base_addr + off_set);
       int firstTwoBytes = (short) value;
       reg.set(rd_int,firstTwoBytes);
        Program_CounterCpy += 4;
    }
   
   // 13
   public  void LW (String instr){
   String[] parts = instr.split("[,\\s]+");
        
        String rd_string = parts[1].substring(1);
        int rd_int = Integer.parseInt(rd_string);
        
        // condition to keep x0 zero
        if (rd_int == 0){
            JOptionPane.showMessageDialog(this, "ERROR: Cannot Use x0 as it is a Constant Register!", "Constant Register",  JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        String off_set_string = parts[2];
        int off_set = Integer.parseInt(off_set_string);
        
        int length_temp = parts[3].length();
        String base_reg_string = parts[3].substring(2,length_temp-1);
        int base_addr_reg = Integer.parseInt(base_reg_string);
        int base_addr = reg.get(base_addr_reg);
        
        
        int value = Data_Memory.get(base_addr + off_set);
        int value_unsinged = Math.abs(value);
        
        reg.set(rd_int,value_unsinged);
        Program_CounterCpy +=4;
    }
   
   // 14
   public  void LBU (String instr){
   String[] parts = instr.split("[,\\s]+");
        
        String rd_string = parts[1].substring(1);
        int rd_int = Integer.parseInt(rd_string);
        // condition to keep x0 zero
        if (rd_int == 0){
            JOptionPane.showMessageDialog(this, "ERROR: Cannot Use x0 as it is a Constant Register!", "Constant Register",  JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        String off_set_string = parts[2];
        int off_set = Integer.parseInt(off_set_string);
        
        int length_temp = parts[3].length();
        String base_reg_string = parts[3].substring(2,length_temp-1);
        int base_addr_reg = Integer.parseInt(base_reg_string);
        int base_addr = reg.get(base_addr_reg);
        
        
        int value = Data_Memory.get(base_addr + off_set);
        int value_unsinged = Math.abs(value);
        int firstByte = (byte) value_unsinged;
        reg.set(rd_int,firstByte);
        Program_CounterCpy += 4;
    }
   
   // 15
   public  void LHU (String instr){
   String[] parts = instr.split("[,\\s]+");
        
        String rd_string = parts[1].substring(1);
        int rd_int = Integer.parseInt(rd_string);
        // condition to keep x0 zero
        if (rd_int == 0){
            JOptionPane.showMessageDialog(this, "ERROR: Cannot Use x0 as it is a Constant Register!", "Constant Register",  JOptionPane.ERROR_MESSAGE);
            return;
        }        
        
        String off_set_string = parts[2];
        int off_set = Integer.parseInt(off_set_string);
        
        int length_temp = parts[3].length();
        String base_reg_string = parts[3].substring(2,length_temp-1);
        int base_addr_reg = Integer.parseInt(base_reg_string);
        int base_addr = reg.get(base_addr_reg);
       // System.out.println("BASE addr  " + base_addr + "\n");
        
        int value = Data_Memory.get(base_addr + off_set);
        int value_unsinged = Math.abs(value);
        int firstByte = (short) value_unsinged;
        reg.set(rd_int,firstByte);
        Program_CounterCpy += 4;
    }
   
   // 16
   public  void SH (String instr){
   String[] parts = instr.split("[,\\s]+");
        
        String value_reg = parts[1].substring(1);
        int value_reg_int = Integer.parseInt(value_reg);
        int value = reg.get(value_reg_int);
      //  System.out.println("VALUE IN SOURCE REG " + value + "\n" );
        
        
        String off_set_string = parts[2];
        int off_set = Integer.parseInt(off_set_string);
      //  System.out.println("OFFset " + off_set+ "\n");
        
        int length_temp = parts[3].length();
        String base_reg_string = parts[3].substring(2,length_temp-1);
        int base_addr_reg = Integer.parseInt(base_reg_string);
        int base_addr = reg.get(base_addr_reg);
      //  System.out.println("BASE addr  " + base_addr + "\n");
     int half_word = (short) value;  
        Data_Memory.put(base_addr + off_set,half_word);
        Program_CounterCpy += 4;
        
       // System.out.println("Memory location: " + Data_Memory);
    }
   
   // 17
   public  void SB (String instr){
   String[] parts = instr.split("[,\\s]+");
        
        String value_reg = parts[1].substring(1);
        int value_reg_int = Integer.parseInt(value_reg);
        int value = reg.get(value_reg_int);
        
        
        String off_set_string = parts[2];
        int off_set = Integer.parseInt(off_set_string);
        
        int length_temp = parts[3].length();
        String base_reg_string = parts[3].substring(2,length_temp-1);
        int base_addr_reg = Integer.parseInt(base_reg_string);
        int base_addr = reg.get(base_addr_reg);
        
        Data_Memory.put(base_addr + off_set , value);
        Program_CounterCpy += 4;
       // System.out.println("Memory location: " + Data_Memory);
    }
   // 18
   public  void SW (String instr){
   String[] parts = instr.split("[,\\s]+");
        
        String value_reg = parts[1].substring(1);
        int value_reg_int = Integer.parseInt(value_reg);
        int value = reg.get(value_reg_int);
        String off_set_string = parts[2];
        int off_set = Integer.parseInt(off_set_string);
        
        int length_temp = parts[3].length();
        String base_reg_string = parts[3].substring(2,length_temp-1);
        int base_addr_reg = Integer.parseInt(base_reg_string);
        int base_addr = reg.get(base_addr_reg);
        int firstByte = (byte) value;  
        Data_Memory.put(base_addr + off_set,firstByte);
        Program_CounterCpy +=4;
      // System.out.println("Memory location: " + Memory);
    }
   
   //19
   public void ADDI (String instr) {
    String[] assembly_line_split = instr.split("[\\s,]+");
        
        //getting the destination register number
        String rd_num_string = assembly_line_split[1].substring(1);
        int rd_num_int = Integer.parseInt(rd_num_string);
        // condition to keep x0 zero
        if (rd_num_int == 0){
            JOptionPane.showMessageDialog(this, "ERROR: Cannot Use x0 as it is a Constant Register!", "Constant Register",  JOptionPane.ERROR_MESSAGE);
            return;
        }        
        // getting the source register number
        String rs_num_string = assembly_line_split[2].substring(1);
        int rs_num_int = Integer.parseInt(rs_num_string);
                
        //getting the immidate value 
        int imm = Integer.parseInt(assembly_line_split[3]);
                
        // doing the operation and saving in the reg 
        
         reg.set(rd_num_int,reg.get(rs_num_int)+imm);
         Program_CounterCpy +=4;
        
//        for(int i = 0; i<32;i++){    // populate Registers  
//        
//        System.out.println("ADDI FUNCTION OUTPUT : "+"x" + i + ": " + reg.get(i)); //Test
//        }
//            System.out.println("------------------------------------------------------"); //Test 
}
   
  // 20
    public  void SLLI (String instr){
        String[] assembly_line_split = instr.split("[\\s,]+");
        
        //getting the destination register number
        String rd_num_string = assembly_line_split[1].substring(1);
        int rd_num_int = Integer.parseInt(rd_num_string);
        
        // getting the source register number
        String rs_num_string = assembly_line_split[2].substring(1);
        int rs_num_int = Integer.parseInt(rs_num_string);
                
        //getting the immidate value 
        int imm = Integer.parseInt(assembly_line_split[3]);
                
        // doing the operation and saving in the reg 
        
        reg.set(rd_num_int,reg.get(rs_num_int) << imm);
        Program_CounterCpy += 4;
        
    }
    
     public void SLTI (String instr) {
    String[] assembly_line_split = instr.split("[\\s,]+");
    
        //getting the destination register number
        String rd_num_string = assembly_line_split[1].substring(1);
        int rd = Integer.parseInt(rd_num_string);
        
        // getting the source register number
        String rs_num_string = assembly_line_split[2].substring(1);
        int rs = Integer.parseInt(rs_num_string);
        
        // getting the immediate value
        int imm = Integer.parseInt(assembly_line_split[3]);
        
        if(reg.get(rs) < Math.abs(imm))
        {
        reg.set(rd , 1);
        }
        else
        {
        reg.set(rd, 0);
        }
        Program_CounterCpy +=4;
    }
    // 21
    public void SLTIU (String instr) {
    
    String[] assembly_line_split = instr.split("[\\s,]+");
    
        //getting the destination register number
        String rd_num_string = assembly_line_split[1].substring(1);
        int rd = Integer.parseInt(rd_num_string);
        
        // getting the source register number
        String rs_num_string = assembly_line_split[2].substring(1);
        int rs = Integer.parseInt(rs_num_string);
        
        // getting the immediate value
        int imm = Integer.parseInt(assembly_line_split[3]);
        
        if(reg.get(rs) < Math.abs(imm))
        {
        reg.set(rd , 1);
        }
        else
        {
        reg.set(rd, 0);
        }
        Program_CounterCpy +=4;
    }
    
    //22
    public void XORI (String instr) {
    String[] assembly_line_split = instr.split("[\\s,]+");
    
        //getting the destination register number
        String rd_num_string = assembly_line_split[1].substring(1);
        int rd = Integer.parseInt(rd_num_string);
        
        // getting the source register number
        String rs_num_string = assembly_line_split[2].substring(1);
        int rs = Integer.parseInt(rs_num_string);
        
        // getting the immediate value
        int imm = Integer.parseInt(assembly_line_split[3]);
        
        reg.set(rd, reg.get(rs) ^ imm);
        Program_CounterCpy += 4;
    }

    
    //23
    public void ORI (String instr) {
    String[] assembly_line_split = instr.split("[\\s,]+");
    
        //getting the destination register number
        String rd_num_string = assembly_line_split[1].substring(1);
        int rd = Integer.parseInt(rd_num_string);
        
        // getting the source register number
        String rs_num_string = assembly_line_split[2].substring(1);
        int rs = Integer.parseInt(rs_num_string);
        
        // getting the immediate value
        int imm = Integer.parseInt(assembly_line_split[3]);
        
        reg.set(rd, reg.get(rs) | imm);
        Program_CounterCpy += 4;
    }

    
    //24
    public void ANDI (String instr) {
    String[] assembly_line_split = instr.split("[\\s,]+");
    
        //getting the destination register number
        String rd_num_string = assembly_line_split[1].substring(1);
        int rd = Integer.parseInt(rd_num_string);
        
        // getting the source register number
        String rs_num_string = assembly_line_split[2].substring(1);
        int rs = Integer.parseInt(rs_num_string);
        
        // getting the immediate value
        int imm = Integer.parseInt(assembly_line_split[3]);
        
        reg.set(rd, reg.get(rs) & imm);
        Program_CounterCpy+=4;}
    
    /****/

    /**
     * /
     * @param instr
     */
    //26
    public void SRLI (String instr) {
        String[] assembly_line_split = instr.split("[\\s,]+");
    
        //getting the destination register number
        String rd_num_string = assembly_line_split[1].substring(1);
        int rd = Integer.parseInt(rd_num_string);
        
        // getting the source register number
        String rs_num_string = assembly_line_split[2].substring(1);
        int rs = Integer.parseInt(rs_num_string);
        
        // getting the immediate value
        int imm = Integer.parseInt(assembly_line_split[3]);
        
        reg.set(rd , reg.get(rs) >> imm);
        Program_CounterCpy += 4;
    }
    
    //27
        public void SRAI (String instr) {
        String[] assembly_line_split = instr.split("[\\s,]+");
    
        //getting the destination register number
        String rd_num_string = assembly_line_split[1].substring(1);
        int rd = Integer.parseInt(rd_num_string);
        
        // getting the source register number
        String rs_num_string = assembly_line_split[2].substring(1);
        int rs = Integer.parseInt(rs_num_string);
        
        // getting the immediate value
        int shamt = Integer.parseInt(assembly_line_split[3]);
        
        reg.set(rd , reg.get(rs) >> shamt);
        Program_CounterCpy += 4;
    }
    
    
    //28
    public void add(String instr) {
        String[] parts = instr.split("[,\\s]+");
        int regd = 0, regs1 = 0, regs2 = 0;
        for (int i = 1; i < parts.length; i++) {
            String part = parts[i];
            if (part.startsWith("x")) {
                if (regd == 0) {
                    regd = Integer.parseInt(part.substring(1));
                } else if (regs1 == 0) {
                    regs1 = Integer.parseInt(part.substring(1));
                } else {
                    regs2 = Integer.parseInt(part.substring(1));
                }
            }
            Program_CounterCpy+=4;
        }
         // Check if destination register is x0
    if (regd == 0){
        JOptionPane.showMessageDialog(this, "ERROR: Cannot Use x0 as it is a Constant Register!", "Constant Register",  JOptionPane.ERROR_MESSAGE);
        return;
    }
        reg.set(regd, reg.get(regs1) + reg.get(regs2));
        Program_CounterCpy += 4;
    }
    //29
    public void sub(String instr) {
        String[] parts = instr.split("[,\\s]+");
        int regd = 0, regs1 = 0, regs2 = 0;
        for (int i = 1; i < parts.length; i++) {
            String part = parts[i];
            if (part.startsWith("x")) {
                if (regd == 0) {
                    regd = Integer.parseInt(part.substring(1));
                } else if (regs1 == 0) {
                    regs1 = Integer.parseInt(part.substring(1));
                } else {
                    regs2 = Integer.parseInt(part.substring(1));
                }
            }
            Program_CounterCpy+=4;
        }
         // Check if destination register is x0
    if (regd == 0){
        JOptionPane.showMessageDialog(this, "ERROR: Cannot Use x0 as it is a Constant Register!", "Constant Register",  JOptionPane.ERROR_MESSAGE);
        return;
    }
        reg.set(regd, reg.get(regs1) - reg.get(regs2));
    }
    //36
    public void or(String instr) {
        String[] parts = instr.split("[,\\s]+");
        int regd = 0, regs1 = 0, regs2 = 0;
        for (int i = 1; i < parts.length; i++) {
            String part = parts[i];
            if (part.startsWith("x")) {
                if (regd == 0) {
                    regd = Integer.parseInt(part.substring(1));
                } else if (regs1 == 0) {
                    regs1 = Integer.parseInt(part.substring(1));
                } else {
                    regs2 = Integer.parseInt(part.substring(1));
                }
            }
        }
         // Check if destination register is x0
    if (regd == 0){
        JOptionPane.showMessageDialog(this, "ERROR: Cannot Use x0 as it is a Constant Register!", "Constant Register",  JOptionPane.ERROR_MESSAGE);
        return;
    }
        reg.set(regd, reg.get(regs1) | reg.get(regs2));
        Program_CounterCpy+=4;
    }
    //30
    public void sll(String instr) {
     String[] assembly_line_split = instr.split("[\\s,]+");
        
        //getting the destination register number
        String rd_num_string = assembly_line_split[1].substring(1);
        int rd_num_int = Integer.parseInt(rd_num_string);
        
        // getting the source register number
        String rs1_num_string = assembly_line_split[2].substring(1);
        int rs1_num_int = Integer.parseInt(rs1_num_string);
        
        // getting the source register number
        String rs2_num_string = assembly_line_split[2].substring(1);
        int rs2_num_int = Integer.parseInt(rs2_num_string);
                
        // doing the operation and saving in the reg 
        
        reg.set(rd_num_int,reg.get(rs1_num_int) << rs2_num_int);
        Program_CounterCpy +=4;
    }
    //31
    public void slt(String instr) {
        String[] parts = instr.split("[,\\s]+");
        int regd = 0, regs1 = 0, regs2 = 0;
        for (int i = 1; i < parts.length; i++) {
            String part = parts[i];
            if (part.startsWith("x")) {
                if (regd == 0) {
                    regd = Integer.parseInt(part.substring(1));
                } else if (regs1 == 0) {
                    regs1 = Integer.parseInt(part.substring(1));
                } else {
                    regs2 = Integer.parseInt(part.substring(1));
                }
            }
        }
         // Check if destination register is x0
    if (regd == 0){
        JOptionPane.showMessageDialog(this, "ERROR: Cannot Use x0 as it is a Constant Register!", "Constant Register",  JOptionPane.ERROR_MESSAGE);
        return;
    }
        reg.set(regd, reg.get(regs1) < reg.get(regs2) ? 1 : 0);
        Program_CounterCpy += 4;
    }
    //32
    public void sltu(String instr) {
        String[] parts = instr.split("[,\\s]+");
        int regd = 0, regs1 = 0, regs2 = 0;
        for (int i = 1; i < parts.length; i++) {
            String part = parts[i];
            if (part.startsWith("x")) {
                if (regd == 0) {
                    regd = Integer.parseInt(part.substring(1));
                } else if (regs1 == 0) {
                    regs1 = Integer.parseInt(part.substring(1));
                } else {
                    regs2 = Integer.parseInt(part.substring(1));
                }
            }
        }
         // Check if destination register is x0
    if (regd == 0){
        JOptionPane.showMessageDialog(this, "ERROR: Cannot Use x0 as it is a Constant Register!", "Constant Register",  JOptionPane.ERROR_MESSAGE);
        return;
    }
        reg.set(regd, Integer.compareUnsigned(reg.get(regs1), reg.get(regs2)) < 0 ? 1 : 0);
        Program_CounterCpy += 4;
    }
    //33
    public void xor(String instr) {
        String[] parts = instr.split("[,\\s]+");
        int regd = 0, regs1 = 0, regs2 = 0;
        for (int i = 1; i < parts.length; i++) {
            String part = parts[i];
            if (part.startsWith("x")) {
                if (regd == 0) {
                    regd = Integer.parseInt(part.substring(1));
                } else if (regs1 == 0) {
                    regs1 = Integer.parseInt(part.substring(1));
                } else {
                    regs2 = Integer.parseInt(part.substring(1));
                }
            }
        }
         // Check if destination register is x0
    if (regd == 0){
        JOptionPane.showMessageDialog(this, "ERROR: Cannot Use x0 as it is a Constant Register!", "Constant Register",  JOptionPane.ERROR_MESSAGE);
        return;
    }
        reg.set(regd, reg.get(regs1) ^ reg.get(regs2));
        Program_CounterCpy += 4;
    }
    //34
    public void srl(String instr) {
        String[] parts = instr.split("[,\\s]+");
        int regd = 0, regs = 0, shamt = 0;
        for (int i = 1; i < parts.length; i++) {
            String part = parts[i];
            if (part.startsWith("x")) {
                if (regd == 0) {
                    regd = Integer.parseInt(part.substring(1));
                } else {
                    regs = Integer.parseInt(part.substring(1));
                }
            } else {
                shamt = Integer.parseInt(part);
            }
        }
         // Check if destination register is x0
    if (regd == 0){
        JOptionPane.showMessageDialog(this, "ERROR: Cannot Use x0 as it is a Constant Register!", "Constant Register",  JOptionPane.ERROR_MESSAGE);
        return;
    }
        reg.set(regd, reg.get(regs) >>> shamt);
        Program_CounterCpy += 4;
    }
    //35
    public void sra(String instr) {
        String[] parts = instr.split("[,\\s]+");
        int regd = 0, regs = 0, shamt = 0;
        for (int i = 1; i < parts.length; i++) {
            String part = parts[i];
            if (part.startsWith("x")) {
                if (regd == 0) {
                    regd = Integer.parseInt(part.substring(1));
                } else {
                    regs = Integer.parseInt(part.substring(1));
                }
            } else {
                shamt = Integer.parseInt(part);
            }
        }
         // Check if destination register is x0
    if (regd == 0){
        JOptionPane.showMessageDialog(this, "ERROR: Cannot Use x0 as it is a Constant Register!", "Constant Register",  JOptionPane.ERROR_MESSAGE);
        return;
    }
        reg.set(regd, reg.get(regs) >> shamt);
        Program_CounterCpy += 4;
    }
    //37
    public void and(String instr) {
        String[] parts = instr.split("[,\\s]+");
        int regd = 0, regs1 = 0, regs2 = 0;
        for (int i = 1; i < parts.length; i++) {
            String part = parts[i];
            if (part.startsWith("x")) {
                if (regd == 0) {
                    regd = Integer.parseInt(part.substring(1));
                } else if (regs1 == 0) {
                    regs1 = Integer.parseInt(part.substring(1));
                } else {
                    regs2 = Integer.parseInt(part.substring(1));
                }
            }
        }
         // Check if destination register is x0
    if (regd == 0){
        JOptionPane.showMessageDialog(this, "ERROR: Cannot Use x0 as it is a Constant Register!", "Constant Register",  JOptionPane.ERROR_MESSAGE);
        return;
    }
        reg.set(regd, reg.get(regs1) & reg.get(regs2));
        Program_CounterCpy += 4;
    }

    //EXTRA INSTRUCTIONS MUL AND DIV
    //m1
    public void mul(String instr) {
        String[] parts = instr.split("[,\\s]+");
        int regd = 0, regs1 = 0, regs2 = 0;
        for (int i = 1; i < parts.length; i++) {
            String part = parts[i];
            if (part.startsWith("x")) {
                if (regd == 0) {
                    regd = Integer.parseInt(part.substring(1));
                } else if (regs1 == 0) {
                    regs1 = Integer.parseInt(part.substring(1));
                } else {
                    regs2 = Integer.parseInt(part.substring(1));
                }
            }
        }
         // Check if destination register is x0
    if (regd == 0){
        JOptionPane.showMessageDialog(this, "ERROR: Cannot Use x0 as it is a Constant Register!", "Constant Register",  JOptionPane.ERROR_MESSAGE);
        return;
    }
        reg.set(regd, reg.get(regs1) * reg.get(regs2));
        Program_CounterCpy +=4;
    }
    //m2
    public void mulh(String instr) {
        String[] parts = instr.split("[,\\s]+");
        int regd = 0, regs1 = 0, regs2 = 0;
        for (int i = 1; i < parts.length; i++) {
            String part = parts[i];
            if (part.startsWith("x")) {
                if (regd == 0) {
                    regd = Integer.parseInt(part.substring(1));
                } else if (regs1 == 0) {
                    regs1 = Integer.parseInt(part.substring(1));
                } else {
                    regs2 = Integer.parseInt(part.substring(1));
                }
            }
        }
         // Check if destination register is x0
    if (regd == 0){
        JOptionPane.showMessageDialog(this, "ERROR: Cannot Use x0 as it is a Constant Register!", "Constant Register",  JOptionPane.ERROR_MESSAGE);
        return;
    }
        // Perform MULH operation
        long result = (long) reg.get(regs1) * (long) reg.get(regs2);
        reg.set(regd, (int) (result >> 32)); // Store the high 32 bits of the result
        Program_CounterCpy += 4;
    }
    //m3
    public void mulhsu(String instr) {
        String[] parts = instr.split("[,\\s]+");
        int regd = 0, regs1 = 0, regs2 = 0;
        for (int i = 1; i < parts.length; i++) {
            String part = parts[i];
            if (part.startsWith("x")) {
                if (regd == 0) {
                    regd = Integer.parseInt(part.substring(1));
                } else if (regs1 == 0) {
                    regs1 = Integer.parseInt(part.substring(1));
                } else {
                    regs2 = Integer.parseInt(part.substring(1));
                }
            }
        }
         // Check if destination register is x0
    if (regd == 0){
        JOptionPane.showMessageDialog(this, "ERROR: Cannot Use x0 as it is a Constant Register!", "Constant Register",  JOptionPane.ERROR_MESSAGE);
        return;
    }
        // Perform MULHSU operation
        long result = (long) reg.get(regs1) * (long) (reg.get(regs2) & 0xFFFFFFFFL);
        reg.set(regd, (int) (result >> 32)); // Store the high 32 bits of the result
        Program_CounterCpy += 4;
    }
    //m4
    public void mulhu(String instr) {
        String[] parts = instr.split("[,\\s]+");
        int regd = 0, regs1 = 0, regs2 = 0;
        for (int i = 1; i < parts.length; i++) {
            String part = parts[i];
            if (part.startsWith("x")) {
                if (regd == 0) {
                    regd = Integer.parseInt(part.substring(1));
                } else if (regs1 == 0) {
                    regs1 = Integer.parseInt(part.substring(1));
                } else {
                    regs2 = Integer.parseInt(part.substring(1));
                }
            }
        }
         // Check if destination register is x0
    if (regd == 0){
        JOptionPane.showMessageDialog(this, "ERROR: Cannot Use x0 as it is a Constant Register!", "Constant Register",  JOptionPane.ERROR_MESSAGE);
        return;
    }
        // Perform MULHU operation
        long result = (long) (reg.get(regs1) & 0xFFFFFFFFL) * (long) (reg.get(regs2) & 0xFFFFFFFFL);
        reg.set(regd, (int) (result >>> 32)); // Store the high 32 bits of the result
        Program_CounterCpy += 4;
    }   
    //m5         
    public void div(String instr) {
        String[] parts = instr.split("[,\\s]+");
        int regd = 0, regs1 = 0, regs2 = 0;
        for (int i = 1; i < parts.length; i++) {
            String part = parts[i];
            if (part.startsWith("x")) {
                if (regd == 0) {
                    regd = Integer.parseInt(part.substring(1));
                } else if (regs1 == 0) {
                    regs1 = Integer.parseInt(part.substring(1));
                } else {
                    regs2 = Integer.parseInt(part.substring(1));
                }
            }
        }
         // Check if destination register is x0
    if (regd == 0){
        JOptionPane.showMessageDialog(this, "ERROR: Cannot Use x0 as it is a Constant Register!", "Constant Register",  JOptionPane.ERROR_MESSAGE);
        return;
    }
        if (reg.get(regs2) != 0) {
            reg.set(regd, reg.get(regs1) / reg.get(regs2));
        } else {
            JOptionPane.showMessageDialog(this, "ERROR: DIVISON BY ZERO", "LOGIC ERROR",  JOptionPane.ERROR_MESSAGE);}
        Program_CounterCpy += 4;
        
    }
    //m6
    public void divu(String instr) {
        String[] parts = instr.split("[,\\s]+");
        int regd = 0, regs1 = 0, regs2 = 0;
        for (int i = 1; i < parts.length; i++) {
            String part = parts[i];
            if (part.startsWith("x")) {
                if (regd == 0) {
                    regd = Integer.parseInt(part.substring(1));
                } else if (regs1 == 0) {
                    regs1 = Integer.parseInt(part.substring(1));
                } else {
                    regs2 = Integer.parseInt(part.substring(1));
                }
            }
        }
         // Check if destination register is x0
    if (regd == 0){
        JOptionPane.showMessageDialog(this, "ERROR: Cannot Use x0 as it is a Constant Register!", "Constant Register",  JOptionPane.ERROR_MESSAGE);
        return;
    }
        // Perform DIVU operation
        int dividend = reg.get(regs1);
        int divisor = reg.get(regs2);
        if (divisor != 0) {
            reg.set(regd, Integer.divideUnsigned(dividend, divisor));
        } else {
            JOptionPane.showMessageDialog(this, "ERROR: DIVISON BY ZERO", "LOGIC ERROR",  JOptionPane.ERROR_MESSAGE);
        }
        Program_CounterCpy += 4;
    }
    //m7
    public void rem(String instr) {
        String[] parts = instr.split("[,\\s]+");
        int regd = 0, regs1 = 0, regs2 = 0;
        for (int i = 1; i < parts.length; i++) {
            String part = parts[i];
            if (part.startsWith("x")) {
                if (regd == 0) {
                    regd = Integer.parseInt(part.substring(1));
                } else if (regs1 == 0) {
                    regs1 = Integer.parseInt(part.substring(1));
                } else {
                    regs2 = Integer.parseInt(part.substring(1));
                }
            }
        }
         // Check if destination register is x0
    if (regd == 0){
        JOptionPane.showMessageDialog(this, "ERROR: Cannot Use x0 as it is a Constant Register!", "Constant Register",  JOptionPane.ERROR_MESSAGE);
        return;
    }
        // Perform REM operation
        int dividend = reg.get(regs1);
        int divisor = reg.get(regs2);
        if (divisor != 0) {
            reg.set(regd, dividend % divisor);
        } else {
            JOptionPane.showMessageDialog(this, "ERROR: DIVISON BY ZERO", "LOGIC ERROR",  JOptionPane.ERROR_MESSAGE);}
        Program_CounterCpy += 4;
    }
    
    //m8
    public void remu(String instr) {
        String[] parts = instr.split("[,\\s]+");
        int regd = 0, regs1 = 0, regs2 = 0;
        for (int i = 1; i < parts.length; i++) {
            String part = parts[i];
            if (part.startsWith("x")) {
                if (regd == 0) {
                    regd = Integer.parseInt(part.substring(1));
                } else if (regs1 == 0) {
                    regs1 = Integer.parseInt(part.substring(1));
                } else {
                    regs2 = Integer.parseInt(part.substring(1));
                }
            }
        }
         // Check if destination register is x0
    if (regd == 0){
        JOptionPane.showMessageDialog(this, "ERROR: Cannot Use x0 as it is a Constant Register!", "Constant Register",  JOptionPane.ERROR_MESSAGE);
        return;
    }
        // Perform REMU operation
        int dividend = reg.get(regs1);
        int divisor = reg.get(regs2);
        if (divisor != 0) {
            reg.set(regd, Integer.remainderUnsigned(dividend, divisor));
        } else {
            JOptionPane.showMessageDialog(this, "ERROR: DIVISON BY ZERO", "LOGIC ERROR",  JOptionPane.ERROR_MESSAGE);
        }
        Program_CounterCpy += 4;
    }
    
    /* COMPRESSED RISC-V INSTRUCTION SET */
    //
    //
    // c.li s1, 0    # has no rs only a rd and imm
    public void c_li (String instr)
    {
        String[] assembly_line_split = instr.split("[\\s,]+");
        
        String rd_num_string = assembly_line_split[1].substring(1);
        int rd = Integer.parseInt(rd_num_string);
        
        String imm_string = assembly_line_split[2].substring(1);
        int imm = Integer.parseInt(imm_string);
        
        if(rd == 0){
          JOptionPane.showMessageDialog(this, "ERROR: Cannot Use x0 as it is a Constant Register!", "Constant Register",  JOptionPane.ERROR_MESSAGE);
          return;
          }
        
        if (rd != 0) {
        
        // sign extend the 6-bit immediate value to 8 bits
        if ( (imm & 0x20)!= 0 )
        {
            imm = imm | 0xC0;
            // loading imm into the register
            rd = imm;
            reg.set(rd , reg.get(imm));
        }
        
        }
        Program_CounterCpy += 4;
    }
    
    // c.andi
    public void c_andi(String instr)
    {   String[] assembly_line_split = instr.split("[\\s,]+");
    
        int imm_se = 0;
        
        String rd_num_string = assembly_line_split[1].substring(1);
        int rd = Integer.parseInt(rd_num_string);
        
        String imm_string = assembly_line_split[2].substring(1);
        int imm = Integer.parseInt(imm_string);
        
        if(rd == 0){
          JOptionPane.showMessageDialog(this, "ERROR: Cannot Use x0 as it is a Constant Register!", "Constant Register",  JOptionPane.ERROR_MESSAGE);
          return;
          }
        
        if ((imm & (1 << 5)) != 0) {
        imm |= 0xC0;
        imm_se = imm; // Sign extend the immediate
        }
        
        rd = rd & imm_se;
        reg.set(rd, reg.get(rd) & reg.get(imm_se));
        Program_CounterCpy += 4;
    }
            
    public void c_addi(String instr)
    {
        String[] assembly_line_split = instr.split("[\\s,]+");
    
        int nzimm = 0;
        
        String rd_num_string = assembly_line_split[1].substring(1);
        int rd = Integer.parseInt(rd_num_string);
        
        String imm_string = assembly_line_split[2].substring(1);
        int imm = Integer.parseInt(imm_string);
        
        if(rd == 0){
          JOptionPane.showMessageDialog(this, "ERROR: Cannot Use x0 as it is a Constant Register!", "Constant Register",  JOptionPane.ERROR_MESSAGE);
          return;
          }
        int nzimm_se = 0;
        
    if ((nzimm & (1 << 5)) != 0) {
        nzimm |= 0xC0;
        nzimm_se = nzimm;
        }
        
        rd = rd + nzimm_se;
        reg.set(rd, reg.get(rd) + reg.get(nzimm_se));
        Program_CounterCpy += 4;
}

//Processing Instructions
public void ProcessInstruction(String instruction){
String opcode = extractOpcode(instruction); // Extracts Instruction
//System.out.println("OpCode : "+ opcode); // Test

switch(opcode){
    //1
    case "lui":{ LUI(instruction); }
    break;
    //2
    case "auipc":{ AUIPC(instruction); }
    break;
    //3
    case "jal":{ JAL(instruction); }
    break;
    //4
    case "jalr": {JALR(instruction);}
    break;
    //5
    case "beq": {BEQ(instruction);}
    break;
    //6
    case "bne": {BNE(instruction);}
    break;
    //7
    case "blt": {BLT(instruction);}
    break;
    //8
    case "bge": {BGE(instruction);}
    break;
    //9
    case "bltu": {BLTU(instruction);}
    break;
    //10
    case "bgeu":{ BGEU(instruction); }
    break;
    //11
    case "lb":{ LB(instruction); }
    break;
    //12
    case "lh":{ LH(instruction); }
    break;
    //13
    case "lw":{ LW(instruction); }
    break;
    //14
    case "lbu":{ LHU(instruction); }
    break;
    case "slti": {SLTI(instruction);}
    break;
    //15
    case "lhu":{ LHU(instruction); }
    break;
    
    //16
    case "sb":{ SB(instruction); }
    break;
    
    //17
    case "sh":{ SH(instruction); }
    break;
    
    //18
    case "sw":{ SW(instruction); }
    break;
    
    // 19
    case "addi":{ ADDI(instruction); }
    break;
    
    // 20
    case "slli":{ SLLI(instruction); }
    break; 
    //21
    case "sltiu" : {SLTIU(instruction);}
    break;
    //22
    case "xori" :{XORI(instruction);}
    break;
    //23
    case "ori" :{ORI(instruction);}
    break;
    //24
    case "andi" : {ANDI(instruction);}
    break;
    //26
    case "srli" :{SRLI(instruction);}
    break;
    //27
    case "srai" :{SRAI(instruction);}
    break;
    // 28
    case "add": {add(instruction);}
    break;
     // 29
    case "sub": {sub(instruction);}
    break;
    // 36
    case "or": {or(instruction);}
    break;
    //30
    case "sll": {sll(instruction);}
    break;
    //31
    case "slt": {slt(instruction);}
    break;
    // 32
    case "sltu": {sltu(instruction);}
    break;
    //33
    case "xor": {xor(instruction);}
    break;
    //34
    case "srl": {srl(instruction);}
    break;
    //35
    case "sra": {sra(instruction);}
    break;
    //37
    case "and": {and(instruction);}
    break;
    //m1
    case "mul": {mul(instruction);}
    break;
    //m2
    case "mulh": {mulh(instruction);}
    break;
    //m3
    case "mulhsu": {mulhsu(instruction);}
    break;
    //m4
    case "mulhu": {mulhu(instruction);}
    break;
    //m5
    case "div": {div(instruction);}
    break;
    //m6
    case "divu": {divu(instruction);}
    break;
    //m7
    case "rem": {rem(instruction);}
    break;
    //m8
    case "remu": {remu(instruction);}
    break;
    //c.li
    case "c.li" : {c_li(instruction); }
    break;
    //c.andi
    case "c.andi" : {c_andi(instruction); }
    break;
    // c.ADDi
    case "c.addi" : {c_addi(instruction); }
    break;
    case "FENCE": {EndSimulation(); ProgressBar.setValue(ProgressBar.getMaximum()); Complete.setText("HALTING INSTRUCTION (FENCE)"); Complete.setForeground(Color.red);}
    break;
    case "ECALL": {EndSimulation(); ProgressBar.setValue(ProgressBar.getMaximum());Complete.setText("HALTING INSTRUCTION (ECALL)"); Complete.setForeground(Color.red); ;}
    break;
    case "EBREAK": {EndSimulation(); ProgressBar.setValue(ProgressBar.getMaximum());Complete.setText("HALTING INSTRUCTION (EBREAK)"); Complete.setForeground(Color.red);}
    break;
    default: { JOptionPane.showMessageDialog(this, "Instruction " + opcode + " is not supported","Unsupported Instruction",  JOptionPane.INFORMATION_MESSAGE);}
    break;
}
}

public void PrintRegisters() {
    
   Registers.setText("");
   String Selection = Format.getSelectedItem().toString();
   System.out.println("Selection : " + Selection);
   if(Selection == "Decimal"){
   
    for (int i = 0; i < 32; i++) 
            {          
           Registers.append("x" + i + ": " + reg.get(i) + "\n");
            System.out.println("PrintReg FUNCTION OUTPUT : " + "x" + i + ": " + reg.get(i)); //Test
   }
}
  else if(Selection == "Binary"){
                for (int i = 0; i < 32; i++) 
            {          
           Registers.append("x" + i + ": " + Integer.toBinaryString(reg.get(i)) + "\n");
            System.out.println("PrintReg FUNCTION OUTPUT : " + "x" + i + ": " + Integer.toBinaryString(reg.get(i))); //Test
            }
             }
  else if(Selection == "HexaDecimal") {
      for (int i = 0; i < 32; i++) { 
           Registers.append("x" + i + ": " + Integer.toHexString(reg.get(i)) + "\n");
          System.out.println("PrintReg FUNCTION OUTPUT : " + "x" + i + ": " + Integer.toHexString(reg.get(i))); //Test
            }
           }

    }

public void PrintDataMemory(){
    Memory1.setText("");
if(!Data_Memory.isEmpty()){
    
    for (var entry : Data_Memory.entrySet()) {
    Memory1.append(entry.getKey() + " | " + entry.getValue() + "\n");
    }
}
    else{Memory1.append("Data Memory is Empty");}
}

public void PrintInstructionMemory(){
    Memory2.setText("");
if(!Instruction_Memory.isEmpty()){
    
    for (var entry : Instruction_Memory.entrySet()) {
    Memory2.append(entry.getKey() + " | " + entry.getValue() + "\n");
    }
}
    else{Memory2.append("Instruction Memory is Empty");}
}
//commit
public Viewer(String Assembly_Code, int Program_Counter, Map <Integer, Integer> DM){      
      initComponents(); // initializes the GUI must always be at top
      Assembly_CodeCpy = Assembly_Code;
      Program_CounterCpy = Program_Counter;
      intialProgramCounter = Program_Counter;
      counter = 0;
      int instmem_counter = Program_CounterCpy;
      int temp = Program_Counter;
      AssemblyCode.setText(Assembly_Code); // Set the Assmblycode Component to the code entered in the Index
      Instruction = Assembly_Code.split("\n"); // Splits the Assembly Code instructions and puts it in an array
      if(DM.isEmpty()){Data_Memory = new LinkedHashMap<>(); intialDataMem = new LinkedHashMap<>();}
      else {Data_Memory=DM; intialDataMem = DM;}
      for(int i =0; i<Instruction.length;i++){Instruction_Memory.put(temp, Instruction[i]);temp+=4;}
      for (Map.Entry<Integer, String> entry : Instruction_Memory.entrySet()) {MaxPR = entry.getKey();}
      ProgramCounter.setText(Integer.toString(Program_CounterCpy));
      ProgressBar.setMaximum(Instruction.length); // sets the maximum value of the progressbar component to the length of the Instruction Array 
      Complete.setVisible(false);
      PrintRegisters();
      PrintDataMemory();
      PrintInstructionMemory();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked") 

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jDialog1 = new javax.swing.JDialog();
        jLabel1 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        NextBttn = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        Format = new javax.swing.JComboBox<>();
        jLabel9 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        AssemblyCode = new javax.swing.JTextArea();
        jLabel8 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        CurrentInstruction = new javax.swing.JTextField();
        ProgramCounter = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        ProgressBar = new javax.swing.JProgressBar();
        jScrollPane1 = new javax.swing.JScrollPane();
        Registers = new javax.swing.JTextArea();
        jScrollPane2 = new javax.swing.JScrollPane();
        Memory1 = new javax.swing.JTextArea();
        Complete = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jScrollPane4 = new javax.swing.JScrollPane();
        Memory2 = new javax.swing.JTextArea();

        javax.swing.GroupLayout jDialog1Layout = new javax.swing.GroupLayout(jDialog1.getContentPane());
        jDialog1.getContentPane().setLayout(jDialog1Layout);
        jDialog1Layout.setHorizontalGroup(
            jDialog1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        jDialog1Layout.setVerticalGroup(
            jDialog1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setName("Simulator"); // NOI18N
        setResizable(false);

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel1.setText("Registers ");

        jPanel1.setBackground(new java.awt.Color(48, 52, 112));
        jPanel1.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N

        jLabel4.setBackground(new java.awt.Color(255, 255, 255));
        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("RISC-V SIMULATOR");

        jLabel5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/10872782.png"))); // NOI18N

        NextBttn.setBackground(new java.awt.Color(204, 204, 204));
        NextBttn.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        NextBttn.setText("Next Instruction");
        NextBttn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                NextBttnActionPerformed(evt);
            }
        });

        jButton1.setBackground(new java.awt.Color(255, 153, 51));
        jButton1.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButton1.setForeground(new java.awt.Color(255, 255, 255));
        jButton1.setText("Exit");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setBackground(new java.awt.Color(204, 204, 204));
        jButton2.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButton2.setText("Restart");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        Format.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        Format.setMaximumRowCount(3);
        Format.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Decimal", "Binary", "HexaDecimal" }));
        Format.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                FormatActionPerformed(evt);
            }
        });

        jLabel9.setBackground(new java.awt.Color(255, 255, 255));
        jLabel9.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(255, 255, 255));
        jLabel9.setText("Format ");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 201, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(14, 14, 14)
                        .addComponent(jButton1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel9)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(Format, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(43, 43, 43)))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(NextBttn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(16, 16, 16))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(15, 15, 15)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jButton1)
                                .addComponent(Format, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel9))
                            .addComponent(NextBttn, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(jLabel4))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE)))))
                .addGap(0, 6, Short.MAX_VALUE))
        );

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel2.setText("Data Memory");

        AssemblyCode.setEditable(false);
        AssemblyCode.setColumns(20);
        AssemblyCode.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        AssemblyCode.setRows(5);
        jScrollPane3.setViewportView(AssemblyCode);

        jLabel8.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel8.setText("Assembly Code");

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel3.setText("Program Counter :");

        jLabel6.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel6.setText("Current Instruction :");

        CurrentInstruction.setEditable(false);

        ProgramCounter.setEditable(false);
        ProgramCounter.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N

        jLabel7.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel7.setText("Progress :");

        ProgressBar.setBackground(new java.awt.Color(255, 153, 0));
        ProgressBar.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        ProgressBar.setForeground(new java.awt.Color(0, 0, 0));

        Registers.setEditable(false);
        Registers.setColumns(20);
        Registers.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        Registers.setRows(5);
        Registers.setVerifyInputWhenFocusTarget(false);
        jScrollPane1.setViewportView(Registers);

        Memory1.setEditable(false);
        Memory1.setColumns(20);
        Memory1.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        Memory1.setRows(5);
        jScrollPane2.setViewportView(Memory1);

        Complete.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        Complete.setForeground(new java.awt.Color(0, 102, 0));
        Complete.setText("SIMULATOR COMPLETE!");

        jLabel10.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel10.setText("Instruction Memory");

        Memory2.setEditable(false);
        Memory2.setColumns(20);
        Memory2.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        Memory2.setRows(5);
        jScrollPane4.setViewportView(Memory2);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 184, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 300, Short.MAX_VALUE)
                    .addComponent(jLabel10)
                    .addComponent(jScrollPane4))
                .addGap(42, 42, 42)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 304, Short.MAX_VALUE)
                        .addGroup(layout.createSequentialGroup()
                            .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(18, 18, 18)
                            .addComponent(ProgramCounter, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addComponent(ProgressBar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(CurrentInstruction)
                        .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 143, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(Complete))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2)
                    .addComponent(jLabel8))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 420, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 191, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel3)
                            .addComponent(ProgramCounter, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addComponent(jLabel6)
                        .addGap(12, 12, 12)
                        .addComponent(CurrentInstruction, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel7)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(ProgressBar, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(Complete))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(12, 12, 12)
                        .addComponent(jLabel10)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 197, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents
 
    private void NextBttnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_NextBttnActionPerformed
        // TODO add your handling code here:
       if(Program_CounterCpy > MaxPR) {EndSimulation(); ProgressBar.setValue(ProgressBar.getMaximum()); counter++;}
        CurrentInstruction.setText(Instruction_Memory.get(Program_CounterCpy));
        ProcessInstruction(Instruction_Memory.get(Program_CounterCpy));
        ProgressBar.setValue(counter+1);
        ProgramCounter.setText(Integer.toString(Program_CounterCpy));
        //Program_CounterCpy+=4;
        counter++;
        PrintRegisters();
        PrintDataMemory();
        PrintInstructionMemory();
        
        
    }//GEN-LAST:event_NextBttnActionPerformed


    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        Index i = new Index();
        i.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
         
        Viewer v = new Viewer(Assembly_CodeCpy, intialProgramCounter, intialDataMem) {};
        v.setVisible(true);
        this.dispose();
        
    }//GEN-LAST:event_jButton2ActionPerformed

    private void FormatActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_FormatActionPerformed
        // TODO add your handling code here:
        
     
        
        
    }//GEN-LAST:event_FormatActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Viewer.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Viewer.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Viewer.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Viewer.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
               
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextArea AssemblyCode;
    private javax.swing.JLabel Complete;
    private javax.swing.JTextField CurrentInstruction;
    private javax.swing.JComboBox<String> Format;
    private javax.swing.JTextArea Memory1;
    private javax.swing.JTextArea Memory2;
    private javax.swing.JButton NextBttn;
    private javax.swing.JTextField ProgramCounter;
    private javax.swing.JProgressBar ProgressBar;
    private javax.swing.JTextArea Registers;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JDialog jDialog1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    // End of variables declaration//GEN-END:variables
}