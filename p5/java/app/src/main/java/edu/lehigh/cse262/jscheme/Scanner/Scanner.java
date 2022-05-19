package edu.lehigh.cse262.jscheme.Scanner;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import javax.security.auth.callback.ChoiceCallback;

import edu.lehigh.cse262.jscheme.Scanner.Tokens;

/**
 * Scanner is responsible for taking a string that is the source code of a
 * program, and transforming it into a stream of tokens.
 *
 * [CSE 262] It is tempting to think "if my code doesn't crash when I give it
 * good input, then I have done a good job". However, a good scanner needs to be
 * able to handle incorrect programs. The bare minimum is that the scanner
 * should not crash if the input is invalid. Even better is if the scanner can
 * print a useful diagnostic message about the point in the source code that was
 * incorrect. Best, of course, is if the scanner can somehow "recover" and keep
 * on scanning, so that it can report additional syntax errors.
 *
 * [CSE 262] **In this class, "even better" is good enough**
 *
 * [CSE 262] This is the only java file that you need to edit in p1. The
 * reference solution has ~240 lines of code (plus 43 blank lines and ~210 lines
 * of comments). Your code may be longer or shorter... the line-of-code count is
 * just a reference.
 *
 * [CSE 262] You are allowed to add private methods and fields to this class.
 * You may also add imports.
 */
public class Scanner {
    /** Construct a scanner */
    public Scanner() {//spears left this empty in his solution



    }

