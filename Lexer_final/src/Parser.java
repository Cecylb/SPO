import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
/*********************************************************************************************************************/
/************************************ПАРСЕР***************************************************************************/
/*********************************************************************************************************************/
public class Parser {
    Token token;
    private List<Token> tokens;
    private List<Token> tokensNPN = new ArrayList<>();
    static int num = 0;
    static int col = 0;
    static int list = 0;
    static int loopn = 0;
    static int loopv = 0;
    static int loopg = 0;
    static int ifexp = 0;
    static int listcom = 0;
    static int iflist = 0;

    public void match() {
        if (num < tokens.size()) {
            num++;
        } else {
            System.out.println("Строка проанализирована");
            StackNpn stack = new StackNpn();
            stack.setTokens(tokensNPN);
            stack.StackFiller();
        }
    }
/******************************************************************************/
/**********************************FOR*****************************************/
    /******************************************************************************/
    public void parsestart() {
        if (num >= tokens.size()) {
            return;
        }
        token = tokens.get(num);
        while (token.getType().equals("EMPTY")) {
            match();
            token = tokens.get(num);
        }
        if (token.getType().equals("VAR") || token.getType().equals("BR_LR")) {
            System.out.println("Возможно выражение");
            ifExpersson();
        }
        if (token.getType().equals("LIST_KW")) {
            System.out.println("Возможен список");
            ifList();
        }
        if (token.getType().equals("FOR_KW")) {
            System.out.println("Возможен цикл");
            match();
            tokensNPN.add(token);
            brLRexpect();
        } else {
            match();
            parsestart();      //**********************************************************************************************************************************************
        }
    }

/******************************************************************************/
/**********************************(*******************************************/
    /******************************************************************************/
    private void brLRexpect() {
        token = tokens.get(num);
        while (token.getType().equals("EMPTY")) {
            match();
            token = tokens.get(num);
        }
        if (token.getType().equals("BR_LR")) {
            match();
            tokensNPN.add(token);
            loopv++;
            varexpect();
        } else {
            match();
            System.out.println("Нет левой скобки. Цикл не создан");
            String size = Integer.toString(tokensNPN.size());
            Token varerr = new Token("ERROR", size);
            tokensNPN.add(varerr);
            parsestart();
        }
    }

/******************************************************************************/
/**********************************)*******************************************/
    /******************************************************************************/
    private void brRRexpect() {
        token = tokens.get(num);
        while (token.getType().equals("EMPTY")) {
            match();
            token = tokens.get(num);
        }
        if (token.getType().equals("BR_RR")) {
            match();
            tokensNPN.add(token);
            brLFexpect();
        } else {
            match();
            System.out.println("Нет правой скобки. Цикл не создан");
            String size = Integer.toString(tokensNPN.size());
            Token varerr = new Token("ERROR", size);
            tokensNPN.add(varerr);
            parsestart();
        }
    }


/******************************************************************************/
/**********************************{*******************************************/
    /******************************************************************************/
    private void brLFexpect() {
        token = tokens.get(num);
        while (token.getType().equals("EMPTY")) {
            match();
            token = tokens.get(num);
        }
        if (token.getType().equals("BR_LF")) {
            match();
            tokensNPN.add(token);
            brRFexpect();
        } else {
            System.out.println("Цикл не имеет основной части");
            String size = Integer.toString(tokensNPN.size());
            Token varerr = new Token("ERROR", size);
            tokensNPN.add(varerr);
            match();
            parsestart();
        }
    }

/******************************************************************************/
/**********************************}*******************************************/
    /******************************************************************************/
    private void brRFexpect() {

        int howmanyloops = 0;
        token = tokens.get(num);
        while (token.getType().equals("EMPTY")) {
            match();
            token = tokens.get(num);
        }
        if (col > 0) {
            while (howmanyloops < col) {
                token = tokens.get(num);
                if (token.getType().equals("BR_RF")) {
                    tokensNPN.add(token);
                    System.out.println("Цикл for был успешно записан");
                    Token correct = new Token("CORRECT", "FOR+");
                    tokensNPN.add(correct);
                    match();
                }
                howmanyloops++;
            }
        }
        if (token.getType().equals("BR_RF")) {
            tokensNPN.add(token);
            System.out.println("Цикл for был успешно записан");
            Token correct = new Token("CORRECT", "FOR+");
            tokensNPN.add(correct);
            match();
            parsestart();
        }
        if (token.getType().equals("FOR_KW")) {
            match();
            tokensNPN.add(token);
            col++;
            System.out.println("В цикле возможно находится ещё один цикл");
            brLRexpect();
        }
        if (token.getType().equals("LIST_KW")) {
            System.out.println("В цикле присутствует команда списка");
            iflist = 1;
            ifList();
        }
        if (token.getType().equals("VAR")) {
            System.out.println("В цикле присутствует выражение");
            ifexp = 1;
            ifExpersson();
        } else {
            if (num != tokens.size()) {
                System.out.println("Цикл не закрывается");
                String size = Integer.toString(tokensNPN.size());
                Token varerr = new Token("ERROR", size);
                tokensNPN.add(varerr);
                match();
                parsestart();
            } else {
                return;
            }
        }
    }

/******************************************************************************/
/**********************************VAR*****************************************/
    /******************************************************************************/
    private void varexpect() {
        token = tokens.get(num);
        while (token.getType().equals("EMPTY")) {
            match();
            token = tokens.get(num);
        }
        if (token.getType().equals("VAR")) {
            if (loopv == 1) {
                match();
                tokensNPN.add(token);
                equatingexpect();
            }
            if (loopv == 2) {
                match();
                tokensNPN.add(token);
                comparisonexpect();
            }
            if (loopv == 3) {
                match();
                tokensNPN.add(token);
                loopv = 0;
                operationexpect();
            }
        } else {
            System.out.println("Отсутствует одна из переменных");
            String size = Integer.toString(tokensNPN.size());
            Token varerr = new Token("ERROR", size);
            tokensNPN.add(varerr);
            match();
            parsestart();
        }
    }

/******************************************************************************/
/********************************EQUATING**************************************/
    /******************************************************************************/
    private void equatingexpect() {
        token = tokens.get(num);
        while (token.getType().equals("EMPTY")) {
            match();
            token = tokens.get(num);
        }
        if (token.getType().equals("EQUATING")) {
            match();
            tokensNPN.add(token);
            loopn++;
            numberexpect();
        } else {
            match();
            System.out.println("Отсутствует знак равенства в первом условии");
            String size = Integer.toString(tokensNPN.size());
            Token varerr = new Token("ERROR", size);
            tokensNPN.add(varerr);
            parsestart();
        }
    }

/******************************************************************************/
/*********************************NUMBER***************************************/
    /******************************************************************************/
    private void numberexpect() {
        token = tokens.get(num);
        while (token.getType().equals("EMPTY")) {
            match();
            token = tokens.get(num);
        }
        if (token.getType().equals("NUMBER")) {
            if (loopn == 1) {
                match();
                tokensNPN.add(token);
                loopg++;
                grammarexpect();
            }
            if (loopn == 2) {
                match();
                tokensNPN.add(token);
                loopg++;
                grammarexpect();
            }
            if (loopn == 3) {
                match();
                tokensNPN.add(token);
                loopn = 0;
                brRRexpect();
            }
        } else {
            System.out.println("Отсутствует одно из чисел");
            String size = Integer.toString(tokensNPN.size());
            Token varerr = new Token("ERROR", size);
            tokensNPN.add(varerr);
            match();
            parsestart();
        }
    }

/******************************************************************************/
/************************************;*****************************************/
    /******************************************************************************/
    private void grammarexpect() {
        token = tokens.get(num);
        while (token.getType().equals("EMPTY")) {
            match();
            token = tokens.get(num);
        }
        if (token.getType().equals("GR_SYNT")) {
            if (loopg == 1) {
                match();
                tokensNPN.add(token);
                loopv++;
                varexpect();
            }
            if (loopg == 2) {
                match();
                tokensNPN.add(token);
                loopv++;
                loopg = 0;
                varexpect();
            }
        } else {
            System.out.println("Плохая грамматика!");
            String size = Integer.toString(tokensNPN.size());
            Token varerr = new Token("ERROR", size);
            tokensNPN.add(varerr);
            match();
            parsestart();
        }
    }

/******************************************************************************/
/*******************************COMPARISON*************************************/
    /******************************************************************************/
    private void comparisonexpect() {
        token = tokens.get(num);
        while (token.getType().equals("EMPTY")) {
            match();
            token = tokens.get(num);
        }
        if (token.getType().equals("COMPARISON")) {
            match();
            tokensNPN.add(token);
            loopn++;
            numberexpect();
        } else {
            System.out.println("Нет знака во втором условии");
            String size = Integer.toString(tokensNPN.size());
            Token varerr = new Token("ERROR", size);
            tokensNPN.add(varerr);
            match();
            parsestart();
        }
    }

/******************************************************************************/
/*******************************OPERATION**************************************/
    /******************************************************************************/
    private void operationexpect() {
        token = tokens.get(num);
        while (token.getType().equals("EMPTY")) {
            match();
            token = tokens.get(num);
        }
        if (token.getType().equals("OPERATION")) {
            match();
            tokensNPN.add(token);
            loopn++;
            numberexpect();
        } else {
            System.out.println("Нет знака в третьем условии");
            String size = Integer.toString(tokensNPN.size());
            Token varerr = new Token("ERROR", size);
            tokensNPN.add(varerr);
            match();
            parsestart();
        }
    }

