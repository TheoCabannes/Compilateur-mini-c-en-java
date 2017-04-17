/* The following code was generated by JFlex 1.6.1 */


package mini_c;

import java_cup.runtime.*;
import java.util.*;
import static mini_c.sym.*;


/**
 * This class is a scanner generated by 
 * <a href="http://www.jflex.de/">JFlex</a> 1.6.1
 * from the specification file <tt>src/mini_c/Lexer.flex</tt>
 */
class Lexer implements java_cup.runtime.Scanner {

  /** This character denotes the end of file */
  public static final int YYEOF = -1;

  /** initial size of the lookahead buffer */
  private static final int ZZ_BUFFERSIZE = 16384;

  /** lexical states */
  public static final int YYINITIAL = 0;

  /**
   * ZZ_LEXSTATE[l] is the state in the DFA for the lexical state l
   * ZZ_LEXSTATE[l+1] is the state in the DFA for the lexical state l
   *                  at the beginning of a line
   * l is of the form l = 2*k, k a non negative integer
   */
  private static final int ZZ_LEXSTATE[] = { 
     0, 0
  };

  /** 
   * Translates characters to character classes
   */
  private static final String ZZ_CMAP_PACKED = 
    "\11\0\1\3\1\2\1\52\1\3\1\1\22\0\1\3\1\21\1\0"+
    "\1\0\1\7\1\0\1\20\1\13\1\45\1\46\1\5\1\24\1\44"+
    "\1\25\1\0\1\4\1\15\7\10\2\11\1\0\1\51\1\22\1\16"+
    "\1\23\2\0\6\12\24\7\1\0\1\0\2\0\1\7\1\0\2\12"+
    "\1\34\1\12\1\36\1\35\1\7\1\41\1\26\2\7\1\37\1\7"+
    "\1\27\1\43\2\7\1\32\1\31\1\30\1\33\1\7\1\40\1\14"+
    "\1\7\1\42\1\47\1\17\1\50\7\0\1\52\34\0\4\7\4\0"+
    "\1\7\12\0\1\7\4\0\1\7\5\0\27\7\1\0\37\7\1\0"+
    "\u01ca\7\4\0\14\7\16\0\5\7\7\0\1\7\1\0\1\7\201\0"+
    "\5\7\1\0\2\7\2\0\4\7\10\0\1\7\1\0\3\7\1\0"+
    "\1\7\1\0\24\7\1\0\123\7\1\0\213\7\10\0\236\7\11\0"+
    "\46\7\2\0\1\7\7\0\47\7\7\0\1\7\100\0\33\7\5\0"+
    "\3\7\30\0\1\7\24\0\53\7\25\0\12\6\4\0\2\7\1\0"+
    "\143\7\1\0\1\7\17\0\2\7\7\0\2\7\12\6\3\7\2\0"+
    "\1\7\20\0\1\7\1\0\36\7\35\0\131\7\13\0\1\7\16\0"+
    "\12\6\41\7\11\0\2\7\4\0\1\7\5\0\26\7\4\0\1\7"+
    "\11\0\1\7\3\0\1\7\27\0\31\7\107\0\1\7\1\0\13\7"+
    "\127\0\66\7\3\0\1\7\22\0\1\7\7\0\12\7\4\0\12\6"+
    "\1\0\7\7\1\0\7\7\5\0\10\7\2\0\2\7\2\0\26\7"+
    "\1\0\7\7\1\0\1\7\3\0\4\7\3\0\1\7\20\0\1\7"+
    "\15\0\2\7\1\0\3\7\4\0\12\6\4\7\7\0\1\7\11\0"+
    "\6\7\4\0\2\7\2\0\26\7\1\0\7\7\1\0\2\7\1\0"+
    "\2\7\1\0\2\7\37\0\4\7\1\0\1\7\7\0\12\6\2\0"+
    "\3\7\20\0\11\7\1\0\3\7\1\0\26\7\1\0\7\7\1\0"+
    "\2\7\1\0\5\7\3\0\1\7\22\0\1\7\17\0\2\7\4\0"+
    "\12\6\1\0\1\7\23\0\10\7\2\0\2\7\2\0\26\7\1\0"+
    "\7\7\1\0\2\7\1\0\5\7\3\0\1\7\36\0\2\7\1\0"+
    "\3\7\4\0\12\6\1\0\1\7\21\0\1\7\1\0\6\7\3\0"+
    "\3\7\1\0\4\7\3\0\2\7\1\0\1\7\1\0\2\7\3\0"+
    "\2\7\3\0\3\7\3\0\14\7\26\0\1\7\25\0\12\6\11\0"+
    "\1\7\13\0\10\7\1\0\3\7\1\0\27\7\1\0\12\7\1\0"+
    "\5\7\3\0\1\7\32\0\2\7\6\0\2\7\4\0\12\6\25\0"+
    "\10\7\1\0\3\7\1\0\27\7\1\0\12\7\1\0\5\7\3\0"+
    "\1\7\40\0\1\7\1\0\2\7\4\0\12\6\1\0\2\7\22\0"+
    "\10\7\1\0\3\7\1\0\51\7\2\0\1\7\20\0\1\7\21\0"+
    "\2\7\4\0\12\6\12\0\6\7\5\0\22\7\3\0\30\7\1\0"+
    "\11\7\1\0\1\7\2\0\7\7\37\0\12\6\21\0\60\7\1\0"+
    "\2\7\13\0\10\7\11\0\12\6\47\0\2\7\1\0\1\7\2\0"+
    "\2\7\1\0\1\7\2\0\1\7\6\0\4\7\1\0\7\7\1\0"+
    "\3\7\1\0\1\7\1\0\1\7\2\0\2\7\1\0\4\7\1\0"+
    "\2\7\11\0\1\7\2\0\5\7\1\0\1\7\11\0\12\6\2\0"+
    "\4\7\40\0\1\7\37\0\12\6\26\0\10\7\1\0\44\7\33\0"+
    "\5\7\163\0\53\7\24\0\1\7\12\6\6\0\6\7\4\0\4\7"+
    "\3\0\1\7\3\0\2\7\7\0\3\7\4\0\15\7\14\0\1\7"+
    "\1\0\12\6\6\0\46\7\1\0\1\7\5\0\1\7\2\0\53\7"+
    "\1\0\u014d\7\1\0\4\7\2\0\7\7\1\0\1\7\1\0\4\7"+
    "\2\0\51\7\1\0\4\7\2\0\41\7\1\0\4\7\2\0\7\7"+
    "\1\0\1\7\1\0\4\7\2\0\17\7\1\0\71\7\1\0\4\7"+
    "\2\0\103\7\45\0\20\7\20\0\125\7\14\0\u026c\7\2\0\21\7"+
    "\1\0\32\7\5\0\113\7\3\0\3\7\17\0\15\7\1\0\4\7"+
    "\16\0\22\7\16\0\22\7\16\0\15\7\1\0\3\7\17\0\64\7"+
    "\43\0\1\7\3\0\2\7\3\0\12\6\46\0\12\6\6\0\130\7"+
    "\10\0\51\7\1\0\1\7\5\0\106\7\12\0\35\7\51\0\12\6"+
    "\36\7\2\0\5\7\13\0\54\7\25\0\7\7\10\0\12\6\46\0"+
    "\27\7\11\0\65\7\53\0\12\6\6\0\12\6\15\0\1\7\135\0"+
    "\57\7\21\0\7\7\4\0\12\6\51\0\36\7\15\0\2\7\12\6"+
    "\54\7\32\0\44\7\34\0\12\6\3\0\3\7\12\6\44\7\153\0"+
    "\4\7\1\0\4\7\3\0\2\7\11\0\300\7\100\0\u0116\7\2\0"+
    "\6\7\2\0\46\7\2\0\6\7\2\0\10\7\1\0\1\7\1\0"+
    "\1\7\1\0\1\7\1\0\37\7\2\0\65\7\1\0\7\7\1\0"+
    "\1\7\3\0\3\7\1\0\7\7\3\0\4\7\2\0\6\7\4\0"+
    "\15\7\5\0\3\7\1\0\7\7\53\0\1\52\1\52\25\0\2\7"+
    "\23\0\1\7\34\0\1\7\15\0\1\7\20\0\15\7\3\0\33\7"+
    "\107\0\1\7\4\0\1\7\2\0\12\7\1\0\1\7\3\0\5\7"+
    "\6\0\1\7\1\0\1\7\1\0\1\7\1\0\4\7\1\0\13\7"+
    "\2\0\4\7\5\0\5\7\4\0\1\7\21\0\51\7\u0a77\0\57\7"+
    "\1\0\57\7\1\0\205\7\6\0\4\7\3\0\2\7\14\0\46\7"+
    "\1\0\1\7\5\0\1\7\2\0\70\7\7\0\1\7\20\0\27\7"+
    "\11\0\7\7\1\0\7\7\1\0\7\7\1\0\7\7\1\0\7\7"+
    "\1\0\7\7\1\0\7\7\1\0\7\7\120\0\1\7\u01d5\0\3\7"+
    "\31\0\11\7\7\0\5\7\2\0\5\7\4\0\126\7\6\0\3\7"+
    "\1\0\132\7\1\0\4\7\5\0\51\7\3\0\136\7\21\0\33\7"+
    "\65\0\20\7\u0200\0\u19b6\7\112\0\u51cd\7\63\0\u048d\7\103\0\56\7"+
    "\2\0\u010d\7\3\0\20\7\12\6\2\7\24\0\57\7\20\0\31\7"+
    "\10\0\120\7\47\0\11\7\2\0\147\7\2\0\4\7\1\0\4\7"+
    "\14\0\13\7\115\0\12\7\1\0\3\7\1\0\4\7\1\0\27\7"+
    "\25\0\1\7\7\0\64\7\16\0\62\7\34\0\12\6\30\0\6\7"+
    "\3\0\1\7\4\0\12\6\34\7\12\0\27\7\31\0\35\7\7\0"+
    "\57\7\34\0\1\7\12\6\26\0\12\6\6\0\51\7\27\0\3\7"+
    "\1\0\10\7\4\0\12\6\6\0\27\7\3\0\1\7\5\0\60\7"+
    "\1\0\1\7\3\0\2\7\2\0\5\7\2\0\1\7\1\0\1\7"+
    "\30\0\3\7\2\0\13\7\7\0\3\7\14\0\6\7\2\0\6\7"+
    "\2\0\6\7\11\0\7\7\1\0\7\7\221\0\43\7\15\0\12\6"+
    "\6\0\u2ba4\7\14\0\27\7\4\0\61\7\u2104\0\u016e\7\2\0\152\7"+
    "\46\0\7\7\14\0\5\7\5\0\1\7\1\0\12\7\1\0\15\7"+
    "\1\0\5\7\1\0\1\7\1\0\2\7\1\0\2\7\1\0\154\7"+
    "\41\0\u016b\7\22\0\100\7\2\0\66\7\50\0\15\7\66\0\2\7"+
    "\30\0\3\7\31\0\1\7\6\0\5\7\1\0\207\7\7\0\1\7"+
    "\13\0\12\6\7\0\32\7\4\0\1\7\1\0\32\7\13\0\131\7"+
    "\3\0\6\7\2\0\6\7\2\0\6\7\2\0\3\7\3\0\2\7"+
    "\3\0\2\7\31\0\14\7\1\0\32\7\1\0\23\7\1\0\2\7"+
    "\1\0\17\7\2\0\16\7\42\0\173\7\105\0\65\7\u010b\0\35\7"+
    "\3\0\61\7\57\0\37\7\21\0\33\7\65\0\36\7\2\0\44\7"+
    "\4\0\10\7\1\0\5\7\52\0\236\7\2\0\12\6\u0356\0\6\7"+
    "\2\0\1\7\1\0\54\7\1\0\2\7\3\0\1\7\2\0\27\7"+
    "\252\0\26\7\12\0\32\7\106\0\70\7\6\0\2\7\100\0\1\7"+
    "\17\0\4\7\1\0\3\7\1\0\33\7\54\0\35\7\203\0\66\7"+
    "\12\0\26\7\12\0\23\7\215\0\111\7\u03ba\0\65\7\56\0\12\6"+
    "\23\0\55\7\40\0\31\7\7\0\12\6\11\0\44\7\17\0\12\6"+
    "\103\0\60\7\16\0\4\7\13\0\12\6\u0116\0\12\6\u01d6\0\12\6"+
    "\u0176\0\12\6\46\0\53\7\25\0\12\6\u0216\0\12\6\u0716\0\u036f\7"+
    "\221\0\143\7\u0b9d\0\u042f\7\u33d1\0\u0239\7\47\0\12\6\346\0\12\6"+
    "\u03a6\0\105\7\13\0\1\7\102\0\15\7\u4060\0\2\7\u23fe\0\125\7"+
    "\1\0\107\7\1\0\2\7\2\0\1\7\2\0\2\7\2\0\4\7"+
    "\1\0\14\7\1\0\1\7\1\0\7\7\1\0\101\7\1\0\4\7"+
    "\2\0\10\7\1\0\7\7\1\0\34\7\1\0\4\7\1\0\5\7"+
    "\1\0\1\7\3\0\7\7\1\0\u0154\7\2\0\31\7\1\0\31\7"+
    "\1\0\37\7\1\0\31\7\1\0\37\7\1\0\31\7\1\0\37\7"+
    "\1\0\31\7\1\0\37\7\1\0\31\7\1\0\10\7\2\0\62\6"+
    "\u1600\0\4\7\1\0\33\7\1\0\2\7\1\0\1\7\2\0\1\7"+
    "\1\0\12\7\1\0\4\7\1\0\1\7\1\0\1\7\6\0\1\7"+
    "\4\0\1\7\1\0\1\7\1\0\1\7\1\0\3\7\1\0\2\7"+
    "\1\0\1\7\2\0\1\7\1\0\1\7\1\0\1\7\1\0\1\7"+
    "\1\0\1\7\1\0\2\7\1\0\1\7\2\0\4\7\1\0\7\7"+
    "\1\0\4\7\1\0\4\7\1\0\1\7\1\0\12\7\1\0\21\7"+
    "\5\0\3\7\1\0\5\7\1\0\21\7\u1144\0\ua6d7\7\51\0\u1035\7"+
    "\13\0\336\7\u3fe2\0\u021e\7\uffff\0\uffff\0\uffff\0\uffff\0\uffff\0\uffff\0\uffff\0\uffff\0\uffff\0\uffff\0\uffff\0\uffff\0\uffff\0\uffff\0\u05f0\0";