    /**
     * scanTokens works through the `source` and transforms it into a list of
     * tokens. It always adds an EOF token at the end.
     *
     * @param source The source code of the program, as one big string, or a
     *               line of code from the REPL.
     *
     * @return A list of tokens
     */
    public TokenStream scanTokens(String source) throws Exception {
        List<Tokens.BaseToken> listoftokens = new ArrayList<>();
        int line = 1;
        int column = 1;
        int i = 0;
        //Loop over the length of the string that we are reading in and making sure that all characters are picked up
        while(i < source.length()) {
            //Checks if the substring is an 'and' and sees if it is followed by correct characters and increments i
            if(source.length()>i+2 &&source.substring(i, i + 3).equals("and") && checkBegEnd(source,i,i+3)) {
                    listoftokens.add(new Tokens.And("and", line, column));
                    i = i + 2;
                    column = column + 2;
            }
            //Checks if the substring is a 'set!' and sees if it is followed by correct characters, increments i
            else if(source.length()>i+3 &&source.substring(i, i + 4).equals("set!") && checkBegEnd(source, i, i+4)) {
                listoftokens.add(new Tokens.Set("set!",line, column));
                i = i + 3;
                column = i + 3;
            }
            //Checks if the substring is a 'define' and sees if it is followed by correct characters, increments i
            else if(source.length()>i+5 && source.substring(i, i + 6).equals("define") && checkBegEnd(source, i, i+6)) {
                listoftokens.add(new Tokens.Define("define", line, column));
                i = i + 5;
                column = column + 5;
            }
            //Checks if the substring is a 'begin' and sees if it is followed by correct characters, increments i
            else if(source.length()>i+4 &&source.substring(i, i + 5).equals("begin") && checkBegEnd(source, i, i+5)) {
                listoftokens.add(new Tokens.Begin("begin", line, column));
                i = i + 4;
                column = column + 4;
            }
            //Checks if the substring is a 'let' and sees if it is followed by correct characters, increments i
            else if(source.length()>i+2 &&source.substring(i, i + 3).equals("let") && checkBegEnd(source, i, i+3)) {
                listoftokens.add(new Tokens.Let("let", line, column));
                i = i + 2;
                column = column + 2;
            }
            //Checks if the substring is a 'cond' and sees if it is followed by correct characters, increments i
            else if(source.length()>i+3 &&source.substring(i, i + 4).equals("cond") && checkBegEnd(source, i, i+4)) {
                listoftokens.add(new Tokens.Cond("cond", line, column));
                i = i + 3;
                column = column + 3;
            }
            //Checks if substring is a 'lambda' and checks if it is followed by correct Characters, increments i
            else if(source.length()>i+5 &&source.substring(i, i + 6).equals("lambda") && checkBegEnd(source, i, i+6)) {
                listoftokens.add(new Tokens.Lambda("lambda", line, column));
                i = i + 5;
                column = column + 5;
            }
            //Checks if substring is a 'if' and checks if followed by correct characters, increments i
            else if(source.length()>i+1 &&source.substring(i, i + 2).equals("if") && checkBegEnd(source, i,i+2)) {
                listoftokens.add(new Tokens.If("if", line, column));
                i = i + 1;
                column = column + 1;
            }
            //Checks if character is an 'or' and check if it is followed by correct characters, increments i
            else if(source.length()>i+1 &&source.substring(i, i + 2).equals("or") && checkBegEnd(source, i, i+2)) {
                listoftokens.add(new Tokens.Or("or", line, column));
                i = i + 1;
                column = column + 1;
            }
            //Checks if substring is a 'quote' literal string, checks if followed by correct characters, increments i
            else if(source.length()>i+4 &&source.substring(i, i + 5).equals("quote") && checkBegEnd(source, i, i+5)) {
                listoftokens.add(new Tokens.Quote("quote", line, column));
                i = i + 4;
                column = column + 4;
            }
            //Checks if the substring is a character, from 1-9 or a-z in whichever case, and checks if followed by correct characters
            else if(source.length()>i+1 && source.substring(i, i+2).equals("#\\")) {
                if(source.length() > i + 8 && source.substring(i+2, i + 9).equals("newline") && checkBegEnd(source, i, i+9)) {
                    listoftokens.add(new Tokens.Char(source.substring(i+2,i+9), line, column, '\n'));
                    i = i + 6;
                    column = column + 6;
                } else if(source.length() > i + 6 && source.substring(i+2,i+7).equals("space") && checkBegEnd(source, i, i+7)) {
                    listoftokens.add(new Tokens.Char(source.substring(i+2,i+7), line, column, ' '));
                    i = i + 4;
                    column = column + 4;
                } else if(source.length() > i + 4 && source.substring(i+2, i+5).equals("tab") && checkBegEnd(source, i, i+5)) {
                    listoftokens.add(new Tokens.Char(source.substring(i+2,i+5), line, column, '\t'));
                    i = i + 4;
                    column = column + 4;
                } else if(source.length() > i + 2 && source.substring(i+2, i+3).equals("\\") && checkBegEnd(source, i, i+3)) {
                    listoftokens.add(new Tokens.Char(source.substring(i+2,i+3), line, column, '\\'));
                }
                else if(source.length() > i + 2 && checkBegEnd(source, i, i+3)) {
                    listoftokens.add(new Tokens.Char(source.substring(i+2,i+3), line, column, source.charAt(i+2)));
                } else {
                    throw new Exception("Syntax Error: Unrecognized character on line " + line + ", column " + column);
                }
                i = i + 2;
                column = column + 2;
            }
            //Checks if the substring is the correct boolean for true, which should be lowercase t, incrememnts i
            else if(source.length()>i+1 &&source.substring(i, i + 2).equals("#t") && checkBegEnd(source, i, i+2)) {
                listoftokens.add(new Tokens.Bool("#t", line, column,true));
                i = i + 1;
                column = column + 1;
            }
            //Check if the substring is the correct boolean for false, which should be a lowercase f, increments i
            else if(source.length()>i+1 &&source.substring(i, i + 2).equals("#f") && checkBegEnd(source, i, i+2)) {
                listoftokens.add(new Tokens.Bool("#f", line, column,false));
                i = i + 1;
                column = column + 1;
            }
            //Checks substring that it is the correct beginning of vector and increments i accordingly
            else if(source.length() > i+1 && source.substring(i, i+2).equals("#(")) {
                listoftokens.add(new Tokens.Vec("#(", line, column));
                i = i + 1;
                column = column + 1;
            }
            //If the character is a newline character '\n', line is then incremented to show accurate xml printings.
            else if(source.length()>i+1 &&source.substring(i, i + 1).equals("\n")) {
                column = -1;
                line++;
            }
            //If the character is a space, increments column by the length of the space.
            else if(source.substring(i,i+1).equals("\"")) {
                //int length = getString(listoftokens,source,i,line,column);
                i = i +1;
                String val = "";
                while(source.length() > i && (!(source.substring(i,i+1).equals("\n")) && !(source.substring(i, i+1).equals("\"")))) {
                    if(source.length() > i+1 && source.substring(i,i+1).equals("\\")) {
                        if(source.length() > i + 1 && source.substring(i, i+1).equals("\"")) {
                            val = val + source.substring(i+1, i+2);
                            i = i + 1;
                            column = column + 1;
                        } else if(source.length() > i + 1 && source.substring(i, i+1).equals("t")) {
                            val = val + source.substring(i, i+1);
                            i = i + 1;
                            column = column + 1;
                        } else if(source.length() > i + 1 && source.substring(i, i+1).equals("\\")) {
                            val = val + "\\\\";
                            i = i + 1; 
                            column = column + 1;
                        } else if(source.length() > i + 1 && source.substring(i, i+1).equals("n")) {
                            val  = val + source.substring(i, i+2);
                            i = i + 1;
                            column = column + 1;
                        } else {
                            throw new Exception("Syntax Error: Unescaped character on line " + line + ", column " + column);
                        }
                    } else if(source.substring(i,i+1).equals("\\\\")) {
                        val = val + "\\\\";
                    } else {
                        val = val + source.substring(i, i+1);
                    }
                    i = i+ 1;
                    column = column + 1;
                }

                if(source.length() > i && source.substring(i,i+1).equals("\"")) {
                    listoftokens.add(new Tokens.Str("\""+val+"\"", line, column, val));
                    column = column + 1;
                } else {
                    throw new Exception("Syntax Error: Unclosed string on line " + line + ", column " + column);
                }
                
            }
            //If the character is a space, increments column by the length of the space.
            // else if(source.substring(i,i+2).equals("(\"")) {
            //     listoftokens.add(new Tokens.LeftParen("(", line, column));
            //     //int length = getString(listoftokens,source,i,line,column);
            //     i = i +1;
            //     String val = "";
            //     while(source.length() > i + 1 && (!source.substring(i,i+1).equals("\n") || !source.substring(i, i+1).equals("\""))) {
            //         if(source.length() > i+1 && source.substring(i,i+1).equals("\\")) {
            //             if(source.length() > i + 1 && source.substring(i, i+1).equals("\"")) {
            //                 val = val + source.substring(i+1, i+2);
            //                 i = i + 1;
            //                 column = column + 1;
            //             } else if(source.length() > i + 1 && source.substring(i, i+1).equals("t")) {
            //                 val = val + source.substring(i, i+1);
            //                 i = i + 1;
            //                 column = column + 1;
            //             } else if(source.length() > i + 1 && source.substring(i, i+1).equals("\\")) {
            //                 val = val + "\\\\";
            //                 i = i + 1; 
            //                 column = column + 1;
            //             } else if(source.length() > i + 1 && source.substring(i, i+1).equals("n")) {
            //                 val  = val + source.substring(i, i+2);
            //                 i = i + 1;
            //                 column = column + 1;
            //             } else {
            //                 throw new Exception("Syntax Error: Unescaped character on line " + line + ", column " + column);
            //             }
            //         } else if(source.substring(i,i+1).equals("\\\\")) {
            //             val = val + "\\\\";
            //         } else {
            //             val = val + source.substring(i, i+1);
            //         }
            //         i = i+ 1;
            //         column = column + 1;
            //     }

            //     if(source.length() > i && source.substring(i,i+1).equals("\"")) {
            //         listoftokens.add(new Tokens.Str("\""+val+"\"", line, column, val));
            //         column = column + 1;
            //         i = i+ 1;
            //     } else if(source.length() <= i || source.substring(i,i+1).equals("\n")) {
            //         throw new Exception("Syntax Error: Unclosed string on line " + line + ", column " + column);
            //     }
                
            // }
            //If the chatacter is a comment starter ';', then the whole thing is ignored and incremented by the length of the comment
            else if(source.substring(i,i+1).equals(";")) {
                while(source.length()>i+1 && !source.substring(i, i+1).equals("\n")) {
                    i++;
                }
                column =-1;
                line++;
            }
            //If character is an Abbreviation, it is added to the token list
            else if(source.substring(i,i+1).equals("'")) {
                listoftokens.add(new Tokens.Abbrev("'", line, column));
            }
            //If the character is a right parenthesis, added to token list 
            else if(source.substring(i,i+1).equals("(")) {
                listoftokens.add(new Tokens.LeftParen("(", line, column));
            }
            //If character is a left parenthesis, added to token list
            else if(source.substring(i,i+1).equals(")")) {
                listoftokens.add(new Tokens.RightParen(")", line, column));
            }
            //If character is plus sign, added to token ist and checks if following characters are correct
            else if(source.substring(i,i+1).equals("+")&& checkBegEnd(source, i, i+1)) {
                listoftokens.add(new Tokens.Identifier("+", line, column));
            }
            else if(source.substring(i,i+1).equals("-")&& checkBegEnd(source, i, i+1)) {
                listoftokens.add(new Tokens.Identifier("-", line, column));
            }
            //Checks if the first character is an identifier, then continues on to check the rest of the characters while they match the identifierlist, stops when it isnt and adds those characters to an identifier string
            else if(source.substring(i, i+1).matches("[A-Za-z\\!\\$\\%\\&\\*\\/\\:\\<\\=\\>\\?\\~\\_\\^]")) {
                String identifier = "";
                identifier = identifier + source.substring(i, i+1);
                i++;
                while(i < source.length() && source.substring(i, i+1).matches("[A-Za-z\\!\\$\\%\\&\\*\\/\\:\\<\\=\\>\\?\\~\\_\\^0-9\\+\\-\\.]")) {
                    identifier = identifier + source.substring(i, i+1);
                    i++;
                }
                listoftokens.add(new Tokens.Identifier(identifier, line, column));
                i = i -1;
                column = column + identifier.length() -1;
            }
            //Checks if string is a number, checks if it is double or integer, adds it to the list accordingly
            else if("0123456789+-".indexOf(source.substring(i,i+1)) != -1 && checkNum(source, i)) {
                String num = "";
                int val =0;
                double valD = 0;
                if(!source.substring(i,i+1).equals("+")) {
                    num = source.substring(i,i+1);
                }else{
                    num = " ";
                }
                i = i + 1;
                if(i == source.length()) {
                    listoftokens.add(new Tokens.Int(num, line, column, Integer.parseInt(num.trim())));
                    i = i - 1;
                }
                else {
                    boolean isDouble = false;
                    while(source.length()>i &&(source.substring(i,i+1).matches("[0-9]")||source.substring(i,i+1).equals("."))){
                        if(source.substring(i,i+1).equals(".")){
                            if(isDouble){
                                 new Exception("Error: Too many decimal points in number col = "+column+" line = "+line);
                            }
                            isDouble = true;
                            if(source.length() <= i + 1 || !source.substring(i+1,i+2).matches("[0-9]")){
                                throw new Exception("Syntax Error: Number missing after decimal point line = "+line+" col = "+column);
                            }
                        }
                        num = num +source.substring(i,i+1);
                        i++;
                    }
                    if(isDouble) {
                        listoftokens.add(new Tokens.Dbl(num, line, column, Double.parseDouble(num.trim())));
                    } else {
                        listoftokens.add(new Tokens.Int(num, line, column, Integer.parseInt(num.trim())));
                    }
                    column = column + num.length() -1;
                    i = i - 1;
                }
        }
            //Checks if it not a whitespace character, throws error since it is an unsupported character
            else if(!Character.isWhitespace(source.charAt(i))){
                String returnVal = "";
                while(i<source.length() && !Character.isWhitespace(source.charAt(i))){
                    returnVal = returnVal+ source.substring(i, i+1);
                    i++;
                }
                throw new Exception("Syntax Error: Unrecognized token on line " + line + ", column " + column);
            }
            //i and column incremented to account for whitespacing
            i = i + 1;
            column = column + 1;
        }
        //Eof added since the scanning is done
        listoftokens.add(new Tokens.Eof("eof",line,column));
        //returns the list of tokens in the tokenstream
        TokenStream test = new TokenStream(listoftokens);
        
        return test;
    }