    public void ifExpersson() {
        int br = 0;
        token = tokens.get(num);
        if (token.getType().equals("VAR")) {
            match();
            tokensNPN.add(token);
            token = tokens.get(num);
            while (token.getType().equals("EMPTY")) {
                match();
                token = tokens.get(num);
            }
            if (token.getType().equals("EQUATING")) {
                match();
                tokensNPN.add(token);
                token = tokens.get(num);
                while (token.getType().equals("EMPTY")) {
                    match();
                    token = tokens.get(num);
                }
                while (!token.getType().equals("GR_SYNT")) {
                    if (token.getType().equals("BR_RR")) {
                        match();
                        tokensNPN.add(token);
                        token = tokens.get(num);
                        br--;
                    }
                    if (token.getType().equals("BR_LR")) {
                        match();
                        tokensNPN.add(token);
                        token = tokens.get(num);
                        br++;
                    }
                    if (token.getType().equals("VAR") || token.getType().equals("NUMBER")) {
                        match();
                        tokensNPN.add(token);
                        token = tokens.get(num);
                        while (token.getType().equals("EMPTY")) {
                            match();
                            token = tokens.get(num);
                        }
                        if (token.getType().equals("OPERATION")) {
                            match();
                            tokensNPN.add(token);
                            token = tokens.get(num);
                            while (token.getType().equals("EMPTY")) {
                                match();
                                token = tokens.get(num);
                            }
                            if (token.getType().equals("VAR") || token.getType().equals("NUMBER")) {
                                match();
                                tokensNPN.add(token);
                                token = tokens.get(num);
                                while (token.getType().equals("EMPTY")) {
                                    match();
                                    token = tokens.get(num);
                                }
                                if (token.getType().equals("BR_RR")) {
                                    match();
                                    tokensNPN.add(token);
                                    token = tokens.get(num);
                                    br--;
                                }
                                if (token.getType().equals("BR_LR")) {
                                    match();
                                    tokensNPN.add(token);
                                    token = tokens.get(num);
                                    br++;
                                }
                                if (!token.getType().equals("GR_SYNT")) {
                                    match();
                                    tokensNPN.add(token);
                                    token = tokens.get(num);
                                    if (token.getType().equals("OPERATION")) {
                                        match();
                                        tokensNPN.add(token);
                                        token = tokens.get(num);
                                    }
                                }
                            }
                        }
                    }
                }
                while (token.getType().equals("EMPTY")) {
                    match();
                    token = tokens.get(num);
                }
                if (br != 0) {
                    System.out.println("Ошибка в скобках! Выражение не записано!");
                    String size = Integer.toString(tokensNPN.size());
                    Token varerr = new Token("ERROR", size);
                    tokensNPN.add(varerr);
                    match();
                    if (ifexp == 0) {
                        parsestart();
                    }
                    if (ifexp == 1) {
                        ifexp = 0;
                        brRFexpect();
                    }
                }
                tokensNPN.add(token);
                System.out.println("Выражение записано");
                Token correct = new Token("CORRECT", "EXP+");
                tokensNPN.add(correct);
                match();
                if (ifexp == 0) {
                    parsestart();
                }
                if (ifexp == 1) {
                    ifexp = 0;
                    brRFexpect();
                }
            }
        }
    }

