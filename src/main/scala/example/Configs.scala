package example

import config.{Parameters, Config}
import testchipip.WithSerialAdapter
import coreplex.WithRoCC
import dma._

class DmaConfig extends Config(
  new WithDma ++ new WithRoCC ++ new DefaultExampleConfig)

class DefaultExampleConfig extends Config(
  new WithSerialAdapter ++ new rocketchip.DefaultConfig)
