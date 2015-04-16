/*
 * Copyright 2001-2015 Artima, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.scalactic

class LazyBagSpec extends UnitSpec {
  "LazyBag" should "offer a size method" in {
    LazyBag(1, 2, 3).size shouldBe 3
    LazyBag(1, 1, 3, 2).size shouldBe 4
    LazyBag(1, 1, 1, 1).size shouldBe 4
  }
  it should "have a pretty toString" in {
    def assertPretty[T](lazyBag: LazyBag[T]) = {
      val lbs = lazyBag.toString
      lbs should startWith ("LazyBag(")
      lbs should endWith (")")
      /*
      scala> lbs.replaceAll(""".*\((.*)\).*""", "$1")
      res0: String = 1,2,3

      scala> res0.split(',')
      res1: Array[String] = Array(1, 2, 3)

      scala> val lbs = "LazyBag()"
      lbs: String = LazyBag()

      scala> lbs.replaceAll(""".*\((.*)\).*""", "$1")
      res2: String = ""

      scala> res2.split(',')
      res3: Array[String] = Array("")
      */
      val elemStrings = lbs.replaceAll(""".*\((.*)\).*""", "$1")
      val elemStrArr = if (elemStrings.size != 0) elemStrings.split(',') else Array.empty[String]
      elemStrArr.size should equal (lazyBag.size)

      elemStrArr should contain theSameElementsAs lazyBag.toList.map(_.toString)
    }

    // Test BasicLazyBag
    assertPretty(LazyBag(1, 2, 3))
    assertPretty(LazyBag(1, 2, 3, 4))
    assertPretty(LazyBag(1))
    assertPretty(LazyBag())
    assertPretty(LazyBag("one", "two", "three", "four", "five"))

    // Test FlatMappedLazyBag
    val trimmed = EquaPath[String](StringNormalizations.trimmed.toHashingEquality)
    val lazySet = trimmed.EquaSet("1", "2", "01", "3").toLazy
    val flatMapped = lazySet.flatMap { (digit: String) =>
      LazyBag(digit.toInt)
    }
    assertPretty(flatMapped)
    val mapped = flatMapped.map(_ + 1)
    assertPretty(mapped)
  }
}

