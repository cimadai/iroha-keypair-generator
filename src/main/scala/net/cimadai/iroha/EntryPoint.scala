package net.cimadai.iroha

import java.io.PrintWriter

case class Config(outputKeyName: String = "")


object EntryPoint extends App {
  import Iroha._

  val parser = new scopt.OptionParser[Config]("iroha-keypair-generator") {
    head("iroha-keypair-generator", "0.0.1")

    opt[String]('o', "output-key-name").action( (x, c) =>
      c.copy(outputKeyName = x)
    ).text("Output key pair name.").required()
  }

  def withWriter(fileName: String)(callback: (PrintWriter) => Unit): Unit = {
    val writer = new PrintWriter(fileName)
    try {
      callback(writer)
    } finally {
      writer.close()
    }
  }

  parser.parse(args, Config()) match {
    case Some(config) =>
      val keyPair = Iroha.createNewKeyPair()
      withWriter(config.outputKeyName + ".priv") { writer =>
        writer.write(keyPair.privateKey.toPrivateKeyHex)
      }

      withWriter(config.outputKeyName + ".pub") { writer =>
        writer.write(keyPair.privateKey.toPublicKeyHex)
      }
    case _ =>
  }

}
