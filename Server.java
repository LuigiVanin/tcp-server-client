import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.UUID;

class Payload {
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

class PayloadReader {
  Payload payload;

  String raw;

  public PayloadReader(String raw) {
    this.raw = raw;
  }

  public Payload read() throws Exception {
    if (this.raw == null) {
      throw new Exception("Payload vazio");
    }

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

          System.out.println("conexão Aceita: " + socket.getLocalAddress());

          var reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
          var out = new PrintWriter(socket.getOutputStream(), true);
          String content;

          while ((content = reader.readLine()) != null) {
            try {
              var payload = new PayloadReader(content).read();
              out.println(payload.toString());
              System.out.println("[" + payload.name + "]: " + payload.text);
            } catch (Exception e) {
              System.err.println("Descartando mensagem inválida: " + e.getMessage());
              out.println(new Payload(UUID.randomUUID(), "servidor", "ERRO: " + e.getMessage()));
            }
          }

          socket.close();
          System.out.println("conexão finalizada");

        }

      } catch (BindException e) {
        System.err.println("Porta " + port + " já está em uso!");
        port += attempts;
        continue;
      } catch (Exception e) {
        System.err.println("Erro inesperado no servidor: " + e);
        break;
      }
    }
  }
}