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

  static void listen(BufferedReader reader, UUID userId) {
    try {
      String answer;

      while ((answer = reader.readLine()) != null) {
        var response = new PayloadReader(answer).read();

        if (!response.id.equals(userId)) {
          System.out.println("[" + response.name + "]: " + response.text);
          System.out.print("> ");
          System.out.flush();
        }
      }

      System.err.println("Servidor encerrou a conexão");

    } catch (Exception e) {
      System.err.println("Conexão encerrada");
    }
  }

  public static void main(String[] aStrings) {
    System.out.println("CLIENT Application\n========!");

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

        var listener = new Thread(() -> listen(reader, userId));
        listener.setDaemon(true);
        listener.start();

        while (true) {
          // System.out.print("[" + name + "] ");
          System.out.print("> ");

          System.out.flush();

          String text = keyboard.readLine();

          if (text == null || text.isBlank()) {
            break;
          }

          var payload = new Payload(userId, name, text);
          out.println(payload);
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
