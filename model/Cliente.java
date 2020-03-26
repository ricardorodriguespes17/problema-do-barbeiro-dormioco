package model;

import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import controller.ControladorTela;

public class Cliente extends Thread{
  //Atributos
  private ControladorTela c;
  private ImageView i;
  private int id, bancoOcupado;
  public double posicaox, posicaoy;

  /* ***************************************************************
  * Metodo: run
  * Funcao: metodo principal da classe thread
  * Parametros: *sem parametro*
  * Retorno: *sem retorno*
  *************************************************************** */
  public void run(){
    try{
      sleep(2000);
      c.botaoMaisClientes.setDisable(false); //desabilita o botao de adicionar clientes
      c.mutex.acquire();  //down mutex
      if(c.esperando < c.cadeiras){
        c.sentarBanco(this);  //senta no banco de espera
        c.esperando++;
        c.clientes.release();  //up clientes
        c.mutex.release();  //up mutex
        c.barbeiros.acquire();  //down barbeiros
        c.sentarCadeira(this);  //senta na cadeira do barbeiro
      }else{
        c.mutex.release();  //up mutex
        c.sair(this);  //sai da barbearia
      }
    }catch(InterruptedException e){}
  }

  /* ***************************************************************
  * Metodo: entrarClientes
  * Funcao: gera um cliente quando aperta o botao 'mais clientes'
  * Parametros: controlador da tela fxml
  * Retorno: *sem retorno*
  *************************************************************** */
  public void setControlador(ControladorTela c){
    this.c = c;
  }

  //Geteres e seteres

  public void setImage(ImageView i){
    //reposiciona e aparece a imageview do cliente
    i.setImage(new Image("imagens/pessoa" + id + "_entrando.png"));
    i.setLayoutX(230);
    i.setLayoutY(480);
    i.setRotate(180); 
    i.setVisible(true); //aparece a image do cliente
    this.i = i;
    posicaoy = i.getLayoutY();
    posicaox = i.getLayoutX();
  }

  public ImageView getImage(){
    return i;
  }

  public void setId(int id){
    this.id = id;
  }

  public int getid(){
    return id;
  }

  public void setBancoOcupado(int i){
    bancoOcupado = i;
  }

  public int getBancoOcupado(){
    return bancoOcupado;
  }
}