    /**
     * 
     * @param listOfTokens
     * @param source
     * @param i
     * @param line
     * @param column
     * @return
     * @throws Exception
     * Method will check if the string is a string and if it is it will return the length of the string.
     * Will throw errors if the string is not closed and if the string is not valid due to an escape sequence.
     */
    private int getString(List<Tokens.BaseToken> listOfTokens, String source,int i,int line, int column) throws Exception{
        i++;
        String word = "";
        while(source.length()>i &&!source.substring(i,i+1).equals("\"")){
            if(source.length()>(i+1)&&source.substring(i,i+1).equals("\n")){
                throw new Exception("Error: Invalid escape sequence column: "+column+" line: "+line);
            }
            word = word + source.substring(i,i+1);
            i++;
        }
        if(!source.substring(i,i+1).equals("\"")){
            throw new Exception("Error: string not closed column: "+column+" line: "+line);
        }
        listOfTokens.add(new Tokens.Str(word,line,column,word));
        return word.length()+1;

    }

    /**
     * 
     * @param source
     * @param index
     * @param column
     * @param line
     * @return
     * @throws Exception
     * Method is called when an integer or decimal is read in. Checks if the next character is a valid number. Also
     * checks if the chatacter is a double or integer. Throws exceptions if too many decimal places are found.
     */
    private Boolean checkNum(String source, int index) throws Exception{
        int i = index;
        if(i == source.length()) {
            if(source.substring(i,i+1).equals("+") || source.substring(i,i+1).equals("-")){
                return false;
            }
            else {
                return true;
            }
        }
        i= i + 1;
        while(i < source.length() && source.substring(i,i+1).matches("[0-9\\.]")){
            i = i + 1;
        }
        if(i < source.length() && source.substring(i, i + 1).matches("[\\(|\\)|\\n| |\\s|\\t]") || i == source.length()) {
            return true;
        }
        return false;
    } 

    /**
     * Checks the beginning and ending of the string to see if it a correct special word from scheme
     * @param source
     * @param start
     * @param end
     * @return
     */
    private boolean checkBegEnd(String source, int start, int end){
        boolean startBool = start == 0 || ("()\n \t".indexOf(source.charAt(start-1)) != -1 );
        boolean endBool = source.length()>end && ("()\n \t".indexOf(source.charAt(end)) != -1 ) || source.length() == end;
        return startBool && endBool;
    }
}


