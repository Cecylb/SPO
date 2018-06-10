import java.util.*;
import java.util.Stack;

import static java.lang.Integer.parseInt;

public class StackNpn {
    Token token;
    Token buf;
    Token F = new Token("FALSEJM", "!F");
    Token p1 = new Token("P1", "P1");
    Token p2 = new Token("P2", "P2");
    Token END = new Token("END", "END");
    List<Token> tokensNPN = new ArrayList<>();
    List<Token> NPN = new ArrayList<>();
    Stack<Token> npn = new Stack<>();
    LinkedList list = new LinkedList();


    public void setTokens(List<Token> tokensNPN) {
        this.tokensNPN = tokensNPN;
    }
    public void StackFiller() {
        int size = tokensNPN.size();
        size = size - 1;
        for(Token token: tokensNPN){                                    //цикл
                System.out.println(token.getType() + "  " + token.getValue());     //вывод результата работы лексера
        }
        NPN.add(END);
        for (int i = size; i > 0; i--) {
            token = tokensNPN.get(i);
            System.out.println(token.getType() + " -----------LOOP START---------- " + token.getValue());
            if (token.getType().equals("ERR")) {
                while (token.getType() != "CORRECT") {
                    if (!token.getType().equals("CORRECT")) {
                        System.out.println(token.getType() + " -----------START CYCLE---------- " + token.getValue());
                        i--;
                        token = tokensNPN.get(i);
                    }
                }
            }
/***************************************************БЛОК ДЛЯ СПИСКА**************************************************/
                if (token.getValue().equals("LIST+")) {
                    Token buf = token;
                    while (!token.getType().equals("VAR_TYPE")){
                        i--;
                        token = tokensNPN.get(i);
                    }
                        NPN.add(tokensNPN.get(i));
                    while (!token.getType().equals("VAR")){
                        i--;
                        token = tokensNPN.get(i);
                    }
                    NPN.add(tokensNPN.get(i));
                    while (!token.getType().equals("LIST_KW")){
                        i--;
                        token = tokensNPN.get(i);
                    }
                    NPN.add(buf);
                    System.out.println("----------------------------LIST CREATED---------------------------------");
                }
                if (token.getValue().equals("ADD+") || token.getValue().equals("REMOVE+") || token.getValue().equals("GET+") || token.getValue().equals("CONTAINS+") || token.getValue().equals("SET+")) {
                    Token buf = token;
                    while (!token.getValue().equals("List")) {
                        if (token.getType().equals("VAR") || token.getType().equals("NUMBER")) {
                            NPN.add(tokensNPN.get(i));
                        }
                        i--;
                        token = tokensNPN.get(i);
                    }
                    NPN.add(buf);
                    System.out.println("-------------------------------LIST--------------------------------------");
                }
/***************************************************БЛОК ДЛЯ ВЫРАЖЕНИЯ***************************************/
                if (token.getValue().equals("EXP+")) {
                    int eq = 0;
                    while (!token.getValue().equals("=")) {
                        i--;
                        token = tokensNPN.get(i);
                    }
                    i--;
                    NPN.add(tokensNPN.get(i));
                    i = i + 2;
                    token = tokensNPN.get(i);
                    while (!token.getValue().equals(";")) {
                        if (token.getType().equals("OPERATION")) {
                            i++;
                        }
                        token = tokensNPN.get(i);
                        if (token.getType().equals("VAR") || token.getType().equals("NUMBER")) {
                            NPN.add(tokensNPN.get(i));
                            i--;
                            if (eq < 0) {
                                NPN.add(tokensNPN.get(i));
                            }
                            eq++;
                            i = i + 2;
                        }
                        token = tokensNPN.get(i);
                        if (!token.getValue().equals(";")) {
                            i++;
                            token = tokensNPN.get(i);
                            if (token.getType().equals("VAR") || token.getType().equals("NUMBER")) {
                                NPN.add(tokensNPN.get(i));
                                i--;
                                if (eq > 0) {
                                    NPN.add(tokensNPN.get(i));
                                }
                                eq++;
                                i = i + 2;
                                token = tokensNPN.get(i);
                            }
                        }
                        if (token.getValue().equals("(")) {
                            i++;
                            NPN.add(tokensNPN.get(i));
                            i = i + 2;
                            NPN.add(tokensNPN.get(i));
                            i--;
                            NPN.add(tokensNPN.get(i));
                            i = i + 2;
                            token = tokensNPN.get(i);
                            while (!token.getValue().equals(")")) {
                                i++;
                                NPN.add(tokensNPN.get(i));
                                i--;
                                NPN.add(tokensNPN.get(i));
                                i = i + 2;
                                token = tokensNPN.get(i);
                            }
                        }
                        token = tokensNPN.get(i);
                    }
                    buf = tokensNPN.get(i);
                    while (!token.getValue().equals("=")) {
                        i--;
                        token = tokensNPN.get(i);
                    }
                    NPN.add(tokensNPN.get(i));
                    NPN.add(buf);
                    i--;
                    token = tokensNPN.get(i);
                    eq = 0;
                    System.out.println("-------------------------------EXPR--------------------------------------");
                }


/***************************************************БЛОК ДЛЯ ЦИКЛА*******************************************/
                if (token.getValue().equals("FOR+")) {
                    NPN.add(p2);
                    i = i - 2;
                    token = tokensNPN.get(i);
                    while (!token.getValue().equals("{")) {
/***************************************************БЛОК ДЛЯ СПИСКА**************************************************/
                        if (token.getValue().equals("ADD+") || token.getValue().equals("REMOVE+") || token.getValue().equals("GET+") || token.getValue().equals("CONTAINS+") || token.getValue().equals("SET+")) {
                            Token buf = token;
                            while (!token.getValue().equals("List")) {
                                if (token.getType().equals("VAR") || token.getType().equals("NUMBER")) {
                                    NPN.add(tokensNPN.get(i));
                                }
                                i--;
                                token = tokensNPN.get(i);
                            }
                            i--;
                            token = tokensNPN.get(i);
                            NPN.add(buf);
                            System.out.println("-------------------------------LIST--------------------------------------");
                        }
/***************************************************БЛОК ДЛЯ ВЫРАЖЕНИЯ***************************************/
                        if (token.getValue().equals("EXP+")) {
                            int eq = 0;
                            while (!token.getValue().equals("=")) {
                                i--;
                                token = tokensNPN.get(i);
                            }
                            i--;
                            NPN.add(tokensNPN.get(i));
                            i = i + 2;
                            token = tokensNPN.get(i);
                            while (!token.getValue().equals(";")) {
                                if (token.getType().equals("OPERATION")) {
                                    i++;
                                }
                                token = tokensNPN.get(i);
                                if (token.getType().equals("VAR") || token.getType().equals("NUMBER")) {
                                    NPN.add(tokensNPN.get(i));
                                    i--;
                                    if (eq < 0) {
                                        NPN.add(tokensNPN.get(i));
                                    }
                                    eq++;
                                    i = i + 2;
                                }
                                token = tokensNPN.get(i);
                                if (!token.getValue().equals(";")) {
                                    i++;
                                    token = tokensNPN.get(i);
                                    if (token.getType().equals("VAR") || token.getType().equals("NUMBER")) {
                                        NPN.add(tokensNPN.get(i));
                                        i--;
                                        if (eq > 0) {
                                            NPN.add(tokensNPN.get(i));
                                        }
                                        eq++;
                                        i = i + 2;
                                        token = tokensNPN.get(i);
                                    }
                                }
                                if (token.getValue().equals("(")) {
                                    i++;
                                    NPN.add(tokensNPN.get(i));
                                    i = i + 2;
                                    NPN.add(tokensNPN.get(i));
                                    i--;
                                    NPN.add(tokensNPN.get(i));
                                    i = i + 2;
                                    token = tokensNPN.get(i);
                                    while (!token.getValue().equals(")")) {
                                        i++;
                                        NPN.add(tokensNPN.get(i));
                                        i--;
                                        NPN.add(tokensNPN.get(i));
                                        i = i + 2;
                                        token = tokensNPN.get(i);
                                    }
                                }
                                token = tokensNPN.get(i);
                            }
                            buf = tokensNPN.get(i);
                            while (!token.getValue().equals("=")) {
                                i--;
                                token = tokensNPN.get(i);
                            }
                            NPN.add(tokensNPN.get(i));
                            NPN.add(buf);
                            i = i - 2;
                            token = tokensNPN.get(i);
                            eq = 0;
                            System.out.println("-------------------------------EXPR--------------------------------------");
                        }
                    }
                    i = i - 4;
                    NPN.add(tokensNPN.get(i)); //0
                    NPN.add(tokensNPN.get(i)); //1
                    i = i + 2;
                    NPN.add(tokensNPN.get(i)); //2
                    i--;
                    NPN.add(tokensNPN.get(i)); //3
                    i = i - 8;
                    NPN.add(tokensNPN.get(i)); //4
                    i = i + 3;
                    NPN.add(tokensNPN.get(i)); //5
                    i = i + 2;
                    NPN.add(tokensNPN.get(i)); //6
                    i--;
                    NPN.add(tokensNPN.get(i)); //7
                    i = i - 5;
                    NPN.add(F);                //8
                    NPN.add(p1);               //9
                    NPN.add(tokensNPN.get(i)); //10
                    i = i + 2;
                    NPN.add(tokensNPN.get(i)); //11
                    i--;
                    NPN.add(tokensNPN.get(i)); //12
                    i = i - 3;
                    System.out.println("-------------------------------FOR--------------------------------------");
                    NPN.add(tokensNPN.get(i));
                    token = tokensNPN.get(i);
                }
        }
        if (token.getType().equals("VAR") || token.getType().equals("LIST_KW")) {
            Stackbegin();
        }
    }
    /************************************************************************************************************************************************STACK****************************************************/
    private void Stackbegin() {
        for (Token token : NPN) {
            System.out.print("  " + token.getValue());
        }
        int I = 0;
        int OP = 0;
        int var = 0;
        int var2 = 0;
        int buf = 0;
        int expr = 0;
        int save = 0;
        int count = 0;
        int i = NPN.size();
        i--;
        int lim = 0;
        int loop = 0;
        while (!token.getType().equals("END")) {
            token=NPN.get(i);
/***********************************************************************************************************************************************************/
            if (token.getType().equals("GR_SYNT")) {
                expr=1;
                i--;
                token=NPN.get(i);
                while(!token.getType().equals("GR_SYNT")){
                    if(token.getType().equals("VAR") || token.getType().equals("NUMBER")) {
                        if(count==0) {
                            npn.push(NPN.get(i));
                            token=npn.peek();
                        }
                        i--;
                        npn.push(NPN.get(i));
                        token = npn.pop();
                        if (token.getType().equals("NUMBER")) {
                            var = parseInt(token.getValue());
                            token = npn.pop();
                            if (token.getType().equals("NUMBER")) {
                                var2 = parseInt(token.getValue());
                                token = npn.pop();
                            }
                            if (token.getType().equals("VAR")) {
                                var2 = OP;
                                token = npn.pop();
                            }
                        }
                        if (token.getType().equals("VAR")) {
                            var = OP;
                            token = npn.pop();
                            if (token.getType().equals("NUMBER")) {
                                var2 = parseInt(token.getValue());
                                token = npn.pop();
                            }
                            if (token.getType().equals("VAR")) {
                                var2 = OP;
                                token = npn.pop();
                            }
                        }
                        if (token.getValue().equals("+")) {
                            buf = var + var2;
                        }
                        if (token.getValue().equals("-")) {
                            buf = var - var2;
                        }
                        if (token.getValue().equals("*")) {
                            buf = var * var2;
                        }
                        if (token.getValue().equals("/")) {
                            buf = var / var2;
                        }
                        if (token.getValue().equals("=")) {
                            I = var2;
                            count=0;
                            expr=0;
                            while (!token.getType().equals("GR_SYNT")){
                                if(!token.getType().equals("FOR_KW")) {
                                    if(!token.getType().equals("CORRECT")) {
                                        i--;
                                        token = NPN.get(i);
                                    }
                                } else {
                                    Token dumbway = new Token("GR_SYNT", ":^]");
                                    token=dumbway;
                                }
                            }
                        }
                        if(expr==1) {
                            String buffer = Integer.toString(buf);
                            Token cur = new Token("NUMBER", buffer);
                            count = 1;
                            npn.push(cur);
                            token = npn.peek();
                        }
                    } else if(!token.getType().equals("GR_SYNT")) {
                        npn.push(NPN.get(i));
                        i--;
                        token = NPN.get(i);
                    }
                }
                System.out.println();
                System.out.printf("I============%d\n", I);
                OP=I;
            }
/************************************************************************************************************************************************FOR******************************************************/
/*********************************************УСЛОВИЕ 1******************************************************/
            if (token.getType().equals("FOR_KW")) {
                i--;
                npn.push(NPN.get(i));
                i--;
                npn.push(NPN.get(i));
                i--;
                npn.push(NPN.get(i));
                i--;
                npn.pop();
                token = npn.pop();
                npn.pop();
                if (token.getType().equals("VAR")) {
                    I = OP;
                }

                if (token.getType().equals("NUMBER")) {
                    I = parseInt(token.getValue());
                }
                i=i-2;
                token = NPN.get(i);
/*********************************************УСЛОВИЕ 2******************************************************/
                while (!token.getType().equals("P2")) {
                    loop = i;
                    npn.push(NPN.get(i));
                    token = NPN.get(i);
                    i--;
                    npn.push(NPN.get(i));
                    i--;
                    npn.push(NPN.get(i));
                    i--;
                    npn.pop();
                    token = npn.pop();
                    if (token.getType().equals("NUMBER")) {
                        lim = parseInt(token.getValue());
                    }
                    if (token.getType().equals("VAR")) {
                        lim = var;
                    }
                    if (I < lim) {
                        npn.push(p1);
                    } else {
                        npn.push(p2);
                    }
                    token = npn.pop();
                    if (token.getType().equals("P1")) {
/*********************************************УСЛОВИЕ 3******************************************************/
                        npn.push(NPN.get(i));
                        i--;
                        npn.push(NPN.get(i));
                        i--;
                        npn.push(NPN.get(i));
                        i--;
                        npn.push(NPN.get(i));
                        i--;
                        npn.push(NPN.get(i));
                        npn.pop();
                        npn.pop();
                        i--;
                        token = npn.pop();
                        if (token.getType().equals("VAR")) {
                            token = npn.pop();
                            if (token.getValue().equals("+")) {
                                I = I + OP;
                            }
                            if (token.getValue().equals("-")) {
                                I = I - OP;
                            }
                            if (token.getValue().equals("*")) {
                                I = I * OP;
                            }
                            if (token.getValue().equals("/")) {
                                I = I / OP;
                            }
                        }
                        if (token.getType().equals("NUMBER")) {
                            var = parseInt(token.getValue());
                            token = npn.pop();
                            if (token.getValue().equals("+")) {
                                I = I + var;
                            }
                            if (token.getValue().equals("-")) {
                                I = I - var;
                            }
                            if (token.getValue().equals("*")) {
                                I = I * var;
                            }
                            if (token.getValue().equals("/")) {
                                I = I / var;
                            }
                        }
                        token = NPN.get(i);
                        while (!token.getType().equals("P2")) {
                            token = NPN.get(i);

                            if (token.getType().equals("GR_SYNT")) {
                                expr = 1;
                                i--;
                                token = NPN.get(i);
                                while (!token.getType().equals("GR_SYNT")) {
                                    if(token.getType().equals("CORRECT")){
                                        break;
                                    }
                                    if (token.getType().equals("VAR") || token.getType().equals("NUMBER")) {
                                        if (count == 0) {
                                            npn.push(NPN.get(i));
                                            token = npn.peek();
                                        }
                                        i--;
                                        npn.push(NPN.get(i));
                                        token = npn.pop();
                                        if (token.getType().equals("NUMBER")) {
                                            var = parseInt(token.getValue());
                                            token = npn.pop();
                                            if (token.getType().equals("NUMBER")) {
                                                var2 = parseInt(token.getValue());
                                                token = npn.pop();
                                            }
                                            if (token.getType().equals("VAR")) {
                                                var2 = I;
                                                token = npn.pop();
                                            }
                                        }
                                        if (token.getType().equals("VAR")) {
                                            var = I;
                                            token = npn.pop();
                                            if (token.getType().equals("NUMBER")) {
                                                var2 = parseInt(token.getValue());
                                                token = npn.pop();
                                            }
                                            if (token.getType().equals("VAR")) {
                                                var2 = I;
                                                token = npn.pop();
                                            }
                                        }
                                        if (token.getValue().equals("+")) {
                                            buf = var + var2;
                                        }
                                        if (token.getValue().equals("-")) {
                                            buf = var - var2;
                                        }
                                        if (token.getValue().equals("*")) {
                                            buf = var * var2;
                                        }
                                        if (token.getValue().equals("/")) {
                                            buf = var / var2;
                                        }
                                        if (token.getValue().equals("=")) {
                                            I = buf;
                                            count = 0;
                                            expr = 0;
                                            while (!token.getType().equals("GR_SYNT")) {
                                                if(token.getType().equals("CORRECT")){
                                                    break;
                                                }
                                                if (!token.getType().equals("P2")) {
                                                    i--;
                                                    token = NPN.get(i);
                                                } else {
                                                    Token dumbway = new Token("GR_SYNT", ":^)");
                                                    token = dumbway;
                                                }
                                            }
                                        }
                                        if (expr == 1) {
                                            String buffer = Integer.toString(buf);
                                            Token cur = new Token("NUMBER", buffer);
                                            count = 1;
                                            npn.push(cur);
                                            token = npn.peek();
                                        }
                                    } else if (!token.getType().equals("GR_SYNT")) {
                                        npn.push(NPN.get(i));
                                        i--;
                                        token = NPN.get(i);
                                    }

                                }
                                OP = I;
                                System.out.println();
                                System.out.printf("I============%d\n", I);
                            }
/************************************************************************************************************************************************************************/
                            if(token.getType().equals("CORRECT")){
                                if(token.getValue().equals("ADD+")) {
                                    i--;
                                    token = NPN.get(i);
                                    if (token.getType().equals("VAR")) {
                                        list.add(I);
                                    }
                                    if(token.getType().equals("NUMBER")){
                                        int num = 0;
                                        num = parseInt(token.getValue());
                                        list.add(num);
                                    }
                                }
                                if(token.getValue().equals("REMOVE+")){
                                    i--;
                                    token = NPN.get(i);
                                    if (token.getType().equals("VAR")) {
                                        list.remove(I);
                                    }
                                    if(token.getType().equals("NUMBER")){
                                        int num = 0;
                                        num = parseInt(token.getValue());
                                        list.remove(num);
                                    }
                                }
                                if(token.getValue().equals("GET+")){
                                    i--;
                                    token = NPN.get(i);
                                    if (token.getType().equals("VAR")) {
                                        list.get(I);
                                    }
                                    if(token.getType().equals("NUMBER")){
                                        int num = 0;
                                        num = parseInt(token.getValue());
                                        list.get(num);
                                    }
                                }
                                if(token.getValue().equals("CONTAINS+")){
                                    i--;
                                    token = NPN.get(i);
                                    if (token.getType().equals("VAR")) {
                                        list.contains(I);
                                    }
                                    if(token.getType().equals("NUMBER")){
                                        int num = 0;
                                        num = parseInt(token.getValue());
                                        list.contains(num);
                                    }
                                }
                                i--;
                                token=NPN.get(i);
                            }
                        }
                        token = NPN.get(i);
                        System.out.println(token.getType() + "  " + token.getValue());
                        save=i;
                        i = loop;
                        token = NPN.get(i);
                    }
                }
                System.out.printf("I=%d\n", I);
                i=save;
                i--;
                token = NPN.get(i);
            }
/************************************************************************************************************************************************************************/
            if(token.getValue().equals("ADD+") || token.getValue().equals("REMOVE+") || token.getValue().equals("GET+") || token.getValue().equals("CONTAINS+") || token.getValue().equals("SET+")){
                if(token.getValue().equals("ADD+")) {
                    i--;
                    token = NPN.get(i);
                    if (token.getType().equals("VAR")) {
                        list.add(I);
                    }
                    if(token.getType().equals("NUMBER")){
                        int num = parseInt(token.getValue());
                        list.add(num);
                    }
                }
                if(token.getValue().equals("REMOVE+")){
                    i--;
                    token = NPN.get(i);
                    if (token.getType().equals("VAR")) {
                        list.remove(I);
                    }
                    if(token.getType().equals("NUMBER")){
                        int num = parseInt(token.getValue());
                        list.remove(num);
                    }
                }
                if(token.getValue().equals("GET+")){
                    i--;
                    token = NPN.get(i);
                    if (token.getType().equals("VAR")) {
                        System.out.printf("Элемент %d = %s\n", I, list.get(I));
                    }
                    if(token.getType().equals("NUMBER")){
                        int num = parseInt(token.getValue());
                        list.get(num);
                        System.out.printf("Элемент %d = %s\n", num, list.get(num));
                    }
                }
                if(token.getValue().equals("CONTAINS+")){
                    i--;
                    token = NPN.get(i);
                    if (token.getType().equals("VAR")) {
                        list.contains(I);
                    }
                    if(token.getType().equals("NUMBER")){
                        int num = parseInt(token.getValue());
                        System.out.printf("Есть ли %d? = %s\n", num, list.contains(num));
                    }
                }
                if(token.getValue().equals("SET+")){

                    i--;
                    token = NPN.get(i);
                    if (token.getType().equals("VAR")) {
                        i--;
                        token = NPN.get(i);
                        if (token.getType().equals("VAR")) {
                            list.set(I, I);
                        }
                        if(token.getType().equals("NUMBER")){
                            int num = parseInt(token.getValue());
                            list.set(I, num);
                        }
                    }
                    if(token.getType().equals("NUMBER")){
                        int num = parseInt(token.getValue());
                        i--;
                        token = NPN.get(i);
                        if (token.getType().equals("VAR")) {
                            list.set(num, I);
                        }
                        if(token.getType().equals("NUMBER")){
                            int num2 = parseInt(token.getValue());
                            list.set(num, num2);
                        }
                    }
                }
                i--;
                token=NPN.get(i);
            }
            if(token.getValue().equals("LIST+")){
                i=i-3;
                token=NPN.get(i);
            }
            if(token.getValue().equals("END")){
                break;
            }
        }
    }
}