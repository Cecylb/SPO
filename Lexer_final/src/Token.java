public class Token {

    private String toktype;                                                    //объявляем переменную-тип токена
    private String value;                                                      //объявляем переменную-значение токена

    Token(String toktype, String value){                                       //объявляем функцию токена (для создания списка токенов)
        this.toktype = toktype;
        this.value = value;
    }

    public String getValue(){                                                         //функция возврата значения токена (для мейна)
        return value;
    }

    public String getType(){                                                          //функция возврата типа токена (для мейна)
        return toktype;
    }

}