  /** 
   * Translates characters to character classes
   */
  private static final char [] ZZ_CMAP = zzUnpackCMap(ZZ_CMAP_PACKED);

  /** 
   * Translates DFA states to action switch labels.
   */
  private static final int [] ZZ_ACTION = zzUnpackAction();

  private static final String ZZ_ACTION_PACKED_0 =
    "\1\0\1\1\1\2\1\3\1\4\1\5\1\6\1\1"+
    "\1\6\1\7\2\1\1\10\1\11\1\12\1\13\1\14"+
    "\5\5\1\15\1\16\1\17\1\20\1\21\1\22\4\0"+
    "\1\23\1\0\1\24\1\25\1\26\1\27\1\30\1\31"+
    "\1\32\1\5\1\33\5\5\1\2\1\0\1\34\1\0"+
    "\1\35\1\36\10\5\1\37\4\5\1\40\1\41\1\42"+
    "\1\43";

  private static int [] zzUnpackAction() {
    int [] result = new int[71];
    int offset = 0;
    offset = zzUnpackAction(ZZ_ACTION_PACKED_0, offset, result);
    return result;
  }

  private static int zzUnpackAction(String packed, int offset, int [] result) {
    int i = 0;       /* index in packed string  */
    int j = offset;  /* index in unpacked array */
    int l = packed.length();
    while (i < l) {
      int count = packed.charAt(i++);
      int value = packed.charAt(i++);
      do result[j++] = value; while (--count > 0);
    }
    return j;
  }


