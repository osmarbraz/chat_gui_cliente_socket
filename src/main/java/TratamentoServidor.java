
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;

public class TratamentoServidor implements Runnable {

    private Cliente cliente;
    private Socket socket;
    private BufferedReader in;

    /**
     * Construtor com parâmetros.
     *
     * @param cliente
     * @param socket     
     */
    public TratamentoServidor(Cliente cliente, Socket socket) {
        this.cliente = cliente;
        try {
            this.socket = socket;
            in = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
        } catch (IOException ioe) {
            cliente.getTxtMensagensEntrada().append("\nProblemas de IO");
        }
    }

    public void run() {
        try {
            while (true) {
                String mensagem = leituraMensagem();
                if (mensagem != null) {
                    cliente.getTxtMensagensEntrada().append("\n" + mensagem);
                }
            }
        } catch (UnknownHostException uhe) {
            cliente.getTxtMensagensEntrada().append("\nConexao Terminada!");
        } catch (IOException ioe) {
           // txtMensagens.append("\nProblemas de IO");
        }
    }

    /**
     * Recupera mensagem do fluxo
     *
     * @return
     * @throws IOException
     */
    public String leituraMensagem() throws IOException {
        String mensagem = in.readLine();
        return mensagem;
    }
}