    public void ifList() {
        tokensNPN.add(token);
        match();
        token = tokens.get(num);
        while (token.getType().equals("EMPTY")) {
            match();
            token = tokens.get(num);
        }
        if (token.getType().equals("VAR")) {
            tokensNPN.add(token);
            match();
            token = tokens.get(num);
            if (token.getType().equals("BR_LT")) {
                tokensNPN.add(token);
                match();
                token = tokens.get(num);
                if (token.getType().equals("VAR_TYPE")) {
                    tokensNPN.add(token);
                    match();
                    token = tokens.get(num);
                    if (token.getType().equals("BR_RT")) {
                        tokensNPN.add(token);
                        match();
                        token = tokens.get(num);
                        while (token.getType().equals("EMPTY")) {
                            match();
                            token = tokens.get(num);
                        }
                        if (token.getType().equals("EQUATING")) {
                            tokensNPN.add(token);
                            match();
                            token = tokens.get(num);
                            while (token.getType().equals("EMPTY")) {
                                match();
                                token = tokens.get(num);
                            }
                            if (token.getType().equals("LIST_NEW")) {
                                tokensNPN.add(token);
                                match();
                                token = tokens.get(num);
                                while (token.getType().equals("EMPTY")) {
                                    match();
                                    token = tokens.get(num);
                                    if (token.getType().equals("LIST_KW")) {
                                        tokensNPN.add(token);
                                        match();
                                        token = tokens.get(num);
                                        if (token.getType().equals("BR_LT")) {
                                            tokensNPN.add(token);
                                            match();
                                            token = tokens.get(num);
                                            if (token.getType().equals("BR_RT")) {
                                                tokensNPN.add(token);
                                                match();
                                                token = tokens.get(num);
                                                if (token.getType().equals("BR_LR")) {
                                                    tokensNPN.add(token);
                                                    match();
                                                    token = tokens.get(num);
                                                    if (token.getType().equals("BR_RR")) {
                                                        tokensNPN.add(token);
                                                        match();
                                                        token = tokens.get(num);
                                                        if (token.getType().equals("GR_SYNT")) {
                                                            tokensNPN.add(token);
                                                            System.out.println("Запрос на создание списка записан");
                                                            Token correct = new Token("CORRECT", "LIST+");
                                                            tokensNPN.add(correct);
                                                            match();
                                                            token = tokens.get(num);
                                                            list++;
                                                            if (iflist == 0) {
                                                                parsestart();
                                                            }
                                                            if (iflist == 1) {
                                                                iflist = 0;
                                                                brRFexpect();
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        if (token.getType().equals("LIST_ADD")) {
            listcom = 1;
        }
        if (token.getType().equals("LIST_REMOVE")) {
            listcom = 2;
        }
        if (token.getType().equals("LIST_GET")) {
            listcom = 3;
        }
        if (token.getType().equals("LIST_CONTAINS")) {
            listcom = 4;
        }
        if (token.getType().equals("LIST_SET")) {
            listcom = 5;
        }
        if (listcom > 0) {
            match();
            tokensNPN.add(token);
            token = tokens.get(num);
            if (token.getType().equals("BR_LR")) {
                match();
                tokensNPN.add(token);
                token = tokens.get(num);
                if (token.getType().equals("VAR") || token.getType().equals("NUMBER")) {
                    match();
                    tokensNPN.add(token);
                    token = tokens.get(num);
                    if(listcom == 5) {
                        while (token.getType().equals("EMPTY")) {
                            match();
                            token = tokens.get(num);
                        }
                        if (token.getType().equals("VAR") || token.getType().equals("NUMBER")) {
                            match();
                            tokensNPN.add(token);
                            token = tokens.get(num);
                        }
                    }
                    if (token.getType().equals("BR_RR")) {
                        match();
                        tokensNPN.add(token);
                        token = tokens.get(num);
                        if (token.getType().equals("GR_SYNT")) {
                            match();
                            tokensNPN.add(token);
                            if (listcom == 1) {
                                System.out.println("Запрос на добавление элемента в список записан");
                                Token correct = new Token("CORRECT", "ADD+");
                                tokensNPN.add(correct);
                            }
                            if (listcom == 2) {
                                System.out.println("Запрос на удаление элемента из списка записан");
                                Token correct = new Token("CORRECT", "REMOVE+");
                                tokensNPN.add(correct);
                            }
                            if (listcom == 3) {
                                System.out.println("Запрос на отображение элемента из списка записан");
                                Token correct = new Token("CORRECT", "GET+");
                                tokensNPN.add(correct);
                            }
                            if (listcom == 4) {
                                System.out.println("Запрос на поиск элемента в списке записан");
                                Token correct = new Token("CORRECT", "CONTAINS+");
                                tokensNPN.add(correct);
                            }
                            if (listcom ==5) {
                                tokensNPN.add(token);
                                System.out.println("Запрос изменение элемента списка записан");
                                Token correct = new Token("CORRECT", "SET+");
                                tokensNPN.add(correct);
                            }
                            listcom = 0;
                            if (num != tokens.size()) {
                                token = tokens.get(num);
                                if (iflist == 0) {
                                    parsestart();
                                }
                                if (iflist == 1) {
                                    iflist = 0;
                                    brRFexpect();
                                }
                            }
                        }
                    }
                }
            }
        } else {
            System.out.println("Ошибка ввода. Список не создан");
            System.out.println(token.getType() + "  " + token.getValue());
            String size = Integer.toString(tokensNPN.size());
            Token varerr = new Token("ERROR", size);
            tokensNPN.add(varerr);
            match();
            parsestart();
        }
    }

    public void setTokens(List<Token> tokens) {
        this.tokens = tokens;
    }
}