  /** 
   * Translates a state to a row index in the transition table
   */
  private static final int [] ZZ_ROWMAP = zzUnpackRowMap();

  private static final String ZZ_ROWMAP_PACKED_0 =
    "\0\0\0\53\0\53\0\126\0\53\0\201\0\254\0\327"+
    "\0\u0102\0\u012d\0\u0158\0\u0183\0\u01ae\0\u01d9\0\u0204\0\53"+
    "\0\u022f\0\u025a\0\u0285\0\u02b0\0\u02db\0\u0306\0\53\0\53"+
    "\0\53\0\53\0\53\0\53\0\u0331\0\u035c\0\u0387\0\u03b2"+
    "\0\u03dd\0\u0408\0\53\0\53\0\53\0\53\0\53\0\53"+
    "\0\53\0\u0433\0\201\0\u045e\0\u0489\0\u04b4\0\u04df\0\u050a"+
    "\0\u0535\0\u0560\0\53\0\u058b\0\u0408\0\201\0\u05b6\0\u05e1"+
    "\0\u060c\0\u0637\0\u0662\0\u068d\0\u06b8\0\u06e3\0\201\0\u070e"+
    "\0\u0739\0\u0764\0\u078f\0\201\0\201\0\201\0\201";

  private static int [] zzUnpackRowMap() {
    int [] result = new int[71];
    int offset = 0;
    offset = zzUnpackRowMap(ZZ_ROWMAP_PACKED_0, offset, result);
    return result;
  }

