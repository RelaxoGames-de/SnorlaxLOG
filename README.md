# SnorlaxLOG

This plugin is the backbone of the RelaxoGames.de Minigame Network. SnorlaxLOG is a multiplatform plugin/library serving on all important parts of the RelaxoGames Network.

# Documentation

Before you continue please ensure that you are logged in into the [PixelMedia DevEnd](http://relaxogames.de/development)

## Used By

This libary is mainly used by the following plugins:

- RelaxoBan (Ban System)
- FraxureAPI (Friend System)
- NexusAPI (Minigames)

But most of the plugins use it to some extend and we recommend you use it to!

## Using it as a dependency

> Please ensure before you try to import it to install SnorlaxLOG

To use this library import it into you maven project like this:
```xml
<!-- SnorlaxLOG -->
<dependency>
    <groupId>de.relaxogames</groupId>
    <artifactId>SnorlaxLOG</artifactId>
    <version>inDev-1.11.1</version>
</dependency>`
```

Or if you want to use it as a dependency in a gradle project import it like this:
```groovy
dependencies {
    implementation 'de.relaxogames:SnorlaxLOG:inDev-1.11.1'
}
```

## Installation

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
