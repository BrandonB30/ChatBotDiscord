# Discord Bot con Interfaz Gráfica

Este proyecto es un bot de Discord desarrollado en Java con una interfaz gráfica que permite controlar y monitorear su funcionamiento fácilmente. La aplicación permite conectar un bot a Discord mediante JDA (Java Discord API) y responder automáticamente a mensajes y comandos.

## Características

- Interfaz gráfica para gestionar el bot
- Activación/desactivación simple mediante un botón
- Área de registro para monitorear la actividad del bot
- Respuestas automáticas a mensajes comunes
- Sistema de comandos personalizables
- Seguimiento de conversaciones activas

## Requisitos

- Java 8 o superior
- Biblioteca JDA (Java Discord API)
- Token de Bot de Discord (desde el portal de desarrolladores)
- Permisos de bot adecuados en Discord

## Instalación

1. Clona este repositorio:
   ```bash
   git clone https://github.com/tu-usuario/discord-bot.git
   cd discord-bot
   ```

2. Compila el proyecto con Maven o tu IDE preferido.

3. Ejecuta la aplicación:
   ```bash
   java -jar DiscordBot.jar
   ```

## Configuración

1. Visita el [Portal de Desarrolladores de Discord](https://discord.com/developers/applications)
2. Crea una nueva aplicación y un bot
3. Activa los "Intents" necesarios:
   - MESSAGE CONTENT
   - GUILD MESSAGES
   - DIRECT MESSAGES
4. Copia el token del bot
5. Pégalo en la aplicación y activa el bot

## Comandos del Bot

El bot responde a los siguientes comandos:

| Comando | Descripción |
|---------|-------------|
| `!start` | Inicia una conversación con el bot |
| `!help` | Muestra la lista de comandos disponibles |
| `!hora` | Muestra la hora actual del servidor |
| `!info` | Muestra información sobre el bot |
| `!conversaciones` | Muestra la lista de conversaciones activas |

## Respuestas Automáticas

El bot responde automáticamente a mensajes como:

- "hola", "buenos días", "buenas tardes", "buenas noches"
- "ayuda", "gracias", "adiós", "chao"
- Preguntas sobre el creador del bot

## Personalización

Puedes personalizar las respuestas del bot modificando el método `inicializarRespuestas()` en el archivo `DiscordBotGUI.java`:

```java
private void inicializarRespuestas() {
    respuestas = new HashMap<>();
    // Añade tus propias respuestas
    respuestas.put("palabra clave", "respuesta");
}
```

## Estructura del Proyecto

- `DiscordBotGUI.java`: Clase principal con la interfaz gráfica y lógica del bot
- `DiscordMessageListener`: Clase interna que maneja los eventos de mensajes

## Licencia

[MIT](LICENSE)

## Autor

Desarrollado por Brandon Stick Bernal Rodriguez