  private static int zzUnpackRowMap(String packed, int offset, int [] result) {
    int i = 0;  /* index in packed string  */
    int j = offset;  /* index in unpacked array */
    int l = packed.length();
    while (i < l) {
      int high = packed.charAt(i++) << 16;
      result[j++] = high | packed.charAt(i++);
    }
    return j;
  }

  /** 
   * The transition table of the DFA
   */
  private static final int [] ZZ_TRANS = zzUnpackTrans();

  private static final String ZZ_TRANS_PACKED_0 =
    "\1\2\3\3\1\4\1\5\1\2\1\6\2\7\1\6"+
    "\1\10\1\6\1\11\1\12\1\13\1\14\1\15\1\16"+
    "\1\17\1\20\1\21\1\22\2\6\1\23\1\24\3\6"+
    "\1\25\1\6\1\26\3\6\1\27\1\30\1\31\1\32"+
    "\1\33\1\34\60\0\1\35\1\36\53\0\5\6\1\0"+
    "\2\6\10\0\16\6\17\0\2\7\3\0\1\7\35\0"+
    "\14\37\1\40\36\37\10\0\1\41\3\0\1\42\1\41"+
    "\53\0\1\43\53\0\1\44\53\0\1\45\50\0\1\46"+
    "\52\0\1\47\52\0\1\50\57\0\1\51\35\0\5\6"+
    "\1\0\2\6\10\0\1\6\1\52\5\6\1\53\6\6"+
    "\15\0\5\6\1\0\2\6\10\0\1\54\1\6\1\55"+
    "\13\6\15\0\5\6\1\0\2\6\10\0\10\6\1\56"+
    "\5\6\15\0\5\6\1\0\2\6\10\0\11\6\1\57"+
    "\4\6\15\0\5\6\1\0\2\6\10\0\13\6\1\60"+
    "\2\6\7\0\1\35\1\61\1\3\50\35\5\36\1\62"+
    "\45\36\13\0\1\63\47\0\3\64\1\63\1\0\1\64"+
    "\16\0\3\64\24\0\1\41\4\0\1\41\45\0\3\65"+
    "\2\0\1\65\16\0\3\65\22\0\5\6\1\0\2\6"+
    "\10\0\2\6\1\66\13\6\15\0\5\6\1\0\2\6"+
    "\10\0\14\6\1\67\1\6\15\0\5\6\1\0\2\6"+
    "\10\0\4\6\1\70\11\6\15\0\5\6\1\0\2\6"+
    "\10\0\2\6\1\71\13\6\15\0\5\6\1\0\2\6"+
    "\10\0\3\6\1\72\12\6\15\0\5\6\1\0\2\6"+
    "\10\0\1\73\15\6\11\0\1\3\50\0\4\36\1\3"+
    "\1\62\45\36\10\0\3\37\2\0\1\37\16\0\3\37"+
    "\22\0\5\6\1\0\2\6\10\0\10\6\1\74\5\6"+
    "\15\0\5\6\1\0\2\6\10\0\5\6\1\75\10\6"+
    "\15\0\5\6\1\0\2\6\10\0\5\6\1\76\10\6"+
    "\15\0\5\6\1\0\2\6\10\0\10\6\1\77\5\6"+
    "\15\0\5\6\1\0\2\6\10\0\11\6\1\100\4\6"+
    "\15\0\5\6\1\0\2\6\10\0\15\6\1\101\15\0"+
    "\5\6\1\0\2\6\10\0\6\6\1\102\7\6\15\0"+
    "\5\6\1\0\2\6\10\0\4\6\1\103\11\6\15\0"+
    "\5\6\1\0\2\6\10\0\10\6\1\104\5\6\15\0"+
    "\5\6\1\0\2\6\10\0\7\6\1\105\6\6\15\0"+
    "\5\6\1\0\2\6\10\0\2\6\1\106\13\6\15\0"+
    "\5\6\1\0\2\6\10\0\1\6\1\107\14\6\7\0";

