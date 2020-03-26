/* ***************************************************************
* Autor: Ricardo Rodrigues Neto
* Matricula: 201710560
* Inicio: ??/09/2018
* Ultima 07/10/2018
* Nome: Barbeiro Dormioco
* Funcao: Mostrar a simulacao de uma barbearia em que o barbeiro 
*   dorme quando nao tem clientes, e o cliente sai quando esta 
*   cheia a barbearia.
*************************************************************** */

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;
import controller.ControladorTela;

public class Principal extends Application{

  /* ***************************************************************
  * Metodo: start
  * Funcao: metodo principal da classe Application que inicia o
  *   palco principal
  * Parametros: palco principal da aplicacao
  * Retorno: *sem retorno*
  *************************************************************** */
  public static void main(String[] args){
    launch(args);
  } 

  /* ***************************************************************
  * Metodo: main
  * Funcao: metodo principal do projeto
  * Parametros: String[] args
  * Retorno: *sem retorno*
  *************************************************************** */
  @Override
  public void start(Stage palco) throws Exception{
    Parent r = FXMLLoader.load(getClass().getResource("/view/Tela.fxml"));
    palco.setScene(new Scene(r));
    palco.show();
  }
}