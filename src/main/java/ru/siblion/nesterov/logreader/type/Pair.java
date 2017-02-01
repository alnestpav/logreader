package ru.siblion.nesterov.logreader.type;

/**
 * Created by alexander on 01.02.2017.
 */

/* Класс, инкапсулирующий пару элементов. Используется в классе LogReader для представления
   диапазона блока сообщения (номер с и номер по строк) */
public class Pair<First, Second> {
    private First first;
    private Second second;

    public Pair(First first, Second second) {
        this.first = first;
        this.second = second;
    }

    public First getFirst() {
        return first;
    }

    public Second getSecond() {
        return second;
    }

}