  private static int [] zzUnpackTrans() {
    int [] result = new int[1978];
    int offset = 0;
    offset = zzUnpackTrans(ZZ_TRANS_PACKED_0, offset, result);
    return result;
  }

  private static int zzUnpackTrans(String packed, int offset, int [] result) {
    int i = 0;       /* index in packed string  */
    int j = offset;  /* index in unpacked array */
    int l = packed.length();
    while (i < l) {
      int count = packed.charAt(i++);
      int value = packed.charAt(i++);
      value--;
      do result[j++] = value; while (--count > 0);
    }
    return j;
  }


  /* error codes */
  private static final int ZZ_UNKNOWN_ERROR = 0;
  private static final int ZZ_NO_MATCH = 1;
  private static final int ZZ_PUSHBACK_2BIG = 2;

  /* error messages for the codes above */
  private static final String ZZ_ERROR_MSG[] = {
    "Unknown internal scanner error",
    "Error: could not match input",
    "Error: pushback value was too large"
  };

  /**
   * ZZ_ATTRIBUTE[aState] contains the attributes of state <code>aState</code>
   */
  private static final int [] ZZ_ATTRIBUTE = zzUnpackAttribute();

  private static final String ZZ_ATTRIBUTE_PACKED_0 =
    "\1\0\2\11\1\1\1\11\12\1\1\11\6\1\6\11"+
    "\4\0\1\1\1\0\7\11\10\1\1\0\1\11\1\0"+
    "\23\1";

  private static int [] zzUnpackAttribute() {
    int [] result = new int[71];
    int offset = 0;
    offset = zzUnpackAttribute(ZZ_ATTRIBUTE_PACKED_0, offset, result);
    return result;
  }

  private static int zzUnpackAttribute(String packed, int offset, int [] result) {
    int i = 0;       /* index in packed string  */
    int j = offset;  /* index in unpacked array */
    int l = packed.length();
    while (i < l) {
      int count = packed.charAt(i++);
      int value = packed.charAt(i++);
      do result[j++] = value; while (--count > 0);
    }
    return j;
  }

  /** the input device */
  private java.io.Reader zzReader;

  /** the current state of the DFA */
  private int zzState;

  /** the current lexical state */
  private int zzLexicalState = YYINITIAL;

  /** this buffer contains the current text to be matched and is
      the source of the yytext() string */
  private char zzBuffer[] = new char[ZZ_BUFFERSIZE];

  /** the textposition at the last accepting state */
  private int zzMarkedPos;

  /** the current text position in the buffer */
  private int zzCurrentPos;

  /** startRead marks the beginning of the yytext() string in the buffer */
  private int zzStartRead;

  /** endRead marks the last character in the buffer, that has been read
      from input */
  private int zzEndRead;

  /** number of newlines encountered up to the start of the matched text */
  private int yyline;

  /** the number of characters up to the start of the matched text */
  private int yychar;

  /**
   * the number of characters from the last newline up to the start of the 
   * matched text
   */
  private int yycolumn;

  /** 
   * zzAtBOL == true <=> the scanner is currently at the beginning of a line
   */
  private boolean zzAtBOL = true;

  /** zzAtEOF == true <=> the scanner is at the EOF */
  private boolean zzAtEOF;

  /** denotes if the user-EOF-code has already been executed */
  private boolean zzEOFDone;
  
  /** 
   * The number of occupied positions in zzBuffer beyond zzEndRead.
   * When a lead/high surrogate has been read from the input stream
   * into the final zzBuffer position, this will have a value of 1;
   * otherwise, it will have a value of 0.
   */
  private int zzFinalHighSurrogate = 0;

  /* user code: */

    private Symbol symbol(int id)
    {
	return new Symbol(id, yyline, yycolumn);
    }

    private Symbol symbol(int id, Object value)
    {
	return new Symbol(id, yyline, yycolumn, value);
    }



  /**
   * Creates a new scanner
   *
   * @param   in  the java.io.Reader to read input from.
   */
  Lexer(java.io.Reader in) {
    this.zzReader = in;
  }


  /** 
   * Unpacks the compressed character translation table.
   *
   * @param packed   the packed character translation table
   * @return         the unpacked character translation table
   */
  private static char [] zzUnpackCMap(String packed) {
    char [] map = new char[0x110000];
    int i = 0;  /* index in packed string  */
    int j = 0;  /* index in unpacked array */
    while (i < 2358) {
      int  count = packed.charAt(i++);
      char value = packed.charAt(i++);
      do map[j++] = value; while (--count > 0);
    }
    return map;
  }


