package com.example.sistemas.tomapedidos.Entidades;

import java.io.Serializable;

public class DiasPactados implements Serializable {

    private String dia1;
    private String dia2;
    private String dia3;
    private String dia4;
    private String dia5;
    private String dia6;
    private String dia7;
    private String dia8;
    private String dia9;
    private String dia10;

    public DiasPactados(String dia1, String dia2, String dia3, String dia4, String dia5, String dia6, String dia7, String dia8, String dia9, String dia10) {
        this.dia1 = dia1;
        this.dia2 = dia2;
        this.dia3 = dia3;
        this.dia4 = dia4;
        this.dia5 = dia5;
        this.dia6 = dia6;
        this.dia7 = dia7;
        this.dia8 = dia8;
        this.dia9 = dia9;
        this.dia10 = dia10;
    }

    public DiasPactados() {
    }

    public String getDia1() {
        return dia1;
    }

    public void setDia1(String dia1) {
        this.dia1 = dia1;
    }

    public String getDia2() {
        return dia2;
    }

    public void setDia2(String dia2) {
        this.dia2 = dia2;
    }

    public String getDia3() {
        return dia3;
    }

    public void setDia3(String dia3) {
        this.dia3 = dia3;
    }

    public String getDia4() {
        return dia4;
    }

    public void setDia4(String dia4) {
        this.dia4 = dia4;
    }

    public String getDia5() {
        return dia5;
    }

    public void setDia5(String dia5) {
        this.dia5 = dia5;
    }

    public String getDia6() {
        return dia6;
    }

    public void setDia6(String dia6) {
        this.dia6 = dia6;
    }

    public String getDia7() {
        return dia7;
    }

    public void setDia7(String dia7) {
        this.dia7 = dia7;
    }

    public String getDia8() {
        return dia8;
    }

    public void setDia8(String dia8) {
        this.dia8 = dia8;
    }

    public String getDia9() {
        return dia9;
    }

    public void setDia9(String dia9) {
        this.dia9 = dia9;
    }

    public String getDia10() {
        return dia10;
    }

    public void setDia10(String dia10) {
        this.dia10 = dia10;
    }
}
