package com.shivsoftech.core;

import com.shivsoftech.util.Constants;

import static com.shivsoftech.util.Constants.NEW_LINE;
import static com.shivsoftech.util.Constants.TAB;

public class Parameters implements Cloneable {

    String front;
    String main;
    String last;
    String output;
    String temp;

    public String getFront() {
        return front;
    }

    public void setFront(String front) {
        this.front = front;
    }

    public String getMain() {
        return main;
    }

    public void setMain(String main) {
        this.main = main;
    }

    public String getLast() {
        return last;
    }

    public void setLast(String last) {
        this.last = last;
    }

    public String getOutput() {
        return output;
    }

    public void setOutput(String output) {
        this.output = output;
    }

    public String getTemp() {
        return temp;
    }

    public void setTemp(String temp) {
        this.temp = temp;
    }

    @Override
    public String toString() {
        return "[ Parameters { " + NEW_LINE +
                TAB + "Front File : " + front + NEW_LINE +
                TAB + "Main File : " + main + NEW_LINE +
                TAB + "End File : " + last + NEW_LINE +
                TAB + "Output File : " + output + NEW_LINE +
                "}";
    }

    @Override
    public Object clone()  {
        Parameters param = new Parameters();
        param.setFront(this.getFront());
        param.setMain(this.getMain());
        param.setLast(this.getLast());
        param.setOutput(this.getOutput());
        return param;
    }
}
