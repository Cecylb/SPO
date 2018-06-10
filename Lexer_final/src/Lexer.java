import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*********************************************************************************************************************/
/************************************ЛЕКСЕР***************************************************************************/
/*********************************************************************************************************************/

public class Lexer {

    static private StringBuffer cur = new StringBuffer();                      //строка для текущего токена (для матчера)
    static private StringBuffer buf = new StringBuffer();                      //строка-буфер (для матчера)

/******************************************************************************/
/**********************************ТИП*ЛЕКСЕМ**********************************/
    /******************************************************************************/

    public enum LexemType {

        /*****************************Перечисление*вариантов***************************/

        NUMBER("NUMBER", Pattern.compile("^(0|[1-9][0-9]*)")),                         //цифры 0-9
        FOR_KW("FOR_KW", Pattern.compile("^for")),
        LIST_KW("LIST_KW", Pattern.compile("^List")),
        LIST_NEW("LIST_NEW", Pattern.compile("^new")),
        LIST_ADD("LIST_ADD", Pattern.compile("^\\.add")),
        LIST_REMOVE("LIST_REMOVE", Pattern.compile("^\\.remove")),
        LIST_GET("LIST_GET", Pattern.compile("^\\.get")),
        LIST_SET("LIST_SET", Pattern.compile("^\\.set")),
        LIST_CONTAINS("LIST_CONTAINS", Pattern.compile("^\\.contains")),
        VAR_TYPE("VAR_TYPE", Pattern.compile("^Integer|^String|^Float")),
        VAR("VAR", Pattern.compile("^([a-z]+[0-1]*)")),                                //буквы a-z
        OPERATION("OPERATION", Pattern.compile("^\\+|-|\\*|/")),                       //операции
        COMPARISON("COMPARISON",Pattern.compile("^(==|<|>)")),                         //сравнения
        EQUATING("EQUATING", Pattern.compile("^(=)")),                                 //равно
        BR_LR("BR_LR", Pattern.compile("^\\(")),
        BR_RR("BR_RR", Pattern.compile("^\\)")),
        BR_LF("BR_LF", Pattern.compile("^\\{")),
        BR_RF("BR_RF", Pattern.compile("^\\}")),
        BR_TL("BR_LT", Pattern.compile("^\\[")),
        BR_TR("BR_RT", Pattern.compile("^\\]")),
        GR_SYNT("GR_SYNT", Pattern.compile("^\\;")),
        EMPTY("EMPTY", Pattern.compile("^\\s"));                                        //пробел

        /*******************************Присвоение*типа********************************/

        public String lextype;                                                         //объявляем переменную-тип лексемы
        public Pattern pattern;                                                        //объявляем переменную-pattern
        LexemType(String lextype, Pattern pattern){                                    //функция LexemType (для списка токенов и матчера)
            this.lextype = lextype;
            this.pattern = pattern;
        }
    }


/******************************************************************************/
/**********************************СПИСОК*ТОКЕНОВ******************************/
    /******************************************************************************/

    private ArrayList<Token> lex(String string) {
        ArrayList<Token> tokens = new ArrayList<>();                           //создаём массив токенов
        while(string.length()!=0) {                                            //цикл с условием конца строки
            for(LexemType lexType: LexemType.values()) {
                Pattern pattern = lexType.pattern;                             //заполняем массив токенов


/******************************************************************************/
/*************************************МАТЧЕР***********************************/
/******************************************************************************/

                Matcher matcher = pattern.matcher(string);                     //объявляем matcher
                if(matcher.lookingAt()){                                       //подбираем подходящий тип лексемы
                    cur.append(matcher.group());                               //обновляем значение cur
                    tokens.add(new Token(lexType.lextype,cur.toString()));     //создаём токен на основе прочитанных данных
                    buf.append(string);                                        //обновляем значение buf
                    buf.delete(0,(cur.length()));                              //очищаем строку buf от нуля до конца
                    string = buf.toString();                                   //сохраняем строку с учётом пройденной лексемы
                    cur.setLength(0);                                          //задаём строке cur значение 0
                    buf.setLength(0);                                          //задаём строке buf значение 0
                }
            }
        }
        return tokens;                                                         //передаём значение списку токенов
    }

/******************************************************************************/
/***************************************МЕЙН***********************************/
    /******************************************************************************/

    public static void main(String[] args) {
        Lexer lexer = new Lexer();
        String input =
                "List array[Float] = new List[](); " +
                "List.add(112); " +
                "i=1+1;" +
                "i=i+1;" +
                "for(i=0; i<45; i+1){ " +
                "i = i*5; " +
                "List.add(i); " +
                "i=i+10;" +
                "}" +
                "List.set(2 40);" +
                "List.contains(40);" +
                "List.get(2);";                            //ввод выражения
        List<Token> tokens = lexer.lex(input);
        for(Token token: tokens){                                    //цикл
            if(!token.getType().equals("EMPTY"))                               //проверка на пробел - их не выводим
                System.out.println(token.getType() + "  " + token.getValue());     //вывод результата работы лексера
        }

        Parser parser = new Parser();
        parser.setTokens(tokens);
        parser.parsestart();
    }
}