  /**
   * Refills the input buffer.
   *
   * @return      <code>false</code>, iff there was new input.
   * 
   * @exception   java.io.IOException  if any I/O-Error occurs
   */
  private boolean zzRefill() throws java.io.IOException {

    /* first: make room (if you can) */
    if (zzStartRead > 0) {
      zzEndRead += zzFinalHighSurrogate;
      zzFinalHighSurrogate = 0;
      System.arraycopy(zzBuffer, zzStartRead,
                       zzBuffer, 0,
                       zzEndRead-zzStartRead);

      /* translate stored positions */
      zzEndRead-= zzStartRead;
      zzCurrentPos-= zzStartRead;
      zzMarkedPos-= zzStartRead;
      zzStartRead = 0;
    }

    /* is the buffer big enough? */
    if (zzCurrentPos >= zzBuffer.length - zzFinalHighSurrogate) {
      /* if not: blow it up */
      char newBuffer[] = new char[zzBuffer.length*2];
      System.arraycopy(zzBuffer, 0, newBuffer, 0, zzBuffer.length);
      zzBuffer = newBuffer;
      zzEndRead += zzFinalHighSurrogate;
      zzFinalHighSurrogate = 0;
    }

    /* fill the buffer with new input */
    int requested = zzBuffer.length - zzEndRead;
    int numRead = zzReader.read(zzBuffer, zzEndRead, requested);

    /* not supposed to occur according to specification of java.io.Reader */
    if (numRead == 0) {
      throw new java.io.IOException("Reader returned 0 characters. See JFlex examples for workaround.");
    }
    if (numRead > 0) {
      zzEndRead += numRead;
      /* If numRead == requested, we might have requested to few chars to
         encode a full Unicode character. We assume that a Reader would
         otherwise never return half characters. */
      if (numRead == requested) {
        if (Character.isHighSurrogate(zzBuffer[zzEndRead - 1])) {
          --zzEndRead;
          zzFinalHighSurrogate = 1;
        }
      }
      /* potentially more input available */
      return false;
    }

    /* numRead < 0 ==> end of stream */
    return true;
  }

    
  /**
   * Closes the input stream.
   */
  public final void yyclose() throws java.io.IOException {
    zzAtEOF = true;            /* indicate end of file */
    zzEndRead = zzStartRead;  /* invalidate buffer    */

    if (zzReader != null)
      zzReader.close();
  }


  /**
   * Resets the scanner to read from a new input stream.
   * Does not close the old reader.
   *
   * All internal variables are reset, the old input stream 
   * <b>cannot</b> be reused (internal buffer is discarded and lost).
   * Lexical state is set to <tt>ZZ_INITIAL</tt>.
   *
   * Internal scan buffer is resized down to its initial length, if it has grown.
   *
   * @param reader   the new input stream 
   */
  public final void yyreset(java.io.Reader reader) {
    zzReader = reader;
    zzAtBOL  = true;
    zzAtEOF  = false;
    zzEOFDone = false;
    zzEndRead = zzStartRead = 0;
    zzCurrentPos = zzMarkedPos = 0;
    zzFinalHighSurrogate = 0;
    yyline = yychar = yycolumn = 0;
    zzLexicalState = YYINITIAL;
    if (zzBuffer.length > ZZ_BUFFERSIZE)
      zzBuffer = new char[ZZ_BUFFERSIZE];
  }


  /**
   * Returns the current lexical state.
   */
  public final int yystate() {
    return zzLexicalState;
  }


  /**
   * Enters a new lexical state
   *
   * @param newState the new lexical state
   */
  public final void yybegin(int newState) {
    zzLexicalState = newState;
  }


  /**
   * Returns the text matched by the current regular expression.
   */
  public final String yytext() {
    return new String( zzBuffer, zzStartRead, zzMarkedPos-zzStartRead );
  }


  /**
   * Returns the character at position <tt>pos</tt> from the 
   * matched text. 
   * 
   * It is equivalent to yytext().charAt(pos), but faster
   *
   * @param pos the position of the character to fetch. 
   *            A value from 0 to yylength()-1.
   *
   * @return the character at position pos
   */
  public final char yycharat(int pos) {
    return zzBuffer[zzStartRead+pos];
  }


  /**
   * Returns the length of the matched text region.
   */
  public final int yylength() {
    return zzMarkedPos-zzStartRead;
  }


  /**
   * Reports an error that occured while scanning.
   *
   * In a wellformed scanner (no or only correct usage of 
   * yypushback(int) and a match-all fallback rule) this method 
   * will only be called with things that "Can't Possibly Happen".
   * If this method is called, something is seriously wrong
   * (e.g. a JFlex bug producing a faulty scanner etc.).
   *
   * Usual syntax/scanner level error handling should be done
   * in error fallback rules.
   *
   * @param   errorCode  the code of the errormessage to display
   */
  private void zzScanError(int errorCode) {
    String message;
    try {
      message = ZZ_ERROR_MSG[errorCode];
    }
    catch (ArrayIndexOutOfBoundsException e) {
      message = ZZ_ERROR_MSG[ZZ_UNKNOWN_ERROR];
    }

    throw new Error(message);
  } 


  /**
   * Pushes the specified amount of characters back into the input stream.
   *
   * They will be read again by then next call of the scanning method
   *
   * @param number  the number of characters to be read again.
   *                This number must not be greater than yylength()!
   */
  public void yypushback(int number)  {
    if ( number > yylength() )
      zzScanError(ZZ_PUSHBACK_2BIG);

    zzMarkedPos -= number;
  }


  /**
   * Contains user EOF-code, which will be executed exactly once,
   * when the end of file is reached
   */
  private void zzDoEOF() throws java.io.IOException {
    if (!zzEOFDone) {
      zzEOFDone = true;
      yyclose();
    }
  }


