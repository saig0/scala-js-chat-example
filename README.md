# Chat-Example with Scala.js and Play!

Scala.js on client side and Play! on server side. Client and server communicate over Web Sockets with JSON messages.

## Packages
The application contains three directories:
* `scalajvm` Play application (server side)
* `scalajs` Scala.js application (client side)
* `scala` scala code that you want to share between scalajs and scalajvm (both client and server sides)

## Run the application
```shell
$ sbt
> run
$ open http://localhost:9000
```