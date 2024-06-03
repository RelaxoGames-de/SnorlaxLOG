
# SnorlaxLOG

This plugin is the main backend libary of the RelaxoGames.de Minigame Network. SnorlaxLOG is a multi platform based Minecraft server plugin.


# Documentation

Before you continue please consider that you are logged in into the [PixelMedia DevEnd](http://relaxogames.de/development)
## Used By

This libary is mainly used by the following plugins:

- SnorlaxBan (Ban System)
- FraxureAPI (Friend System)
- NexusAPI (Minigames)


## Installation

Import the SnorlaxLOG libary adding this code to your pom.xml
```xml
    <!-- SnorlaxLOG -->
        <dependency>
            <groupId>de.relaxogames</groupId>
            <artifactId>SnorlaxLOG</artifactId>
            <version>inDev-1.11.1</version>
        </dependency>`
```

If you want to install SnorlaxLOG on your device:

```sh
mvn clean install
```

If you want to package this libary for a server use this instead:

```sh
mvn clean package
```
## Support

For support, email support@relaxogames.de or write an issue.


## Authors

- [@DevTexx](https://github.com/DevTexx)
- [@Jotrorox](https://github.com/Jotrorox)