  /**
   * Resumes scanning until the next regular expression is matched,
   * the end of input is encountered or an I/O-Error occurs.
   *
   * @return      the next token
   * @exception   java.io.IOException  if any I/O-Error occurs
   */
  public java_cup.runtime.Symbol next_token() throws java.io.IOException, Exception {
    int zzInput;
    int zzAction;

    // cached fields:
    int zzCurrentPosL;
    int zzMarkedPosL;
    int zzEndReadL = zzEndRead;
    char [] zzBufferL = zzBuffer;
    char [] zzCMapL = ZZ_CMAP;

    int [] zzTransL = ZZ_TRANS;
    int [] zzRowMapL = ZZ_ROWMAP;
    int [] zzAttrL = ZZ_ATTRIBUTE;

    while (true) {
      zzMarkedPosL = zzMarkedPos;

      boolean zzR = false;
      int zzCh;
      int zzCharCount;
      for (zzCurrentPosL = zzStartRead  ;
           zzCurrentPosL < zzMarkedPosL ;
           zzCurrentPosL += zzCharCount ) {
        zzCh = Character.codePointAt(zzBufferL, zzCurrentPosL, zzMarkedPosL);
        zzCharCount = Character.charCount(zzCh);
        switch (zzCh) {
        case '\u000B':
        case '\u000C':
        case '\u0085':
        case '\u2028':
        case '\u2029':
          yyline++;
          yycolumn = 0;
          zzR = false;
          break;
        case '\r':
          yyline++;
          yycolumn = 0;
          zzR = true;
          break;
        case '\n':
          if (zzR)
            zzR = false;
          else {
            yyline++;
            yycolumn = 0;
          }
          break;
        default:
          zzR = false;
          yycolumn += zzCharCount;
        }
      }

      if (zzR) {
        // peek one character ahead if it is \n (if we have counted one line too much)
        boolean zzPeek;
        if (zzMarkedPosL < zzEndReadL)
          zzPeek = zzBufferL[zzMarkedPosL] == '\n';
        else if (zzAtEOF)
          zzPeek = false;
        else {
          boolean eof = zzRefill();
          zzEndReadL = zzEndRead;
          zzMarkedPosL = zzMarkedPos;
          zzBufferL = zzBuffer;
          if (eof) 
            zzPeek = false;
          else 
            zzPeek = zzBufferL[zzMarkedPosL] == '\n';
        }
        if (zzPeek) yyline--;
      }
      zzAction = -1;

      zzCurrentPosL = zzCurrentPos = zzStartRead = zzMarkedPosL;
  
      zzState = ZZ_LEXSTATE[zzLexicalState];

      // set up zzAction for empty match case:
      int zzAttributes = zzAttrL[zzState];
      if ( (zzAttributes & 1) == 1 ) {
        zzAction = zzState;
      }


      zzForAction: {
        while (true) {
    
          if (zzCurrentPosL < zzEndReadL) {
            zzInput = Character.codePointAt(zzBufferL, zzCurrentPosL, zzEndReadL);
            zzCurrentPosL += Character.charCount(zzInput);
          }
          else if (zzAtEOF) {
            zzInput = YYEOF;
            break zzForAction;
          }
          else {
            // store back cached positions
            zzCurrentPos  = zzCurrentPosL;
            zzMarkedPos   = zzMarkedPosL;
            boolean eof = zzRefill();
            // get translated positions and possibly new buffer
            zzCurrentPosL  = zzCurrentPos;
            zzMarkedPosL   = zzMarkedPos;
            zzBufferL      = zzBuffer;
            zzEndReadL     = zzEndRead;
            if (eof) {
              zzInput = YYEOF;
              break zzForAction;
            }
            else {
              zzInput = Character.codePointAt(zzBufferL, zzCurrentPosL, zzEndReadL);
              zzCurrentPosL += Character.charCount(zzInput);
            }
          }
          int zzNext = zzTransL[ zzRowMapL[zzState] + zzCMapL[zzInput] ];
          if (zzNext == -1) break zzForAction;
          zzState = zzNext;

          zzAttributes = zzAttrL[zzState];
          if ( (zzAttributes & 1) == 1 ) {
            zzAction = zzState;
            zzMarkedPosL = zzCurrentPosL;
            if ( (zzAttributes & 8) == 8 ) break zzForAction;
          }

        }
      }

      // store back cached position
      zzMarkedPos = zzMarkedPosL;

      if (zzInput == YYEOF && zzStartRead == zzCurrentPos) {
        zzAtEOF = true;
            zzDoEOF();
          { return new java_cup.runtime.Symbol(sym.EOF); }
      }
      else {
        switch (zzAction < 0 ? zzAction : ZZ_ACTION[zzAction]) {
          case 1: 
            { throw new Exception (String.format (
        "Line %d, column %d: illegal character: '%s'\n", yyline + 1, yycolumn + 1, yytext()
      ));
            }
          case 36: break;
          case 2: 
            { /* ignore */
            }
          case 37: break;
          case 3: 
            { return symbol(DIV);
            }
          case 38: break;
          case 4: 
            { return symbol(TIMES);
            }
          case 39: break;
          case 5: 
            { return symbol(IDENT, yytext().intern());
            }
          case 40: break;
          case 6: 
            { return symbol(CST, Integer.parseInt(yytext()));
            }
          case 41: break;
          case 7: 
            { return symbol(EQUAL);
            }
          case 42: break;
          case 8: 
            { return symbol(NOT);
            }
          case 43: break;
          case 9: 
            { return symbol(CMP, Binop.Blt);
            }
          case 44: break;
          case 10: 
            { return symbol(CMP, Binop.Bgt);
            }
          case 45: break;
          case 11: 
            { return symbol(PLUS);
            }
          case 46: break;
          case 12: 
            { return symbol(MINUS);
            }
          case 47: break;
          case 13: 
            { return symbol(COMMA);
            }
          case 48: break;
          case 14: 
            { return symbol(LP);
            }
          case 49: break;
          case 15: 
            { return symbol(RP);
            }
          case 50: break;
          case 16: 
            { return symbol(LA);
            }
          case 51: break;
          case 17: 
            { return symbol(RA);
            }
          case 52: break;
          case 18: 
            { return symbol(SEMICOLUMN);
            }
          case 53: break;
          case 19: 
            { String s = yytext(); return symbol(CST, Integer.parseInt(s.substring(1, s.length()), 8));
            }
          case 54: break;
          case 20: 
            { return symbol(CMP, Binop.Beq);
            }
          case 55: break;
          case 21: 
            { return symbol(OR);
            }
          case 56: break;
          case 22: 
            { return symbol(AND);
            }
          case 57: break;
          case 23: 
            { return symbol(CMP, Binop.Bneq);
            }
          case 58: break;
          case 24: 
            { return symbol(CMP, Binop.Ble);
            }
          case 59: break;
          case 25: 
            { return symbol(CMP, Binop.Bge);
            }
          case 60: break;
          case 26: 
            { return symbol(FLECHE);
            }
          case 61: break;
          case 27: 
            { return symbol(IF);
            }
          case 62: break;
          case 28: 
            { String s = yytext(); return symbol(CST, ((int) s.charAt(1)));
            }
          case 63: break;
          case 29: 
            { String s = yytext(); return symbol(CST, Integer.parseInt(s.substring(2, s.length()), 16));
            }
          case 64: break;
          case 30: 
            { return symbol(INT);
            }
          case 65: break;
          case 31: 
            { return symbol(ELSE);
            }
          case 66: break;
          case 32: 
            { return symbol(WHILE);
            }
          case 67: break;
          case 33: 
            { return symbol(SIZEOF);
            }
          case 68: break;
          case 34: 
            { return symbol(STRUCT);
            }
          case 69: break;
          case 35: 
            { return symbol(RETURN);
            }
          case 70: break;
          default:
            zzScanError(ZZ_NO_MATCH);
        }
      }
    }
  }

