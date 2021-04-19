/**
 * Hack VM part 1
 * Michael Outmesguine
 * CS 3650.03 (S21)
 * 4/18/21
 */
package hackvm1;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JFileChooser;


public class HackVM1 {

    static File fileIn; //("C:/Users/Me/Desktop/nand2tetris/nand2tetris/nand2tetris/projects/07/StackArithmetic/SimpleAdd.vm");
    static ArrayList<String> read = new ArrayList();
    static ArrayList<String> instList = new ArrayList();
    static File output; // = new File("C:/Users/Me/Desktop/nand2tetris/nand2tetris/nand2tetris/projects/06/max/Max.hack");
    static Scanner input;
    static FileWriter writer;
    static int result;
    
    public static void main(String[] args) throws IOException {
        //Pick input file and create output file
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
        int fileResult = fileChooser.showOpenDialog(fileChooser);
        if(fileResult == JFileChooser.APPROVE_OPTION){
            fileIn = fileChooser.getSelectedFile();
        }else{
            System.out.println("not a valid file");
            System.exit(1);
        }
        
        output = new File(fileIn.getAbsolutePath().substring(0, fileIn.getAbsolutePath().indexOf(".")) + ".asm");
        
        String filePath = fileIn.getAbsolutePath();
        filePath = filePath.replace("\\", "$");
        Pattern p = Pattern.compile(".*\\$ *(.*) *\\$.*");
        Matcher m = p.matcher(filePath);
        m.find();
        String fileName = m.group(1);
        
        
        //Read input
        input = new Scanner(fileIn);
        while(input.hasNext()){
            read.add(input.nextLine());
        }

        //Remove comments and empty lines
        for(int index = 0; index < read.size(); index++){
            String readLine = read.get(index);
            if(readLine.contains("//")){
                readLine = readLine.substring(0, readLine.indexOf("//"));
            }
            if(!readLine.equals(""))
                instList.add(readLine);
        }
        
        //create output file
        output.createNewFile();
        writer = new FileWriter(output);
        
        int labelCounter = 0;
        
        /**
         * Parse each input and write the equivalent Hack assembly code
         * Currently supporting:
         * stack arithmetic
         * push constant/local/argument/this/that x
         */
        for(String str : instList){
            //stack arithmetic
            if(str.equals("add")){
                writer.write("@SP\n" +
                    "M=M-1\n" +
                    "A=M\n" +
                    "D=M\n" +
                    "@SP\n" +
                    "M=M-1\n" +
                    "A=M\n" +
                    "M=M+D\n" +
                    "@SP\n" +
                    "M=M+1\n");
            }else if(str.equals("sub")){
                writer.write("@SP\n" +
                    "M=M-1\n" +
                    "A=M\n" +
                    "D=M\n" +
                    "@SP\n" +
                    "M=M-1\n" +
                    "A=M\n" +
                    "M=M-D\n" +
                    "@SP\n" +
                    "M=M+1\n");
            }else if(str.equals("neg")){
                writer.write("@SP\n" +
                    "A=M-1\n" +
                    "M=-M\n");
            }else if(str.equals("eq")){
                String thisLabel = "label" + Integer.toString(labelCounter);
                labelCounter++;
                writer.write("@SP\n" +
                    "M=M-1\n" +
                    "A=M\n" +
                    "D=M\n" +
                    "@SP\n" +
                    "M=M-1\n" +
                    "A=M\n" +
                    "D=D-M\n" +
                    "M=-1\n" +
                    "@" + thisLabel + "\n" +
                    "D;JEQ\n" +
                    "@SP\n" +
                    "A=M\n" +
                    "M=0\n" +
                    "(" + thisLabel + ")\n" +
                    "@SP\n" +
                    "M=M+1\n");
            }else if(str.equals("gt")){
                String thisLabel = "label" + Integer.toString(labelCounter);
                labelCounter++;
                writer.write("@SP\n" +
                    "M=M-1\n" +
                    "A=M\n" +
                    "D=M\n" +
                    "@SP\n" +
                    "M=M-1\n" +
                    "A=M\n" +
                    "D=M-D\n" +
                    "M=-1\n" +
                    "@" + thisLabel + "\n" +
                    "D;JGT\n" +
                    "@SP\n" +
                    "A=M\n" +
                    "M=0\n" +
                    "(" + thisLabel + ")\n" +
                    "@SP\n" +
                    "M=M+1\n");
            }else if(str.equals("lt")){
                String thisLabel = "label" + Integer.toString(labelCounter);
                labelCounter++;
                writer.write("@SP\n" +
                    "M=M-1\n" +
                    "A=M\n" +
                    "D=M\n" +
                    "@SP\n" +
                    "M=M-1\n" +
                    "A=M\n" +
                    "D=M-D\n" +
                    "M=-1\n" +
                    "@" + thisLabel + "\n" +
                    "D;JLT\n" +
                    "@SP\n" +
                    "A=M\n" +
                    "M=0 \n" +
                    "(" + thisLabel + ")\n" +
                    "@SP\n" +
                    "M=M+1\n");
            }else if(str.equals("and")){
                writer.write("@SP\n" +
                    "M=M-1\n" +
                    "A=M\n" +
                    "D=M\n" +
                    "@SP\n" +
                    "M=M-1\n" +
                    "A=M\n" +
                    "M=M&D\n" +
                    "@SP\n" +
                    "M=M+1\n");
            }else if(str.equals("or")){
                writer.write("@SP\n" +
                    "M=M-1\n" +
                    "A=M\n" +
                    "D=M\n" +
                    "@SP\n" +
                    "M=M-1\n" +
                    "A=M\n" +
                    "M=M|D\n" +
                    "@SP\n" +
                    "M=M+1\n");
            }else if(str.equals("not")){
                writer.write("@SP\n" +
                    "A=M-1\n" +
                    "M=!M\n");
            }
            
            
            //push segment index    
            else if(str.contains("push constant")){
                String[] strContents = str.split(" ");
                writer.write("@" + strContents[2] + "\n" +
                    "D=A\n" +
                    "@SP\n" +
                    "A=M\n" +
                    "M=D\n" +
                    "@SP\n" +
                    "M=M+1\n");
            }else if(str.contains("push local")){
                String[] strContents = str.split(" ");
                writer.write("@" + strContents[2] + "\n" +
                    "D=A\n" +
                    "@LCL\n" +
                    "A=M+D\n" +
                    "D=M\n" +
                    "@SP\n" +
                    "A=M\n" +
                    "M=D\n" +
                    "@SP\n" +
                    "M=M+1\n");
            }else if(str.contains("push argument")){
                String[] strContents = str.split(" ");
                writer.write("@" + strContents[2] + "\n" +
                    "D=A\n" +
                    "@ARG\n" +
                    "A=M+D\n" +
                    "D=M\n" +
                    "@SP\n" +
                    "A=M\n" +
                    "M=D\n" +
                    "@SP\n" +
                    "M=M+1\n");
            }else if(str.contains("push this")){
                String[] strContents = str.split(" ");
                writer.write("@" + strContents[2] + "\n" +
                    "D=A\n" +
                    "@THIS\n" +
                    "A=M+D\n" +
                    "D=M\n" +
                    "@SP\n" +
                    "A=M\n" +
                    "M=D\n" +
                    "@SP\n" +
                    "M=M+1\n");
            }else if(str.contains("push that")){
                String[] strContents = str.split(" ");
                writer.write("@" + strContents[2] + "\n" +
                    "D=A\n" +
                    "@THAT\n" +
                    "A=M+D\n" +
                    "D=M\n" +
                    "@SP\n" +
                    "A=M\n" +
                    "M=D\n" +
                    "@SP\n" +
                    "M=M+1\n");
            }else if(str.contains("push pointer")){
                String[] strContents = str.split(" ");
                writer.write("@" + strContents[2] + "\n" +
                    "D=A\n" +
                    "@3\n" +
                    "A=A+D\n" +
                    "D=M\n" +
                    "@SP\n" +
                    "A=M\n" +
                    "M=D\n" +
                    "@SP\n" +
                    "M=M+1\n");
            }else if(str.contains("push temp")){
                String[] strContents = str.split(" ");
                writer.write("@" + strContents[2] + "\n" +
                    "D=A\n" +
                    "@5\n" +
                    "A=A+D\n" +
                    "D=M\n" +
                    "@SP\n" +
                    "A=M\n" +
                    "M=D\n" +
                    "@SP\n" +
                    "M=M+1\n");
            }else if(str.contains("push static")){
                String[] strContents = str.split(" ");
                writer.write("@" + fileName + "." + strContents[2] + "\n" +
                    "D=M\n" +
                    "@SP\n" +
                    "A=M\n" +
                    "M=D\n" +
                    "@SP\n" +
                    "M=M+1\n");
            }
            
            
            //pop segment index
            else if(str.contains("pop local")){
                String[] strContents = str.split(" ");
                writer.write("@" + strContents[2] + "\n" +
                    "D=A\n" +
                    "@LCL\n" +
                    "D=M+D\n" +
                    "@R15\n" +
                    "M=D\n" +
                    "@SP\n" +
                    "M=M-1\n" +
                    "A=M\n" +
                    "D=M\n" +
                    "@R15\n" +
                    "A=M\n" +
                    "M=D\n");
            }else if(str.contains("pop argument")){
                String[] strContents = str.split(" ");
                writer.write("@" + strContents[2] + "\n" +
                    "D=A\n" +
                    "@ARG\n" +
                    "D=M+D\n" +
                    "@R15\n" +
                    "M=D\n" +
                    "@SP\n" +
                    "M=M-1\n" +
                    "A=M\n" +
                    "D=M\n" +
                    "@R15\n" +
                    "A=M\n" +
                    "M=D\n");
            }else if(str.contains("pop this")){
                String[] strContents = str.split(" ");
                writer.write("@" + strContents[2] + "\n" +
                    "D=A\n" +
                    "@THIS\n" +
                    "D=M+D\n" +
                    "@R15\n" +
                    "M=D\n" +
                    "@SP\n" +
                    "M=M-1\n" +
                    "A=M\n" +
                    "D=M\n" +
                    "@R15\n" +
                    "A=M\n" +
                    "M=D\n");
            }else if(str.contains("pop that")){
                String[] strContents = str.split(" ");
                writer.write("@" + strContents[2] + "\n" +
                    "D=A\n" +
                    "@THAT\n" +
                    "D=M+D\n" +
                    "@R15\n" +
                    "M=D\n" +
                    "@SP\n" +
                    "M=M-1\n" +
                    "A=M\n" +
                    "D=M\n" +
                    "@R15\n" +
                    "A=M\n" +
                    "M=D\n");
            }else if(str.contains("pop pointer")){
                String[] strContents = str.split(" ");
                writer.write("@" + strContents[2] + "\n" +
                    "D=A\n" +
                    "@3\n" +
                    "D=A+D\n" +
                    "@R15\n" +
                    "M=D\n" +
                    "@SP\n" +
                    "M=M-1\n" +
                    "A=M\n" +
                    "D=M\n" +
                    "@R15\n" +
                    "A=M\n" +
                    "M=D\n");
            }else if(str.contains("pop temp")){
                String[] strContents = str.split(" ");
                writer.write("@" + strContents[2] + "\n" +
                    "D=A\n" +
                    "@5\n" +
                    "D=A+D\n" +
                    "@R15\n" +
                    "M=D\n" +
                    "@SP\n" +
                    "M=M-1\n" +
                    "A=M\n" +
                    "D=M\n" +
                    "@R15\n" +
                    "A=M\n" +
                    "M=D\n");
            }else if(str.contains("pop static")){
                String[] strContents = str.split(" ");
                writer.write("@SP\n" +
                    "M=M-1\n" +
                    "A=M\n" +
                    "D=M\n" +
                    "@" + fileName + "." + strContents[2] + "\n"  +
                    "M=D\n");
            }
            
            
            
            
        }

        writer.flush();
        writer.close();
        
    }
    
}
