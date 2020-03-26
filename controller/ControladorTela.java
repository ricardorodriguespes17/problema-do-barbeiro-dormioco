package controller;

import javafx.fxml.Initializable;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.Semaphore;
import javafx.scene.control.Spinner;
import javafx.scene.control.Button;
import model.Cliente;
import model.Barbeiro;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import javafx.fxml.FXML;
import java.util.Random;
import java.util.List;
import java.util.ArrayList;

public class ControladorTela implements Initializable{
  public Semaphore mutex, barbeiros, clientes;  //semaforos
  //numero de clientes esperando, numero de cadeiras, velocidade de corte, identificador para gerar clientes, respectivamente
  public int esperando = 0, cadeiras = 5, tempoCorte = 5000, identificador = 0;
  //array booleano para cada banco de espera, para ver se pode sentar ou nao
  public boolean[] bancos;
  //Thread do barbeiro
  public Barbeiro barbeiro;
  //botao para chegar mais clientes
  @FXML public Button botaoMaisClientes;
  //Spinner para aumentar
  @FXML public Spinner<Integer> velocidade;
  //ImageViews dos clientes e do barbeiro
  @FXML public ImageView pessoa1, pessoa2, pessoa3, pessoa4, pessoa5, pessoa6, pessoa7, barb;
  //lista de ImageView dos clientes
  public List<ImageView> pessoas;
  
  public ControladorTela(){
    mutex = new Semaphore(1);
    barbeiros = new Semaphore(0);
    clientes = new Semaphore(0);
    barbeiro = new Barbeiro();
    velocidade = new Spinner<>();
    pessoa1 = new ImageView();
    pessoa2 = new ImageView();
    pessoa3 = new ImageView();
    pessoa4 = new ImageView();
    pessoa5 = new ImageView();
    pessoa6 = new ImageView();
    pessoa7 = new ImageView();
    pessoas = new ArrayList<>();
    barb = new ImageView();
    botaoMaisClientes = new Button();
    bancos = new boolean[cadeiras];
    //inicia todas as posicoes do array 'bancos' com true
    for(int i = 0; i < cadeiras; i++){
      bancos[i] = true;
    }
  }

  /* ***************************************************************
  * Metodo: setPalco
  * Funcao: recebe o palco principal para poder fecha-lo quando for
  *   necessario
  * Parametros: palco principal da tela
  * Retorno: *sem retorno*
  *************************************************************** */
  @Override
  public void initialize(URL url, ResourceBundle rb){
    //Adicionando as ImageView dos clientes na Lista
    pessoas.add(pessoa1);
    pessoas.add(pessoa2);
    pessoas.add(pessoa3);
    pessoas.add(pessoa4);
    pessoas.add(pessoa5);
    pessoas.add(pessoa6);
    pessoas.add(pessoa7);
    //inicializando o Barbeiro
    barbeiro.setControlador(this);
    barbeiro.setDaemon(true);
    barbeiro.start();
  }

  /* ***************************************************************
  * Metodo: atualizarVelocidade
  * Funcao: atualiza a velocidade de corte do barbeiro
  * Parametros: *sem parametro*
  * Retorno: *sem retorno*
  *************************************************************** */
  @FXML
  public void atualizarVelocidade(){
    tempoCorte = 2000 * (int) velocidade.getValue();
  }

  /* ***************************************************************
  * Metodo: entrarClientes
  * Funcao: gera um cliente quando aperta o botao 'mais clientes'
  * Parametros: *sem parametro*
  * Retorno: *sem retorno*
  *************************************************************** */
  @FXML
  public void entrarClientes(){
    botaoMaisClientes.setDisable(true); //desabilita o botao de adicionar clientes
    identificador++;
    if(identificador > 7){
      identificador = 1;
    }

    Cliente c = new Cliente();  //cria novo cliente
    c.setControlador(this);  //passa o controlador por parametro para a thread
    c.setDaemon(true);  //habilita o modo de quando o fechar a tela encerrar as threads
    c.setId(identificador);  //muda o identificador do cliente

    for(ImageView imageView : pessoas){
      //procura ImageView, na lista, que esta disponivel para mandar para o cliente
      if(!imageView.isVisible()){
        c.setImage(imageView);  //muda a ImageView do cliente
        break;
      }
    }

    c.start();  //inicializa a thread
  }

