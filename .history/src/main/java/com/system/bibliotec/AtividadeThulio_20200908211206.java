package com.system.bibliotec;

import java.util.Scanner;

public class AtividadeThulio {
  public static void main(String[] args) {
      
    Scanner myObj = new Scanner(System.in);

    System.out.println("###########################################################");
    System.out.println("###########################################################");
    System.out.println("###########################################################");
    System.out.println("ATIVIDADE : Elaboração de Algoritmo - MAIOR, MENOR OU IGUAL");
    System.out.println("###########################################################");
    System.out.println("###########################################################");
    System.out.println("###########################################################");

    System.out.println();
    System.out.println();
    System.out.println();

    System.out.println("INFORME O PRIMEIRO NUMERO ");

    int numero1 = myObj.nextInt();

    System.out.println();
    System.out.println();
    System.out.println();

    System.out.println("INFORME O SEGUNDO NUMERO ");

    int numero2 = myObj.nextInt();

    System.out.println();
    System.out.println();

    boolean n1IsMaior = (numero1 > numero2)? true: false;
    boolean n2IsMaior = (numero2 > numero1)? true: false;

    




    
    System.out.println("###########################################################");
    System.out.println("###########################################################");
    System.out.println("###########################################################");
    System.out.println("------------------------------------------------------------");
    System.out.println("###########################################################");
    System.out.println("###########################################################");
    System.out.println("###########################################################");
    
    
  }
}