  /**
   * Converts an int token code into the name of the
   * token by reflection on the cup symbol class/interface sym
   *
   * This code was contributed by Karl Meissner <meissnersd@yahoo.com>
   */
  private String getTokenName(int token) {
    try {
      java.lang.reflect.Field [] classFields = sym.class.getFields();
      for (int i = 0; i < classFields.length; i++) {
        if (classFields[i].getInt(null) == token) {
          return classFields[i].getName();
        }
      }
    } catch (Exception e) {
      e.printStackTrace(System.err);
    }

    return "UNKNOWN TOKEN";
  }

  /**
   * Same as next_token but also prints the token to standard out
   * for debugging.
   *
   * This code was contributed by Karl Meissner <meissnersd@yahoo.com>
   */
  public java_cup.runtime.Symbol debug_next_token() throws java.io.IOException, Exception {
    java_cup.runtime.Symbol s = next_token();
    System.out.println( "line:" + (yyline+1) + " col:" + (yycolumn+1) + " --"+ yytext() + "--" + getTokenName(s.sym) + "--");
    return s;
  }

  /**
   * Runs the scanner on input files.
   *
   * This main method is the debugging routine for the scanner.
   * It prints debugging information about each returned token to
   * System.out until the end of file is reached, or an error occured.
   *
   * @param argv   the command line, contains the filenames to run
   *               the scanner on.
   */
  public static void main(String argv[]) {
    if (argv.length == 0) {
      System.out.println("Usage : java Lexer [ --encoding <name> ] <inputfile(s)>");
    }
    else {
      int firstFilePos = 0;
      String encodingName = "UTF-8";
      if (argv[0].equals("--encoding")) {
        firstFilePos = 2;
        encodingName = argv[1];
        try {
          java.nio.charset.Charset.forName(encodingName); // Side-effect: is encodingName valid? 
        } catch (Exception e) {
          System.out.println("Invalid encoding '" + encodingName + "'");
          return;
        }
      }
      for (int i = firstFilePos; i < argv.length; i++) {
        Lexer scanner = null;
        try {
          java.io.FileInputStream stream = new java.io.FileInputStream(argv[i]);
          java.io.Reader reader = new java.io.InputStreamReader(stream, encodingName);
          scanner = new Lexer(reader);
          while ( !scanner.zzAtEOF ) scanner.debug_next_token();
        }
        catch (java.io.FileNotFoundException e) {
          System.out.println("File not found : \""+argv[i]+"\"");
        }
        catch (java.io.IOException e) {
          System.out.println("IO error scanning file \""+argv[i]+"\"");
          System.out.println(e);
        }
        catch (Exception e) {
          System.out.println("Unexpected exception:");
          e.printStackTrace();
        }
      }
    }
  }


}