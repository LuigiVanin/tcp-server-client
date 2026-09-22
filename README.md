## Resumo

Aplicação de um chat TCP usando as classes de Socket e SocketServer do Java para implementar uma estrutura de cliente servidor.

O projeto suporta múltiplos clientes simultâneos nomeados. Para rodar a aplicação de maneira correta, primeiro rode o [Server.java](./Server.java) seguido por n [Client.java](./Client.java) em outros terminais. Para propagar as mensagens pela lista de clientes conectados a um serverdor se usou a estratégia de broadcast.


## Como rodar:

> [!warning]
> O projeto rodou usando java v25 (openjdk 25.0.2). Testei também a compatibilidade com java 17 e em tese deve funcionar também.


### Server
Deve ser testado primeiro. *Para funcionar propriamente, a porta 6000 deve estar liberada em seu computador.*

```bash
> java Server.java
```

Se quiser buildar (e rodar):
```bash
> javac -d build/ Client.java
> java -cp build/ Client.java
```

### Client

```bash
> java Client.java
```

Se quiser buildar (e rodar):
```bash
> javac -d build/ Client.java
> java -cp build/ Client.java
```



> [!note]
> Adicinei as flags de destino `-d` nos comandos de build, mas não necessários. Caso rode sem a flag, será necessário remover a flag `cp` do comando Java