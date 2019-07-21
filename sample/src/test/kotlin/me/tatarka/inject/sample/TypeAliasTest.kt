package me.tatarka.inject.sample

import assertk.assertAll
import assertk.assertThat
import assertk.assertions.isEqualTo
import me.tatarka.inject.annotations.Inject
import me.tatarka.inject.annotations.Module
import org.junit.Test

class NamedFoo(val name: String)

typealias NamedFoo1 = NamedFoo
typealias NamedFoo2 = NamedFoo

@Module abstract class ProvidesAliasedModule {
    abstract val foo1: NamedFoo1

    abstract val foo2: NamedFoo2

    fun foo1(): NamedFoo1 = NamedFoo("1")
    fun foo2(): NamedFoo2 = NamedFoo("2")

    companion object
}

@Inject class AliasedFoo(val foo1: NamedFoo1, val foo2: NamedFoo2)

@Module abstract class ConstructorAliasedModule {
    abstract val aliasedFoo: AliasedFoo

    fun foo1(): NamedFoo1 = NamedFoo("1")
    fun foo2(): NamedFoo2 = NamedFoo("2")

    companion object
}

class QualifierTest {

    @Test
    fun generates_a_module_that_provides_different_values_based_on_the_type_alias_name() {
        val module = ProvidesAliasedModule.create()

        assertAll {
            assertThat(module.foo1.name).isEqualTo("1")
            assertThat(module.foo2.name).isEqualTo("2")
        }
    }

    @Test
    fun generates_a_module_that_constructs_different_values_based_on_the_type_alias_name() {
        val module = ConstructorAliasedModule.create()

        assertAll {
            assertThat(module.aliasedFoo.foo1.name).isEqualTo("1")
            assertThat(module.aliasedFoo.foo2.name).isEqualTo("2")
        }
    }
}