
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import javax.swing.JTextArea;

/**
 *
 * @author osmar
 */
public class Cliente {

    private String enderecoServidor;
    private int portaServidor;
    private String nome;

    private JTextArea txtMensagensEntrada;

    //Socket com o servidor
    private Socket socketServidor;
    private PrintWriter out;

    public JTextArea getTxtMensagensEntrada() {
        return txtMensagensEntrada;
    }

    public void setTxtMensagensEntrada(JTextArea txtMensagensEntrada) {
        this.txtMensagensEntrada = txtMensagensEntrada;
    }
    
    public Cliente(String enderecoServidor, int portaServidor, String nome, JTextArea txtMensagensEntrada) {
        this.enderecoServidor = enderecoServidor;
        this.portaServidor = portaServidor;
        this.nome = nome;
        this.txtMensagensEntrada = txtMensagensEntrada;        
    }

    public void conecta() {
        try {            
            txtMensagensEntrada.append("\nConectando com o servidor " + enderecoServidor + " na porta " + portaServidor);
            socketServidor = new Socket(enderecoServidor, portaServidor);//endereco e porta
            txtMensagensEntrada.append("\nCriando fluxo com o servidor.");
            out = new PrintWriter(socketServidor.getOutputStream(), true);// cria o objeto de saida para o fluxo    

            //Cria a Thread para receber as mensagens
            Runnable cliente = new TratamentoServidor(this, socketServidor);
            Thread threadCliente = new Thread(cliente);
            threadCliente.start();

            //Envia o nome do usuário na conexao
            enviaMensagem(nome); 
            
        } catch (UnknownHostException uhe) {
            txtMensagensEntrada.append("\nConexao Terminada!");
        } catch (IOException ioe) {
            txtMensagensEntrada.append("\nProblemas de IO");
        }
    }

    public void desconecta() {
        try {
            //Envia o fim da conexao para o servidor
            enviaMensagem("#fim");
            
            txtMensagensEntrada.append("\nDesconectando do servidor.");
            //Fecha a conexão            
            socketServidor.close();
            
            txtMensagensEntrada.append("\nServidor desconectado.");
        } catch (UnknownHostException uhe) {
            txtMensagensEntrada.append("\nConexao Terminada!");
        } catch (IOException ioe) {
            txtMensagensEntrada.append("\nProblemas de IO");
        }
    }

    //Envia uma mensagem
    public void enviaMensagem(String mensagem) {
        if (out != null) {
            out.println(mensagem); //escreve a mensagem
        }
    }
}
