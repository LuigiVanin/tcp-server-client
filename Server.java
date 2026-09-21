import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
  public static void main(String[] aStrings) {
    System.out.println("Hello From Server!");

    int port = 6000;
    int maxAttmpets = 3;
    int attempts = 0;

    while (attempts < maxAttmpets) {
      attempts++;

      try (var server = new ServerSocket(port)) {

        System.out.println("Escutando com sucesso na porta " + port + "!");

        while (true) {
          Socket socket = server.accept();
          System.out.println("conexão acertada: " + socket.getLocalAddress());

          var reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
          // var out = new PrintWriter(socket.getOutputStream(), true);
          String content = "";

          while (content != null) {
            content = reader.readLine();
            System.out.println("Mensagem: " + content);
          }

          socket.close();
          System.out.println("conexão finalizada");

        }

      } catch (BindException e) {
        System.err.println("Porta " + port + " já está em uso!");
        port += attempts;
        continue;
      } catch (Exception e) {
        System.err.println("Erro desconhecido ao tentar conectar a porta " + port);
        break;
      }
    }
  }
}