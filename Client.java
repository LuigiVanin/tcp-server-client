import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.Socket;
import java.util.UUID;

public class Client {

  static class Payload {
    UUID id;
    String name;
    String text;

    public Payload(UUID id, String name, String text) {
      this.id = id;
      this.name = name;
      this.text = text;
    }

    public String toString() {
      return String.join("|", this.id.toString(), this.name, this.text);
    }

  }

  static class PayloadReader {
    String raw;

    public PayloadReader(String raw) {
      this.raw = raw;
    }

    public Payload read() throws Exception {
      String[] responses = this.raw.split("\\|", 3);

      if (responses.length != 3) {
        throw new Exception("Payload Mal Formatado: " + this.raw);
      }

      var uuid = UUID.fromString(responses[0]);
      var name = responses[1];
      var text = responses[2];

      return new Payload(uuid, name, text);
    }

  }

  public static void main(String[] aStrings) {
    System.out.println("Hello From Client!");

    String host = "localhost";
    int port = 6000;

    var keyboard = new BufferedReader(new InputStreamReader(System.in));

    try {

      System.out.print("Digite seu nome: ");
      System.out.flush();

      String name = keyboard.readLine();

      if (name == null || name.isBlank()) {
        System.err.println("Nome inválido, encerrando.");
        return;
      }

      name = name.trim();
      var userId = UUID.randomUUID();

      try (var socket = new Socket(host, port);
          var out = new PrintWriter(socket.getOutputStream(), true);
          var reader = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

        System.out.println("Conectado em " + socket.getRemoteSocketAddress() + " como " + name + "\n");
        System.out.println("Digite suas mensagens (linha vazia ou Ctrl+D encerra)");

        while (true) {
          System.out.print("> ");
          System.out.flush();

          String text = keyboard.readLine();

          if (text == null || text.isBlank()) {
            break;
          }

          var payload = new Payload(userId, name, text);
          out.println(payload);

          String answer = reader.readLine();

          if (answer == null) {
            System.err.println("Servidor encerrou a conexão");
            break;
          }

          var response = new PayloadReader(answer).read();

          if (response.id != userId) {
            System.out.println("[" + response.name + "]: " + response.text);
          }
        }

      }

      System.out.println("conexão finalizada");

    } catch (ConnectException e) {
      System.err.println("Não foi possível conectar em " + host + ":" + port + " - o servidor está rodando?");
    } catch (Exception e) {
      System.err.println("Erro no cliente: " + e.getMessage());
    }
  }
}
