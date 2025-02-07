package com.yubico.webauthn

import org.bouncycastle.jce.provider.BouncyCastleProvider
import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should.Matchers

import java.security.Provider
import java.security.Security

trait TestWithEachProvider extends Matchers {
  this: AnyFunSpec =>

  /** Run the `body` in a context with the given JCA [[Security]] providers,
    * then reset the providers to their state before.
    */
  def withProviderContext(
      providers: List[Provider]
  )(
      body: => Any
  ): Unit = {
    val originalProviders = Security.getProviders.toList
    Security.getProviders.foreach(prov => Security.removeProvider(prov.getName))
    providers.foreach(Security.addProvider)

    body

    Security.getProviders.foreach(prov => Security.removeProvider(prov.getName))
    originalProviders.foreach(Security.addProvider)
  }

  def wrapItFunctionWithProviderContext(
      providerSetName: String,
      providers: List[Provider],
      testSetupFun: (String => (=> Any) => Unit) => Any,
  ): Any = {

    /** Wrapper around the standard [[AnyFunSpec#it]] that sets the JCA
      * [[Security]] providers before running the test, and then resets the
      * providers to the original state after the test.
      *
      * This is needed because ScalaTest shared tests work by taking fixture
      * parameters as lexical context, but JCA providers are set in the dynamic
      * context. The [[AnyFunSpec#it]] call does not immediately run the test,
      * instead it registers a test to be run later. This helper ensures that
      * the dynamic context matches the lexical context at the time the test
      * runs.
      */
    def it(testName: String)(testFun: => Any): Unit = {
      this.it.apply(testName) {
        withProviderContext(providers)(testFun)
      }
    }

    describe(s"""With providers "${providerSetName}":""") {
      it(s"This test runs with the right security providers: ${providers}.") {
        Security.getProviders.toSet should equal(providers.toSet)
      }

      testSetupFun(it _)
    }
  }

  /** Register tests in a modified DSL environment where the `it` "keyword" is
    * modified to set the JCA [[Security]] providers before running the test,
    * and then reset the providers to the original state after the test.
    *
    * The caller SHOULD name the callback parameter `it`, in order to shadow the
    * standard [[AnyFunSpec#it]] from ScalaTest.
    *
    * This is needed because ScalaTest shared tests work by taking fixture
    * parameters as lexical context, but JCA providers are set in the dynamic
    * context. The [[AnyFunSpec#it]] call does not immediately run the test,
    * instead it registers a test to be run later. This helper ensures that the
    * dynamic context matches the lexical context at the time the test runs.
    */
  def testWithEachProvider(
      registerTests: (String => (=> Any) => Unit) => Any
  ): Unit = {
    val defaultProviders: List[Provider] = Security.getProviders.toList

    // TODO: Uncomment this in the next major version
    //it should behave like wrapItFunctionWithProviderContext("default", defaultProviders, registerTests)

    it should behave like wrapItFunctionWithProviderContext(
      "BouncyCastle",
      List(new BouncyCastleProvider()),
      registerTests,
    )

    // TODO: Delete this in the next major version
    it should behave like wrapItFunctionWithProviderContext(
      "default and BouncyCastle",
      defaultProviders.appended(new BouncyCastleProvider()),
      registerTests,
    )
  }

}
