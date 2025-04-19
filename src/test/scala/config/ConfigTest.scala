package config

import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should

class ConfigTest extends AnyFunSpec with should.Matchers {
  describe("fromTomlFile"):
    it("should be able to read from a file"):
      val configPath: String = getClass.getResource("/config.toml").getPath
      val configMap: Map[String, String] = Config.getMapFromToml(configPath, "section_1")

      configMap("item_1") shouldEqual "this"
      configMap("item_2") shouldEqual "that"

    it("should throw an exception if the section is not available"):
      val configPath: String = getClass.getResource("/config.toml").getPath

      an[NoSuchElementException] should be thrownBy Config.getMapFromToml(configPath, "section_3")
}