  /* ***************************************************************
  * Metodo: sentarCadeira
  * Funcao: chama um cliente para sentar na cadeira do barbeiro
  * Parametros: o cliente que sentara na cadeira
  * Retorno: *sem retorno*
  *************************************************************** */
  public void sentarCadeira(Cliente c){
    //banco que o cliente estava sentado fica livre
    bancos[c.getBancoOcupado()] = true;
    c.getImage().setLayoutX(90);
    c.getImage().setLayoutY(220);
    c.getImage().setRotate(90);

    try{
      //dorme a thread de acordo com o tempo de corte
      c.sleep(tempoCorte);
    }catch(InterruptedException e){}

    //cliente muda a imagem para uma com o cabelo cortado
    c.getImage().setImage(new Image("imagens/pessoa" + c.getid() + "_cabelo_cortado.png"));
    //sai da barbearia
    sair(c);
  }

  /* ***************************************************************
  * Metodo: cortarCabelo
  * Funcao: muda o barbeiro para posicao de corte de cabelo
  * Parametros: *sem parametro*
  * Retorno: *sem retorno*
  *************************************************************** */
  public void cortarCabelo(){
    barb.setLayoutX(185);
    barb.setLayoutY(220);
    barb.setImage(new Image("imagens/barbeiro_cortando.png"));

    try{
      barbeiro.sleep(tempoCorte);
    }catch(InterruptedException e){}
  }

  public void espera(){
    barb.setLayoutX(90);
    barb.setLayoutY(220);
    barb.setImage(new Image("imagens/barbeiro_dormindo.png"));
  }

  /* ***************************************************************
  * Metodo: sentarBanco
  * Funcao: seleciona o banco e faz sentar o cliente para espera
  * Parametros: cliente que sentara no banco
  * Retorno: *sem retorno*
  *************************************************************** */
  public void sentarBanco(Cliente c){
    double posicaox = 0, posicaoy = 0;
    for(int i = 0; i < cadeiras; i++){
      //verifica se o banco esta esta livre
      if(bancos[i]){
        switch(i){
          case 0:
            posicaox = 460;
            posicaoy = 330;
            break;
          case 1:
            posicaox = 460;
            posicaoy = 180;
            break;
          case 2:
            posicaox = 460;
            posicaoy = 30;
            break;
          case 3:
            posicaox = 310;
            posicaoy = 30;
            break;
          case 4:
            posicaox = 160;
            posicaoy = 30;
            break;
          default:
            return;
        }
        //manda a posicao que esta sentando para o cliente
        c.setBancoOcupado(i);
        //banco escolhido fica ocupado
        bancos[i] = false;
        break;
      }
    }

    if(posicaoy == 0){
      //senao for escolhido nenhum banco o cliente sai da barbearia
      sair(c);
    }else{
      c.getImage().setLayoutX(posicaox);
      c.getImage().setLayoutY(posicaoy);
      c.getImage().setRotate(90);
    }
  }

  /* ***************************************************************
  * Metodo: sair
  * Funcao: coloca o cliente na posicao de saida da barbearia
  * Parametros: cliente que saira da barbearia
  * Retorno: *sem retorno*
  *************************************************************** */
  public void sair(Cliente c){
    c.getImage().setLayoutY(400);
    c.getImage().setLayoutX(230);
    c.getImage().setRotate(0);
    try{
      c.sleep(2000);
    }catch(InterruptedException e){}
    //muda a visibilidade da image do cliente para falso
    c.getImage().setVisible(false);
    //para a thread
    c.stop();
  }

}