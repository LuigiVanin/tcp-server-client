## Como rodar:


### Client

```bash
> java Client.java
```

Se quiser buildar (e rodar):
```bash
> javac -d build/ Client.java
> java -cp build/ Client.java
```


### Server

```bash
> java Server.java
```

Se quiser buildar (e rodar):
```bash
> javac -d build/ Client.java
> java -cp build/ Client.java
```

> [!note] No Build
> Adicinei as flags de destino `-d` nos comandos de build, mas não necessários. Caso rode sem a flag, será necessário remover a flag `cp` do comando Java