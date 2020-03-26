package model;

import controller.ControladorTela;

public class Barbeiro extends Thread{
  private ControladorTela c;

  /* ***************************************************************
  * Metodo: run
  * Funcao: metodo principal da classe thread
  * Parametros: *sem parametro*
  * Retorno: *sem retorno*
  *************************************************************** */
  public void run(){
    while(1 > 0){
      try{
        c.clientes.acquire(); //down clientes
        c.mutex.acquire();  //down mutex
          c.esperando--;
          c.barbeiros.release();  //up barbeiros
       	c.mutex.release();  //up mutex
        c.cortarCabelo();  //barbeiro corta cabelo
        c.espera();  //volta a dormir
      }catch(InterruptedException e){}
    }
  }

  //set do controlador
  public void setControlador(ControladorTela c){
    this.c = c;
  }

}